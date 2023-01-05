package rbasamoyai.betsyross;

import com.mojang.logging.LogUtils;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.stats.StatFormatter;
import net.minecraft.stats.Stats;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.BlockItem;
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
import net.minecraftforge.fml.event.config.ModConfigEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegisterEvent;
import net.minecraftforge.registries.RegistryObject;
import org.slf4j.Logger;
import rbasamoyai.betsyross.config.BetsyRossConfig;
import rbasamoyai.betsyross.crafting.EmbroideryTableBlock;
import rbasamoyai.betsyross.crafting.EmbroideryTableMenu;
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
    public static final RegistryObject<EmbroideryTableBlock> EMBROIDERY_TABLE_BLOCK = BLOCKS.register("embroidery_table",
            () -> new EmbroideryTableBlock(EmbroideryTableBlock.properties()));

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MOD_ID);
    public static final RegistryObject<FlagBlockItem> FLAG_ITEM = ITEMS.register("flag_block",
            () -> new FlagBlockItem(FLAG_BLOCK.get(), DRAPED_FLAG_BLOCK.get(), new Item.Properties().stacksTo(1)));
    public static final RegistryObject<FlagStandardItem> FLAG_STANDARD = ITEMS.register("flag_standard",
            () -> new FlagStandardItem(new Item.Properties().stacksTo(1)));
    public static final RegistryObject<BannerStandardItem> BANNER_STANDARD = ITEMS.register("banner_standard",
            () -> new BannerStandardItem(new Item.Properties().stacksTo(1)));
    public static final RegistryObject<ArmorBannerItem> ARMOR_BANNER = ITEMS.register("armor_banner",
            () -> new ArmorBannerItem(new Item.Properties().stacksTo(1)));
    public static final RegistryObject<BlockItem> EMBROIDERY_TABLE_ITEM = ITEMS.register("embroidery_table",
            () -> new BlockItem(EMBROIDERY_TABLE_BLOCK.get(), new Item.Properties()));

    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, MOD_ID);
    public static final RegistryObject<BlockEntityType<FlagBlockEntity>> FLAG_BLOCK_ENTITY = BLOCK_ENTITY_TYPES.register("flag",
            () -> BlockEntityType.Builder.of(FlagBlockEntity::new, FLAG_BLOCK.get(), DRAPED_FLAG_BLOCK.get()).build(null));

    public static final DeferredRegister<MenuType<?>> MENU_TYPES = DeferredRegister.create(ForgeRegistries.MENU_TYPES, MOD_ID);
    public static final RegistryObject<MenuType<EmbroideryTableMenu>> EMBROIDERY_TABLE_MENU = MENU_TYPES.register("embroidery_table",
            () -> new MenuType<>(EmbroideryTableMenu::new));

    public static final ResourceLocation INTERACT_WITH_EMBROIDERY_TABLE = path("interact_with_embroidery_table");

    public BetsyRoss() {
        IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();
        IEventBus forgeBus = MinecraftForge.EVENT_BUS;

        BLOCKS.register(modBus);
        ITEMS.register(modBus);
        BLOCK_ENTITY_TYPES.register(modBus);
        MENU_TYPES.register(modBus);
        modBus.addListener(this::onRegisterStats);

        modBus.addListener(this::onCommonSetup);
        modBus.addListener(this::onCreativeTabRegistry);
        modBus.addListener(this::onConfigLoad);
        modBus.addListener(this::onConfigReload);

        ModLoadingContext mlCtx = ModLoadingContext.get();
        mlCtx.registerConfig(ModConfig.Type.CLIENT, BetsyRossConfig.CLIENT_SPEC);
        mlCtx.registerConfig(ModConfig.Type.SERVER, BetsyRossConfig.SERVER_SPEC);

        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> BetsyRossClient.onCtor(modBus, forgeBus));
    }

    private void onCommonSetup(FMLCommonSetupEvent evt) {
        BetsyRossNetwork.init();
        evt.enqueueWork(() -> {
           Stats.CUSTOM.get(INTERACT_WITH_EMBROIDERY_TABLE, StatFormatter.DEFAULT);
        });
    }

    private void onRegisterStats(RegisterEvent evt) {
        registerCustomStat(evt, INTERACT_WITH_EMBROIDERY_TABLE);
    }

    private void registerCustomStat(RegisterEvent evt,  ResourceLocation loc) {
        evt.register(BuiltInRegistries.CUSTOM_STAT.key(), loc, () -> loc);
    }

    private void onCreativeTabRegistry(CreativeModeTabEvent.Register evt) {
        evt.registerCreativeModeTab(path("base"), builder -> builder.title(Component.translatable("itemGroup." + MOD_ID))
                .icon(() -> FLAG_ITEM.get().getLogoStack())
                .displayItems((flagSet, output, perms) -> {
                    output.accept(EMBROIDERY_TABLE_ITEM.get().getDefaultInstance());
                    output.accept(FLAG_ITEM.get().getDefaultInstance());
                    output.accept(FLAG_ITEM.get().getLogoStack());
                    output.accept(FLAG_STANDARD.get().getDefaultInstance());
                    output.accept(BANNER_STANDARD.get().getDefaultInstance());
                    output.accept(ARMOR_BANNER.get().getDefaultInstance());
                }));
    }

    private void onConfigLoad(ModConfigEvent.Loading evt) {
        LOGGER.debug("Loaded Betsy Ross config file {}", evt.getConfig().getFileName());
    }

    private void onConfigReload(ModConfigEvent.Reloading evt) {
        LOGGER.debug("Reloaded Betsy Ross config file {}", evt.getConfig().getFileName());
    }

    public static ResourceLocation path(String path) { return new ResourceLocation(MOD_ID, path); }

}
