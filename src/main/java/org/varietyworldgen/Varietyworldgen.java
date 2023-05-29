package org.varietyworldgen;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;
import net.fabricmc.api.ModInitializer;

public class Varietyworldgen implements ModInitializer {
    public static WorldgenConfig config;

    @Override
    public void onInitialize() {
        AutoConfig.register(WorldgenConfig.class, GsonConfigSerializer::new);
        config = AutoConfig.getConfigHolder(WorldgenConfig.class).getConfig();
    }
}