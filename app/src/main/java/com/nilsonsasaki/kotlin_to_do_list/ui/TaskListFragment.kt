package com.nilsonsasaki.kotlin_to_do_list.ui

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.nilsonsasaki.kotlin_to_do_list.R
import com.nilsonsasaki.kotlin_to_do_list.TaskApplication
import com.nilsonsasaki.kotlin_to_do_list.adapters.TaskItemAdapter
import com.nilsonsasaki.kotlin_to_do_list.databinding.FragmentAllTaskListBinding
import com.nilsonsasaki.kotlin_to_do_list.ui.models.TaskViewModel
import com.nilsonsasaki.kotlin_to_do_list.ui.models.TaskViewModelFactory
import java.util.*

class TaskListFragment : Fragment() {

    private var _binding: FragmentAllTaskListBinding? = null

    private val binding get() = _binding!!
    private var isShowingAllTasks: Boolean = true

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
        setHasOptionsMenu(true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val taskItemAdapter = TaskItemAdapter {
            val action =
                TaskListFragmentDirections.actionTaskListFragmentToTaskDetailsFragment(it.id)
            this.findNavController().navigate(action)
        }
        binding.rvTaskList.layoutManager = LinearLayoutManager(this.context)
        binding.rvTaskList.adapter = taskItemAdapter
        binding.rvTaskList.addItemDecoration(
            DividerItemDecoration(
                requireContext(),
                LinearLayoutManager.VERTICAL
            )
        )
        if (isShowingAllTasks) {
            viewModel.allTasks.observe(this.viewLifecycleOwner) { tasks ->
                tasks.let { taskItemAdapter.submitList(it) }
            }

        } else {
            val cal: Calendar = Calendar.getInstance()
            val day = cal.get(Calendar.DATE)
            val month = cal.get(Calendar.MONTH)
            val year = cal.get(Calendar.YEAR)
            viewModel.getByDate("$day/$month/$year").observe(this.viewLifecycleOwner) { tasks ->
                tasks.let { taskItemAdapter.submitList(it) }
            }
        }

        binding.floatingActionButton.setOnClickListener {
            val action = TaskListFragmentDirections.actionTaskListFragmentToEditTaskFragment(
                title = getString(R.string.title_create_task))
            this.findNavController().navigate(action)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)

        val filterList = menu.findItem(R.id.action_filter_results)
        setMenuText(filterList)
    }

    private fun setMenuText(menuItem: MenuItem?) {
        if (menuItem == null) return

        if (isShowingAllTasks) {
            menuItem.title = getString(R.string.action_all_tasks)
        } else {
            menuItem.title = getString(R.string.action_today_tasks)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId){
            R.id.action_filter_results ->{
                isShowingAllTasks=!isShowingAllTasks
                setMenuText(item)
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}