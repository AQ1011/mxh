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

import com.example.socialapp.Facade;
import com.example.socialapp.R;
import com.example.socialapp.data.model.ChildComment;
import com.example.socialapp.data.model.Comment;

public class AddCommentDialog extends DialogFragment {
    EditText edAddComment;
    ImageButton ibSend;
    Facade facade = Facade.getInstance();

    public AddCommentDialog() {
    }

    public static AddCommentDialog newInstance(String postId, int type){
        AddCommentDialog frag = new AddCommentDialog();
        Bundle args = new Bundle();
        args.putString("postId",postId);
        args.putInt("type",type);
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
        ibSend = (ImageButton) view.findViewById(R.id.ib_send_button);

        edAddComment.requestFocus();

        getDialog().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        Integer type = getArguments().getInt("type");
        String postId = getArguments().getString("postId");
        ibSend.setOnClickListener(v -> {
            if(type == 1 ){
                addCommentToPost(postId);
            }
            else {
                addReplyToComment(postId);
            }
            edAddComment.setText("");
            this.dismiss();
        });
    }

    private void addCommentToPost(String postId) {
        if(edAddComment.getText().toString().equals("")) {
            return;
        }
        Comment c = new ChildComment();
        c.setPostId(postId);
        c.setUserId(facade.getCurrentUserId());
        c.setContent(edAddComment.getText().toString());
        facade.addComment(c);
    }

    private void addReplyToComment(String postId) {

    }

}
