package com.diogopereira.rackit

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import com.diogopereira.rackit.v2.R
import com.diogopereira.rackit.v2.databinding.ActivityAddProductBinding
import com.diogopereira.rackit.v2.databinding.ActivityMainBinding

class AddProductActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddProductBinding
    private lateinit var backButton : ImageButton
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddProductBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        backButton = binding.backButton

        backButton.setOnClickListener{
            startActivity(Intent(this@AddProductActivity, MainActivity::class.java))
            finish()
        }
    }

    // TODO: 26/12/2021 Adicionar Produto a base de Dados com ligação a Lista automaticamente <- Urgente 

}