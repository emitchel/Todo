package com.kylehanish.todo.interfaces;

import com.kylehanish.todo.classes.TodoItem;

/**
 * Created by Kyle Hanish on 4/29/18.
 */

public interface iTodoItemChangeListener {
    /**
     *
     * @param item
     * @param position (0 based index)
     */
    void SaveTodoItem(TodoItem item, Integer position);
}
