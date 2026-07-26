package com.sighs.petiteinventory.compat;

import com.sighs.petiteinventory.compat.Events;
import dev.latvian.mods.kubejs.KubeJSPlugin;

public class JSPlugin extends KubeJSPlugin {

    public void registerEvents() {
        Events.GROUP.register();
    }

}
