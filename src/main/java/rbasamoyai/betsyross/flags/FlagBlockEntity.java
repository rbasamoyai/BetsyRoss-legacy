package rbasamoyai.betsyross.flags;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.RotationSegment;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.Nullable;
import rbasamoyai.betsyross.BetsyRoss;

public class FlagBlockEntity extends BlockEntity {

	private String flagUrl = "";
	private byte flagHeight = 1;
	private byte flagWidth = 1;

	public FlagBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
	}

	public FlagBlockEntity(BlockPos pos, BlockState state) { this(BetsyRoss.FLAG_BLOCK_ENTITY.get(), pos, state); }

	@Override
	public AABB getRenderBoundingBox() {
		float dir = RotationSegment.convertToDegrees(this.getBlockState().getValue(FlagBlock.ROTATION));
		float f1 = Mth.sin(dir * Mth.DEG_TO_RAD);
		float f2 = Mth.cos(dir * Mth.DEG_TO_RAD);
		return new AABB(this.getBlockPos()).expandTowards(f1 * this.flagWidth, this.flagHeight, f2 * this.flagWidth);
	}

	public void setFlagUrl(String url) { this.flagUrl = url; }
	public String getFlagUrl() { return this.flagUrl; }

	public void setFlagHeight(byte height) {
		this.flagHeight = height;
		this.setChanged();
	}
	public byte getFlagHeight() { return this.flagHeight; }

	public void setFlagWidth(byte width) {
		this.flagWidth = width;
		this.setChanged();
	}
	public byte getFlagWidth() { return this.flagWidth; }

	@Override
	protected void saveAdditional(CompoundTag tag) {
		super.saveAdditional(tag);
		tag.putString("FlagUrl", this.flagUrl);
		tag.putByte("Height", this.flagHeight);
		tag.putByte("Width", this.flagWidth);
	}

	@Override
	public void load(CompoundTag tag) {
		super.load(tag);
		this.flagUrl = tag.getString("FlagUrl");
		this.flagHeight = tag.getByte("Height");
		this.flagWidth = tag.getByte("Width");
	}

	@Nullable
	@Override
	public Packet<ClientGamePacketListener> getUpdatePacket() {
		return ClientboundBlockEntityDataPacket.create(this);
	}

	@Override public CompoundTag getUpdateTag() {
		return this.saveWithoutMetadata();
	}

}
