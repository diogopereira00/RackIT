package com.diogopereira.rackit

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.diogopereira.rackit.v2.R
import com.diogopereira.rackit.v2.databinding.ActivityAddProductBinding
import com.diogopereira.rackit.v2.databinding.ActivityInfoProdutoBinding

class InfoProdutoActivity : AppCompatActivity() {
    private lateinit var binding: ActivityInfoProdutoBinding
    private var gv = GlobalClass()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInfoProdutoBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        gv = application as GlobalClass
        var nomeProduto = gv.currentProduto.nomeProduto

        if (gv.currentProduto.imagemProduto != "") {
            Glide.with(this).load(gv.currentProduto.imagemProduto)
                .into(binding.imageView3)
        } else {
            Glide.with(this).load(R.drawable.ic_camera)
                .into(binding.imageView3)

        }
        binding.nomeProdutoEditText.setText(nomeProduto)
        if (gv.currentProduto.codBarras.isNullOrEmpty()) {
            binding.codBarrasEditText.setText("Introduza agora")
        } else
            binding.codBarrasEditText.setText(gv.currentProduto.codBarras)

    }
}