package com.rg.headernotes.ui.ViewPagerStart

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.rg.headernotes.R
import com.rg.headernotes.databinding.FragmentStartSecondBinding
import com.rg.headernotes.util.GraphActions
import com.rg.headernotes.util.Identifiers

class SecondStartFragment : Fragment() {

    private val binding by lazy { FragmentStartSecondBinding.inflate(layoutInflater) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.finishTV.setOnClickListener {
            Navigation
                .findNavController(requireActivity(),  Identifiers.navHostFragment)
                .navigate(GraphActions.vpStartToVpAuth)
            viewPagerStartFinish()
        }
    }

    private fun viewPagerStartFinish() {
        val sharedPref = requireActivity().getSharedPreferences("viewPager2Start", Context.MODE_PRIVATE)
        val editor = sharedPref.edit()
        editor.putBoolean("Finished", true)
        editor.apply()
    }
}