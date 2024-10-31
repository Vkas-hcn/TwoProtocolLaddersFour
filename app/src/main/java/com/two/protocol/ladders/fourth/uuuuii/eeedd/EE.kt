package com.two.protocol.ladders.fourth.uuuuii.eeedd

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.animation.LinearInterpolator
import androidx.activity.addCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import com.github.shadowsocks.Core.activity
import com.google.gson.Gson
import com.two.protocol.ladders.fourth.R
import com.two.protocol.ladders.fourth.aaaaa.ZZZ
import com.two.protocol.ladders.fourth.databinding.ActivityFirstBinding
import com.two.protocol.ladders.fourth.databinding.ActivityGoBinding
import com.two.protocol.ladders.fourth.databinding.ActivityListBinding
import com.two.protocol.ladders.fourth.ggggg.BaseAd
import com.two.protocol.ladders.fourth.uuutt.DataUser
import com.two.protocol.ladders.fourth.uuutt.DataUser.getSaturnImage
import com.two.protocol.ladders.fourth.uuutt.GlobalTimer
import com.two.protocol.ladders.fourth.uuutt.VPNGet
import com.two.protocol.ladders.fourth.uuutt.VpnServerBean
import com.two.protocol.ladders.fourth.uuuuii.zzzzzz.ZZ
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class EE : AppCompatActivity() {
    val binding by lazy { ActivityGoBinding.inflate(layoutInflater) }
    private var backJob: Job? = null
    private var endJob: Job? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        initState()
        showEndAd()
        binding.mainMenu.setOnClickListener {
            showBackAd {
                backJob?.cancel()
                backJob = null
                binding.showLoad = false
                finish()
            }
        }
        onBackPressedDispatcher.addCallback(this) {
            showBackAd {
                backJob?.cancel()
                backJob = null
                binding.showLoad = false
                finish()
            }
        }
        val data = Intent().apply {
            // Add any data you want to return
            putExtra("end", "end")
        }
        setResult(Activity.RESULT_OK, data)
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

    private fun showBackAd(nextFun: () -> Unit) {
        backJob?.cancel()
        backJob = null
        val baseAd = BaseAd.getBackEndInstance()
        backJob = lifecycleScope.launch(Dispatchers.Main) {
            if (baseAd.canShowAd(this@EE, baseAd) == 0) {
                nextFun()
                return@launch
            }
            if(baseAd.appAdDataFlash==null){
                baseAd.advertisementLoadingFlash(this@EE)
            }
            val startTime = System.currentTimeMillis()
            var elapsedTime: Long
            binding.showLoad = true
            try {
                while (isActive) {
                    elapsedTime = System.currentTimeMillis() - startTime
                    if (isActive && elapsedTime >= 5000L) {
                        nextFun()
                        break
                    }
                    if (elapsedTime >= 1000L && baseAd.canShowAd(this@EE, baseAd) == 2) {
                        baseAd.playIntAdvertisementFlash(
                            this@EE,
                            baseAd,
                            closeWindowFun = {
                                nextFun()
                            })
                    }
                    delay(500L)
                }
            } catch (e: Exception) {
                nextFun()
            }
        }
    }

    private fun showEndAd() {
        endJob?.cancel()
        endJob = null
        val baseAd = BaseAd.getEndInstance()
        if (DataUser.blockAdBlacklist()) {
            binding.adLayout.isVisible = false
            return
        }
        if (!VPNGet.isVPNConnected()) {
            binding.adLayoutAdmob.isVisible = false
            binding.imgOcAd.isVisible = true
            return
        }
        binding.adLayout.isVisible = true
        binding.imgOcAd.isVisible = true
        baseAd.advertisementLoadingFlash(this)
        if (baseAd.canShowAd(this@EE, baseAd) == 0) {
            endJob?.cancel()
            endJob = null
            return
        }
        endJob = lifecycleScope.launch {
            while (isActive) {
                delay(500L)
                if (baseAd.canShowAd(this@EE, baseAd) == 2) {
                    baseAd.playNativeAdvertisementFlash(this@EE,baseAd)
                    endJob?.cancel()
                    endJob = null
                    break
                }
            }
        }
    }
}