package com.two.protocol.ladders.fourth.uuutt

import android.os.SystemClock
import kotlinx.coroutines.*
import kotlin.system.measureTimeMillis

object GlobalTimer {

    private var startTime: Long = 0L
    private var isRunning: Boolean = false
    private var job: Job? = null
    var onTimeUpdate: ((String) -> Unit)? = null
    fun start() {
        if (!isRunning) {
            startTime = SystemClock.elapsedRealtime()
            isRunning = true
            job = CoroutineScope(Dispatchers.Main).launch {
                while (isRunning) {
                    val elapsedMillis = SystemClock.elapsedRealtime() - startTime
                    val formattedTime = formatTime(elapsedMillis)
                    onTimeUpdate?.invoke(formattedTime)
                    delay(1000)
                }
            }
        }
    }

    fun stop() {
        if (isRunning) {
            isRunning = false
            job?.cancel()
        }
    }

    fun reset() {
        stop()
        startTime = 0L
    }

    private fun formatTime(elapsedMillis: Long): String {
        val hours = (elapsedMillis / 3600000).toInt()
        val minutes = (elapsedMillis % 3600000 / 60000).toInt()
        val seconds = (elapsedMillis % 60000 / 1000).toInt()
        return String.format("%02d:%02d:%02d", hours, minutes, seconds)
    }
}
