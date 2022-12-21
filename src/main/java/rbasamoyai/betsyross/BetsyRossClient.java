package rbasamoyai.betsyross;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import rbasamoyai.betsyross.flags.FlagBlockEntityRenderer;
import rbasamoyai.betsyross.flags.FlagItemRenderer;

public class BetsyRossClient {

    public static void onCtor(IEventBus modBus, IEventBus forgeBus) {
        modBus.addListener(BetsyRossClient::onClientSetup);
        modBus.addListener(BetsyRossClient::onRendererRegistry);
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

}
