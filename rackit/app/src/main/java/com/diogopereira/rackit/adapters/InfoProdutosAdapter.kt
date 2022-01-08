package com.diogopereira.rackit.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.diogopereira.rackit.GlobalClass
import com.diogopereira.rackit.classes.InfoProduto
import com.diogopereira.rackit.classes.Produto
import com.diogopereira.rackit.v2.R
import com.diogopereira.rackit.v2.databinding.ActivityInfoProdutoBinding
import com.diogopereira.rackit.v2.databinding.InfoProdutosListaBinding
import com.diogopereira.rackit.v2.databinding.ItemListaProdutosBinding

class InfoProdutosAdapter(private val productsList: ArrayList<InfoProduto>, private val produto : Produto) :
    RecyclerView.Adapter<InfoProdutosAdapter.InfoProductsViewHolder>() {
    private lateinit var binding: InfoProdutosListaBinding
    private var gv = GlobalClass()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InfoProductsViewHolder {
        binding = InfoProdutosListaBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        gv = parent.context.applicationContext as GlobalClass

        return InfoProductsViewHolder(binding.root)
    }

    override fun onBindViewHolder(holder: InfoProductsViewHolder, position: Int) {
        val currentItem = productsList[position]
        if (produto.imagemProduto != "") {
            Glide.with(holder.itemView.context).load(produto.imagemProduto)
                .into(holder.imagemProduto)
        } else {
            Glide.with(holder.itemView.context).load(R.drawable.ic_camera)
                .into(holder.imagemProduto)


        }
        holder.nome.text  = gv.currentProduto.nomeProduto
        holder.dataValidade.text = "Expira a " + currentItem.dataValidade
    }

    override fun getItemCount(): Int {
        return productsList.size
    }

    inner class InfoProductsViewHolder(productView: View) : RecyclerView.ViewHolder(productView) {
        val nome : TextView = binding.nomeProduto
        val dataValidade : TextView = binding.dataValidade
        val imagemProduto : ImageView = binding.iconeNaLista

    }


}

