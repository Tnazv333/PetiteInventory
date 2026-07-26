package com.sighs.petiteinventory.platform.mixin;

import com.sighs.petiteinventory.inventory.InventoryAdmissionResult;
import com.sighs.petiteinventory.inventory.InventoryAdmissionService;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/** Forge adapter: intercepts pickup and delegates all policy to the defense module. */
@Mixin(Inventory.class)
public abstract class InventoryMixin {
    @Shadow @Final public Player player;

    @Inject(method = "add(Lnet/minecraft/world/item/ItemStack;)Z", at = @At("HEAD"), cancellable = true)
    private void interceptIncomingStack(ItemStack incoming, CallbackInfoReturnable<Boolean> callback) {
        InventoryAdmissionResult result = InventoryAdmissionService.admit(player, incoming);
        if (result == InventoryAdmissionResult.ACCEPTED) {
            callback.setReturnValue(true);
        } else if (result == InventoryAdmissionResult.REJECTED) {
            callback.setReturnValue(false);
        }
        // DEFER_TO_VANILLA intentionally leaves the original method untouched.
    }
}
