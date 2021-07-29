package com.vip.apnaadda.ui;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.renderscript.ScriptGroup;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.vip.apnaadda.MainActivity;
import com.vip.apnaadda.MainActivity2;
import com.vip.apnaadda.R;
import com.vip.apnaadda.model.UserApi;
import com.vip.apnaadda.util.ImageUploadedCallback;
import com.vip.apnaadda.util.UserObject;

import java.util.Date;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class NewProfileActivity extends AppCompatActivity{

    private static final int GALLERY_CODE = 10;
    private EditText userName;
    private Button saveProfileButton;
    private CircleImageView userImage;
    private EditText userStatus;

    private final UserApi userApi = UserApi.getInstance();

    private ProgressDialog mProfileDialog;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference collectionReference = db.collection("Users");
    private Uri userImageUri;

    private FirebaseStorage storage = FirebaseStorage.getInstance();
    private StorageReference storageReference = storage.getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_profile);

        mProfileDialog = new ProgressDialog(this, R.style.ProgressDialogStyle);

        userName = findViewById(R.id.new_profile_name_edit_text);
        try {
            userName.setText(userApi.getName());
        } catch(Exception ignored){}

        userImage = findViewById(R.id.user_image_newProfile);
        userStatus = findViewById(R.id.user_status_newProfile);

        userImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent, GALLERY_CODE);
            }
        });

        saveProfileButton = findViewById(R.id.save_new_profile_button);
        saveProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);

                showProgressDialog();

                UserObject userObject = new UserObject();

                saveProfile(new ImageUploadedCallback() {
                    @Override
                    public void onTaskCompleted(String imageUrl) {
                        String status = userStatus.getText().toString().trim();
                        if(!TextUtils.isEmpty(userApi.getName()) && !TextUtils.isEmpty(userApi.getGender())) {
                            userObject.setName(userApi.getName());
                            userObject.setGender(userApi.getGender());
                            if(!TextUtils.isEmpty(status)) {
                                userObject.setStatus(status);
                            } else {
                                userObject.setStatus("");
                            }
                            userObject.setImageUrl(imageUrl);
                            userObject.setUid(userApi.getUserUid());
                            userObject.setTimeAdded(new Timestamp(new Date()));

                            Log.d("heyman", "saveProfile: " + userObject.getImageUrl());

                            collectionReference.add(userObject)
                                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                        @Override
                                        public void onSuccess(DocumentReference documentReference) {
                                            mProfileDialog.dismiss();

                                            startActivity(new Intent(NewProfileActivity.this, MainActivity2.class)
                                                    .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            mProfileDialog.hide();

                                            Toast.makeText(NewProfileActivity.this, e.toString(),
                                                    Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        } else {
                            Toast.makeText(NewProfileActivity.this, "Fields cannot be empty", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }

    private void showProgressDialog() {
        mProfileDialog.setTitle("Creating Profile");
        mProfileDialog.setMessage("Please wait while we create your profile.");
        mProfileDialog.setCanceledOnTouchOutside(false);
        mProfileDialog.show();
    }

    private void saveProfile(ImageUploadedCallback callback) {
        String name = userName.getText().toString().trim();
        userApi.setName(name);

//        UserObject userObject = new UserObject();

        if(userImageUri != null) {

            StorageReference imagePath = storageReference  // .../user_images/username_image
                    .child("user_images")
                    .child(userApi.getName().toLowerCase() + "_image");  // vipul_image

            imagePath.putFile(userImageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            imagePath.getDownloadUrl()
                                    .addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {
                                            String imageUrl = uri.toString();
//                                            userObject.setImageUrl(imageUrl);
                                            callback.onTaskCompleted(imageUrl);
                                        }

                                    });
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                        }
                    });
        } else {
//            userObject.setImageUrl("");
            callback.onTaskCompleted("");
        }



//        HashMap<String, Object> user = new HashMap<>();
//        user.put("name", userApi.getName());
//        user.put("gender", userApi.getGender());
//        user.put("uid", userApi.getUserUid());

//        collectionReference.add(user)
//                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
//                    @Override
//                    public void onSuccess(DocumentReference documentReference) {
//                        mProfileDialog.dismiss();
//
//                        startActivity(new Intent(NewProfileActivity.this, MainActivity.class)
//                            .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
//                    }
//                })
//                .addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        mProfileDialog.hide();
//
//                        Toast.makeText(NewProfileActivity.this, e.toString(),
//                                Toast.LENGTH_SHORT).show();
//                    }
//                });

    }

    @SuppressLint("NonConstantResourceId")
    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.radio_male:
                if(checked) {
                    userApi.setGender("Male");
                }
                break;
            case R.id.radio_female:
                if (checked)
                    userApi.setGender("Female");
                break;
            default:
                userApi.setGender("Not Described");
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == GALLERY_CODE && resultCode == RESULT_OK) {
            if(data != null) {
                userImageUri = data.getData();  // we have the actual path where image is stored in the device
                userImage.setImageURI(userImageUri);  // showing image on the image view
            }
        }
    }
}