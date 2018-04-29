package com.kylehanish.todo.classes;

import java.io.Serializable;
import java.util.Date;

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

    public Date getCreatedOn() {
        return CreatedOn;
    }

    public void setCreatedOn(Date createdOn) {
        CreatedOn = createdOn;
    }

    public Date getLastEditedOn() {
        return LastEditedOn;
    }

    public void setLastEditedOn(Date lastEditedOn) {
        LastEditedOn = lastEditedOn;
    }

    private Date CreatedOn;
    private Date LastEditedOn;
    private int ID;
    private String Description;
    private boolean Completed;

    public TodoItem(){}


}
