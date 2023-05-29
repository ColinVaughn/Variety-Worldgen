package org.varietyworldgen;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;

@Config(name = "worldgenconfig")
public class WorldgenConfig implements ConfigData {
    @ConfigEntry.Category(value = "water")
    public boolean waterToggle = true;
    @ConfigEntry.Category(value = "water")
    public float waterStart = -100.0f;
    @ConfigEntry.Category(value = "water")
    public float waterEnd = 65.0f;
    @ConfigEntry.Category(value = "water")
    public float waterEndSwamp = 50.0f;
}