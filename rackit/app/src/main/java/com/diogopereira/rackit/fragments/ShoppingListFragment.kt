package com.diogopereira.rackit.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.diogopereira.rackit.GlobalClass
import com.diogopereira.rackit.v2.R
import com.diogopereira.rackit.v2.databinding.FragmentProductsBinding
import com.diogopereira.rackit.v2.databinding.FragmentShoppingListBinding


class ShoppingListFragment : Fragment() {
    private var _binding: FragmentShoppingListBinding? = null
    private val binding get() = _binding!!
    lateinit var gv: GlobalClass
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentShoppingListBinding.inflate(inflater, container, false)
        val view = binding.root

        binding.tag.text ="Teste"
        return view
    }
// TODO: 04/01/2022 RECYCLYER VIEW LISTA DE COMPRAS <--------------

}