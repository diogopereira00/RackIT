package com.diogo.rackit

import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.util.Patterns
import android.widget.Button
import android.widget.Toast
import com.diogo.rackit.databinding.ActivityRegisterBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import android.app.Application

import com.google.firebase.auth.FirebaseAuthUserCollisionException


import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import java.lang.Exception

class VariaveisGlobais : Application() {
     var loggedEmail : String = ""
}

class RegisterActivity : AppCompatActivity() {
    private lateinit var botaoRegister: Button
    private lateinit var email : String
    private lateinit var username: String
    private lateinit var password : String
    lateinit var gv: VariaveisGlobais
    private lateinit var binding: ActivityRegisterBinding
    private lateinit var auth: FirebaseAuth



    override fun onCreate(savedInstanceState: Bundle?) {
        gv = application as VariaveisGlobais
        super.onCreate(savedInstanceState)
        this.binding = ActivityRegisterBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        botaoRegister = binding.registerButton

        auth = Firebase.auth





        botaoRegister.setOnClickListener{
            validateData()
        }
    }

    private fun validateData(){
        email  = binding.emailEditText.text.toString().trim()
        password  = binding.passwordEditText.text.toString().trim()
        username = binding.usernameEditText.text.toString().trim()

        //validar dados
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            //invalido
            binding.emailEditText.error ="Introduza um email valido"

            }
        else if(TextUtils.isEmpty(password)){
            binding.passwordEditText.error ="Por favor introduza uma password"
        }
        else if(password.length<6){
            binding.passwordEditText.error ="A sua password deve ter pelo menos 6 caracteres"
        }
        else{
            //valido continua signup
            firebaseSignUp()
        }
    }

    private fun firebaseSignUp() {
        createAccount(email=email,password=password)
    }

    private fun createAccount(email: String, password: String) {
        // [START create_user_with_email]
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "createUserWithEmail:success")
                    val user = auth.currentUser
                    gv.loggedEmail = email
                    //closeOpenActivity(outraActivity = MainActivity::class.java)

                    updateUI(user)
                } else {
                    // If sign in fails, display a message to the user
                    try {
                        throw task.exception!!
                    } catch (e: FirebaseAuthWeakPasswordException) {
                        binding.passwordEditText.error="A password é demasiado fraca"
                        binding.passwordEditText.requestFocus()
                    } catch (e: FirebaseAuthUserCollisionException) {
                        binding.emailEditText.error="Já existe uma conta com este email"
                        binding.emailEditText.requestFocus()
                    } catch (e: Exception) {
                        Log.e(TAG, e.message!!)
                    }

                    Log.w(TAG, "createUserWithEmail:failure", task.exception)
                    Toast.makeText(baseContext, "Authentication failed.",
                        Toast.LENGTH_SHORT).show()
                    updateUI(null)
                }
            }
        // [END create_user_with_email]
    }
    private fun updateUI(user: FirebaseUser?) {
        if(user !=null) {
            closeOpenActivity(outraActivity = MainActivity::class.java)
        }

    }
    private fun executarOutraActivity(outraActivity: Class<*>) {
        val x = Intent(this, outraActivity)
        startActivity(x)
    }
    private fun closeOpenActivity(outraActivity: Class<*>) {
        val x = Intent(this, outraActivity)
        finish()
        startActivity(x)
    }
}