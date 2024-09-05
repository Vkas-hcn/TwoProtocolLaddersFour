package com.two.protocol.ladders.fourth.aaaaa

import android.app.ActivityManager
import android.app.Application
import android.content.Context
import android.os.Process
import android.util.Log
import com.github.shadowsocks.Core
import com.two.protocol.ladders.fourth.uuutt.DataUser
import com.two.protocol.ladders.fourth.uuutt.NetOnInfo
import com.two.protocol.ladders.fourth.uuuuii.zzzzzz.ZZ
import de.blinkt.openvpn.OpenContentProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ZZZ : Application() {
    companion object {
        lateinit var appContext: Context
        var saoState = false
        var saoClick = -1
        var jumpToHomeType = "0"
        var nowVpnUI = 0
    }

    override fun onCreate() {
        super.onCreate()
        appContext = this
        Core.init(this, ZZ::class)
        OpenContentProvider.app = this
        if (isMainProcess(this)) {
            CoroutineScope(Dispatchers.IO).launch {
                NetOnInfo.getIPInfo()
            }
        } else {
            Log.e("TAG", "onCreate: ZZZ")
            DataUser.isCloneGuideKey = "1"
        }
    }

    private fun isMainProcess(context: Context): Boolean {
        val activityManager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val myPid = Process.myPid()
        val packageName = context.packageName

        val runningAppProcesses = activityManager.runningAppProcesses ?: return false

        for (processInfo in runningAppProcesses) {
            if (processInfo.pid == myPid && processInfo.processName == packageName) {
                return true
            }
        }
        return false
    }
}