package rbasamoyai.betsyross.flags;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import rbasamoyai.betsyross.BetsyRoss;

public class FlagBlockEntity extends BlockEntity {

    private String flagUrl = "";
    private byte flagHeight = 1;
    private byte flagWidth = 1;

    public FlagBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    public FlagBlockEntity(BlockPos pos, BlockState state) { this(BetsyRoss.FLAG_BLOCK_ENTITY.get(), pos, state); }

    public void setFlagUrl(String url) { this.flagUrl = url; }
    public String getFlagUrl() { return this.flagUrl; }

    public void setFlagHeight(byte height) { this.flagHeight = height; }
    public byte getFlagHeight() { return this.flagHeight; }

    public void setFlagWidth(byte width) { this.flagWidth = width; }
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

}
