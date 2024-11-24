package com.dakuo.networkmonitor

import io.netty.channel.ChannelDuplexHandler
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.ChannelPromise
import org.bukkit.entity.Player
import java.util.*
import java.util.concurrent.atomic.AtomicLong


class TrafficMonitorHandler(private val player: Player) : ChannelDuplexHandler() {

    private val totalIncomingBytes = AtomicLong(0)
    private val totalOutgoingBytes = AtomicLong(0)

    private val incomingBytesPerSecond = LinkedList<Long>()
    private val outgoingBytesPerSecond = LinkedList<Long>()

    private var currentSecondIncomingBytes: Long = 0
    private var currentSecondOutgoingBytes: Long = 0

    override fun channelRead(ctx: ChannelHandlerContext, msg: Any) {
        val size = estimatePacketSize(msg)
        totalIncomingBytes.addAndGet(size)
        currentSecondIncomingBytes += size
        super.channelRead(ctx, msg)
    }

    override fun write(ctx: ChannelHandlerContext, msg: Any, promise: ChannelPromise) {
        val size = estimatePacketSize(msg)
        totalOutgoingBytes.addAndGet(size)
        currentSecondOutgoingBytes += size
        super.write(ctx, msg, promise)
    }

    private fun estimatePacketSize(packet: Any): Long {
        return packet.toString().length.toLong()
    }

    fun updateTrafficStats() {
        synchronized(this) {
            incomingBytesPerSecond.add(currentSecondIncomingBytes)
            outgoingBytesPerSecond.add(currentSecondOutgoingBytes)

            if (incomingBytesPerSecond.size > 60) incomingBytesPerSecond.poll()
            if (outgoingBytesPerSecond.size > 60) outgoingBytesPerSecond.poll()

            currentSecondIncomingBytes = 0
            currentSecondOutgoingBytes = 0
        }
    }

    fun getTotalIncomingBytes(): Long = totalIncomingBytes.get()
    fun getTotalOutgoingBytes(): Long = totalOutgoingBytes.get()

    fun getLastSecondIncomingBytes(): Long = incomingBytesPerSecond.lastOrNull() ?: 0
    fun getLastSecondOutgoingBytes(): Long = outgoingBytesPerSecond.lastOrNull() ?: 0

    fun getLastMinuteIncomingBytes(): Long = incomingBytesPerSecond.sum()
    fun getLastMinuteOutgoingBytes(): Long = outgoingBytesPerSecond.sum()
}
