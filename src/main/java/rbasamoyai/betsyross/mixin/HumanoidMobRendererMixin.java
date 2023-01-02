package rbasamoyai.betsyross.mixin;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.world.entity.Mob;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import rbasamoyai.betsyross.flags.ArmorBannerLayer;

@Mixin(HumanoidMobRenderer.class)
public abstract class HumanoidMobRendererMixin<T extends Mob, M extends HumanoidModel<T>> extends LivingEntityRenderer<T, M> {

	public HumanoidMobRendererMixin(EntityRendererProvider.Context context, M model, float shadowRadius) {
		super(context, model, shadowRadius);
	}

	@Inject(method = "<init>(Lnet/minecraft/client/renderer/entity/EntityRendererProvider$Context;Lnet/minecraft/client/model/HumanoidModel;FFFF)V", at = @At("TAIL"))
	public void betsyross$init(EntityRendererProvider.Context context, M model,
							   float shadowRadius, float scaleX, float scaleY, float scaleZ, CallbackInfo ci) {
		this.addLayer(new ArmorBannerLayer<>(this, scaleX, scaleY, scaleZ, context.getItemInHandRenderer()));
	}

}
