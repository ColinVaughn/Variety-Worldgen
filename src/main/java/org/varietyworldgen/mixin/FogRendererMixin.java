package org.varietyworldgen.mixin;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.BackgroundRenderer;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.CameraSubmersionType;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.tag.BiomeTags;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.registry.RegistryEntry;
import net.minecraft.world.biome.Biome;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.varietyworldgen.Util.MathUtil;
import org.varietyworldgen.Util.RenderSystemUtil;
import org.varietyworldgen.Varietyworldgen;

@Mixin(BackgroundRenderer.class)
public class FogRendererMixin {
    @Inject(method = "applyFog", at = @At("TAIL"))
    private static void afterSetupFog(Camera camera, BackgroundRenderer.FogType fogType, float viewDistance, boolean thickFog, float tickDelta, CallbackInfo ci) {
        CameraSubmersionType submersionType = camera.getSubmersionType();
        Entity entity = camera.getFocusedEntity();
        boolean mobEffect = entity instanceof LivingEntity && (((LivingEntity) entity).hasStatusEffect(StatusEffects.BLINDNESS) || ((LivingEntity) entity).hasStatusEffect(StatusEffects.DARKNESS));

        if (submersionType == CameraSubmersionType.WATER && Varietyworldgen.config.waterToggle) {
            overrideWaterFog(viewDistance, entity);
        }
    }

    private static void overrideWaterFog(float viewDistance, Entity entity) {
        float fogStart = viewDistance * Varietyworldgen.config.waterStart * 0.01f;
        float fogEnd = viewDistance * Varietyworldgen.config.waterEnd * 0.01f;

        if (entity instanceof ClientPlayerEntity) {
            ClientPlayerEntity localPlayer = (ClientPlayerEntity) entity;
            RegistryEntry<Biome> biomeHolder = localPlayer.world.getBiome(localPlayer.getBlockPos());

            if (biomeHolder.isIn(BiomeTags.HAS_CLOSER_WATER_FOG)) {
                fogEnd = viewDistance * Varietyworldgen.config.waterEndSwamp * 0.01f;
            }

            fogEnd *= Math.max(0.25f, localPlayer.getUnderwaterVisibility());
        }

        RenderSystem.setShaderFogStart(fogStart);
        RenderSystem.setShaderFogEnd(fogEnd);
        RenderSystemUtil.setShaderFogColor(new Vec3d(0.211, 0.211, 0.211));
        MinecraftClient.getInstance().worldRenderer.renderLightSky();
        MinecraftClient.getInstance().worldRenderer.renderDarkSky();
    }

    private static float vanillaFogStart(float viewDistance) {
        float f = MathUtil.clamp(viewDistance / 10.0f, 4.0f, 64.0f);
        return viewDistance - f;
    }

    @Shadow
    public static void clearFog() {
    }

    @Inject(method = "render", at = @At(value = "HEAD"), cancellable = true)
    private static void renderInject(Camera camera, float tickDelta, ClientWorld world, int viewDistance, float skyDarkness, CallbackInfo ci) {
        if (camera.getSubmersionType() == CameraSubmersionType.WATER) {
            RenderSystemUtil.setClearColor(new Vec3d(0.211, 0.211, 0.211));
            clearFog();
            ci.cancel();
        }
    }

    // Changes the color of the seam/transition in the sky
    @Inject(method = "setFogBlack", at = @At("HEAD"), cancellable = true)
    private static void setFogBlackInject(CallbackInfo ci) {
        if (MinecraftClient.getInstance().gameRenderer.getCamera().getSubmersionType() == CameraSubmersionType.WATER) {
            RenderSystemUtil.setShaderFogColor(new Vec3d(0.211, 0.211, 0.211));
            ci.cancel();
        }
    }
}

