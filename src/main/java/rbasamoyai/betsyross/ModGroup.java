package rbasamoyai.betsyross;

import net.minecraft.core.NonNullList;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

public class ModGroup extends CreativeModeTab {


	public ModGroup() {
		super(BetsyRoss.MOD_ID);
	}

	@Override public ItemStack makeIcon() { return BetsyRoss.FLAG_ITEM.get().getLogoStack(); }

	@Override
	public void fillItemList(NonNullList<ItemStack> pItems) {
		pItems.add(BetsyRoss.EMBROIDERY_TABLE_ITEM.get().getDefaultInstance());
		pItems.add(BetsyRoss.FLAG_ITEM.get().getDefaultInstance());
		pItems.add(BetsyRoss.FLAG_ITEM.get().getLogoStack());
		pItems.add(BetsyRoss.FLAG_STANDARD.get().getDefaultInstance());
		pItems.add(BetsyRoss.BANNER_STANDARD.get().getDefaultInstance());
		pItems.add(BetsyRoss.ARMOR_BANNER.get().getDefaultInstance());
	}
}
