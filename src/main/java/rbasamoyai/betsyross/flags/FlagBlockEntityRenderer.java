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
import org.joml.Matrix4f;
import org.joml.Vector3f;

public class FlagBlockEntityRenderer implements BlockEntityRenderer<FlagBlockEntity> {

	public FlagBlockEntityRenderer(BlockEntityRendererProvider.Context ctx) {}

	@Override
	public void render(FlagBlockEntity flag, float partialTicks, PoseStack stack, MultiBufferSource buffers, int packedLight, int packedOverlay) {
		if (flag.getFlagWidth() <= 0 || flag.getFlagHeight() <= 0) return;
		String url = flag.getFlagUrl();
		if (url != null && url.length() > 0) {
			renderFullTexture(flag, partialTicks, stack, buffers, packedLight, packedOverlay, false);
			renderFullTexture(flag, partialTicks, stack, buffers, packedLight, packedOverlay, true);
		}
	}

	private static void renderFullTexture(FlagBlockEntity flag, float partialTicks, PoseStack stack, MultiBufferSource buffers, int packedLight, int packedOverlay, boolean flip) {
		stack.pushPose();

		stack.translate(0.5, flag.getFlagHeight(), 0.5);

		Vector3f v3f = new Vector3f(0, 0, 0);
		v3f.mulTransposePosition(stack.last().pose());
		stack.translate(-v3f.x(), -v3f.y(), -v3f.z());

		stack.mulPose(Axis.YP.rotationDegrees(-flag.getBlockState().getValue(FlagBlock.FACING).toYRot() - 90));
		stack.mulPose(Axis.XP.rotationDegrees(180));

		stack.translate(v3f.x(), v3f.y(), v3f.z());

		stack.translate(0, 0, flip ? -0.01 : 0.01);

		if (!flip) {
			Vector3f v3f1 = new Vector3f(0, 0, 0);
			v3f1.mulTransposePosition(stack.last().pose());
			stack.translate(-v3f1.x(), -v3f1.y(), -v3f1.z());
			stack.mulPose(Axis.YP.rotationDegrees(180));
			stack.translate(v3f1.x() - flag.getFlagWidth(), v3f1.y(), v3f1.z());
		}

		VertexConsumer vcons = buffers.getBuffer(getFlagBuffer(flag.getFlagUrl()));
		Matrix4f pose = stack.last().pose();
		vcons.vertex(pose, 0, 0, 0)
                .color(255, 255, 255, 255)
                .uv(flip ? 0 : 1, 0)
                .overlayCoords(packedOverlay)
                .uv2(packedLight)
                .normal(0, 0, 0)
                .endVertex();

        vcons.vertex(pose, 0, flag.getFlagHeight(), 0)
                .color(255, 255, 255, 255)
                .uv(flip ? 0 : 1, 1)
                .overlayCoords(packedOverlay)
                .uv2(packedLight)
                .normal(0, 0, 0)
                .endVertex();

        vcons.vertex(pose, flag.getFlagWidth(), flag.getFlagHeight(), 0)
                .color(255, 255, 255, 255)
                .uv(flip ? 1 : 0, 1)
                .overlayCoords(packedOverlay)
                .uv2(packedLight)
                .normal(0, 0, 0)
                .endVertex();

        vcons.vertex(pose, flag.getFlagWidth(), 0, 0)
                .color(255, 255, 255, 255)
                .uv(flip ? 1 : 0, 0)
                .overlayCoords(packedOverlay)
                .uv2(packedLight)
                .normal(0, 0, 0)
                .endVertex();

		stack.popPose();
	}

	public static RenderType getFlagBuffer(String url) {
		ResourceLocation loc = FlagTexture.textureId(url);
		TextureManager manager = Minecraft.getInstance().getTextureManager();
		AbstractTexture tex = manager.getTexture(loc, MissingTextureAtlasSprite.getTexture());
		if (tex == MissingTextureAtlasSprite.getTexture()) manager.register(loc, new FlagTexture(url));
		return RenderType.entityTranslucentCull(loc);
	}

}
