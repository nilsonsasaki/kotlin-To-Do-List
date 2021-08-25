package com.nilsonsasaki.kotlin_to_do_list.ui

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.nilsonsasaki.kotlin_to_do_list.databinding.FragmentTodaysTaskListBinding

class TodaysTaskList : Fragment() {

    private var _binding: FragmentTodaysTaskListBinding?= null
    private val binding get() = _binding!!
    private lateinit var todaysTasks: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTodaysTaskListBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        todaysTasks = binding.rvTodaysTasks
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}