package rbasamoyai.betsyross.flags;

import net.minecraft.ChatFormatting;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.StandingAndWallBlockItem;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import org.jetbrains.annotations.Nullable;
import rbasamoyai.betsyross.BetsyRossClient;

import java.util.List;
import java.util.function.Consumer;

public class FlagBlockItem extends StandingAndWallBlockItem {

	public FlagBlockItem(Block standing, Block wall, Properties properties) {
		super(standing, wall, properties, Direction.DOWN);
	}

	@Override
	public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
		super.appendHoverText(stack, level, tooltip, flag);
		if (flag.isAdvanced()) {
			CompoundTag flagData = stack.getOrCreateTag().getCompound("BlockEntityTag");
			int width = flagData.getByte("Width");
			int height = flagData.getByte("Height");

			String key = this.getDescriptionId(stack) + ".tooltip";
			tooltip.add(Component.empty());
			tooltip.add(Component.translatable(key + ".width", width).withStyle(ChatFormatting.GRAY));
			tooltip.add(Component.translatable(key + ".height", height).withStyle(ChatFormatting.GRAY));
		}
	}

	@Override
	public ItemStack getDefaultInstance() {
		ItemStack result = super.getDefaultInstance();
		CompoundTag tag = result.getOrCreateTag();
		CompoundTag blockData = new CompoundTag();
		blockData.putByte("Width", (byte) 1);
		blockData.putByte("Height", (byte) 1);
		tag.put("BlockEntityTag", blockData);
		return result;
	}

	@Override
	public void initializeClient(Consumer<IClientItemExtensions> consumer) {
		consumer.accept(new IClientItemExtensions() {
			@Override
			public BlockEntityWithoutLevelRenderer getCustomRenderer() {
				return BetsyRossClient.getFlagItemRenderer();
			}
		});
	}

	public ItemStack getLogoStack() {
		ItemStack result = new ItemStack(this);
		CompoundTag tag = result.getOrCreateTag();
		CompoundTag blockData = new CompoundTag();
		blockData.putString("FlagUrl", "betsyrosslogo");
		blockData.putByte("Width", (byte) 4);
		blockData.putByte("Height", (byte) 4);
		tag.put("BlockEntityTag", blockData);
		return result;
	}

}
