package com.diogopereira.rackit.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.diogopereira.rackit.AddProductActivity
import com.diogopereira.rackit.AuthenticationActivity
import com.diogopereira.rackit.GlobalClass
import com.diogopereira.rackit.adapters.SettingsAdapter
import com.diogopereira.rackit.adapters.ShopAdapter
import com.diogopereira.rackit.classes.ProdutoComprar
import com.diogopereira.rackit.classes.Settings
import com.diogopereira.rackit.v2.R
import com.diogopereira.rackit.v2.databinding.FragmentSettingsBinding
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class SettingsFragment : Fragment() {
    var firedatabase : FirebaseDatabase? = null
    private lateinit var firebaseAuth: FirebaseAuth
    lateinit var gv: GlobalClass
    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!
    private lateinit var settingsList: ArrayList<Settings>
    private lateinit var definicoesAdapter: SettingsAdapter
    private lateinit var definicoesRecyclerView: RecyclerView


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        gv = activity?.application as GlobalClass

        firebaseAuth = FirebaseAuth.getInstance()

        _binding = FragmentSettingsBinding.inflate(inflater,container,false)
        val view = binding.root

//        val currentUser = firebaseAuth.currentUser
//        if (currentUser != null) {
//
//
//
//
//
//        } else {
//            startActivity(Intent(activity, AuthenticationActivity::class.java))
//
//        }
        settingsList = ArrayList()
        gerarLista()
        definicoesAdapter = SettingsAdapter(requireContext(), settingsList)
        definicoesRecyclerView = binding.recyclerViewDefinicoes
        definicoesRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        definicoesRecyclerView.adapter = definicoesAdapter



        return view
    }

    private fun gerarLista() {
        settingsList.clear()
        settingsList.add(Settings(id = "Conta", nomeSetting = "Sua conta", descricao = "Veja a seguran??a da sua conta, baixe um arquivo com seus dados ou saiba mais sobre as op????es de desativa????o da conta", imagemSetting = R.drawable.ic_baseline_account_circle_24))
        settingsList.add(Settings(id = "Seguran??a",nomeSetting = "Seguran??a e acesso ?? conta", descricao = "Gira a seguran??a da sua conta e monitorize o uso dela, inclusive os aplicativos conectados.", imagemSetting = R.drawable.ic_baseline_lock_24))
        settingsList.add(Settings(id = "Privacidade",nomeSetting = "Privacidade e seguran??a", descricao = "Consulte as informa????es que v?? e partilha no RackIT", imagemSetting = R.drawable.ic_baseline_security_24))
        settingsList.add(Settings(id = "Notificacoes",nomeSetting = "Notifica????es", descricao = "Selecione os tipos de informa????es que recebe sobre atividades, interesses e recomenda????es", imagemSetting = R.drawable.ic_baseline_notifications_24))
        settingsList.add(Settings(id = "Acessibilidade",nomeSetting ="Acessibilidade, exibi????o e idiomas", descricao = "Selecione a forma como o conteudo ?? exibido", imagemSetting = R.drawable.ic_baseline_remove_red_eye_24))
        settingsList.add(Settings(id = "Recursos adicionais",nomeSetting = "Recursos adicionais", descricao = "Verifique informa????es uteis do RackIT", imagemSetting = R.drawable.ic_baseline_more_horiz_24))
        settingsList.add(Settings(id = "Logout",nomeSetting = "Terminar sess??o", descricao = "Termine sess??o em seguran??a", imagemSetting = R.drawable.ic_baseline_power_settings_new_24))



    }

    override fun onStart() {
        super.onStart()

    }
    override fun onResume() {
        super.onResume()

    }


}