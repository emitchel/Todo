package com.kylehanish.todo.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
import butterknife.OnClick;

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
        return new ViewHolder(view, mListener);
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
        holder.finished.setTag(currentItem);
        holder.finished.setOnCheckedChangeListener(checkboxOnClickListener);

        holder.textDescription.setText(currentItem.getDescription());

        holder.delete.setTag(currentItem);
        holder.delete.setOnClickListener(DeleteOnClickListener);

        holder.edit.setTag(currentItem);
        holder.edit.setOnClickListener(EditOnClickListener);
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
                TodoItem clickedItem = (TodoItem) buttonView.getTag();
                clickedItem.setCompleted(isChecked);
                clickedItem.setLastEditedOn(new Date());
                mListener.SaveTodoItem(clickedItem,true);
            }
        }
    };

    private View.OnClickListener DeleteOnClickListener = new View.OnClickListener(){
        @Override
        public void onClick(View v) {
            if(mListener != null){
                TodoItem currentItem = (TodoItem) v.getTag();
                mListener.DeleteItem(mItems.indexOf(currentItem),currentItem.getID());
            }
        }
    };


    private View.OnClickListener EditOnClickListener = new View.OnClickListener(){
        @Override
        public void onClick(View v) {
            if(mListener != null){
                TodoItem currentItem = (TodoItem) v.getTag();
                mListener.EditItem(currentItem);
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

        @BindView(R.id.edit)
        ImageView edit;

        private iTodoItemChangeListener mListener;

        public ViewHolder(View view, iTodoItemChangeListener itemChangeListener){
            super(view);
            ButterKnife.bind(this,view);
            mListener = itemChangeListener;
        }

    }

}
