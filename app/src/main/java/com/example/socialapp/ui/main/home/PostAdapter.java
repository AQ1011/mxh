package com.example.socialapp.ui.main.home;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.socialapp.Facade;
import com.example.socialapp.R;
import com.example.socialapp.data.model.Post;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;

import static android.content.ContentValues.TAG;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder> {

    private ArrayList<Post> posts;
    private FirebaseFirestore db;
    private onPostListener onPostListener;
    Facade facade = Facade.getInstance();

    public PostAdapter (ArrayList<Post> posts, onPostListener onPostListener){
        this.posts = posts;
        this.onPostListener = onPostListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_post, parent, false);

        return new ViewHolder(view, onPostListener);

    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    public String getPostId(int position){
        return posts.get(position).getPostId();
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
        holder.getLikebtn().setText(String.valueOf(posts.get(position).getLike()));
        holder.getLikebtn().setOnClickListener(v -> {
            facade.likePost(posts.get(position).getPostId());
            holder.getLikebtn().setText(String.valueOf(Long.valueOf(holder.getLikebtn().getText().toString())+1));
        });
    }

    public static Drawable LoadImageFromWebOperations(String url) {
        try {
            InputStream is = (InputStream) new URL(url).getContent();
            Drawable d = Drawable.createFromStream(is, "src name");
            return d;
        } catch (Exception e) {
            return null;
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        final TextView contentText;
        final TextView userName;
        final ImageView imageView;
        final ImageView avatar;
        final Button likebtn;

        onPostListener onPostListener;

        public ViewHolder(@NonNull View itemView, onPostListener onPostListener) {
            super(itemView);

            contentText = itemView.findViewById(R.id.tv_post_content);
            imageView = itemView.findViewById(R.id.iv_post_image);
            avatar = itemView.findViewById(R.id.iv_post_avatar);
            userName = itemView.findViewById(R.id.tv_post_username);
            likebtn = itemView.findViewById(R.id.button_thumbup);
            this.onPostListener = onPostListener;

            itemView.setOnClickListener(this);
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

        public Button getLikebtn() {return this.likebtn; }

        @Override
        public void onClick(View v) {
            onPostListener.onPostClick(getAdapterPosition());
        }
    }

    public static class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
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

    public interface onPostListener {
        void onPostClick(int position);
    }
}
