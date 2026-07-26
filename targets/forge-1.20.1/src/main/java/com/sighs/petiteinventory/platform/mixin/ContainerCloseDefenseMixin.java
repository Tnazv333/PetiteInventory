package com.sighs.petiteinventory.platform.mixin;

import com.sighs.petiteinventory.inventory.InventoryAdmissionService;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/** Adapter for the carried-stack return path when a container closes. */
@Mixin(value = AbstractContainerMenu.class, priority = 1100)
public abstract class ContainerCloseDefenseMixin {
    @Shadow public abstract ItemStack getCarried();

    @Shadow public abstract void setCarried(ItemStack stack);

    @Inject(method = "removed", at = @At("HEAD"), cancellable = true)
    private void returnCarriedStackThroughDefense(Player player, CallbackInfo callback) {
        if (player.level().isClientSide || getCarried().isEmpty()) {
            return;
        }

        ItemStack carried = getCarried();
        setCarried(ItemStack.EMPTY);
        InventoryAdmissionService.returnCarriedItem(player, carried);
        callback.cancel();
    }
}
