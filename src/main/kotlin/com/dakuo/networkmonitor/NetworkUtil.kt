package com.dakuo.networkmonitor

import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer
import org.bukkit.entity.Player
import taboolib.common.util.unsafeLazy
import taboolib.library.reflex.Reflex.Companion.getProperty
import taboolib.module.nms.MinecraftVersion
import taboolib.module.nms.nmsProxy

object NetworkUtil {

    fun getNetworkManager(player: Player): Any {
//        val cbPlayer = (player as CraftPlayer).handle
////        val nmsPlayer = cbPlayer.invokeMethod<Any>("getHandle")!!
//        val connection = cbPlayer.getProperty<Any>("playerConnection")!!
        return NMSConnection.INSTANCE.getNetworkManager(player)
    }

//    fun getCBPlayer(): Class<*> {
//        try {
//            return nmsClass("CraftPlayer")
//        } catch (var4: ClassNotFoundException) {
//            val exception = var4
//            throw RuntimeException("[LightInjector] Cannot find CB Class! (CraftPlayer)", exception)
//        }
//
//    }

    abstract class NMSConnection {

        abstract fun getNetworkManager(player: Player): Any

        companion object {
            val INSTANCE by unsafeLazy {
                nmsProxy<NMSConnection>()
            }
        }
    }

    class NMSConnectionImpl : NMSConnection() {
        override fun getNetworkManager(player: Player): Any {
            val nmsPlayer = (player as CraftPlayer).handle
            val connection = nmsPlayer.getProperty<Any>(if (MinecraftVersion.isUniversal) "connection" else "playerConnection")!!
            return if (MinecraftVersion.isUniversal) {
                connection.getProperty<Any>("connection", findToParent = true)!!
            } else {
                connection.getProperty<Any>("networkManager", findToParent = true)!!
            }
        }

    }

}