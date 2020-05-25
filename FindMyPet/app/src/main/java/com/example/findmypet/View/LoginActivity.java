package com.example.findmypet.View;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.findmypet.R;
import com.example.findmypet.Model.Userlogin;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Arrays;
import java.util.HashMap;


public class LoginActivity extends AppCompatActivity {

    private FirebaseUser user;
    private FirebaseAuth auth;
    private Button btn_facebook;
    private CallbackManager callbackManager;
    private String Email;
    private String Name;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // initiallize
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        // login
        onClickFacebookLogin();
    }


    protected void onClickFacebookLogin(){
        // facebook
        // facebook login button invisible
        // manage login
        callbackManager = CallbackManager.Factory.create();
        btn_facebook = findViewById(R.id.Fb_login_button);

        btn_facebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_facebook.setVisibility(View.INVISIBLE);
                ManageLogin();

            }
        });
    }

    protected void ManageLogin(){
        LoginManager.getInstance().logInWithReadPermissions(LoginActivity.this, Arrays.asList("email", "public_profile"));
        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                handleFacebookAccessToken(loginResult.getAccessToken());
            }
            @Override
            public void onCancel() {
                Toast.makeText(getApplicationContext(), "User Cancel it", Toast.LENGTH_LONG).show();
                Intent goLogin = new Intent(LoginActivity.this, LoginActivity.class);
                startActivity(goLogin);
            }
            @Override
            public void onError(FacebookException error) {
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void handleFacebookAccessToken(AccessToken token) {
        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        auth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = auth.getCurrentUser();
                            getUserDataOnFirebase(user);
                        }
                        else {
                            Toast.makeText(getApplicationContext(), "Cound not register to firebase", Toast.LENGTH_LONG).show();
                        }

                    }
                });
    }

    protected void getUserDataOnFirebase(final FirebaseUser user){
        // add Reference "User" on firebase
        // collect user data
        // user data
        //         - user_id
        //         - user_name
        //         - Email
        Email = user.getEmail();
        Name = user.getDisplayName();

        final Uri photoUrl = user.getPhotoUrl();
        final String id = user.getUid();

        final DatabaseReference database = FirebaseDatabase.getInstance().getReference("User").child(id);

        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    goHome();
                }
                else{
                    // save data to UserLogin class
                    Userlogin userlogin = new Userlogin();
                    userlogin.setUserId(id);
                    userlogin.setEmail(Email);
                    userlogin.setName(Name);
                    userlogin.setPhotoUri(photoUrl);

                    HashMap<String , Object> hashMap = new HashMap<>();
                    hashMap.put("ID", userlogin.getUserId());
                    hashMap.put("Email", userlogin.getEmail());
                    hashMap.put("Name", userlogin.getName());
                    hashMap.put("photo", userlogin.getPhotoUri().toString());
                    database.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            goHome();
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), "Database error", Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void goHome(){
        Intent home = new Intent(LoginActivity.this, HomeActivity.class);
        startActivity(home);
    }

}
