package com.nilsonsasaki.kotlin_to_do_list.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.nilsonsasaki.kotlin_to_do_list.TaskApplication
import com.nilsonsasaki.kotlin_to_do_list.database.Task
import com.nilsonsasaki.kotlin_to_do_list.databinding.FragmentEditTaskBinding
import com.nilsonsasaki.kotlin_to_do_list.ui.models.TaskViewModel
import com.nilsonsasaki.kotlin_to_do_list.ui.models.TaskViewModelFactory

class EditTaskFragment : Fragment() {

    private var _binding: FragmentEditTaskBinding? = null
    private val binding get() = _binding!!

    private val navigationArgs: EditTaskFragmentArgs by navArgs()

    lateinit var task: Task

    private val viewModel: TaskViewModel by activityViewModels {
        TaskViewModelFactory(
            (activity?.application as TaskApplication).database
                .taskDao()
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentEditTaskBinding.inflate(inflater, container, false)
        return binding.root
    }

    private fun getSelectedTask() {

        viewModel.getTaskById(navigationArgs.itemId)
            .observe(this.viewLifecycleOwner) { selectedTask ->
                task = selectedTask
                bindSelectedTask(task)
            }

    }

    private fun bindSelectedTask(task: Task) {
        binding.apply {
            etTaskTitle.editText?.setText(task.title, TextView.BufferType.SPANNABLE)
            etTaskDate.editText?.setText(task.date, TextView.BufferType.SPANNABLE)
            etTaskStartingTime.editText?.setText(task.startingTime, TextView.BufferType.SPANNABLE)
            etTaskEndingTime.editText?.setText(task.endingTime, TextView.BufferType.SPANNABLE)
            etTaskPriority.editText?.setText(task.priority, TextView.BufferType.SPANNABLE)
            etTaskPeriodicity.editText?.setText(task.periodicity, TextView.BufferType.SPANNABLE)
            etTaskDescription.editText?.setText(task.description, TextView.BufferType.SPANNABLE)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fun loadTaskValues(){
            task=Task(navigationArgs.itemId,
                binding.etTaskTitle.editText?.text.toString(),
                binding.etTaskDate.editText?.text.toString(),
                binding.etTaskStartingTime.editText?.text.toString(),
                binding.etTaskEndingTime.editText?.text.toString(),
                binding.etTaskPriority.editText?.text.toString(),
                binding.etTaskPeriodicity.editText?.text.toString(),
                binding.etTaskDescription.editText?.text.toString())
        }

        if (navigationArgs.itemId > 0) {
            getSelectedTask()
            binding.btSaveButton.setOnClickListener {
                loadTaskValues()
                viewModel.updateTask(task)
                findNavController().navigateUp()
            }

        } else {

            binding.btSaveButton.setOnClickListener {
                loadTaskValues()
                viewModel.addNewTask(task)
                val action = EditTaskFragmentDirections.actionEditTaskFragmentToTaskListFragment()
                findNavController().navigate(action)
            }
        }
        binding.cancelButton.setOnClickListener {
            findNavController().navigateUp()
        }
    }

}