package com.diogopereira.rackit.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.diogopereira.rackit.GlobalClass
import com.diogopereira.rackit.adapters.produtosAdapter
import com.diogopereira.rackit.classes.InfoProduto
import com.diogopereira.rackit.classes.Produto
import com.diogopereira.rackit.v2.databinding.FragmentProductsBinding
import com.google.firebase.database.*

class ProductsFragment : Fragment() {
    private var _binding: FragmentProductsBinding? = null
    private val binding get() = _binding!!
    lateinit var gv: GlobalClass

    private lateinit var dbref: DatabaseReference
    private lateinit var dbrefInfo : DatabaseReference
    private lateinit var InfoprodutosArrayList: ArrayList<Produto>
    private lateinit var ProdutoArrayList: ArrayList<Produto>

    private lateinit var produtoRecyclerView: RecyclerView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onResume() {
        super.onResume()
        //getProdutoData()
        //produtosArrayList.clear()
        produtoRecyclerView.adapter?.notifyDataSetChanged()
        getProdutoData()

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

        produtoRecyclerView = binding.recyclerView
        produtoRecyclerView.layoutManager = LinearLayoutManager(activity)
        produtoRecyclerView.setHasFixedSize(true)

        InfoprodutosArrayList = arrayListOf<Produto>()
        ProdutoArrayList = arrayListOf<Produto>()
        // TODO: 28/12/2021 Recyler view com os produtos na lista de produtos


        return view

    }

    private fun getProdutoData() {
        ProdutoArrayList.clear()

        val listid = "list_" + gv.uidUtilizador
        dbref = FirebaseDatabase.getInstance().getReference("Produtos")
        dbref.orderByChild("listaDe").equalTo(listid)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        //produtoRecyclerView.adapter?.notifyDataSetChanged()
                        produtoRecyclerView.adapter?.notifyDataSetChanged()
                        ProdutoArrayList.clear()
                        for (productSnapshot in snapshot.children) {

                            val produto = productSnapshot.getValue(Produto::class.java)
                            ProdutoArrayList.add(produto!!)
                            if(produto == null){
                                binding.semProduto.visibility = View.VISIBLE
                                binding.semProduto.setText("Ups, parece que ainda não tem nenhum produto na lista. Comece por adicionar um")
                            }
                            // TODO: 03/01/2022 Obter dados do info produtos
                            produtoRecyclerView.adapter?.notifyDataSetChanged()

                        }
                        getProdutosArrayList(ProdutoArrayList)

                    }

                }


                override fun onCancelled(error: DatabaseError) {
                    binding.semProduto.visibility = View.VISIBLE
                    binding.semProduto.setText("Ups, parece que ainda não tem nenhum produto na lista. Comece por adicionar um")
                }
            })


    }

    private fun getProdutosArrayList(produtoArrayList: ArrayList<Produto>) {
        //InfoprodutosArrayList.clear()
        for(produto in produtoArrayList){
            InfoprodutosArrayList.clear()
            dbrefInfo = FirebaseDatabase.getInstance().getReference("InfoProdutos")
            dbrefInfo.orderByChild("produtoID").equalTo(produto.produtoID)
                .addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (snapshot.exists()) {
                            //InfoprodutosArrayList.clear()
                            produtoRecyclerView.adapter?.notifyDataSetChanged()
                            for (productSnapshot in snapshot.children) {

                                val Infoproduto = productSnapshot.getValue(InfoProduto::class.java)

                                if(InfoprodutosArrayList.contains(produto)){

                                }
                                produto.adicionarInfoProduto(Infoproduto!!)



                            }
                            InfoprodutosArrayList.add(produto!!)
                            produtoRecyclerView.adapter?.notifyDataSetChanged()
                            if (InfoprodutosArrayList.isEmpty()) {
                                binding.semProduto.visibility = View.VISIBLE
                                binding.semProduto.setText("Ups, parece que ainda não tem nenhum produto na lista. Comece por adicionar um")
                            } else {
                                binding.semProduto.visibility = View.GONE
                                produtoRecyclerView.adapter = produtosAdapter(InfoprodutosArrayList)
                                produtoRecyclerView.adapter?.notifyDataSetChanged()

                            }
                        }

                    }


                    override fun onCancelled(error: DatabaseError) {

                    }


                })
        }
    }


}