package com.diogopereira.rackit.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.diogopereira.rackit.v2.R

class HorizontalAdapter(val list: ArrayList<String>) :
    RecyclerView.Adapter<HorizontalAdapter.MyView>() {
    class MyView(view: View) : RecyclerView.ViewHolder(view) {
        var imageView: ImageView

        init {
            imageView = view
                .findViewById<ImageView>(R.id.image)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyView {
        val itemView: View = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.recycler_item, parent, false
            )
        return MyView(itemView)
    }

    override fun onBindViewHolder(holder: MyView, position: Int) {
        val listData = list[position]

        //Loading Image into view
//        Picasso.get().load(listData).placeholder(R.mipmap.ic_launcher).into(holder.imageView)

    }

    override fun getItemCount(): Int {
        return list.size
    }

}