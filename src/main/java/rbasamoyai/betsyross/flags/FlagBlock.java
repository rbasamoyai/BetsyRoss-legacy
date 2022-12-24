package rbasamoyai.betsyross.flags;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.block.state.properties.RotationSegment;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.network.PacketDistributor;
import org.jetbrains.annotations.Nullable;
import rbasamoyai.betsyross.data.BetsyRossTags;
import rbasamoyai.betsyross.network.BetsyRossNetwork;
import rbasamoyai.betsyross.network.ClientboundSyncFlagpolePacket;

public class FlagBlock extends Block implements EntityBlock {

    public static final IntegerProperty ROTATION = BlockStateProperties.ROTATION_16;

    public FlagBlock(Properties properties) { super(properties); }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(ROTATION);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState().setValue(ROTATION, RotationSegment.convertToSegment(-context.getRotation() + 180));
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new FlagBlockEntity(pos, state);
    }

    @Override public RenderShape getRenderShape(BlockState state) { return RenderShape.ENTITYBLOCK_ANIMATED; }

    @Override
    public VoxelShape getVisualShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return Shapes.empty();
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult result) {
        ItemStack stack = player.getItemInHand(hand);
        if (level.getBlockEntity(pos) instanceof FlagBlockEntity flag) {
            if (stack.getItem() instanceof BlockItem item
                && flag.getFlagPole().isAir()
                && isFlagpole(item.getBlock())) {
                if (!level.isClientSide) {
                    BlockState state1 = item.getBlock().defaultBlockState();
                    flag.setFlagPole(state1);
                    SoundType soundType = state1.getSoundType();
                    level.playSound(null, pos, soundType.getPlaceSound(), SoundSource.BLOCKS, (soundType.getVolume() + 1.0F) / 2.0F, soundType.getPitch() * 0.8F);
                    BetsyRossNetwork.NETWORK.send(PacketDistributor.TRACKING_CHUNK.with(() -> level.getChunkAt(pos)), new ClientboundSyncFlagpolePacket(flag));
                    if (!player.isCreative()) stack.shrink(1);
                }
                return InteractionResult.sidedSuccess(level.isClientSide);
            } else if (stack.isEmpty() && !flag.getFlagPole().isAir()) {
                if (!level.isClientSide) {
                    BlockState oldFlagpole = flag.getFlagPole();
                    flag.setFlagPole(Blocks.AIR.defaultBlockState());
                    level.playSound(null, pos, SoundEvents.ITEM_FRAME_REMOVE_ITEM, SoundSource.BLOCKS, 1.0f, 1.0f);
                    BetsyRossNetwork.NETWORK.send(PacketDistributor.TRACKING_CHUNK.with(() -> level.getChunkAt(pos)), new ClientboundSyncFlagpolePacket(flag));
                    if (!player.isCreative()) player.addItem(oldFlagpole.getCloneItemStack(null, level, pos, player));
                }
                return InteractionResult.sidedSuccess(level.isClientSide);
            }
        }

        return super.use(state, level, pos, player, hand, result);
    }

    public static boolean isFlagpole(Block block) {
        return block.defaultBlockState().is(BetsyRossTags.FLAGPOLE);
    }

    public static Properties properties() {
        return Properties.of(Material.WOOL).sound(SoundType.WOOL).noCollission().noOcclusion().instabreak();
    }

}
