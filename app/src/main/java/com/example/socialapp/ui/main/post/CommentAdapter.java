package com.example.socialapp.ui.main.post;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.socialapp.R;
import com.example.socialapp.data.model.ChildComment;
import com.example.socialapp.data.model.ParentComment;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder> {

    private ArrayList<ChildComment> comments;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private onPostListener onPostListener;

    public CommentAdapter (ArrayList<ChildComment> comments){
        this.comments = comments;
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
                        }
                    }
        });

    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        final TextView comment;
        final TextView userName;

        onPostListener onPostListener;

        public ViewHolder(@NonNull View itemView, onPostListener onPostListener) {
            super(itemView);

            comment = itemView.findViewById(R.id.tv_comment_comment);
            userName = itemView.findViewById(R.id.tv_comment_username);
            this.onPostListener = onPostListener;

            itemView.setOnClickListener(this);
        }

        public TextView getUserName() {
            return this.userName;
        }

        public TextView getComment() {
            return this.comment;
        }

        @Override
        public void onClick(View v) {
//            onPostListener.onPostClick(getAdapterPosition());
        }
    }

    public interface onPostListener {
        void onPostClick(int position);
    }
}
