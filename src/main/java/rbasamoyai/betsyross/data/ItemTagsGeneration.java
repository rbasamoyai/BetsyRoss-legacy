package rbasamoyai.betsyross.data;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;
import rbasamoyai.betsyross.BetsyRoss;

import java.util.concurrent.CompletableFuture;

public class ItemTagsGeneration extends ItemTagsProvider {

	public ItemTagsGeneration(PackOutput output, CompletableFuture<HolderLookup.Provider> provider, TagsProvider<Block> blockTags, @Nullable ExistingFileHelper helper) {
		super(output, provider, blockTags, BetsyRoss.MOD_ID, helper);
	}

	@Override
	protected void addTags(HolderLookup.Provider provider) {
		tag(BetsyRossTags.FLAG_MATERIAL).addTag(ItemTags.WOOL);
		tag(BetsyRossTags.FLAG_STICK_MATERIAL).add(Items.STICK);
	}

}
