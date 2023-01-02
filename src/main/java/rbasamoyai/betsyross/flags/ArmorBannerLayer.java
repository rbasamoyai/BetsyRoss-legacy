package rbasamoyai.betsyross.flags;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.CustomHeadLayer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.ZombieVillager;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.item.ItemStack;
import rbasamoyai.betsyross.BetsyRoss;

public class ArmorBannerLayer<T extends LivingEntity, M extends EntityModel<T>> extends RenderLayer<T, M> {

	private final float scaleX;
	private final float scaleY;
	private final float scaleZ;
	private final ItemInHandRenderer itemRenderer;

	public ArmorBannerLayer(RenderLayerParent<T, M> parent, ItemInHandRenderer itemRenderer) {
		this(parent, 1, 1, 1, itemRenderer);
	}

	public ArmorBannerLayer(RenderLayerParent<T, M> parent, float scaleX, float scaleY, float scaleZ, ItemInHandRenderer itemRenderer) {
		super(parent);
		this.scaleX = scaleX;
		this.scaleY = scaleY;
		this.scaleZ = scaleZ;
		this.itemRenderer = itemRenderer;
	}

	@Override
	public void render(PoseStack posestack, MultiBufferSource buffers, int light, T entity,
					   float animPos, float animSpeed, float partialTicks, float bobbing, float yawDiff, float xRot) {
		EntityModel<T> model = this.getParentModel();
		ItemStack stack = entity.getItemBySlot(EquipmentSlot.HEAD);
		if (!stack.is(BetsyRoss.ARMOR_BANNER.get())) return;

		posestack.pushPose();
		posestack.scale(this.scaleX, this.scaleY, this.scaleZ);

		boolean isVillager = entity instanceof Villager || entity instanceof ZombieVillager;
		if (entity.isBaby() && !(entity instanceof Villager)) {
			posestack.translate(0.0F, 0.03125F, 0.0F);
			posestack.scale(0.7F, 0.7F, 0.7F);
			posestack.translate(0.0F, 1.0F, 0.0F);
		}

		CustomHeadLayer.translateToHead(posestack, isVillager);
		this.itemRenderer.renderItem(entity, stack, ItemTransforms.TransformType.HEAD, false, posestack, buffers, light);

		posestack.popPose();
	}

}
