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
import com.diogopereira.rackit.adapters.InfoProdutosAdapter
import com.diogopereira.rackit.classes.InfoProduto
import com.diogopereira.rackit.classes.Produto
import com.diogopereira.rackit.v2.R
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.dialog_add_infoproduto.*
import kotlinx.android.synthetic.main.dialog_delete_produto.cancelar
import java.text.SimpleDateFormat
import java.util.*

class AdicionarInfoProduto(currentProduto: Produto, infoAdapter: InfoProdutosAdapter) :
    BottomSheetDialogFragment() {
    var mContext: Context? = null
    var produto = currentProduto
    var dataValidade: String = ""
    var adapter = infoAdapter
    private var gv = GlobalClass()

    val myCalendar = Calendar.getInstance()
    val datePicker = DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
        myCalendar.set(Calendar.YEAR, year)
        myCalendar.set(Calendar.MONTH, month)
        myCalendar.set(Calendar.DAY_OF_YEAR, dayOfMonth)
        updateData(myCalendar)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.dialog_add_infoproduto, container, false)

        mContext = context
        gv = mContext!!.applicationContext as GlobalClass

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sure.setText("Tem a certeza que pretende adicionar uma unidade de ${produto.nomeProduto} à sua lista de produtos?")
        adicionar.setOnClickListener {

            val hasMapInfoProduto: HashMap<String, Any?> = HashMap()
            var timestamp = System.currentTimeMillis()
            val sdf = SimpleDateFormat("dd/MM/yyyy hh:mm:ss")
            val currentDate = sdf.format(Date())
            val keyInfo = "Info_" + produto.nomeProduto!!.replace(" ", "_") + "_" + timestamp
            hasMapInfoProduto["dataValidade"] = dataValidadeEditText.text.toString()
            hasMapInfoProduto["precoCompra"] = ""
            hasMapInfoProduto["produtoID"] = produto.produtoID
            hasMapInfoProduto["infoProdutoID"] = keyInfo

            val refInfoProdutos = FirebaseDatabase.getInstance().getReference("InfoProdutos")
            refInfoProdutos.child(keyInfo).setValue(hasMapInfoProduto)
                .addOnSuccessListener {
                    Toast.makeText(mContext, "Produto adicionado com sucesso.", Toast.LENGTH_SHORT)
                        .show()
                    //adapter.notifyDataSetChanged()
                    adapter.notifyDataSetChanged()
                    dismiss()
                    (mContext as Activity).finish()

                }
                .addOnFailureListener { e ->
                    Toast.makeText(mContext, "Erro...${e.message}", Toast.LENGTH_SHORT).show()
                    dismiss()
                }

        }

        dataValidadeEditText.setOnClickListener {
            DatePickerDialog(
                mContext!!,
                datePicker,
                myCalendar.get(Calendar.YEAR),
                myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)
            ).show()
        }
        cancelar.setOnClickListener {
            dismiss()
        }
    }


    private fun updateData(myCalendar: Calendar) {
        val myFormat = "dd/MM/yyyy"
        val sdf = SimpleDateFormat(myFormat, Locale.ENGLISH)
        dataValidade = sdf.format(myCalendar.time)
        dataValidadeEditText.setText(sdf.format(myCalendar.time))
//        Toast.makeText(this,"Erro ao entrar, ${sdf.format(myCalendar.time)}", Toast.LENGTH_SHORT).show()

    }

}

