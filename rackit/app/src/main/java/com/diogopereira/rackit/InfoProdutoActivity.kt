package com.diogopereira.rackit

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.diogopereira.rackit.adapters.InfoProdutosAdapter
import com.diogopereira.rackit.classes.InfoProduto
import com.diogopereira.rackit.dialogs.DeleteInfoProdutos
import com.diogopereira.rackit.v2.R
import com.diogopereira.rackit.v2.databinding.ActivityInfoProdutoBinding
import com.google.firebase.database.FirebaseDatabase

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


        binding.APAGAR.setOnClickListener {

            var dialog = DeleteInfoProdutos(gv.currentProduto)
            val fm: FragmentManager = this.supportFragmentManager
            dialog.show(fm, "")


        }



        binding.update.setOnClickListener {
            val hashMapProduto: HashMap<String, Any?> = HashMap()
            // TODO: 10/01/2022 Alterar imagem, similar ao addProductsActivity 

            hashMapProduto["nomeProduto"] = binding.nomeProdutoEditText.text.toString()
            gv.currentProduto.nomeProduto = binding.nomeProdutoEditText.text.toString()
            hashMapProduto["imagemProduto"] = gv.currentProduto.imagemProduto
            hashMapProduto["codBarras"] = binding.codBarrasEditText.text.toString()
            gv.currentProduto.codBarras = binding.codBarrasEditText.text.toString()

            hashMapProduto["listaDe"] = gv.currentList
            //hashMapProduto["imagemProduto"] = photoFile.toUri()
            hashMapProduto["adicionadoEm"] = gv.currentProduto.adicionadoEm
            hashMapProduto["produtoID"] = gv.currentProduto.produtoID
            hashMapProduto["imagemProduto"] = gv.currentProduto.imagemProduto
            val keyProduto = gv.currentProduto.produtoID
            val ref = FirebaseDatabase.getInstance().getReference("Produtos")
            ref.child(keyProduto!!).updateChildren(hashMapProduto)
                .addOnSuccessListener {
//                    Toast.makeText(
//                        this,
//                        "Produto atualizado $hashMapProduto...",
//                        Toast.LENGTH_SHORT
//                    ).show()
                    infoAdapter.notifyDataSetChanged()

                }
                .addOnFailureListener { e ->
                    Toast.makeText(this, "Erro...${e.message}", Toast.LENGTH_SHORT).show()
                }
        }
    }
}