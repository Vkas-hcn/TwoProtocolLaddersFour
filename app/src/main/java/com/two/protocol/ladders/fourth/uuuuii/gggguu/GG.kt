package com.two.protocol.ladders.fourth.uuuuii.gggguu

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.app.Application
import android.content.Intent
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.view.animation.LinearInterpolator
import android.widget.Toast
import androidx.activity.addCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import com.android.installreferrer.api.InstallReferrerClient
import com.android.installreferrer.api.InstallReferrerStateListener
import com.facebook.FacebookSdk
import com.facebook.appevents.AppEventsLogger
import com.google.android.ump.ConsentDebugSettings
import com.google.android.ump.ConsentInformation
import com.google.android.ump.ConsentRequestParameters
import com.google.android.ump.UserMessagingPlatform
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.two.protocol.ladders.fourth.BuildConfig
import com.two.protocol.ladders.fourth.aaaaa.ZZZ
import com.two.protocol.ladders.fourth.databinding.ActivityFirstBinding
import com.two.protocol.ladders.fourth.ggggg.BaseAd
import com.two.protocol.ladders.fourth.uuutt.DataUpMix
import com.two.protocol.ladders.fourth.uuutt.DataUser
import com.two.protocol.ladders.fourth.uuutt.DataUser.getLogicJson
import com.two.protocol.ladders.fourth.uuutt.GetAdminNetData
import com.two.protocol.ladders.fourth.uuutt.VPNGet
import com.two.protocol.ladders.fourth.uuuuii.zzzzzz.ZZ
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeout
import kotlin.system.exitProcess

class GG : AppCompatActivity() {
    private val binding by lazy { ActivityFirstBinding.inflate(layoutInflater) }
    private var jobOpenAdsForest: Job? = null
    private var startCateForest: Job? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        updateUserOpinions()
        initTime()
        lifecycleScope.launch(Dispatchers.IO) {
            rrrrrData()
            DataUpMix.postSessionData()
        }
        onBackPressedDispatcher.addCallback(this) {
        }
    }

    private fun initTime() {
        getFileBaseData()
        startCountdown()
    }

    private fun startCountdown() {
        val animator = ValueAnimator.ofInt(0, 100)
        animator.duration = 14000
        animator.interpolator = LinearInterpolator()
        animator.addUpdateListener { animation ->
            val progress = animation.animatedValue as Int
            binding.progressBarFirst.progress = progress
        }
        animator.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
            }
        })
        animator.start()
    }

    private fun getFileBaseData() {
        initFaceBook()
        startCateForest = lifecycleScope.launch {
            var isCa = false
            val auth = Firebase.remoteConfig
            auth.fetchAndActivate().addOnSuccessListener {
                DataUser.ooo_ad = auth.getString(DataUser.oooAdKey)?.let {
                    String(Base64.decode(it, Base64.DEFAULT))
                }
                DataUser.ooo_lj = auth.getString(DataUser.oooLjKey)?.let {
                    String(Base64.decode(it, Base64.DEFAULT))
                }
                DataUser.ooo_tz = auth.getString(DataUser.oooTzKey)
                Log.e("TAG", "getFileBaseData: ${DataUser.ooo_lj}")
                isCa = true
                initFaceBook()
            }
            try {
                withTimeout(4000L) {
                    while (true) {
                        if (!isActive) {
                            break
                        }
                        if (isCa) {
                            loadAdFun()
                            cancel()
                            startCateForest = null
                        }
                        delay(500)
                    }
                }
            } catch (e: TimeoutCancellationException) {
                cancel()
                startCateForest = null
                loadAdFun()
            }
        }
    }

    private fun initFaceBook() {
        val bean = getLogicJson().dda ?: ""
        if (bean.isBlank()) {
            return
        }
        Log.e("TAG", "initFaceBook: ${bean}")
        FacebookSdk.setApplicationId(bean)
        FacebookSdk.sdkInitialize(ZZZ.appContext)
        AppEventsLogger.activateApp(ZZZ.appContext as Application)
    }

    private fun loadAdFun() {
        BaseAd.getOpenInstance().advertisementLoadingForest(this)
        connectToVPNFun()
        BaseAd.getHomeInstance().advertisementLoadingForest(this)
        BaseAd.getConnectInstance().advertisementLoadingForest(this)
    }

    private fun ccc() {
        if (VPNGet.isVPNConnected()) {
            loadOpenAd()
        } else {
            checkData()
        }
    }

    private fun connectToVPNFun() {
        if (DataUser.cmpState == "1") {
            ccc()
            return
        }
        GlobalScope.launch {
            while (isActive) {
                if (DataUser.cmpState == "1") {
                    ccc()
                    cancel()
                }
                delay(500)
            }
        }
    }

    private fun checkData() {
        jobOpenAdsForest?.cancel()
        jobOpenAdsForest = lifecycleScope.launch {
            delay(1000L)
            try {
                withTimeout(6000L) {
                    var keepLooping = true
                    while (keepLooping && isActive) {
                        val adData = DataUser.ooo_ad
                        val blockData = DataUser.blockData
                        if (adData.isNullOrBlank() || blockData.isNullOrBlank()) {
                            delay(500L)
                        } else {
                            keepLooping = false
                            finishOpenAd()
                        }
                    }
                }
            } catch (e: TimeoutCancellationException) {
                finishOpenAd()
            }
        }
        jobOpenAdsForest?.invokeOnCompletion {
            jobOpenAdsForest = null
        }
    }

    private fun loadOpenAd() {
        jobOpenAdsForest?.cancel()
        jobOpenAdsForest = null
        jobOpenAdsForest = lifecycleScope.launch {
            if (!VPNGet.isVPNConnected() || BaseAd.getOpenInstance().limitIsExceeded()) {
                finishOpenAd()
                return@launch
            }
            try {
                withTimeout(12000L) {
                    while (isActive) {
                        val showState = BaseAd.getOpenInstance()
                            .displayOpenAdvertisementForest(this@GG, fullScreenFun = {
                                startToMain()
                            })
                        if (showState) {
                            cancel()
                            jobOpenAdsForest = null
                            binding.progressBarFirst.progress = 100
                        }
                        delay(500L)
                    }
                }
            } catch (e: TimeoutCancellationException) {
                finishOpenAd()
            }
        }
    }

    private fun finishOpenAd() {
        jobOpenAdsForest?.cancel()
        jobOpenAdsForest = null
        binding.progressBarFirst.progress = 100
        startToMain()
    }

    private fun startToMain() {
        lifecycleScope.launch {
            delay(300)
            if (lifecycle.currentState == Lifecycle.State.RESUMED) {
                val intent = Intent(this@GG, ZZ::class.java)
                startActivity(intent)
                finish()
            }
        }

    }

    private fun updateUserOpinions() {
        if (DataUser.cmpState == "1") {
            return
        }
        val debugSettings =
            ConsentDebugSettings.Builder(this)
                .setDebugGeography(ConsentDebugSettings.DebugGeography.DEBUG_GEOGRAPHY_EEA)
                .addTestDeviceHashedId("BCD99A19DFC84C1B71AF2A884D73059C")
                .build()
        val params = ConsentRequestParameters
            .Builder()
            .setConsentDebugSettings(debugSettings)
            .build()
        val consentInformation: ConsentInformation =
            UserMessagingPlatform.getConsentInformation(this)
        consentInformation.requestConsentInfoUpdate(
            this,
            params, {
                UserMessagingPlatform.loadAndShowConsentFormIfRequired(this) {
                    if (consentInformation.canRequestAds()) {
                        DataUser.cmpState = "1"
                    }
                }
            },
            {
                DataUser.cmpState = "1"
            }
        )
    }
    private fun rrrrrData() {
        runCatching {
            val referrerClient = InstallReferrerClient.newBuilder(this).build()
            referrerClient.startConnection(object : InstallReferrerStateListener {
                override fun onInstallReferrerSetupFinished(p0: Int) {
                    when (p0) {
                        InstallReferrerClient.InstallReferrerResponse.OK -> {
                            DataUser.refData = referrerClient.installReferrer.toString()
                            val timeElapsed =
                                ((System.currentTimeMillis() - ZZZ.startAppTime) / 1000).toInt()
                            DataUpMix.postPointData("u_rf","time",timeElapsed)
                            CoroutineScope(Dispatchers.IO).launch {
                                GetAdminNetData.getAdminData()
                            }
                            referrerClient.installReferrer?.run {
                                if (DataUser.installUpState != "1") {
                                    DataUpMix.log("onInstallReferrerSetupFinished: ")
                                    DataUpMix.postInstallJson(this)
                                }
                            }
                        }
                    }
                    referrerClient.endConnection()
                }

                override fun onInstallReferrerServiceDisconnected() {
                }
            })
        }.onFailure { e ->
        }
    }

}