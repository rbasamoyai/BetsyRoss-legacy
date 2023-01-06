package rbasamoyai.betsyross.data;

import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.BlockTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import rbasamoyai.betsyross.BetsyRoss;

@Mod.EventBusSubscriber(modid = BetsyRoss.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class DataGeneration {

	@SubscribeEvent
	public static void onDataGeneration(GatherDataEvent evt) {
		DataGenerator gen = evt.getGenerator();
		ExistingFileHelper helper = evt.getExistingFileHelper();

		boolean s = evt.includeServer();
		BlockTagsProvider blockTags = new BlockTagsGeneration(gen, helper);
		gen.addProvider(s, blockTags);
		gen.addProvider(s, new ItemTagsGeneration(gen, blockTags, helper));
	}

}
