package com.sally.todoapp.view

import android.Manifest
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.RadioButton
import android.widget.TimePicker
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.sally.todoapp.R
import com.sally.todoapp.databinding.FragmentCreateTodoBinding
import com.sally.todoapp.model.Todo
import com.sally.todoapp.util.NotificationHelper
import com.sally.todoapp.util.NotificationHelper.Companion.REQUEST_NOTIF
import com.sally.todoapp.util.TodoWorker
import com.sally.todoapp.viewmodel.DetailTodoViewModel
import java.util.Calendar
import java.util.concurrent.TimeUnit

class CreateTodoFragment : Fragment(), RadioClickListener, TodoEditClickListener,
    DateClickListener, TimeClickListener, DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {
    private lateinit var binding: FragmentCreateTodoBinding
    private lateinit var viewModel: DetailTodoViewModel

    var year = 0
    var month = 0
    var day = 0
    var hour = 0
    var minute = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCreateTodoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.POST_NOTIFICATIONS)
            != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                NotificationHelper.REQUEST_NOTIF)
        }

        binding.todo = Todo("","",3, 0, 0)
        viewModel = ViewModelProvider(this).get(DetailTodoViewModel::class.java)
        binding.submitListener = this
        binding.radioListener = this
        binding.dateListener = this
        binding.timeListener = this

        viewModel = ViewModelProvider(this).get(DetailTodoViewModel::class.java)

//        binding.btnAdd.setOnClickListener {
////            val notif = NotificationHelper(view.context)
////            notif.createNotification("Todo Created", "A new todo has been created! Stay focus!")
//
//            val workRequest = OneTimeWorkRequestBuilder<TodoWorker>()
//                .setInitialDelay(20, TimeUnit.SECONDS)
//                .setInputData(
//                    workDataOf(
//                        "title" to "Todo created",
//                        "message" to "Stay focus"
//                    ))
//                .build()
//            WorkManager.getInstance(requireContext()).enqueue(workRequest)
//
//            var radio = view.findViewById<RadioButton>(binding.radioGroupPriority.checkedRadioButtonId)
//            val todo = Todo(binding.txtTitle.text.toString(), binding.txtNotes.text.toString(), radio.tag.toString().toInt(), 0)
//            val list = listOf(todo)
//            viewModel.addTodo(list)
//            Toast.makeText(view.context, "Data added", Toast.LENGTH_SHORT).show()
//            Navigation.findNavController(it).popBackStack()
//        }
    }

    override fun onRequestPermissionsResult(requestCode:Int, permissions:Array<out String>, grantResults:IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode == NotificationHelper.REQUEST_NOTIF) {
            if(grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                NotificationHelper(requireContext())
                    .createNotification("Todo Created", "A new todo has been created! Stay focus!")
            }
        }
    }

    override fun onTodoEditClick(v: View) {
        val c = Calendar.getInstance()
        c.set(year, month, day, hour, minute, 0)
        val today = Calendar.getInstance()
        val diff = (c.timeInMillis/1000L) - (today.timeInMillis/1000L)
        binding.todo!!.todo_date = (c.timeInMillis/1000L).toInt()

        val workRequest = OneTimeWorkRequestBuilder<TodoWorker>()
            .setInitialDelay(diff, TimeUnit.SECONDS)
            .setInputData(
                workDataOf(
                    "title" to "Todo created",
                    "message" to "Stay focus"
                ))
            .build()
        WorkManager.getInstance(requireContext()).enqueue(workRequest)

        val list = listOf(binding.todo!!)
        viewModel.addTodo(list)
        Toast.makeText(view?.context, "Data added", Toast.LENGTH_SHORT).show()
        Navigation.findNavController(v).popBackStack()
    }

    override fun onRadioClick(v: View) {
        binding.todo!!.priority = v.tag.toString().toInt()
    }

    override fun onDateClick(v: View) {
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)
        DatePickerDialog(requireContext(), this, year, month, day).show()
    }

    override fun onTimeClick(v: View) {
        val c = Calendar.getInstance()
        val hour = c.get(Calendar.HOUR_OF_DAY)
        val minute = c.get(Calendar.MINUTE)
        TimePickerDialog(requireContext(), this, hour, minute, true).show()
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, day: Int) {
        Calendar.getInstance().let {
            it.set(year, month, day)
            binding.txtDate.setText(
                day.toString().padStart(2, '0') + "-" +
                month.toString().padStart(2, '0') + "-" +
                year
            )
            this.year = year
            this.month = month
            this.day = day
        }
    }

    override fun onTimeSet(view: TimePicker?, hour: Int, minute: Int) {
        binding.txtTime.setText(
            hour.toString().padStart(2, '0') + ":" +
            minute.toString().padStart(2, '0')
        )
        this.hour = hour
        this.minute = minute
    }
}