package me.vaan.treefeller

import org.bukkit.Material
import org.bukkit.Tag
import org.bukkit.block.Block
import java.util.EnumSet

object Utils {
    private val trunkBlocks = EnumSet.noneOf(Material::class.java)
    private val leafBlocks = EnumSet.noneOf(Material::class.java)

    init {
        trunkBlocks.addAll(Tag.LOGS.values)

        leafBlocks.addAll(Tag.LEAVES.values)
        leafBlocks.add(Material.SHROOMLIGHT)
    }

    fun isTrunk(m: Material): Boolean {
        return trunkBlocks.contains(m)
    }

    fun isLeaf(m: Material): Boolean {
        return leafBlocks.contains(m)
    }

    fun getSurroundingBlocks(block: Block): List<Block> {
        val blocks: MutableList<Block> = ArrayList()
        for (x in -1..1) {
            for (z in -1..1) {
                for (y in 1 downTo -1) {
                    if (x == 0 && y == 0 && z == 0) {
                        continue
                    }
                    blocks.add(block.getRelative(x, y, z))
                }
            }
        }
        return blocks
    }
}