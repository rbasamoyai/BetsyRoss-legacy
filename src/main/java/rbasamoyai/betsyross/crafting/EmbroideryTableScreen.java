package rbasamoyai.betsyross.crafting;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import rbasamoyai.betsyross.BetsyRoss;
import rbasamoyai.betsyross.config.BetsyRossConfig;

import java.util.ArrayList;
import java.util.List;

public class EmbroideryTableScreen extends AbstractContainerScreen<EmbroideryTableMenu> {

	private static final ResourceLocation EMBROIDERY_TABLE_SCREEN = BetsyRoss.path("textures/gui/embroidery_table.png");
	private static final String KEY = BetsyRoss.key("gui", "embroidery_table");

	private static final int MAX_URL_INPUT = 256;

	private EditBox url;
	private CooldownImageButton refreshButton;
	private ScrollTextWidget width;
	private ScrollTextWidget height;

	public EmbroideryTableScreen(EmbroideryTableMenu menu, Inventory playerInv, Component title) {
		super(menu, playerInv, title);
		this.imageWidth = 176;
		this.imageHeight = 188;
		this.inventoryLabelX = 8;
		this.inventoryLabelY = this.imageHeight - 94;
		menu.getSlot(EmbroideryTableMenu.STICKS_INDEX).setBackground(TextureAtlas.LOCATION_BLOCKS, BetsyRoss.path("item/stick_no_item"));
	}

	@Override
	protected void init() {
		super.init();

		this.url = new EditBox(this.minecraft.font, this.leftPos + 44, this.topPos + 73, 103, 12, Component.empty());
		this.url.setCanLoseFocus(false);
		this.url.setTextColor(-1);
		this.url.setTextColorUneditable(-1);
		this.url.setBordered(false);
		this.url.setMaxLength(MAX_URL_INPUT);
		this.url.setValue("");
		this.addWidget(this.url);
		this.setInitialFocus(this.url);
		this.url.setEditable(true);

		this.refreshButton = this.addRenderableWidget(new CooldownImageButton(this.leftPos + 152, this.topPos + 71, 11,
				11, 176, 0, EMBROIDERY_TABLE_SCREEN, this::onRefreshUrl));

		this.width = this.addRenderableWidget(new ScrollTextWidget(this.leftPos + 85, this.topPos + 22, 34, 16,
				Component.translatable(KEY + ".flag_width"), (byte) 1, getMaxCraftableWidth()));
		this.height = this.addRenderableWidget(new ScrollTextWidget(this.leftPos + 85, this.topPos + 44, 34, 16,
				Component.translatable(KEY + ".flag_height"), (byte) 1, getMaxCraftableHeight()));
	}

	private static byte getMaxCraftableWidth() {
		byte b = BetsyRossConfig.SERVER.maxCraftableWidth.get().byteValue();
		return b > 0 ? b : Byte.MAX_VALUE;
	}

	private static byte getMaxCraftableHeight() {
		byte b = BetsyRossConfig.SERVER.maxCraftableHeight.get().byteValue();
		return b > 0 ? b : Byte.MAX_VALUE;
	}

	@Override
	public void resize(Minecraft minecraft, int width, int height) {
		String s = this.url == null ? "" : this.url.getValue();
		byte w = this.width == null ? (byte) 1 : this.width.getValue();
		byte h = this.height == null ? (byte) 1 : this.height.getValue();
		super.resize(minecraft, width, height);
		this.url.setValue(s);
		this.width.setValue(w);
		this.height.setValue(h);
	}

	@Override
	protected void renderBg(PoseStack posestack, float partialTicks, int mouseX, int mouseY) {
		RenderSystem.setShader(GameRenderer::getPositionTexShader);
		RenderSystem.setShaderColor(1, 1, 1, 1);
		RenderSystem.setShaderTexture(0, EMBROIDERY_TABLE_SCREEN);

		this.blit(posestack, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);

		this.blit(posestack, this.leftPos + 41, this.topPos + 69, 0, 188, 110, 16);
	}

	@Override
	public void render(PoseStack posestack, int mouseX, int mouseY, float partialTicks) {
		this.renderBackground(posestack);
		super.render(posestack, mouseX, mouseY, partialTicks);

		if (this.url != null) this.url.render(posestack, mouseX, mouseY, partialTicks);
		if (this.width != null) this.width.render(posestack, mouseX, mouseY, partialTicks);
		if (this.height != null) this.height.render(posestack, mouseX, mouseY, partialTicks);

		this.renderTooltip(posestack, mouseX, mouseY);
	}

	@Override
	protected void renderLabels(PoseStack pPoseStack, int pMouseX, int pMouseY) {
		super.renderLabels(pPoseStack, pMouseX, pMouseY);
		Component c = Component.translatable(KEY + ".url");
		this.font.draw(pPoseStack, c, 38 - this.font.width(c), 73, 4210752);
	}

	@Override
	protected void renderTooltip(PoseStack pPoseStack, int pX, int pY) {
		if (this.menu.getCarried().isEmpty() && this.hoveredSlot instanceof EmbroideryTableMenu.OutputSlot out) {
			List<Component> append = new ArrayList<>();
			EmbroideryTableMenu.TakenItem mode = out.mode();

			append.add(Component.empty());
			if (mode.getRequiredSticks() > 0)
				append.add(Component.translatable(KEY + ".sticks_required", mode.getRequiredSticks()).withStyle(ChatFormatting.GRAY, ChatFormatting.ITALIC));
			if (mode.getMaxWidth() > 0)
				append.add(Component.translatable(KEY + ".max_height", mode.getMaxWidth()).withStyle(ChatFormatting.GRAY, ChatFormatting.ITALIC));
			if (mode.getMaxHeight() > 0)
				append.add(Component.translatable(KEY + ".max_height", mode.getMaxHeight()).withStyle(ChatFormatting.GRAY, ChatFormatting.ITALIC));
			append.add(Component.translatable(KEY + ".consumed_wool", this.menu.getRequiredWool()).withStyle(ChatFormatting.GRAY, ChatFormatting.ITALIC));

			List<Component> tooltip = this.getTooltipFromItem(out.getItem());
			tooltip.addAll(append);
			this.renderTooltip(pPoseStack, tooltip, out.getItem().getTooltipImage(), pX, pY);
			return;
		}

		super.renderTooltip(pPoseStack, pX, pY);

		if (this.refreshButton != null && this.refreshButton.isHoveredOrFocused()) {
			List<Component> tooltip = new ArrayList<>();
			tooltip.add(Component.translatable(KEY + ".reload_flags"));
			if (this.refreshButton.getCooldownTime() > 0) {
				int seconds = this.refreshButton.getCooldownTime() / 20;
				tooltip.add(Component.translatable(KEY + ".reload_flags.cooldown", seconds).withStyle(ChatFormatting.GRAY, ChatFormatting.ITALIC));
			}
			this.renderComponentTooltip(pPoseStack, tooltip, pX, pY);
		}
	}

	private void onRefreshUrl(Button button) {
		if (button == this.refreshButton && this.menu.canSync()) {
			this.menu.setAndSendDataToServer(this.url.getValue(), this.width.getValue(), this.height.getValue());
			this.refreshButton.setCooldownTime(100);
		}
	}

	@Override
	protected void containerTick() {
		super.containerTick();
		if (this.refreshButton != null) this.refreshButton.tickCooldownTime();
		if (this.url != null) this.url.tick();
	}

	@Override
	public boolean mouseScrolled(double pMouseX, double pMouseY, double pDelta) {
		if (this.width != null && this.width.mouseScrolled(pMouseX, pMouseY, pDelta)) return true;
		if (this.height != null && this.height.mouseScrolled(pMouseX, pMouseY, pDelta)) return true;
		return super.mouseScrolled(pMouseX, pMouseY, pDelta);
	}

}
