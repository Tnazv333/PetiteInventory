package com.sighs.petiteinventory.platform;

import com.sighs.petiteinventory.Petiteinventory;
import com.sighs.petiteinventory.platform.EditModeCommand;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Petiteinventory.MODID)
public final class CommandRegistrationEvent {
    @SubscribeEvent
    public static void onCommandRegistration(RegisterCommandsEvent event) {
        EditModeCommand.register(event.getDispatcher());
    }
}
