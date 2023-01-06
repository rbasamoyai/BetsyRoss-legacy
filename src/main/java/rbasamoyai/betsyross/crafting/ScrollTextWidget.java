package rbasamoyai.betsyross.crafting;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;

public class ScrollTextWidget extends AbstractWidget {

	private byte value;
	private final byte minValue;
	private final byte maxValue;
	private final Screen screen;

	public ScrollTextWidget(Screen screen, int pX, int pY, int pWidth, int pHeight, Component pMessage, byte minValue, byte maxValue) {
		super(pX, pY, pWidth, pHeight, pMessage);
		this.screen = screen;
		this.minValue = minValue;
		this.maxValue = maxValue;
		this.value = this.minValue;
	}

	@Override
	public void renderButton(PoseStack pPoseStack, int pMouseX, int pMouseY, float pPartialTick) {
		Minecraft mc = Minecraft.getInstance();
		pPoseStack.pushPose();
		pPoseStack.translate(3, 4, 0);
		mc.font.drawShadow(pPoseStack, Byte.toString(this.value), this.x, this.y, -1);
		pPoseStack.popPose();
	}

	@Override
	public void renderToolTip(PoseStack pPoseStack, int pMouseX, int pMouseY) {
		this.screen.renderTooltip(pPoseStack, this.getMessage(), pMouseX, pMouseY);
	}

	@Override
	public boolean mouseScrolled(double pMouseX, double pMouseY, double pDelta) {
		if (this.isHoveredOrFocused()) {
			this.value = (byte) Mth.clamp((float) this.value + pDelta, (float) this.minValue, (float) maxValue);
			return true;
		}
		return false;
	}

	public void setValue(byte value) { this.value = value; }
	public byte getValue() { return this.value; }

	@Override public void updateNarration(NarrationElementOutput pNarrationElementOutput) {}

}
