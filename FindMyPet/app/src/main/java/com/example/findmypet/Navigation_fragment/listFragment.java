package com.example.findmypet.Navigation_fragment;

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
import com.example.findmypet.Model.PostInfo;
import com.example.findmypet.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.zip.Inflater;

public class listFragment extends Fragment {

    View v;

    private LinearLayoutManager mLinearLayoutManager;
    private RecyclerView mRecyclerview;
    private FirebaseDatabase mfirebaseDatabase;
    private DatabaseReference mDataRef;
    private FirebaseRecyclerAdapter<PostInfo, ViewHolder> firebaseRecyclerAdapter;
    private FirebaseRecyclerOptions<PostInfo> options;

    public listFragment() {
    }

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_list, container, false);

        mLinearLayoutManager = new LinearLayoutManager(v.getContext());
        mLinearLayoutManager.setReverseLayout(true);
        mLinearLayoutManager.setStackFromEnd(true);

        mRecyclerview = v.findViewById(R.id.recycle_view_list);
        mfirebaseDatabase = FirebaseDatabase.getInstance();
        mDataRef = mfirebaseDatabase.getReference("MyList2").child(FirebaseAuth.getInstance().getCurrentUser().getUid());

        showData();

        return v;

    }

    private void showAdoptDialog(final String curr_title, String curr_des,
                                 final String curr_lo, final String curr_tel,
                                 String curr_img){

        // alert dialog
        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Do you want to remove post");
        builder.setMessage(curr_title);

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                removeData(curr_title, "title");
                removeData(curr_lo, "location");
                removeData(curr_tel, "tel");
            }
        });

        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
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

    // show recyclear view

    private void showData(){
        options = new FirebaseRecyclerOptions.Builder<PostInfo>()
                .setQuery(mDataRef, PostInfo.class)
                .build();

        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<PostInfo, ViewHolder>(options) {

            // create view then go to viewHolder (class)
            @Override
            protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull PostInfo model) {
                holder.setDetails(getContext(), model.getImage(), model.getTitle(), model.getLocation(), model.getTel());
            }

            // display content on device
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
