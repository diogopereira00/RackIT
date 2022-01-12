package com.diogopereira.rackit.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.diogopereira.rackit.GlobalClass
import com.diogopereira.rackit.adapters.ProductsAdapter
import com.diogopereira.rackit.adapters.ShopAdapter
import com.diogopereira.rackit.classes.InfoProduto
import com.diogopereira.rackit.classes.Produto
import com.diogopereira.rackit.classes.ProdutoComprar
import com.diogopereira.rackit.v2.databinding.FragmentHomeBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class HomeFragment : Fragment() {
    var firedatabase: FirebaseDatabase? = null
    private lateinit var firebaseAuth: FirebaseAuth
    lateinit var gv: GlobalClass
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!


    private lateinit var comprasRecyclerView: RecyclerView
    private lateinit var shopList: ArrayList<ProdutoComprar>
    private lateinit var shopAdapter: ShopAdapter


    private lateinit var produtoList: ArrayList<Produto>
    private lateinit var produtosRecyclerView: RecyclerView
    private lateinit var productsAdapter : ProductsAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        gv = activity?.application as GlobalClass

        firebaseAuth = FirebaseAuth.getInstance()

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val view = binding.root
        //creating recycler val
        produtosRecyclerView = binding.recycler

        //setting recycler to horizontal scroll
//        val data: ArrayList<String> = ArrayList()
//        data.add("https://images.unsplash.com/photo-1503023345310-bd7c1de61c7d?ixid=MXwxMjA3fDB8MHxzZWFyY2h8MXx8aHVtYW58ZW58MHx8MHw%3D&ixlib=rb-1.2.1&w=1000&q=80")
//        data.add("https://images.ctfassets.net/hrltx12pl8hq/4plHDVeTkWuFMihxQnzBSb/aea2f06d675c3d710d095306e377382f/shutterstock_554314555_copy.jpg")
//        data.add("https://images.unsplash.com/photo-1541963463532-d68292c34b19?ixlib=rb-1.2.1&ixid=MXwxMjA3fDB8MHxleHBsb3JlLWZlZWR8M3x8fGVufDB8fHw%3D&w=1000&q=80")
//        data.add("https://images.unsplash.com/photo-1503023345310-bd7c1de61c7d?ixid=MXwxMjA3fDB8MHxzZWFyY2h8MXx8aHVtYW58ZW58MHx8MHw%3D&ixlib=rb-1.2.1&w=1000&q=80")
//        data.add("https://images.ctfassets.net/hrltx12pl8hq/4plHDVeTkWuFMihxQnzBSb/aea2f06d675c3d710d095306e377382f/shutterstock_554314555_copy.jpg")
//        data.add("https://images.unsplash.com/photo-1541963463532-d68292c34b19?ixlib=rb-1.2.1&ixid=MXwxMjA3fDB8MHxleHBsb3JlLWZlZWR8M3x8fGVufDB8fHw%3D&w=1000&q=80")
//        data.add("https://images.unsplash.com/photo-1503023345310-bd7c1de61c7d?ixid=MXwxMjA3fDB8MHxzZWFyY2h8MXx8aHVtYW58ZW58MHx8MHw%3D&ixlib=rb-1.2.1&w=1000&q=80")
//        data.add("https://images.ctfassets.net/hrltx12pl8hq/4plHDVeTkWuFMihxQnzBSb/aea2f06d675c3d710d095306e377382f/shutterstock_554314555_copy.jpg")
//        data.add("https://images.unsplash.com/photo-1541963463532-d68292c34b19?ixlib=rb-1.2.1&ixid=MXwxMjA3fDB8MHxleHBsb3JlLWZlZWR8M3x8fGVufDB8fHw%3D&w=1000&q=80")



        produtoList = ArrayList()
        productsAdapter = ProductsAdapter(requireContext(),produtoList)
        produtosRecyclerView = binding.recycler
        produtosRecyclerView.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        produtosRecyclerView.adapter = productsAdapter


        shopList = ArrayList()
        shopAdapter = ShopAdapter(requireContext(), shopList)
        comprasRecyclerView = binding.recyclerViewCompras
        comprasRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        comprasRecyclerView.adapter = shopAdapter




        loadUserData()

        loadProdutosExpirar()

        loadAllListaCompras()





        return view
    }



    private fun loadUserData() {
        val currentUser = firebaseAuth.currentUser
        val ref = FirebaseDatabase.getInstance().getReference("Users")
        ref.child(currentUser!!.uid)
            //.addListenerForSingleValueEvent(object : ValueEventListener {  so procura 1x
            .addValueEventListener(object : ValueEventListener {

                override fun onDataChange(snapshot: DataSnapshot) {

                    val name = snapshot.child("name").value.toString()
                    gv.nomeUtilizador = name
                    gv.currentList = "list_" + currentUser.uid
                    binding.teste.setText("Olá ${gv.nomeUtilizador}!")
                }

                override fun onCancelled(error: DatabaseError) {

                }
            })
    }

    private fun loadProdutosExpirar() {

        val listid = "list_" + gv.uidUtilizador
        val dbref = FirebaseDatabase.getInstance().getReference("Produtos")
        dbref.orderByChild("listaDe").equalTo(listid)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        produtoList.clear()
                        //produtoRecyclerView.adapter?.notifyDataSetChanged()
                        for (productSnapshot in snapshot.children) {

                            val produto = productSnapshot.getValue(Produto::class.java)
                            verificarInfoProdutos(produto!!)

                        }
                        if(produtoList.size>0){
                            // TODO: 12/01/2022 esconder array mostrar textview
                        }
                        else{

                        }




                    }

                }
                override fun onCancelled(error: DatabaseError) {
                }
            })
    }

    private fun verificarInfoProdutos(currentItem: Produto) {
        var dbrefInfo = FirebaseDatabase.getInstance().getReference("InfoProdutos")
        dbrefInfo.orderByChild("produtoID").equalTo(currentItem.produtoID)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {

                    if (snapshot.exists()) {
                        currentItem.listaInfoProduto.clear()

                        //InfoprodutosArrayList.clear()
                        for (productSnapshot in snapshot.children) {

                            val Infoproduto = productSnapshot.getValue(InfoProduto::class.java)

                            currentItem.adicionarInfoProduto(Infoproduto!!)

                        }
                        produtoList.add(currentItem)
                        produtosRecyclerView.adapter = productsAdapter

                    }
                }
                override fun onCancelled(error: DatabaseError) {
                }
            })
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
                        binding.semProdutosListaCompras.visibility = View.GONE
                        binding.recyclerViewCompras.visibility = View.VISIBLE

                    } else {
                        binding.semProdutosListaCompras.visibility = View.VISIBLE
                        binding.recyclerViewCompras.visibility = View.GONE
                    }
                    shopList.sortByDescending { it.urgente }
                    comprasRecyclerView.adapter = shopAdapter
                }

                override fun onCancelled(error: DatabaseError) {
                }
            })
    }

    override fun onStart() {
        super.onStart()

    }

    override fun onResume() {
        super.onResume()

    }


}