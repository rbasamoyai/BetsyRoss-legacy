package rbasamoyai.betsyross.flags;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.client.renderer.texture.MissingTextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.RotationSegment;
import net.minecraftforge.client.model.data.ModelData;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import rbasamoyai.betsyross.BetsyRoss;
import rbasamoyai.betsyross.config.BetsyRossConfig;

public class FlagBlockEntityRenderer implements BlockEntityRenderer<FlagBlockEntity> {

	private final BlockRenderDispatcher brd;

	public FlagBlockEntityRenderer(BlockEntityRendererProvider.Context ctx) {
		this.brd = ctx.getBlockRenderDispatcher();
	}

	@Override
	public void render(FlagBlockEntity flag, float partialTicks, PoseStack stack, MultiBufferSource buffers, int packedLight, int packedOverlay) {
		int w = flag.getFlagWidth();
		int h = flag.getFlagHeight();
		if (w <= 0 || h <= 0) return;
		String url = flag.getFlagUrl();
		BlockState state = flag.getBlockState();

		stack.pushPose();

		if (state.is(BetsyRoss.FLAG_BLOCK.get())) {
			float dir = RotationSegment.convertToDegrees(state.getValue(FlagBlock.ROTATION));
			FlagAnimationDetail detail = BetsyRossConfig.CLIENT.animationDetail.get();

			stack.pushPose();
			stack.translate(0.5, h, 0.5);
			renderFullTexture(state, url, w, h, partialTicks, dir, stack, buffers, packedLight, packedOverlay, false, detail, false);
			renderFullTexture(state, url, w, h, partialTicks, dir, stack, buffers, packedLight, packedOverlay, true, detail, false);
			stack.popPose();

			BlockState flagpole = flag.getFlagPole();
			if (flagpole != null) {
				this.brd.renderSingleBlock(flagpole, stack, buffers, packedLight, packedOverlay, ModelData.EMPTY, null);
			}
		} else if (state.is(BetsyRoss.DRAPED_FLAG_BLOCK.get())) {
			Direction dir = state.getValue(DrapedFlagBlock.FACING);
			Direction dir1 = dir.getAxis() == Direction.Axis.X ? dir.getCounterClockWise() : dir.getClockWise();
			float f = dir1.toYRot();
			stack.translate(0, 1, 0);

			if (dir == Direction.WEST || dir == Direction.NORTH) stack.translate(1, 0, 0);
			if (dir == Direction.NORTH || dir == Direction.EAST) stack.translate(0, 0, 1);

			renderFullTexture(state, url, w, h, partialTicks, f, stack, buffers, packedLight, packedOverlay, false, FlagAnimationDetail.NO_WAVE, false);
			renderFullTexture(state, url, w, h, partialTicks, f, stack, buffers, packedLight, packedOverlay, true, FlagAnimationDetail.NO_WAVE, false);
		}

		stack.popPose();
	}

	public static void renderFullTexture(BlockState state, String url, int width, int height, float partialTicks, float dir, PoseStack stack, MultiBufferSource buffers, int packedLight, int packedOverlay, boolean flip, FlagAnimationDetail detail, boolean isItem) {
		if (width <= 0 || height <= 0) return;
		stack.pushPose();

		Vector3f v3f = new Vector3f(0, 0, 0);
		v3f.mulTransposePosition(stack.last().pose());
		stack.translate(-v3f.x(), -v3f.y(), -v3f.z());

		stack.mulPose(Axis.YP.rotationDegrees(dir - 90));
		stack.mulPose(Axis.XP.rotationDegrees(180));

		stack.translate(v3f.x(), v3f.y(), v3f.z());

		stack.translate(0, 0, flip ? -0.01 : 0.01);

		VertexConsumer vcons = buffers.getBuffer(getFlagBuffer(url));
		switch (detail) {
			case NO_WAVE -> renderSimple(vcons, stack, width, height, packedLight, packedOverlay, flip, isItem);
			case WAVE -> renderWaveSimple(vcons, stack, partialTicks, width, height, packedLight, packedOverlay, flip);
		}

		stack.popPose();
	}

	private static void renderSimple(VertexConsumer vcons, PoseStack stack, int w, int h, int light, int overlay, boolean flip, boolean isItem) {
		float nx = 0;
		float ny = isItem ? 1 : 0;
		float nz = isItem ? 0 : flip ? 1 : -1;

		if (!flip) {
			Vector3f v3f1 = new Vector3f(0, 0, 0);
			v3f1.mulTransposePosition(stack.last().pose());
			stack.translate(-v3f1.x(), -v3f1.y(), -v3f1.z());
			stack.mulPose(Axis.YP.rotationDegrees(180));
			stack.translate(v3f1.x() - w, v3f1.y(), v3f1.z());
		}

		Matrix4f pose = stack.last().pose();

		vcons.vertex(pose, 0, 0, 0)
				.color(255, 255, 255, 255)
				.uv(flip ? 0 : 1, 0)
				.overlayCoords(overlay)
				.uv2(light)
				.normal(nx, ny, nz)
				.endVertex();

		vcons.vertex(pose, 0, h, 0)
				.color(255, 255, 255, 255)
				.uv(flip ? 0 : 1, 1)
				.overlayCoords(overlay)
				.uv2(light)
				.normal(nx, ny, nz)
				.endVertex();

		vcons.vertex(pose, w, h, 0)
				.color(255, 255, 255, 255)
				.uv(flip ? 1 : 0, 1)
				.overlayCoords(overlay)
				.uv2(light)
				.normal(nx, ny, nz)
				.endVertex();

		vcons.vertex(pose, w, 0, 0)
				.color(255, 255, 255, 255)
				.uv(flip ? 1 : 0, 0)
				.overlayCoords(overlay)
				.uv2(light)
				.normal(nx, ny, nz)
				.endVertex();
	}

	private static void renderWaveSimple(VertexConsumer vcons, PoseStack stack, float partialTicks, float w, float h, int light, int overlay, boolean flip) {
		Minecraft mc = Minecraft.getInstance();

		float sample = 1;
		float freq = Mth.PI / 8f;
		float coAmp = 1 / 2f / (w <= 1e-2f ? 1 : w);
		int sz = Mth.ceil(w * sample) + 1;
		float phaseOffs = mc.level == null ? 0 : (float)(mc.level.getGameTime() % 16) + partialTicks;

		float[] horizDisp = new float[sz];
		float sampleRec = 1 / sample;
		for (int i = 0; i < sz; ++i) {
			horizDisp[i] = Mth.sin(freq * (i * sampleRec - phaseOffs)) * coAmp * i * sampleRec;
		}

		Vector3f[] normals = new Vector3f[sz];
		for (int i = 0 ; i < sz; ++i) {
			if (i == 0) normals[i] = new Vector3f(sampleRec, 0, i + 1 == sz ? 0 : -horizDisp[i]).normalize(); // 90 deg rotation ccw
			else if (i + 1 == sz) normals[i] = new Vector3f(sampleRec, 0, horizDisp[i]).normalize(); // 90 deg rotation cw
			else { // https://math.stackexchange.com/a/2285989 for algorithm
				Vector3f vec1 = new Vector3f(horizDisp[i - 1], 0, -sampleRec);
				float len1 = vec1.length();
				Vector3f vec2 = new Vector3f(horizDisp[i + 1], 0, sampleRec);
				float len2 = vec2.length();
				Vector3f vec3 = vec1.mul(len2).add(vec2.mul(len1));
				if (vec3.lengthSquared() <= 1e-4d) vec3 = new Vector3f(sampleRec, 0, -horizDisp[i]);
				if (!flip) vec3.mul(-1);
				normals[i] = vec3.normalize();
			}
		}

		if (!flip) {
			Vector3f v3f1 = new Vector3f(0, 0, 0);
			v3f1.mulTransposePosition(stack.last().pose());
			stack.translate(-v3f1.x(), -v3f1.y(), -v3f1.z());
			stack.translate(v3f1.x() + w, v3f1.y(), v3f1.z());
		}

		Matrix4f pose = stack.last().pose();

		float f = sz <= 2 ? 1 : 1f / (sz - 1);
		float ulen = w * f;
		for (int i = 0; i < sz - 1; ++i) {
			Vector3f n1 = normals[i];
			Vector3f n2 = normals[i + 1];

			float u1 = flip ? 0 + i * f : 1 - i * f;
			float u2 = flip ? u1 + f : u1 - f;
			float w1 = flip ? ulen * i : ulen * -i;
			float w2 = flip ? w1 + ulen : w1 - ulen;
			float z1 = horizDisp[flip ? i : sz - i - 1];
			float z2 = horizDisp[flip ? i + 1 : sz - i - 2];

			vcons.vertex(pose, w1, 0, z1)
					.color(255, 255, 255, 255)
					.uv(u1, 0)
					.overlayCoords(overlay)
					.uv2(light)
					.normal(n1.x, n1.y, n1.z)
					.endVertex();

			vcons.vertex(pose, w1, h, z1)
					.color(255, 255, 255, 255)
					.uv(u1, 1)
					.overlayCoords(overlay)
					.uv2(light)
					.normal(n1.x, n1.y, n1.z)
					.endVertex();

			vcons.vertex(pose, w2, h, z2)
					.color(255, 255, 255, 255)
					.uv(u2, 1)
					.overlayCoords(overlay)
					.uv2(light)
					.normal(n2.x, n2.y, n2.z)
					.endVertex();

			vcons.vertex(pose, w2, 0, z2)
					.color(255, 255, 255, 255)
					.uv(u2, 0)
					.overlayCoords(overlay)
					.uv2(light)
					.normal(n2.x, n2.y, n2.z)
					.endVertex();
		}
	}

	public static RenderType getFlagBuffer(String url) {
		if ("betsyrosslogo".equals(url))
			return RenderType.entityTranslucentCull(BetsyRoss.path("textures/block/logo.png"));
		if (url == null || url.length() == 0)
			return RenderType.entityTranslucentCull(BetsyRoss.path("textures/block/default.png"));
		ResourceLocation loc = FlagTexture.textureId(url);
		TextureManager manager = Minecraft.getInstance().getTextureManager();
		AbstractTexture tex = manager.getTexture(loc, MissingTextureAtlasSprite.getTexture());
		if (tex == MissingTextureAtlasSprite.getTexture()) manager.register(loc, new FlagTexture(url));
		return RenderType.entityTranslucentCull(loc);
	}

}