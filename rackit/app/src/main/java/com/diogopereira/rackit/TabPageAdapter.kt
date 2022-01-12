package com.diogopereira.rackit

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.diogopereira.rackit.fragments.HomeFragment
import com.diogopereira.rackit.fragments.ProductsFragment
import com.diogopereira.rackit.fragments.SettingsFragment
import com.diogopereira.rackit.fragments.ShoppingListFragment

class TabPageAdapter(activity:FragmentActivity, private val tabCount:Int): FragmentStateAdapter(activity){
    override fun getItemCount(): Int = tabCount
    override fun createFragment(position: Int): Fragment {
        return when(position)
        {
            0-> HomeFragment()
            1-> ProductsFragment()
            2-> ShoppingListFragment()
            3-> SettingsFragment()
            else -> HomeFragment()
        }
    }
}