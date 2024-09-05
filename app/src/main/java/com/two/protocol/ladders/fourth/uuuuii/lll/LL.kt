package com.two.protocol.ladders.fourth.uuuuii.lll

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.content.Intent
import android.os.Bundle
import android.view.animation.LinearInterpolator
import androidx.activity.addCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.two.protocol.ladders.fourth.databinding.ActivityFirstBinding
import com.two.protocol.ladders.fourth.databinding.ActivityListBinding
import com.two.protocol.ladders.fourth.uuutt.DataUser
import com.two.protocol.ladders.fourth.uuutt.VPNGet
import com.two.protocol.ladders.fourth.uuutt.VpnServerBean
import com.two.protocol.ladders.fourth.uuuuii.zzzzzz.ZZ
class LL:AppCompatActivity() {
    private val binding by lazy { ActivityListBinding.inflate(layoutInflater) }
    private var vpnServerBeanList:MutableList<VpnServerBean>?=null
    private var Lap: LAP?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        initAdapter()
        clickFun()
    }

    private fun initAdapter(){
        vpnServerBeanList =  VPNGet.getAllData()
        binding.rvService.layoutManager = LinearLayoutManager(this)
        Lap = vpnServerBeanList?.let { LAP(it,this) }
        binding.rvService.adapter = Lap
    }
    private fun clickFun() {
        onBackPressedDispatcher.addCallback(this) {
            finish()
        }
        binding.mainMenu.setOnClickListener {
            finish()
        }
    }
}