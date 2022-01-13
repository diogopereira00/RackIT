package com.diogopereira.rackit

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.viewpager2.widget.ViewPager2
import com.diogopereira.rackit.v2.R
import com.diogopereira.rackit.v2.databinding.ActivityMainBinding
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.tabs.TabLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_main.*
import android.content.DialogInterface


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var teste: TextView
    private lateinit var firebaseAuth: FirebaseAuth
    lateinit var addButton: FloatingActionButton

    lateinit var gv: GlobalClass

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val viewPager = binding.viewPager
        val view = binding.root
        setContentView(view)
        gv = application as GlobalClass
        addButton = binding.addButton

        //teste = binding.teste
        firebaseAuth = FirebaseAuth.getInstance()

        if (firebaseAuth == null) {
            startActivity(Intent(this, AuthenticationActivity::class.java))

        }
        else{
            gv.uidUtilizador = firebaseAuth.currentUser!!.uid
            gv.emailUtilizador = firebaseAuth.currentUser!!.email.toString()
        }
        setUpBar()

        addButton.setOnClickListener {
            //fragmnet produto
            if (viewPager.currentItem == 1)
                startActivity(Intent(this, AddProductActivity::class.java))
            //fragment listacompras
            if(viewPager.currentItem == 2){
                startActivity(Intent(this, AddProductListaComprasActivity::class.java))

            }
        }

    }
    public fun modificarPosicao(posicao : Int){
        viewPager.currentItem = posicao
    }
    private fun setUpBar() {
        val adapter = TabPageAdapter(activity = this, tabLayout.tabCount)
        binding.viewPager.adapter = adapter

        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                tabLayout.selectTab(tabLayout.getTabAt(position))
            }
        })
        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                viewPager.currentItem = tab.position

                if (viewPager.currentItem == 0) {
                    binding.fragmentName.text = "Home"
                    binding.addButton.visibility = View.GONE

                } else if (viewPager.currentItem == 1) {
                    binding.fragmentName.text = "Lista de Produtos"
                    binding.addButton.visibility = View.VISIBLE

                } else if (viewPager.currentItem == 2) {
                    binding.fragmentName.text = "Lista de Compras"
                    binding.addButton.visibility = View.VISIBLE

                } else if (viewPager.currentItem == 3) {
                    binding.fragmentName.text = "Definições"
                    binding.addButton.visibility = View.GONE

                }

            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {

            }

            override fun onTabReselected(tab: TabLayout.Tab?) {

            }
        })
    }

    override fun onStart() {
        super.onStart()
        //Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = firebaseAuth.currentUser

        if (currentUser == null) {

            startActivity(Intent(this@MainActivity, AuthenticationActivity::class.java))
            finish()

        }
//            val ref = FirebaseDatabase.getInstance().getReference("Users")
//            ref.child(currentUser.uid)
//                .addListenerForSingleValueEvent(object : ValueEventListener {
//                    override fun onDataChange(snapshot: DataSnapshot) {
//                        val userType = snapshot.child("userType").value
//                        val name = snapshot.child("name").value.toString()
//                        gv.nomeUtilizador = name
//                        //teste.text = gv.nomeUtilizador
//
//                    }
//
//                    override fun onCancelled(error: DatabaseError) {
//                    }
//                })


    }

    override fun onResume() {
        super.onResume()
        // Check if user is signed in (non-null) and update UI accordingly.
        //        val currentUser = auth.currentUser
        //
        //        if (currentUser != null) {
        //
        //        }
        //teste.text = gv.nomeUtilizador

    }
}
