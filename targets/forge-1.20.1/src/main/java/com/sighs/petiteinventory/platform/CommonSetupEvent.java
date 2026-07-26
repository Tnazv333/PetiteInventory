package com.sighs.petiteinventory.platform;

import com.sighs.petiteinventory.Petiteinventory;
import com.sighs.petiteinventory.compat.KubeJSCompat;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, modid = Petiteinventory.MODID)
public class CommonSetupEvent {
    @SubscribeEvent
    public static void setup(FMLCommonSetupEvent event) {
        event.enqueueWork(KubeJSCompat::init);
    }
}
