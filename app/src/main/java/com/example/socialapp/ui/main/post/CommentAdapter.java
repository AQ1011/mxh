package com.example.socialapp.ui.main.post;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.socialapp.R;
import com.example.socialapp.data.model.ChildComment;
import com.example.socialapp.data.model.Comment;
import com.example.socialapp.data.model.ParentComment;
import com.example.socialapp.dialogs.AddCommentDialog;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.zip.Inflater;

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
                                    showAddDialog(comments.get(position).getId()));
                        }
                    }
        });
        if(comments.get(position).getClass().equals(ParentComment.class)){
            ArrayList<DocumentReference> children = ((ParentComment)(comments.get(position))).getChildren();
            for(int i = 0; i< children.size(); i ++) {
                LayoutInflater vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View v = vi.inflate(R.layout.item_comment_noreply, null);
                TextView usn = v.findViewById(R.id.tv_comment_username);
                TextView cm = v.findViewById(R.id.tv_comment_comment);
                children.get(i).get().addOnCompleteListener(task -> {
                    cm.setText(task.getResult().getString("content"));
                    FirebaseFirestore.getInstance().collection("users")
                            .whereEqualTo("uid",task.getResult().getString("userId")).get()
                            .addOnCompleteListener(task1 -> {
                                usn.setText(task1.getResult().getDocuments().get(0).getString("email"));
                            });
                });
                holder.getLinearLayout().addView(v);
            }
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        final TextView comment;
        final TextView userName;
        final ImageButton ib;
        final LinearLayout ll;
        onPostListener onPostListener;

        public ViewHolder(@NonNull View itemView, onPostListener onPostListener) {
            super(itemView);
            comment = itemView.findViewById(R.id.tv_comment_comment);
            userName = itemView.findViewById(R.id.tv_comment_username);
            ib = itemView.findViewById(R.id.ib_comment_reply);
            ll = itemView.findViewById(R.id.ll_child_comments);
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

        public LinearLayout getLinearLayout() {
            return this.ll;
        }

        @Override
        public void onClick(View v) {
//            onPostListener.onPostClick(getAdapterPosition());
        }
    }

    private void showAddDialog(String postId) {
        FragmentManager fm = ((AppCompatActivity)context).getSupportFragmentManager();
        AddCommentDialog addCommentDialog = AddCommentDialog.newInstance(postId,2);
        addCommentDialog.show(fm, "add_comment_dialog");
    }

    public interface onPostListener {
        void onPostClick(int position);
    }
}
