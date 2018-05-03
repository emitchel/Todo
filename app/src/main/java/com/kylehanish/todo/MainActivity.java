package com.kylehanish.todo;

import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.kylehanish.todo.adapters.TodoItemArrayAdapter;
import com.kylehanish.todo.classes.TodoItem;
import com.kylehanish.todo.fragments.PreviewDialogFragment;
import com.kylehanish.todo.fragments.TodoDialogFragment;
import com.kylehanish.todo.interfaces.iTodoItemChangeListener;
import com.kylehanish.todo.repository.TodoSQLRepository;
import com.kylehanish.todo.repository.iTodoRepository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class MainActivity extends AppCompatActivity implements iTodoItemChangeListener {


    private static final String BUNDLE_TODO_ITEMS = "todo_items";

//    Views

    @BindView(R.id.items_list)
    RecyclerView recyclerView;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.empty_todo_list)
    TextView emptyTodoList;

    //    Page Variables
    private Context mContext;
    private Unbinder mUnbinder;
    private TodoItemArrayAdapter mAdapter;
    private ArrayList<TodoItem> mTodoItems;
    private iTodoRepository mRepository;
    private LinearLayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mUnbinder = ButterKnife.bind(this);

        mContext = this;

        setSupportActionBar(toolbar);

        mRepository = new TodoSQLRepository(mContext);

        SetBundleValues(savedInstanceState);
        getListData();

        mAdapter = new TodoItemArrayAdapter(this, R.layout.list_item_todo, mTodoItems, this);
        recyclerView.setAdapter(mAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//        recyclerView.setOnLongClickListener(new View.OnLongClickListener() {
//            @Override
//            public boolean onLongClick(View v) {
//                if (getFragmentManager().findFragmentByTag(PreviewDialogFragment.TAG) == null) {
//                    PreviewDialogFragment dialogFragment = new PreviewDialogFragment();
//
//                    int position = recyclerView.getChildLayoutPosition(v);
//                    TodoItem item = mTodoItems.get(position);
//
//                    if (item != null) {
//                        Bundle args = new Bundle();
//                        args.putParcelable(PreviewDialogFragment.BUNDLE_TODO_ITEM, item);
//                        dialogFragment.setArguments(args);
//                    }
//
//                    dialogFragment.show(getFragmentManager(), PreviewDialogFragment.TAG);
//                }
//
//                return true;
//            }
//        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.add_new_item) {
            showEditorDialog(null);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(BUNDLE_TODO_ITEMS, mTodoItems);

    }

    private void SetBundleValues(Bundle args) {

        if (args != null) {
            if (args.containsKey(BUNDLE_TODO_ITEMS)) {
                mTodoItems = args.getParcelableArrayList(BUNDLE_TODO_ITEMS);
            }
        }

    }

    @Override
    public void onResume() {
        super.onResume();

        getListData();
        SetListVisibilty();
    }


    private void getListData() {
        if (mTodoItems == null) {
            mTodoItems = mRepository.getTodoItems(null, null, null, null, null);
        }
    }

    private void SetListVisibilty() {
        if (mTodoItems != null && mTodoItems.size() > 0) {
            if (recyclerView.getVisibility() == View.GONE) {
                emptyTodoList.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
            }
        } else {
            if (recyclerView.getVisibility() == View.VISIBLE) {
                emptyTodoList.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.GONE);
            }
        }
    }


    @Override
    public void SaveTodoItem(TodoItem item, boolean isEdit) {
        item = mRepository.SaveTodoItem(item);
        if (!isEdit) {
            //new item is being addeed
            mTodoItems.add(item);
        }
        mAdapter.notifyDataSetChanged();
        SetListVisibilty();
    }

    @Override
    public void DeleteItem(int position, int itemID) {
        if (mTodoItems.size() > 0 && position >= 0 && position < mTodoItems.size()) {
            boolean deleted = mRepository.DeleteTodoItem(itemID);
            if (deleted) {
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

    @Override
    public void PreviewItem(View view) {
        if (getFragmentManager().findFragmentByTag(PreviewDialogFragment.TAG) == null) {
            PreviewDialogFragment dialogFragment = new PreviewDialogFragment();

            int position = recyclerView.getChildLayoutPosition(view);
            TodoItem item = mTodoItems.get(position);

            if (item != null) {
                Bundle args = new Bundle();
                args.putParcelable(PreviewDialogFragment.BUNDLE_TODO_ITEM, item);
                dialogFragment.setArguments(args);
            }

            dialogFragment.show(getFragmentManager(), PreviewDialogFragment.TAG);
        }

    }

    private void showEditorDialog(TodoItem item) {

        if (getFragmentManager().findFragmentByTag(TodoDialogFragment.TAG) == null) {
            TodoDialogFragment dialogFragment = new TodoDialogFragment();

            if (item != null) {
                Bundle args = new Bundle();
                args.putParcelable(TodoDialogFragment.BUNDLE_TODO_ITEM, item);
                dialogFragment.setArguments(args);
            }

            dialogFragment.show(getFragmentManager(), TodoDialogFragment.TAG);
        }
    }


}
