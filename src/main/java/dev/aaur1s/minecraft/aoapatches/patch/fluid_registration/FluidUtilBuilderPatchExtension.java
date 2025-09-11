package dev.aaur1s.minecraft.aoapatches.patch.fluid_registration;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.FlowingFluidBlock;
import net.minecraftforge.fluids.ForgeFlowingFluid;
import net.tslat.aoa3.library.object.MutableSupplier;

import java.util.function.BiFunction;
import java.util.function.Supplier;

public interface FluidUtilBuilderPatchExtension {
    void AoAPatches$customBlock(BiFunction<MutableSupplier<ForgeFlowingFluid.Source>, AbstractBlock.Properties, Supplier<FlowingFluidBlock>> function);
}
