package rbasamoyai.betsyross.flags;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import rbasamoyai.betsyross.BetsyRoss;

import static rbasamoyai.betsyross.flags.FlagBlockEntityRenderer.renderFullTexture;

public class FlagItemRenderer extends BlockEntityWithoutLevelRenderer {

	public FlagItemRenderer(BlockEntityRenderDispatcher dispatcher, EntityModelSet models) {
		super(dispatcher, models);
	}

	@Override
	public void renderByItem(ItemStack stack, ItemTransforms.TransformType transform, PoseStack posestack, MultiBufferSource buffers, int light, int overlay) {
		CompoundTag flagData = stack.getOrCreateTag().getCompound("BlockEntityTag");
		BlockState state = BetsyRoss.FLAG_BLOCK.get().defaultBlockState();
		String url = flagData.getString("FlagUrl");

		posestack.pushPose();

		if (transform == ItemTransforms.TransformType.GUI) {
			posestack.mulPose(Axis.YP.rotationDegrees(-90));
			posestack.translate(0, 0, -0.5);
		}

		renderFullTexture(state, url, 1, 1, 1, posestack, buffers, light, overlay, false);
		renderFullTexture(state, url, 1, 1, 1, posestack, buffers, light, overlay, true);

		posestack.popPose();
	}

}
