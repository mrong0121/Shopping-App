package com.example.productsearch;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class PhotoRecycleAdapter extends RecyclerView.Adapter<PhotoRecycleAdapter.MyviewHolder> {

    private Context mContext;
    private List<photoit> mData;

    public PhotoRecycleAdapter(Context mContext, List<photoit> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }

    @NonNull
    @Override
    public MyviewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view;
        LayoutInflater myInflater = LayoutInflater.from(mContext);
        view = myInflater.inflate(R.layout.card_photo_item,viewGroup,false);
        return new MyviewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull MyviewHolder holder, int position) {
        Picasso.get().load(mData.get(position).getPhoto()).into(holder.img_photo);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class MyviewHolder extends RecyclerView.ViewHolder{

        ImageView img_photo;


        public MyviewHolder(@NonNull View itemView) {
            super(itemView);

            img_photo = (ImageView) itemView.findViewById(R.id.photo_id);
        }
    }

}
