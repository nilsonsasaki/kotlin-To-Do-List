package com.nilsonsasaki.kotlin_to_do_list

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.nilsonsasaki.kotlin_to_do_list.databinding.FragmentAddNewTaskBinding
import com.nilsonsasaki.kotlin_to_do_list.ui.models.TaskViewModel
import com.nilsonsasaki.kotlin_to_do_list.ui.models.TaskViewModelFactory

class add_new_task_Fragment : Fragment() {

    private var _binding : FragmentAddNewTaskBinding? = null
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
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentAddNewTaskBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btSaveButton.setOnClickListener{
            viewModel.addNewTask(
                binding.etTaskTitle.editText?.text.toString(),
                binding.etTaskDate.text.toString(),
                binding.etTaskStartingTime.text.toString(),
                binding.etTaskEndingTime.text.toString(),
                binding.etTaskPriority.text.toString(),
                binding.etTaskPeriodicity.text.toString(),
                binding.etTaskDescription.text.toString()
            )
            val action = add_new_task_FragmentDirections.actionAddNewTaskFragmentToAllTaskList()
            findNavController().navigate(action)

        }
    }

}