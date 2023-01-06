package rbasamoyai.betsyross.data;

import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.BlockTagsProvider;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.data.ExistingFileHelper;
import rbasamoyai.betsyross.BetsyRoss;

public class BlockTagsGeneration extends BlockTagsProvider {

	public BlockTagsGeneration(DataGenerator gen, ExistingFileHelper helper) {
		super(gen, BetsyRoss.MOD_ID, helper);
	}

	@Override
	protected void addTags() {
		tag(BetsyRossTags.FLAGPOLE).addTag(BlockTags.FENCES);
		tag(BetsyRossTags.FLAGPOLE).addTag(BlockTags.WALLS);
		tag(BetsyRossTags.FLAGPOLE).add(Blocks.IRON_BARS);
	}

}
