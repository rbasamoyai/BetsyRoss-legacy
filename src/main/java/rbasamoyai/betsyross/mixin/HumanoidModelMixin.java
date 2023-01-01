package rbasamoyai.betsyross.mixin;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import rbasamoyai.betsyross.BetsyRoss;

@Mixin(HumanoidModel.class)
public class HumanoidModelMixin {

	@Inject(method = "setupAnim", at = @At("TAIL"))
	public <T extends LivingEntity> void betsyross$setupAnimTail(T entity, float f, float g, float h, float i, float j, CallbackInfo ci) {
		HumanoidModel<?> self = (HumanoidModel<?>) (Object) this;

		boolean flag = entity.getMainArm() == HumanoidArm.LEFT;
		boolean flag1 = entity.isUsingItem();
		float sign = flag == flag1 ? 1 : -1;

		ModelPart poleArm = flag ? self.leftArm : self.rightArm;
		ModelPart otherArm = flag ? self.rightArm : self.leftArm;

		ItemStack main = entity.getMainHandItem();
		ItemStack off = entity.getOffhandItem();

		if (main.is(BetsyRoss.FLAG_STANDARD.get()) && !off.is(BetsyRoss.FLAG_STANDARD.get())) {
			poleArm.xRot = -1.7f;
			poleArm.yRot = -0.6f * sign;

			otherArm.xRot = -1f;
			otherArm.yRot = 0.5f * sign;
		}
	}

}
