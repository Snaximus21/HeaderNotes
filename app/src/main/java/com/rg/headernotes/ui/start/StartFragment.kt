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
import com.rg.headernotes.util.navigate
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
            navigate(
                if (viewPagerStartFinish())
                    GraphActions.startToAuth
                else
                    GraphActions.startToVpStart
            )
        }
        return binding.root
    }

    /**
     * работу с префами лучше вынести в какой то отдельный класс, все таки не совсем это обязанность
     * фрагмента работать с хранилищем напрямую
     */
    private fun viewPagerStartFinish(): Boolean {
        val sharedPref = requireActivity().getSharedPreferences("viewPager2Start", Context.MODE_PRIVATE)
        return sharedPref.getBoolean("Finished", false)
    }
}