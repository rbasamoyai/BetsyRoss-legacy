package rbasamoyai.betsyross.network;

import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;
import rbasamoyai.betsyross.BetsyRoss;

public class BetsyRossNetwork {

	public static final SimpleChannel NETWORK = buildChannel();
	public static final String VERSION = "0.0.1";

	public static SimpleChannel buildChannel() {
		int id = 0;

		SimpleChannel network = NetworkRegistry.newSimpleChannel(BetsyRoss.path("network"), () -> VERSION, VERSION::equals, VERSION::equals);

		network.messageBuilder(ClientboundSyncFlagpolePacket.class, id++)
				.encoder(ClientboundSyncFlagpolePacket::encode)
				.decoder(ClientboundSyncFlagpolePacket::new)
				.consumerMainThread(ClientboundSyncFlagpolePacket::handle)
				.add();

		return network;
	}

	public static void init() {}

}
