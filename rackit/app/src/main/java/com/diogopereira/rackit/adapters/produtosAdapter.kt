package com.diogopereira.rackit.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.diogopereira.rackit.classes.Produto
import com.diogopereira.rackit.v2.R
import com.diogopereira.rackit.v2.databinding.ItemListaProdutosBinding

class produtosAdapter(private val productsList : ArrayList<Produto>) : RecyclerView.Adapter<produtosAdapter.ProductsViewHolder>() {

    private lateinit var binding : ItemListaProdutosBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductsViewHolder {
        binding = ItemListaProdutosBinding.inflate(LayoutInflater.from(parent.context),parent,false)

        //val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_lista_produtos, parent,false)
        return ProductsViewHolder(binding.root)
    }

    override fun onBindViewHolder(holder: ProductsViewHolder, position: Int) {
        val currentItem =productsList[position]
        holder.nomeProduto.text = currentItem.nomeProduto

    }

    override fun getItemCount(): Int {
        return productsList.size

    }

    inner class ProductsViewHolder(productView : View) : RecyclerView.ViewHolder(productView){

        val nomeProduto :TextView = binding.nomeProduto
        
    }
}