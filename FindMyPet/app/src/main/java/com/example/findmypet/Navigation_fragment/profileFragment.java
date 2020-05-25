package com.example.findmypet.Navigation_fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.findmypet.Model.Userlogin;
import com.example.findmypet.R;
import com.example.findmypet.View.LoginActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class profileFragment extends Fragment {

    View v;

    private Toolbar toolbar;
    private ImageView image_profile;


    private Button logoutbtn;
    private FirebaseAuth mAuth;

    private TextView Email_tv;
    private TextView Name_tv;

    private Userlogin userlogin;



    // constructor
    public profileFragment() {
    }


    @Nullable
    @Override
    // init view
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_profile, container, false);

        mAuth = FirebaseAuth.getInstance();
        final DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference("User").child(mAuth.getCurrentUser().getUid());

        Email_tv = (TextView) v.findViewById(R.id.email_tv);
        Name_tv = (TextView) v.findViewById(R.id.name_tv);
        image_profile = (ImageView) v.findViewById(R.id.profile_image);

        Name_tv.setText(FirebaseAuth.getInstance().getCurrentUser().getDisplayName());
        Email_tv.setText(FirebaseAuth.getInstance().getCurrentUser().getEmail());


        onclickLogOut();
        return v;
    }


    // init implement module here !!!!!!
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);




    }

    private void onclickLogOut(){
        logoutbtn = (Button) v.findViewById(R.id.logoutBtn);
        logoutbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();

                Intent gologin = new Intent(getContext(), LoginActivity.class);
                startActivity(gologin);
            }
        });
    }

}
