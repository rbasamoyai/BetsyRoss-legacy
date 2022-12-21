package rbasamoyai.betsyross.flags;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.client.renderer.texture.MissingTextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.RotationSegment;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import rbasamoyai.betsyross.BetsyRoss;

public class FlagBlockEntityRenderer implements BlockEntityRenderer<FlagBlockEntity> {

	public FlagBlockEntityRenderer(BlockEntityRendererProvider.Context ctx) {}

	@Override
	public void render(FlagBlockEntity flag, float partialTicks, PoseStack stack, MultiBufferSource buffers, int packedLight, int packedOverlay) {
		if (flag.getFlagWidth() <= 0 || flag.getFlagHeight() <= 0) return;
		String url = flag.getFlagUrl();
		renderFullTexture(flag, partialTicks, stack, buffers, packedLight, packedOverlay, false);
		renderFullTexture(flag, partialTicks, stack, buffers, packedLight, packedOverlay, true);
	}

	private static void renderFullTexture(FlagBlockEntity flag, float partialTicks, PoseStack stack, MultiBufferSource buffers, int packedLight, int packedOverlay, boolean flip) {
		renderFullTexture(flag.getBlockState(), flag.getFlagUrl(), flag.getFlagWidth(), flag.getFlagHeight(), partialTicks, stack, buffers, packedLight, packedOverlay, flip);
	}

	public static void renderFullTexture(BlockState state, String url, int width, int height, float partialTicks, PoseStack stack, MultiBufferSource buffers, int packedLight, int packedOverlay, boolean flip) {
		stack.pushPose();

		stack.translate(0.5, height, 0.5);

		Vector3f v3f = new Vector3f(0, 0, 0);
		v3f.mulTransposePosition(stack.last().pose());
		stack.translate(-v3f.x(), -v3f.y(), -v3f.z());

		float dir = RotationSegment.convertToDegrees(state.getValue(FlagBlock.ROTATION));

		stack.mulPose(Axis.YP.rotationDegrees(dir - 90));
		stack.mulPose(Axis.XP.rotationDegrees(180));

		stack.translate(v3f.x(), v3f.y(), v3f.z());

		stack.translate(0, 0, flip ? -0.01 : 0.01);

		float dir1 = dir;

		if (!flip) {
			Vector3f v3f1 = new Vector3f(0, 0, 0);
			v3f1.mulTransposePosition(stack.last().pose());
			stack.translate(-v3f1.x(), -v3f1.y(), -v3f1.z());
			stack.mulPose(Axis.YP.rotationDegrees(180));
			stack.translate(v3f1.x() - width, v3f1.y(), v3f1.z());
		} else {
			dir1 = dir1 + 180;
		}

		float nx = Mth.sin(dir1 * Mth.DEG_TO_RAD);
		float ny = 0;
		float nz = Mth.cos(dir1 * Mth.DEG_TO_RAD);

		VertexConsumer vcons = buffers.getBuffer(getFlagBuffer(url));
		Matrix4f pose = stack.last().pose();
		vcons.vertex(pose, 0, 0, 0)
				.color(255, 255, 255, 255)
				.uv(flip ? 0 : 1, 0)
				.overlayCoords(packedOverlay)
				.uv2(packedLight)
				.normal(nx, ny, nz)
				.endVertex();

		vcons.vertex(pose, 0, height, 0)
				.color(255, 255, 255, 255)
				.uv(flip ? 0 : 1, 1)
				.overlayCoords(packedOverlay)
				.uv2(packedLight)
				.normal(nx, ny, nz)
				.endVertex();

		vcons.vertex(pose, width, height, 0)
				.color(255, 255, 255, 255)
				.uv(flip ? 1 : 0, 1)
				.overlayCoords(packedOverlay)
				.uv2(packedLight)
				.normal(nx, ny, nz)
				.endVertex();

		vcons.vertex(pose, width, 0, 0)
				.color(255, 255, 255, 255)
				.uv(flip ? 1 : 0, 0)
				.overlayCoords(packedOverlay)
				.uv2(packedLight)
				.normal(nx, ny, nz)
				.endVertex();

		stack.popPose();
	}

	public static RenderType getFlagBuffer(String url) {
		if (url == null || url.length() == 0)
			return RenderType.entityTranslucentCull(BetsyRoss.path("textures/block/default.png"));
		ResourceLocation loc = FlagTexture.textureId(url);
		TextureManager manager = Minecraft.getInstance().getTextureManager();
		AbstractTexture tex = manager.getTexture(loc, MissingTextureAtlasSprite.getTexture());
		if (tex == MissingTextureAtlasSprite.getTexture()) manager.register(loc, new FlagTexture(url));
		return RenderType.entityTranslucentCull(loc);
	}

}