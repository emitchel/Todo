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

    private Unbinder mUnbinder;
    private iTodoItemChangeListener mListener;


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        Bundle args = getArguments();
        builder.setTitle(getString(R.string.add_new_item));

         builder.setPositiveButton(getString(R.string.save), new DialogInterface.OnClickListener() {
             @Override
             public void onClick(DialogInterface dialog, int which) {
                 String test = inputDescription.getText().toString();
                 TodoItem newItem = new TodoItem();
                 newItem.setCreatedOn(new Date());
                 newItem.setDescription(inputDescription.getText().toString());

                 mListener.SaveTodoItem(newItem,null);
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

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mListener = (iTodoItemChangeListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement iTodoItemChangeListener");
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
        mUnbinder = ButterKnife.bind(this, getDialog());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mUnbinder.unbind();
    }


}
