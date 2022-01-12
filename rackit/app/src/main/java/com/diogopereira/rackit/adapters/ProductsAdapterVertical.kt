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
import com.diogopereira.rackit.classes.Produto
import com.diogopereira.rackit.v2.R
import com.diogopereira.rackit.v2.databinding.ItemListaProdutosBinding
import com.diogopereira.rackit.v2.databinding.RecyclerItemBinding

class ProductsAdapterVertical  : RecyclerView.Adapter<ProductsAdapterVertical.HolderProdutoVertical> {

    // context, get using construtor
    private var context: Context
    private var gv = GlobalClass()

    private var shopListArray: ArrayList<Produto>

    //viewbinding RowReviewsBinding.xml
    private lateinit var binding: ItemListaProdutosBinding

    // construtor
    constructor(context: Context, list: ArrayList<Produto>) {
        this.context = context
        this.shopListArray = list
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HolderProdutoVertical {
        binding = ItemListaProdutosBinding.inflate(LayoutInflater.from(context), parent, false)
        gv = parent.context.applicationContext as GlobalClass

        return HolderProdutoVertical(binding.root)
    }

    override fun onBindViewHolder(holder: HolderProdutoVertical, position: Int) {
        val currentItem = shopListArray[position]
        // TODO: 12/01/2022 verificar se chega a lista de produtos

        for (teste in currentItem.listaInfoProduto) {
            holder.nomeProduto.text =
                currentItem.listaInfoProduto.size.toString() + "x " + currentItem.nomeProduto
        }

        if (!currentItem.imagemProduto.isNullOrEmpty()) {
            Glide.with(holder.itemView.context).load(currentItem.imagemProduto)
                .into(holder.imagemProduto)
        } else {
            Glide.with(holder.itemView.context).load(R.drawable.ic_camera)
                .into(holder.imagemProduto)


        }
        holder.itemView.setOnClickListener {
            gv.currentProduto = currentItem
            Toast.makeText(context, "${gv.currentProduto.nomeProduto}", Toast.LENGTH_SHORT).show()
            val intent = Intent(context, InfoProdutoActivity::class.java)
            context.startActivity(intent)
        }
//        holder.nomeProduto.text = currentItem.nomeProduto


    }


    override fun getItemCount(): Int {
        return shopListArray.size
    }


    inner class HolderProdutoVertical(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nomeProduto = binding.nomeProduto
        val imagemProduto = binding.iconeNaLista
        val dataValidade = binding.dataValidade
    }
}
