package com.diogopereira.rackit

import android.app.DatePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import com.diogopereira.rackit.v2.R
import com.diogopereira.rackit.v2.databinding.ActivityAddProductBinding
import com.diogopereira.rackit.v2.databinding.ActivityMainBinding
import java.text.SimpleDateFormat
import java.util.*

class AddProductActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddProductBinding
    private lateinit var backButton : ImageButton
    private lateinit var dataValidade: EditText
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddProductBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        dataValidade = binding.dataValidadeEditText
        backButton = binding.backButton

        val myCalendar =  Calendar.getInstance()
        val datePicker = DatePickerDialog.OnDateSetListener{ view, year, month, dayOfMonth ->
            myCalendar.set(Calendar.YEAR,year)
            myCalendar.set(Calendar.MONTH,month)
            myCalendar.set(Calendar.DAY_OF_YEAR,dayOfMonth)
            updateData(myCalendar)
        }
        dataValidade.setOnClickListener{
            DatePickerDialog(this,datePicker,myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
            myCalendar.get(Calendar.DAY_OF_MONTH)).show()
         }
        backButton.setOnClickListener{
            startActivity(Intent(this@AddProductActivity, MainActivity::class.java))
            finish()
        }
    }

    private fun updateData(myCalendar: Calendar) {
        val myFormat = "dd-MM-yyyy"
        val sdf = SimpleDateFormat(myFormat,Locale.ENGLISH)
        dataValidade.setText(sdf.format(myCalendar.time))
//        Toast.makeText(this,"Erro ao entrar, ${sdf.format(myCalendar.time)}", Toast.LENGTH_SHORT).show()

    }
    // TODO: 27/12/2021 adicionar imagem https://www.youtube.com/watch?v=DPHkhamDoyc 
    // TODO: 26/12/2021 Adicionar Produto a base de Dados com ligação a Lista automaticamente <- Urgente 

}