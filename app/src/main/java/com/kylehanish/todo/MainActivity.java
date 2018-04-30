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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;

import com.kylehanish.todo.adapters.TodoItemArrayAdapter;
import com.kylehanish.todo.classes.TodoItem;
import com.kylehanish.todo.fragments.TodoDialogFragment;
import com.kylehanish.todo.interfaces.iTodoItemChangeListener;
import com.kylehanish.todo.repository.TodoRepository;
import com.kylehanish.todo.repository.iTodoRepository;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class MainActivity extends AppCompatActivity implements iTodoItemChangeListener {


//    Views

    @BindView(R.id.items_list)
    RecyclerView recyclerView;

    @BindView(R.id.fab)
    FloatingActionButton fab;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.empty_todo_list)
    TextView emptyTodoList;

//    Page Variables
    private Context mContext;
    private Unbinder mUnbinder;
    private TodoItemArrayAdapter mAdapter;
    private List<TodoItem> mTodoItems;
    private iTodoRepository mRepository;

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
        mAdapter = new TodoItemArrayAdapter(this,R.layout.list_item_todo, mTodoItems,this);
        recyclerView.setAdapter(mAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    public void onResume(){
        super.onResume();

        if(mTodoItems == null){
            mTodoItems = mRepository.getTodoItems(this);
            mAdapter.notifyDataSetChanged();
        }

        SetListVisibilty();

    }


//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.menu_main, menu);
//        return true;
//    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        int id = item.getItemId();
//        if (id == R.id.action_settings) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }

    private void SetListVisibilty(){

        if(mTodoItems != null && mTodoItems.size() >0){
            if(recyclerView.getVisibility() == View.GONE){
                emptyTodoList.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
            }
        }else{
            if(recyclerView.getVisibility() == View.VISIBLE){
                emptyTodoList.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.GONE);
            }
        }
    }



    @Override
    public void SaveTodoItem(TodoItem item, Integer position) {
        if(position == null){
            //new item is being addeed
            mTodoItems.add(item);
        }else{
            //editing an existing item
            mTodoItems.set(position,item);
        }

        mRepository.saveTodoItems(mTodoItems,mContext);
        mAdapter.notifyDataSetChanged();

        SetListVisibilty();

    }

    @Override
    public void DeleteItem(int position) {
        if(mTodoItems.size() > 0 && position >= 0 && position < mTodoItems.size() ){
            mTodoItems.remove(position);
            mAdapter.notifyDataSetChanged();

            SetListVisibilty();
        }
    }
}
