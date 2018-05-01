package com.kylehanish.todo.classes;

import android.os.Parcel;
import android.os.Parcelable;

import com.kylehanish.todo.utility.FormatterUtils;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Kyle Hanish on 4/29/18.
 */

public class TodoItem implements Serializable, Parcelable {


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

    public String getTag() {
        return Tag;
    }

    public void setTag(String tag) {
        Tag = tag;
    }


    private Date CreatedOn;
    private Date LastEditedOn;
    private int ID;
    private String Description;
    private boolean Completed;
    private String Tag;

    public TodoItem(){}

    private TodoItem(Parcel in){
        long tmpDate = in.readLong();
        CreatedOn = tmpDate == -1 ? null : new Date(tmpDate);
        tmpDate = in.readLong();
        LastEditedOn = tmpDate == -1 ? null : new Date(tmpDate);
        ID = in.readInt();
        Description = in.readString();
        Completed = in.readByte() == 1;
        Tag = in.readString();
    }


    public static final Creator<TodoItem> CREATOR = new Creator<TodoItem>() {
        @Override
        public TodoItem createFromParcel(Parcel in) {
            return new TodoItem(in);
        }

        @Override
        public TodoItem[] newArray(int size) {
            return new TodoItem[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(CreatedOn != null ? CreatedOn.getTime() : -1);
        dest.writeLong(LastEditedOn != null ? LastEditedOn.getTime() : -1);
        dest.writeInt(ID);
        dest.writeString(Description);
        dest.writeByte((byte) (Completed ? 1 : 0));
        dest.writeString(Tag);
    }
}
