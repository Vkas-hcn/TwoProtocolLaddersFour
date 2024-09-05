package de.blinkt.openvpn

import android.app.Application
import android.content.ContentValues
import android.net.VpnService
import android.util.Log

object OpenContentProvider {
    lateinit var app: Application

    private const val upValue = "upValue"
    private const val downValue = "downValue"
    private fun insertData(key: String, value: String) {
        val values = ContentValues().apply {
            put("key_name", key)
            put("value", value)
        }
        val rowsUpdated = app.contentResolver.update(
            MyContentProvider.CONTENT_URI,
            values,
            "key_name=?",
            arrayOf(key)
        )
        if (rowsUpdated == 0) {
            app.contentResolver.insert(MyContentProvider.CONTENT_URI, values)?.let {
            }
        }
    }

    fun brand(builder: VpnService.Builder, myPackageName: String) {
//        if (getFlowData()) {
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
//            "com.android.chrome",
//        )
//    }

    fun getSpeedData(upData: String, downData: String, statistics: String) {
        insertData(downValue,downData)
        insertData(upValue, upData)
    }
}