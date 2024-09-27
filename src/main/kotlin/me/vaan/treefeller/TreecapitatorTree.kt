package me.vaan.treefeller

import org.bukkit.block.Block

class TreecapitatorTree(private val originalBlock: Block) {
    private var trunkBroken = 0
    private var leavesBroken = 0
    private var maxTrunkBlocks = 0
    private var maxLeavesBlock = 0

    init {
        setMaxBlocks()
    }

    fun getOriginalBlock(): Block {
        return originalBlock
    }

    fun getTrunkBroken(): Int {
        return trunkBroken
    }

    fun incrementTrunkBroken() {
        trunkBroken++
    }

    fun incrementLeavesBroken() {
        leavesBroken++
    }

    fun getMaxTrunkBlocks(): Int {
        return maxTrunkBlocks
    }

    fun getMaxLeavesBlock(): Int {
        return maxLeavesBlock
    }

    fun getLeavesBroken(): Int {
        return leavesBroken
    }

    private fun setMaxBlocks() {
        val matName = originalBlock.type.toString()

        when {
            matName.contains("OAK") || matName.contains("BIRCH") || matName.contains("ACACIA") || matName.contains("CHERRY") -> {
                maxTrunkBlocks = 100
                maxLeavesBlock = 200
            }
            matName.contains("SPRUCE") || matName.contains("CRIMSON") || matName.contains("WARPED") || matName.contains("MANGROVE") -> {
                maxTrunkBlocks = 120
                maxLeavesBlock = 400
            }
            matName.contains("JUNGLE") -> {
                maxTrunkBlocks = 200
                maxLeavesBlock = 400
            }
            matName.contains("DARK_OAK") -> {
                maxTrunkBlocks = 100
                maxLeavesBlock = 250
            }
        }
    }
}