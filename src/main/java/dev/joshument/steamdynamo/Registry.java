package dev.joshument.steamdynamo;

import cofh.core.util.helpers.AugmentDataHelper;
import cofh.lib.util.DeferredRegisterCoFH;
import cofh.lib.util.constants.BlockStatePropertiesCoFH;
import cofh.lib.util.constants.NBTTags;
import cofh.lib.util.helpers.BlockHelper;
import cofh.thermal.core.common.config.ThermalCoreConfig;
import cofh.thermal.core.init.registries.ThermalCreativeTabs;
import cofh.thermal.lib.common.block.DynamoBlock;
import cofh.thermal.lib.common.item.AugmentItem;
import cofh.thermal.lib.common.item.BlockItemAugmentable;
import cofh.thermal.lib.util.ThermalAugmentRules;
import dev.joshument.steamdynamo.client.gui.DynamoSteamScreen;
import dev.joshument.steamdynamo.common.block.entity.DynamoSteamBlockEntity;
import dev.joshument.steamdynamo.common.inventory.DynamoSteamMenu;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import static dev.joshument.steamdynamo.common.block.entity.DynamoSteamBlockEntity.*;

public class Registry {
    public static final DeferredRegisterCoFH<Block> BLOCKS = DeferredRegisterCoFH.create(ForgeRegistries.BLOCKS, SteamDynamo.MODID);
    public static final DeferredRegisterCoFH<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegisterCoFH.create(ForgeRegistries.BLOCK_ENTITY_TYPES, SteamDynamo.MODID);
    public static final DeferredRegisterCoFH<Item> ITEMS = DeferredRegisterCoFH.create(ForgeRegistries.ITEMS, SteamDynamo.MODID);
    public static final DeferredRegisterCoFH<MenuType<?>> MENUS = DeferredRegisterCoFH.create(ForgeRegistries.MENU_TYPES, SteamDynamo.MODID);

    public static final BlockBehaviour.Properties STEAM_DYNAMO_PROPERTIES = BlockBehaviour.Properties.copy(net.minecraft.world.level.block.Blocks.NETHERITE_BLOCK)
            .strength(2.0F)
            .lightLevel(BlockHelper.lightValue(BlockStatePropertiesCoFH.ACTIVE, 14));

    public static void init(IEventBus bus) {
        // make sure the static class is initialized
        BlockEntities.init();
        Blocks.init();
        Items.init();
        Menus.init();

        BLOCK_ENTITIES.register(bus);
        BLOCKS.register(bus);
        ITEMS.register(bus);
        MENUS.register(bus);

        // add dynamo to thermal devices
        ThermalCreativeTabs.devicesTab(Items.DYNAMO_STEAM);

        // register menu on client
        bus.addListener((FMLClientSetupEvent event) -> event.enqueueWork(Menus::registerFactories));
    }

    // the block and block entity rely on each other to work, so separate classes are required to prevent forward reference
    public static class BlockEntities {
        static void init() {} // can call to make sure classes are initialized

        public static final RegistryObject<BlockEntityType<?>> DYNAMO_STEAM = BLOCK_ENTITIES.register(SteamDynamo.ID_DYNAMO_STEAM, () -> BlockEntityType.Builder.of(DynamoSteamBlockEntity::new, BLOCKS.get(SteamDynamo.ID_DYNAMO_STEAM)).build(null));
    }

    public static class Blocks {
        public static void init() {}

        public static final RegistryObject<Block> DYNAMO_STEAM = BLOCKS.register(SteamDynamo.ID_DYNAMO_STEAM, () -> new DynamoBlock(STEAM_DYNAMO_PROPERTIES, DynamoSteamBlockEntity.class, BlockEntities.DYNAMO_STEAM));
    }

    public static class Items {
        public static void init() {}

        public static final RegistryObject<Item> DYNAMO_STEAM = ITEMS.register(
                SteamDynamo.ID_DYNAMO_STEAM,
                () -> new BlockItemAugmentable(
                        Blocks.DYNAMO_STEAM.get(),
                        new Item.Properties()
                )
                        .setNumSlots(() -> ThermalCoreConfig.dynamoAugments)
                        .setAugValidator(ThermalAugmentRules.DYNAMO_VALIDATOR)
                        .setModId(SteamDynamo.ID_DYNAMO_STEAM)
        );

        public static final RegistryObject<Item> STEAM_TURBINE_AUGMENT = ITEMS.register(
                "steam_turbine_augment",
                () -> new AugmentItem(
                        new Item.Properties(),
                        AugmentDataHelper.builder()
                                .type(TAG_TYPE_STEAM)
                                .feature(TAG_FEATURE_CONVERSION_TYPE, TAG_TURBINE)
                                .mod(NBTTags.TAG_AUGMENT_DYNAMO_POWER, 1.0F)
                                .build()
                )
        );

        public static final RegistryObject<Item> STEAM_BOILER_AUGMENT = ITEMS.register(
                "steam_boiler_augment",
                () -> new AugmentItem(
                        new Item.Properties(),
                        AugmentDataHelper.builder()
                                .type(TAG_TYPE_STEAM)
                                .feature(TAG_FEATURE_CONVERSION_TYPE, TAG_BOILER)
                                .build()
                )
        );
    }

    public static class Menus {
        public static void init() {}
        static void registerFactories() {
            MenuScreens.register(DYNAMO_STEAM.get(), DynamoSteamScreen::new);
        }
        public static final RegistryObject<MenuType<DynamoSteamMenu>> DYNAMO_STEAM = MENUS.register(SteamDynamo.ID_DYNAMO_STEAM, () -> IForgeMenuType.create(DynamoSteamMenu::new));
    }
}

