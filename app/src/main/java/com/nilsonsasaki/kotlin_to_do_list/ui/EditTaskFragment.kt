package com.nilsonsasaki.kotlin_to_do_list.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.nilsonsasaki.kotlin_to_do_list.TaskApplication
import com.nilsonsasaki.kotlin_to_do_list.databinding.FragmentEditTaskBinding
import com.nilsonsasaki.kotlin_to_do_list.ui.models.TaskViewModel
import com.nilsonsasaki.kotlin_to_do_list.ui.models.TaskViewModelFactory

class edit_task_Fragment : Fragment() {

    private var _binding : FragmentEditTaskBinding? = null
    private val binding get() = _binding!!

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
        _binding = FragmentEditTaskBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btSaveButton.setOnClickListener{
            viewModel.addNewTask(
                binding.etTaskTitle.editText?.text.toString(),
                binding.etTaskDate.editText?.text.toString(),
                binding.etTaskStartingTime.editText?.text.toString(),
                binding.etTaskEndingTime.editText?.text.toString(),
                binding.etTaskPriority.editText?.text.toString(),
                binding.etTaskPeriodicity.editText?.text.toString(),
                binding.etTaskDescription.editText?.text.toString()
            )
            val action = edit_task_FragmentDirections.actionAddNewTaskFragmentToAllTaskList()
            findNavController().navigate(action)

        }
    }

}