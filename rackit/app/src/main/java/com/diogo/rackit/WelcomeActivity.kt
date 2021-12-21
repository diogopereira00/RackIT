package com.diogo.rackit

import android.app.Application
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import com.diogo.rackit.databinding.ActivityMainBinding
import com.diogo.rackit.databinding.ActivityWelcomeBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase

class VariaveisGlobais : Application() {
    lateinit var user: User
}

class WelcomeActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore

    lateinit var gv: VariaveisGlobais

    lateinit var botaoComecar: Button
    private lateinit var binding: ActivityWelcomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        gv = application as VariaveisGlobais

        super.onCreate(savedInstanceState)
        this.binding = ActivityWelcomeBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        botaoComecar = binding.welcomeButton
        botaoComecar.setOnClickListener {
            closeOpenActivity(outraActivity = AuthenticationActivity::class.java)

        }

        auth = Firebase.auth
        db = Firebase.firestore

    }

    // TODO: 21/12/2021  - alterar esta função para o splash screen


    override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        if (currentUser != null) {
            val email = currentUser.email
            val uid = currentUser.uid


            val docRef = db.collection("Users").document(email.toString())
            docRef.get()
                .addOnSuccessListener { document ->
                    if (document != null) {
                        val nome = document.getString("name")
                        gv.user = User(nome, email, uid)

                        closeOpenActivity(outraActivity = MainActivity::class.java)


                    } else {
                        Log.d("noexist", "nao existe")
                    }
                }.addOnFailureListener { exception ->
                    Log.d("error", "get failed with  ", exception)
                }

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