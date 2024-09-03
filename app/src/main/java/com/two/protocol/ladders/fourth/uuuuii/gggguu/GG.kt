package com.two.protocol.ladders.fourth.uuuuii.gggguu

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.content.Intent
import android.os.Bundle
import android.view.animation.LinearInterpolator
import androidx.appcompat.app.AppCompatActivity
import com.two.protocol.ladders.fourth.databinding.ActivityFirstBinding
import com.two.protocol.ladders.fourth.uuuuii.zzzzzz.ZZ
class GG:AppCompatActivity() {
    private val binding by lazy { ActivityFirstBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        initTime()
    }

    private fun initTime(){
        startCountdown()
    }
    private fun startCountdown() {
        val animator = ValueAnimator.ofInt(0, 100)
        animator.duration = 2000 // 2秒钟
        animator.interpolator = LinearInterpolator()
        animator.addUpdateListener { animation ->
            val progress = animation.animatedValue as Int
            binding.progressBarFirst.progress = progress
        }
        animator.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                startActivity(Intent(this@GG, ZZ::class.java))
                finish()
            }
        })
        animator.start()
    }

}