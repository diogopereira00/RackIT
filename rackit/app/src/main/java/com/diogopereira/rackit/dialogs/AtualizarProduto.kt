package com.diogopereira.rackit.dialogs

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.diogopereira.rackit.GlobalClass
import com.diogopereira.rackit.adapters.InfoProdutosAdapter
import com.diogopereira.rackit.classes.Produto
import com.diogopereira.rackit.v2.R
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.dialog_delete_produto.cancelar
import kotlinx.android.synthetic.main.dialog_update_produto.*

class AtualizarProduto(
    currentProduto: Produto,
    nome: String,
    codbarras: String,
    infoAdapter: InfoProdutosAdapter
) : BottomSheetDialogFragment() {
    var mContext: Context? = null
    var produto = currentProduto
    var nomeproduto = nome
    var codigobarras = codbarras
    var adapter = infoAdapter
    private var gv = GlobalClass()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.dialog_update_produto, container, false)



        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mContext = context
        gv = mContext!!.applicationContext as GlobalClass

        atualizar.setOnClickListener {
            val hashMapProduto: HashMap<String, Any?> = HashMap()
            // TODO: 10/01/2022 Alterar imagem, similar ao addProductsActivity

            hashMapProduto["nomeProduto"] = nomeproduto
            gv.currentProduto.nomeProduto = nomeproduto
            hashMapProduto["imagemProduto"] = gv.currentProduto.imagemProduto
            hashMapProduto["codBarras"] = codigobarras
            gv.currentProduto.codBarras = codigobarras

            hashMapProduto["listaDe"] = gv.currentList
            //hashMapProduto["imagemProduto"] = photoFile.toUri()
            hashMapProduto["adicionadoEm"] = gv.currentProduto.adicionadoEm
            hashMapProduto["produtoID"] = gv.currentProduto.produtoID
            hashMapProduto["imagemProduto"] = gv.currentProduto.imagemProduto
            val keyProduto = gv.currentProduto.produtoID
            val ref = FirebaseDatabase.getInstance().getReference("Produtos")
            ref.child(keyProduto!!).updateChildren(hashMapProduto)
                .addOnSuccessListener {
//                    Toast.makeText(
//                        this,
//                        "Produto atualizado $hashMapProduto...",
//                        Toast.LENGTH_SHORT
//                    ).show()
                    var teste = context
                    adapter.notifyDataSetChanged()

                }
                .addOnFailureListener { e ->
                    Toast.makeText(mContext, "Erro...${e.message}", Toast.LENGTH_SHORT)
                        .show()

                }



            dismiss()
//            (mContext as Activity).
        }
        cancelar.setOnClickListener {
            dismiss()
        }
    }
}
