package com.two.protocol.ladders.fourth.uuutt

import android.content.Context
import android.util.Log
import androidx.appcompat.app.AlertDialog
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
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
        return  DataUser.codeConKey in restrictedCountryCodes
    }

    private fun isRestrictedLocale(context: Context): Boolean {
        val restrictedLocales =
            listOf(Locale.SIMPLIFIED_CHINESE, Locale.TRADITIONAL_CHINESE, Locale("fa", "IR"))
        val currentLocale: Locale =
            context.resources.configuration.locales[0]
        Log.e("TAG", "isRestrictedLocale1: ${currentLocale.country}", )
        for (data in restrictedLocales){
            Log.e("TAG", "isRestrictedLocale2: ${data.country}", )
          return   currentLocale.country.contains(data.country)
        }
        Log.e("TAG", "isRestrictedLocale3")

        return false
    }

    private fun shouldIntercept(context: Context): Boolean {
        return isRestrictedCountryOrRegion() || isRestrictedLocale(context)
    }

    fun showDueDialog(context: Context): Boolean {
//        if (shouldIntercept(context)) {
//            AlertDialog.Builder(context).apply {
//                setTitle("WARN")
//                setMessage("Due to the policy reason , this service is not available in your country")
//                setPositiveButton("OK") { dialog, _ ->
//                    dialog.dismiss()
//                    exitProcess(0)
//                }
//                setCancelable(false)
//                show()
//            }
//            return true
//        }
        return false
    }
}