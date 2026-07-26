package com.sighs.petiteinventory.platform;

import com.sighs.petiteinventory.Petiteinventory;
import com.sighs.petiteinventory.platform.NetworkChannel;
import com.sighs.petiteinventory.platform.RotateAreaPayload;
import com.sighs.petiteinventory.client.ModKeybindings;
import com.sighs.petiteinventory.inventory.ItemInventoryService;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.toasts.SystemToast;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.lwjgl.glfw.GLFW;

@Mod.EventBusSubscriber(modid = Petiteinventory.MODID, value = Dist.CLIENT)
public class KeyInput {

    private static long lastR = 0;

    @SubscribeEvent
    public static void copy(ScreenEvent.KeyReleased event) {
        if (!(event.getScreen() instanceof AbstractContainerScreen<?> screen)) return;

        int key = event.getKeyCode();

        if (key == ModKeybindings.KEY.getKey().getValue()) {
            if (screen.hoveredSlot == null) return;
            String menuType = screen.getMenu().getClass().toString();
            SystemToast.add(
                    Minecraft.getInstance().getToasts(),
                    SystemToast.SystemToastIds.TUTORIAL_HINT,
                    Component.translatable("toast.petiteinventory.copied.title"),
                    Component.translatable("toast.petiteinventory.copied.detail")
            );
            Minecraft.getInstance().keyboardHandler.setClipboard(menuType);
        }

        if (key == ModKeybindings.ROTATE.getKey().getValue()) {
            long now = System.currentTimeMillis();
            if (now - lastR < 150) return;
            lastR = now;

            ItemStack carried = screen.getMenu().getCarried();
            if (carried.isEmpty()) return;

            boolean nowRot = !ItemInventoryService.ItemRotateHelper.isRotated(carried);
            ItemInventoryService.ItemRotateHelper.setRotated(carried, nowRot);

            // -1 鐞涖劎銇氭Η鐘崇垼娑撳﹦娈戦悧鈺佹惂
            NetworkChannel.CHANNEL.sendToServer(new RotateAreaPayload(-1, nowRot));
        }
    }
}
