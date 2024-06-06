package com.sally.todoapp.view

import android.view.View
import android.widget.CompoundButton
import com.sally.todoapp.model.Todo

interface TodoCheckedChangeListener {
    fun onTodoCheckedChange(cb:CompoundButton, isChecked:Boolean, todo:Todo)
}

interface TodoEditClickListener {
    fun onTodoEditClick(v: View)
}

interface RadioClickListener {
    fun onRadioClick(v: View)
}