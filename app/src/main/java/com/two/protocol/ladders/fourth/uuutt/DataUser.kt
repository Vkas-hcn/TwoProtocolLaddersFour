package com.two.protocol.ladders.fourth.uuutt
import android.content.ContentValues
import android.net.Uri
import android.util.Log
import com.two.protocol.ladders.fourth.R
import com.two.protocol.ladders.fourth.aaaaa.ZZZ

object DataUser {
    const val ppUrl = "https://www.wanandroid.com/"
    private const val upValue = "upValue"
    private const val downValue = "downValue"
    private const val protocolValue = "protocolValue"
    private const val bestService = "bestService"
    private const val nowService = "nowService"
    private const val chooneService = "chooneService"
    private const val isCloneGuide = "isCloneGuide"
    private const val codeCon = "codeCon"
    private const val timeEd = "timeEd"
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
    fun String.getSaturnImage():  Int {
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
}
