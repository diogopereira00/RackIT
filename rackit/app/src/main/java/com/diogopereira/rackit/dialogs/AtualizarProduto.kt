package com.diogopereira.rackit.dialogs

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.diogopereira.rackit.classes.Produto
import com.diogopereira.rackit.v2.R
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.dialog_delete_infoproduto.*

class AtualizarProduto: BottomSheetDialogFragment() {
    var mContext: Context? = null
    //var produto = currentProduto
    public var foiEliminado = false
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.dialog_delete_infoproduto, container, false)



        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mContext = context
        APAGAR.setOnClickListener {


            FirebaseDatabase.getInstance().getReference("Produtos")
                .child(produto.produtoID!!).removeValue()

            dismiss()
            (mContext as Activity).finish()
        }
        cancelar.setOnClickListener {
            dismiss()
        }
    }
}
