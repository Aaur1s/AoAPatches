package dev.aaur1s.minecraft.aoapatches.mixin.fluid_registration;

import dev.aaur1s.minecraft.aoapatches.AoAPatchesMod;
import dev.aaur1s.minecraft.aoapatches.patch.fluid_registration.FluidUtilBuilderPatchExtension;
import dev.aaur1s.minecraft.aoapatches.patch.fluid_registration.LogicKt;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.FlowingFluidBlock;
import net.minecraft.block.material.Material;
import net.minecraftforge.fluids.ForgeFlowingFluid;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.tslat.aoa3.library.object.MutableSupplier;
import net.tslat.aoa3.util.FluidUtil;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.function.BiFunction;
import java.util.function.Supplier;

@Mixin(FluidUtil.Builder.class)
public abstract class FluidUtilBuilderMixin implements FluidUtilBuilderPatchExtension {
    @Shadow
    private Material material;
    @Shadow
    private int luminosity;
    @Shadow
    @Final
    private MutableSupplier<ForgeFlowingFluid.Source> sourceFluid;
    @Unique private BiFunction<MutableSupplier<ForgeFlowingFluid.Source>, AbstractBlock.Properties, Supplier<FlowingFluidBlock>> AoAPatches$actualBlockCreationFunction = null;

    @Override
    public void AoAPatches$customBlock(@NotNull BiFunction<MutableSupplier<ForgeFlowingFluid.Source>, AbstractBlock.Properties, Supplier<FlowingFluidBlock>> function) {
        AoAPatches$actualBlockCreationFunction = function;
    }

    @Redirect(method = "registerBlock", at = @At(value = "INVOKE", target = "Lnet/minecraftforge/registries/DeferredRegister;register(Ljava/lang/String;Ljava/util/function/Supplier;)Lnet/minecraftforge/fml/RegistryObject;"), remap = false)
    private RegistryObject<FlowingFluidBlock> registerBlockWithCorrectCreationFunction(DeferredRegister<Block> instance, String ret, Supplier<? extends FlowingFluidBlock> supplier) {
        return LogicKt.handleRegisterBlock(
                ret,
                instance,
                supplier,
                AbstractBlock.Properties.of(this.material)
                        .noCollission()
                        .strength(100.0F)
                        .noDrops()
                        .lightLevel((state) -> this.luminosity),
                this.sourceFluid,
                AoAPatches$actualBlockCreationFunction
        );
    }
}
