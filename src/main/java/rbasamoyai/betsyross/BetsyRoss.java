package rbasamoyai.betsyross;

import com.mojang.logging.LogUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.CreativeModeTabEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.slf4j.Logger;
import rbasamoyai.betsyross.config.BetsyRossConfig;
import rbasamoyai.betsyross.flags.*;
import rbasamoyai.betsyross.network.BetsyRossNetwork;

@Mod(BetsyRoss.MOD_ID)
public class BetsyRoss {

    public static final String MOD_ID = "betsyross";
    public static final Logger LOGGER = LogUtils.getLogger();

    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, MOD_ID);
    public static final RegistryObject<FlagBlock> FLAG_BLOCK = BLOCKS.register("flag_block",
            () -> new FlagBlock(FlagBlock.properties()));
    public static final RegistryObject<DrapedFlagBlock> DRAPED_FLAG_BLOCK = BLOCKS.register("draped_flag_block",
            () -> new DrapedFlagBlock(FlagBlock.properties()));

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MOD_ID);
    public static final RegistryObject<FlagBlockItem> FLAG_ITEM = ITEMS.register("flag_block",
            () -> new FlagBlockItem(FLAG_BLOCK.get(), DRAPED_FLAG_BLOCK.get(), new Item.Properties().stacksTo(1)));
    public static final RegistryObject<FlagStandardItem> FLAG_STANDARD = ITEMS.register("flag_standard",
            () -> new FlagStandardItem(new Item.Properties().stacksTo(1)));
    public static final RegistryObject<BannerStandardItem> BANNER_STANDARD = ITEMS.register("banner_standard",
            () -> new BannerStandardItem(new Item.Properties().stacksTo(1)));
    public static final RegistryObject<ArmorBannerItem> ARMOR_BANNER = ITEMS.register("armor_banner",
            () -> new ArmorBannerItem(new Item.Properties().stacksTo(1)));

    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, MOD_ID);
    public static final RegistryObject<BlockEntityType<FlagBlockEntity>> FLAG_BLOCK_ENTITY = BLOCK_ENTITY_TYPES.register("flag",
            () -> BlockEntityType.Builder.of(FlagBlockEntity::new, FLAG_BLOCK.get(), DRAPED_FLAG_BLOCK.get()).build(null));

    public static CreativeModeTab BASE_TAB = null;

    public BetsyRoss() {
        IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();
        IEventBus forgeBus = MinecraftForge.EVENT_BUS;

        BLOCKS.register(modBus);
        ITEMS.register(modBus);
        BLOCK_ENTITY_TYPES.register(modBus);

        modBus.addListener(this::onCommonSetup);
        modBus.addListener(this::onCreativeTabRegistry);

        ModLoadingContext mlCtx = ModLoadingContext.get();
        mlCtx.registerConfig(ModConfig.Type.CLIENT, BetsyRossConfig.CLIENT_SPEC);

        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> BetsyRossClient.onCtor(modBus, forgeBus));
    }

    private void onCommonSetup(FMLCommonSetupEvent evt) {
        BetsyRossNetwork.init();
    }

    private void onCreativeTabRegistry(CreativeModeTabEvent.Register evt) {
        BASE_TAB = evt.registerCreativeModeTab(path("base"), builder -> builder.title(Component.translatable("itemGroup." + MOD_ID))
                .icon(() -> FLAG_ITEM.get().getLogoStack())
                .displayItems((flagSet, output, perms) -> {
                    output.accept(FLAG_ITEM.get().getDefaultInstance());
                    output.accept(FLAG_ITEM.get().getLogoStack());
                    output.accept(FLAG_STANDARD.get().getDefaultInstance());
                    output.accept(BANNER_STANDARD.get().getDefaultInstance());
                    output.accept(ARMOR_BANNER.get().getDefaultInstance());
                }));
    }

    public static ResourceLocation path(String path) { return new ResourceLocation(MOD_ID, path); }

}
