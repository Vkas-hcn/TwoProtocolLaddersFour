package com.two.protocol.ladders.fourth.uuutt

import androidx.annotation.Keep

@Keep
data class VpnDataBean(
    val code: Int,
    val data: Data,
    val msg: String
)
@Keep
data class Data(
    val server_list: List<VpnServerBean>,
    val smart_list: List<VpnServerBean>
)
@Keep
data class VpnServerBean(
    var city: String,
    var country_name: String,
    var ip: String,
    var mode: String,
    var port: Int,
    var password:String,
    var bestState:Boolean = false,
    var checkState:Boolean = false,
)