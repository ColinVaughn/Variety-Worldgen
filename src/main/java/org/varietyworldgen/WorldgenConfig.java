package org.varietyworldgen;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;

@Config(name = "fogconfig")
public class FogConfig implements ConfigData {
    @ConfigEntry.Category(value = "terrain")
    public boolean terrainToggle = true;
    @ConfigEntry.Category(value = "terrain")
    public float terrainStart = 70.0f;
    @ConfigEntry.Category(value = "terrain")
    public float terrainEnd = 130.0f;

    @ConfigEntry.Category(value = "water")
    public boolean waterToggle = true;
    @ConfigEntry.Category(value = "water")
    public float waterStart = -20.0f;
    @ConfigEntry.Category(value = "water")
    public float waterEnd = 90.0f;
    @ConfigEntry.Category(value = "water")
    public float waterEndSwamp = 60.0f;

    @ConfigEntry.Category(value = "nether")
    public boolean netherToggle = true;
    @ConfigEntry.Category(value = "nether")
    public float netherStart = 5.0f;
    @ConfigEntry.Category(value = "nether")
    public float netherEnd = 80.0f;

    @ConfigEntry.Category(value = "lava")
    public boolean lavaToggle = true;
    @ConfigEntry.Category(value = "lava")
    public float lavaStart = 0.0f;
    @ConfigEntry.Category(value = "lava")
    public float lavaEnd = 10.0f;
    @ConfigEntry.Category(value = "lava")
    public float lavaEndFireResistance = 30.0f;
    @ConfigEntry.Category(value = "lava")
    public float lavaEndSpectator = 50.0f;
}