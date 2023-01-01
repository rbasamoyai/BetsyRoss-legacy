package rbasamoyai.betsyross.flags;

import net.minecraft.ChatFormatting;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import org.jetbrains.annotations.Nullable;
import rbasamoyai.betsyross.BetsyRoss;
import rbasamoyai.betsyross.BetsyRossClient;

import java.util.List;
import java.util.function.Consumer;

public class FlagStandardItem extends Item {

	public FlagStandardItem(Properties properties) { super(properties); }

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

	@Override
	public void initializeClient(Consumer<IClientItemExtensions> consumer) {
		consumer.accept(new IClientItemExtensions() {
			@Override
			public BlockEntityWithoutLevelRenderer getCustomRenderer() {
				return BetsyRossClient.getFlagStandardRenderer();
			}
		});
	}

}
