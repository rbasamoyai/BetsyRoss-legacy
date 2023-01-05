package rbasamoyai.betsyross.network;

import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;
import rbasamoyai.betsyross.BetsyRoss;

public class BetsyRossNetwork {

	public static final SimpleChannel NETWORK = buildChannel();
	public static final String VERSION = "1.0.0";

	public static SimpleChannel buildChannel() {
		int id = 0;

		SimpleChannel network = NetworkRegistry.newSimpleChannel(BetsyRoss.path("network"), () -> VERSION, VERSION::equals, VERSION::equals);

		network.messageBuilder(ServerboundSyncTableDataPacket.class, id++)
				.encoder(ServerboundSyncTableDataPacket::encode)
				.decoder(ServerboundSyncTableDataPacket::new)
				.consumerMainThread(ServerboundSyncTableDataPacket::handle)
				.add();

		return network;
	}

	public static void init() {}

}
