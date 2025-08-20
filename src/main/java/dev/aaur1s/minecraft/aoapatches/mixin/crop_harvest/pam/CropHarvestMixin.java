package dev.aaur1s.minecraft.aoapatches.mixin.crop_harvest.pam;

import dev.aaur1s.minecraft.aoapatches.patch.crop_harvest.LogicKt;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import pam.pamhc2crops.events.harvest.CropHarvest;

@Mixin(CropHarvest.class)
public abstract class CropHarvestMixin {
    @Inject(method = "onCropHarvest", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;setBlock(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;I)Z"))
    private void invokeAoAHarvest(PlayerInteractEvent.RightClickBlock event, CallbackInfo ci) {
        LogicKt.handleCropHarvest(event.getWorld(), event.getPos(), event.getPlayer());
    }
}
