package com.example.socialapp.ui.main.user;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.socialapp.Facade;
import com.example.socialapp.R;
import com.example.socialapp.data.LoginDataSource;
import com.example.socialapp.data.LoginRepository;
import com.example.socialapp.data.model.LoggedInUser;
import com.example.socialapp.data.model.User;
import com.example.socialapp.ui.main.home.PostAdapter;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.FileNotFoundException;
import java.io.InputStream;

public class UserFragment extends Fragment {
    LoginRepository loginRepository;
    TextView txtName, txtEmail, txtPhone, txtBirthday, txtWork, txtAddress;
    ImageView imgViewAvatar;
    Facade facade = Facade.getInstance();
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseUser user = firebaseAuth.getCurrentUser();

    // Avatar
    private static final int RESULT_OK = -1;
    int REQUEST_CODE_CAMERA = 123;
    int REQUEST_CODE_FOLDER = 456;

    View root;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.setHasOptionsMenu(true);

        root = inflater.inflate(R.layout.fragment_user, container, false);

        Mapping();

        loginRepository = LoginRepository.getInstance(new LoginDataSource());
        //FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        //FirebaseUser user = firebaseAuth.getCurrentUser();
        /*final TextView name = root.findViewById(R.id.tv_user_frag_name);
        final TextView uid = root.findViewById(R.id.tv_user_frag_id);
        name.setText(user.getEmail());
        uid.setText(user.getUid());*/
        //txtPhone.setText(user.getUid());

        facade.getUser(user.getUid()).addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                txtName.setText(task.getResult().getDocuments().get(0).getString("name"));
                txtEmail.setText(task.getResult().getDocuments().get(0).getString("email"));
                txtPhone.setText(task.getResult().getDocuments().get(0).getString("phone"));
                txtBirthday.setText(task.getResult().getDocuments().get(0).getString("birthday"));
                txtWork.setText(task.getResult().getDocuments().get(0).getString("work"));
                txtAddress.setText(task.getResult().getDocuments().get(0).getString("address"));
                facade.getUser(user.getUid()).addOnSuccessListener(documentSnapshots -> {
                   String url = documentSnapshots.getDocuments().get(0).getString("avatar");
                    new PostAdapter.DownloadImageTask(imgViewAvatar)
                            .execute(url);
                });
            }
        });
        imgViewAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popup = new PopupMenu(getContext(), v);
                popup.inflate(R.menu.photo);
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.action_camera:
                                Intent intent1 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                startActivityForResult(intent1,REQUEST_CODE_CAMERA);
                                return true;
                            case R.id.action_gallery:
                                Intent intent2 = new Intent(Intent.ACTION_PICK);
                                intent2.setType(("image/*"));
                                startActivityForResult(intent2, REQUEST_CODE_FOLDER);
                                return true;
                            default:
                                return false;
                        }
                    }
                });
                popup.show();
            }
        });

        return root;
    }

    private void Mapping() {
        txtName         = (TextView) root.findViewById(R.id.tv_name);
        txtEmail        = (TextView) root.findViewById(R.id.tv_email);
        txtPhone        = (TextView) root.findViewById(R.id.tv_phone);
        txtBirthday     = (TextView) root.findViewById(R.id.tv_birthday);
        txtWork         = (TextView) root.findViewById(R.id.tv_work);
        txtAddress      = (TextView) root.findViewById(R.id.tv_address);
        imgViewAvatar   = (ImageView) root.findViewById(R.id.imageViewAvatar);
    }



    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.action_editProfile) {
//            Toast.makeText(getActivity(), "ok", Toast.LENGTH_SHORT).show();
            showDialog();
        }
        if(item.getItemId() == R.id.action_changepassword)
        {
//            Toast.makeText(getActivity(), "change password", Toast.LENGTH_SHORT).show();
            showDialogChangePass();

        }
        return super.onOptionsItemSelected(item);
    }

    public void showDialog()
    {
        Dialog dialog = new Dialog(getActivity(),android.R.style.Theme_Light_NoTitleBar_Fullscreen);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_edit_profile);

        EditText edtName        = (EditText) dialog.findViewById(R.id.editTextNameEdit);
        EditText edtEmail       = (EditText) dialog.findViewById(R.id.editTextEmailEdit);
        EditText edtPhone       = (EditText) dialog.findViewById(R.id.editTextPhoneEdit);
        EditText edtAddress     = (EditText) dialog.findViewById(R.id.editTextAddressEdit);
        Button btnXacNhan       = (Button) dialog.findViewById(R.id.buttonXacNhan);
        Button btnHuyEdit       = (Button) dialog.findViewById(R.id.buttonHuyEdit);


        btnXacNhan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String uid          = user.getUid();
                String name         = edtName.getText().toString();
                String email        = edtEmail.getText().toString();
                String phone        = edtPhone.getText().toString();
                String address      = edtAddress.getText().toString();

                if(name.equals(" ") || email.equals(" ") || phone.equals(" ") || address.equals(""))
                {
                    Toast.makeText(getActivity(), "Vui lòng nhập đầy đủ thông tin!" + uid, Toast.LENGTH_SHORT).show();
                }
                else
                {
                    //facade.updateProfile(new User(uid, name, phone, email, address));
                    User u = new User();
                    u.setUid(uid);
                    u.setName(name);
                    u.setEmail(email);
                    u.setPhone(phone);
                    u.setAddress(address);
                    facade.updateProfile(u);
                    Toast.makeText(getActivity(), "Đã cập nhật!.", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }
            }
        });

        btnHuyEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();

    }

    public void showDialogChangePass()
    {
        Dialog dialog = new Dialog(getActivity(),android.R.style.Theme_Light_NoTitleBar_Fullscreen);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_change_password);

        EditText edtCurrPass        = (EditText) dialog.findViewById(R.id.editTextCurPass);
        EditText edtNewPass         = (EditText) dialog.findViewById(R.id.editTextNewPass);
        EditText edtNewPass2        = (EditText) dialog.findViewById(R.id.editTextNewPass2);
        Button btnXacNhan           = (Button) dialog.findViewById(R.id.buttonXacNhan);
        Button btnHuyEdit           = (Button) dialog.findViewById(R.id.buttonHuyEdit);


        btnXacNhan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = user.getEmail();
                String uid              = user.getUid();
                String currPass         = edtCurrPass.getText().toString();
                String newPass          = edtNewPass.getText().toString();
                String newPass2         = edtNewPass2.getText().toString();

                if(currPass.equals(" ") || newPass.equals(" "))
                {
                    Toast.makeText(getActivity(), "Vui lòng nhập đầy đủ!" + uid, Toast.LENGTH_SHORT).show();
                    return;
                }
                if(currPass.length()<6 || newPass.length()<6)
                {
                    Toast.makeText(getActivity(), "Mật khẩu phải dài hơn 6 ký tự" + uid, Toast.LENGTH_SHORT).show();
                    return;
                }
                if(!newPass.equals(newPass2))
                {
                    Toast.makeText(getActivity(), "Mật khẩu không giống" + uid, Toast.LENGTH_SHORT).show();
                    return;
                }
                facade.changePass(email, currPass, newPass);
                Toast.makeText(getActivity(), "Đã cập nhật!.", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });

        btnHuyEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    public void editAvatar()
    {


    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode == REQUEST_CODE_CAMERA && resultCode == RESULT_OK && data != null) {
            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
            imgViewAvatar.setImageBitmap(bitmap);
            facade.changeProfilePicture(bitmap);
        }

        if(requestCode == REQUEST_CODE_FOLDER && resultCode == RESULT_OK && data != null) {
            Uri uri = data.getData();
            try {

                InputStream inputStream = getActivity().getContentResolver().openInputStream(uri);
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                imgViewAvatar.setImageBitmap(bitmap);
                facade.changeProfilePicture(bitmap);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

}
