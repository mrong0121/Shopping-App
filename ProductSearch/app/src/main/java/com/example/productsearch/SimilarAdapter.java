package com.example.productsearch;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class SimilarAdapter extends RecyclerView.Adapter<SimilarAdapter.viewHolder> {
    private ArrayList<SimilarItem> data;
    private ListItemClickListener clickListener;

    public SimilarAdapter(ArrayList<SimilarItem> data, ListItemClickListener listener) {
        this.data = data;
        this.clickListener = listener;
    }

    public interface ListItemClickListener {
        void onListItemClick(int clickedItemIndex);
    }


    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.similar_item, parent, false);
        viewHolder holder = new viewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, final int position) {

        Picasso.get().load(data.get(position).getImage()).into(holder.similaritemimg);
        holder.simtitle.setText(data.get(position).getTitle().toUpperCase());
        String daysleft = Integer.toString(data.get(position).getDaysleft());
        String shipp;
        String price;
        if (data.get(position).getDaysleft()==0||data.get(position).getDaysleft()==1){
            daysleft = daysleft.concat(" Day Left");
        }else{
            daysleft = daysleft.concat(" Days Left");
        }
        if (String.format("%.2f",data.get(position).getShipping()).equals("0.00")){
            shipp = "Free Shipping";
        }else{
            shipp = "$"+String.format("%.2f",data.get(position).getShipping());
        }
        price = "$"+String.format("%.2f",data.get(position).getPrice());
        holder.simdaysleft.setText(daysleft);
        holder.simprice.setText(price);
        holder.simshipping.setText(shipp);



        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            String url= data.get(position).getViewurl();
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(url));
            v.getContext().startActivity(i);
            }
        });




    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView similaritemimg;
        private TextView simtitle;
        private TextView simdaysleft;
        private TextView simprice;
        private TextView simshipping;
        private CardView cardView;

        public viewHolder(View itemView) {
            super(itemView);

            similaritemimg = itemView.findViewById(R.id.similaritemimg);
            simtitle = itemView.findViewById(R.id.simtitle);
            simdaysleft = itemView.findViewById(R.id.simdaysleft);
            simprice = itemView.findViewById(R.id.simprice);
            simshipping = itemView.findViewById(R.id.simshipping);
            cardView = itemView.findViewById(R.id.similarlistlay);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

        }
    }
}
