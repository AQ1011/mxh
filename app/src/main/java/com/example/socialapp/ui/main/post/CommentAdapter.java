package com.example.socialapp.ui.main.post;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.socialapp.R;
import com.example.socialapp.data.model.ChildComment;
import com.example.socialapp.data.model.Comment;
import com.example.socialapp.data.model.ParentComment;
import com.example.socialapp.dialogs.AddCommentDialog;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder> {

    private ArrayList<Comment> comments;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private onPostListener onPostListener;
    private Context context;

    public CommentAdapter (ArrayList<Comment> comments, Context context){
        this.comments = comments;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_comment, parent, false);
        return new ViewHolder(view, onPostListener);

    }

    @Override
    public int getItemCount() {
        return comments.size();
    }

    public String getCommentId(int position){
        return comments.get(position).getPostId();
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.getComment().setText(comments.get(position).getContent());
        db.collection("users")
                .whereEqualTo("uid", comments.get(position).getUserId()).get().addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            holder.getUserName().setText(document.getString("email"));
                            holder.getReplyButton().setOnClickListener(v ->
                                    showAddDialog(comments.get(position).getPostId()));
                        }
                    }
        });

    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        final TextView comment;
        final TextView userName;
        final ImageButton ib;
        onPostListener onPostListener;

        public ViewHolder(@NonNull View itemView, onPostListener onPostListener) {
            super(itemView);
            comment = itemView.findViewById(R.id.tv_comment_comment);
            userName = itemView.findViewById(R.id.tv_comment_username);
            ib = itemView.findViewById(R.id.ib_comment_reply);
            this.onPostListener = onPostListener;
            itemView.setOnClickListener(this);
        }

        public TextView getUserName() {
            return this.userName;
        }

        public TextView getComment() {
            return this.comment;
        }

        public ImageButton getReplyButton() {
            return this.ib;
        }

        @Override
        public void onClick(View v) {
//            onPostListener.onPostClick(getAdapterPosition());
        }
    }

    private void showAddDialog(String postId) {
        FragmentManager fm = ((AppCompatActivity)context).getSupportFragmentManager();
        AddCommentDialog addCommentDialog = AddCommentDialog.newInstance(postId,1);
        addCommentDialog.show(fm, "add_comment_dialog");
    }

    public interface onPostListener {
        void onPostClick(int position);
    }
}
