package com.dakuo.networkmonitor

import io.netty.channel.Channel
import io.netty.channel.ChannelPipeline
import org.bukkit.entity.Player
import taboolib.library.reflex.Reflex.Companion.getProperty

object TrafficMonitorInjector {


    fun inject(player: Player) {
        try {
            val networkManager = NetworkUtil.getNetworkManager(player)
            val channel: Channel = networkManager.getProperty<Channel>("channel")!!
            val handler = TrafficMonitorHandler(player)

            val pipeline: ChannelPipeline = channel.pipeline()
            if (pipeline.get("traffic_monitor") == null) {
                pipeline.addBefore("packet_handler", "traffic_monitor", handler)
                NetworkMonitor.registerHandler(player, handler)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun remove(player: Player) {
        try {
            val networkManager = NetworkUtil.getNetworkManager(player)
            val channel: Channel = networkManager.getProperty<Channel>("channel")!!

            val pipeline: ChannelPipeline = channel.pipeline()
            if (pipeline.get("traffic_monitor") != null) {
                pipeline.remove("traffic_monitor")
                NetworkMonitor.unregisterHandler(player)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

}