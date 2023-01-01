package rbasamoyai.betsyross.flags;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
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
	private BlockState flagPole = Blocks.AIR.defaultBlockState();

	public FlagBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
	}

	public FlagBlockEntity(BlockPos pos, BlockState state) { this(BetsyRoss.FLAG_BLOCK_ENTITY.get(), pos, state); }

	@Override
	public AABB getRenderBoundingBox() {
		BlockState state = this.getBlockState();
		BlockPos pos = this.getBlockPos();
		if (state.is(BetsyRoss.FLAG_BLOCK.get())) {
			float dir = RotationSegment.convertToDegrees(state.getValue(FlagBlock.ROTATION));
			float f1 = Mth.sin(dir * Mth.DEG_TO_RAD);
			float f2 = Mth.cos(dir * Mth.DEG_TO_RAD);
			return new AABB(pos).expandTowards(f1 * this.flagWidth, this.flagHeight, f2 * this.flagWidth).inflate(1);
		}
		if (state.is(BetsyRoss.DRAPED_FLAG_BLOCK.get())) {
			Direction dir = state.getValue(DrapedFlagBlock.FACING);
			return new AABB(pos.relative(dir.getOpposite()), pos.below(this.flagHeight).relative(dir.getCounterClockWise(), this.flagWidth)).inflate(1);
		}
		return new AABB(pos);
	}

	public String getFlagUrl() { return this.flagUrl; }
	public byte getFlagHeight() { return this.flagHeight; }
	public byte getFlagWidth() { return this.flagWidth; }

	public void setFlagPole(BlockState state) {
		this.flagPole = state;
		this.setChanged();
	}
	public BlockState getFlagPole() { return this.flagPole; }

	@Override
	protected void saveAdditional(CompoundTag tag) {
		super.saveAdditional(tag);
		tag.putString("FlagUrl", this.flagUrl);
		tag.putByte("Height", this.flagHeight);
		tag.putByte("Width", this.flagWidth);
		if (this.flagPole != null) tag.put("Flagpole", NbtUtils.writeBlockState(this.flagPole));
	}

	@Override
	public void load(CompoundTag tag) {
		super.load(tag);
		this.flagUrl = tag.getString("FlagUrl");
		this.flagHeight = tag.getByte("Height");
		this.flagWidth = tag.getByte("Width");
		HolderGetter<Block> holder = this.level == null ? BuiltInRegistries.BLOCK.asLookup() : this.level.holderLookup(Registries.BLOCK);
		this.flagPole = NbtUtils.readBlockState(holder, tag.getCompound("Flagpole"));
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
