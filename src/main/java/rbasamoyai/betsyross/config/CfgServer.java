package rbasamoyai.betsyross.config;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.IntValue;
import rbasamoyai.betsyross.BetsyRoss;

public class CfgServer {

	public final IntValue flagBlockRequiredSticks;
	public final IntValue flagBlockMaxWidth;
	public final IntValue flagBlockMaxHeight;

	public final IntValue armorBannerRequiredSticks;
	public final IntValue armorBannerMaxWidth;
	public final IntValue armorBannerMaxHeight;

	public final IntValue flagStandardRequiredSticks;
	public final IntValue flagStandardMaxWidth;
	public final IntValue flagStandardMaxHeight;

	public final IntValue bannerStandardRequiredSticks;
	public final IntValue bannerStandardMaxWidth;
	public final IntValue bannerStandardMaxHeight;

	public final IntValue maxCraftableWidth;
	public final IntValue maxCraftableHeight;
	public final IntValue areaPerWool;

	public CfgServer(ForgeConfigSpec.Builder builder) {
		this.flagBlockRequiredSticks = builder.comment("How many sticks needed to craft a flag block. Set to 0 to not require sticks.")
				.translation(BetsyRoss.key("config", "flagBlockRequiredSticks"))
				.defineInRange("flagBlockRequiredSticks", 0, 0, 64);
		this.flagBlockMaxWidth = builder.comment("Maximum width allowed when crafting a flag block. Set to 0 to disable the maximum limit.")
				.translation(BetsyRoss.key("config", "flagBlockMaxWidth"))
				.defineInRange("flagBlockMaxWidth", 0, 0, Byte.MAX_VALUE);
		this.flagBlockMaxHeight = builder.comment("Maximum height allowed when crafting a flag block. Set to 0 to disable the maximum limit.")
				.translation(BetsyRoss.key("config", "flagBlockMaxHeight"))
				.defineInRange("flagBlockMaxHeight", 0, 0, Byte.MAX_VALUE);

		this.armorBannerRequiredSticks = builder.comment("How many sticks needed to craft an armor banner. Set to 0 to not require sticks.")
				.translation(BetsyRoss.key("config", "armorBannerRequiredSticks"))
				.defineInRange("armorBannerRequiredSticks", 2, 0, 64);
		this.armorBannerMaxWidth = builder.comment("Maximum width allowed when crafting an armor banner. Set to 0 to disable the maximum limit.")
				.translation(BetsyRoss.key("config", "armorBannerMaxWidth"))
				.defineInRange("armorBannerMaxWidth", 1, 0, Byte.MAX_VALUE);
		this.armorBannerMaxHeight = builder.comment("Maximum height allowed when crafting an armor banner. Set to 0 to disable the maximum limit.")
				.translation(BetsyRoss.key("config", "armorBannerMaxHeight"))
				.defineInRange("armorBannerMaxHeight", 2, 0, Byte.MAX_VALUE);

		this.flagStandardRequiredSticks = builder.comment("How many sticks needed to craft a flag standard. Set to 0 to not require sticks.")
				.translation(BetsyRoss.key("config", "flagStandardRequiredSticks"))
				.defineInRange("flagStandardRequiredSticks", 3, 0, 64);
		this.flagStandardMaxWidth = builder.comment("Maximum width allowed when crafting a flag standard. Set to 0 to disable the maximum limit.")
				.translation(BetsyRoss.key("config", "flagStandardMaxWidth"))
				.defineInRange("flagStandardMaxWidth", 2, 0, Byte.MAX_VALUE);
		this.flagStandardMaxHeight = builder.comment("Maximum height allowed when crafting a flag standard. Set to 0 to disable the maximum limit.")
				.translation(BetsyRoss.key("config", "flagStandardMaxHeight"))
				.defineInRange("flagStandardMaxHeight", 2, 0, Byte.MAX_VALUE);

		this.bannerStandardRequiredSticks = builder.comment("How many sticks needed to craft a banner standard. Set to 0 to not require sticks.")
				.translation(BetsyRoss.key("config", "bannerStandardRequiredSticks"))
				.defineInRange("bannerStandardRequiredSticks", 5, 0, 64);
		this.bannerStandardMaxWidth = builder.comment("Maximum width allowed when crafting a banner standard. Set to 0 to disable the maximum limit.")
				.translation(BetsyRoss.key("config", "bannerStandardMaxWidth"))
				.defineInRange("bannerStandardMaxWidth", 2, 0, Byte.MAX_VALUE);
		this.bannerStandardMaxHeight = builder.comment("Maximum height allowed when crafting a banner standard. Set to 0 to disable the maximum limit.")
				.translation(BetsyRoss.key("config", "bannerStandardMaxHeight"))
				.defineInRange("bannerStandardMaxHeight", 3, 0, Byte.MAX_VALUE);

		this.maxCraftableWidth = builder.comment("Maximum width allowed when crafting flags in the Embroidery Table. Set to 0 to disable the maximum limit.")
				.translation(BetsyRoss.key("config", "maxCraftableWidth"))
				.defineInRange("maxCraftableWidth", 16, 0, Byte.MAX_VALUE);
		this.maxCraftableHeight = builder.comment("Maximum height allowed when crafting flags in the Embroidery Table. Set to 0 to disable the maximum limit.")
				.translation(BetsyRoss.key("config", "maxCraftableHeight"))
				.defineInRange("maxCraftableHeight", 16, 0, Byte.MAX_VALUE);
		this.areaPerWool = builder.comment("Amount of flag area that can be crafted for each wool block supplied in the Embroidery Table.")
				.translation(BetsyRoss.key("config", "areaPerWool"))
				.defineInRange("areaPerWool", 4, 1, Integer.MAX_VALUE);
	}

}
