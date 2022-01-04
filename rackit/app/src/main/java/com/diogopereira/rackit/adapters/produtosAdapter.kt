package com.diogopereira.rackit.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.diogopereira.rackit.classes.Produto
import com.diogopereira.rackit.v2.R
import com.diogopereira.rackit.v2.databinding.ItemListaProdutosBinding

class produtosAdapter(private val productsList: ArrayList<Produto>) :
    RecyclerView.Adapter<produtosAdapter.ProductsViewHolder>() {

    private lateinit var binding: ItemListaProdutosBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductsViewHolder {
        binding =
            ItemListaProdutosBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        //val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_lista_produtos, parent,false)
        return ProductsViewHolder(binding.root)
    }

    override fun onBindViewHolder(holder: ProductsViewHolder, position: Int) {
        val currentItem = productsList[position]
        holder.nomeProduto.text = currentItem.listaInfoProduto.size.toString()+"x " + currentItem.nomeProduto
        if (!currentItem.listaInfoProduto.isEmpty())
            holder.dataValidade.text = "Expira em: " +currentItem.listaInfoProduto[0].dataValidade.toString()
        // TODO: 04/01/2022 Percorrer lista e comparar datas, convertendo string em data 
        else
            holder.dataValidade.text =""
        if (currentItem.imagemProduto != "") {
            Glide.with(holder.itemView.context).load(currentItem.imagemProduto)
                .into(holder.imagemProduto)
        } else {
            Glide.with(holder.itemView.context).load(R.drawable.ic_camera)
                .into(holder.imagemProduto)

        }
    }

    override fun getItemCount(): Int {
        return productsList.size

    }

    inner class ProductsViewHolder(productView: View) : RecyclerView.ViewHolder(productView) {

        val nomeProduto: TextView = binding.nomeProduto
        val imagemProduto: ImageView = binding.iconeNaLista
        val dataValidade: TextView = binding.dataValidade

    }
}