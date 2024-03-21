package dev.joshument.steamdynamo.common.block.entity;

import cofh.lib.api.StorageGroup;
import cofh.lib.common.fluid.FluidStorageCoFH;
import cofh.lib.common.inventory.ItemStorageCoFH;
import cofh.thermal.core.common.config.ThermalCoreConfig;
import cofh.thermal.core.util.managers.dynamo.StirlingFuelManager;
import cofh.thermal.lib.common.block.entity.DynamoBlockEntity;
import dev.joshument.steamdynamo.Config;
import dev.joshument.steamdynamo.Registry;
import dev.joshument.steamdynamo.common.inventory.DynamoSteamMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;

import javax.annotation.Nullable;

import static cofh.lib.util.Constants.TANK_SMALL;

public class DynamoSteamBlockEntity extends DynamoBlockEntity {
    // stirling fuel because we want the same power generation as a stirling generator
    protected ItemStorageCoFH fuelSlot = new ItemStorageCoFH(item -> filter.valid(item) && StirlingFuelManager.instance().validFuel(item));
    protected FluidStorageCoFH waterTank = new FluidStorageCoFH(TANK_SMALL, fluid -> fluid.getFluid().isSame(Fluids.WATER));
    protected int waterFuelBuffer = 0;

    public DynamoSteamBlockEntity(BlockPos pos, BlockState state) {
        super(Registry.BlockEntities.DYNAMO_STEAM.get(), pos, state);

        inventory.addSlot(fuelSlot, StorageGroup.INPUT);
        tankInv.addTank(waterTank, StorageGroup.INPUT);

        addAugmentSlots(ThermalCoreConfig.dynamoAugments);
        initHandlers();
    }

    @Override
    protected boolean canProcessStart() {
        // again, stirling because we want to go off of stirling fuel
        return StirlingFuelManager.instance().getEnergy(fuelSlot.getItemStack()) > 0 && waterTank.getStored() > Config.waterConsumptionAmount;
    }

    @Override
    protected void processStart() {
        // if the fuel is above zero the dynamo was forcefully stopped by a lack of water and therefore should not refill.
        if (fuel > 0) {
            return;
        }

        // we still primarily depend on burnable fuels, so the entire process starting only relies on an input fuel, water comes later
        int fuelVal = Math.round(StirlingFuelManager.instance().getEnergy(fuelSlot.getItemStack()) * energyMod);
        processTick = Math.min(baseProcessTick, fuelVal);
        fuelMax = fuelVal;
        fuel += fuelMax;
        fuelSlot.consume(1);
    }

    // separate check for water since we want to decrease it in specific intervals
    @Override
    protected int processTick() {
        // if we don't have enough water to do anything then don't bother
        // run first so that the super function can't consume additional fuel if we are out of water.
        if (waterFuelBuffer <= 0) {
            if (waterTank.getStored() >= Config.waterConsumptionAmount) {
                waterTank.modify(-Config.waterConsumptionAmount);
                waterFuelBuffer += (int) (Config.waterRFConsumptionAmount * energyMod);
            } else {
                // no water = no power haha
                isActive = false;
                return 0;
            }
        }

        waterFuelBuffer -= super.processTick();

        return 0;
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int i, Inventory inventory, Player player) {
        return new DynamoSteamMenu(i, level, worldPosition, inventory, player);
    }
}
