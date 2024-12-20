package com.two.protocol.ladders.fourth.uuutt

import android.content.ContentValues
import android.graphics.Outline
import android.net.Uri
import android.util.Log
import android.view.View
import android.view.ViewOutlineProvider
import com.google.gson.Gson
import com.two.protocol.ladders.fourth.BuildConfig
import com.two.protocol.ladders.fourth.R
import com.two.protocol.ladders.fourth.aaaaa.ZZZ
import com.two.protocol.ladders.fourth.ggggg.ForestAdBean
import com.two.protocol.ladders.fourth.ggggg.ForestLogicBean

object DataUser {
    const val TAG = "TWO"
    const val ppUrl = "https://www.wanandroid.com/"
    val tba_url = if (BuildConfig.DEBUG) {
        "https://test-hardware.foreststable.com/overt/fleck/israel"
    } else {
        "https://hardware.foreststable.com/donald/devious"
    }
    private const val upValue = "upValue"
    private const val downValue = "downValue"
    private const val protocolValue = "protocolValue"
    private const val bestService = "bestService"
    private const val nowService = "nowService"
    private const val chooneService = "chooneService"
    private const val isCloneGuide = "isCloneGuide"
    private const val codeCon = "codeCon"
    private const val localIpKey = "localIpKey"
    private const val timeEd = "timeEd"
    private const val blockDataKey = "blockDataKey"
    private const val postUUIDKey = "postUUIDKey"
    private const val cmpStateKey = "cmpStateKey"

    const val oooAdKey = "frst_net"
    const val oooLjKey = "ool"
    const val oooTzKey = "hiw"

    var connectIp = ""
    var connectCity = ""
    var openTypeIp = ""
    var homeTypeIp = ""
    var endTypeIp = ""
    var contTypeIp = ""
    var backEndTypeIp = ""
    var backListTypeIp = ""

    var cmpState: String
        get() = queryData(cmpStateKey) ?: "0"
        set(value) {
            value.let {
                insertData(cmpStateKey, it)
            }
        }
    var postUUID: String
        get() = queryData(postUUIDKey) ?: ""
        set(value) {
            value.let {
                insertData(postUUIDKey, it)
            }
        }
    var blockData: String?
        get() = queryData(blockDataKey)
        set(value) {
            value?.let {
                insertData(blockDataKey, it)
            }
        }
    var ooo_ad: String?
        get() = queryData(oooAdKey)
        set(value) {
            value?.let {
                insertData(oooAdKey, it)
            }
        }
    var ooo_lj: String?
        get() = queryData(oooLjKey)
        set(value) {
            value?.let {
                insertData(oooLjKey, it)
            }
        }

    var ooo_tz: String?
        get() = queryData(oooTzKey)
        set(value) {
            value?.let {
                insertData(oooTzKey, it)
            }
        }
    var timeEdKey: String?
        get() = queryData(timeEd)
        set(value) {
            value?.let {
                insertData(timeEd, it)
            }
        }
    var codeConKey: String?
        get() = queryData(codeCon)
        set(value) {
            value?.let {
                insertData(codeCon, it)
            }
        }

    var localIp: String?
        get() = queryData(localIpKey)
        set(value) {
            value?.let {
                insertData(localIpKey, it)
            }
        }
    var isCloneGuideKey: String?
        get() = queryData(isCloneGuide)
        set(value) {
            value?.let {
                insertData(isCloneGuide, it)
            }
        }
    var nowServiceKey: String?
        get() = queryData(nowService)
        set(value) {
            value?.let {
                insertData(nowService, it)
            }
        }
    var chooneServiceKey: String?
        get() = queryData(chooneService)
        set(value) {
            value?.let {
                insertData(chooneService, it)
            }
        }
    var bestServiceKey: String?
        get() = queryData(bestService)
        set(value) {
            value?.let {
                insertData(bestService, it)
            }
        }
    var protocolValueKey: String?
        get() = queryData(protocolValue)
        set(value) {
            insertData(protocolValue, value.toString())
        }
    var upValueKey: String?
        get() = queryData(upValue)
        set(value) {
            value?.let {
                insertData(upValue, it)
            }
        }

    var downValueKey: String?
        get() = queryData(downValue)
        set(value) {
            value?.let {
                insertData(downValue, it)
            }
        }

    var installUpState: String?
        get() = queryData("install_up_state")
        set(value) {
            value?.let {
                insertData("install_up_state", it)
            }
        }

    var ajState: String?
        get() = queryData("ajState")
        set(value) {
            value?.let {
                insertData("ajState", it)
            }
        }

    var refData: String?
        get() = queryData("refData")
        set(value) {
            value?.let {
                insertData("refData", it)
            }
        }

    var bbb_admin: String?
        get() = queryData("bbb_admin")
        set(value) {
            value?.let {
                insertData("bbb_admin", it)
            }
        }
    var rrr_admin: String?
        get() = queryData("rrr_admin")
        set(value) {
            value?.let {
                insertData("rrr_admin", it)
            }
        }

    var bus: String?
        get() = queryData("bus")
        set(value) {
            value?.let {
                insertData("bus", it)
            }
        }

    var local_s_n :Int?
        get() = queryData("local_s_n")?.toInt()
        set(value) {
            value?.let {
                insertData("local_s_n", it.toString())
            }
        }

    var local_c_n :Int?
        get() = queryData("local_c_n")?.toInt()
        set(value) {
            value?.let {
                insertData("local_c_n", it.toString())
            }
        }

    var ad_load_date: String?
        get() = queryData("ad_load_date")
        set(value) {
            value?.let {
                insertData("ad_load_date", it)
            }
        }

    var adLimState: String?
        get() = queryData("adLimState")
        set(value) {
            value?.let {
                insertData("adLimState", it)
            }
        }

    private fun insertData(key: String, value: String) {
        val values = ContentValues().apply {
            put("key_name", key)
            put("value", value)
        }
        val rowsUpdated = ZZZ.appContext.contentResolver.update(
            MyContentProvider.CONTENT_URI,
            values,
            "key_name=?",
            arrayOf(key)
        )

        if (rowsUpdated == 0) {
            ZZZ.appContext.contentResolver.insert(MyContentProvider.CONTENT_URI, values)?.let {
                Log.e("TAG", "Data insertion successful，URI：$it")
            }
        } else {
            Log.e("TAG", "Data updated successfully")
        }
    }

    private fun queryData(key: String): String? {
        val cursor = ZZZ.appContext.contentResolver.query(
            MyContentProvider.CONTENT_URI,
            arrayOf("value"),
            "key_name=?",
            arrayOf(key),
            null
        )
        cursor?.use {
            if (it.moveToFirst()) {
                return it.getString(it.getColumnIndexOrThrow("value"))
            }
        }
        return null
    }

    fun String.getSaturnImage(): Int {
        return when (this) {
            "United States" -> R.drawable.unitedstates
            "Australia" -> R.drawable.australia
            "Canada" -> R.drawable.canada
            "China" -> R.drawable.canada
            "France" -> R.drawable.france
            "Germany" -> R.drawable.de
            "Hong Kong" -> R.drawable.hongkong
            "India" -> R.drawable.india
            "Japan" -> R.drawable.japan
            "koreasouth" -> R.drawable.korea
            "Singapore" -> R.drawable.singapore
            "Brazil" -> R.drawable.brazil
            "United Kingdom" -> R.drawable.gb
            "Netherlands" -> R.drawable.netherlands
            else -> R.drawable.fast
        }
    }

    fun fromLogicJson(json: String): ForestLogicBean {
        val gson = Gson()
        return gson.fromJson(json, ForestLogicBean::class.java)
    }

    fun fromAdJson(json: String): ForestAdBean {
        val gson = Gson()
        return gson.fromJson(json, ForestAdBean::class.java)
    }

    fun getLogicJson(): ForestLogicBean {
        val dataJson =
            if (ooo_lj.isNullOrEmpty()) {
                local_ad_logic
            } else {
                ooo_lj
            }
        return runCatching {
            fromLogicJson(dataJson!!)
        }.getOrNull() ?: fromLogicJson(local_ad_logic)
    }

    fun getAdJson(): ForestAdBean {
        val dataJson =
            if (ooo_ad.isNullOrEmpty()) {
                getIsRelease()
            } else {
                ooo_ad
            }
        return runCatching {
            fromAdJson(dataJson!!)
        }.getOrNull() ?: fromAdJson(getIsRelease())
    }

    private fun getIsRelease(): String {
        return if (BuildConfig.DEBUG) {
            local_ad_data
        } else {
            local_ad_data_formal
        }
    }

fun blockAdBlacklist(): Boolean {
    val logicJson = getLogicJson() ?: return true
    val pkk = logicJson.pkk ?: return true

    val res = when (pkk) {
        "1" -> blockData != "booty"
        "2" -> false
        else -> true
    }

    if (bus != "OK" && !res) {
        DataUpMix.postPointData("u_whitelist")
        bus = "OK"
    }

    return res
}


    fun parseTwoNumbers(callback: (first: Int, second: Int) -> Unit) {
        val default = 10
        val num = getLogicJson().eed ?: ""
        val parts = num.split("*")
        val firstNumber = parts.getOrNull(0)?.toIntOrNull() ?: default
        val secondNumber = parts.getOrNull(1)?.toIntOrNull() ?: default
        callback(firstNumber, secondNumber)
    }

    fun log(msg: String) {
        if (BuildConfig.DEBUG) {
            Log.e("TAG", msg)
        }
    }

    class NavigationViewOutlineProvider : ViewOutlineProvider() {
        override fun getOutline(view: View?, outline: Outline?) {
            val sView = view ?: return
            val sOutline = outline ?: return
            sOutline.setRoundRect(
                0,
                0,
                sView.width,
                sView.height,
                8f
            )
        }
    }
    fun getAdmobIdList(adList:String):Array<String>{
       return adList.split(",").toTypedArray()
    }

    // 本地广告数据
    const val local_ad_data = """
{
  "ffftt_skhjg":40,
  "ffftt_csadf":2,
  "open":"ca-app-pub-3940256099942544/9257395921,ca-app-pub-3940256099942544/9257395921x",
  "mnnt":"ca-app-pub-3940256099942544/2247696110,ca-app-pub-3940256099942544/2247696111",
  "rsnt":"ca-app-pub-3940256099942544/2247696110,ca-app-pub-3940256099942544/2247696111",
  "ctint":"ca-app-pub-3940256099942544/1033173712,ca-app-pub-3940256099942544/1033173713",
  "bcintserv":"ca-app-pub-3940256099942544/1033173712,ca-app-pub-3940256099942544/1033173713",
  "bcintres":"ca-app-pub-3940256099942544/1033173712,ca-app-pub-3940256099942544/1033173713"
}
    """

    // 本地正式广告数据
    const val local_ad_data_formal = """
{
  "ffftt_skhjg":40,
  "ffftt_csadf":2,
  "open": "ca-app-pub-8116342442786555/9926869240",
  "mnnt": "ca-app-pub-8116342442786555/9202813074",
  "rsnt": "ca-app-pub-8116342442786555/4762172475",
  "ctint": "ca-app-pub-8116342442786555/4068900307",
  "bcintres": "",
  "bcintserv": "ca-app-pub-8116342442786555/1442736969"
}
    """

    //本地广告逻辑
    const val local_ad_logic = """
{
    "dda":"",
    "pkk": "1",
    "eed": "10*10"
}    """
}
