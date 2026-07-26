package com.sighs.petiteinventory.platform;

import com.sighs.petiteinventory.Petiteinventory;
import com.sighs.petiteinventory.platform.IAbstractContainerMenu;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Petiteinventory.MODID)
public class ServerEvent {
    @SubscribeEvent
    public static void tick(TickEvent.PlayerTickEvent event) {
        ((IAbstractContainerMenu) event.player.containerMenu).setPlayer(event.player);
    }
}
