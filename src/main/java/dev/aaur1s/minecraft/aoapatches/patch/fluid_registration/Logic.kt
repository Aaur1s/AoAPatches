package dev.aaur1s.minecraft.aoapatches.patch.fluid_registration

import dev.aaur1s.minecraft.aoapatches.AoAPatchesMod
import dev.aaur1s.minecraft.aoapatches.AoAPatchesMod.Companion.unsafeCast
import net.minecraft.block.AbstractBlock
import net.minecraft.block.Block
import net.minecraft.block.FlowingFluidBlock
import net.minecraftforge.fluids.ForgeFlowingFluid
import net.minecraftforge.fml.RegistryObject
import net.minecraftforge.registries.DeferredRegister
import net.tslat.aoa3.library.`object`.MutableSupplier as AoAMutableSupplier
import net.tslat.aoa3.util.FluidUtil as AoAFluidUtil
import java.util.function.BiFunction
import java.util.function.Supplier

fun handleRegisterBlock(
    name: String,
    registry: DeferredRegister<Block>,
    defaultSupplier: Supplier<out FlowingFluidBlock>,
    fluidProperties: AbstractBlock.Properties,
    fluidSourceSupplier: AoAMutableSupplier<ForgeFlowingFluid.Source>,
    actualBlockCreationFunction: BiFunction<AoAMutableSupplier<ForgeFlowingFluid.Source>, AbstractBlock.Properties, Supplier<FlowingFluidBlock>>?,
): RegistryObject<FlowingFluidBlock> {
    val actualSupplier = actualBlockCreationFunction?.apply(fluidSourceSupplier, fluidProperties) ?: run {
        AoAPatchesMod.error("ActualBlockCreationFunction is not set by mixin for fluid $name")
        defaultSupplier
    }

    return registry.register(name, actualSupplier)
}

fun handleInvokeCustomBlock(
    builder: AoAFluidUtil.Builder,
    function: BiFunction<AoAMutableSupplier<ForgeFlowingFluid.Flowing>, AbstractBlock.Properties, Supplier<FlowingFluidBlock>>
) {
    builder.customBlock(function)
    (builder as FluidUtilBuilderPatchExtension).`AoAPatches$customBlock`(function.unsafeCast())
}