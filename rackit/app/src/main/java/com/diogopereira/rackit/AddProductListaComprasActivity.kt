package com.diogopereira.rackit

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.diogopereira.rackit.v2.databinding.ActivityAddProductBinding
import com.diogopereira.rackit.v2.databinding.ActivityAddProductListaComprasBinding
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.dialog_adicionar_listacompras.*
import java.util.HashMap

class AddProductListaComprasActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddProductListaComprasBinding
    private var gv = GlobalClass()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddProductListaComprasBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        gv = application as GlobalClass

        binding.addButton.setOnClickListener {
            if (binding.nomeProdutoEditText.text.toString() != "" && binding.quantidadeEditText.text.toString() != ""){
                val hashMapListaCompras : HashMap<String, Any?> = HashMap()
                var timestamp = System.currentTimeMillis()
                val keyInfo = "listC_" + gv.uidUtilizador
                var keyProduto = binding.nomeProdutoEditText.text.toString().replace(" ", "_")+"_"+timestamp
                hashMapListaCompras["nome"] = binding.nomeProdutoEditText.text.toString()
                hashMapListaCompras["quantidade"] = quantidadeEditText.text.toString()
                hashMapListaCompras["produtoComprarID"] = keyProduto
                hashMapListaCompras["urgente"] = urgente.isChecked

                val refListaCompras = FirebaseDatabase.getInstance().getReference("ListaCompras")
                refListaCompras.child(keyInfo+"/Produtos/"+keyProduto).setValue(hashMapListaCompras)
                    .addOnSuccessListener {
                        Toast.makeText(this, "Produto adicionado a lista de compras com sucesso.", Toast.LENGTH_SHORT).show()
                        //adapter.notifyDataSetChanged()
                        finish()
//                        (mContext as Activity).finish()

                    }
                    .addOnFailureListener { e ->
                        Toast.makeText(this, "Erro...${e.message}", Toast.LENGTH_SHORT).show()
                    }
            }
        }

        binding.backButton.setOnClickListener{
            finish()
        }
    }
}