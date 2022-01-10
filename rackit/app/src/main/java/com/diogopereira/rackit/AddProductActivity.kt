package com.diogopereira.rackit

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.widget.*
import androidx.core.content.FileProvider
import androidx.core.net.toUri
import com.diogopereira.rackit.v2.databinding.ActivityAddProductBinding
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.android.synthetic.main.activity_add_product.*
import kotlinx.android.synthetic.main.activity_welcome.*
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap

class AddProductActivity : AppCompatActivity() {


    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var storageReference: StorageReference
    private lateinit var imageUri: Uri
    private lateinit var binding: ActivityAddProductBinding
    private lateinit var backButton: ImageButton
    private lateinit var addButton: Button
    private lateinit var dataValidade: EditText
    private lateinit var editTextNomeProduto: EditText
    private lateinit var editTextCodBarras: EditText
    private lateinit var editTextPrecoCompra: EditText
    private lateinit var imagem: EditText
    private val REQUEST_CODE = 100
    lateinit var photoFile: File


    lateinit var mImageView: ImageView

    private var gv = GlobalClass()

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            mImageView.setImageURI(photoFile.toUri())
            imagem.setText(photoFile.toString())
            imageUri = photoFile.toUri()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddProductBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        firebaseAuth = FirebaseAuth.getInstance()
        gv = application as GlobalClass

        addButton = binding.addButton
        mImageView = binding.imageView2
        editTextNomeProduto = binding.nomeProdutoEditText
        editTextCodBarras = binding.codBarrasEditText
        editTextPrecoCompra = binding.precoCompraEditText

        dataValidade = binding.dataValidadeEditText
        backButton = binding.backButton
        imagem = binding.imagemEditText
        photoFile = getPhotoFile("photo.jpg")
        val myCalendar = Calendar.getInstance()
        val datePicker = DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
            myCalendar.set(Calendar.YEAR, year)
            myCalendar.set(Calendar.MONTH, month)
            myCalendar.set(Calendar.DAY_OF_YEAR, dayOfMonth)
            updateData(myCalendar)
        }
        addButton.setOnClickListener {
            validateData()

        }
        imagem.setOnClickListener {
            takePicture()
        }
        dataValidade.setOnClickListener {
            DatePickerDialog(
                this, datePicker, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)
            ).show()
        }
        backButton.setOnClickListener {
            //startActivity(Intent(this@AddProductActivity, MainActivity::class.java))
            finish()
        }
    }

    private var nomeProduto = ""
    private var codBarras = ""
    private var precoCompra = ""
    private var data = ""
    private var imagemProduto = ""
    private fun validateData() {
        nomeProduto = editTextNomeProduto.text.toString()
        codBarras = editTextCodBarras.text.toString()
        precoCompra = editTextPrecoCompra.text.toString()
        data = dataValidade.text.toString()
        imagemProduto = imagem.text.toString()

        if (nomeProduto != "") {
            createProduct()
        } else {
            if (nomeProduto == "") {
                editTextNomeProduto.error = "Tem de atribuir um nome ao produto"
            }
        }
    }

    private fun createProduct() {
        var timestamp = System.currentTimeMillis()

        val uid = firebaseAuth.uid
        val keyProduct = nomeProduto.replace(" ", "_") + "_" + timestamp

        val sdf = SimpleDateFormat("dd/MM/yyyy hh:mm:ss")
        val currentDate = sdf.format(Date())

        val hashMapProduto: HashMap<String, Any?> = HashMap()
        hashMapProduto["nomeProduto"] = nomeProduto
        hashMapProduto["codBarras"] = codBarras
        hashMapProduto["timestamp"] = uid
        hashMapProduto["listaDe"] = gv.currentList
        //hashMapProduto["imagemProduto"] = photoFile.toUri()
        hashMapProduto["adicionadoEm"] = timestamp
        hashMapProduto["produtoID"] = keyProduct
        hashMapProduto["imagemProduto"] = ""

        val keyProduto = nomeProduto + "_" + timestamp
        val ref = FirebaseDatabase.getInstance().getReference("Produtos")
        ref.child(keyProduct).setValue(hashMapProduto)
            .addOnSuccessListener {

                if (imagemProduto != "") {
                    updateProductImage(keyProduct)
                } else {
                    Toast.makeText(this, "Produto adicionado...", Toast.LENGTH_SHORT).show()
                    //finish()

                }
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Erro...${e.message}", Toast.LENGTH_SHORT).show()
            }

        val hasMapInfoProduto: HashMap<String, Any?> = HashMap()
        val keyInfo = "Info_" + nomeProduto.replace(" ", "_") + "_" + timestamp
        hasMapInfoProduto["dataCompra"] = currentDate
        hasMapInfoProduto["dataValidade"] = data
        hasMapInfoProduto["precoCompra"] = precoCompra
        hasMapInfoProduto["produtoID"] = keyProduct
        hasMapInfoProduto["infoProdutoID"] = keyInfo

        hashMapProduto["adicionadoPor"] = uid
        timestamp = System.currentTimeMillis()
        hashMapProduto["adicionadoEm"] = timestamp

        val refInfoProdutos = FirebaseDatabase.getInstance().getReference("InfoProdutos")
        refInfoProdutos.child(keyInfo).setValue(hasMapInfoProduto)
            .addOnSuccessListener {
                Toast.makeText(this, "Produto adicionado com sucesso.", Toast.LENGTH_SHORT).show()
                finish()
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Erro...${e.message}", Toast.LENGTH_SHORT).show()
            }


    }


    private fun updateProductImage(key: String) {
        storageReference = FirebaseStorage.getInstance()
            .getReference("ListaProdutos/" + gv.currentList + "/" + key)
        storageReference.putFile(imageUri).addOnSuccessListener { taskSnapshot ->
            val uriTask: Task<Uri> = taskSnapshot.storage.downloadUrl
            while (!uriTask.isSuccessful);
            val uploadImageUrl = "${uriTask.result}"
            updateImageURL(uploadImageUrl, key)
            //Toast.makeText(this, "Imagem adicionado... ${uriTask.result}\"", Toast.LENGTH_SHORT).show()

        }
            .addOnFailureListener { e ->

                Toast.makeText(this, "Erro...${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun updateImageURL(uploadImageUrl: String, key: String) {
        val hashMap: HashMap<String, Any> = HashMap()
        hashMap["imagemProduto"] = uploadImageUrl
        val ref = FirebaseDatabase.getInstance().getReference("Produtos")
        ref.child(key).updateChildren(hashMap)
            .addOnSuccessListener {
                //Toast.makeText(this, "Imagem atualizado ", Toast.LENGTH_SHORT).show()
                finish()
            }
            .addOnFailureListener { e ->
                Toast.makeText(
                    this,
                    "Erro de atualização de imagem devido  ${e.message} ",
                    Toast.LENGTH_SHORT
                ).show()
            }
    }

    private fun takePicture() {
        val picInt = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        photoFile = createPhotoFile()
        val uris =
            FileProvider.getUriForFile(this, "com.diogopereira.rackit.fileprovider", photoFile)
        picInt.putExtra(MediaStore.EXTRA_OUTPUT, uris)
        startActivityForResult(picInt, REQUEST_CODE)
    }

    private fun createPhotoFile(): File {
        val timestamp: String = SimpleDateFormat("ddMMyyyy_HHmmss").format(Date())
        val storageDir: File? = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
            "JPEG_${timestamp}_",
            ".jpg",
            storageDir
        ).apply {

        }
    }

    private fun getPhotoFile(fileName: String): File {
        val diStorage = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(fileName, "jpg", diStorage)
    }

    private fun updateData(myCalendar: Calendar) {
        val myFormat = "dd/MM/yyyy"
        val sdf = SimpleDateFormat(myFormat, Locale.ENGLISH)
        dataValidade.setText(sdf.format(myCalendar.time))
//        Toast.makeText(this,"Erro ao entrar, ${sdf.format(myCalendar.time)}", Toast.LENGTH_SHORT).show()

    }


}