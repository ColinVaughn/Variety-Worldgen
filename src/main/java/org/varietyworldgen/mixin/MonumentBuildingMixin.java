package org.varietyworldgen.mixin;

import net.minecraft.structure.OceanMonumentGenerator;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(OceanMonumentGenerator.Base.class)
public abstract class MonumentBuildingMixin {

    @ModifyConstant(
            method = "<init>(Ljava/util/Random;IILnet/minecraft/util/math/Direction;)V",
            constant = @Constant(intValue = 39, ordinal = 0)
    )
    private static int mixin(int in) {
        return 10;
    }
}