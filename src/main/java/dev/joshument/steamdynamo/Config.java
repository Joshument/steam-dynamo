package dev.joshument.steamdynamo;

import cofh.thermal.core.util.managers.dynamo.StirlingFuelManager;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import net.minecraftforge.registries.ForgeRegistries;

// An example config class. This is not required, but it's a good idea to have one to keep your config organized.
// Demonstrates how to use Forge's config APIs
@Mod.EventBusSubscriber(modid = SteamDynamo.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class Config
{
    private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();

    private static final ForgeConfigSpec.IntValue WATER_CONSUMPTION_AMOUNT = BUILDER
            .comment("The amount of water consumed per operation.")
            .defineInRange("waterConsumptionAmount", 100, 0, Integer.MAX_VALUE);

    private static final ForgeConfigSpec.IntValue WATER_RF_PRODUCTION_AMOUNT = BUILDER
            .comment("The amount of RF that one consumption cycle of water provides.")
            .defineInRange("waterRFConsumptionAmount", 50000, 0, Integer.MAX_VALUE);


    static final ForgeConfigSpec SPEC = BUILDER.build();

    public static int waterConsumptionAmount;
    public static int waterRFConsumptionAmount;

    private static boolean validateItemName(final Object obj)
    {
        return obj instanceof final String itemName && ForgeRegistries.ITEMS.containsKey(new ResourceLocation(itemName));
    }

    @SubscribeEvent
    static void onLoad(final ModConfigEvent event)
    {
        waterConsumptionAmount = WATER_CONSUMPTION_AMOUNT.get();
        waterRFConsumptionAmount = WATER_RF_PRODUCTION_AMOUNT.get();
    }
}
