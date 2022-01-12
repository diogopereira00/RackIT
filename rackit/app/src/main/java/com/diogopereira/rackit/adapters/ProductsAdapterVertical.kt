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
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class ProductsAdapterVertical :
    RecyclerView.Adapter<ProductsAdapterVertical.HolderProdutoVertical> {

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
        currentItem.listaInfoProduto.sortByDescending { it.dataValidade }
        holder.nomeProduto.text =
            currentItem.listaInfoProduto.size.toString() + "x " + currentItem.nomeProduto
        if (currentItem.listaInfoProduto[0].dataValidade.isNullOrEmpty()) {
            holder.dataValidade.text = "Sem data de validade"

        } else {
            holder.dataValidade.text =
                "Expira em: " + currentItem.listaInfoProduto[0].dataValidade.toString()
        }
        if (!currentItem.imagemProduto.isNullOrEmpty()) {
            Glide.with(holder.itemView.context).load(currentItem.imagemProduto)
                .into(holder.imagemProduto)
        } else {
            Glide.with(holder.itemView.context).load(R.drawable.ic_camera)
                .into(holder.imagemProduto)

//        verificarData(holder,position)
        }
        holder.itemView.setOnClickListener {
            gv.currentProduto = currentItem
            Toast.makeText(context, "${gv.currentProduto.nomeProduto}", Toast.LENGTH_SHORT).show()
            val intent = Intent(context, InfoProdutoActivity::class.java)
            context.startActivity(intent)
        }
//        holder.nomeProduto.text = currentItem.nomeProduto


    }


    private fun verificarData(holder: HolderProdutoVertical, position: Int) {
        val myFormat = "dd/MM/yyyy"
        val sdf = SimpleDateFormat(myFormat, Locale.ENGLISH)
        var hojeString = sdf.format(Date())
        var novaData = Date()
        var hojeDate = SimpleDateFormat(myFormat).parse(hojeString)
        val currentItem = shopListArray[position]

        //se houver lista produtos
        if (!currentItem.listaInfoProduto.isEmpty()) {
            var dataV: Date = Date()
            //percorro a lista e verifico se ha data de validade
            for (p in currentItem.listaInfoProduto) {
                //se houver retiro a data e comparo, se for menor passa a ser novaData
                if (p.dataValidade!!.isNotEmpty()) {
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
