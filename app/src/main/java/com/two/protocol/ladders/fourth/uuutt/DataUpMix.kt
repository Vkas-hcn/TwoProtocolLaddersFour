package com.two.protocol.ladders.fourth.uuutt

import android.content.Context
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.webkit.WebSettings
import com.adjust.sdk.Adjust
import com.adjust.sdk.AdjustAdRevenue
import com.adjust.sdk.AdjustConfig
import com.android.installreferrer.api.ReferrerDetails
import com.facebook.appevents.AppEventsLogger.Companion.newLogger
import com.github.shadowsocks.utils.Key.name
import com.google.android.gms.ads.AdValue
import com.google.android.gms.ads.ResponseInfo
import com.google.android.gms.ads.identifier.AdvertisingIdClient
import com.google.firebase.analytics.FirebaseAnalytics
import com.two.protocol.ladders.fourth.BuildConfig
import com.two.protocol.ladders.fourth.aaaaa.ZZZ
import com.two.protocol.ladders.fourth.aaaaa.ZZZ.Companion.appContext
import com.two.protocol.ladders.fourth.aaaaa.ZZZ.Companion.saoState
import com.two.protocol.ladders.fourth.ggggg.ForestAdBean

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject
import timber.log.Timber.Forest
import timber.log.Timber.Forest.tag
import java.io.BufferedReader
import java.io.InputStreamReader
import java.math.BigDecimal
import java.util.Currency
import java.util.Locale
import java.util.UUID
import kotlin.jvm.internal.Intrinsics

object DataUpMix {

    fun getAppVersion(): String {
        try {
            val packageInfo =
                appContext.packageManager.getPackageInfo(appContext.packageName, 0)
            val str = packageInfo.versionName
            Intrinsics.checkNotNullExpressionValue(str, "packageInfo.versionName")
            return str
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
            return "Version information not available"
        }
    }

    private fun firstJsonData(): JSONObject {
        return JSONObject().apply {
            put("riddance", JSONObject().apply {
                //manufacturer
                put("ounce", "111")
                //app_version
                put("brandt", getAppVersion())
                //os
                put("schism", "shoshone")
                //bundle_id
                put("frazzle", appContext.packageName)
                //os_version
                put("haste", "1")
                //log_id
                put("stein", UUID.randomUUID().toString())
                //device_model
                put("nature", "111")
                //operator
                put("jeannie", "111")
                //android_id
                put("invoice", "1")
            })
            put("teetotal", JSONObject().apply {
                //client_ts
                put("commerce", System.currentTimeMillis())
                //distinct_id
                put("dedicate", DataUser.postUUID)
                //shabby gaid
                put(
                    "shabby",
                    (runCatching { AdvertisingIdClient.getAdvertisingIdInfo(appContext).id }.getOrNull()
                        ?: "")
                )
                //system_language
                put("jacobean", Locale.getDefault().language + '_' + Locale.getDefault().country)
            })
        }

    }

    private fun getSessionJson(): String {
        return firstJsonData().apply {
            put("ala", {})
        }.toString()
    }

    private fun getInstallJson(referrerDetails: ReferrerDetails): String {
        return firstJsonData().apply {
            put("knockout", JSONObject().apply {
                //build
                put("smoky", "build/${Build.ID}")

                //referrer_url
                put("alabama", referrerDetails.installReferrer)

                //install_version
                put("fourfold", referrerDetails.installVersion)

                //user_agent
                put("wharton", getWebDefaultUserAgent())

                //lat
                put("veal", getLimitTracking())

                //referrer_click_timestamp_seconds
                put("jill", referrerDetails.referrerClickTimestampSeconds)

                //install_begin_timestamp_seconds
                put("scandium", referrerDetails.installBeginTimestampSeconds)

                //referrer_click_timestamp_server_seconds
                put("karp", referrerDetails.referrerClickTimestampServerSeconds)

                //install_begin_timestamp_server_seconds
                put("rheum", referrerDetails.installBeginTimestampServerSeconds)

                //install_first_seconds
                put("prolific", getFirstInstallTime())

                //last_update_seconds
                put("hewett", getLastUpdateTime())
            })

        }.toString()
    }

    private fun getAdJson(
        adValue: AdValue,
        responseInfo: ResponseInfo,
        adInformation: ForestAdBean,
        adId: String,
        adWhere: String,
        adType: String?
    ): String {
        val topLevelJson = firstJsonData()
        topLevelJson.apply {
            put("verlag_ot", adInformation.loadCity)
            put("verlag_cy", adInformation.showTheCity)
        }
        val `$this$getAdJson_u24lambda_u245_u24lambda_u244` = JSONObject()
        `$this$getAdJson_u24lambda_u245_u24lambda_u244`.put("gainful", adValue.valueMicros)
        `$this$getAdJson_u24lambda_u245_u24lambda_u244`.put("perform", adValue.currencyCode)
        `$this$getAdJson_u24lambda_u245_u24lambda_u244`.put(
            "yolk",
            responseInfo.mediationAdapterClassName
        )
        `$this$getAdJson_u24lambda_u245_u24lambda_u244`.put("syphilis", "admob")
        `$this$getAdJson_u24lambda_u245_u24lambda_u244`.put("slush", adId)
        `$this$getAdJson_u24lambda_u245_u24lambda_u244`.put("shrove", adWhere)
        `$this$getAdJson_u24lambda_u245_u24lambda_u244`.put("cadmium", adType)
        `$this$getAdJson_u24lambda_u245_u24lambda_u244`.put(
            "luger",
            getPrecisionType(adValue.precisionType)
        )
        var loadIp = adInformation.loadIp
        if (loadIp == null) {
            loadIp = ""
        }
        `$this$getAdJson_u24lambda_u245_u24lambda_u244`.put("semite", loadIp)
        val showIp = adInformation.showIp
        `$this$getAdJson_u24lambda_u245_u24lambda_u244`.put("facet", showIp ?: "")
        topLevelJson.put("takeoff", `$this$getAdJson_u24lambda_u245_u24lambda_u244`)
        val jSONObject = topLevelJson.toString()
        Intrinsics.checkNotNullExpressionValue(jSONObject, "topLevelJson.toString()")
        return jSONObject
    }


    private fun getTbaDataJson(name: String): String {
        return firstJsonData().apply {
            put("bourbon", name)
        }.toString()
    }

    fun getTbaTimeDataJson(
        name: String?,
        parameterName1: String,
        parameterValue1: Any?,
        parameterName2: String?,
        parameterValue2: Any?,
        parameterName3: String?,
        parameterValue3: Any?,
        parameterName4: String?,
        parameterValue4: Any?
    ): String {
        val `$this$getTbaTimeDataJson_u24lambda_u247` = firstJsonData()
        `$this$getTbaTimeDataJson_u24lambda_u247`.put("bourbon", name)
        `$this$getTbaTimeDataJson_u24lambda_u247`.put("verlag_$parameterName1", parameterValue1)
        if (parameterName2 != null) {
            `$this$getTbaTimeDataJson_u24lambda_u247`.put(
                "verlag_$parameterName2",
                parameterValue2
            )
        }
        if (parameterName3 != null) {
            `$this$getTbaTimeDataJson_u24lambda_u247`.put(
                "verlag_$parameterName3",
                parameterValue3
            )
        }
        if (parameterName4 != null) {
            `$this$getTbaTimeDataJson_u24lambda_u247`.put(
                "verlag_$parameterName4",
                parameterValue4
            )
        }
        val jSONObject = `$this$getTbaTimeDataJson_u24lambda_u247`.toString()
        Intrinsics.checkNotNullExpressionValue(
            jSONObject,
            "firstJsonData().apply {\n…   }\n        }.toString()"
        )
        return jSONObject
    }

    fun postAdOnline(adValue: AdValue, responseInfo: ResponseInfo?) {
        val adRevenue = AdjustAdRevenue(AdjustConfig.AD_REVENUE_ADMOB)
        adRevenue.setRevenue(adValue.valueMicros / 1000000.0, adValue.currencyCode)
        adRevenue.setAdRevenueNetwork(responseInfo?.mediationAdapterClassName)
        Adjust.trackAdRevenue(adRevenue)
        // TODO fb id 2
        if (!BuildConfig.DEBUG && DataUser.getLogicJson().dda.isNotBlank()) {
            newLogger(appContext).logPurchase(
                BigDecimal((adValue.valueMicros / 1000000.0).toString()),
                Currency.getInstance("USD")
            )
            return
        }
        Log.d("TBA", "purchase打点--value=" + adValue.valueMicros)
    }

    fun beforeLoadLink(adInformation: ForestAdBean): ForestAdBean {
        Log.e(
            "TAG",
            "beforeLoadLink: saoState=${saoState}---appContext.vpn_ip=${DataUser.connectIp}",
        )
        if (saoState) {
            adInformation.loadIp = DataUser.connectIp
            adInformation.loadCity = DataUser.connectCity
            return adInformation
        }
        adInformation.loadIp = DataUser.localIp.toString()
        adInformation.loadCity = "none"
        return adInformation
    }

    fun afterLoadLink(adInformation: ForestAdBean): ForestAdBean {
        if (saoState) {
            adInformation.showIp = DataUser.connectIp
            adInformation.showTheCity = DataUser.connectCity
            return adInformation
        }
        adInformation.showIp = DataUser.localIp.toString()
        adInformation.showTheCity = "none"
        return adInformation
    }

    fun postSessionData() {
        val data = getSessionJson()
        tag("TAG").e("postSessionData: data=%s", data)
        NetOnInfo.postInformation(data, object : NetOnInfo.ResponseCallback {
            override fun onSuccess(response: String) {
                Intrinsics.checkNotNullParameter(response, "response")
                tag("TAG").e("postSessionData: onSuccess=%s", response)
            }

            // com.otters.lying.flat.eating.kiwifruit.saturnvpn.uuuuss.NetUtils.Callback
            override fun onFailure(error: String) {
                Intrinsics.checkNotNullParameter(error, "error")
                tag("TAG").e("postSessionData: onFailure=%s", error)
            }
        })
    }

    fun postInstallJson(rd: ReferrerDetails?) {
        Intrinsics.checkNotNullParameter(rd, "rd")
        val data = getInstallJson(rd!!)
        tag("TAG").e("postInstallJson: data=%s", data)
        NetOnInfo.postInformation(data, object : NetOnInfo.ResponseCallback {
            override fun onSuccess(response: String) {
                Intrinsics.checkNotNullParameter(response, "response")
                tag("TAG").e("postInstallJson: onSuccess=%s", response)
                DataUser.installUpState = "1"
            }

            override fun onFailure(error: String) {
                Intrinsics.checkNotNullParameter(error, "error")
                tag("TAG").e("postInstallJson: onFailure=%s", error)
                DataUser.installUpState = "0"
            }
        })
    }

    fun postAdData(
        adValue: AdValue,
        responseInfo: ResponseInfo,
        adInformation: ForestAdBean,
        adId: String,
        adWhere: String,
        adType: String?
    ) {
        Intrinsics.checkNotNullParameter(adValue, "adValue")
        Intrinsics.checkNotNullParameter(responseInfo, "responseInfo")
        Intrinsics.checkNotNullParameter(adInformation, "adInformation")
        Intrinsics.checkNotNullParameter(adType, "adType")
        val data = getAdJson(adValue, responseInfo, adInformation, adId, adWhere, adType)
        tag("TAG").e("${adWhere}-postAdData: data=%s", data)
        NetOnInfo.postInformation(data, object : NetOnInfo.ResponseCallback {
            override fun onSuccess(response: String) {
                Intrinsics.checkNotNullParameter(response, "response")
                tag("TAG").e("${adWhere}-postAdData: onSuccess=%s", response)
            }

            override fun onFailure(error: String) {
                Intrinsics.checkNotNullParameter(error, "error")
                tag("TAG").e("${adWhere}-postAdData: onFailure=%s", error)
            }
        })
        postAdOnline(adValue, responseInfo)
    }


    fun postPointData(
        name: String,
        key: String? = null,
        keyValue: Any? = null,
        key2: String? = null,
        keyValue2: Any? = null,
        key3: String? = null,
        keyValue3: Any? = null,
        key4: String? = null,
        keyValue4: Any? = null
    ) {
        Intrinsics.checkNotNullParameter(name, "name")
        val pointJson = if (key != null && keyValue != null) {
            getTbaTimeDataJson(
                name,
                key,
                keyValue,
                key2,
                keyValue2,
                key3,
                keyValue3,
                key4,
                keyValue4
            )
        } else {
            getTbaDataJson(name)
        }
        log("${name}-打点--Json--->${pointJson}")
        try {
            NetOnInfo.postInformation(pointJson, object : NetOnInfo.ResponseCallback {
                override fun onSuccess(response: String) {
                    log("${name}-打点事件上报-成功->${response}")

                }

                override fun onFailure(error: String) {
                    log("${name}-打点事件上报-失败=$error")
                    log("${name}-打点事件上报-失败=$error")
                }
            })
            pointFaceBook()
        } catch (e: java.lang.Exception) {
            log("${name}-打点事件上报-失败=$e")
        }
    }

    private fun pointFaceBook(
        key1: String? = null,
        keyValue1: Any? = null,
        key2: String? = null,
        keyValue2: Any? = null,
        key3: String? = null,
        keyValue3: Any? = null,
        key4: String? = null,
        keyValue4: Any? = null
    ) {
        if (DataUser.getLogicJson().dda.isBlank()) {
            return
        }
        val bundleEvent = Bundle()
        if (keyValue1 != null) {
            bundleEvent.putString(key1, keyValue1.toString())
        }
        if (keyValue2 != null) {
            bundleEvent.putString(key2, keyValue2.toString())
        }
        if (keyValue3 != null) {
            bundleEvent.putString(key3, keyValue3.toString())
        }
        if (keyValue4 != null) {
            bundleEvent.putString(key4, keyValue4.toString())
        }
        FirebaseAnalytics.getInstance(appContext).logEvent(name, bundleEvent)
    }

    fun abcAsk(adWhere: String, id: String) {
        postPointData(
            "abc_ask",
            "inform",
            adWhere,
            "page",
            getTextPage(),
            "ID",
            "${id}+${saoState}",
            "IP",
            DataUser.connectIp
        )
        if (saoState) {
            postPointData(
                "abc_connect_ask",
                "inform",
                adWhere,
                "page",
                getTextPage(),
                "ID",
                "${id}+${saoState}",
                "IP",
                DataUser.connectIp
            )
        }
    }

    fun abcGett(adWhere: String, id: String, ip: String) {
        postPointData(
            "abc_gett",
            "inform",
            adWhere,
            "page",
            getTextPage(),
            "ID",
            "${id}+${saoState}",
            "IP",
            ip
        )
    }

    fun abcAskdis(adWhere: String, id: String, ip: String, errorString: String) {
        postPointData(
            "abc_askdis",
            "inform",
            adWhere,
            "ID",
            "${id}+${saoState}",
            "IP",
            ip,
            "reason",
            errorString
        )
    }

    fun abcView(adWhere: String, id: String, ip: String) {
        postPointData(
            "abc_view",
            "inform",
            adWhere,
            "page",
            getTextPage(),
            "ID",
            "${id}+${saoState}",
            "pp",
            ip
        )
    }


    private fun getTextPage(): String {
        return when (ZZZ.nowAName) {
            "EE" -> {
                "EndPage"
            }

            "GG" -> {
                "StartPage"
            }

            "LL" -> {
                "ListPage"
            }

            "ZZ" -> {
                "HomePage"
            }

            else -> {
                "OtherPage"
            }
        }
    }

    private fun getWebDefaultUserAgent(): String {
        return try {
            WebSettings.getDefaultUserAgent(appContext)
        } catch (e: Exception) {
            ""
        }
    }

    private fun getFirstInstallTime(): Long {
        try {
            val packageInfo =
                appContext.packageManager.getPackageInfo(appContext.packageName, 0)
            return packageInfo.firstInstallTime / 1000
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
        return 0
    }

    private fun getLastUpdateTime(): Long {
        try {
            val packageInfo =
                appContext.packageManager.getPackageInfo(appContext.packageName, 0)
            return packageInfo.lastUpdateTime / 1000
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
        return 0
    }

    private fun getPrecisionType(precisionType: Int): String {
        return when (precisionType) {
            0 -> {
                "UNKNOWN"
            }

            1 -> {
                "ESTIMATED"
            }

            2 -> {
                "PUBLISHER_PROVIDED"
            }

            3 -> {
                "PRECISE"
            }

            else -> {
                "UNKNOWN"
            }
        }
    }

    private fun getLimitTracking(): String {
        return try {
            if (AdvertisingIdClient.getAdvertisingIdInfo(appContext).isLimitAdTrackingEnabled) {
                "mastery"
            } else {
                "foamy"
            }
        } catch (e: Exception) {
            "foamy"
        }
    }

    fun log(msg: String) {
        if (BuildConfig.DEBUG) {
            Log.e("TAG", msg)
        }
    }
}