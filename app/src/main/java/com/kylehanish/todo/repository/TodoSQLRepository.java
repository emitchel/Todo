package com.kylehanish.todo.repository;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.kylehanish.todo.classes.TodoItem;
import com.kylehanish.todo.utility.FormatterUtils;

import java.io.WriteAbortedException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by Kyle Hanish on 4/29/18.
 */

public class TodoSQLRepository extends SQLiteOpenHelper implements iTodoRepository{
    private static final String DATABASE_NAME = "sample_database";
    private static final int DATABASE_VERSION = 1;
    private static final String TAG = TodoSQLRepository.class.getSimpleName();

    //  ID | DESCRIPTION | COMPLETED | PRIORITY | CREATED ON | LAST UPDATED ON | TAG
    public static final String TABLE = "table_todo";
    public static final String COL_ID = "col_id";
    public static final String COL_DESCRIPTION = "col_description";
    public static final String COL_COMPLETED = "col_completed";
    public static final String COL_PRIORITY = "col_priority";
    public static final String COL_CREATEDON = "col_createdon";
    public static final String COL_LASTUPDATEDON = "col_lastupdatedon";
    public static final String COL_TAG = "col_tag";

    public static final String TODO_ITEM_ID_SELECTOR = COL_ID + " =?" ;

    public static final String DB_CREATE = "CREATE TABLE " + TABLE + "(" + COL_ID + " INTEGER NOT NULL PRIMARY KEY, "
            + COL_DESCRIPTION + " TEXT, " + COL_COMPLETED + " BIT, " + COL_PRIORITY + " TEXT, " + COL_CREATEDON + " TEXT, "
            + COL_LASTUPDATEDON+ " TEXT, " + COL_TAG + " TEXT " + ")";


    String[] columns={
            COL_ID.trim(),
            COL_DESCRIPTION.trim() ,
            COL_COMPLETED.trim(),
            COL_PRIORITY.trim() ,
            COL_CREATEDON.trim() ,
            COL_LASTUPDATEDON.trim(),
            COL_TAG.trim()
    };

    public TodoSQLRepository(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DB_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + DB_CREATE);
        onCreate(db);
    }


    private SQLiteDatabase WriteableDB(){
        return this.getWritableDatabase();
    }
    private SQLiteDatabase ReadableDB(){
        return this.getReadableDatabase();
    }




//    public boolean insertData(String tableName,ContentValues values){
//        SQLiteDatabase db = this.getWritableDatabase();
//        long result = db.insert(tableName, null, values);
//        if (result == -1){
//            Log.d(TAG, "failed to save data!");
//            return false;
//        }else{
//            Log.d(TAG, "save data successful");
//            return true;
//        }
//    }


    private ContentValues getContentValuesFromTodoItem(TodoItem item){
        ContentValues values = new ContentValues();
        if(item.getID() > 0){
            values.put(COL_ID,item.getID());
        }
        values.put(COL_DESCRIPTION,item.getDescription());
        values.put(COL_COMPLETED,item.isCompleted());
        if(item.getCreatedOn() != null){
            values.put(COL_CREATEDON, FormatterUtils.GetFormattedDate(item.getCreatedOn()));
        }

        if(item.getLastEditedOn() != null){
            values.put(COL_LASTUPDATEDON, FormatterUtils.GetFormattedDate(item.getLastEditedOn()));
        }

        values.put(COL_TAG,item.getTag());
        return values;
    }


//
//    public List<TodoItem> GetTodoListItems(String selection){
//        SQLiteDatabase db  = this.getReadableDatabase();  //ID | DESCRIPTION | COMPLETED | PRIORITY | CREATED ON | LAST UPDATED ON | TAG
//
//        Cursor cursor = db.query(TABLE, columns,
//                null, null, null, null, null);
//
//        List<TodoItem> items = new ArrayList<>();
//
//
//        if (cursor.moveToFirst()){
//            while (!cursor.isAfterLast()) {
//                TodoItem todoItem = new TodoItem();
//                todoItem.setID(cursor.getInt(0));
//                todoItem.setDescription(cursor.getString(1));
//                int completed = cursor.getInt(2);
//                todoItem.setCompleted(completed == 1);
//
//                todoItem.setCreatedOn(TryParseStringToDate(cursor.getString(4)));
//                todoItem.setLastEditedOn(TryParseStringToDate(cursor.getString(5)));
//
//                todoItem.setTag(cursor.getString(6));
//                items.add(todoItem);
//
//                cursor.moveToNext();
//            }
//        }
//
//        return items;
//    }




    private List<TodoItem> BuildTodoItems(Cursor cursor){
        List<TodoItem> items = new ArrayList<>();

        if (cursor != null &&  cursor.moveToFirst()){

            int idIndex = cursor.getColumnIndex(COL_ID);
            int descIndex = cursor.getColumnIndex(COL_DESCRIPTION);
            int completedIndex = cursor.getColumnIndex(COL_COMPLETED);
            int createdonIndex = cursor.getColumnIndex(COL_CREATEDON);
            int lasteditIndex = cursor.getColumnIndex(COL_LASTUPDATEDON);
            int tagIndex = cursor.getColumnIndex(COL_TAG);

            while (!cursor.isAfterLast()) {
                TodoItem todoItem = new TodoItem();
                todoItem.setID(cursor.getInt(idIndex));
                todoItem.setDescription(cursor.getString(descIndex));
                int completed = cursor.getInt(completedIndex);
                todoItem.setCompleted(completed == 1);

                todoItem.setCreatedOn(FormatterUtils.TryParseStringToDate(cursor.getString(createdonIndex)));
                todoItem.setLastEditedOn(FormatterUtils.TryParseStringToDate(cursor.getString(lasteditIndex)));

                todoItem.setTag(cursor.getString(tagIndex));
                items.add(todoItem);

                cursor.moveToNext();
            }
        }

        return items;
    }


    private TodoItem AddNewItem(TodoItem item){
        long result = WriteableDB().insert(TABLE, null, getContentValuesFromTodoItem(item));
        if (result != -1){
               return GetTodoItem((int)result);
        }
        return null; //Item did not save

    }

    private TodoItem UpdateItem(TodoItem item){
        if(item.getID() > 0){
            int result = WriteableDB().update(TABLE,getContentValuesFromTodoItem(item),TODO_ITEM_ID_SELECTOR,new String[] {String.valueOf(item.getID())});
            if(result > 0){ //successfully updated
                return GetTodoItem(item.getID());
            }
        }
        return null;
    }

    @Override
    public List<TodoItem> getTodoItems(String selection, String[] selectionArgs, String groupBy, String having, String orderBy) {
        Cursor cursor = ReadableDB().query(TABLE, null, selection, selectionArgs, groupBy, having, orderBy);
        return BuildTodoItems(cursor);
    }

    @Override
    public TodoItem SaveTodoItem(TodoItem item) {
        if(item.getID() > 0){ //Item already exists, do update
            return  UpdateItem(item);
        }else{ //Item doesn't exist, create new
            return AddNewItem(item);
        }
    }

    @Override
    public boolean DeleteTodoItem(int ID) {
        SQLiteDatabase db = this.getWritableDatabase();
        return WriteableDB().delete(TABLE,TODO_ITEM_ID_SELECTOR,new String[] {String.valueOf(ID)}) > 0;
    }

    @Override
    public TodoItem GetTodoItem(int ID) {
        List<TodoItem> items = getTodoItems(TODO_ITEM_ID_SELECTOR, new String[] {String.valueOf(ID)}, null, null , null);
        if(items.size() > 0){
            return items.get(0); //ID is primary key, at most 1 value should be returned.
        }

        return null;
    }
}
