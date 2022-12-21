package rbasamoyai.betsyross.flags;

import com.google.common.hash.Hashing;
import com.mojang.blaze3d.platform.NativeImage;
import com.mojang.blaze3d.platform.TextureUtil;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.logging.LogUtils;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.MissingTextureAtlasSprite;
import net.minecraft.client.renderer.texture.SimpleTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import org.slf4j.Logger;
import rbasamoyai.betsyross.BetsyRoss;

import javax.annotation.Nullable;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.CompletableFuture;

public class FlagTexture extends SimpleTexture {

    private static final Logger LOGGER = LogUtils.getLogger();

    private final String url;
    private boolean loaded;
    private CompletableFuture<?> future;

    public FlagTexture(String url) {
        super(MissingTextureAtlasSprite.getLocation());
        this.url = url;
    }

    @SuppressWarnings({"deprecated", "UnstableApiUsage"})
    public static ResourceLocation textureId(String url) {
        return BetsyRoss.path("flag_textures/" + Hashing.sha1().hashUnencodedChars(url));
    }

    @Override
    public void load(ResourceManager manager) throws IOException {
        Minecraft.getInstance().execute(() -> {
            if (!this.loaded) {
                try {
                    super.load(manager);
                } catch (IOException ioex) {
                    LOGGER.warn("Failed to load online flag texture: {}", this.url, ioex);
                }
                this.loaded = true;
            }
        });

        if (this.future != null) return;
        this.future = CompletableFuture.runAsync(() -> {
            HttpURLConnection connection = null;
            try {
                connection = (HttpURLConnection) new URL(this.url).openConnection(Minecraft.getInstance().getProxy());
                connection.setDoInput(true);
                connection.setDoOutput(false);
                connection.connect();
                if (connection.getResponseCode() / 100 == 2) {
                    NativeImage image = this.loadImage(connection.getInputStream());
                    if (image != null) {
                        Minecraft.getInstance().execute(() -> {
                            this.loaded = true;
                            if (!RenderSystem.isOnRenderThread()) {
                                RenderSystem.recordRenderCall(() -> this.upload(image));
                            } else {
                                this.upload(image);
                            }
                        });
                    }
                }
            } catch (Exception e) {
                LOGGER.warn("Couldn't download online flag texture: {}", this.url, e);
            } finally {
                if (connection != null) connection.disconnect();
            }
        }, Util.backgroundExecutor());
    }

    private void upload(NativeImage nativeImg) {
        TextureUtil.prepareImage(this.getId(), nativeImg.getWidth(), nativeImg.getHeight());
        nativeImg.upload(0, 0, 0, true);
    }

    @Nullable
    private NativeImage loadImage(InputStream stream) {
        NativeImage image = null;
        try {
            image = NativeImage.read(stream);
        } catch (Exception e) {
            LOGGER.warn("Error while loading downloaded flag texture: {}", this.url, e);
        }
        return image;
    }

}
