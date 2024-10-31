package com.two.protocol.ladders.fourth.uuuuii.lll

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.animation.LinearInterpolator
import androidx.activity.addCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.two.protocol.ladders.fourth.databinding.ActivityFirstBinding
import com.two.protocol.ladders.fourth.databinding.ActivityListBinding
import com.two.protocol.ladders.fourth.ggggg.BaseAd
import com.two.protocol.ladders.fourth.uuutt.DataUser
import com.two.protocol.ladders.fourth.uuutt.VPNGet
import com.two.protocol.ladders.fourth.uuutt.VpnServerBean
import com.two.protocol.ladders.fourth.uuuuii.zzzzzz.ZZ
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class LL : AppCompatActivity() {
    private val binding by lazy { ActivityListBinding.inflate(layoutInflater) }
    private var vpnServerBeanList: MutableList<VpnServerBean>? = null
    private var Lap: LAP? = null
    private var backJob: Job? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        initAdapter()
        clickFun()
    }

    private fun initAdapter() {
        vpnServerBeanList = VPNGet.getAllData()
        binding.rvService.layoutManager = LinearLayoutManager(this)
        Lap = vpnServerBeanList?.let { LAP(it, this) }
        binding.rvService.adapter = Lap
    }

    private fun clickFun() {
        onBackPressedDispatcher.addCallback(this) {
            showBackAd {
                backJob?.cancel()
                backJob = null
                binding.showLoad = false
                finish()
            }
        }
        binding.mainMenu.setOnClickListener {
            showBackAd {
                backJob?.cancel()
                backJob = null
                binding.showLoad = false
                finish()
            }
        }
        val data = Intent().apply {
            // Add any data you want to return
            putExtra("list", "list")
        }
        setResult(Activity.RESULT_OK, data)
    }

    private fun showBackAd(nextFun: () -> Unit) {
        backJob?.cancel()
        backJob = null
        val baseAd = BaseAd.getBackListInstance()
        backJob = lifecycleScope.launch(Dispatchers.Main) {
            if (baseAd.canShowAd(this@LL, baseAd) == 0) {
                nextFun()
                return@launch
            }
            if (baseAd.appAdDataFlash == null) {
                baseAd.advertisementLoadingFlash(this@LL)
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
                    if (elapsedTime >= 1000L && baseAd.canShowAd(this@LL, baseAd) == 2) {
                        backJob?.cancel()
                        backJob = null
                        binding.showLoad = false
                        baseAd.playIntAdvertisementFlash(
                            this@LL,
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

}