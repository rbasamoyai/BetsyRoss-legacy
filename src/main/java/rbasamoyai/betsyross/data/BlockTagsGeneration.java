package rbasamoyai.betsyross.data;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.data.BlockTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import rbasamoyai.betsyross.BetsyRoss;

import java.util.concurrent.CompletableFuture;

public class BlockTagsGeneration extends BlockTagsProvider {

	public BlockTagsGeneration(PackOutput output, CompletableFuture<HolderLookup.Provider> provider, ExistingFileHelper helper) {
		super(output, provider, BetsyRoss.MOD_ID, helper);
	}

	@Override
	protected void addTags(HolderLookup.Provider provider) {
		tag(BetsyRossTags.FLAGPOLE).addTag(BlockTags.FENCES);
		tag(BetsyRossTags.FLAGPOLE).addTag(BlockTags.WALLS);
		tag(BetsyRossTags.FLAGPOLE).add(Blocks.IRON_BARS);
	}

}
