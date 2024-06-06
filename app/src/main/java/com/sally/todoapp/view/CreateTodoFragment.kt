package com.sally.todoapp.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.sally.todoapp.R
import com.sally.todoapp.databinding.FragmentCreateTodoBinding
import com.sally.todoapp.model.Todo
import com.sally.todoapp.viewmodel.DetailTodoViewModel

class CreateTodoFragment : Fragment() {
    private lateinit var binding: FragmentCreateTodoBinding
    private lateinit var viewModel: DetailTodoViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCreateTodoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(DetailTodoViewModel::class.java)

        binding.btnAdd.setOnClickListener {
            var radio = view.findViewById<RadioButton>(binding.radioGroupPriority.checkedRadioButtonId)
            val todo = Todo(binding.txtTitle.text.toString(), binding.txtNotes.text.toString(), radio.tag.toString().toInt())
            val list = listOf(todo)
            viewModel.addTodo(list)
            Toast.makeText(view.context, "Data added", Toast.LENGTH_SHORT).show()
            Navigation.findNavController(it).popBackStack()
        }
    }
}