package rbasamoyai.betsyross.network;

import net.minecraft.client.Minecraft;
import rbasamoyai.betsyross.flags.FlagBlockEntity;

public class ClientHandlers {

	public static void handleFlagpoleSync(ClientboundSyncFlagpolePacket pkt) {
		Minecraft mc = Minecraft.getInstance();
		if (mc.level == null || !(mc.level.getBlockEntity(pkt.pos()) instanceof FlagBlockEntity flag)) return;
		flag.setFlagPole(pkt.flagpole());
	}

}
