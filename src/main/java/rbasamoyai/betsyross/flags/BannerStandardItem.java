package rbasamoyai.betsyross.flags;

import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import rbasamoyai.betsyross.BetsyRossClient;

import java.util.function.Consumer;

public class BannerStandardItem extends StandardItem {

	public BannerStandardItem(Properties properties) { super(properties); }

	@Override
	public void initializeClient(Consumer<IClientItemExtensions> consumer) {
		consumer.accept(new IClientItemExtensions() {
			@Override
			public BlockEntityWithoutLevelRenderer getCustomRenderer() {
				return BetsyRossClient.getBannerStandardRenderer();
			}
		});
	}

}
