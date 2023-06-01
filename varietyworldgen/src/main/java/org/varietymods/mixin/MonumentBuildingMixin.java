package org.varietymods.mixin;

import net.minecraft.world.level.levelgen.structure.structures.OceanMonumentPieces;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(OceanMonumentPieces.MonumentBuilding.class)
public abstract class MonumentBuildingMixin {

    @ModifyConstant(
            method = "<init>(Lnet/minecraft/util/RandomSource;IILnet/minecraft/core/Direction;)V",
            constant = @Constant(intValue = 39, ordinal = 0)
    )
    private static int mixin(int in) {
        return 10;
    }
}