package org.varietyworldgen.mixin;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Camera;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.FogRenderer;
import net.minecraft.client.renderer.FogRenderer.FogMode;
import net.minecraft.core.Holder;
import net.minecraft.tags.BiomeTags;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.material.FogType;

import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import org.varietyworldgen.Util.RenderSystemUtil;
import org.varietyworldgen.Varietyworldgen;

@Mixin(FogRenderer.class)
public class FogRendererMixin {
    @Inject(at = @At("TAIL"), method = "setupFog")
    private static void afterSetupFog(Camera camera, FogMode fogMode, float viewDistance, boolean thickFog, float partialTick, CallbackInfo info) {
        FogType fogType = camera.getFluidInCamera();
        Entity entity = camera.getEntity();
        boolean mobEffect = entity instanceof LivingEntity && (((LivingEntity) entity).hasEffect(MobEffects.BLINDNESS) || ((LivingEntity) entity).hasEffect(MobEffects.DARKNESS));

        if (fogType == FogType.WATER && Varietyworldgen.config.waterToggle) {
            overrideWaterFog(viewDistance, entity);
        }
    }

    private static void overrideWaterFog(float viewDistance, Entity entity) {
        float fogStart = viewDistance * Varietyworldgen.config.waterStart * 0.01f;
        float fogEnd = viewDistance * Varietyworldgen.config.waterEnd * 0.01f;

        if (entity instanceof LocalPlayer) {
            LocalPlayer localPlayer = (LocalPlayer) entity;
            Holder<Biome> biomeHolder = localPlayer.level.getBiome(localPlayer.blockPosition());

            if (biomeHolder.is(BiomeTags.HAS_CLOSER_WATER_FOG)) {
                fogEnd = viewDistance * Varietyworldgen.config.waterEndSwamp * 0.01f;
            }

            fogEnd *= Math.max(0.25f, localPlayer.getWaterVision());
        }

        RenderSystem.setShaderFogStart(fogStart);
        RenderSystem.setShaderFogEnd(fogEnd);

    }
}
