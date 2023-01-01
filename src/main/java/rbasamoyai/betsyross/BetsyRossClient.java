package rbasamoyai.betsyross;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import rbasamoyai.betsyross.flags.FlagBlockEntityRenderer;
import rbasamoyai.betsyross.flags.FlagItemRenderer;
import rbasamoyai.betsyross.flags.FlagStandardRenderer;

import java.util.HashSet;
import java.util.Set;

public class BetsyRossClient {

    public static void onCtor(IEventBus modBus, IEventBus forgeBus) {
        modBus.addListener(BetsyRossClient::onClientSetup);
        modBus.addListener(BetsyRossClient::onRendererRegistry);
        modBus.addListener(BetsyRossClient::onRegisterModelLayers);
    }

    public static void onClientSetup(FMLClientSetupEvent evt) {
        evt.enqueueWork(() -> {
            
        });
    }

    public static void onRendererRegistry(EntityRenderersEvent.RegisterRenderers evt) {
        evt.registerBlockEntityRenderer(BetsyRoss.FLAG_BLOCK_ENTITY.get(), FlagBlockEntityRenderer::new);
    }

    private static BlockEntityWithoutLevelRenderer FLAG_ITEM_RENDERER;
    public static BlockEntityWithoutLevelRenderer getFlagItemRenderer() {
        if (FLAG_ITEM_RENDERER == null) {
            Minecraft mc = Minecraft.getInstance();
            FLAG_ITEM_RENDERER = new FlagItemRenderer(mc.getBlockEntityRenderDispatcher(), mc.getEntityModels());
        }
        return FLAG_ITEM_RENDERER;
    }

    private static BlockEntityWithoutLevelRenderer FLAG_STANDARD_RENDERER;
    public static BlockEntityWithoutLevelRenderer getFlagStandardRenderer() {
        if (FLAG_STANDARD_RENDERER == null) {
            Minecraft mc = Minecraft.getInstance();
            FLAG_STANDARD_RENDERER = new FlagStandardRenderer(mc.getBlockEntityRenderDispatcher(), mc.getEntityModels());
        }
        return FLAG_STANDARD_RENDERER;
    }

    private static final Set<ModelLayerLocation> ALL_LAYERS = new HashSet<>();
    public static final ModelLayerLocation ITEM_FLAGPOLE = registerLayer("item_flagpole");

    private static ModelLayerLocation registerLayer(String id) {
        ModelLayerLocation loc = new ModelLayerLocation(BetsyRoss.path(id), "main");
        if (!ALL_LAYERS.add(loc)) {
            throw new IllegalStateException("Duplicate registration for " + loc);
        }
        return loc;
    }

    public static void onRegisterModelLayers(EntityRenderersEvent.RegisterLayerDefinitions evt) {
        evt.registerLayerDefinition(ITEM_FLAGPOLE, FlagStandardRenderer::defineFlagpole);
    }

}
