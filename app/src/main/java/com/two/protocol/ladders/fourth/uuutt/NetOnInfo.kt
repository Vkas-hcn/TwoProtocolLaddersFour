package com.two.protocol.ladders.fourth.uuutt

import android.content.Context
import android.content.pm.PackageManager
import android.util.Log
import androidx.appcompat.app.AlertDialog
import com.google.android.gms.common.util.DataUtils
import com.two.protocol.ladders.fourth.BuildConfig
import com.two.protocol.ladders.fourth.aaaaa.ZZZ
import com.two.protocol.ladders.fourth.uuutt.DataUser.log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.IOException
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder
import java.util.Locale
import kotlin.system.exitProcess

object NetOnInfo {
    fun getIPInfo() {
        val primaryUrl = "https://api.myip.com/"
        val secondaryUrl = "https://api.infoip.io/"
        val primaryResponse = fetchIPInfo(primaryUrl)
        if (primaryResponse != null) {
            val jsonObject = JSONObject(primaryResponse)
            IPInfo(
                ip = jsonObject.getString("ip"),
                country = jsonObject.getString("country"),
                countryCode = jsonObject.getString("cc")
            )
            DataUser.codeConKey = jsonObject.getString("cc")
            if(!ZZZ.saoState){
                DataUser.localIp = jsonObject.getString("ip")
            }
        } else {
            val secondaryResponse = fetchIPInfo(secondaryUrl)
            if (secondaryResponse != null) {
                val jsonObject = JSONObject(secondaryResponse)
                IPInfo(
                    ip = jsonObject.getString("ip"),
                    country = jsonObject.getString("country_long"),
                    countryCode = jsonObject.getString("country_short"),
                    city = jsonObject.getString("city"),
                    region = jsonObject.getString("region"),
                    timezone = jsonObject.getString("timezone"),
                    latitude = jsonObject.getDouble("latitude"),
                    longitude = jsonObject.getDouble("longitude"),
                    postalCode = jsonObject.getString("postal_code"),
                    countryShort = jsonObject.getString("country_short"),
                    countryLong = jsonObject.getString("country_long")
                )
                DataUser.codeConKey = jsonObject.getString("country_short")
                if(!ZZZ.saoState){
                    DataUser.localIp = jsonObject.getString("ip")
                }
            }
        }
    }

    data class IPInfo(
        var ip: String? = null,
        var country: String? = null,
        var countryCode: String? = null,
        var city: String? = null,
        var region: String? = null,
        var timezone: String? = null,
        var latitude: Double? = null,
        var longitude: Double? = null,
        var postalCode: String? = null,
        var countryShort: String? = null,
        var countryLong: String? = null
    )

    private fun fetchIPInfo(url: String): String? {

        return try {

            val urlObj = URL(url)
            val conn = urlObj.openConnection() as HttpURLConnection
            conn.requestMethod = "GET"
            conn.connectTimeout = 5000
            conn.readTimeout = 5000
            val responseCode = conn.responseCode

            if (responseCode == HttpURLConnection.HTTP_OK) {
                val inputStream = conn.inputStream
                val reader = BufferedReader(InputStreamReader(inputStream))
                val response = reader.readText()
                reader.close()
                response
            } else {
                Log.e(
                    "TAG",
                    "Error fetching IP info=connection.responseCode=${conn.responseCode}"
                )
                null
            }
        } catch (e: Exception) {
            Log.e("TAG", "Error fetching IP info: ${e.message}")
            e.printStackTrace()
            null
        }
    }

    private fun isRestrictedCountryOrRegion(): Boolean {
        val restrictedCountryCodes = listOf("CN", "IR", "MO", "HK")
        return DataUser.codeConKey in restrictedCountryCodes
    }

    private fun isRestrictedLocale(context: Context): Boolean {
        val restrictedLocales =
            listOf(Locale.SIMPLIFIED_CHINESE, Locale.TRADITIONAL_CHINESE, Locale("fa", "IR"))
        val currentLocale: Locale =
            context.resources.configuration.locales[0]
        Log.e("TAG", "isRestrictedLocale1: ${currentLocale.country}")
        for (data in restrictedLocales) {
            Log.e("TAG", "isRestrictedLocale2: ${data.country}")
            return currentLocale.country.contains(data.country)
        }
        Log.e("TAG", "isRestrictedLocale3")

        return false
    }

    private fun shouldIntercept(context: Context): Boolean {
        return isRestrictedCountryOrRegion() || isRestrictedLocale(context)
    }

    fun showDueDialog(context: Context): Boolean {
        if (BuildConfig.DEBUG) {
            return false
        }
        if (shouldIntercept(context)) {
            DataUpMix.postPointData("u_area_limit", "cy", DataUser.codeConKey)
            AlertDialog.Builder(context).apply {
                setTitle("WARN")
                setMessage("Due to the policy reason , this service is not available in your country")
                setPositiveButton("OK") { dialog, _ ->
                    dialog.dismiss()
                    exitProcess(0)
                }
                setCancelable(false)
                show()
            }
            return true
        }
        return false
    }

    private fun getAppVersion(): String? {
        return try {
            val packageInfo =
                ZZZ.appContext.packageManager.getPackageInfo(ZZZ.appContext.packageName, 0)
            packageInfo.versionName
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
            null
        }
    }

    private fun blackData(): Map<String, Any> {
        return mapOf(
            "frazzle" to "com.forest.stable.game.video.fast.easy",
            "schism" to "shoshone",
            "brandt" to getAppVersion().orEmpty(),
            "dedicate" to DataUser.postUUID
        )
    }

    fun getBlackList(context: Context) {
        if (!DataUser.blockData.isNullOrBlank()) {
            return
        }
        postMapData(
            "https://font.foreststable.com/traffic/gunflint",
            blackData(),
            object : ResponseCallback {
                override fun onSuccess(response: String) {
                    log("The blacklist request is successful：$response")
                    DataUser.blockData = response
                }

                override fun onFailure(error: String) {
                    GlobalScope.launch(Dispatchers.IO) {
                        delay(10000)
                        log("The blacklist request failed：$error")
                        getBlackList(context)
                    }
                }
            })
    }

    interface ResponseCallback {
        fun onSuccess(response: String)
        fun onFailure(error: String)
    }
    private fun postMapData(
        url: String,
        map: Map<String, Any>,
        callback: ResponseCallback
    ) {
        val jsonBody = JSONObject(map).toString()  // Convert map to JSON string

        val urlObj = URL(url)
        val connection = urlObj.openConnection() as HttpURLConnection
        connection.requestMethod = "POST"
        connection.connectTimeout = 12000
        connection.doOutput = true
        connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8")

        try {
            // Write JSON to the request body
            val outputStream = OutputStreamWriter(connection.outputStream, "UTF-8")
            outputStream.write(jsonBody)
            outputStream.flush()
            outputStream.close()

            val responseCode = connection.responseCode
            if (responseCode == HttpURLConnection.HTTP_OK) {
                val inputStream = connection.inputStream
                val reader = BufferedReader(InputStreamReader(inputStream))
                val response = StringBuilder()
                var line: String?
                while (reader.readLine().also { line = it } != null) {
                    response.append(line)
                }
                reader.close()
                inputStream.close()
                callback.onSuccess(response.toString())
            } else {
                callback.onFailure("HTTP error: $responseCode")
            }
        } catch (e: Exception) {
            callback.onFailure("Network error: ${e.message}")
        } finally {
            connection.disconnect()
        }
    }
    fun postInformation(body: Any, callback: ResponseCallback) {
        Thread {
            var connection: HttpURLConnection? = null
            try {
                val urlConnection = URL(DataUser.tba_url).openConnection() as HttpURLConnection
                connection = urlConnection
                connection.requestMethod = "POST"
                connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8")
                connection.setRequestProperty("Accept", "application/json")
                connection.doOutput = true
                connection.doInput = true

                // Write body to the request
                BufferedWriter(OutputStreamWriter(connection.outputStream, "UTF-8")).use { writer ->
                    writer.write(body.toString())
                    writer.flush()
                }

                val responseCode = connection.responseCode
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    val responseBody = connection.inputStream.bufferedReader().use { it.readText() }
                    callback.onSuccess(responseBody)
                } else {
                    val errorBody = connection.errorStream.bufferedReader().use { it.readText() }
                    callback.onFailure(errorBody)
                }
            } catch (e: IOException) {
                callback.onFailure("Network error: ${e.message}")
            } finally {
                connection?.disconnect()
            }
        }.start()
    }
}