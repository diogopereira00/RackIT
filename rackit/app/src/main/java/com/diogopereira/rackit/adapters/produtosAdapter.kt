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
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.collections.ArrayList

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
        //codigo para retirar a data do produto com menor data de validade
        val myFormat = "dd/MM/yyyy"
        val sdf = SimpleDateFormat(myFormat, Locale.ENGLISH)
        var hojeString = sdf.format(Date())
        var novaData = Date()
        var hojeDate = SimpleDateFormat(myFormat).parse(hojeString)
        var arrayPosition = 0
        val currentItem = productsList[position]
        
        //se houver lista produtos
        if (!currentItem.listaInfoProduto.isEmpty()) {
            var dataV: Date = Date()
            //percorro a lista e verifico se ha data de validade
            for (p in currentItem.listaInfoProduto) {
                //se houver retiro a data e comparo, se for menor passa a ser novaData
                if (p.dataValidade != null) {
                    dataV = SimpleDateFormat("dd/MM/yyyy").parse(p.dataValidade)
                    if (dataV > hojeDate) {
                        if (novaData != Date())
                            novaData = dataV
                    }
                }
            }
            val dataEmSTRING = sdf.format(novaData)
            holder.nomeProduto.text =
                currentItem.listaInfoProduto.size.toString() + "x " + currentItem.nomeProduto

            holder.dataValidade.text = "Expira em: " + dataEmSTRING
        } else
            holder.dataValidade.text = ""
        
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