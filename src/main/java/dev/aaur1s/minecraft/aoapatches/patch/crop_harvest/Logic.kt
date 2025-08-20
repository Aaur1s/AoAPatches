package dev.aaur1s.minecraft.aoapatches.patch.crop_harvest

import net.minecraft.block.CropsBlock
import net.minecraft.block.NetherWartBlock
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.player.ServerPlayerEntity
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import net.tslat.aoa3.common.registration.custom.AoASkills
import net.tslat.aoa3.util.BlockUtil as AoABlockUtil
import net.tslat.aoa3.util.PlayerUtil as AoAPlayerUtil

/**
 * Ported logic from [this](https://github.com/Tslat/Advent-Of-Ascension/blob/e97502da8bdd6081b6acc4635f03296d3e2e69a0/source/player/skill/FarmingSkill.java#L32) method
 */
fun handleCropHarvest(world: World, pos: BlockPos, player: PlayerEntity) {
    if (world.isClientSide) return

    val player = player as ServerPlayerEntity
    val blockState = world.getBlockState(pos)
    val block = blockState.block

    with(AoAPlayerUtil.getSkill(player, AoASkills.FARMING.get())) {
        val canGainXp = canGainXp(true)
        val canHarvest = AoABlockUtil.canPlayerHarvest(blockState, player, world, pos)
        if (!canGainXp || !canHarvest) return

        val xpTime = when (block) {
            is CropsBlock if (block.isMaxAge(blockState)) -> 7f * block.maxAge
            is NetherWartBlock if (NetherWartBlock.AGE.value(blockState).value() == 3) -> 21f
            else -> 0f
        }

        if (xpTime > 0f) {
            adjustXp(AoAPlayerUtil.getTimeBasedXpForLevel(getLevel(true), xpTime), false, false)
        }
    }
}