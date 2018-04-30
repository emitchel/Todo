package com.kylehanish.todo.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.QuickContactBadge;
import android.widget.TextView;

import com.kylehanish.todo.R;
import com.kylehanish.todo.classes.TodoItem;
import com.kylehanish.todo.interfaces.iTodoItemChangeListener;
import com.kylehanish.todo.repository.iTodoRepository;

import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Kyle Hanish on 4/29/18.
 */

public class TodoItemArrayAdapter extends RecyclerView.Adapter<TodoItemArrayAdapter.ViewHolder> {

    private Context mContext;
    private List<TodoItem> mItems;
    private iTodoItemChangeListener mListener;

    public TodoItemArrayAdapter(Context context, int resource, List<TodoItem> items, iTodoItemChangeListener itemChangeListener){
        mContext = context;
        mItems = items;
        mListener = itemChangeListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_todo, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.finished.setOnCheckedChangeListener(null);
        holder.delete.setOnClickListener(null);

        TodoItem currentItem = mItems.get(position);

        if(currentItem == null){
            return;
        }

        holder.finished.setChecked(currentItem.isCompleted());
        holder.finished.setTag(position);
        holder.finished.setOnCheckedChangeListener(checkboxOnClickListener);

        holder.textDescription.setText(currentItem.getDescription());

        holder.delete.setTag(position);
        holder.delete.setOnClickListener(DeleteOnClickListener);

    }

    @Override
    public int getItemCount() {
        if(mItems != null){
            return mItems.size();
        }
        return 0;
    }

    private CompoundButton.OnCheckedChangeListener checkboxOnClickListener = new CompoundButton.OnCheckedChangeListener(){

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if(mListener != null){
                int position = (int) buttonView.getTag();
                TodoItem clickedItem = mItems.get(position);

                clickedItem.setCompleted(isChecked);
                clickedItem.setLastEditedOn(new Date());
                mListener.SaveTodoItem(clickedItem,position);
            }
        }
    };



    private View.OnClickListener DeleteOnClickListener = new View.OnClickListener(){
        @Override
        public void onClick(View v) {
            if(mListener != null){
                mListener.DeleteItem((int)v.getTag());
            }
        }
    };



    static class ViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.description)
        TextView textDescription;

        @BindView(R.id.cb_finished)
        CheckBox finished;

        @BindView(R.id.delete)
        ImageView delete;


        public ViewHolder(View view){
            super(view);
            ButterKnife.bind(this,view);
        }
    }

}
