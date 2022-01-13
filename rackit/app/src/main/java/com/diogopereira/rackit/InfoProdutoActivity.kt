package com.diogopereira.rackit

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.diogopereira.rackit.adapters.InfoProdutosAdapter
import com.diogopereira.rackit.classes.InfoProduto
import com.diogopereira.rackit.dialogs.AdicionarInfoProduto
import com.diogopereira.rackit.dialogs.AdicionarListaCompras
import com.diogopereira.rackit.dialogs.AtualizarProduto
import com.diogopereira.rackit.dialogs.DeleteProduto
import com.diogopereira.rackit.v2.R
import com.diogopereira.rackit.v2.databinding.ActivityInfoProdutoBinding

class InfoProdutoActivity : AppCompatActivity() {
    private lateinit var binding: ActivityInfoProdutoBinding
    private var gv = GlobalClass()

    private lateinit var infoRecyclerView: RecyclerView

    private lateinit var infoList: ArrayList<InfoProduto>
    private lateinit var infoAdapter: InfoProdutosAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInfoProdutoBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        gv = application as GlobalClass
        var nomeProduto = gv.currentProduto.nomeProduto
        var codBarras = gv.currentProduto.codBarras

        if (gv.currentProduto.imagemProduto != "") {
            Glide.with(this).load(gv.currentProduto.imagemProduto)
                .into(binding.imageView3)
        } else {
            Glide.with(this).load(R.drawable.ic_camera)
                .into(binding.imageView3)

        }
        binding.nomeProdutoEditText.setText(nomeProduto)
        if (gv.currentProduto.codBarras.isNullOrEmpty()) {
            binding.codBarrasEditText.setHint("Introduza agora")
        } else
            binding.codBarrasEditText.setText(gv.currentProduto.codBarras)

        infoList = gv.currentProduto.listaInfoProduto
        infoAdapter = InfoProdutosAdapter(gv.currentProduto.listaInfoProduto, gv.currentProduto)

        infoRecyclerView = binding.recyclerInfos
        infoRecyclerView.layoutManager = LinearLayoutManager(this)
        infoRecyclerView.adapter = infoAdapter

        // botao apagar produto
        binding.APAGAR.setOnClickListener {

            var dialog = DeleteProduto(gv.currentProduto)
            val fm: FragmentManager = this.supportFragmentManager
            dialog.show(fm, "")


        }
        //botao atualizar produto
        binding.update.setOnClickListener {
            var dialog = AtualizarProduto(
                gv.currentProduto,
                binding.nomeProdutoEditText.text.toString(),
                binding.codBarrasEditText.text.toString(),
                infoAdapter
            )
            val fm: FragmentManager = this.supportFragmentManager
            dialog.show(fm, "")
        }
        //botao voltar atras
        binding.backButton.setOnClickListener {
            finish()
        }
        
        //botao adicionar infoproduto 
        binding.addButton.setOnClickListener{

            var dialog = AdicionarInfoProduto(gv.currentProduto,infoAdapter)
            val fm: FragmentManager = this.supportFragmentManager
            dialog.show(fm, "")
        }
        
        binding.shopButton.setOnClickListener{
            var dialog = AdicionarListaCompras(gv.currentProduto)
            val fm: FragmentManager = this.supportFragmentManager
            dialog.show(fm, "")
        }


    }
}