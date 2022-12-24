package rbasamoyai.betsyross.network;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;
import rbasamoyai.betsyross.flags.FlagBlockEntity;

import java.util.function.Supplier;

public class ClientboundSyncFlagpolePacket {

	private final BlockState flagpole;
	private final BlockPos pos;

	public ClientboundSyncFlagpolePacket(FlagBlockEntity flag) {
		this.flagpole = flag.getFlagPole();
		this.pos = flag.getBlockPos();
	}

	public ClientboundSyncFlagpolePacket(FriendlyByteBuf buf) {
		this.flagpole = Block.stateById(buf.readVarInt());
		this.pos = buf.readBlockPos();
	}

	public BlockState flagpole() { return this.flagpole; }
	public BlockPos pos() { return this.pos; }

	public void encode(FriendlyByteBuf buf) {
		buf.writeVarInt(Block.getId(this.flagpole)).writeBlockPos(this.pos);
	}

	public void handle(Supplier<NetworkEvent.Context> sup) {
		NetworkEvent.Context ctx = sup.get();
		ctx.enqueueWork(() -> {
			DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> ClientHandlers.handleFlagpoleSync(this));
		});
		ctx.setPacketHandled(true);
	}

}
