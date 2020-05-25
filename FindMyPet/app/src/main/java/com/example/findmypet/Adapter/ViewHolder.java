package com.example.findmypet.Adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.findmypet.R;
import com.squareup.picasso.Picasso;

public class ViewHolder extends RecyclerView.ViewHolder {

    View v;
    private ViewHolder.ClickListener mclick;

    public ViewHolder(@NonNull View itemView) {
        super(itemView);

        v = itemView;

        itemView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                mclick.onItemClick(view, getAdapterPosition());
            }
        });

        itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                mclick.onItemLongClick(view, getAdapterPosition());
                return true;
            }
        });
    }

    public void setDetails(Context context, String image, String title, String location, String tel){

        ImageView imageView = v.findViewById(R.id.picture_post);
        TextView tv_title = v.findViewById(R.id.post_title);
        TextView tv_location = v.findViewById(R.id.post_location);
        TextView tv_tel = v.findViewById(R.id.post_tel);


        // set Detail content
        tv_title.setText(title);
        tv_location.setText(location);
        tv_tel.setText(tel);
        Picasso.get().load(image).into(imageView);
    }



    public interface ClickListener {

        void onItemClick(View v, int position);
        void onItemLongClick(View v, int position);
    }

    public void setOnClickListener(ViewHolder.ClickListener clickListener){
        mclick = clickListener;
    }
}
