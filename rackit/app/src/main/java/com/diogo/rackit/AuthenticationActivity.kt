package com.diogo.rackit

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.diogo.rackit.databinding.ActivityAuthenticationBinding.inflate
import com.diogo.rackit.databinding.ActivityAuthenticationBinding

class AuthenticationActivity : AppCompatActivity() {
    lateinit var botaoLogin: Button
    private lateinit var binding: ActivityAuthenticationBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        this.binding = ActivityAuthenticationBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        botaoLogin = binding.loginButton
        botaoLogin.setOnClickListener{
            executarOutraActivity(outraActivity = LoginActivity::class.java)

        }
    }

    private fun executarOutraActivity(outraActivity: Class<*>) {
        val x = Intent(this, outraActivity)
        startActivity(x)
    }
    }
