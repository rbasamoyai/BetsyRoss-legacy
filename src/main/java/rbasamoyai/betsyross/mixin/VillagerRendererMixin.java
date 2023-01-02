package rbasamoyai.betsyross.mixin;

import net.minecraft.client.model.VillagerModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.VillagerRenderer;
import net.minecraft.world.entity.npc.Villager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import rbasamoyai.betsyross.flags.ArmorBannerLayer;

@Mixin(VillagerRenderer.class)
public abstract class VillagerRendererMixin extends MobRenderer<Villager, VillagerModel<Villager>> {

	public VillagerRendererMixin(EntityRendererProvider.Context context, VillagerModel<Villager> model, float shadowRadius) {
		super(context, model,  shadowRadius);
	}

	@Inject(method = "<init>", at = @At("TAIL"))
	public void betsyross$init(EntityRendererProvider.Context context, CallbackInfo ci) {
		this.addLayer(new ArmorBannerLayer<>(this, context.getItemInHandRenderer()));
	}

}
