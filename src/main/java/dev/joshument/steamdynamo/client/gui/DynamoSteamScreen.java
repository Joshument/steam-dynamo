package dev.joshument.steamdynamo.client.gui;

import cofh.core.client.gui.IGuiAccess;
import cofh.core.client.gui.element.ElementFluidStorage;
import cofh.core.util.helpers.GuiHelper;
import cofh.lib.common.fluid.FluidStorageCoFH;
import cofh.lib.util.helpers.StringHelper;
import cofh.thermal.core.client.gui.ThermalGuiHelper;
import dev.joshument.steamdynamo.common.block.entity.DynamoSteamBlockEntity;
import dev.joshument.steamdynamo.common.inventory.DynamoSteamMenu;
import cofh.thermal.lib.client.gui.DynamoScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

import static cofh.core.util.helpers.GuiHelper.*;
import static cofh.lib.util.Constants.PATH_ELEMENTS;
import static cofh.lib.util.constants.ModIds.ID_THERMAL;

public class DynamoSteamScreen extends DynamoScreen<DynamoSteamMenu> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(ID_THERMAL, "textures/gui/container/item_dynamo.png");
    private static final ResourceLocation TEXTURE_TURBINE = new ResourceLocation(ID_THERMAL, "textures/gui/container/fluid_dynamo.png");

    public DynamoSteamScreen(DynamoSteamMenu container, Inventory inv, Component _title) {
        super(container, inv, container.tile, StringHelper.getTextComponent("block.steamdynamo.steam_dynamo"));
        texture = ((DynamoSteamBlockEntity) container.tile).isTurbine() ? TEXTURE_TURBINE : TEXTURE;
        info = GuiHelper.appendLine(GuiHelper.generatePanelInfo("info.steamdynamo.steam_dynamo"), "info.thermal.dynamo.throttle");
    }

    public void init() {
        super.init();
        addElement(setClearable(createMediumFluidStorage(this, 16, 22, tile.getTank(0)), tile, 0));
        addElement(ThermalGuiHelper.createDefaultDuration(this, 80, 35, SCALE_FLAME, tile));
    }
}
