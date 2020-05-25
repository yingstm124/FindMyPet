package com.example.findmypet.Fragment_;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.findmypet.Adapter.ViewHolder;
import com.example.findmypet.R;
import com.example.findmypet.Model.PostInfo;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class fragment_dog extends Fragment {

    View v;

    private LinearLayoutManager mLinearLayoutManager;
    private RecyclerView mRecyclerview;
    private FirebaseDatabase mfirebaseDatabase;
    private DatabaseReference mDataRef;
    private FirebaseRecyclerAdapter<PostInfo, ViewHolder> firebaseRecyclerAdapter;
    private FirebaseRecyclerOptions<PostInfo> options;


    // constructor
    public fragment_dog() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_dog, container, false);

        mLinearLayoutManager = new LinearLayoutManager(v.getContext());
        mLinearLayoutManager.setReverseLayout(true);
        mLinearLayoutManager.setStackFromEnd(true);

        mRecyclerview = v.findViewById(R.id.recycle_view_dog);
        mfirebaseDatabase = FirebaseDatabase.getInstance();
        mDataRef = mfirebaseDatabase.getReference("Post").child("Dog");

        showData();

        return v;

    }

    private void showAdoptDialog(final String curr_title, final String curr_des,
                                 final String curr_lo, final String curr_tel,
                                 final String curr_img){

        // alert dialog
        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(curr_title);
        builder.setMessage(curr_des);

        builder.setPositiveButton("Adopt", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                removeData(curr_title, "title");
                removeData(curr_lo, "location");
                removeData(curr_tel, "tel");

                AddMyList(curr_title, curr_des, curr_lo, curr_tel, curr_img);
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        builder.show();
    }



    private void removeData(String curr, String value){
        // remove title
        Query mQuery = mDataRef.orderByChild(value).equalTo(curr);
        mQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds: dataSnapshot.getChildren()){
                    ds.getRef().removeValue();
                }
                Toast.makeText(getContext(), "remove", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(), databaseError.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void AddMyList(final String curr_title, final String curr_des,
                           final String curr_lo, final String curr_tel,
                           final String curr_img){

        final DatabaseReference databaseList =  FirebaseDatabase.getInstance().getReference("MyList2").child(FirebaseAuth.getInstance().getCurrentUser().getUid());

        // put in firebase
        HashMap<String , Object> hashMap = new HashMap<>();
        hashMap.put("title", curr_title);
        hashMap.put("location", curr_lo);
        hashMap.put("tel", curr_tel);
        hashMap.put("description", curr_des);
        //hashMap.put("type", type_pet);
        hashMap.put("image", curr_img);
        databaseList.push().setValue(hashMap);

        Toast.makeText(getContext(), "Add My List ", Toast.LENGTH_LONG).show();
    }


    private void showData(){
        options = new FirebaseRecyclerOptions.Builder<PostInfo>()
                .setQuery(mDataRef, PostInfo.class)
                .build();

        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<PostInfo, ViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull PostInfo model) {
                holder.setDetails(getContext(), model.getImage(), model.getTitle(), model.getLocation(), model.getTel());

            }

            @NonNull
            @Override
            public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                View itemv = LayoutInflater.from(parent.getContext()).inflate(R.layout.post_element, parent, false);

                ViewHolder viewHolder = new ViewHolder(itemv);
                viewHolder.setOnClickListener(new ViewHolder.ClickListener() {
                    @Override
                    public void onItemClick(View v, int position) {

                        String currenttiltle = getItem(position).getTitle();
                        String currentdes = getItem(position).getDescription();
                        String currentlocation = getItem(position).getLocation();
                        String currenttel = getItem(position).getTel();
                        String currentimg= getItem(position).getImage();

                        showAdoptDialog(currenttiltle, currentdes, currentlocation, currenttel, currentimg);


                    }

                    @Override
                    public void onItemLongClick(View v, int position) {
                        Toast.makeText(getContext(), "Long click", Toast.LENGTH_LONG).show();
                    }
                });
                return viewHolder;
            }
        };

        mRecyclerview.setLayoutManager(mLinearLayoutManager);
        firebaseRecyclerAdapter.startListening();
        mRecyclerview.setAdapter(firebaseRecyclerAdapter);


    }

    public void onStart() {
        super.onStart();

        if(firebaseRecyclerAdapter != null){
            firebaseRecyclerAdapter.startListening();
        }
    }

}
