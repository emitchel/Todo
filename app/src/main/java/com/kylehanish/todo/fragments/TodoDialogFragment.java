package com.kylehanish.todo.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.kylehanish.todo.R;
import com.kylehanish.todo.classes.TodoItem;
import com.kylehanish.todo.interfaces.iTodoItemChangeListener;

import java.time.LocalDateTime;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by Kyle Hanish on 4/29/18.
 */

public class TodoDialogFragment extends DialogFragment {


    @BindView(R.id.inputDescription)
    EditText inputDescription;


    public static final String TAG = TodoDialogFragment.class.getSimpleName();
    public static final String BUNDLE_TODO_ITEM = "todo_item";
    public static final String BUNDLE_DESRIPTION_VALUE = "et_description_value";

    private Unbinder mUnbinder;
    private iTodoItemChangeListener mListener;

    private TodoItem mCurrentItem;
    private String mCurrentDecriptionValue;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        setValuesFromBundle(getArguments(),savedInstanceState);

        String titleString = mCurrentItem.getID() > 0 ? getString(R.string.edit_item) : getString(R.string.add_new_item);

        builder.setTitle(titleString);
         builder.setPositiveButton(getString(R.string.save), new DialogInterface.OnClickListener() {
             @Override
             public void onClick(DialogInterface dialog, int which) {
                 if(mCurrentItem.getID() == 0){
                     mCurrentItem.setCreatedOn(new Date());
                 }
                 mCurrentItem.setDescription(inputDescription.getText().toString());
                mCurrentItem.setCreatedOn(new Date());


                 mListener.SaveTodoItem(mCurrentItem,mCurrentItem.getID() > 0);
             }
         });

         builder.setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
             @Override
             public void onClick(DialogInterface dialog, int which) {
                 dialog.dismiss();
             }
         });

         setCancelable(false);

        builder.setView(getActivity().getLayoutInflater().inflate(R.layout.view_add_todo,null));
        return builder.create();
    }


    private void setValuesFromBundle(Bundle arguments, Bundle savedInstanceState){
        Bundle args = savedInstanceState != null ?  savedInstanceState : arguments;

        if(args != null){
            mCurrentItem =  args.containsKey(BUNDLE_TODO_ITEM) ? (TodoItem) arguments.getParcelable(BUNDLE_TODO_ITEM) : new TodoItem();;
            mCurrentDecriptionValue = args.containsKey(BUNDLE_DESRIPTION_VALUE) ? args.getString(BUNDLE_DESRIPTION_VALUE) : mCurrentItem.getDescription();
        }else{
            mCurrentItem = new TodoItem();
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            mListener = (iTodoItemChangeListener) getActivity();
        } catch (ClassCastException e) {
            throw new ClassCastException(getActivity().toString()
                    + " must implement iTodoItemChangeListener");
        }

    }


    @Override
    public void onStart() {
        super.onStart();
        mUnbinder = ButterKnife.bind(this, getDialog());
    }

    @Override
    public void onResume() {
        super.onResume();
        SetViewValues();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(BUNDLE_TODO_ITEM,mCurrentItem);
        outState.putString(BUNDLE_DESRIPTION_VALUE,inputDescription.getText().toString());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mUnbinder.unbind();
    }

    private void SetViewValues(){
        if(mCurrentDecriptionValue != null){
            inputDescription.setText(mCurrentDecriptionValue);
            inputDescription.setSelection(mCurrentDecriptionValue.length());
        }
    }


}
