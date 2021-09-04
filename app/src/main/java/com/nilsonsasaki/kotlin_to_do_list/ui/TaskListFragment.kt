package com.nilsonsasaki.kotlin_to_do_list.ui

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.nilsonsasaki.kotlin_to_do_list.TaskApplication
import com.nilsonsasaki.kotlin_to_do_list.adapters.TaskItemAdapter
import com.nilsonsasaki.kotlin_to_do_list.databinding.FragmentAllTaskListBinding
import com.nilsonsasaki.kotlin_to_do_list.ui.models.TaskViewModel
import com.nilsonsasaki.kotlin_to_do_list.ui.models.TaskViewModelFactory

class TaskListFragment : Fragment() {

    private var _binding: FragmentAllTaskListBinding?= null

    private val binding get() = _binding!!

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
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val taskItemAdapter = TaskItemAdapter {
            val action = TaskListFragmentDirections.actionTaskListFragmentToTaskDetailsFragment(it.id)
            this.findNavController().navigate(action)
        }
        binding.rvTaskList.layoutManager = LinearLayoutManager(this.context)
        binding.rvTaskList.adapter = taskItemAdapter

        viewModel.allTasks.observe(this.viewLifecycleOwner){
            tasks-> tasks.let { taskItemAdapter.submitList(it) }
        }

        binding.floatingActionButton.setOnClickListener{
            val action = TaskListFragmentDirections.actionTaskListFragmentToEditTaskFragment()
            this.findNavController().navigate(action)
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}