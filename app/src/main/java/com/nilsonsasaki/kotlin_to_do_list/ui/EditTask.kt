package com.nilsonsasaki.kotlin_to_do_list.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.nilsonsasaki.kotlin_to_do_list.databinding.FragmentEditTaskBinding


class EditTask : Fragment() {

    private var _binding: FragmentEditTaskBinding ?= null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentEditTaskBinding.inflate(inflater,container,false)
        val view = binding.root
        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}