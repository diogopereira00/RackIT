package com.diogopereira.rackit.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.diogopereira.rackit.GlobalClass
import com.diogopereira.rackit.InfoProdutoActivity
import com.diogopereira.rackit.classes.InfoProduto
import com.diogopereira.rackit.classes.Produto
import com.diogopereira.rackit.classes.ProdutoExpirar
import com.diogopereira.rackit.v2.R
import com.diogopereira.rackit.v2.databinding.RecyclerItemBinding
import java.util.*
import kotlin.collections.ArrayList

class ProductsAdapter : RecyclerView.Adapter<ProductsAdapter.HolderProduto> {

    // context, get using construtor
    private var context: Context
    private var gv = GlobalClass()

    private var shopListArray: ArrayList<ProdutoExpirar>

    //viewbinding RowReviewsBinding.xml
    private lateinit var binding: RecyclerItemBinding

    // construtor
    constructor(context: Context, list: ArrayList<ProdutoExpirar>) {
        this.context = context
        this.shopListArray = list
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HolderProduto {
        binding = RecyclerItemBinding.inflate(LayoutInflater.from(context), parent, false)
        gv = parent.context.applicationContext as GlobalClass

        return HolderProduto(binding.root)
    }

    override fun onBindViewHolder(holder: HolderProduto, position: Int) {
        val currentItem = shopListArray[position]
        currentItem.listaInfoProduto.sortWith(compareBy<InfoProduto,Date?>(nullsLast(), { it.dataValidadeAux }))

        for (teste in currentItem.listaInfoProduto) {
            holder.nomeProduto.text = currentItem.nomeProduto
            if(currentItem.dataValidade == null){
                holder.dataValidade.text = "Sem data de validade"

            }
            else{
                holder.dataValidade.text = "Expira em: " + currentItem.dataValidade.toString()

            }
        }

        if (!currentItem.imagemProduto.isNullOrEmpty()) {
            Glide.with(holder.itemView.context).load(currentItem.imagemProduto)
                .into(holder.imagemProduto)
        } else {
            Glide.with(holder.itemView.context).load(R.drawable.ic_camera)
                .into(holder.imagemProduto)


        }

        holder.itemView.setOnClickListener {
            gv.currentProduto = Produto(
                nomeProduto = currentItem.nomeProduto,
                produtoID = currentItem.produtoID,
                listaInfoProduto = currentItem.listaInfoProduto,
                listaDe = currentItem.listaDe,
                adicionadoEm = currentItem.adicionadoEm,
                codBarras = currentItem.codBarras,
                imagemProduto = currentItem.imagemProduto
            )
            Toast.makeText(context, "${gv.currentProduto.nomeProduto}", Toast.LENGTH_SHORT).show()
            val intent = Intent(context, InfoProdutoActivity::class.java)
            context.startActivity(intent)
        }
//        holder.nomeProduto.text = currentItem.nomeProduto

    }


    override fun getItemCount(): Int {
        return shopListArray.size
    }


    inner class HolderProduto(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nomeProduto = binding.nomeProduto
        val imagemProduto = binding.image
        val dataValidade = binding.dataValidade
    }
}
