package rbasamoyai.betsyross.crafting;

import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.resources.ResourceLocation;

public class CooldownImageButton extends ImageButton {

	private int cooldownTime;

	public CooldownImageButton(int pX, int pY, int pWidth, int pHeight, int pXTexStart, int pYTexStart, ResourceLocation pResourceLocation, OnPress pOnPress) {
		super(pX, pY, pWidth, pHeight, pXTexStart, pYTexStart, pResourceLocation, pOnPress);
	}

	@Override
	public boolean isActive() {
		return super.isActive() && this.cooldownTime <= 0;
	}

	public void tickCooldownTime() { this.cooldownTime--; }
	public void setCooldownTime(int time) { this.cooldownTime = time; }
	public int getCooldownTime() { return this.cooldownTime; }

}
