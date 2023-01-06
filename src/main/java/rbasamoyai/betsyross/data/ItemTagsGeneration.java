package rbasamoyai.betsyross.data;

import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.BlockTagsProvider;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Items;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;
import rbasamoyai.betsyross.BetsyRoss;

public class ItemTagsGeneration extends ItemTagsProvider {

	public ItemTagsGeneration(DataGenerator gen, BlockTagsProvider blockTags, @Nullable ExistingFileHelper helper) {
		super(gen, blockTags, BetsyRoss.MOD_ID, helper);
	}

	@Override
	protected void addTags() {
		tag(BetsyRossTags.FLAG_MATERIAL).addTag(ItemTags.WOOL);
		tag(BetsyRossTags.FLAG_STICK_MATERIAL).add(Items.STICK);
	}

}
