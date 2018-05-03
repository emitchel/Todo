package com.kylehanish.todo.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.kylehanish.todo.R;
import com.kylehanish.todo.classes.TodoItem;
import com.kylehanish.todo.interfaces.iTodoItemChangeListener;
import com.kylehanish.todo.utility.FormatterUtils;

import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static com.kylehanish.todo.fragments.TodoDialogFragment.BUNDLE_DESRIPTION_VALUE;

/**
 * Created by Kyle Hanish on 4/29/18.
 */

public class PreviewDialogFragment extends DialogFragment {


    @BindView(R.id.preview_text)
    TextView previewText;

    @BindView(R.id.created_on)
    TextView createdOnDate;

    public static final String TAG = PreviewDialogFragment.class.getSimpleName();
    public static final String BUNDLE_TODO_ITEM = "todo_item";


    private Unbinder mUnbinder;
    private TodoItem mCurrentItem;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        setValuesFromBundle(getArguments(), savedInstanceState);
        builder.setPositiveButton(getString(R.string.close), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        setCancelable(false);

        builder.setView(getActivity().getLayoutInflater().inflate(R.layout.todo_preview, null));

        return builder.create();
    }


    private void setValuesFromBundle(Bundle arguments, Bundle savedInstanceState) {
        Bundle args = savedInstanceState != null ? savedInstanceState : arguments;

        if (args != null) {
            mCurrentItem = args.containsKey(BUNDLE_TODO_ITEM) ? (TodoItem) arguments.getParcelable(BUNDLE_TODO_ITEM) : new TodoItem();
            ;
        } else {
            dismiss();
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        outState.putParcelable(BUNDLE_TODO_ITEM, mCurrentItem);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mUnbinder.unbind();
    }

    private void SetViewValues() {
        previewText.setText(mCurrentItem.getDescription());
        if(createdOnDate != null){
            createdOnDate.setText(FormatterUtils.GetDisplayDateFormat(mCurrentItem.getCreatedOn()));
        }
    }


}
