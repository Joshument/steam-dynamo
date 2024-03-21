package dev.joshument.steamdynamo.common.inventory;

import cofh.core.common.inventory.BlockEntityCoFHMenu;
import cofh.core.util.ProxyUtils;
import cofh.lib.common.inventory.SlotCoFH;
import cofh.lib.common.inventory.wrapper.InvWrapperCoFH;
import cofh.thermal.lib.common.block.entity.DynamoBlockEntity;
import dev.joshument.steamdynamo.Registry;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public class DynamoSteamMenu extends BlockEntityCoFHMenu {
    public final DynamoBlockEntity tile;

    public DynamoSteamMenu(int windowId, Inventory inv, FriendlyByteBuf data) {
        this(windowId, ProxyUtils.getClientWorld(), data.readBlockPos(), inv, ProxyUtils.getClientPlayer());
    }

    public DynamoSteamMenu(int windowId, Level level, BlockPos pos, Inventory inventory, Player player) {
        super(Registry.Menus.DYNAMO_STEAM.get(), windowId, level, pos, inventory, player);
        this.tile = (DynamoBlockEntity) level.getBlockEntity(pos);

        InvWrapperCoFH tileInv = new InvWrapperCoFH(this.tile.getItemInv());

        addSlot(new SlotCoFH(tileInv, 0, 44, 35));

        bindAugmentSlots(tileInv, 1, this.tile.augSize());
        bindPlayerInventory(inventory);
    }
}
