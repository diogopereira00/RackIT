package com.diogopereira.rackit.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.diogopereira.rackit.classes.Produto
import com.diogopereira.rackit.v2.R

class produtosAdapter(private val productsList : ArrayList<Produto>) : RecyclerView.Adapter<produtosAdapter.ProductsViewHolder>() {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductsViewHolder {

        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_lista_produtos,
        parent,false)
        return ProductsViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ProductsViewHolder, position: Int) {
        val currentItem =productsList[position]
        holder.nomeProduto.text = currentItem.nomeProduto

    }

    override fun getItemCount(): Int {
        return productsList.size

    }

    class ProductsViewHolder(productView : View) : RecyclerView.ViewHolder(productView){

        val nomeProduto :TextView = productView.findViewById(R.id.nomeProduto)
    }
}