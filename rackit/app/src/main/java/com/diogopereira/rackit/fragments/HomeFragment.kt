package com.diogopereira.rackit.fragments

import android.content.Intent
import android.icu.text.IDNA
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.diogopereira.rackit.AuthenticationActivity
import com.diogopereira.rackit.GlobalClass
import com.diogopereira.rackit.adapters.ProductsAdapter
import com.diogopereira.rackit.adapters.ShopAdapter
import com.diogopereira.rackit.classes.InfoProduto
import com.diogopereira.rackit.classes.Produto
import com.diogopereira.rackit.classes.ProdutoComprar
import com.diogopereira.rackit.classes.ProdutoExpirar
import com.diogopereira.rackit.v2.databinding.FragmentHomeBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.fragment_home.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class HomeFragment : Fragment() {
    var firedatabase: FirebaseDatabase? = null
    private lateinit var firebaseAuth: FirebaseAuth
    lateinit var gv: GlobalClass
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!


    private lateinit var comprasRecyclerView: RecyclerView
    private lateinit var shopList: ArrayList<ProdutoComprar>
    private lateinit var shopAdapter: ShopAdapter


    private lateinit var produtoList: ArrayList<ProdutoExpirar>
    private lateinit var produtosRecyclerView: RecyclerView
    private lateinit var productsAdapter: ProductsAdapter

    override fun onResume() {
        super.onResume()
        //getProdutoData()
        //produtosArrayList.clear()
//        produtoList.clear()
//        loadProdutosExpirar()
        loadProdutosExpirar()


//        produtoRecyclerView.adapter = produtosAdapter(InfoprodutosArrayList)
//        getProdutoData()

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        gv = activity?.application as GlobalClass

        firebaseAuth = FirebaseAuth.getInstance()

        if (firebaseAuth == null) {
            startActivity(Intent(activity, AuthenticationActivity::class.java))

        }
        else{
            gv.uidUtilizador = firebaseAuth.currentUser!!.uid
            gv.emailUtilizador = firebaseAuth.currentUser!!.email.toString()
        }
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
        productsAdapter = ProductsAdapter(requireContext(), produtoList)
        produtosRecyclerView = binding.recycler
        produtosRecyclerView.layoutManager =
            LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        produtosRecyclerView.adapter = productsAdapter


        shopList = ArrayList()
        shopAdapter = ShopAdapter(requireContext(), shopList)
        comprasRecyclerView = binding.recyclerViewCompras
        comprasRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        comprasRecyclerView.adapter = shopAdapter


        loadUserData()
        loadAllListaCompras()



        binding.swipeLayout.setOnRefreshListener {
//            produtoRecyclerView.adapter = produtosAdapter(InfoprodutosArrayList)
//            getProdutoData()
            if (produtoList.size <= 0) {
                binding.semProdutosListaProdutos.visibility = View.VISIBLE
                binding.recycler.visibility = View.GONE
            }
            loadProdutosExpirar()

            binding.swipeLayout.isRefreshing = false
        }



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
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    produtoList.clear()

                    if (snapshot.exists()) {
                        //produtoRecyclerView.adapter?.notifyDataSetChanged()
                        for (productSnapshot in snapshot.children) {

                            val produto = productSnapshot.getValue(Produto::class.java)
                            verificarInfoProdutos(produto!!)

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
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {

                    if (snapshot.exists()) {
                        currentItem.listaInfoProduto.clear()

                        //InfoprodutosArrayList.clear()
                        for (productSnapshot in snapshot.children) {

                            val Infoproduto = productSnapshot.getValue(InfoProduto::class.java)
                            if (!Infoproduto!!.dataValidade.isNullOrEmpty()) {
                                var produtoAdicionar = ProdutoExpirar(
                                    produtoID = currentItem.produtoID,
                                    imagemProduto = currentItem.imagemProduto,
                                    codBarras = currentItem.codBarras,
                                    adicionadoEm = currentItem.adicionadoEm,
                                    infoProdutoID = Infoproduto!!.infoProdutoID,
                                    listaDe = currentItem.listaDe,
                                    listaInfoProduto = currentItem.listaInfoProduto,
                                    nomeProduto = currentItem.nomeProduto,
                                    precoCompra = Infoproduto!!.precoCompra,
                                    dataCompra = Infoproduto!!.dataCompra,
                                    dataValidadeAux = SimpleDateFormat("dd/MM/yyyy").parse(Infoproduto!!.dataValidade),
                                    dataValidade = Infoproduto.dataValidade
                                )
                                produtoList.add(produtoAdicionar)

                            } else {
                                var produtoAdicionar = ProdutoExpirar(
                                    produtoID = currentItem.produtoID,
                                    imagemProduto = currentItem.imagemProduto,
                                    codBarras = currentItem.codBarras,
                                    adicionadoEm = currentItem.adicionadoEm,
                                    infoProdutoID = Infoproduto!!.infoProdutoID,
                                    listaDe = currentItem.listaDe,
                                    listaInfoProduto = currentItem.listaInfoProduto,
                                    nomeProduto = currentItem.nomeProduto,
                                    precoCompra = Infoproduto!!.precoCompra,
                                    dataCompra = Infoproduto!!.dataCompra
                                )
                                produtoList.add(produtoAdicionar)

                            }
//                            val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH)

                            if (produtoList.size > 0) {
                                binding.semProdutosListaProdutos.visibility = View.GONE
                                binding.recycler.visibility = View.VISIBLE

                            }

                            currentItem.adicionarInfoProduto(Infoproduto!!)

                        }

                        produtoList.sortWith(compareBy<ProdutoExpirar,Date?>(nullsLast(), { it.dataValidadeAux }))

//                        produtoList.sort
//                        produtoList.sortByDescending { it.dataValidade }
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


}