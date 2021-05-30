package com.example.socialapp.dialogs;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.socialapp.R;

public class AddCommentDialog extends DialogFragment {
    EditText edAddComment;
    ImageButton ibSend;

    public AddCommentDialog() {
    }

    public static AddCommentDialog newInstance(){
        AddCommentDialog frag = new AddCommentDialog();
        Bundle args = new Bundle();
        frag.setArguments(args);
        return frag;
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,

                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.dialog_add_comment, container);

    }



    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);

        edAddComment = (EditText) view.findViewById(R.id.edt_add_comment);
        edAddComment.requestFocus();

        getDialog().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

    }

}
