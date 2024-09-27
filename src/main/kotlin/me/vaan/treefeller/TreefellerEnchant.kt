package me.vaan.treefeller

import org.bukkit.Tag
import org.bukkit.block.Block
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.scheduler.BukkitRunnable
import su.nightexpress.excellentenchants.EnchantsPlugin
import su.nightexpress.excellentenchants.api.enchantment.TradeType
import su.nightexpress.excellentenchants.api.enchantment.type.BlockBreakEnchant
import su.nightexpress.excellentenchants.enchantment.impl.EnchantDefinition
import su.nightexpress.excellentenchants.enchantment.impl.EnchantDistribution
import su.nightexpress.excellentenchants.enchantment.impl.GameEnchantment
import su.nightexpress.excellentenchants.rarity.EnchantRarity
import su.nightexpress.excellentenchants.util.EnchantUtils
import su.nightexpress.excellentenchants.util.ItemCategories
import su.nightexpress.nightcore.config.FileConfig
import java.io.File


class TreefellerEnchant(plugin: EnchantsPlugin, file: File) :
    GameEnchantment(plugin, file, this.definition(), EnchantDistribution.treasure(TradeType.SAVANNA_SPECIAL)),
    BlockBreakEnchant {

    companion object {
        val ID: java.lang.String = java.lang.String("tree_capitator")

        @JvmStatic
        fun definition(): EnchantDefinition {
            return EnchantDefinition.create(
                "Grants the ability to fell trees. Shift to not activate the ability.",
                EnchantRarity.RARE,
                1,
                ItemCategories.AXE
            )
        }
    }

    override fun loadAdditional(p0: FileConfig) { }
    override fun onBreak(event: BlockBreakEvent, entity: LivingEntity, item: ItemStack, level: Int): Boolean {
        if (entity !is Player) return false
        if (EnchantUtils.isBusy()) return false

        val block = event.block
        if (!Tag.LOGS.isTagged(block.type)) return false

        breakTree(block)
        return true
    }

    private fun breakTree(block: Block) {
        breakBlock(block, TreecapitatorTree(block))
    }

    private fun breakBlock(block: Block, tree: TreecapitatorTree) {
        val brokenEnoughTrunks = tree.getTrunkBroken() > tree.getMaxTrunkBlocks()
        val brokenEnoughLeaves = tree.getLeavesBroken() > tree.getMaxLeavesBlock()

        if (brokenEnoughTrunks && brokenEnoughLeaves) return

        for (adjacentBlock in Utils.getSurroundingBlocks(block)) {
            val mat = adjacentBlock.type
            val isTrunk: Boolean = Utils.isTrunk(mat)
            val isLeaf: Boolean = Utils.isLeaf(mat)

            if (!isTrunk && !isLeaf) continue

            if (brokenEnoughTrunks && isTrunk || brokenEnoughLeaves && isLeaf) {
                continue
            }

            adjacentBlock.breakNaturally()

            if (isTrunk) {
                tree.incrementTrunkBroken()
            } else {
                tree.incrementLeavesBroken()
            }

            // Continue breaking blocks
            val originalBlock: Block = tree.getOriginalBlock()
            if (adjacentBlock.x > originalBlock.x + 6 || adjacentBlock.z > originalBlock.z + 6 || adjacentBlock.y > originalBlock.y + 31) {
                return
            }

            // Break the next blocks
            object : BukkitRunnable() {
                override fun run() {
                    breakBlock(adjacentBlock, tree)
                }
            }.runTaskLater(TreefellerEnchantPlugin.instance, 1L)
        }
    }
}