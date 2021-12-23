package com.diogopereira.rackit

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.diogopereira.rackit.v2.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var teste: TextView
    private lateinit var firebaseAuth: FirebaseAuth

    lateinit var gv: GlobalClass

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        gv = application as GlobalClass
        teste = binding.teste
        firebaseAuth = FirebaseAuth.getInstance()


    }

    override fun onStart() {
        super.onStart()
        //Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = firebaseAuth.currentUser

        if (currentUser != null) {
            val ref = FirebaseDatabase.getInstance().getReference("Users")
            ref.child(currentUser.uid)
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val userType = snapshot.child("userType").value
                        val name = snapshot.child("name").value.toString()
                        gv.nomeUtilizador = name
                        teste.text = gv.nomeUtilizador

                    }

                    override fun onCancelled(error: DatabaseError) {
                        TODO("Not yet implemented")
                    }
                })
        } else {
            startActivity(Intent(this@MainActivity, AuthenticationActivity::class.java))
            finish()
        }


    }

    override fun onResume() {
        super.onResume()
        // Check if user is signed in (non-null) and update UI accordingly.
        //        val currentUser = auth.currentUser
        //
        //        if (currentUser != null) {
        //
        //        }
        teste.text = gv.nomeUtilizador

    }
}
