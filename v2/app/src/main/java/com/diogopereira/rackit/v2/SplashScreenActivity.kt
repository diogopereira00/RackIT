package com.diogopereira.rackit.v2

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.diogopereira.rackit.v2.databinding.ActivitySplashScreenBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import java.util.concurrent.CountDownLatch

class SplashScreenActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashScreenBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var firebaseAuth: FirebaseAuth

    lateinit var gv : GlobalClass
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        auth = FirebaseAuth.getInstance()
        gv = application as GlobalClass

    }

    override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        if (currentUser != null) {
            val email = currentUser.email!!
            gv.emailUtilizador = email
            val uid = currentUser.uid



            //val nome = getNome(uid)
            gv.uidUtilizador = uid


            startActivity(Intent(this@SplashScreenActivity,MainActivity::class.java))
            finish()


        } else {
            startActivity(Intent(this@SplashScreenActivity,WelcomeActivity::class.java))


        }

    }


}