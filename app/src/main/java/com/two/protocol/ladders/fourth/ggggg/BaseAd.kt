package com.two.protocol.ladders.fourth.ggggg

import android.content.Context
import android.provider.ContactsContract.Data
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
import com.google.android.gms.ads.AdValue
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
import com.two.protocol.ladders.fourth.aaaaa.ZZZ
import com.two.protocol.ladders.fourth.uuutt.DataUpMix
import com.two.protocol.ladders.fourth.uuutt.DataUpMix.log
import com.two.protocol.ladders.fourth.uuutt.DataUser
import com.two.protocol.ladders.fourth.uuutt.DataUser.TAG
import com.two.protocol.ladders.fourth.uuutt.VPNGet
import com.two.protocol.ladders.fourth.uuuuii.eeedd.EE
import com.two.protocol.ladders.fourth.uuuuii.zzzzzz.ZZ
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.Date


class BaseAd private constructor() {
    companion object {
        fun getOpenInstance() = BaseAdInstall.openLoadForest
        fun getHomeInstance() = BaseAdInstall.homeLoadForest
        fun getEndInstance() = BaseAdInstall.resultLoadForest
        fun getConnectInstance() = BaseAdInstall.connectLoadForest
        fun getBackEndInstance() = BaseAdInstall.backEndLoadForest
        fun getBackListInstance() = BaseAdInstall.backListLoadForest


        private var idCounter = 0
    }

    object BaseAdInstall {
        val openLoadForest = BaseAd()
        val homeLoadForest = BaseAd()
        val resultLoadForest = BaseAd()
        val connectLoadForest = BaseAd()
        val backEndLoadForest = BaseAd()
        val backListLoadForest = BaseAd()
    }

    private val id = getBaseId()
    var isFirstLoad: Boolean = false
    private var adDataOpen: ForestAdBean? = null
    private var adDataHome: ForestAdBean? = null
    private var adDataResult: ForestAdBean? = null
    private var adDataCont: ForestAdBean? = null
    private var adDataList: ForestAdBean? = null
    private var adDataEnd: ForestAdBean? = null
    private fun getBaseId(): Int {
        idCounter++
        return idCounter
    }

    var whetherToShowForest = false

    var loadTimeForest: Long = Date().time

    private val instanceName: String = getInstanceName()

    var appAdDataForest: Any? = null

    var isLoadingForest = false


    private fun adGuoQi(loadTime: Long): Boolean =
        Date().time - loadTime < 60 * 60 * 1000

    fun advertisementLoadingForest(context: Context) {
        if (isLoadingForest) {
            log( "${getInstanceName()}-The ad is loading and cannot be loaded again")
            return
        }
        if (!VPNGet.isVPNConnected()) {
            log( "${getInstanceName()}-The VPN is not connected, the ad cannot be loaded")
            return
        }
        if (limitIsExceeded()) {
            log("广告超限不在加载")
            return
        }
        val blacklistState = DataUser.blockAdBlacklist()
        if (blacklistState && (instanceName == "connect" || instanceName == "backEnd" || instanceName == "backList" || instanceName == "home")) {
            log( "黑名单屏蔽：${instanceName}广告，不加载")
            return
        }
        if (DataUser.bbb_admin == "2" && (instanceName == "connect" || instanceName == "backEnd" || instanceName == "home")) {
            Log.e("TAG", "admin屏蔽${instanceName}广告")
            return
        }
        if (appAdDataForest == null) {
            isLoadingForest = true
            loadStartupPageAdvertisementForest(context, DataUser.getAdJson())
        }
        if ((getLoadIp().isNotEmpty()) && getLoadIp() != DataUser.connectIp) {
            Timber.e(getInstanceName() + "-ip不一致-重新加载-load_ip=" + getLoadIp() + "-now-ip=" + DataUser.connectIp)
            whetherToShowForest = false
            appAdDataForest = null
            clearLoadIp()
            advertisementLoadingForest(context)
            return
        }
        if (appAdDataForest != null && !adGuoQi(loadTimeForest)) {
            isLoadingForest = true
            loadStartupPageAdvertisementForest(context, DataUser.getAdJson())
        }
    }


    private fun loadStartupPageAdvertisementForest(context: Context, adData: ForestAdBean) {
        DataUpMix.abcAsk(getInstanceName(), getLoadId(adData))
        adLoaders[id]?.invoke(context, adData)
    }

    private val adLoaders = createAdLoadersMap()

    private fun createAdLoadersMap(): Map<Int, (Context, ForestAdBean) -> Unit> {
        val adLoadersMap = mutableMapOf<Int, (Context, ForestAdBean) -> Unit>()

        adLoadersMap[1] = { context, adData ->
            loadOpenAdForest(context, adData)
        }

        adLoadersMap[2] = { context, adData ->
            loadNativeAdvertisement(context, adData, getHomeInstance())
        }

        adLoadersMap[3] = { context, adData ->
            loadNativeAdvertisement(context, adData, getEndInstance())
        }

        adLoadersMap[4] = { context, adData ->
            loadIntAdvertisementForest(context, adData, getConnectInstance())
        }

        adLoadersMap[5] = { context, adData ->
            loadIntAdvertisementForest(context, adData, getBackEndInstance())

        }

        adLoadersMap[6] = { context, adData ->
            loadIntAdvertisementForest(context, adData, getBackListInstance())
        }

        return adLoadersMap
    }


    private fun loadOpenAdForest(context: Context, adData: ForestAdBean, currentIndex: Int = 0) {
        val adIds = DataUser.getAdmobIdList(adData.open)
        if (currentIndex >= adIds.size) {
            if (!isFirstLoad) {
                isFirstLoad = true
                loadOpenAdForest(context, adData, 0)
                return
            }
            getOpenInstance().isLoadingForest = false
            log( "${getInstanceName()}-所有广告 ID 加载失败")
            return
        }

        val currentAdId = adIds[currentIndex]
        log( "${getInstanceName()}-开始加载广告 ID: $currentAdId")

        DataUser.openTypeIp = DataUser.connectIp
        adDataOpen = DataUpMix.beforeLoadLink(adData)
        val request = AdRequest.Builder().build()
        AppOpenAd.load(
            context,
            currentAdId,
            request,
            AppOpenAd.APP_OPEN_AD_ORIENTATION_PORTRAIT,
            object : AppOpenAd.AppOpenAdLoadCallback() {
                override fun onAdLoaded(ad: AppOpenAd) {
                    log( "open ads start loading success")
                    getOpenInstance().isLoadingForest = false
                    getOpenInstance().appAdDataForest = ad
                    getOpenInstance().loadTimeForest = Date().time
                    DataUpMix.abcGett(getInstanceName(), getLoadId(adData), getLoadIp())
                    ad.setOnPaidEventListener { adValue ->
                        DataUpMix.postAdData(
                            adValue,
                            ad.responseInfo,
                            adDataOpen!!,
                            adData.open,
                            "open",
                            "open"
                        )
                    }
                }

                override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                    getOpenInstance().isLoadingForest = false
                    getOpenInstance().appAdDataForest = null

                    val error =
                        """
           domain: ${loadAdError.domain}, code: ${loadAdError.code}, message: ${loadAdError.message}
          """"
                    log( "open ads start loading Failed=${error}")
                    DataUpMix.abcAskdis(getInstanceName(), getLoadId(adData), getLoadIp(), error)
                    loadOpenAdForest(context, adData, currentIndex + 1)
                }
            }
        )
    }


    private fun advertisingOpenCallbackForest(fullScreenFun: () -> Unit) {
        if (getOpenInstance().appAdDataForest !is AppOpenAd) {
            return
        }
        (getOpenInstance().appAdDataForest as AppOpenAd).fullScreenContentCallback =
            object : FullScreenContentCallback() {
                override fun onAdDismissedFullScreenContent() {
                    getOpenInstance().whetherToShowForest = false
                    getOpenInstance().appAdDataForest = null
                    fullScreenFun()
                }

                override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                    getOpenInstance().whetherToShowForest = false
                    getOpenInstance().appAdDataForest = null
                }

                override fun onAdShowedFullScreenContent() {
                    getOpenInstance().appAdDataForest = null
                    getOpenInstance().whetherToShowForest = true
                    DataUser.local_s_n = (DataUser.local_s_n ?: 0) + 1
                }

                override fun onAdClicked() {
                    super.onAdClicked()
                    DataUser.local_c_n = (DataUser.local_c_n ?: 0) + 1
                    ZZZ.clickAdZZ = true
                }
            }
    }


    fun displayOpenAdvertisementForest(
        activity: AppCompatActivity,
        fullScreenFun: () -> Unit
    ): Boolean {
        if (getOpenInstance().appAdDataForest == null) {
            return false
        }
        if (getOpenInstance().whetherToShowForest || activity.lifecycle.currentState != Lifecycle.State.RESUMED) {
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
        advertisingOpenCallbackForest(fullScreenFun)
        (getOpenInstance().appAdDataForest as AppOpenAd).show(activity)
        adDataOpen = DataUpMix.afterLoadLink(adDataOpen!!)
        DataUpMix.abcView(getInstanceName(), getLoadId(adDataOpen!!), getLoadIp())
        clearLoadIp()
        return true
    }

    private fun loadNativeAdvertisement(
        context: Context,
        adData: ForestAdBean,
        adBase: BaseAd?,
        currentIndex: Int = 0
    ) {
        if (adBase == null) {
            log( "adBase is null")
            return
        }

        // 获取广告 ID 列表
        val adIds = when (adBase) {
            getHomeInstance() -> DataUser.getAdmobIdList(adData.mnnt)
            getEndInstance() -> DataUser.getAdmobIdList(adData.rsnt)
            else -> DataUser.getAdmobIdList(adData.mnnt)
        }

        // 检查广告 ID 列表有效性
        if (adIds.isEmpty() || currentIndex >= adIds.size) {
            adBase.isLoadingForest = false
            log( "${adBase.getInstanceName()}-所有广告 ID 加载失败")
            return
        }

        val currentAdId = adIds[currentIndex]
        log( "${adBase.getInstanceName()}-开始加载广告 ID: $currentAdId")

        // 设置 IP 和广告数据
        when (adBase) {
            getHomeInstance() -> {
                DataUser.homeTypeIp = DataUser.connectIp
                adDataHome = DataUpMix.beforeLoadLink(adData)
            }

            getEndInstance() -> {
                DataUser.endTypeIp = DataUser.connectIp
                adDataResult = DataUpMix.beforeLoadLink(adData)
            }
        }

        try {
            // 构建广告加载器
            AdLoader.Builder(context.applicationContext, currentAdId)
                .apply {
                    withNativeAdOptions(
                        NativeAdOptions.Builder()
                            .setVideoOptions(VideoOptions.Builder().setStartMuted(true).build())
                            .setAdChoicesPlacement(NativeAdOptions.ADCHOICES_TOP_LEFT)
                            .setMediaAspectRatio(NativeAdOptions.NATIVE_MEDIA_ASPECT_RATIO_PORTRAIT)
                            .build()
                    )

                    forNativeAd {
                        adBase.appAdDataForest = it
                        log( "${adBase.getInstanceName()}-广告加载成功")
                        DataUpMix.abcGett(getInstanceName(), getLoadId(adData), getLoadIp())

                        it.setOnPaidEventListener { adValue ->
                            getNatData(it, adValue, currentAdId, adBase)
                            if (adBase == getHomeInstance()) {
                                getHomeInstance().advertisementLoadingForest(context)
                            }
                        }
                    }

                    withAdListener(object : AdListener() {
                        override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                            Log.e(
                                TAG,
                                "${adBase.getInstanceName()}-广告加载失败: ${loadAdError.message}"
                            )
                            adBase.isLoadingForest = false
                            adBase.appAdDataForest = null
                            DataUpMix.abcAskdis(
                                getInstanceName(),
                                getLoadId(adData),
                                getLoadIp(),
                                loadAdError.message
                            )
                            loadNativeAdvertisement(context, adData, adBase, currentIndex + 1)
                        }

                        override fun onAdLoaded() {
                            adBase.loadTimeForest = Date().time
                            adBase.isLoadingForest = false
                        }
                        override fun onAdClicked() {
                            super.onAdClicked()
                            log("点击原生广告")
                            DataUser.local_c_n = (DataUser.local_c_n ?: 0) + 1
                            ZZZ.clickAdZZ = true
                        }
                    })
                }
                .build()
                .loadAd(AdRequest.Builder().build())
        } catch (e: Exception) {
            log( "Error building AdLoader or AdRequest: ${e.message}")
        }
    }
    private fun loadIntAdvertisementForest(
        context: Context,
        adData: ForestAdBean,
        adBase: BaseAd?,
        currentIndex: Int = 0
    ) {
        if (adBase == null) {
            log( "adBase is null")
            return
        }

        // 获取广告 ID 列表
        val adIds = when (adBase) {
            getConnectInstance() -> DataUser.getAdmobIdList(adData.ctint)
            getBackEndInstance() -> DataUser.getAdmobIdList(adData.bcintres)
            else -> DataUser.getAdmobIdList(adData.bcintserv)
        }

        // 检查广告 ID 列表有效性
        if (adIds.isEmpty() || currentIndex >= adIds.size) {
            adBase.isLoadingForest = false
            log( "${adBase.getInstanceName()}-所有广告 ID 加载失败")
            return
        }

        val currentAdId = adIds[currentIndex]
        log( "${adBase.getInstanceName()}-开始加载广告 ID: $currentAdId")

        val adRequest = AdRequest.Builder().build()
        when (adBase) {
            getConnectInstance() -> {
                DataUser.contTypeIp = DataUser.connectIp
                adDataCont = DataUpMix.beforeLoadLink(adData)

            }

            getBackEndInstance() -> {
                DataUser.backEndTypeIp = DataUser.connectIp
                adDataEnd = DataUpMix.beforeLoadLink(adData)
            }

            getBackListInstance() -> {
                DataUser.backListTypeIp = DataUser.connectIp
                adDataList = DataUpMix.beforeLoadLink(adData)
            }
        }
        InterstitialAd.load(
            context,
            currentAdId,
            adRequest,
            object : InterstitialAdLoadCallback() {
                override fun onAdFailedToLoad(adError: LoadAdError) {
                    adBase.isLoadingForest = false
                    adBase.appAdDataForest = null
                    val error =
                        """
           domain: ${adError.domain}, code: ${adError.code}, message: ${adError.message}
          """
                    log( "${adBase.getInstanceName()}-The ad failed to load:$error ")
                    DataUpMix.abcAskdis(getInstanceName(), getLoadId(adData), getLoadIp(), error)
                    loadIntAdvertisementForest(context, adData, adBase, currentIndex + 1)
                }

                override fun onAdLoaded(interstitialAd: InterstitialAd) {
                    log( "${adBase.getInstanceName()}-The ad loads successfully: ")
                    adBase.loadTimeForest = Date().time
                    adBase.isLoadingForest = false
                    adBase.appAdDataForest = interstitialAd
                    DataUpMix.abcGett(getInstanceName(), getLoadId(adData), getLoadIp())
                    getIntData(interstitialAd, currentAdId, adBase)
                }
            })
    }

    fun getIntData(ad: InterstitialAd, loadId: String, adBase: BaseAd) {
        val bean = when (adBase) {
            getConnectInstance() -> {
                adDataCont
            }

            getBackEndInstance() -> {
                adDataEnd
            }

            getBackListInstance() -> {
                adDataList
            }

            else -> {
                null
            }
        }
        val adWhere = when (adBase) {
            getConnectInstance() -> {
                "ctint"
            }

            getBackEndInstance() -> {
                "bcintres"
            }

            getBackListInstance() -> {
                "bcintserv"
            }

            else -> {
                ""
            }
        }
        ad.setOnPaidEventListener { adValue ->
            log("插屏广告 -${adWhere}，开始上报: ")
            DataUpMix.postAdData(
                adValue,
                ad.responseInfo,
                bean!!,
                loadId,
                adWhere,
                "int"
            )
        }
    }

    private fun intScreenAdCallback(adBase: BaseAd, closeWindowFun: () -> Unit) {
        (adBase.appAdDataForest as? InterstitialAd)?.fullScreenContentCallback =
            object : FullScreenContentCallback() {
                override fun onAdClicked() {
                    DataUser.local_c_n = (DataUser.local_c_n ?: 0) + 1
                    ZZZ.clickAdZZ = true
                }

                override fun onAdDismissedFullScreenContent() {
                    adBase.appAdDataForest = null
                    adBase.whetherToShowForest = false
                    closeWindowFun()

                }

                override fun onAdFailedToShowFullScreenContent(p0: AdError) {
                    adBase.appAdDataForest = null
                    adBase.whetherToShowForest = false
                }

                override fun onAdImpression() {
                }

                override fun onAdShowedFullScreenContent() {
                    adBase.appAdDataForest = null
                    adBase.whetherToShowForest = true
                    DataUser.local_s_n = (DataUser.local_s_n ?: 0) + 1
                }
            }
    }

    fun canShowAd(
        activity: AppCompatActivity,
        adBase: BaseAd
    ): Int {
        if (!VPNGet.isVPNConnected() || getOpenInstance().limitIsExceeded()) {
            return 0
        }
        val blacklistState = DataUser.blockAdBlacklist()
        if (blacklistState && (adBase == getConnectInstance() || adBase == getBackEndInstance() || adBase == getBackListInstance() || adBase == getHomeInstance())) {
            log( "黑名单屏蔽：${adBase.getInstanceName()}广告，不显示")
            return 0
        }
        if (DataUser.bbb_admin == "2" && (adBase == getConnectInstance() || adBase == getBackEndInstance() || adBase == getHomeInstance())) {
            Log.e("TAG", "admin屏蔽${instanceName}广告")
            return 0
        }
        if (adBase.appAdDataForest == null) {
            return 1
        }

        if (adBase.whetherToShowForest || activity.lifecycle.currentState != Lifecycle.State.RESUMED) {
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

    fun playIntAdvertisementForest(
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
                (adBase.appAdDataForest as InterstitialAd).show(activity)
                when (adBase) {
                    getConnectInstance() -> {
                        adDataCont = DataUpMix.afterLoadLink(adDataCont!!)
                        DataUpMix.abcView(getInstanceName(), getLoadId(adDataCont!!), getLoadIp())
                    }

                    getBackEndInstance() -> {
                        adDataEnd = DataUpMix.afterLoadLink(adDataEnd!!)
                        DataUpMix.abcView(getInstanceName(), getLoadId(adDataEnd!!), getLoadIp())
                    }

                    getBackListInstance() -> {
                        adDataList = DataUpMix.afterLoadLink(adDataList!!)
                        DataUpMix.abcView(getInstanceName(), getLoadId(adDataList!!), getLoadIp())
                    }
                }
                clearLoadIp()
            }
        }
    }

    fun playNativeAdvertisementForest(
        activity: AppCompatActivity,
        adBase: BaseAd,
    ) {
        Log.e(
            TAG,
            "${adBase.getInstanceName()}-ip一致-展示-load_ip=" + getLoadIp() + "-now-ip=" + DataUser.connectIp
        )
        if (adBase == getHomeInstance()) {
            setDisplayHomeNativeAdForest(activity as ZZ, adBase)
        } else {
            setDisplayEndNativeAdForest(activity as EE, adBase)
        }
    }



    private fun getNatData(ad: NativeAd, adValue: AdValue, loadId: String, adBase: BaseAd) {
        val bean = when (adBase) {
            getHomeInstance() -> {
                adDataHome
            }

            getEndInstance() -> {
                adDataResult
            }

            else -> {
                null
            }
        }
        val adWhere = when (adBase) {
            getHomeInstance() -> {
                "mnnt"
            }

            getEndInstance() -> {
                "rsnt"
            }

            else -> {
                ""
            }
        }
        log("原生广告 -${adWhere}，开始上报: ")
        ad.responseInfo?.let {
            DataUpMix.postAdData(
                adValue,
                it,
                bean!!,
                loadId,
                adWhere,
                "native"
            )
        }
    }

    private fun setDisplayHomeNativeAdForest(activity: ZZ, adBase: BaseAd) {
        activity.lifecycleScope.launch(Dispatchers.Main) {
            (adBase.appAdDataForest as NativeAd).let { adData ->
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
                    DataUpMix.abcView(getInstanceName(), getLoadId(adDataHome!!), getLoadIp())
                    activity.binding.imgOcAd.isVisible = false
                    activity.binding.adLayoutAdmob.isVisible = true
                    adBase.appAdDataForest = null
                    adBase.isLoadingForest = false
                    DataUser.homeTypeIp = ""
                    DataUser.local_s_n = (DataUser.local_s_n ?: 0) + 1
                    adDataHome = DataUpMix.afterLoadLink(adDataHome!!)

                }
            }
        }
    }

    private fun setDisplayEndNativeAdForest(activity: EE, adBase: BaseAd) {
        activity.lifecycleScope.launch(Dispatchers.Main) {
            (adBase.appAdDataForest as NativeAd).let { adData ->
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
                    DataUpMix.abcView(getInstanceName(), getLoadId(adDataResult!!), getLoadIp())
                    activity.binding.imgOcAd.isVisible = false
                    activity.binding.adLayoutAdmob.isVisible = true
                    adBase.appAdDataForest = null
                    adBase.isLoadingForest = false
                    DataUser.endTypeIp = ""
                    adDataResult = DataUpMix.afterLoadLink(adDataResult!!)
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

    private fun getLoadIdLog(adBean: ForestAdBean): String {
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

    private fun getLoadId(adBean: ForestAdBean): String {
        return when (id) {
            1 -> adBean.open
            2 -> adBean.mnnt
            3 -> adBean.rsnt
            4 -> adBean.ctint
            5 -> adBean.bcintres
            6 -> adBean.bcintserv
            else -> ""
        }
    }

    fun isAppOpenSameDayBa() {
        if (DataUser.ad_load_date.isNullOrBlank()) {
            DataUser.ad_load_date = formatDateNow()
        } else {
            if (dateAfterDate(DataUser.ad_load_date, formatDateNow())) {
                DataUser.ad_load_date = formatDateNow()
                log("超限-清除数据:")
                DataUser.local_s_n = 0
                DataUser.local_c_n = 0
                DataUser.adLimState = ""
            }
        }
    }

    private fun formatDateNow(): String {
        val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd")
        val date = Date()
        return simpleDateFormat.format(date)
    }

    private fun dateAfterDate(startTime: String?, endTime: String?): Boolean {
        val format = SimpleDateFormat("yyyy-MM-dd")
        try {
            val startDate: Date = format.parse(startTime)
            val endDate: Date = format.parse(endTime)
            val start: Long = startDate.getTime()
            val end: Long = endDate.getTime()
            if (end > start) {
                return true
            }
        } catch (e: Exception) {
            return false
        }
        return false
    }

     fun limitIsExceeded(): Boolean {
        isAppOpenSameDayBa()
        val adOpenNum = DataUser.getAdJson().ffftt_skhjg
        val adClickNum = DataUser.getAdJson().ffftt_csadf
        val currentOpenCount = DataUser.local_s_n ?: 0
        val currentClickCount = DataUser.local_c_n ?: 0
        if (currentOpenCount >= adOpenNum && DataUser.adLimState != "limit") {
            adUpperLimitAdPointData("show")
        }
        if (currentClickCount >= adClickNum && DataUser.adLimState != "limit") {
            adUpperLimitAdPointData("click")
        }
        return (currentOpenCount >= adOpenNum) || (currentClickCount >= adClickNum)
    }

    private fun adUpperLimitAdPointData(showType: String) {
        DataUser.adLimState = "limit"
        DataUpMix.postPointData("abc_limit", "type", showType)
    }
}

