package rbasamoyai.betsyross.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;
import rbasamoyai.betsyross.crafting.EmbroideryTableMenu;

import java.util.function.Supplier;

public class ServerboundSyncTableDataPacket {

	private final String url;
	private final byte width;
	private final byte height;

	public ServerboundSyncTableDataPacket(String url, byte width, byte height) {
		this.url = url;
		this.width = width;
		this.height = height;
	}

	public ServerboundSyncTableDataPacket(FriendlyByteBuf buf) {
		this.url = buf.readUtf();
		this.width = buf.readByte();
		this.height = buf.readByte();
	}

	public void encode(FriendlyByteBuf buf) {
		buf.writeUtf(this.url);
		buf.writeByte(this.width);
		buf.writeByte(this.height);
	}

	public void handle(Supplier<NetworkEvent.Context> sup) {
		NetworkEvent.Context ctx = sup.get();
		ctx.enqueueWork(() -> {
			if (ctx.getSender().containerMenu instanceof EmbroideryTableMenu menu) menu.setDataOnServer(this.url, this.width, this.height);
		});
		ctx.setPacketHandled(true);
	}

}
