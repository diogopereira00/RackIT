package com.diogo.rackit

import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.diogo.rackit.databinding.ActivityRegisterBinding
import com.diogo.rackit.databinding.ActivityWelcomeBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class RegisterActivity : AppCompatActivity() {
    private lateinit var botaoRegister: Button
    private lateinit var email : String
    private lateinit var username: String
    private lateinit var password : String

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var auth: FirebaseAuth



    override fun onCreate(savedInstanceState: Bundle?) {
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
                    updateUI(user)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "createUserWithEmail:failure", task.exception)
                    Toast.makeText(baseContext, "Authentication failed.",
                        Toast.LENGTH_SHORT).show()
                    updateUI(null)
                }
            }
        // [END create_user_with_email]
    }
    private fun updateUI(user: FirebaseUser?) {
        closeOpenActivity(outraActivity = AuthenticationActivity::class.java)


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