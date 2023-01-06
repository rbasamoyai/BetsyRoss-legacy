package rbasamoyai.betsyross.flags;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.resources.model.Material;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import rbasamoyai.betsyross.BetsyRoss;
import rbasamoyai.betsyross.BetsyRossClient;
import rbasamoyai.betsyross.config.BetsyRossConfig;

import static rbasamoyai.betsyross.flags.FlagBlockEntityRenderer.renderFullTexture;

public class FlagStandardRenderer extends BlockEntityWithoutLevelRenderer {
	public static final Material STANDARD_FLAGPOLE = new Material(TextureAtlas.LOCATION_BLOCKS, BetsyRoss.path("item/standard_flagpole"));

	private final ModelPart flagpole;

	public FlagStandardRenderer(BlockEntityRenderDispatcher dispatcher, EntityModelSet models) {
		super(dispatcher, models);
		this.flagpole = models.bakeLayer(BetsyRossClient.ITEM_FLAGPOLE);
	}

	@Override
	public void renderByItem(ItemStack stack, ItemTransforms.TransformType transform, PoseStack posestack, MultiBufferSource buffers, int light, int overlay) {
		Minecraft mc = Minecraft.getInstance();

		CompoundTag flagData = stack.getOrCreateTag();
		BlockState state = BetsyRoss.FLAG_BLOCK.get().defaultBlockState();
		String url = flagData.getString("FlagUrl");

		posestack.pushPose();

		int width = flagData.getByte("Width");
		int height = flagData.getByte("Height");
		FlagAnimationDetail detail = BetsyRossConfig.CLIENT.animationDetail.get();

		float pt = mc.isPaused() ? mc.pausePartialTick : mc.getFrameTime();

		if (transform == ItemTransforms.TransformType.GUI) {
			width = 1;
			height = 1;
			detail = FlagAnimationDetail.NO_WAVE;
			pt = 1.0f;
		} else {
			this.flagpole.render(posestack, STANDARD_FLAGPOLE.buffer(buffers, RenderType::entitySolid), light, overlay);
			posestack.translate(.5, 3, 0);
		}

		float dir = transform == ItemTransforms.TransformType.GUI ? -90 : 0;

		posestack.translate(0, 1, 0.5);

		renderFullTexture(state, url, width, height, pt, dir, posestack, buffers, light, overlay, false, detail, true);
		renderFullTexture(state, url, width, height, pt, dir, posestack, buffers, light, overlay, true, detail, true);

		posestack.popPose();
	}

	public static LayerDefinition defineFlagpole() {
		MeshDefinition mesh = new MeshDefinition();
		mesh.getRoot().addOrReplaceChild("flagpole", CubeListBuilder.create()
				.texOffs(0, 0).addBox(7, 0, 7, 2, 16, 2)
				.texOffs(0, 0).addBox(7, 16, 7, 2, 16, 2)
				.texOffs(0, 0).addBox(7, 32, 7, 2, 16, 2)
				.texOffs(0, 0).addBox(7, 48, 7, 2, 16, 2), PartPose.ZERO);
		return LayerDefinition.create(mesh, 64, 64);
	}

}
