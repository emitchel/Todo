package com.kylehanish.todo.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.kylehanish.todo.R;
import com.kylehanish.todo.classes.TodoItem;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Kyle Hanish on 4/29/18.
 */

public class TodoItemArrayAdapter extends RecyclerView.Adapter<TodoItemArrayAdapter.ViewHolder> {

    private Context mContext;
    private List<TodoItem> mItems;

    public TodoItemArrayAdapter(Context context, int resource, List<TodoItem> items){
        mContext = context;
        mItems = items;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_todo, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        TodoItem currentItem = mItems.get(position);

        if(currentItem == null){
            return;
        }

        holder.finished.setChecked(currentItem.isCompleted());
        holder.textDescription.setText(currentItem.getDescription());
    }

    @Override
    public int getItemCount() {
        if(mItems != null){
            return mItems.size();
        }
        return 0;
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.description)
        TextView textDescription;

        @BindView(R.id.cb_finished)
        CheckBox finished;


        public ViewHolder(View view){
            super(view);
            ButterKnife.bind(this,view);
        }
    }

}
