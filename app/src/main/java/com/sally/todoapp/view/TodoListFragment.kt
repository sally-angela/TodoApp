package com.sally.todoapp.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.sally.todoapp.R
import com.sally.todoapp.databinding.FragmentTodoListBinding
import com.sally.todoapp.viewmodel.ListTodoViewModel

class TodoListFragment : Fragment() {
    private lateinit var binding: FragmentTodoListBinding
    private lateinit var viewModel: ListTodoViewModel
    private val todoListAdapter = TodoListAdapter(arrayListOf(), { item -> viewModel.checkTask(item) })

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTodoListBinding.inflate(inflater,container,false)
        return  binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this).get(ListTodoViewModel::class.java)
        viewModel.refresh()
        binding.recViewTodo.layoutManager = LinearLayoutManager(context)
        binding.recViewTodo.adapter = todoListAdapter

        binding.btnFab.setOnClickListener {
            val action = TodoListFragmentDirections.actionCreateTodo()
            Navigation.findNavController(it).navigate(action)
        }

        observeViewModel()
    }

    fun observeViewModel() {
        viewModel.todoLD.observe(viewLifecycleOwner, Observer {
            todoListAdapter.updateTodoList(it)
            if(it.isEmpty()) {
                binding.recViewTodo?.visibility = View.GONE
                binding.txtError.setText("Your todo still empty.")
            } else {
                binding.recViewTodo?.visibility = View.VISIBLE
            }
        })

        viewModel.loadingLD.observe(viewLifecycleOwner, Observer {
            if(it == false) {
                binding.progressBar?.visibility = View.GONE
            } else {
                binding.progressBar?.visibility = View.VISIBLE
            }
        })

        viewModel.todoLoadErrorLD.observe(viewLifecycleOwner, Observer {
            if(it == false) {
                binding.txtError?.visibility = View.GONE
            } else {
                binding.txtError?.visibility = View.VISIBLE
            }
        })
    }

}