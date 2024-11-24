package com.dakuo.networkmonitor

import org.bukkit.entity.Player
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent
import taboolib.common.platform.Plugin
import taboolib.common.platform.event.SubscribeEvent
import taboolib.common.platform.function.info
import taboolib.common.platform.function.submitAsync
import java.util.concurrent.ConcurrentHashMap

object NetworkMonitor : Plugin() {

    private val monitorHandlers = ConcurrentHashMap<Player, TrafficMonitorHandler>()


    override fun onEnable() {

        submitAsync(period = 20) {
            updateTrafficStats()
        }


        submitAsync(period = 60) {
            displayTrafficStats()
        }

        info("Successfully running NetworkMonitor!")
    }

    private fun displayTrafficStats() {
        val header = """
        |=============================================
        | 玩家流量统计 (最近 60 秒)
        |=============================================
        | 玩家名         | 下载 (KB)   | 上传 (KB)
        |---------------------------------------------
    """.trimMargin()
        println(header)

        monitorHandlers.forEach { (player, handler) ->
            val incomingKb = handler.getLastMinuteIncomingBytes() / 1024.0
            val outgoingKb = handler.getLastMinuteOutgoingBytes() / 1024.0

            val row = String.format("| %-15s | %12.2f | %12.2f |", player.name, incomingKb, outgoingKb)
            println(row)
        }

        println("|=============================================")
    }


    @SubscribeEvent
    fun onJoin(event: PlayerJoinEvent) {
        TrafficMonitorInjector.inject(event.player)
    }

    @SubscribeEvent
    fun onQuit(event: PlayerQuitEvent) {
        TrafficMonitorInjector.remove(event.player)
    }

    fun registerHandler(player: Player, handler: TrafficMonitorHandler) {
        monitorHandlers[player] = handler
    }

    fun unregisterHandler(player: Player) {
        monitorHandlers.remove(player)
    }

    private fun updateTrafficStats() {
        for (handler in monitorHandlers.values) {
            handler.updateTrafficStats()
        }
    }

    fun getMonitorHandlers(): Map<Player, TrafficMonitorHandler> = monitorHandlers
}