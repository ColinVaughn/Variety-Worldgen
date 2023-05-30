package org.varietyworldgen;

import eu.midnightdust.lib.config.MidnightConfig;

public class WorldgenConfig extends MidnightConfig {
    @Entry public static boolean waterToggle = true;
    @Entry(min=-200,max=200) public static float waterStartDeep = -100.0f;
    @Entry(min=-200,max=200)public static float waterStartShallow = -80.0f;
    @Entry(min=-200,max=200)public static float waterEndDeep = 65.0f;
    @Entry(min=-200,max=200)public static float waterEndShallow = 75.0f;
    @Entry(min=-200,max=200)public static float waterEndSwamp = 50.0f;
}