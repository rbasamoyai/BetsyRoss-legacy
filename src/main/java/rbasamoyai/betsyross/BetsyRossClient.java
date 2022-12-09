package rbasamoyai.betsyross;

import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import rbasamoyai.betsyross.flags.FlagBlockEntityRenderer;

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

}
