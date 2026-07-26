package com.sighs.petiteinventory.platform;

import com.sighs.petiteinventory.Petiteinventory;
import com.sighs.petiteinventory.config.ItemSizeRuleCache;
import net.minecraftforge.event.CommandEvent;
import net.minecraftforge.event.level.LevelEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Petiteinventory.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ReloadEvent {
    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onCommand(CommandEvent event) {
        String rawCommand = event.getParseResults().getReader().getString();
        if (rawCommand.equals("reload")) ItemSizeRuleCache.loadAllRule();
    }

    @SubscribeEvent
    public static void onLoad(LevelEvent.Load event) {
        ItemSizeRuleCache.loadAllRule();
    }
}
