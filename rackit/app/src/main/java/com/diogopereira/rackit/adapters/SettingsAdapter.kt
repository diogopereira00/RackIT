package com.diogopereira.rackit.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.diogopereira.rackit.AuthenticationActivity
import com.diogopereira.rackit.GlobalClass
import com.diogopereira.rackit.classes.Settings
import com.diogopereira.rackit.v2.R
import com.diogopereira.rackit.v2.databinding.ItemDefinicoesBinding
import com.google.firebase.auth.FirebaseAuth

class SettingsAdapter : RecyclerView.Adapter<SettingsAdapter.HolderDefinicoes> {

    // context, get using construtor
    private var context: Context
    private var gv = GlobalClass()
    private var settingsList: ArrayList<Settings>

    //viewbinding RowReviewsBinding.xml
    private lateinit var binding: ItemDefinicoesBinding

    // construtor
    constructor(context: Context, reviewArrayList: ArrayList<Settings>) {
        this.context = context
        this.settingsList = reviewArrayList
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HolderDefinicoes {
        binding = ItemDefinicoesBinding.inflate(LayoutInflater.from(context), parent, false)
        gv = parent.context.applicationContext as GlobalClass
        return HolderDefinicoes(binding.root)
    }

    override fun onBindViewHolder(holder: HolderDefinicoes, position: Int) {
        val currentItem = settingsList[position]
        if (currentItem.imagemSetting != null) {
            Glide.with(holder.itemView.context).load(currentItem.imagemSetting)
                .into(holder.imagemProduto)
        } else {
            Glide.with(holder.itemView.context).load(R.drawable.ic_camera)
                .into(holder.imagemProduto)


        }
        holder.descricao.text = currentItem.descricao
        holder.nomeSettings.text = currentItem.nomeSetting

        binding.layout.setOnClickListener {
            if(currentItem.id=="Conta") {
                FirebaseAuth.getInstance().signOut()
                var intent = Intent(context, AuthenticationActivity::class.java)
                context.startActivity(intent)

            }
        }
    }

    override fun getItemCount(): Int {
        return settingsList.size
    }


    inner class HolderDefinicoes(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nomeSettings = binding.nomeSetting
        val imagemProduto = binding.iconeNaLista
        val descricao = binding.descricao
    }
}

