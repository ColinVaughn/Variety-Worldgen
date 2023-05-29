package org.varietyworldgen.Util;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.world.phys.Vec3;

public class RenderSystemUtil {
    public static void setShaderFogColor(Vec3 fogColor) {
        RenderSystem.setShaderFogColor((float) fogColor.x, (float) fogColor.y, (float) fogColor.z, 1f);
    }

    public static void setClearColor(Vec3 clearColor) {
        RenderSystem.clearColor((float) clearColor.x, (float) clearColor.y, (float) clearColor.z, 1f);
    }
}