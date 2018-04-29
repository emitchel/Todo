package com.kylehanish.todo.classes;

import android.support.annotation.ColorRes;

import java.io.Serializable;

/**
 * Created by Kyle Hanish on 4/29/18.
 */

public class TodoItem implements Serializable{


    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public boolean isCompleted() {
        return Completed;
    }

    public void setCompleted(boolean completed) {
        Completed = completed;
    }

    private int ID;
    private String Description;
    private boolean Completed;

    public TodoItem(){}
    public TodoItem(String Description, boolean Completed){
        this.Description = Description;
        this.Completed = Completed;
    }


}
