package com.kylehanish.todo.repository;

import android.content.Context;

import com.kylehanish.todo.classes.TodoItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kyle Hanish on 4/29/18.
 */

public interface iTodoRepository {
    ArrayList<TodoItem> getTodoItems(String selection,
                                     String[] selectionArgs, String groupBy, String having,
                                     String orderBy);
    TodoItem SaveTodoItem(TodoItem item);
    boolean DeleteTodoItem(int ID);
    TodoItem GetTodoItem(int ID);
}
