package com.sally.todoapp.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.sally.todoapp.model.Todo
import com.sally.todoapp.model.TodoDatabase
import com.sally.todoapp.util.buildDb
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class DetailTodoViewModel(application: Application): AndroidViewModel(application), CoroutineScope {
    private val job = Job()
    val todoLD = MutableLiveData<Todo>()

    fun addTodo(list:List<Todo>) {
        launch {
            val db = buildDb(getApplication())
            db.todoDao().insertAll(*list.toTypedArray())
        }
    }

    override val coroutineContext: CoroutineContext get() = job + Dispatchers.IO

    fun fetch(uuid:Int) {
        launch {
            val db = buildDb(getApplication())
            todoLD.postValue(db.todoDao().selectTodo(uuid))
        }
    }

    fun update(todo: Todo) {
        launch {
            val db = buildDb(getApplication())
            db.todoDao().updateTodo(todo)
        }
    }
}