package rbasamoyai.betsyross.mixin;

import net.minecraft.client.model.VillagerModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.WanderingTraderRenderer;
import net.minecraft.world.entity.npc.WanderingTrader;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import rbasamoyai.betsyross.flags.ArmorBannerLayer;

@Mixin(WanderingTraderRenderer.class)
public abstract class WanderingTraderRendererMixin extends MobRenderer<WanderingTrader, VillagerModel<WanderingTrader>> {

	public WanderingTraderRendererMixin(EntityRendererProvider.Context context, VillagerModel<WanderingTrader> model, float shadowRadius) {
		super(context, model,  shadowRadius);
	}

	@Inject(method = "<init>", at = @At("TAIL"))
	public void betsyross$init(EntityRendererProvider.Context context, CallbackInfo ci) {
		this.addLayer(new ArmorBannerLayer<>(this, context.getItemInHandRenderer()));
	}

}
