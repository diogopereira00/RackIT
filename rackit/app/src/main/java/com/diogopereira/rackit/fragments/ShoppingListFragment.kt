package com.diogopereira.rackit.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.diogopereira.rackit.GlobalClass
import com.diogopereira.rackit.adapters.ShopAdapter
import com.diogopereira.rackit.classes.ProdutoComprar
import com.diogopereira.rackit.v2.R
import com.diogopereira.rackit.v2.databinding.FragmentProductsBinding
import com.diogopereira.rackit.v2.databinding.FragmentShoppingListBinding
import com.google.android.datatransport.runtime.dagger.multibindings.ElementsIntoSet
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class ShoppingListFragment : Fragment() {
    private var _binding: FragmentShoppingListBinding? = null
    private val binding get() = _binding!!
    private lateinit var comprasRecyclerView: RecyclerView
    private lateinit var shopList: ArrayList<ProdutoComprar>
    private lateinit var shopAdapter: ShopAdapter
    lateinit var gv: GlobalClass
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentShoppingListBinding.inflate(inflater, container, false)
        val view = binding.root
        gv = activity?.application as GlobalClass


        shopList = ArrayList()
        shopAdapter = ShopAdapter(requireContext(), shopList)
        comprasRecyclerView = binding.recyclerViewCompras
        comprasRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        comprasRecyclerView.adapter = shopAdapter

        loadAllListaCompras()



        binding.swipeLayoutCompras.setOnRefreshListener {
            comprasRecyclerView.adapter = shopAdapter
            loadAllListaCompras()
            binding.swipeLayoutCompras.isRefreshing = false
        }
        return view
    }

    private fun loadAllListaCompras() {
        val caminho = "ListaCompras/listC_" + gv.uidUtilizador
        val ref = FirebaseDatabase.getInstance().getReference(caminho)
            .child("Produtos").addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    shopList.clear()
                    for (ds in snapshot.children) {
                        val model = ds.getValue(ProdutoComprar::class.java)
                        shopList.add(model!!)

                    }
                    if (shopList.size > 0) {
                        binding.semProdutos.visibility = View.GONE
                        binding.recyclerViewCompras.visibility = View.VISIBLE

                    } else {
                        binding.semProdutos.visibility = View.VISIBLE
                        binding.recyclerViewCompras.visibility = View.GONE
                    }
                    shopList.sortByDescending { it.urgente }
                    comprasRecyclerView.adapter = shopAdapter
                }

                override fun onCancelled(error: DatabaseError) {
                }
            })
    }

}