package com.example.socialapp.ui.main.home;

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
import com.example.socialapp.data.model.Post;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.io.InputStream;
import java.util.ArrayList;

import static android.content.ContentValues.TAG;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder> {

    private ArrayList<Post> posts;
    private FirebaseFirestore db;

    public PostAdapter (ArrayList<Post> posts){
        this.posts = posts;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_post, parent, false);

        return new ViewHolder(view);

    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        db = FirebaseFirestore.getInstance();
        db.collection("users").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot document : task.getResult()) {
                    if(document.getString("uid").equals(posts.get(position).getUserId())) {
                        new DownloadImageTask(holder.getAvatar())
                                .execute(document.getString("avatar"));
                        holder.getUserName().setText(document.getString("email"));
                    }
                }
            } else {
                Log.w(TAG, "Error getting documents.", task.getException());
            }
        });
        holder.getContentText().setText(posts.get(position).toString());
        new DownloadImageTask(holder.getImageView())
                .execute(posts.get(position).getImageUrl());

    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        final TextView contentText;
        final TextView userName;
        final ImageView imageView;
        final ImageView avatar;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            contentText = itemView.findViewById(R.id.tv_post_content);
            imageView = itemView.findViewById(R.id.iv_post_image);
            avatar = itemView.findViewById(R.id.iv_post_avatar);
            userName = itemView.findViewById(R.id.tv_post_username);
        }

        public ImageView getImageView() {
            return this.imageView;
        }

        public TextView getContentText() {
            return this.contentText;
        }

        public TextView getUserName() {
            return this.userName;
        }

        public ImageView getAvatar() {
            return this.avatar;
        }
    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }
}
