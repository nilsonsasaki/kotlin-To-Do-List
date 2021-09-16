package com.nilsonsasaki.kotlin_to_do_list.ui

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.nilsonsasaki.kotlin_to_do_list.TaskApplication
import com.nilsonsasaki.kotlin_to_do_list.database.Task
import com.nilsonsasaki.kotlin_to_do_list.databinding.FragmentEditTaskBinding
import com.nilsonsasaki.kotlin_to_do_list.R
import com.nilsonsasaki.kotlin_to_do_list.extensions.dateFormat
import com.nilsonsasaki.kotlin_to_do_list.extensions.text
import com.nilsonsasaki.kotlin_to_do_list.extensions.timeFormat
import com.nilsonsasaki.kotlin_to_do_list.ui.models.TaskViewModel
import com.nilsonsasaki.kotlin_to_do_list.ui.models.TaskViewModelFactory
import java.util.*

class EditTaskFragment : Fragment(), DatePickerDialog.OnDateSetListener,
    TimePickerDialog.OnTimeSetListener {

    private var _binding: FragmentEditTaskBinding? = null
    private val binding get() = _binding!!

    private val navigationArgs: EditTaskFragmentArgs by navArgs()

    lateinit var task: Task
    private lateinit var errorsMap: Map<String, String>

    private var day = 0
    private var month = 0
    private var year = 0
    private var hour = 0
    private var minute = 0

    private var isStartingTime: Boolean = false
    private var hasError: Boolean = false
    private var isEditingTask: Boolean = false

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getDateAndTimeCalendar()

        isEditingTask = (navigationArgs.itemId > 0)
        if (!isEditingTask && viewModel.hasChangedValues.value == false) {
            viewModel.setEditingTask(
                Task(
                    id = 0,
                    title = "",
                    date = dateFormat(day, month + 1, year),
                    startingTime = timeFormat(hour, minute),
                    endingTime = timeFormat(hour + 1, minute),
                    priority = "Normal",
                    description = ""
                )
            )
        }
        if (isEditingTask && viewModel.hasChangedValues.value == false) {
            getSelectedTask()
        } else {
            getEditingTask()
        }
        setOnClickListeners()
    }

    private fun getEditingTask() {
        viewModel.editingTask.observe(viewLifecycleOwner, { editingTask ->
            task = editingTask
            bindTask(task)
        })
    }

    private fun setOnClickListeners() {
        binding.etTaskDate.editText?.setOnClickListener {
            DatePickerDialog(requireContext(), this, year, month, day).show()
        }

        binding.etTaskStartingTime.editText?.setOnClickListener {
            isStartingTime = true
            TimePickerDialog(requireContext(), this, hour, minute, true).show()
        }

        binding.etTaskEndingTime.editText?.setOnClickListener {
            isStartingTime = false
            TimePickerDialog(requireContext(), this, hour, minute, true).show()
        }
        binding.btSaveButton.setOnClickListener {
            loadTaskValues()
            setErrorObserver()
            checkForErrors()
            if (!hasError && isEditingTask) {

                viewModel.updateTask(task)
                clearEditingValues()
                findNavController().navigateUp()
            } else if (!hasError && !isEditingTask) {
                viewModel.addNewTask(task)
                clearEditingValues()
                val action =
                    EditTaskFragmentDirections.actionEditTaskFragmentToTaskListFragment()
                findNavController().navigate(action)
            }
        }

        binding.cancelButton.setOnClickListener {
            findNavController().navigateUp()
            clearEditingValues()
        }
    }

    private fun clearEditingValues() {
        viewModel.clearErrorsMap()
        viewModel.setHasChangedValues(false)
        viewModel.setHasInputErrors(false)
    }

    private fun getDateAndTimeCalendar() {

        val cal: Calendar = Calendar.getInstance()
        day = cal.get(Calendar.DAY_OF_MONTH)
        month = cal.get(Calendar.MONTH)
        year = cal.get(Calendar.YEAR)
        hour = cal.get(Calendar.HOUR_OF_DAY)
        minute = cal.get(Calendar.MINUTE)
    }

    private fun getSelectedTask() {
        viewModel.getTaskById(navigationArgs.itemId)
            .observe(this.viewLifecycleOwner) { selectedTask ->
                task = selectedTask
                bindTask(task)
            }
    }

    private fun bindTask(task: Task) {
        binding.apply {
            etTaskTitle.editText?.setText(task.title, TextView.BufferType.SPANNABLE)
            etTaskDate.editText?.setText(task.date, TextView.BufferType.SPANNABLE)
            etTaskStartingTime.editText?.setText(task.startingTime, TextView.BufferType.SPANNABLE)
            etTaskEndingTime.editText?.setText(task.endingTime, TextView.BufferType.SPANNABLE)
            etTaskPriority.editText?.setText(task.priority, TextView.BufferType.SPANNABLE)
            etTaskDescription.editText?.setText(task.description, TextView.BufferType.SPANNABLE)
        }
        val items = listOf("Very Low", "Low", "Normal", "High", "Very High")
        val adapter = ArrayAdapter(requireContext(), R.layout.dropdown_menu, items.reversed())
        (binding.etTaskPriority.editText as? AutoCompleteTextView)?.setAdapter(adapter)
    }

    private fun loadTaskValues() {
        task = Task(
            navigationArgs.itemId,
            binding.etTaskTitle.editText?.text.toString(),
            binding.etTaskDate.editText?.text.toString(),
            binding.etTaskStartingTime.editText?.text.toString(),
            binding.etTaskEndingTime.editText?.text.toString(),
            binding.etTaskPriority.editText?.text.toString(),
            binding.etTaskDescription.editText?.text.toString()
        )
    }

    private fun setErrorObserver() {
        viewModel.hasInputErrors.observe(this.viewLifecycleOwner) { newValue ->
            hasError = newValue
        }
        viewModel.inputErrorsMap.observe(this.viewLifecycleOwner) { newMap ->
            errorsMap = newMap
            for (currentEditText in errorsMap.keys) {
                when (currentEditText) {
                    "title" -> binding.etTaskTitle.editText?.error = errorsMap[currentEditText]
                    "date" -> binding.etTaskDate.editText?.error = errorsMap[currentEditText]
                    "startingTime" -> binding.etTaskStartingTime.editText?.error =
                        errorsMap[currentEditText]
                    "endingTime" -> binding.etTaskEndingTime.editText?.error =
                        errorsMap[currentEditText]
                    "priority" -> binding.etTaskPriority.editText?.error =
                        errorsMap[currentEditText]
                }
            }
        }
    }

    private fun checkForErrors() {
        checkBlankValue()
    }

    private fun checkBlankValue() {
        if (binding.etTaskTitle.editText?.text.isNullOrBlank()) {
            addError("title", getString(R.string.blank_input_error_text, "title"))

        }
    }

    private fun addError(textField: String, errorText: String) {

        errorsMap = errorsMap + mapOf(textField to errorText)
        viewModel.setHasChangedValues(true)
        viewModel.setHasInputErrors(true)
        viewModel.setInputErrorsMap(errorsMap)
    }


    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        binding.etTaskDate.text = dateFormat(dayOfMonth, month, year)
    }

    override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
        if (isStartingTime) {
            binding.etTaskStartingTime.text = timeFormat(hourOfDay, minute)
        } else {
            binding.etTaskEndingTime.text = timeFormat(hourOfDay, minute)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        loadTaskValues()
        viewModel.setEditingTask(task)
        viewModel.setHasChangedValues(true)
    }
}