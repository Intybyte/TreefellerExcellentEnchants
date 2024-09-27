package me.vaan.treefeller

import org.bukkit.Bukkit
import org.bukkit.plugin.java.JavaPlugin
import su.nightexpress.excellentenchants.EnchantsPlugin
import su.nightexpress.excellentenchants.enchantment.impl.GameEnchantment
import java.io.File
import java.util.function.Function

class TreefellerEnchantPlugin : JavaPlugin() {

    companion object {
        private lateinit var _instance: TreefellerEnchantPlugin

        val instance: TreefellerEnchantPlugin
            get() {
                return _instance
            }
    }

    override fun onEnable() {
        _instance = this
        val enchantsPlugin = Bukkit.getPluginManager().getPlugin("ExcellentEnchants") as EnchantsPlugin
        enchantsPlugin.enchantNMS.unfreezeRegistry()
        val registry = enchantsPlugin.registry

        val fileHandler: Function<File, GameEnchantment> = Function<File, GameEnchantment> {
            t: File ->  TreefellerEnchant(enchantsPlugin, t)
        }

        val registerMethod = registry.javaClass.getDeclaredMethod("register", String::class.java, Function::class.java)
        registerMethod.isAccessible = true

        registerMethod.invoke(registry, TreefellerEnchant.ID, fileHandler)
        enchantsPlugin.enchantNMS.freezeRegistry()
    }
}
