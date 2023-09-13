package com.rg.headernotes.ui.ViewPagerStart

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.viewpager2.widget.ViewPager2
import com.rg.headernotes.R
import com.rg.headernotes.databinding.FragmentStartFirstBinding
import com.rg.headernotes.databinding.FragmentVpStartBinding
import com.rg.headernotes.util.GraphActions
import com.rg.headernotes.util.Identifiers

class FirstStartFragment : Fragment() {

    private val binding by lazy { FragmentStartFirstBinding.inflate(layoutInflater) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.nextTV.setOnClickListener {
            activity?.findViewById<ViewPager2>(Identifiers.viewPagerStart)?.currentItem = 1
        }

        binding.skipTV.setOnClickListener {
            Navigation.findNavController(requireActivity(), Identifiers.navHostFragment)
                .navigate(GraphActions.vpStartToVpAuth)
            viewPager2StartFinish()
        }

    }

    private fun viewPager2StartFinish() {
        val sharedPref = requireActivity().getSharedPreferences("viewPager2Start", Context.MODE_PRIVATE)
        val editor = sharedPref.edit()
        editor.putBoolean("Finished", true)
        editor.apply()
    }
}