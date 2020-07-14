package com.globant.harrypotterapp.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.globant.harrypotterapp.fragment.CharactersFragment
import com.globant.harrypotterapp.fragment.HouseDetailFragment

class ViewPagerAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle) :
    FragmentStateAdapter(fragmentManager, lifecycle) {

    private var fragments: List<Fragment> = listOf(HouseDetailFragment(), CharactersFragment())

    override fun createFragment(position: Int): Fragment = fragments[position]

    override fun getItemCount(): Int = fragments.size
}
