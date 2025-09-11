package dev.aaur1s.minecraft.aoapatches

import net.minecraftforge.fml.common.Mod
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger

const val MOD_ID = "aoapatches"

@Mod(MOD_ID)
class AoAPatchesMod {
    companion object : Logger by LogManager.getLogger(AoAPatchesMod::class.java) {
        @Suppress("UNCHECKED_CAST")
        fun <T> Any.unsafeCast() = this as T
    }
}