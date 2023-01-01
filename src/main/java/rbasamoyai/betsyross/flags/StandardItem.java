package rbasamoyai.betsyross.flags;

import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import rbasamoyai.betsyross.BetsyRoss;

import java.util.List;

public abstract class StandardItem extends Item {

	protected StandardItem(Properties properties) { super(properties); }

	@Override
	public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
		super.appendHoverText(stack, level, tooltip, flag);
		if (flag.isAdvanced()) {
			CompoundTag flagData = stack.getOrCreateTag();
			int width = flagData.getByte("Width");
			int height = flagData.getByte("Height");

			String key = BetsyRoss.FLAG_BLOCK.get().getDescriptionId() + ".tooltip";
			tooltip.add(Component.empty());
			tooltip.add(Component.translatable(key + ".width", width).withStyle(ChatFormatting.GRAY));
			tooltip.add(Component.translatable(key + ".height", height).withStyle(ChatFormatting.GRAY));
		}
	}

	@Override
	public ItemStack getDefaultInstance() {
		ItemStack result = super.getDefaultInstance();
		CompoundTag tag = result.getOrCreateTag();
		tag.putByte("Width", (byte) 1);
		tag.putByte("Height", (byte) 1);
		return result;
	}

	@Override public int getUseDuration(ItemStack stack) { return 72000; }

	@Override
	public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
		player.startUsingItem(hand);
		return InteractionResultHolder.sidedSuccess(player.getItemInHand(hand), level.isClientSide);
	}

}
