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
import com.nilsonsasaki.kotlin_to_do_list.extensions.text
import com.nilsonsasaki.kotlin_to_do_list.ui.models.TaskViewModel
import com.nilsonsasaki.kotlin_to_do_list.ui.models.TaskViewModelFactory
import java.util.*

class EditTaskFragment : Fragment(), DatePickerDialog.OnDateSetListener,
    TimePickerDialog.OnTimeSetListener {

    private var _binding: FragmentEditTaskBinding? = null
    private val binding get() = _binding!!

    private val navigationArgs: EditTaskFragmentArgs by navArgs()

    lateinit var task :Task

    private var day = 0
    private var month = 0
    private var year = 0
    private var hour = 0
    private var minute = 0

    private var isStartingTime: Boolean = false
    private var hasError: Boolean = false

    private val viewModel: TaskViewModel by activityViewModels {
        TaskViewModelFactory(
            (activity?.application as TaskApplication).database
                .taskDao()
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getDateAndTimeCalendar()
        task =Task(
            id = 0,
            title = "",
            date = getString(R.string.edit_task_date_text,day,month,year)
            ,startingTime = getTimeText(hour,minute),
            endingTime = getTimeText(hour+1,minute),
            priority = "Normal",
            description = "")
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

        val items = listOf("Very Low", "Low", "Normal", "High", "Very High")
        val adapter = ArrayAdapter(requireContext(), R.layout.dropdown_menu, items.reversed())
        (binding.etTaskPriority.editText as? AutoCompleteTextView)?.setAdapter(adapter)
        setOnClickListeners()

        if (navigationArgs.itemId > 0) {
            getSelectedTask()
            binding.btSaveButton.setOnClickListener {
                checkValues()
                if (!hasError) {
                    loadTaskValues()
                    viewModel.updateTask(task)
                    findNavController().navigateUp()
                }
            }

        } else {
            bindTask(task)
            binding.btSaveButton.setOnClickListener {
                checkValues()
                if (!hasError) {
                    loadTaskValues()
                    viewModel.addNewTask(task)
                    val action =
                        EditTaskFragmentDirections.actionEditTaskFragmentToTaskListFragment()
                    findNavController().navigate(action)
                }
            }
        }
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

        binding.cancelButton.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun getDateAndTimeCalendar() {

        val cal: Calendar = Calendar.getInstance()
        day = cal.get(Calendar.DAY_OF_MONTH)
        month = cal.get(Calendar.MONTH)
        year = cal.get(Calendar.YEAR)
        hour = cal.get(Calendar.HOUR)
        minute = cal.get(Calendar.MINUTE)
    }
    private fun getSelectedTask(){
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

    private fun checkValue(field: EditText): Boolean {
        return if (field.text.isBlank()) {
            field.error = "Required"
            true
        } else {
            field.error = null
            false
        }
    }

    private fun checkValues() {
        hasError = (checkValue(binding.etTaskTitle.editText!!)
                || checkValue(binding.etTaskDate.editText!!)
                || checkValue(binding.etTaskPriority.editText!!)
                || checkValue(binding.etTaskStartingTime.editText!!)
                || checkValue(binding.etTaskEndingTime.editText!!))
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        binding.etTaskDate.text = "$dayOfMonth/$month/$year"
    }

    override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
        if (isStartingTime) {
            binding.etTaskStartingTime.text = getTimeText(hourOfDay,minute)
            binding.etTaskEndingTime.text = getTimeText(hourOfDay+1,minute)
        } else {
            binding.etTaskEndingTime.text = getTimeText(hourOfDay,minute)
        }
    }
    private fun getTimeText(hour: Int, minute:Int):String{
        val timeText: String = if (minute in 0..9) {
            "$hour:0$minute"
        } else {
            "$hour:$minute"
        }
        return timeText
    }
}