package com.diogo.rackit

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.diogo.rackit.databinding.ActivityMainBinding
import com.diogo.rackit.databinding.ActivityRegisterBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var binding: ActivityMainBinding
    private lateinit var  teste : TextView
    lateinit var gv: VariaveisGlobais

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        gv = application as VariaveisGlobais
        this.binding = ActivityMainBinding.inflate(layoutInflater) // getLayoutInflater()
        val view = binding.root
        setContentView(view)

        auth = Firebase.auth

    }
     override fun onStart() {
         super.onStart()
         // Check if user is signed in (non-null) and update UI accordingly.
         val currentUser = auth.currentUser

         if (currentUser != null) {

         }
         binding.teste.text = gv.user.email

     }
    //}

    private fun reload() {
    }


}