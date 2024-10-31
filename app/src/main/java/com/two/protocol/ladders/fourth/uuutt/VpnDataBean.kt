package com.two.protocol.ladders.fourth.uuutt

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class VpnDataBean(
    @SerializedName("orm")
    val smart_list: List<VpnServerBean>,
    @SerializedName("rrl")
    val server_list: List<VpnServerBean>
)

@Keep
data class VpnServerBean(
    @SerializedName("paa")
    var mode: String,

    @SerializedName("ceo")
    var city: String,

    @SerializedName("ttc")
    var country_name: String,

    @SerializedName("woo")
    var ip: String,

    @SerializedName("edw")
    var port: Int,

    @SerializedName("sne")
    var password: String,

    var bestState: Boolean = false,
    var checkState: Boolean = false,
)