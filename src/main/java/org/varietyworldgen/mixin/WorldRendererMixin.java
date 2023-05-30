package org.varietyworldgen.mixin;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
@Mixin(WorldRenderer.class)
public class WorldRendererMixin {
    // The amount of transition between the top of the sky and the bottom of the sky
    @ModifyVariable(method = "renderSky(Lnet/minecraft/client/render/BufferBuilder;F)Lnet/minecraft/client/render/BufferBuilder$BuiltBuffer;", at = @At(value = "INVOKE_ASSIGN", target = "Ljava/lang/Math;signum(F)F"), argsOnly = true)
    private static float renderSkyBufferBuilderInject(float g) {
        return Math.signum(16.0f) * 8.0f;
    }

    // Changes the color of the top of the sky
    @ModifyVariable(method = "renderSky(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/util/math/Matrix4f;FLnet/minecraft/client/render/Camera;ZLjava/lang/Runnable;)V", at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/client/world/ClientWorld;getSkyColor(Lnet/minecraft/util/math/Vec3d;F)Lnet/minecraft/util/math/Vec3d;"))
    @SuppressWarnings("InvalidInjectorMethodSignature")
    public Vec3d renderSkyInject(Vec3d original) {
        var world = MinecraftClient.getInstance().world;
        if (world == null) return original;



        return original.add(new Vec3d(0,0,0));
    }
}