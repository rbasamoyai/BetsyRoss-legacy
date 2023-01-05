package rbasamoyai.betsyross.data;

import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import rbasamoyai.betsyross.BetsyRoss;

public class BetsyRossTags {

	public static final TagKey<Block> FLAGPOLE = BlockTags.create(BetsyRoss.path("flagpole"));

	public static final TagKey<Item> FLAG_MATERIAL = ItemTags.create(BetsyRoss.path("flag_material"));
	public static final TagKey<Item> FLAG_STICK_MATERIAL = ItemTags.create(BetsyRoss.path("flag_stick_material"));
}
