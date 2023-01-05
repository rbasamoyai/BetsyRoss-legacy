package rbasamoyai.betsyross.data;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import rbasamoyai.betsyross.BetsyRoss;

import java.util.concurrent.CompletableFuture;

@Mod.EventBusSubscriber(modid = BetsyRoss.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class DataGeneration {

	@SubscribeEvent
	public static void onDataGeneration(GatherDataEvent evt) {
		DataGenerator gen = evt.getGenerator();
		ExistingFileHelper helper = evt.getExistingFileHelper();
		CompletableFuture<HolderLookup.Provider> provider = evt.getLookupProvider();
		PackOutput output = gen.getPackOutput();


		boolean s = evt.includeServer();
		TagsProvider<Block> blockTags = new BlockTagsGeneration(output, provider, helper);
		gen.addProvider(s, blockTags);
		gen.addProvider(s, new ItemTagsGeneration(output, provider, blockTags, helper));
	}

}
