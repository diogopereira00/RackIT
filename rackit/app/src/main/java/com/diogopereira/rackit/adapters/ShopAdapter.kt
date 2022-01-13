package com.diogopereira.rackit.adapters

import android.content.Context
import android.text.SpannableStringBuilder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.text.color
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.diogopereira.rackit.GlobalClass
import com.diogopereira.rackit.classes.ProdutoComprar
import com.diogopereira.rackit.v2.R
import com.diogopereira.rackit.v2.databinding.ItemListaComprasBinding
import com.google.firebase.database.FirebaseDatabase

class ShopAdapter  : RecyclerView.Adapter<ShopAdapter.HolderListaCompras>  {

    // context, get using construtor
    private var context: Context
    private var gv = GlobalClass()

    private var shopListArray: ArrayList<ProdutoComprar>

    //viewbinding RowReviewsBinding.xml
    private lateinit var binding: ItemListaComprasBinding

    // construtor
    constructor(context: Context, reviewArrayList: ArrayList<ProdutoComprar>) {
        this.context = context
        this.shopListArray = reviewArrayList
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HolderListaCompras {
        binding = ItemListaComprasBinding.inflate(LayoutInflater.from(context), parent, false)
        gv = parent.context.applicationContext as GlobalClass

        return HolderListaCompras(binding.root)
    }

    override fun onBindViewHolder(holder: HolderListaCompras, position: Int) {
        val currentItem = shopListArray[position]
        if (!currentItem.imagem.isNullOrEmpty()) {
            Glide.with(holder.itemView.context).load(currentItem.imagem)
                .into(holder.imagemProduto)
        } else {
            Glide.with(holder.itemView.context).load(R.drawable.ic_camera)
                .into(holder.imagemProduto)


        }
        holder.quantidadeProduto.text = "Quantidade: " + currentItem.quantidade
        if(currentItem.urgente == true){
            val red = ContextCompat.getColor(context, R.color.red)
            val text = SpannableStringBuilder()
                .color(red) { append("⚠ ") }
                .append(currentItem.nome)

            holder.nomeProduto.text = text
        }

        else
            holder.nomeProduto.text = currentItem.nome

        binding.check.setOnClickListener {
            val caminho = "ListaCompras/listC_" + gv.uidUtilizador+"/Produtos"
            FirebaseDatabase.getInstance().getReference(caminho)
                .child(currentItem.produtoComprarID!!).removeValue()
        }
    }

    override fun getItemCount(): Int {
        return shopListArray.size
    }






    inner class HolderListaCompras(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nomeProduto = binding.nomeProduto
        val imagemProduto = binding.iconeNaLista
        val quantidadeProduto = binding.quantidade
    }

}