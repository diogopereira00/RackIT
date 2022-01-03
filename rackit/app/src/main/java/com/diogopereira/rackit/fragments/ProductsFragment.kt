package com.diogopereira.rackit.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.diogopereira.rackit.GlobalClass
import com.diogopereira.rackit.adapters.produtosAdapter
import com.diogopereira.rackit.classes.InfoProduto
import com.diogopereira.rackit.classes.Produto
import com.diogopereira.rackit.v2.R
import com.diogopereira.rackit.v2.databinding.ActivityAddProductBinding
import com.diogopereira.rackit.v2.databinding.FragmentHomeBinding
import com.diogopereira.rackit.v2.databinding.FragmentProductsBinding
import com.google.firebase.database.*

class ProductsFragment : Fragment() {
    private var _binding: FragmentProductsBinding? = null
    private val binding get() = _binding!!
    lateinit var gv: GlobalClass

    private lateinit var dbref :DatabaseReference
    private lateinit var produtosArrayList: ArrayList<Produto>
    private lateinit var  produtoRecyclerView: RecyclerView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProductsBinding.inflate(inflater,container,false)
        val view = binding.root
        gv = activity?.application as GlobalClass

        produtoRecyclerView = binding.recyclerView
        produtoRecyclerView.layoutManager = LinearLayoutManager(activity)
        produtoRecyclerView.setHasFixedSize(true)

        produtosArrayList = arrayListOf<Produto>()
        getProdutoData()
        // TODO: 28/12/2021 Recyler view com os produtos na lista de produtos


        return view

    }

    private fun getProdutoData() {
        val listid = "list_" + gv.uidUtilizador
        dbref  = FirebaseDatabase.getInstance().getReference("Produtos")
        dbref.orderByChild("listaDe").equalTo(listid)
            .addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){
                    for(productSnapshot in snapshot.children){
                        val produto = productSnapshot.getValue(Produto::class.java)
                        produtosArrayList.add(produto!!)
                    }
                    if(produtosArrayList.isEmpty()){
                        binding.semProduto.visibility = View.VISIBLE
                        binding.semProduto.setText("Ups, parece que ainda não tem nenhum produto na lista. Comece por adicionar um")
                        // TODO: 01/01/2022 quando adiciono items nao ta a dar refresh 
                        // TODO: 01/01/2022 adicionar imagem 
                    }
                    else{
                        binding.semProduto.visibility = View.GONE

<<<<<<< Updated upstream
                        produtoRecyclerView.adapter = produtosAdapter(produtosArrayList)
=======
                        // TODO: 03/01/2022 Obter dados do info produtos
                        getInfoProdutos()
                        //var teste = InfoProduto(produtoID = produto!!.produtoID)
                        //produto!!.adicionarInfoProduto(InfoProduto(dataCompra = "", dataValidade = "teste", precoCompra = "2,3", produtoID = produto!!.produtoID))
                            produtosArrayList.add(produto!!)
                            produtoRecyclerView.adapter?.notifyDataSetChanged()


                        if (produtosArrayList.isEmpty()) {
                            binding.semProduto.visibility = View.VISIBLE
                            binding.semProduto.setText("Ups, parece que ainda não tem nenhum produto na lista. Comece por adicionar um")
                        } else {
                            binding.semProduto.visibility = View.GONE
                            produtoRecyclerView.adapter = produtosAdapter(produtosArrayList)
                            produtoRecyclerView.adapter?.notifyDataSetChanged()
>>>>>>> Stashed changes

                    }
                }

            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })


}

    private fun getInfoProdutos() {
    }
}