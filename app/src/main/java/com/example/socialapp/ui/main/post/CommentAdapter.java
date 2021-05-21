package com.example.socialapp.ui.main.post;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.socialapp.R;
import com.example.socialapp.data.model.Comment;
import com.example.socialapp.data.model.Post;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.io.InputStream;
import java.util.ArrayList;

import static android.content.ContentValues.TAG;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder> {

    private ArrayList<Comment> comments;
    private FirebaseFirestore db;
    private onPostListener onPostListener;

    public CommentAdapter (ArrayList<Comment> comments){
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
        holder.getUserName().setText(comments.get(position).getUserId());

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
            onPostListener.onPostClick(getAdapterPosition());
        }
    }

    public interface onPostListener {
        void onPostClick(int position);
    }
}
