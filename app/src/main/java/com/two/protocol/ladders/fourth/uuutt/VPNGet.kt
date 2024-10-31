package com.two.protocol.ladders.fourth.uuutt

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.net.ConnectivityManager
import android.view.KeyEvent
import androidx.appcompat.app.AlertDialog
import com.google.gson.Gson
import com.two.protocol.ladders.fourth.R
import com.two.protocol.ladders.fourth.aaaaa.ZZZ
import com.two.protocol.ladders.fourth.uuuuii.zzzzzz.ZZ
import java.io.IOException

object VPNGet {
    private fun getJsonDataFromAsset(context: Context, fileName: String): String? {
        val jsonString: String
        try {
            jsonString = context.assets.open(fileName).bufferedReader().use { it.readText() }
        } catch (ioException: IOException) {
            ioException.printStackTrace()
            return null
        }
        return jsonString
    }

    fun getAllData(): MutableList<VpnServerBean>? {
        val allData = if (DataUser.ooo_tz.isNullOrBlank()) {
            getJsonDataFromAsset(ZZZ.appContext, "servicesJson.json")
        } else {
            DataUser.ooo_tz
        }
        val vpnAllListBean = try {
            Gson().fromJson(allData, VpnDataBean::class.java)
        } catch (e: Exception) {
            Gson().fromJson(
                getJsonDataFromAsset(ZZZ.appContext, "servicesJson.json"), VpnDataBean::class.java
            )
        }
        return try {
            vpnAllListBean?.server_list?.let { data ->
                if (data.isEmpty()) {
                    return null
                }
                getBestData()
                val vpnBeatBean =
                    Gson().fromJson(DataUser.bestServiceKey, VpnServerBean::class.java)
                val filteredServices = data.toMutableList()
                filteredServices.add(0, vpnBeatBean)
                return filteredServices
            } ?: run {
                null
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    private fun getBestData() {
        val allData = if (DataUser.ooo_tz.isNullOrBlank()) {
            getJsonDataFromAsset(ZZZ.appContext, "servicesJson.json")
        } else {
            DataUser.ooo_tz
        }
        val vpnAllListBean = Gson().fromJson(allData, VpnDataBean::class.java)
        if (vpnAllListBean != null && vpnAllListBean.smart_list.isNotEmpty()) {
            val VpnDataBean: VpnServerBean = vpnAllListBean.smart_list.random()
            VpnDataBean.bestState = true
            VpnDataBean.country_name = "Smart"
            DataUser.bestServiceKey = Gson().toJson(VpnDataBean)
        }
        try {
            val vpnNowBean =
                Gson().fromJson(DataUser.nowServiceKey, VpnServerBean::class.java)
            if (vpnNowBean.bestState) {
                DataUser.nowServiceKey = DataUser.bestServiceKey
            }
        } catch (_: Exception) {
            DataUser.nowServiceKey = DataUser.bestServiceKey
        }
    }

    fun isNetworkConnected(activity: ZZ): Boolean {
        val mConnectivityManager =
            activity.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val mNetworkInfo = mConnectivityManager.activeNetworkInfo
        if (mNetworkInfo != null) {
            return !mNetworkInfo.isAvailable
        }
        noInternetDialog(activity)
        return true
    }

    private fun noInternetDialog(activity: ZZ) {
        val alertDialogBuilder = AlertDialog.Builder(activity)
            .setTitle("Tip")
            .setMessage("No network available. Please check your connection.")
            .setIcon(R.mipmap.ic_launcher)
            .setPositiveButton("OK") { dialog: DialogInterface?, which: Int ->
                dialog?.dismiss()
            }

        val alertDialog = alertDialogBuilder.create()
        alertDialog.setCanceledOnTouchOutside(false)
        alertDialog.setCancelable(false)
        alertDialog.setOnKeyListener { _, keyCode, event ->
            return@setOnKeyListener keyCode == KeyEvent.KEYCODE_BACK && event.action == KeyEvent.ACTION_UP
        }
        alertDialog.show()
    }

    fun switchingDialog(activity: ZZ, nextFun: () -> Unit) {
        val alertDialogBuilder = AlertDialog.Builder(activity)
            .setTitle("Tip")
            .setMessage(
                "Switching the connection mode will\n" +
                        "disconnect the current connection\n" +
                        "whether to continue\n"
            )
            .setIcon(R.mipmap.ic_launcher)
            .setPositiveButton("OK") { dialog: DialogInterface?, which: Int ->
                dialog?.dismiss()
                nextFun()
            }
            .setNegativeButton("Cancel") { dialog: DialogInterface?, which: Int ->
                dialog?.dismiss()
            }

        val alertDialog = alertDialogBuilder.create()
        alertDialog.setCanceledOnTouchOutside(false)
        alertDialog.setCancelable(false)
        alertDialog.setOnKeyListener { _, keyCode, event ->
            return@setOnKeyListener keyCode == KeyEvent.KEYCODE_BACK && event.action == KeyEvent.ACTION_UP
        }
        alertDialog.show()
    }

    fun Context.shareText(text: String, subject: String = "") {
        val shareIntent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, text)
            putExtra(Intent.EXTRA_SUBJECT, subject)
            type = "text/plain"
        }
        startActivity(Intent.createChooser(shareIntent, "Share via"))
    }

    fun isVPNConnected(): Boolean {
        return ZZZ.saoState
    }
}