package com.diogopereira.rackit

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.diogopereira.rackit.v2.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class LoginActivity : AppCompatActivity() {
    lateinit var botaoLogin: Button
    private lateinit var binding: ActivityLoginBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var progressDialog : ProgressDialog

    private lateinit var editTextEmail: EditText
    private lateinit var editTextPassword: EditText
    private var email = ""
    private var password =""
    private var gv = GlobalClass()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        editTextEmail = binding.editTextEmail
        editTextPassword = binding.editTextPassword
        botaoLogin = binding.loginButton
        firebaseAuth = FirebaseAuth.getInstance()

        progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Um momento")
        progressDialog.setCanceledOnTouchOutside(false)

        botaoLogin.setOnClickListener {
            validadarDados()
            //openActivity(outraActivity = MainActivity::class.java)
        }
    }


    private fun validadarDados() {
        email = editTextEmail.text.toString().trim()
        password = editTextPassword.text.toString().trim()

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            editTextEmail.error = "Introduza um email valido"
        }
        else if(password.isEmpty()){
            editTextPassword.error ="Introduza uma password valida"
        }
        else{
            loginUser()
        }

    }

    private fun loginUser() {
        progressDialog.setMessage("Entrando...")
        progressDialog.show()

        firebaseAuth.signInWithEmailAndPassword(email,password)
            .addOnSuccessListener {
                checkUser()
            }
            .addOnFailureListener { e->
                progressDialog.dismiss()
                Toast.makeText(this,"Erro ao entrar, ${e.message}",Toast.LENGTH_SHORT).show()
            }
    }

    private fun checkUser() {
        progressDialog.setMessage("A verificar utilizador...")

        val fireBaseUser = firebaseAuth.currentUser!!

        val ref = FirebaseDatabase.getInstance().getReference("Users")
        ref.child(fireBaseUser.uid)
            .addListenerForSingleValueEvent(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    progressDialog.dismiss()
                    val userType = snapshot.child("userType").value
                    val name = snapshot.child("name").value.toString()

                    if(userType=="user"){
                        gv.nomeUtilizador = name
                        gv.emailUtilizador = email
                        startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                        finish()
                    }
                    else if(userType=="admin"){
                        startActivity(Intent(this@LoginActivity, WelcomeActivity::class.java))
                        finish()
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })
    }
}