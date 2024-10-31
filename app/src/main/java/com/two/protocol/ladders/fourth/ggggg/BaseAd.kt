package com.two.protocol.ladders.fourth.ggggg

import android.content.Context
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdLoader
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.VideoOptions
import com.google.android.gms.ads.appopen.AppOpenAd
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.google.android.gms.ads.nativead.NativeAd
import com.google.android.gms.ads.nativead.NativeAdOptions
import com.google.android.gms.ads.nativead.NativeAdView
import com.two.protocol.ladders.fourth.R
import com.two.protocol.ladders.fourth.uuutt.DataUser
import com.two.protocol.ladders.fourth.uuutt.DataUser.TAG
import com.two.protocol.ladders.fourth.uuutt.DataUser.endTypeIp
import com.two.protocol.ladders.fourth.uuutt.DataUser.homeTypeIp
import com.two.protocol.ladders.fourth.uuutt.VPNGet
import com.two.protocol.ladders.fourth.uuuuii.eeedd.EE
import com.two.protocol.ladders.fourth.uuuuii.zzzzzz.ZZ
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.Date


class BaseAd private constructor() {
    companion object {
        private val instanceHelper = InstanceHelper

        fun getOpenInstance() = InstanceHelper.openLoadFlash
        fun getHomeInstance() = InstanceHelper.homeLoadFlash
        fun getEndInstance() = InstanceHelper.resultLoadFlash
        fun getConnectInstance() = InstanceHelper.connectLoadFlash
        fun getBackEndInstance() = InstanceHelper.backEndLoadFlash
        fun getBackListInstance() = InstanceHelper.backListLoadFlash


        private var idCounter = 0
    }

    object InstanceHelper {
        val openLoadFlash = BaseAd()
        val homeLoadFlash = BaseAd()
        val resultLoadFlash = BaseAd()
        val connectLoadFlash = BaseAd()
        val backEndLoadFlash = BaseAd()
        val backListLoadFlash = BaseAd()
    }

    private val id = generateId()
    var isFirstLoad: Boolean = false

    private fun generateId(): Int {
        idCounter++
        return idCounter
    }

    private val instanceName: String = getInstanceName()

    private fun getInstanceName(): String {
        return when (id) {
            1 -> "open"
            2 -> "home"
            3 -> "end"
            4 -> "connect"
            5 -> "backEnd"
            6 -> "backList"
            else -> ""
        }
    }

    fun getID(adBean: FlashAdBean): String {
        return when (id) {
            1 -> "open+${adBean.open}"
            2 -> "home+${adBean.mnnt}"
            3 -> "end+${adBean.rsnt}"
            4 -> "connect+${adBean.ctint}"
            5 -> "backEnd+${adBean.bcintres}"
            6 -> "backList+${adBean.bcintserv}"
            else -> ""
        }
    }

    var appAdDataFlash: Any? = null
    var adView: AdView? = null

    var isLoadingFlash = false


    var whetherToShowFlash = false

    var loadTimeFlash: Long = Date().time

    private fun whetherAdExceedsOneHour(loadTime: Long): Boolean =
        Date().time - loadTime < 60 * 60 * 1000

    fun advertisementLoadingFlash(context: Context) {
        if (isLoadingFlash) {
            Log.e(TAG, "${getInstanceName()}-The ad is loading and cannot be loaded again")
            return
        }
        if (!VPNGet.isVPNConnected()) {
            Log.e(TAG, "${getInstanceName()}-The VPN is not connected, the ad cannot be loaded")
            return
        }
        val blacklistState = DataUser.blockAdBlacklist()
        if (blacklistState && (instanceName == "connect" || instanceName == "backEnd" || instanceName == "backList" || instanceName == "home")) {
            Log.e(TAG, "黑名单屏蔽：${instanceName}广告，不加载")
            return
        }

        if (appAdDataFlash == null) {
            isLoadingFlash = true
            loadStartupPageAdvertisementFlash(context, DataUser.getAdJson())
        }
        if ((getLoadIp().isNotEmpty()) && getLoadIp() != DataUser.connectIp) {
            Timber.e(getInstanceName() + "-ip不一致-重新加载-load_ip=" + getLoadIp() + "-now-ip=" + DataUser.connectIp)
            whetherToShowFlash = false
            appAdDataFlash = null
            clearLoadIp()
            advertisementLoadingFlash(context)
            return
        }
        if (appAdDataFlash != null && !whetherAdExceedsOneHour(loadTimeFlash)) {
            isLoadingFlash = true
            loadStartupPageAdvertisementFlash(context, DataUser.getAdJson())
        }
    }


    private fun loadStartupPageAdvertisementFlash(context: Context, adData: FlashAdBean) {
        Log.e(TAG, "${getInstanceName()}-Ads - start loading-id=${getID(adData)}")
        adLoaders[id]?.invoke(context, adData)
    }

    private val adLoaders = createAdLoadersMap()

    private fun createAdLoadersMap(): Map<Int, (Context, FlashAdBean) -> Unit> {
        val adLoadersMap = mutableMapOf<Int, (Context, FlashAdBean) -> Unit>()

        adLoadersMap[1] = { context, adData ->
            loadOpenAdFlash(context, adData)
        }

        adLoadersMap[2] = { context, adData ->
            loadNativeAdvertisement(context, adData, getHomeInstance())
        }

        adLoadersMap[3] = { context, adData ->
            loadNativeAdvertisement(context, adData, getEndInstance())
        }

        adLoadersMap[4] = { context, adData ->
            loadIntAdvertisementFlash(context, adData, getConnectInstance())
        }

        adLoadersMap[5] = { context, adData ->
            loadIntAdvertisementFlash(context, adData, getBackEndInstance())

        }

        adLoadersMap[6] = { context, adData ->
            loadIntAdvertisementFlash(context, adData, getBackListInstance())
        }

        return adLoadersMap
    }


    private fun loadOpenAdFlash(context: Context, adData: FlashAdBean) {
        DataUser.openTypeIp = DataUser.connectIp
        val request = AdRequest.Builder().build()
        AppOpenAd.load(
            context,
            adData.open,
            request,
            AppOpenAd.APP_OPEN_AD_ORIENTATION_PORTRAIT,
            object : AppOpenAd.AppOpenAdLoadCallback() {
                override fun onAdLoaded(ad: AppOpenAd) {
                    Log.e(TAG, "open ads start loading success")
                    getOpenInstance().isLoadingFlash = false
                    getOpenInstance().appAdDataFlash = ad
                    getOpenInstance().loadTimeFlash = Date().time
                }

                override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                    getOpenInstance().isLoadingFlash = false
                    getOpenInstance().appAdDataFlash = null
                    if (!isFirstLoad) {
                        getOpenInstance().advertisementLoadingFlash(context)
                        isFirstLoad = true
                    }
                    val error =
                        """
           domain: ${loadAdError.domain}, code: ${loadAdError.code}, message: ${loadAdError.message}
          """"
                    Log.e(TAG, "open ads start loading Failed=${error}")
                }
            }
        )
    }


    private fun advertisingOpenCallbackFlash(fullScreenFun: () -> Unit) {
        if (getOpenInstance().appAdDataFlash !is AppOpenAd) {
            return
        }
        (getOpenInstance().appAdDataFlash as AppOpenAd).fullScreenContentCallback =
            object : FullScreenContentCallback() {
                override fun onAdDismissedFullScreenContent() {
                    getOpenInstance().whetherToShowFlash = false
                    getOpenInstance().appAdDataFlash = null
                    fullScreenFun()
                }

                override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                    getOpenInstance().whetherToShowFlash = false
                    getOpenInstance().appAdDataFlash = null
                }

                override fun onAdShowedFullScreenContent() {
                    getOpenInstance().appAdDataFlash = null
                    getOpenInstance().whetherToShowFlash = true
                }

                override fun onAdClicked() {
                    super.onAdClicked()
                }
            }
    }


    fun displayOpenAdvertisementFlash(
        activity: AppCompatActivity,
        fullScreenFun: () -> Unit
    ): Boolean {
        if (getOpenInstance().appAdDataFlash == null) {
            return false
        }
        if (getOpenInstance().whetherToShowFlash || activity.lifecycle.currentState != Lifecycle.State.RESUMED) {
            return false
        }
        if ((DataUser.openTypeIp.isNotEmpty()) && DataUser.openTypeIp != DataUser.connectIp) {
            Log.e(
                TAG,
                "open-ip不一致-不能展示-load_ip=" + DataUser.openTypeIp + "-now-ip=" + DataUser.connectIp
            )
            return false
        }
        Log.e(
            TAG,
            "open-ip一致-展示-load_ip=" + DataUser.openTypeIp + "-now-ip=" + DataUser.connectIp
        )
        advertisingOpenCallbackFlash(fullScreenFun)
        (getOpenInstance().appAdDataFlash as AppOpenAd).show(activity)
        clearLoadIp()
        return true
    }


    private fun loadIntAdvertisementFlash(context: Context, adData: FlashAdBean, adBase: BaseAd) {
        val adRequest = AdRequest.Builder().build()
        var loadId = ""
        when (adBase) {
            getConnectInstance() -> {
                DataUser.contTypeIp = DataUser.connectIp
                loadId = adData.ctint
            }

            getBackEndInstance() -> {
                DataUser.backEndTypeIp = DataUser.connectIp
                loadId = adData.bcintres
            }

            getBackListInstance() -> {
                DataUser.backListTypeIp = DataUser.connectIp
                loadId = adData.bcintserv
            }
        }
        InterstitialAd.load(
            context,
            loadId,
            adRequest,
            object : InterstitialAdLoadCallback() {
                override fun onAdFailedToLoad(adError: LoadAdError) {
                    adBase.isLoadingFlash = false
                    adBase.appAdDataFlash = null
                    val error =
                        """
           domain: ${adError.domain}, code: ${adError.code}, message: ${adError.message}
          """
                    Log.e(TAG, "${adBase.getInstanceName()}-The ad failed to load:$error ")

                }

                override fun onAdLoaded(interstitialAd: InterstitialAd) {
                    Log.e(TAG, "${adBase.getInstanceName()}-The ad loads successfully: ")
                    adBase.loadTimeFlash = Date().time
                    adBase.isLoadingFlash = false
                    adBase.appAdDataFlash = interstitialAd
                    interstitialAd.setOnPaidEventListener { adValue ->

                    }
                }
            })
    }


    private fun intScreenAdCallback(adBase: BaseAd, closeWindowFun: () -> Unit) {
        (adBase.appAdDataFlash as? InterstitialAd)?.fullScreenContentCallback =
            object : FullScreenContentCallback() {
                override fun onAdClicked() {
                }

                override fun onAdDismissedFullScreenContent() {
                    adBase.appAdDataFlash = null
                    adBase.whetherToShowFlash = false
                    closeWindowFun()
                }

                override fun onAdFailedToShowFullScreenContent(p0: AdError) {
                    adBase.appAdDataFlash = null
                    adBase.whetherToShowFlash = false
                }

                override fun onAdImpression() {
                }

                override fun onAdShowedFullScreenContent() {
                    adBase.appAdDataFlash = null
                    adBase.whetherToShowFlash = true
                }
            }
    }

    fun canShowAd(
        activity: AppCompatActivity,
        adBase: BaseAd
    ): Int {
        if (!VPNGet.isVPNConnected()) {
            return 0
        }
        val blacklistState = DataUser.blockAdBlacklist()
        if (blacklistState && (adBase == getConnectInstance() || adBase == getBackEndInstance() || adBase == getBackListInstance() || adBase == getHomeInstance())) {
            Log.e(TAG, "黑名单屏蔽：${adBase.getInstanceName()}广告，不显示")
            return 0
        }

        if (adBase.appAdDataFlash == null) {
            return 1
        }

        if (adBase.whetherToShowFlash || activity.lifecycle.currentState != Lifecycle.State.RESUMED) {
            return 1
        }
        val vpnIp = DataUser.connectIp
        if ((getLoadIp().isNotEmpty()) && getLoadIp() != vpnIp) {
            Log.e(
                TAG,
                "${adBase.getInstanceName()}-ip不一致-不能展示-load_ip=" + getLoadIp() + "-now-ip=" + vpnIp
            )
            return 0
        }
        return 2
    }

    fun playIntAdvertisementFlash(
        activity: AppCompatActivity,
        adBase: BaseAd,
        closeWindowFun: () -> Unit
    ) {
        Log.e(
            TAG,
            "${adBase.getInstanceName()}-ip一致-展示-load_ip=" + getLoadIp() + "-now-ip=" + DataUser.connectIp
        )
        intScreenAdCallback(adBase, closeWindowFun)
        activity.lifecycleScope.launch(Dispatchers.Main) {
            if (activity.lifecycle.currentState.isAtLeast(Lifecycle.State.STARTED)) {
                (adBase.appAdDataFlash as InterstitialAd).show(activity)
                clearLoadIp()
            }
        }
    }

    fun playNativeAdvertisementFlash(
        activity: AppCompatActivity,
        adBase: BaseAd,
    ) {
        Log.e(
            TAG,
            "${adBase.getInstanceName()}-ip一致-展示-load_ip=" + getLoadIp() + "-now-ip=" + DataUser.connectIp
        )
        if (adBase == getHomeInstance()) {
            setDisplayHomeNativeAdFlash(activity as ZZ, adBase)
        } else {
            setDisplayEndNativeAdFlash(activity as EE, adBase)
        }
    }

    private fun loadNativeAdvertisement(context: Context, adData: FlashAdBean, adBase: BaseAd) {
        var loadId = ""
        when (adBase) {
            getHomeInstance() -> {
                DataUser.homeTypeIp = DataUser.connectIp
                loadId = adData.mnnt
            }

            getEndInstance() -> {
                DataUser.endTypeIp = DataUser.connectIp
                loadId = adData.rsnt
            }
        }
        Log.e(TAG, "loadNativeAdvertisement: $loadId")
        val vpnNativeAds = AdLoader.Builder(
            context.applicationContext,
            loadId
        )
        val videoOptions = VideoOptions.Builder()
            .setStartMuted(true)
            .build()

        val adOptions = NativeAdOptions.Builder()
            .setVideoOptions(videoOptions)
            .setAdChoicesPlacement(NativeAdOptions.ADCHOICES_TOP_LEFT)
            .setMediaAspectRatio(NativeAdOptions.NATIVE_MEDIA_ASPECT_RATIO_PORTRAIT)
            .build()

        vpnNativeAds.withNativeAdOptions(adOptions)
        vpnNativeAds.forNativeAd {
            adBase.appAdDataFlash = it
            Log.e(TAG, "${adBase.getInstanceName()}- ad loads successfully")
            it.setOnPaidEventListener {
                if (adBase == getHomeInstance()) {
                    getHomeInstance().advertisementLoadingFlash(context)
                }
            }
        }
        vpnNativeAds.withAdListener(object : AdListener() {
            override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                super.onAdFailedToLoad(loadAdError)
                val error =
                    """
           domain: ${loadAdError.domain}, code: ${loadAdError.code}, message: ${loadAdError.message}
          """"
                Log.e(TAG, "${adBase.getInstanceName()}- ad failed to load:$error ")
                adBase.isLoadingFlash = false
                adBase.appAdDataFlash = null
            }

            override fun onAdLoaded() {
                super.onAdLoaded()
                adBase.loadTimeFlash = Date().time
                adBase.isLoadingFlash = false
            }

            override fun onAdOpened() {
                super.onAdOpened()
            }

            override fun onAdClicked() {
                super.onAdClicked()
            }
        }).build().loadAd(AdRequest.Builder().build())
    }


    private fun setDisplayHomeNativeAdFlash(activity: ZZ, adBase: BaseAd) {
        activity.lifecycleScope.launch(Dispatchers.Main) {
            (adBase.appAdDataFlash as NativeAd).let { adData ->
                val state = activity.lifecycle.currentState == Lifecycle.State.RESUMED

                if (state) {
                    activity.binding.imgOcAd.isVisible = true
                    if (activity.isDestroyed || activity.isFinishing || activity.isChangingConfigurations) {
                        adData.destroy()
                        return@let
                    }
                    val adView = activity.layoutInflater.inflate(
                        R.layout.admod_home,
                        null
                    ) as NativeAdView
                    populateNativeAdView(adData, adView)
                    activity.binding.adLayoutAdmob.apply {
                        removeAllViews()
                        addView(adView)
                    }
                    activity.binding.imgOcAd.isVisible = false
                    activity.binding.adLayoutAdmob.isVisible = true
                    adBase.appAdDataFlash = null
                    adBase.isLoadingFlash = false
                    homeTypeIp = ""
                }
            }
        }
    }

    private fun setDisplayEndNativeAdFlash(activity: EE, adBase: BaseAd) {
        activity.lifecycleScope.launch(Dispatchers.Main) {
            (adBase.appAdDataFlash as NativeAd).let { adData ->
                val state = activity.lifecycle.currentState == Lifecycle.State.RESUMED
                if (state) {
                    activity.binding.imgOcAd.isVisible = true
                    if (activity.isDestroyed || activity.isFinishing || activity.isChangingConfigurations) {
                        adData.destroy()
                        return@let
                    }
                    val adView = activity.layoutInflater.inflate(
                        R.layout.admod_end,
                        null
                    ) as NativeAdView
                    populateNativeAdView(adData, adView)
                    activity.binding.adLayoutAdmob.apply {
                        removeAllViews()
                        addView(adView)
                    }
                    activity.binding.imgOcAd.isVisible = false
                    activity.binding.adLayoutAdmob.isVisible = true
                    adBase.appAdDataFlash = null
                    adBase.isLoadingFlash = false
                    endTypeIp = ""
                }
            }
        }
    }

    private fun populateNativeAdView(nativeAd: NativeAd, adView: NativeAdView) {
        adView.headlineView = adView.findViewById(R.id.ad_headline)
        adView.bodyView = adView.findViewById(R.id.ad_body)
        adView.callToActionView = adView.findViewById(R.id.ad_call_to_action)
        adView.iconView = adView.findViewById(R.id.ad_app_icon)
        adView.mediaView = adView.findViewById(R.id.ad_media)

        nativeAd.mediaContent?.let {
            adView.mediaView?.apply { setImageScaleType(ImageView.ScaleType.CENTER_CROP) }?.mediaContent =
                it
        }
        adView.mediaView?.clipToOutline = true
        adView.mediaView?.outlineProvider = DataUser.NavigationViewOutlineProvider()
        if (nativeAd.body == null) {
            adView.bodyView?.visibility = View.INVISIBLE
        } else {
            adView.bodyView?.visibility = View.VISIBLE
            (adView.bodyView as TextView).text = nativeAd.body
        }
        if (nativeAd.callToAction == null) {
            adView.callToActionView?.visibility = View.INVISIBLE
        } else {
            adView.callToActionView?.visibility = View.VISIBLE
            (adView.callToActionView as TextView).text = nativeAd.callToAction
        }
        if (nativeAd.headline == null) {
            adView.headlineView?.visibility = View.INVISIBLE
        } else {
            adView.headlineView?.visibility = View.VISIBLE
            (adView.headlineView as TextView).text = nativeAd.headline
        }

        if (nativeAd.icon == null) {
            adView.iconView?.visibility = View.GONE
        } else {
            (adView.iconView as ImageView).setImageDrawable(
                nativeAd.icon?.drawable
            )
            adView.iconView?.visibility = View.VISIBLE
        }
        adView.setNativeAd(nativeAd)
    }

    private fun getLoadIp(): String {
        return when (getInstanceName()) {
            "open" -> DataUser.openTypeIp
            "home" -> DataUser.homeTypeIp
            "end" -> DataUser.endTypeIp
            "connect" -> DataUser.contTypeIp
            "backEnd" -> DataUser.backEndTypeIp
            "backList" -> DataUser.backListTypeIp
            else -> {
                ""
            }
        }
    }

    private fun clearLoadIp() {
        when (getInstanceName()) {
            "open" -> DataUser.openTypeIp = ""
            "home" -> DataUser.homeTypeIp = ""
            "end" -> DataUser.endTypeIp = ""
            "connect" -> DataUser.contTypeIp = ""
            "backEnd" -> DataUser.backEndTypeIp = ""
            "backList" -> DataUser.backListTypeIp = ""
            else -> {
                ""
            }
        }
    }
}

