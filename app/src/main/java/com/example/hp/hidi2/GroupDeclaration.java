package com.example.hp.hidi2;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class GroupDeclaration extends AppCompatActivity {

    ArrayList<String> arrayListFinalGroupName = new ArrayList<>();
    ArrayList<String> arrayListFinalGroupImage = new ArrayList<>();
    ArrayList<String> arrayListFinalGroupUid = new ArrayList<>();
    AddPeopleAdapter addPeopleAdapter;
    private StorageReference mStorageReference;
    ArrayList<AddPeopleSet> addPeopleSets = new ArrayList<>();
    private RecyclerView recyclerView;
    String name, uid, image;
    private Uri imageUri;
    AddPeopleSet addPeopleSet;
    SessionManager session;
    DatabaseReference databaseReference;
    EditText editText;
    CircleImageView circleImageView;
    FloatingActionButton floatingActionButton;
    public static final String STORAGE_PATH_GROUP = "group/";
    public static final int REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_declaration);
        recyclerView = findViewById(R.id.recyclerViewFinalGroupList);
        editText = findViewById(R.id.groupName);
        circleImageView = findViewById(R.id.groupimage);
        floatingActionButton = findViewById(R.id.confirmGroup);
        session = new SessionManager(getApplicationContext());
        databaseReference = FirebaseDatabase.getInstance().getReference();
        mStorageReference = FirebaseStorage.getInstance().getReference();
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        arrayListFinalGroupName = bundle.getStringArrayList("arraylistsendname");
        arrayListFinalGroupImage = bundle.getStringArrayList("arraylistsendimage");
        arrayListFinalGroupUid = bundle.getStringArrayList("arraylistsendUid");
        for (int i = 0; i < arrayListFinalGroupName.size(); i++) {
            name = arrayListFinalGroupName.get(i);
            uid = arrayListFinalGroupUid.get(i);
            image = arrayListFinalGroupImage.get(i);
            addPeopleSet = new AddPeopleSet(image, uid, name);
            addPeopleSets.add(addPeopleSet);
        }
        addPeopleAdapter = new AddPeopleAdapter(getApplicationContext(), addPeopleSets);
        recyclerView.setAdapter(addPeopleAdapter);
        circleImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnUploadGroupImage();
            }
        });
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnUploadGroupDetails();
                Intent intent1 = new Intent(GroupDeclaration.this,ChatList.class);
                startActivity(intent1);
            }
        });
    }

    private void btnUploadGroupImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Image"), REQUEST_CODE);
    }

    private void btnUploadGroupDetails() {
        if (imageUri != null) {
            final ProgressDialog dialog = new ProgressDialog(this);
            dialog.setTitle("Uploading Image...");
            dialog.show();
            //get Storage Reference
            StorageReference ref = mStorageReference.child(STORAGE_PATH_GROUP + System.currentTimeMillis() + "." + getImageExt(imageUri));
            //add file to refrence
            ref.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    dialog.dismiss();
                    Toast.makeText(GroupDeclaration.this, "IMAGE UPLOADED", Toast.LENGTH_LONG).show();
                    GroupDeclerationSet groupDeclerationSet = new GroupDeclerationSet(session.getUID() + "", taskSnapshot.getDownloadUrl().toString(), "", "", "", editText.getText().toString(), "Group Chat");
                    String uploadUrl = databaseReference.push().getKey();
                    databaseReference.child("threads").child(uploadUrl).setValue(groupDeclerationSet);
                    for (int j = 0; j < arrayListFinalGroupImage.size(); j++) {
                        String namehidi = arrayListFinalGroupName.get(j);
                        String uidhidi = arrayListFinalGroupUid.get(j);
                        databaseReference.child("threads").child(uploadUrl).child("participants").child(uidhidi).setValue(namehidi);
                        databaseReference.child("users").child(uidhidi).child("threads").child(uploadUrl).setValue(true);
                    }
                }
            })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            dialog.dismiss();
                            Toast.makeText(GroupDeclaration.this, e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                            dialog.setMessage("Uploading" + (int) progress + "%");
                        }
                    });
        } else {
            Toast.makeText(GroupDeclaration.this, "Please Select image", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            try {
                Bitmap bm = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                circleImageView.setImageBitmap(bm);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public String getImageExt(Uri uri) {
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }
}

