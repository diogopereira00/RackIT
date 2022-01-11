package com.diogopereira.rackit.adapters

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
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
import com.diogopereira.rackit.v2.databinding.InfoProdutosListaBinding
import com.google.firebase.database.FirebaseDatabase
import android.content.DialogInterface
import androidx.core.graphics.drawable.DrawableCompat

import android.graphics.Color

import androidx.appcompat.content.res.AppCompatResources


class InfoProdutosAdapter(
    private val productsList: ArrayList<InfoProduto>,
    private val produto: Produto
) :
    RecyclerView.Adapter<InfoProdutosAdapter.InfoProductsViewHolder>() {
    private lateinit var binding: InfoProdutosListaBinding
    private var gv = GlobalClass()
    private lateinit var context: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InfoProductsViewHolder {
        binding =
            InfoProdutosListaBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        gv = parent.context.applicationContext as GlobalClass
        context = parent.context
        return InfoProductsViewHolder(binding.root)
    }

    override fun onBindViewHolder(holder: InfoProductsViewHolder, position: Int) {
        val currentItem = productsList[position]
        if (produto.imagemProduto != "") {
            Glide.with(holder.itemView.context).load(produto.imagemProduto)
                .into(holder.imagemProduto)
        } else {
            Glide.with(holder.itemView.context)
                .load(com.diogopereira.rackit.v2.R.drawable.ic_camera)
                .into(holder.imagemProduto)


        }
        holder.nome.text = gv.currentProduto.nomeProduto
        if (currentItem.dataValidade == "") {
            holder.dataValidade.text = "Sem data de validade"

        } else
            holder.dataValidade.text = "Expira a " + currentItem.dataValidade


        holder.deleteButton.setOnClickListener {
            //bug ao eliminar mais que 1

            val unwrappedDrawable =
                AppCompatResources.getDrawable(context, android.R.drawable.ic_dialog_alert)
            val wrappedDrawable = DrawableCompat.wrap(unwrappedDrawable!!)
            DrawableCompat.setTint(wrappedDrawable, Color.RED)
            //codigo

            AlertDialog.Builder(context)
                .setTitle("Apagar entrada")
                .setMessage("Tem a certeza que pretende apagar este produto?")

                .setPositiveButton("Sim") { dialog, which ->
                    FirebaseDatabase.getInstance().getReference("InfoProdutos")
                        .child(currentItem.infoProdutoID!!).removeValue()
                    productsList.remove(currentItem)
                    if (productsList.size == 0) {
                        (context as Activity).finish()
                    }
                    //notifyItemRemoved(position)
                    notifyDataSetChanged()


                }
                .setNegativeButton("Não", null)
                .setIcon(wrappedDrawable)
                .create()
                .show()


        }
    }


    override fun getItemCount(): Int {
        return productsList.size
    }

    inner class InfoProductsViewHolder(productView: View) : RecyclerView.ViewHolder(productView) {
        val nome: TextView = binding.nomeProduto
        val dataValidade: TextView = binding.dataValidade
        val imagemProduto: ImageView = binding.iconeNaLista
        val deleteButton: ImageView = binding.delete

    }


}

