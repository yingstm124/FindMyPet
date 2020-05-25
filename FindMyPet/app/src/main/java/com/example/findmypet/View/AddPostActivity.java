package com.example.findmypet.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DownloadManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;


import com.example.findmypet.R;
import com.example.findmypet.Model.PostInfo;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.net.URL;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;


public class AddPostActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private static ImageView image;
    private PostInfo postInfo;

    private String type_pet;
    final private int GALLERY = 100;

    public Uri selectedImage;
    private String keepimage;
    private UploadTask uploadTask;
    private Uri pickImageuri = null;

    static EditText title;
    static EditText location;
    static EditText tel;
    static EditText description;


    // generate key
    private String name_img;
    final private String lexicon = "ABCDEFGHIJKLMNOPQRSTUVWXYZ12345674890";
    final private java.util.Random rand = new java.util.Random();
    final private Set<String> identifiers = new HashSet<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_post);

        ImageView img = (ImageView)findViewById(R.id.add_img);

        // init
        title = (EditText)findViewById(R.id.add_title);
        location = (EditText)findViewById(R.id.add_location);
        tel = (EditText)findViewById(R.id.add_tel);
        description = (EditText)findViewById(R.id.add_des);
        image = (ImageView)findViewById(R.id.add_img);

        // when user click  Image View      choose picture on gallery
        //                  Upload button   upload to Firebase

        settingToolbar();
        settingChoosePet();
        ClickUploadImage();
        ClickUploadButton();

    }

    //----- toolbar ------///////////////////////////////////////////////////////////////////////////

    protected void settingToolbar(){
        androidx.appcompat.widget.Toolbar toolbar = findViewById(R.id.toolbar_Addpost);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        // add back arrow toolbar
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
    }
    // handle arrow click here
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }

        return super.onOptionsItemSelected(item);
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////



    //----- select type pet-----/////////////////////////////////////////////////////////////////////
    protected void settingChoosePet(){
        Spinner spinner = (Spinner) findViewById(R.id.type);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(AddPostActivity.this, R.array.type_pet, android.R.layout.simple_spinner_item );
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(AddPostActivity.this);
    }
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        type_pet = parent.getItemAtPosition(position).toString();
        Toast.makeText(parent.getContext(), type_pet, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    /////////////////////////////////////////////////////////////////////////////////////////////////




    //------ Select Image ----///////////////////////////////////////////////////////////////////////

    private void ClickUploadImage(){
        // init image
        ImageView img = (ImageView)findViewById(R.id.add_img);
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // show pop up for selec gallery
                AlertDialog.Builder pictureDialog = new AlertDialog.Builder(AddPostActivity.this);
                pictureDialog.setTitle("Select Action");
                String[] pictureDialogItems = {"Select photo from gallery"};
                pictureDialog.setItems(pictureDialogItems, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                choosePhotoFromGallary();
                                break;
                        }
                    }
                });
                pictureDialog.show();
            }
        });
    }

    private void choosePhotoFromGallary(){
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, GALLERY);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK && requestCode == GALLERY){
            switch (requestCode){
                case GALLERY:
                    image = (ImageView) findViewById(R.id.add_img);
                    selectedImage = data.getData();

                    // show image
                    image.setImageURI(selectedImage);
                    break;
            }

        }


    }

    //////////////////////////////////////////////////////////////////////////////////////////////////



    //--------Click upload button -----------//////////////////////////////////////////////////////////

    public void ClickUploadButton(){

        // confirm post information
        // put information to Firebase  - ref " Post "
        //                              - Post
        //                                  - UserId (String)
        //                                      - title (String)
        //                                      - name (String)
        //                                      - description (String)
        //                                      - location (String)
        //                                      - tel no (String)
        //                                      - picture (Uri)

        Button btn_up = (Button) findViewById(R.id.btn_upload);
        btn_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // check user have to every text edit
                if(!isEmpty(title) && !isEmpty(location) && !isEmpty(tel) && !isEmpty(description)){

                    postInfo = new PostInfo();
                    name_img = randomIdentifier();

                    putImageToFirebase();

                    //Toast.makeText(getApplicationContext(), "uploade to realtime database", Toast.LENGTH_LONG).show();
                    finish();
                }

            }
        });


    }
    private boolean isEmpty(EditText etText) {
        if (etText.getText().toString().trim().length() > 0) {
            return false;
        }
        return true;
    }
    private void putImageToFirebase(){

        StorageReference storageRef = FirebaseStorage.getInstance().getReference();
        String child_name = "Pet"+name_img;

        final StorageReference ref = storageRef.child(child_name);

        // solution 1
//        ref.putFile(selectedImage)
//                .continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
//                    @Override
//                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
//                        if(!task.isSuccessful()){
//                            throw task.getException();
//                        }
//                        return ref.getDownloadUrl();
//                    }
//                })
//                .addOnCompleteListener(new OnCompleteListener<Uri>() {
//                    @Override
//                    public void onComplete(@NonNull Task<Uri> task) {
//                        if(task.isSuccessful()){
//                            Uri downUri = task.getResult();
//                            selectedImage = downUri;
//                            //String selected = selectedImage.toString().replaceAll("[https://]", "");
//                            //Log.d("before put in set image" , "is "+ selected);
//                            postInfo.setImage(downUri.toString());
//
//                        }
//
//                    }
//                });
        // solution 2

        ref.putFile(selectedImage).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        keepimage = uri.toString();
                        //Log.d("Sukkkk", "onSuccess: uri= "+ keepimage);
                        PutInformationOnFirebase();

                    }
                });
            }
        });





    }


    private void PutInformationOnFirebase(){

        final DatabaseReference database = FirebaseDatabase.getInstance().getReference("Post").child(type_pet);

        postInfo.setTitle(title.getText().toString());
        postInfo.setDescription(description.getText().toString());
        postInfo.setLocation(location.getText().toString());
        postInfo.setTel(tel.getText().toString());
        postInfo.setImageUrl(pickImageuri);

        Log.d("selectedImage" , "is "+ selectedImage.toString());
        //postInfo.setImage(selectedImage.toString());

        // put in firebase
        HashMap<String , Object> hashMap = new HashMap<>();

        hashMap.put("title", postInfo.getTitle());
        hashMap.put("location", postInfo.getLocation());
        hashMap.put("tel", postInfo.getTel());
        hashMap.put("description", postInfo.getDescription());

        Log.d("postinfo image" , "is "+ postInfo.getImage());
        
        hashMap.put("image", ""+keepimage);
        database.push().setValue(hashMap);


        /// put on history in firebase

        final DatabaseReference databaseHistory = FirebaseDatabase.getInstance().getReference("MyHistory").child(FirebaseAuth.getInstance().getCurrentUser().getUid());

        postInfo.setTitle(title.getText().toString());
        postInfo.setDescription(description.getText().toString());
        postInfo.setLocation(location.getText().toString());
        postInfo.setTel(tel.getText().toString());
        postInfo.setImage(selectedImage.toString());

        HashMap<String , Object> hashMap1 = new HashMap<>();

        hashMap1.put("title", postInfo.getTitle());
        hashMap1.put("location", postInfo.getLocation());
        hashMap1.put("tel", postInfo.getTel());
        hashMap1.put("description", postInfo.getDescription());
        //hashMap.put("type", type_pet);
        hashMap1.put("image", ""+keepimage);

        databaseHistory.push().setValue(hashMap);

    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////





    // --------- not working module ----------///////////////////////////////////////////////////////////

    // generate key
    private String randomIdentifier(){

        StringBuilder builder = new StringBuilder();
        while(builder.toString().length() == 0) {
            int length = rand.nextInt(5)+5;
            for(int i = 0; i < length; i++) {
                builder.append(lexicon.charAt(rand.nextInt(lexicon.length())));
            }
            if(identifiers.contains(builder.toString())) {
                builder = new StringBuilder();
            }
        }
        return builder.toString();

    }

}
