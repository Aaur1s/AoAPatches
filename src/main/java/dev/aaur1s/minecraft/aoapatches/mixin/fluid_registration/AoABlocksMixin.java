package dev.aaur1s.minecraft.aoapatches.mixin.fluid_registration;

import dev.aaur1s.minecraft.aoapatches.patch.fluid_registration.LogicKt;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.FlowingFluidBlock;
import net.minecraftforge.fluids.ForgeFlowingFluid;
import net.tslat.aoa3.common.registration.AoABlocks;
import net.tslat.aoa3.library.object.MutableSupplier;
import net.tslat.aoa3.util.FluidUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.function.BiFunction;
import java.util.function.Supplier;

@Mixin(AoABlocks.class)
public abstract class AoABlocksMixin {
    @Redirect(method = "<clinit>", at = @At(value = "INVOKE", target = "Lnet/tslat/aoa3/util/FluidUtil$Builder;customBlock(Ljava/util/function/BiFunction;)Lnet/tslat/aoa3/util/FluidUtil$Builder;"))
    private static FluidUtil.Builder invokeCorrectFluidCustomBlock(FluidUtil.Builder instance, BiFunction<MutableSupplier<ForgeFlowingFluid.Flowing>, AbstractBlock.Properties, Supplier<FlowingFluidBlock>> blockCreationFunction) {
        LogicKt.handleInvokeCustomBlock(instance, blockCreationFunction);
        return instance;
    }

}
