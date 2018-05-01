package com.kylehanish.todo.interfaces;

import android.view.View;

import com.kylehanish.todo.classes.TodoItem;

/**
 * Created by Kyle Hanish on 4/29/18.
 */

public interface iTodoItemChangeListener {
    void SaveTodoItem(TodoItem item, boolean isEdit);

    void DeleteItem(int position, int itemID);

    void EditItem(TodoItem item);

}
