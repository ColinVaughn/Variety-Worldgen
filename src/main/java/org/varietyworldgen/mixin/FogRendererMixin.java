package org.varietyworldgen.mixin;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.BackgroundRenderer;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.CameraSubmersionType;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.tag.BiomeTags;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.registry.RegistryEntry;
import net.minecraft.world.biome.Biome;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.varietyworldgen.Util.RenderSystemUtil;
import org.varietyworldgen.Varietyworldgen;

@Mixin(BackgroundRenderer.class)
public class FogRendererMixin {
    @Inject(method = "applyFog", at = @At("TAIL"))
    private static void afterSetupFog(Camera camera, BackgroundRenderer.FogType fogType, float viewDistance, boolean thickFog, float tickDelta, CallbackInfo ci) {
        CameraSubmersionType submersionType = camera.getSubmersionType();
        Entity entity = camera.getFocusedEntity();

        if (submersionType == CameraSubmersionType.WATER && Varietyworldgen.config.waterToggle) {
            float fogStart = viewDistance * Varietyworldgen.config.waterStartDeep * 0.01f;
            float fogEnd = viewDistance * Varietyworldgen.config.waterEndDeep * 0.01f;

            if (entity instanceof ClientPlayerEntity) {
                ClientPlayerEntity localPlayer = (ClientPlayerEntity) entity;
                RegistryEntry<Biome> biomeHolder = localPlayer.world.getBiome(localPlayer.getBlockPos());

                if (biomeHolder.isIn(BiomeTags.HAS_CLOSER_WATER_FOG)) {
                    fogEnd = viewDistance * Varietyworldgen.config.waterEndSwamp * 0.01f;
                }

                fogEnd *= Math.max(0.25f, localPlayer.getUnderwaterVisibility());

                float depth = (float) (localPlayer.getPos().y - localPlayer.world.getSeaLevel());
                float maxDepth = -20.0f;
                float depthFactor = MathHelper.clamp(depth / maxDepth, 0.0f, 1.0f);
                float smoothDepthFactor = smoothstep(0.0f, 1.0f, depthFactor);
                Vec3d waterColor = lerpColor(new Vec3d(0.237f, 0.237f, 0.237f), new Vec3d(0.198, 0.198, 0.198), smoothDepthFactor);
                RenderSystemUtil.setShaderFogColor(waterColor);
            }

            RenderSystem.setShaderFogStart(fogStart);
            RenderSystem.setShaderFogEnd(fogEnd);
            MinecraftClient.getInstance().worldRenderer.renderLightSky();
            MinecraftClient.getInstance().worldRenderer.renderDarkSky();
        }
    }

    @Shadow
    public static void clearFog() {
    }

    @Inject(method = "render", at = @At(value = "HEAD"), cancellable = true)
    private static void renderInject(Camera camera, float tickDelta, ClientWorld world, int viewDistance, float skyDarkness, CallbackInfo ci) {
        if (camera.getSubmersionType() == CameraSubmersionType.WATER && Varietyworldgen.config.waterToggle) {
            RenderSystemUtil.setClearColor(new Vec3d(0.211, 0.211, 0.211));
            clearFog();
            ci.cancel();
        }
    }

    // Changes the color of the seam/transition in the sky
    @Inject(method = "setFogBlack", at = @At("HEAD"), cancellable = true)
    private static void setFogBlackInject(CallbackInfo ci) {
        if (MinecraftClient.getInstance().gameRenderer.getCamera().getSubmersionType() == CameraSubmersionType.WATER && Varietyworldgen.config.waterToggle) {
            RenderSystemUtil.setShaderFogColor(new Vec3d(0.211, 0.211, 0.211));
            ci.cancel();
        }
    }

    private static float smoothstep(float edge0, float edge1, float x) {
        float t = MathHelper.clamp((x - edge0) / (edge1 - edge0), 0.0f, 1.0f);
        return t * t * (3.0f - 2.0f * t);
    }

    private static Vec3d lerpColor(Vec3d colorStart, Vec3d colorEnd, float t) {
        float r = MathHelper.lerp(t, (float) colorStart.x, (float) colorEnd.x);
        float g = MathHelper.lerp(t, (float) colorStart.y, (float) colorEnd.y);
        float b = MathHelper.lerp(t, (float) colorStart.z, (float) colorEnd.z);
        return new Vec3d(r, g, b);
    }
}
