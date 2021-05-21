package com.example.socialapp.ui.main.home;

import android.app.AlertDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.Observable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.socialapp.Facade;
import com.example.socialapp.R;
import com.example.socialapp.crud.PostActions;
import com.example.socialapp.data.model.Post;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

import static android.app.Activity.RESULT_OK;

public class HomeFragment extends Fragment implements PostAdapter.onPostListener {

    private FirebaseFirestore db;
    private HomeViewModel homeViewModel;
    RecyclerView recyclerView;
    PostAdapter postAdapter;

    private static final int PICK_IMAGE = 1;
    Uri imageUri;
    ImageView imageView;
    String picturePath;
    Facade facade;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        this.setHasOptionsMenu(true);

        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        homeViewModel.getPostsLiveData().observe(getViewLifecycleOwner(),postsUpdateObserver);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        facade = Facade.getInstance();
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        db = FirebaseFirestore.getInstance();

        recyclerView = root.findViewById(R.id.rv_posts);

        return root;
    }

    Observer<ArrayList<Post>> postsUpdateObserver = new Observer<ArrayList<Post>>() {
        @Override
        public void onChanged(ArrayList<Post> posts) {
            postAdapter = new PostAdapter(posts, HomeFragment.this::onPostClick);
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            recyclerView.setAdapter(postAdapter);
        }
    };


    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {

        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();

        AlertDialog al = new AlertDialog.Builder(this.getContext())
            .setTitle("Add new Post").create();

        al.setView(View.inflate(this.getContext(),R.layout.dialog_add_post,null));

        MenuItem add = menu.add(0,0,0,"New post");
        add.setOnMenuItemClickListener(item -> {
            al.show();
            imageUri = null;
            ImageButton imageButton = al.findViewById(R.id.btn_new_post_add_image);
            ImageButton btn_addPost = al.findViewById(R.id.btn_new_post_add_post);
            EditText ed_addPost = al.findViewById(R.id.ed_add_post);
            imageView  = al.findViewById(R.id.iv_add_post);
            imageButton.setOnClickListener(v -> {
                Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(pickPhoto , 1);//one can be replaced with any action code
            });
            btn_addPost.setOnClickListener(v -> {
                Post p = new Post();
                if(imageUri != null) {
                    String result = picturePath;
                    int cut = result.lastIndexOf('/');
                    if (cut != -1) {
                        result = result.substring(cut + 1);
                    }

                    StorageReference imageRef = storageRef.child("images/" + result);

                    imageRef.putFile(imageUri).addOnSuccessListener(taskSnapshot -> {
                        imageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                            p.setImageUrl(uri.toString());
                            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                            p.setUserId(user.getUid());
                            p.setContent(ed_addPost.getText().toString());
                            //TODO: do this please!!
                            //facade.post
                            al.cancel();
                        });
                    });
                } else {
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    p.setUserId(user.getUid());
                    p.setContent(ed_addPost.getText().toString());
                    db.collection("posts").add(p);
                    al.cancel();
                }
            });
            Toast.makeText(this.getContext(), "New post", Toast.LENGTH_SHORT).show();
            return true;
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == PICK_IMAGE){
            Uri selectedImage =  data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            if (selectedImage != null) {
                Cursor cursor = getContext().getContentResolver().query(selectedImage,
                        filePathColumn, null, null, null);
                if (cursor != null) {
                    cursor.moveToFirst();

                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    picturePath = cursor.getString(columnIndex);
                    imageUri = selectedImage;
                    imageView.setImageURI(selectedImage);
                    cursor.close();

                }
            };
        }
    }

    @Override
    public void onPostClick(int position) {
        String postId = postAdapter.getPostId(position);
        NavDirections action = HomeFragmentDirections.actionNavHomeToPostFragment().setPostId(postId);
        Navigation.findNavController(getView()).navigate(action);
    }
}