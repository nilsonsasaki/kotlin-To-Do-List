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
import com.nilsonsasaki.kotlin_to_do_list.extensions.dateFormat
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
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

        binding.rvTaskList.layoutManager = LinearLayoutManager(this.context)
        binding.rvTaskList.addItemDecoration(
            DividerItemDecoration(
                requireContext(),
                LinearLayoutManager.VERTICAL
            )
        )
        toggleFilter()

        binding.floatingActionButton.setOnClickListener {
            val action = TaskListFragmentDirections.actionTaskListFragmentToEditTaskFragment(
                title = getString(R.string.title_create_task))
            this.findNavController().navigate(action)
        }
    }

    private fun toggleFilter() {
        val taskItemAdapter = TaskItemAdapter {
            val action =
                TaskListFragmentDirections.actionTaskListFragmentToTaskDetailsFragment(it.id)
            this.findNavController().navigate(action)
        }
        binding.rvTaskList.adapter = taskItemAdapter
        if (isShowingAllTasks) {
            viewModel.allTasks.observe(this.viewLifecycleOwner) { tasks ->
                tasks.let { taskItemAdapter.submitList(it) }
            }

        } else {
            val cal: Calendar = Calendar.getInstance()
            val day = cal.get(Calendar.DATE)
            val month = cal.get(Calendar.MONTH)
            val year = cal.get(Calendar.YEAR)
            viewModel.getByDate(dateFormat(day,month+1,year)).observe(this.viewLifecycleOwner) { tasks ->
                tasks.let { taskItemAdapter.submitList(it) }
            }
        }
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.layout_menu,menu)

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
                toggleFilter()
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