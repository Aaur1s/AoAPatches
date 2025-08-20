package dev.aaur1s.minecraft.aoapatches.mixin.crop_harvest.quark;

import dev.aaur1s.minecraft.aoapatches.patch.crop_harvest.LogicKt;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import vazkii.quark.content.tweaks.module.SimpleHarvestModule;

@Mixin(SimpleHarvestModule.class)
public abstract class SimpleHarvestModuleMixin {
    @Inject(method = "replant", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;setBlockAndUpdate(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;)Z"))
    private static void invokeAoAHarvest(World world, BlockPos pos, BlockState inWorld, PlayerEntity playerEntity, CallbackInfo ci) {
        LogicKt.handleCropHarvest(world, pos, playerEntity);
    }
}
