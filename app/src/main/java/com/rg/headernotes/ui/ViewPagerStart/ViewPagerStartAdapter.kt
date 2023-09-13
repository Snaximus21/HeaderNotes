package com.rg.headernotes.ui.ViewPagerStart

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter

class ViewPagerStartAdapter(
    list: ArrayList<Fragment>,
    manager: FragmentManager,
    lifecycle: Lifecycle
) : FragmentStateAdapter(manager, lifecycle) {

    private val fragmentList: ArrayList<Fragment> = list

    override fun getItemCount(): Int {
        return fragmentList.size
    }

    override fun createFragment(position: Int): Fragment {
        return fragmentList[position]
    }
}