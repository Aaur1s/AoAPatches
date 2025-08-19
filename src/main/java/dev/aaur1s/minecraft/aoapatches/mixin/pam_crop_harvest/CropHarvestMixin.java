package dev.aaur1s.minecraft.aoapatches.mixin.pam_crop_harvest;

import dev.aaur1s.minecraft.aoapatches.patch.pam_crop_harvest.LogicKt;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import pam.pamhc2crops.events.harvest.CropHarvest;

@Mixin(CropHarvest.class)
public abstract class CropHarvestMixin {
    @Inject(method = "onCropHarvest", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/Block;getDrops(Lnet/minecraft/block/BlockState;Lnet/minecraft/world/server/ServerWorld;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/tileentity/TileEntity;)Ljava/util/List;"))
    private void invokeAoAHarvest(PlayerInteractEvent.RightClickBlock event, CallbackInfo ci) {
        LogicKt.handleCropHarvest(event);
    }
}
