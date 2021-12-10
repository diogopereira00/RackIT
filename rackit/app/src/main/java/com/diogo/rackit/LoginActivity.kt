package com.diogo.rackit

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.diogo.rackit.databinding.ActivityWelcomeBinding

class LoginActivity : AppCompatActivity() {
    lateinit var botaoComecar: Button
    private lateinit var binding: ActivityWelcomeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
    }
}