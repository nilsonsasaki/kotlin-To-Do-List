package com.nilsonsasaki.kotlin_to_do_list.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.nilsonsasaki.kotlin_to_do_list.TaskApplication
import com.nilsonsasaki.kotlin_to_do_list.database.Task
import com.nilsonsasaki.kotlin_to_do_list.databinding.FragmentTaskDetailsBinding
import com.nilsonsasaki.kotlin_to_do_list.ui.models.TaskViewModel
import com.nilsonsasaki.kotlin_to_do_list.ui.models.TaskViewModelFactory

class TaskDetailsFragment : Fragment() {

    private val navigationArgs: TaskDetailsFragmentArgs by navArgs()
    lateinit var task :Task

    private var _binding: FragmentTaskDetailsBinding? = null
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
        // Inflate the layout for this fragment
        _binding = FragmentTaskDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val id = navigationArgs.taskId
        viewModel.getTaskById(id).observe(this.viewLifecycleOwner){
            selectedTask -> task = selectedTask
            bind(task)
        }
        binding.fabEditTask.setOnClickListener {
            val action  = TaskDetailsFragmentDirections.actionTaskDetailsToEditTaskFragment(id)
            findNavController().navigate(action)
        }
        binding.btReturnButton.setOnClickListener {
            findNavController().navigateUp()
        }
        binding.btDeleteButton.setOnClickListener {
            deleteTask()
            findNavController().navigateUp()
        }
    }

    private fun deleteTask(){
        viewModel.deleteTask(task)
    }

    private fun bind (task:Task){
        binding.tvDate.text = task.date
        binding.tvStartingTime.text = task.startingTime
        binding.tvEndingTime.text = task.endingTime
        binding.tvTaskTitle.text = task.title
        binding.tvTaskDescription.text = task.description
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}