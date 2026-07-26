package com.sighs.petiteinventory.inventory;

import com.sighs.petiteinventory.inventory.InventorySlotService;
import com.sighs.petiteinventory.inventory.ItemInventoryService;
import com.sighs.petiteinventory.inventory.Area;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

/**
 * The single policy gate for any stack entering a player's inventory.
 * Adapters must not duplicate stacking, rotation, footprint or fallback rules.
 */
public final class InventoryAdmissionService {
    private static final int HOTBAR_END = 9;
    private static final int MAIN_INVENTORY_START = 9;
    private static final int MAIN_INVENTORY_END = 36;

    private InventoryAdmissionService() {
    }

    public static InventoryAdmissionResult admit(Player player, ItemStack incoming) {
        if (incoming.isEmpty() || stackIntoExisting(player.getInventory(), incoming)) {
            return InventoryAdmissionResult.ACCEPTED;
        }

        Area area = ItemInventoryService.getArea(incoming);
        if (area.width() == 1 && area.height() == 1) {
            ItemInventoryService.ItemRotateHelper.setRotated(incoming, false);
            return stackIntoExisting(player.getInventory(), incoming)
                    ? InventoryAdmissionResult.ACCEPTED
                    : InventoryAdmissionResult.DEFER_TO_VANILLA;
        }

        Inventory inventory = player.getInventory();
        int targetSlot = findAreaSlot(player, incoming, area);
        if (targetSlot < 0) {
            return InventoryAdmissionResult.REJECTED;
        }

        inventory.setItem(targetSlot, incoming.copy());
        incoming.setCount(0);
        return InventoryAdmissionResult.ACCEPTED;
    }

    /** Applies the same normalization rules when a trusted server action sets a slot. */
    public static ItemStack normalizeForSlot(int slotIndex, ItemStack stack) {
        ItemStack normalized = stack.copy();
        if (slotIndex >= 0 && slotIndex < HOTBAR_END) {
            ItemInventoryService.ItemRotateHelper.setRotated(normalized, false);
        }
        return normalized;
    }

    /**
     * Handles a carried item when a container closes. No path may silently lose
     * an item: a rejected stack is dropped only after all valid placements fail.
     */
    public static void returnCarriedItem(Player player, ItemStack carried) {
        ItemStack remaining = carried.copy();
        InventoryAdmissionResult result = admit(player, remaining);
        if (result == InventoryAdmissionResult.ACCEPTED) {
            return;
        }

        if (result == InventoryAdmissionResult.DEFER_TO_VANILLA && placeOneByOneFallback(player.getInventory(), remaining)) {
            return;
        }

        player.drop(remaining, false);
    }

    private static int findAreaSlot(Player player, ItemStack stack, Area area) {
        int slot = InventorySlotService.findSlotIndexForArea(player, area);
        if (slot >= 0) {
            return slot;
        }

        boolean rotated = ItemInventoryService.ItemRotateHelper.isRotated(stack);
        ItemInventoryService.ItemRotateHelper.setRotated(stack, !rotated);
        Area rotatedArea = ItemInventoryService.getArea(stack);
        slot = InventorySlotService.findSlotIndexForArea(player, rotatedArea);
        if (slot < 0) {
            ItemInventoryService.ItemRotateHelper.setRotated(stack, rotated);
        }
        return slot;
    }

    private static boolean stackIntoExisting(Inventory inventory, ItemStack incoming) {
        for (int slotIndex = 0; slotIndex < MAIN_INVENTORY_END; slotIndex++) {
            ItemStack existing = inventory.getItem(slotIndex);
            if (existing.isEmpty() || !ItemInventoryService.isSameItemIgnoreRotate(existing, incoming)) {
                continue;
            }

            int maximum = Math.min(inventory.getMaxStackSize(), incoming.getMaxStackSize());
            int amount = Math.min(incoming.getCount(), maximum - existing.getCount());
            if (amount > 0) {
                existing.grow(amount);
                incoming.shrink(amount);
                if (incoming.isEmpty()) {
                    return true;
                }
            }
        }
        return incoming.isEmpty();
    }

    private static boolean placeOneByOneFallback(Inventory inventory, ItemStack stack) {
        for (int slotIndex = MAIN_INVENTORY_START; slotIndex < MAIN_INVENTORY_END; slotIndex++) {
            if (inventory.getItem(slotIndex).isEmpty()) {
                inventory.setItem(slotIndex, stack.copy());
                return true;
            }
        }
        for (int slotIndex = 0; slotIndex < HOTBAR_END; slotIndex++) {
            if (inventory.getItem(slotIndex).isEmpty()) {
                inventory.setItem(slotIndex, stack.copy());
                return true;
            }
        }
        return false;
    }
}
