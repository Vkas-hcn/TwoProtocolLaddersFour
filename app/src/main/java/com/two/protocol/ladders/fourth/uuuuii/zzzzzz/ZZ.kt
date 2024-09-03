package com.two.protocol.ladders.fourth.uuuuii.zzzzzz

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.two.protocol.ladders.fourth.R
import com.two.protocol.ladders.fourth.databinding.ActivityMainBinding

class ZZ:AppCompatActivity() {
    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }
}