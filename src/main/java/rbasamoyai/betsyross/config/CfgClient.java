package rbasamoyai.betsyross.config;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.ConfigValue;
import rbasamoyai.betsyross.BetsyRoss;
import rbasamoyai.betsyross.flags.FlagAnimationDetail;

public class CfgClient {

	public final ConfigValue<FlagAnimationDetail> animationDetail;

	public CfgClient(ForgeConfigSpec.Builder builder) {
		this.animationDetail = builder.comment("Level of animation detail for flags to use. Only applies to certain flags.")
				.translation("config." + BetsyRoss.MOD_ID + ".animationDetail")
				.defineEnum("animationDetail", FlagAnimationDetail.WAVE);
	}

}
