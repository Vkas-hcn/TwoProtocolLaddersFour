package com.two.protocol.ladders.fourth.uuuuii.eeedd

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.content.Intent
import android.os.Bundle
import android.view.animation.LinearInterpolator
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.two.protocol.ladders.fourth.R
import com.two.protocol.ladders.fourth.aaaaa.ZZZ
import com.two.protocol.ladders.fourth.databinding.ActivityFirstBinding
import com.two.protocol.ladders.fourth.databinding.ActivityGoBinding
import com.two.protocol.ladders.fourth.databinding.ActivityListBinding
import com.two.protocol.ladders.fourth.uuutt.DataUser
import com.two.protocol.ladders.fourth.uuutt.DataUser.getSaturnImage
import com.two.protocol.ladders.fourth.uuutt.GlobalTimer
import com.two.protocol.ladders.fourth.uuutt.VpnServerBean
import com.two.protocol.ladders.fourth.uuuuii.zzzzzz.ZZ

class EE : AppCompatActivity() {
    private val binding by lazy { ActivityGoBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        initState()
        binding.mainMenu.setOnClickListener {
            finish()
        }
    }

    private fun initState() {
        if (ZZZ.saoState) {
            binding.tvState.text = "Connection successful!"
            binding.imgState.setImageResource(R.drawable.icon_connected)
            GlobalTimer.onTimeUpdate = { formattedTime ->
                binding.tvTime.text = formattedTime
            }
        } else {
            binding.tvState.text = "DisConnection successful!"
            binding.imgState.setImageResource(R.drawable.icon_disconnect)
            binding.tvTime.text = DataUser.timeEdKey
        }
        val vpnBean =
            Gson().fromJson(DataUser.nowServiceKey, VpnServerBean::class.java)
        binding.imgFlag.setImageResource(vpnBean.country_name.getSaturnImage())
        binding.tvName.text = vpnBean.country_name
        if (DataUser.chooneServiceKey?.isNotBlank() == true) {
            DataUser.nowServiceKey = DataUser.chooneServiceKey
            DataUser.chooneServiceKey = ""
        }
    }

}