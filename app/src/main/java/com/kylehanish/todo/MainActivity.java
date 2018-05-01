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
import android.view.View;
import android.widget.TextView;

import com.kylehanish.todo.adapters.TodoItemArrayAdapter;
import com.kylehanish.todo.classes.TodoItem;
import com.kylehanish.todo.fragments.TodoDialogFragment;
import com.kylehanish.todo.interfaces.iTodoItemChangeListener;
import com.kylehanish.todo.repository.TodoSQLRepository;
import com.kylehanish.todo.repository.iTodoRepository;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class MainActivity extends AppCompatActivity implements iTodoItemChangeListener {


    private static final String BUNDLE_TODO_ITEMS = "todo_items";

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
                showEditorDialog(null);
            }
        });

        mRepository = new TodoSQLRepository(mContext);
        mTodoItems = mRepository.getTodoItems(null,null,null,null,null);
        mAdapter = new TodoItemArrayAdapter(this,R.layout.list_item_todo, mTodoItems,this);
        recyclerView.setAdapter(mAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    public void onResume(){
        super.onResume();

        if(mTodoItems == null){
            mTodoItems = mRepository.getTodoItems(null,null,null,null,null);
            mAdapter.notifyDataSetChanged();
        }
        SetListVisibilty();
    }

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
    public void SaveTodoItem(TodoItem item, boolean isEdit) {
        item = mRepository.SaveTodoItem(item);
        if(!isEdit){
            //new item is being addeed
            mTodoItems.add(item);
        }
        mAdapter.notifyDataSetChanged();
        SetListVisibilty();
    }

    @Override
    public void DeleteItem(int position, int itemID) {
        if(mTodoItems.size() > 0 && position >= 0 && position < mTodoItems.size()){
            boolean deleted = mRepository.DeleteTodoItem(itemID);
            if(deleted){
                mTodoItems.remove(position);
                mAdapter.notifyDataSetChanged();

                SetListVisibilty();
            }
        }
    }

    @Override
    public void EditItem(TodoItem item) {
        showEditorDialog(item);
    }

    private void showEditorDialog(TodoItem item){

        if(getFragmentManager().findFragmentByTag(TodoDialogFragment.TAG) == null){
            TodoDialogFragment dialogFragment = new TodoDialogFragment();

            if(item != null){
                Bundle args = new Bundle();
                args.putParcelable(TodoDialogFragment.BUNDLE_TODO_ITEM, item);
                dialogFragment.setArguments(args);
            }

            dialogFragment.show(getFragmentManager(),TodoDialogFragment.TAG);
        }
    }


}
