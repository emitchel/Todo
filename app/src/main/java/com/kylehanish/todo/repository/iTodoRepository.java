package com.kylehanish.todo.repository;

import android.content.Context;

import com.kylehanish.todo.classes.TodoItem;

import java.util.List;

/**
 * Created by Kyle Hanish on 4/29/18.
 */

public interface iTodoRepository {
    List<TodoItem> getTodoItems(Context context);
    void saveTodoItems(List<TodoItem> todoItems, Context context);
}
