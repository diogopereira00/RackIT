package com.diogo.rackit

import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.diogo.rackit.databinding.ActivityLoginBinding

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class LoginActivity : AppCompatActivity() {
    lateinit var botaoLogin: Button
    private lateinit var binding: ActivityLoginBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var editTextEmail: EditText
    private lateinit var editTextPassword: EditText
    override fun onCreate(savedInstanceState: Bundle?) {
        var gv = application as VariaveisGlobais

        super.onCreate(savedInstanceState)
        this.binding = ActivityLoginBinding.inflate(layoutInflater)
        val view = binding.root

        setContentView(view)
        auth = Firebase.auth
        editTextEmail = binding.editTextEmail
        editTextPassword = binding.editTextPassword
        botaoLogin = binding.loginButton

        botaoLogin.setOnClickListener {
            validadarDados()
            //openActivity(outraActivity = MainActivity::class.java)
        }

    }

    private fun validadarDados() {
        var email = editTextEmail.text.toString()
        var password = editTextPassword.text.toString()

        if(Patterns.EMAIL_ADDRESS.matcher(email).matches() && password.isNotEmpty()){
            login(email,password)
        }
    }

    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        if (currentUser != null) {
            reload();
        }
    }

    private fun login(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithEmail:success")
                    val user = auth.currentUser
                    //closeOpenActivity(outraActivity = MainActivity::class.java)
                    // TODO: 22/12/2021 Adicionar utilizador a gv.user, pois crasha no main -> gv.user vazia 
                    updateUI(user)
                    //openActivity(outraActivity = MainActivity::class.java)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInWithEmail:failure", task.exception)
                    Toast.makeText(
                        baseContext, "Authentication failed.",
                        Toast.LENGTH_SHORT
                    ).show()
                    updateUI(null)
                }
            }

    }

    private fun updateUI(user: FirebaseUser?) {
        if (user != null) {
            openActivity(outraActivity = MainActivity::class.java)
            Toast.makeText(
                baseContext, "OK.",
                Toast.LENGTH_SHORT
            ).show()
        }

    }
    private fun closeOpenActivity(outraActivity: Class<*>) {
        val x = Intent(this, outraActivity)
        finish()
        startActivity(x)
    }
    private fun openActivity(outraActivity: Class<*>) {
        val x = Intent(this, outraActivity)
        startActivity(x)
    }

    private fun reload() {

    }


}