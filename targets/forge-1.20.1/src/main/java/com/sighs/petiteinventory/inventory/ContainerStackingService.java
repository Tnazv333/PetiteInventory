package com.sighs.petiteinventory.inventory;

import com.sighs.petiteinventory.inventory.ItemInventoryService;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

import java.util.List;

/** Shared stacking policy for container transfers and inventory fallback paths. */
public final class ContainerStackingService {
    private ContainerStackingService() {
    }

    public static boolean stackIntoExisting(ItemStack source, List<Slot> targets) {
        if (source.isEmpty() || !source.isStackable()) {
            return false;
        }

        boolean changed = false;
        for (Slot target : targets) {
            ItemStack targetStack = target.getItem();
            if (targetStack.isEmpty() || !ItemInventoryService.isSameItemIgnoreRotate(targetStack, source)) {
                continue;
            }
            changed |= stack(source, targetStack, target);
            if (source.isEmpty()) {
                return true;
            }
        }
        return changed;
    }

    public static boolean stack(ItemStack source, ItemStack target, Slot targetSlot) {
        int maximum = Math.min(targetSlot.getMaxStackSize(), source.getMaxStackSize());
        int amount = Math.min(source.getCount(), maximum - target.getCount());
        if (amount <= 0) {
            return false;
        }
        target.grow(amount);
        source.shrink(amount);
        targetSlot.setChanged();
        return true;
    }
}
