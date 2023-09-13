package com.rg.headernotes.ui.ViewPagerStart

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.rg.headernotes.databinding.FragmentVpStartBinding

class ViewPagerStartFragment  : Fragment() {
    private val binding by lazy { FragmentVpStartBinding.inflate(layoutInflater) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.viewPagerStart.adapter = ViewPagerStartAdapter(
            arrayListOf(FirstStartFragment(), SecondStartFragment()),
            requireActivity().supportFragmentManager,
            lifecycle
        )
    }
}