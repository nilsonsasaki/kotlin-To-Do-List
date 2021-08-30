package com.nilsonsasaki.kotlin_to_do_list.ui

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.coroutineScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.nilsonsasaki.kotlin_to_do_list.TaskApplication
import com.nilsonsasaki.kotlin_to_do_list.adapters.TaskItemAdapter
import com.nilsonsasaki.kotlin_to_do_list.databinding.FragmentAllTaskListBinding
import com.nilsonsasaki.kotlin_to_do_list.ui.models.TaskViewModel
import com.nilsonsasaki.kotlin_to_do_list.ui.models.TaskViewModelFactory
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class AllTaskList : Fragment() {

    private var _binding: FragmentAllTaskListBinding?= null

    private val binding get() = _binding!!

    private lateinit var recyclerView: RecyclerView

    private val viewModel: TaskViewModel by activityViewModels {
       TaskViewModelFactory(
            (activity?.application as TaskApplication).database.taskDao()
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAllTaskListBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        recyclerView = binding.rvTodaysTasks
        recyclerView.layoutManager=LinearLayoutManager(requireContext())
        val taskItemAdapter = TaskItemAdapter {}
        recyclerView.adapter = taskItemAdapter
        lifecycle.coroutineScope.launch {
            viewModel.getAllTasks().collect() {
                taskItemAdapter.submitList(it)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}