package com.example.productsearch;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class WishlistAdapter extends RecyclerView.Adapter<WishlistAdapter.MyViewHolder>{

    @NonNull
    private List<item> mitems;

    ArrayList<item> wishdata;

    SharedPreferences myprefs;

    private ListItemClickListener clickListener;
    private Context context;



    public WishlistAdapter(List<item> mitems, ListItemClickListener listener){
        this.mitems = mitems;
        clickListener = listener;
    }

    public interface  ListItemClickListener{
        void onListItemDetailClicked(int clickedItemIndex);
        void onListItemWishClicked(int clickedItemIndex);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int i) {
        View view;
        LayoutInflater mInflater = LayoutInflater.from(parent.getContext());
        view = mInflater.inflate(R.layout.cardview_item, parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {

        String ttt = mitems.get(position).getTitle();

        String st = "";

        String fintit;

        if (ttt.length()>50){
            st = st.concat(ttt.substring(0,50));
            int end = st.lastIndexOf(" ");
            fintit = st.substring(0,end);
            fintit = fintit.concat("...");
        }else{
            fintit = ttt;
        }

        fintit = fintit.toUpperCase();

        holder.tv_item_title.setText(fintit);
        Picasso.get().load(mitems.get(position).getImage()).into(holder.item_image);
        holder.tv_item_zipcode.setText(mitems.get(position).getZip());
        holder.tv_item_shipping.setText(mitems.get(position).getShipping());
        holder.tv_item_condition.setText(mitems.get(position).getCondition());
        holder.tv_item_price.setText(mitems.get(position).getPrice());

        holder.imageButton.setImageResource(R.drawable.cart_remove);



    }


    @Override
    public int getItemCount() {
        return mitems.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {


        TextView tv_item_title;
        ImageView item_image;
        TextView tv_item_zipcode;
        TextView tv_item_shipping;
        TextView tv_item_condition;
        TextView tv_item_price;
        CardView cardView;
        ImageButton imageButton;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_item_title = (TextView) itemView.findViewById(R.id.item_title);
            item_image = (ImageView) itemView.findViewById(R.id.item_image);
            tv_item_zipcode = (TextView) itemView.findViewById(R.id.item_zip);
            tv_item_shipping = (TextView) itemView.findViewById(R.id.item_shipping);
            tv_item_condition = (TextView) itemView.findViewById(R.id.item_condition);
            tv_item_price = (TextView) itemView.findViewById(R.id.item_price);
            cardView = (CardView) itemView.findViewById(R.id.card_item);
            imageButton = (ImageButton) itemView.findViewById(R.id.item_wish);

            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int clickedPosition = getAdapterPosition();
                    clickListener.onListItemDetailClicked(clickedPosition);
                }
            });

            imageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int clickedPosition = getAdapterPosition();
                    clickListener.onListItemWishClicked(clickedPosition);
                }
            });
        }
    }
}
