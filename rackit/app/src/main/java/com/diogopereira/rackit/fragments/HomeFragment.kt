package com.diogopereira.rackit.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.diogopereira.rackit.AddProductActivity
import com.diogopereira.rackit.AuthenticationActivity
import com.diogopereira.rackit.GlobalClass
import com.diogopereira.rackit.v2.databinding.FragmentHomeBinding
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class HomeFragment : Fragment() {
    var firedatabase : FirebaseDatabase? = null
    private lateinit var firebaseAuth: FirebaseAuth
    lateinit var gv: GlobalClass
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    lateinit var addButton : FloatingActionButton


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        gv = activity?.application as GlobalClass

        firebaseAuth = FirebaseAuth.getInstance()

        _binding = FragmentHomeBinding.inflate(inflater,container,false)
        val view = binding.root
        addButton = binding.addButton






        return view
    }

    override fun onStart() {
        super.onStart()
        addButton.setOnClickListener {
            startActivity(Intent(activity, AddProductActivity::class.java))

        }
    }
    override fun onResume() {
        super.onResume()
        val currentUser = firebaseAuth.currentUser
        if (currentUser != null) {
            val ref = FirebaseDatabase.getInstance().getReference("Users")
            ref.child(currentUser.uid)
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val userType = snapshot.child("userType").value
                        val name = snapshot.child("name").value.toString()
                        gv.nomeUtilizador = name
                        binding.teste.text = gv.nomeUtilizador
                    }

                    override fun onCancelled(error: DatabaseError) {
                        TODO("Not yet implemented")

                    }
                })
        } else {
            startActivity(Intent(activity, AuthenticationActivity::class.java))

        }
    }


}