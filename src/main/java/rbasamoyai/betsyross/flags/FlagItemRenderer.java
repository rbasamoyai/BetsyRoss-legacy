package rbasamoyai.betsyross.flags;

import com.mojang.blaze3d.vertex.PoseStack;
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

		float dir = transform == ItemTransforms.TransformType.GUI ? 90 : 0;

		posestack.translate(0, 1, 0.5);

		// TODO: hand transform

		renderFullTexture(state, url, 1, 1, 1, dir, posestack, buffers, light, overlay, false, FlagAnimationDetail.NO_WAVE, true);
		renderFullTexture(state, url, 1, 1, 1, dir, posestack, buffers, light, overlay, true, FlagAnimationDetail.NO_WAVE, true);

		posestack.popPose();
	}

}
