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
import android.widget.EditText
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import java.lang.Exception

class VariaveisGlobais : Application() {
     lateinit var user : User
}

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private  lateinit var  db: FirebaseFirestore
    private lateinit var auth: FirebaseAuth

    lateinit var gv: VariaveisGlobais

    private lateinit var botaoRegister: Button
    private lateinit var editTextNome : EditText
    private lateinit var editTextEmail : EditText
    private lateinit var editTextPassword : EditText




    override fun onCreate(savedInstanceState: Bundle?) {
        gv = application as VariaveisGlobais

        super.onCreate(savedInstanceState)
        this.binding = ActivityRegisterBinding.inflate(layoutInflater)
        val view = binding.root

        setContentView(view)

        botaoRegister = binding.registerButton
        editTextEmail = binding.emailEditText
        editTextNome  = binding.usernameEditText
        editTextPassword = binding.passwordEditText
        db = Firebase.firestore
        auth = Firebase.auth


        botaoRegister.setOnClickListener{
            validateData()
        }
    }

    private fun validateData(){
        val email  = editTextEmail.text.toString().trim()
        val password  = editTextPassword.text.toString().trim()
        val username = editTextNome.text.toString().trim()

        //validar dados
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            //invalido
            editTextEmail.error ="Introduza um email valido"

            }
        else if(TextUtils.isEmpty(password)){
            editTextPassword.error ="Por favor introduza uma password"
        }
        else if(password.length<6){
            editTextPassword.error ="A sua password deve ter pelo menos 6 caracteres"
        }
        else{
            //valido continua signup
            createAccount(email,password, username)
        }
    }

    private fun createAccount(email: String, password: String, nome : String) {
        // [START create_user_with_email]
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "createUserWithEmail:success")
                    val user = auth.currentUser

                    //crio utilizador e guardo na base de dados se !=null
                    val userInfo = User(nome, email,user?.uid)
                    if (user != null) {
                        gv.user = userInfo

                        db.collection("Users").document(email).set(userInfo)

                        }

                    //closeOpenActivity(outraActivity = MainActivity::class.java)

                    updateUI(user)
                } else {
                    // If sign in fails, display a message to the user
                    try {
                        throw task.exception!!
                    } catch (e: FirebaseAuthWeakPasswordException) {
                        editTextPassword.error="A password é demasiado fraca"
                        editTextPassword.requestFocus()
                    } catch (e: FirebaseAuthUserCollisionException) {
                        editTextEmail.error="Já existe uma conta com este email"
                        editTextEmail.requestFocus()
                    } catch (e: Exception) {
                        Log.e(TAG, e.message!!)
                    }

                    Log.w(TAG, "createUserWithEmail:failure", task.exception)
                    Toast.makeText(baseContext, "Authentication failed.",
                        Toast.LENGTH_SHORT).show()
                    updateUI(null)
                }
            }
    }

    private fun updateUI(user: FirebaseUser?) {
        if(user !=null) {
            closeOpenActivity(outraActivity = MainActivity::class.java)
        }

    }
    private fun closeOpenActivity(outraActivity: Class<*>) {
        val x = Intent(this, outraActivity)
        finish()
        startActivity(x)
    }
}