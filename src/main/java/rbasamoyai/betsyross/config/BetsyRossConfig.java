package rbasamoyai.betsyross.config;

import net.minecraftforge.common.ForgeConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

public class BetsyRossConfig {

	public static ForgeConfigSpec CLIENT_SPEC;
	public static CfgClient CLIENT;
	static {
		Pair<CfgClient, ForgeConfigSpec> pair = new ForgeConfigSpec.Builder().configure(CfgClient::new);
		CLIENT_SPEC = pair.getRight();
		CLIENT = pair.getLeft();
	}

	public static ForgeConfigSpec SERVER_SPEC;
	public static CfgServer SERVER;
	static {
		Pair<CfgServer, ForgeConfigSpec> pair = new ForgeConfigSpec.Builder().configure(CfgServer::new);
		SERVER_SPEC = pair.getRight();
		SERVER = pair.getLeft();
	}

}
