package com.diogopereira.rackit.dialogs

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.diogopereira.rackit.GlobalClass
import com.diogopereira.rackit.classes.Produto
import com.diogopereira.rackit.v2.R
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_add_product_lista_compras.*
import kotlinx.android.synthetic.main.dialog_add_infoproduto.dataValidadeEditText
import kotlinx.android.synthetic.main.dialog_adicionar_listacompras.*
import kotlinx.android.synthetic.main.dialog_adicionar_listacompras.quantidadeEditText
import kotlinx.android.synthetic.main.dialog_adicionar_listacompras.urgente
import java.text.SimpleDateFormat
import java.util.*

class AdicionarListaCompras(currentProduto: Produto) : BottomSheetDialogFragment() {
    var mContext: Context? = null
//    var produto = currentProduto
    var dataValidade: String = ""
//    var adapter = infoAdapter
    var produto = currentProduto
    private var gv = GlobalClass()



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.dialog_adicionar_listacompras, container, false)

        mContext = context
        gv = mContext!!.applicationContext as GlobalClass

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sure.setText("Tem a certeza que pretende adicionar ${produto.nomeProduto} à sua lista de compras?")
        cancelar.setOnClickListener {
            dismiss()
        }
        adicionarListaCompras.setOnClickListener {
            if(quantidadeEditText.text.toString()!=""){
                val hashMapListaCompras : HashMap<String, Any?> = HashMap()
                var timestamp = System.currentTimeMillis()
                val keyInfo = "listC_" + gv.uidUtilizador
                var keyProduto = produto.nomeProduto+"_"+timestamp
                hashMapListaCompras["nome"] = produto.nomeProduto
                hashMapListaCompras["imagem"] = produto.imagemProduto
                hashMapListaCompras["quantidade"] = quantidadeEditText.text.toString()
                hashMapListaCompras["produtoComprarID"] = keyProduto
                hashMapListaCompras["urgente"] = urgente.isChecked

                val refListaCompras = FirebaseDatabase.getInstance().getReference("ListaCompras")
                refListaCompras.child(keyInfo+"/Produtos/"+keyProduto).setValue(hashMapListaCompras)
                    .addOnSuccessListener {
                        Toast.makeText(mContext, "Produto adicionado a lista de compras com sucesso.", Toast.LENGTH_SHORT).show()
                        //adapter.notifyDataSetChanged()
                        dismiss()
                        (mContext as Activity).finish()


                    }
                    .addOnFailureListener { e ->
                        Toast.makeText(mContext, "Erro...${e.message}", Toast.LENGTH_SHORT).show()
                        dismiss()
                    }
            }
            else{
                quantidadeEditText.error = "Introduza a quantidade de produtos que pretende comprar"
            }
        }

    }




}