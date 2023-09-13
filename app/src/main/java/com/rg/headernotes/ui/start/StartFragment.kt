package com.rg.headernotes.ui.start

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.rg.headernotes.R
import com.rg.headernotes.databinding.FragmentStartBinding
import com.rg.headernotes.util.GraphActions
import com.rg.headernotes.util.Identifiers
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class StartFragment : Fragment() {
    private val binding by lazy { FragmentStartBinding.inflate(layoutInflater) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        CoroutineScope(Dispatchers.Main).launch {
            while (!this@StartFragment.isAdded) delay(100)
            if (viewPagerStartFinish()) {
                Navigation.findNavController(requireActivity(), Identifiers.navHostFragment)
                    .navigate(GraphActions.startToAuth)
            } else {
                Navigation.findNavController(requireActivity(), Identifiers.navHostFragment)
                    .navigate(GraphActions.startToVpStart)
            }
        }
        return binding.root
    }

    private fun viewPagerStartFinish(): Boolean {
        val sharedPref = requireActivity().getSharedPreferences("viewPager2Start", Context.MODE_PRIVATE)
        return sharedPref.getBoolean("Finished", false)
    }
}