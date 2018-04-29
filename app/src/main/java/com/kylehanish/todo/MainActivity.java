package com.kylehanish.todo;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.kylehanish.todo.adapters.TodoItemArrayAdapter;
import com.kylehanish.todo.classes.TodoItem;
import com.kylehanish.todo.fragments.TodoDialogFragment;
import com.kylehanish.todo.repository.TodoRepository;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class MainActivity extends AppCompatActivity implements TodoDialogFragment.TodoDialogListener {


//    Views

    @BindView(R.id.items_list)
    RecyclerView recyclerView;

    @BindView(R.id.fab)
    FloatingActionButton fab;

    @BindView(R.id.toolbar)
    Toolbar toolbar;



//    Page Variables
    private Context mContext;
    private Unbinder mUnbinder;
    private TodoItemArrayAdapter mAdapter;
    private List<TodoItem> mTodoItems;
    private TodoRepository mRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mUnbinder = ButterKnife.bind(this);

        mContext = this;

        setSupportActionBar(toolbar);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(getFragmentManager().findFragmentByTag(TodoDialogFragment.TAG) == null){
                    new TodoDialogFragment().show(getFragmentManager(),TodoDialogFragment.TAG);
                }
            }
        });


        mRepository = new TodoRepository();
        mTodoItems = mRepository.getTodoItems(this);

        mAdapter = new TodoItemArrayAdapter(this,R.layout.list_item_todo, mTodoItems);
        recyclerView.setAdapter(mAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }


//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.menu_main, menu);
//        return true;
//    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void getTODOItemDialog(){
        new TodoDialogFragment().show(getFragmentManager(),TodoDialogFragment.TAG);
    }

    @Override
    public void onTodoSave(TodoItem item) {
        mTodoItems.add(item);
        mRepository.saveTodoItems(mTodoItems,mContext);
        mAdapter.notifyDataSetChanged();
    }



}
