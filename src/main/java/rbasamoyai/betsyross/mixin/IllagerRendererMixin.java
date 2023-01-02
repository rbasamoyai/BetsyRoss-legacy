package rbasamoyai.betsyross.mixin;

import net.minecraft.client.model.IllagerModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.IllagerRenderer;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.world.entity.monster.AbstractIllager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import rbasamoyai.betsyross.flags.ArmorBannerLayer;

@Mixin(IllagerRenderer.class)
public abstract class IllagerRendererMixin<T extends AbstractIllager> extends MobRenderer<T, IllagerModel<T>> {

	public IllagerRendererMixin(EntityRendererProvider.Context context, IllagerModel<T> model, float shadowRadius) {
		super(context, model, shadowRadius);
	}

	@Inject(method = "<init>", at = @At("TAIL"))
	public void betsyross$init(EntityRendererProvider.Context context, IllagerModel<T> model, float shadowRadius, CallbackInfo ci) {
		this.addLayer(new ArmorBannerLayer<>(this, context.getItemInHandRenderer()));
	}

}
