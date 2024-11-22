package com.two.protocol.ladders.fourth.uuutt

import android.annotation.SuppressLint
import android.util.Base64
import com.two.protocol.ladders.fourth.BuildConfig
import com.two.protocol.ladders.fourth.aaaaa.ZZZ
import com.two.protocol.ladders.fourth.uuutt.DataUpMix.log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL
import java.nio.charset.StandardCharsets

object GetAdminNetData {
    private val adminUrl = if (BuildConfig.DEBUG) {
        "https://svdo.foreststable.com/apitest/asdef/"
    } else {
        "https://svdo.foreststable.com/api/asdef/"
    }

    @SuppressLint("HardwareIds")
    fun adminData(): String {
        return JSONObject().apply {
            put("XXI", "com.forest.stable.game.video.fast.easy")
            put("YFq", DataUser.postUUID)
            put("gRCk", DataUser.refData)
            put("MeLHvOXBji", DataUpMix.getAppVersion())
        }.toString()
    }

    suspend fun getAdminData() = withContext(Dispatchers.IO) {
        val params = adminData()
        log("getAdminData: -params-$params")
        val maxRetries = 2
        var attempt = 0
        val timeStart = System.currentTimeMillis()
        val appTimeEnd = ((System.currentTimeMillis() - ZZZ.startAppTime) / 1000).toInt()
        DataUpMix.postPointData("u_admin_ask", "time", appTimeEnd)
        while (attempt <= maxRetries) {
            try {
                val response = postAdminDataWithRetry(
                    adminUrl,
                    params
                )
                val timeEnd = ((System.currentTimeMillis() - timeStart) / 1000).toInt()
                DataUpMix.postPointData("u_admin_result", "time", timeEnd)
                DataUser.bbb_admin = getAdminBlackData(response)
                DataUser.rrr_admin = getAdminRefData(response)
                postV24proxyData()
                log(
                    "getAdminData-Success: $response---${DataUser.bbb_admin}----${DataUser.rrr_admin}"
                )
                break

            } catch (e: Exception) {
                log("getAdminData-Exception: $e")
                if (attempt >= maxRetries) {
                    log("getAdminData-FinalFailure: Max retries reached.")
                }
                attempt++ // 请求失败，进行下一次尝试
            }
        }
    }

    private suspend fun postAdminDataWithRetry(url: String, params: Any): String =
        withContext(Dispatchers.IO) {
            repeat(3) { attempt ->
                val result = postAdminData(url, params)
                if (result.isSuccess) {
                    return@withContext result.getOrThrow()
                } else if (attempt == 2) {
                    throw result.exceptionOrNull() ?: Exception("Unknown error")
                }
            }
            throw Exception("Retry attempts exhausted")
        }

    private fun postAdminData(url: String, body: Any): Result<String> {
        return try {
            val jsonBodyString = JSONObject(body.toString()).toString()
            val datetime = System.currentTimeMillis().toString()
            val xorEncryptedString = xorWithTimestamp(jsonBodyString, datetime)
            val base64EncodedString = Base64.encodeToString(
                xorEncryptedString.toByteArray(StandardCharsets.UTF_8),
                Base64.NO_WRAP
            )

            val urlConnection = (URL(url).openConnection() as HttpURLConnection).apply {
                requestMethod = "POST"
                setRequestProperty("Content-Type", "application/json")
                setRequestProperty("datetime", datetime)
                doOutput = true
                outputStream.use { it.write(base64EncodedString.toByteArray()) }
            }

            val responseCode = urlConnection.responseCode
            if (responseCode in 200..299) {
                val timestampResponse = urlConnection.headerFields["datetime"]?.first()
                    ?: throw IllegalArgumentException("Timestamp missing in headers")
                val responseBody = urlConnection.inputStream.bufferedReader().use { it.readText() }
                val decodedBytes = Base64.decode(responseBody, Base64.DEFAULT)
                val decodedString = String(decodedBytes, Charsets.UTF_8)
                val finalData = xorWithTimestamp(decodedString, timestampResponse)
                Result.success(finalData)
            } else {
                val errorBody = urlConnection.errorStream?.bufferedReader()?.use { it.readText() }
                Result.failure(Exception("HTTP error $responseCode: $errorBody"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }


    private fun xorWithTimestamp(text: String, datetime: String): String {
        val cycleKey = datetime.toCharArray()
        val keyLength = cycleKey.size
        return text.mapIndexed { index, char ->
            char.toInt().xor(cycleKey[index % keyLength].toInt()).toChar()
        }.joinToString("")
    }

    private fun getAdminBlackData(jsonString: String): String {
        val outerJson = JSONObject(jsonString)
        val confString = outerJson.getJSONObject("wfIK").getString("conf")
        val confJson = JSONObject(confString)
        return confJson.getString("forest_ast")
    }

    private fun getAdminRefData(jsonString: String): String {
        val outerJson = JSONObject(jsonString)
        val confString = outerJson.getJSONObject("wfIK").getString("conf")
        val confJson = JSONObject(confString)
        return confJson.getString("forest_hxm")
    }

    private fun postV24proxyData() {
        if (DataUser.rrr_admin == "1") {
            DataUpMix.postPointData("u_buying")
        }
    }

}