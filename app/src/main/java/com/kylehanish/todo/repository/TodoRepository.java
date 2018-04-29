package com.kylehanish.todo.repository;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kylehanish.todo.classes.TodoItem;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import static com.kylehanish.todo.utility.constants.SHARED_PREFERENCES_APP_KEY;

/**
 * Created by Kyle Hanish on 4/29/18.
 */

public class TodoRepository implements iTodoRepository {

    private static final String TODO_ITEMS_KEY = "TODO_ITEMS";


    /**
     * Get all TODO items from the repository
     *
     * @param context
     * @return
     */
    @Override
    public List<TodoItem> getTodoItems(Context context) {

        SharedPreferences sharedPref = context.getSharedPreferences(SHARED_PREFERENCES_APP_KEY, Context.MODE_PRIVATE);
        if(sharedPref.contains(TODO_ITEMS_KEY)){
            String json =  sharedPref.getString(TODO_ITEMS_KEY, null);
            Type listType = new TypeToken<ArrayList<TodoItem>>(){}.getType();
            return new Gson().fromJson(json, listType);
        }

        return new ArrayList<>();
    }

    /**
     *Save a list of TODO Items to the repository
     *
     * @param todoItems
     * @param context
     */
    @Override
    public void saveTodoItems(List<TodoItem> todoItems, Context context) {

        //Todo: Move away from Shared preferences
        // Add Ability to save single item instead of all items

        SharedPreferences sharedPref = context.getSharedPreferences(SHARED_PREFERENCES_APP_KEY,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();

        String json = new Gson().toJson(todoItems);
        editor.putString(TODO_ITEMS_KEY, json);
        boolean completed = editor.commit();
        Log.d(TodoRepository.class.getName(), "saveTodoItems: ");

    }
}
