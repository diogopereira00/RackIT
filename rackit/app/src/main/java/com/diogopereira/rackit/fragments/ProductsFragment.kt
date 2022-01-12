package com.diogopereira.rackit.fragments

import android.opengl.Visibility
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isEmpty
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.diogopereira.rackit.GlobalClass
import com.diogopereira.rackit.adapters.ProductsAdapter
import com.diogopereira.rackit.adapters.ProductsAdapterVertical
import com.diogopereira.rackit.adapters.produtosAdapter
import com.diogopereira.rackit.classes.InfoProduto
import com.diogopereira.rackit.classes.Produto
import com.diogopereira.rackit.v2.databinding.FragmentProductsBinding
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.fragment_products.*

class ProductsFragment : Fragment() {
    private var _binding: FragmentProductsBinding? = null
    private val binding get() = _binding!!
    lateinit var gv: GlobalClass

//    private lateinit var dbref: DatabaseReference
//    private lateinit var dbrefInfo: DatabaseReference
//    private lateinit var InfoprodutosArrayList: ArrayList<Produto>
//    private lateinit var ProdutoArrayList: ArrayList<Produto>
    private var haProdutos: Boolean = false
//    private lateinit var produtoRecyclerView: RecyclerView


    private lateinit var produtoList: ArrayList<Produto>
    private lateinit var produtosRecyclerView: RecyclerView
    private lateinit var productsAdapter : ProductsAdapterVertical
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onResume() {
        super.onResume()
        //getProdutoData()
        //produtosArrayList.clear()
//        produtoList.clear()
//        loadProdutosExpirar()



//        produtoRecyclerView.adapter = produtosAdapter(InfoprodutosArrayList)
//        getProdutoData()

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    override fun onStart() {
        super.onStart()

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProductsBinding.inflate(inflater, container, false)
        val view = binding.root
        gv = activity?.application as GlobalClass

        produtoList = ArrayList()
        productsAdapter = ProductsAdapterVertical(requireContext(),produtoList)
        produtosRecyclerView = binding.recyclerView
        produtosRecyclerView.layoutManager = LinearLayoutManager(activity)
        produtosRecyclerView.adapter = productsAdapter
        produtoList.clear()

        loadProdutosExpirar()

//        produtoRecyclerView = binding.recyclerView
//        produtoRecyclerView.layoutManager = LinearLayoutManager(activity)
//        InfoprodutosArrayList = arrayListOf<Produto>()
//
//        produtoRecyclerView.adapter = produtosAdapter(InfoprodutosArrayList)
//
//        produtoRecyclerView.setHasFixedSize(true)
//
//        ProdutoArrayList = arrayListOf<Produto>()

        binding.swipeLayout.setOnRefreshListener {
//            produtoRecyclerView.adapter = produtosAdapter(InfoprodutosArrayList)
//            getProdutoData()
            if(produtoList.size<=0) {
                binding.semProduto.visibility = View.VISIBLE
                binding.recyclerView.visibility = View.GONE
            }
            loadProdutosExpirar()

            binding.swipeLayout.isRefreshing = false
        }

        return view

    }


    private fun loadProdutosExpirar() {
//        if(produtoList.size<=0){
//            binding.semProduto.visibility = View.VISIBLE
//            binding.recyclerView.visibility =  View.GONE
//        }
        val listid = "list_" + gv.uidUtilizador
        val dbref = FirebaseDatabase.getInstance().getReference("Produtos")
        dbref.orderByChild("listaDe").equalTo(listid)
            .addValueEventListener(object : ValueEventListener {
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
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {

                    if (snapshot.exists()) {
                        currentItem.listaInfoProduto.clear()

                        //InfoprodutosArrayList.clear()
                        for (productSnapshot in snapshot.children) {

                            val Infoproduto = productSnapshot.getValue(InfoProduto::class.java)

                            currentItem.adicionarInfoProduto(Infoproduto!!)

                        }

                        if(produtoList.contains(currentItem)){
                            produtoList.remove(currentItem)
                        }

//                        if(produtoList.size>0){
//                            binding.semProduto.visibility = View.GONE
//                            binding.recyclerView.visibility = View.VISIBLE
//                        }
//                        else{
//                            binding.semProduto.visibility = View.VISIBLE
//                            binding.recyclerView.visibility = View.GONE
//                        }
                        produtoList.add(currentItem)
                        if(produtoList.size>0){
                            binding.semProduto.visibility = View.GONE
                            binding.recyclerView.visibility =  View.VISIBLE
                        }
                        produtoList.sortBy { it.nomeProduto }
                        produtosRecyclerView.adapter = productsAdapter

                    }
                }
                override fun onCancelled(error: DatabaseError) {
                }
            })

    }


//    private fun getProdutoData() {
//
//        val listid = "list_" + gv.uidUtilizador
//        dbref = FirebaseDatabase.getInstance().getReference("Produtos")
//        dbref.orderByChild("listaDe").equalTo(listid)
//            .addValueEventListener(object : ValueEventListener {
//                override fun onDataChange(snapshot: DataSnapshot) {
//                    haProdutos = true
//                    if (snapshot.exists()) {
//                        //produtoRecyclerView.adapter?.notifyDataSetChanged()
//                        ProdutoArrayList.clear()
//                        for (productSnapshot in snapshot.children) {
//
//                            val produto = productSnapshot.getValue(Produto::class.java)
//                            ProdutoArrayList.add(produto!!)
//                            if (produto == null) {
//                                binding.semProduto.visibility = View.VISIBLE
//                                binding.semProduto.setText("Ups, parece que ainda não tem nenhum produto na lista. Comece por adicionar um")
//                            }
////                            produtoRecyclerView.adapter?.notifyDataSetChanged()
//
//                        }
//                        getProdutosArrayList(ProdutoArrayList)
//                        produtoRecyclerView.adapter?.notifyDataSetChanged()
//
//                    }
//
//                }
//
//
//                override fun onCancelled(error: DatabaseError) {
//                    binding.semProduto.visibility = View.VISIBLE
//                    binding.semProduto.setText("Ups, parece que ainda não tem nenhum produto na lista. Comece por adicionar um")
//                }
//            })
//
//
//    }
//
//    private fun getProdutosArrayList(produtoArrayList: ArrayList<Produto>) {
//        //InfoprodutosArrayList.clear()
//        for (produto in produtoArrayList) {
//            InfoprodutosArrayList.clear()
//            dbrefInfo = FirebaseDatabase.getInstance().getReference("InfoProdutos")
//            dbrefInfo.orderByChild("produtoID").equalTo(produto.produtoID)
//                .addValueEventListener(object : ValueEventListener {
//                    override fun onDataChange(snapshot: DataSnapshot) {
//
//                        produtoRecyclerView.adapter = produtosAdapter(InfoprodutosArrayList)
//                        if (snapshot.exists()) {
//                            //InfoprodutosArrayList.clear()
//                            for (productSnapshot in snapshot.children) {
//
//                                val Infoproduto = productSnapshot.getValue(InfoProduto::class.java)
//
//                                produto.adicionarInfoProduto(Infoproduto!!)
//
//
//                            }
//                            InfoprodutosArrayList.add(produto!!)
//                            if (InfoprodutosArrayList.isEmpty()) {
//                                binding.semProduto.visibility = View.VISIBLE
//                                binding.semProduto.setText("Ups, parece que ainda não tem nenhum produto na lista. Comece por adicionar um")
//                            } else {
//                                binding.semProduto.visibility = View.GONE
//                                produtoRecyclerView.layoutManager = LinearLayoutManager(activity)
//                                produtoRecyclerView.adapter = produtosAdapter(InfoprodutosArrayList)
//
//                            }
//                        }
////                        produtoRecyclerView.adapter?.notifyDataSetChanged()
//
//                    }
//
//
//                    override fun onCancelled(error: DatabaseError) {
//
//                    }
//
//
//                })
//        }
//    }


}