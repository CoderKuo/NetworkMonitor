package com.dakuo.networkmonitor

import org.bukkit.command.CommandSender
import taboolib.common.platform.command.CommandBody
import taboolib.common.platform.command.CommandHeader
import taboolib.common.platform.command.mainCommand
import taboolib.common.platform.command.player

@CommandHeader("networkMonitor", [ "nwm" ], permission = "*")
object MainCommand {
    @CommandBody
    val main = mainCommand {
        player {
            exec<CommandSender> {
                NetworkMonitor.displayTrafficStats(ctx.player())
            }
        }
        exec<CommandSender> {
            NetworkMonitor.displayTrafficStats()
        }
    }
}