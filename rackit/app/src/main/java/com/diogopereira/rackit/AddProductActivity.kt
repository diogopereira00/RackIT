package com.diogopereira.rackit

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import androidx.core.content.FileProvider
import androidx.core.net.toUri
import com.diogopereira.rackit.v2.databinding.ActivityAddProductBinding
import kotlinx.android.synthetic.main.activity_welcome.*
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
class AddProductActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddProductBinding
    private lateinit var backButton : ImageButton
    private lateinit var dataValidade: EditText
    private lateinit var imagem: EditText
    private val REQUEST_CODE = 100
    lateinit var photoFile : File
    lateinit var mImageView : ImageView


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK){
            mImageView.setImageURI(photoFile.toUri())
            imagem.setText(photoFile.toString())
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddProductBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        mImageView = binding.imageView2
        dataValidade = binding.dataValidadeEditText
        backButton = binding.backButton
        imagem = binding.imagemEditText
        photoFile = getPhotoFile("photo.jpg")
        val myCalendar =  Calendar.getInstance()
        val datePicker = DatePickerDialog.OnDateSetListener{ view, year, month, dayOfMonth ->
            myCalendar.set(Calendar.YEAR,year)
            myCalendar.set(Calendar.MONTH,month)
            myCalendar.set(Calendar.DAY_OF_YEAR,dayOfMonth)
            updateData(myCalendar)
        }
        imagem.setOnClickListener {
            takePicture()
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

    private fun takePicture() {
         val picInt = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        photoFile = createPhotoFile()
        val uris = FileProvider.getUriForFile(this,"com.diogopereira.rackit.fileprovider", photoFile)
        picInt.putExtra(MediaStore.EXTRA_OUTPUT,uris)
        startActivityForResult(picInt, REQUEST_CODE)
    }

    private fun createPhotoFile(): File {
        val timestamp:String = SimpleDateFormat("ddMMyyyy_HHmmss").format(Date())
        val storageDir : File? = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile("JPEG_${timestamp}_",
            ".jpg",
            storageDir).apply {

        }
    }

    private fun getPhotoFile(fileName:String): File {
        val diStorage = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(fileName,"jpg",diStorage)
    }

    private fun updateData(myCalendar: Calendar) {
        val myFormat = "dd-MM-yyyy"
        val sdf = SimpleDateFormat(myFormat,Locale.ENGLISH)
        dataValidade.setText(sdf.format(myCalendar.time))
//        Toast.makeText(this,"Erro ao entrar, ${sdf.format(myCalendar.time)}", Toast.LENGTH_SHORT).show()

    }
    // TODO: 27/12/2021 guardar imagem na base de dados https://www.youtube.com/watch?v=y4npeX35B34
    // TODO: 26/12/2021 Adicionar Produto a base de Dados com ligação a Lista automaticamente <- Urgente 

}