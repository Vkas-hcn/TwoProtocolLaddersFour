package com.two.protocol.ladders.fourth.aaaaa

import android.app.Activity
import android.app.ActivityManager
import android.app.Application
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Process
import android.provider.Settings
import android.util.Log
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import androidx.lifecycle.ProcessLifecycleOwner
import com.adjust.sdk.Adjust
import com.adjust.sdk.AdjustConfig
import com.android.installreferrer.api.InstallReferrerClient
import com.android.installreferrer.api.InstallReferrerStateListener
import com.github.shadowsocks.Core
import com.google.android.gms.ads.AdActivity
import com.google.firebase.FirebaseApp
import com.google.firebase.ktx.Firebase
import com.google.firebase.ktx.initialize
import com.two.protocol.ladders.fourth.BuildConfig
import com.two.protocol.ladders.fourth.ggggg.BaseAd
import com.two.protocol.ladders.fourth.uuutt.DataUpMix
import com.two.protocol.ladders.fourth.uuutt.DataUpMix.log
import com.two.protocol.ladders.fourth.uuutt.DataUser
import com.two.protocol.ladders.fourth.uuutt.GetAdminNetData
import com.two.protocol.ladders.fourth.uuutt.NetOnInfo
import com.two.protocol.ladders.fourth.uuuuii.gggguu.GG
import com.two.protocol.ladders.fourth.uuuuii.zzzzzz.ZZ
import de.blinkt.openvpn.OpenContentProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.UUID

class ZZZ : Application(), Application.ActivityLifecycleCallbacks, LifecycleObserver {
    companion object {
        lateinit var appContext: Context
        var saoState = false
        var saoClick = -1
        var jumpToHomeType = "0"
        var nowVpnUI = 0
        var isInBackground = false
        var nowAName: String? = null
        var startAppTime:Long = 0
        var clickAdZZ = false
    }

    var adActivity: Activity? = null
    var lastBackgroundTime: Long = 0
    override fun onCreate() {
        super.onCreate()
        appContext = this
        Core.init(this, ZZ::class)
        OpenContentProvider.app = this
        Firebase.initialize(this)
        FirebaseApp.initializeApp(this)
        registerActivityLifecycleCallbacks(this)
        ProcessLifecycleOwner.get().lifecycle.addObserver(this)
        if (isMainProcess(this)) {
            BaseAd.getOpenInstance().isAppOpenSameDayBa()
            startAppTime = System.currentTimeMillis()
            CoroutineScope(Dispatchers.IO).launch {
                NetOnInfo.getIPInfo()
                NetOnInfo.getBlackList(this@ZZZ)
                withContext(Dispatchers.Main){
                    initAdJust()
                }
            }
            if (DataUser.postUUID.isNullOrBlank()) {
                DataUser.postUUID = UUID.randomUUID().toString()
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

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
    }

    override fun onActivityStarted(activity: Activity) {
        if (activity is AdActivity) {
            adActivity = activity
        }else{
            nowAName = activity.javaClass.simpleName
        }
    }

    override fun onActivityResumed(activity: Activity) {
        Adjust.onResume()
        if (isInBackground) {
            isInBackground = false
            val currentTime = System.currentTimeMillis()
            val backgroundDuration = currentTime - lastBackgroundTime
            if (backgroundDuration > 3000) {
                restartApp(activity)
            }
        }
    }

    override fun onActivityPaused(activity: Activity) {
        Adjust.onPause()
    }

    override fun onActivityStopped(activity: Activity) {

    }

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
    }

    override fun onActivityDestroyed(activity: Activity) {
    }


    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun onEnterForeground() {
    }


    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun onEnterBackground() {
        lastBackgroundTime = System.currentTimeMillis()
        isInBackground = true
    }


    private fun restartApp(activity: Activity) {
        if (adActivity != null) {
            adActivity?.finish()
        }
        val intent = Intent(activity, GG::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(intent)
        activity.finish()
    }
    private fun initAdJust() {
        Adjust.addSessionCallbackParameter(
            "customer_user_id",
            DataUser.postUUID
        )
        val appToken = if (BuildConfig.DEBUG) {
            "ih2pm2dr3k74"
        } else {
            "cdd93w9cc3y8"
        }
        val environment = if (BuildConfig.DEBUG) {
            AdjustConfig.ENVIRONMENT_SANDBOX
        } else {
            AdjustConfig.ENVIRONMENT_PRODUCTION
        }

        val config = AdjustConfig(this, appToken, environment)
        config.needsCost = true
        config.setOnAttributionChangedListener { attribution ->
            log("adjust=${attribution}")
            if (DataUser.ajState != "1" && attribution.network.isNotEmpty() && attribution.network.contains(
                    "organic",
                    true
                ).not()
            ) {
                DataUser.ajState = "1"
            }
        }
        Adjust.onCreate(config)
    }
}