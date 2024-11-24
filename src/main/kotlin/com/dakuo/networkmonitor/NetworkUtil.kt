package com.dakuo.networkmonitor

import org.bukkit.entity.Player
import taboolib.common.reflect.ClassHelper
import taboolib.library.reflex.Reflex.Companion.getProperty
import taboolib.library.reflex.Reflex.Companion.invokeMethod
import taboolib.module.nms.MinecraftVersion.isUniversal
import taboolib.module.nms.MinecraftVersion.minecraftVersion

object NetworkUtil {

    fun getNetworkManager(player: Player): Any {
        val cbPlayer = getCBPlayer()!!.cast(player)
        val nmsPlayer = cbPlayer.invokeMethod<Any>("getHandle")!!
        val connection = nmsPlayer.getProperty<Any>("connection")!!
        return connection.getProperty<Any>("connection", findToParent = true)!!
    }

    fun getCBPlayer(): Class<*>? {
        val version = minecraftVersion
        val clazz = if (version == "UNKNOWN") {
            "org.bukkit.craftbukkit.entity.CraftPlayer"
        } else {
            "org.bukkit.craftbukkit.$version.entity.CraftPlayer"
        }


        try {
            return ClassHelper.getClass(clazz)
        } catch (var4: ClassNotFoundException) {
            val exception = var4
            throw RuntimeException("[LightInjector] Cannot find CB Class! ($clazz)", exception)
        }

    }


}