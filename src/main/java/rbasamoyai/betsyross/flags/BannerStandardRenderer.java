package rbasamoyai.betsyross.flags;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Vector3f;
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
import net.minecraft.client.resources.model.Material;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import rbasamoyai.betsyross.BetsyRoss;
import rbasamoyai.betsyross.BetsyRossClient;

import static rbasamoyai.betsyross.flags.FlagBlockEntityRenderer.renderFullTexture;

public class BannerStandardRenderer extends BlockEntityWithoutLevelRenderer {
	public static final Material STANDARD_FLAGPOLE = FlagStandardRenderer.STANDARD_FLAGPOLE;

	private final ModelPart flagpole;
	private final ModelPart bar;

	public BannerStandardRenderer(BlockEntityRenderDispatcher dispatcher, EntityModelSet models) {
		super(dispatcher, models);
		this.flagpole = models.bakeLayer(BetsyRossClient.ITEM_FLAGPOLE);
		this.bar = models.bakeLayer(BetsyRossClient.ITEM_BANNER);
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

		float pt = mc.isPaused() ? mc.pausePartialTick : mc.getFrameTime();

		if (transform == ItemTransforms.TransformType.GUI) {
			width = 1;
			height = 1;
			pt = 1.0f;
			posestack.translate(0, 1, 0);
		} else {
			VertexConsumer vcons = STANDARD_FLAGPOLE.buffer(buffers, RenderType::entitySolid);
			this.flagpole.render(posestack, vcons, light, overlay);
			this.bar.render(posestack,vcons, light, overlay);

			posestack.translate(0, 1, 0.5);

			float freq = Mth.PI / 75f;
			float phaseOffs = mc.level == null ? 0 : (float)(mc.level.getGameTime() % 150) + pt;
			float c = Mth.sin(-phaseOffs * freq);

			posestack.mulPose(Vector3f.YP.rotationDegrees(180));

			posestack.translate(width > 1 ? -width * 0.75f : 0, 3.125, 1 / 16f);

			posestack.mulPose(Vector3f.XP.rotationDegrees(-5 * c * c));
		}

		renderFullTexture(state, url, width, height, pt, -90, posestack, buffers, light, overlay, false, FlagAnimationDetail.NO_WAVE, true);
		renderFullTexture(state, url, width, height, pt, -90, posestack, buffers, light, overlay, true, FlagAnimationDetail.NO_WAVE, true);

		posestack.popPose();
	}

	public static LayerDefinition defineBannerBar() {
		MeshDefinition mesh = new MeshDefinition();
		mesh.getRoot().addOrReplaceChild("bar", CubeListBuilder.create()
				.texOffs(0, 18).addBox(-2, 64, 7, 20, 2, 2), PartPose.ZERO);
		return LayerDefinition.create(mesh, 64, 64);
	}

}
