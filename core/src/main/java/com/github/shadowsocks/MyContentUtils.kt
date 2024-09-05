package com.github.shadowsocks

import android.content.ContentValues
import android.content.Context
import android.net.Uri
import android.net.VpnService
import android.text.format.Formatter
import android.util.Log
import com.github.shadowsocks.aidl.TrafficStats
import com.github.shadowsocks.bg.BaseService

object MyContentUtils {
    private const val upValue = "upValue"
    private const val downValue = "downValue"
    private fun insertData(key: String, value: String) {
        val values = ContentValues().apply {
            put("key_name", key)
            put("value", value)
        }
        val rowsUpdated = Core.app.contentResolver.update(
            MyContentProvider.CONTENT_URI,
            values,
            "key_name=?",
            arrayOf(key)
        )
        if (rowsUpdated == 0) {
            Core.app.contentResolver.insert(MyContentProvider.CONTENT_URI, values)?.let {
            }
        }
    }

    private fun queryData(key: String): String? {
        val cursor = Core.app.contentResolver.query(
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


    fun brand(builder: VpnService.Builder, myPackageName: String) {
        val data = queryData("nowService")
        Log.e("TAG", "brand: $data")
//        if(getFlowData()){
//            //黑名单绕流
//            (listOf(myPackageName) + listGmsPackages())
//                .iterator()
//                .forEachRemaining {
//                    runCatching { builder.addDisallowedApplication(it) }
//                }
//        }
    }

//    private fun listGmsPackages(): List<String> {
//        return listOf(
//            "com.google.android.gms",
//            "com.google.android.ext.services",
//            "com.google.process.gservices",
//            "com.android.vending",
//            "com.google.android.gms.persistent",
//            "com.google.android.cellbroadcastservice",
//            "com.google.android.packageinstaller",
//            "com.google.android.gms.location.history",
//        )
//    }


    fun getSpeedData(service: BaseService.Interface, stats: TrafficStats) {
        val data = (service as Context).getString(
            com.github.shadowsocks.core.R.string.traffic,
            (service as Context).getString(
                com.github.shadowsocks.core.R.string.speed,
                Formatter.formatFileSize((service as Context), stats.txRate)
            ),
            (service as Context).getString(
                com.github.shadowsocks.core.R.string.speed,
                Formatter.formatFileSize((service as Context), stats.rxRate)
            )
        )
        val pattern = """([\d.]+)\s*([^\s]+)\s*([↑↓])\s*([\d.]+)\s*([^\s]+)\s*([↑↓])""".toRegex()
        val matches = pattern.find(data)
        if (matches != null) {
            val (value1, unit1, arrow1, value2, unit2, arrow2) = matches.destructured
            insertData(downValue, "$value1 $unit1")
            insertData(upValue, "$value2 $unit2")
        }
    }
}