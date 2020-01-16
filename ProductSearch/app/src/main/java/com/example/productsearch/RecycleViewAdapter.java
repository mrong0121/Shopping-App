package com.example.productsearch;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.util.List;

public class RecycleViewAdapter extends RecyclerView.Adapter<RecycleViewAdapter.MyViewHolder>{

    @NonNull
    private Context mContext;
    private List<item> mitems;


    SharedPreferences myprefs;

    public RecycleViewAdapter(Context mContext, List<item> mitems){
        this.mContext = mContext;
        this.mitems = mitems;
    }



    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int i) {
        View view;
        LayoutInflater mInflater = LayoutInflater.from(mContext);
        view = mInflater.inflate(R.layout.cardview_item, parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {

        myprefs = mContext.getSharedPreferences("Myprefs",Context.MODE_PRIVATE);

        String tt = mitems.get(position).getTitle();

        String st = "";

        String fintit;

        if (tt.length()>50){
            st = st.concat(tt.substring(0,50));
            int end = st.lastIndexOf(" ");
            fintit = st.substring(0,end);
            fintit = fintit.concat("...");
        }else{
            fintit = tt;
        }
        fintit = fintit.toUpperCase();

//        holder.tv_item_title.setText(mitems.get(position).getTitle());
        holder.tv_item_title.setText(fintit);
        Picasso.get().load(mitems.get(position).getImage()).into(holder.item_image);
        holder.tv_item_zipcode.setText(mitems.get(position).getZip());
        holder.tv_item_shipping.setText(mitems.get(position).getShipping());
        holder.tv_item_condition.setText(mitems.get(position).getCondition());
        holder.tv_item_price.setText(mitems.get(position).getPrice());

        //Set click listener

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext,DetailsActivity.class);
                intent.putExtra("title",mitems.get(position).getTitle());
                intent.putExtra("id",mitems.get(position).getId());
                intent.putExtra("shippingcost",mitems.get(position).getShipping());
                intent.putExtra("price",mitems.get(position).getPrice());
                intent.putExtra("zip",mitems.get(position).getZip());
                intent.putExtra("condition",mitems.get(position).getCondition());
                intent.putExtra("image",mitems.get(position).getImage());
                mContext.startActivity(intent);

            }
        });

        final item curr = mitems.get(position);
        final String id = curr.getId();
        final String title = curr.getTitle();

        if(myprefs.contains(id)){
            holder.itemwish.setImageResource(R.drawable.cart_remove);
        }
        else{
            holder.itemwish.setImageResource(R.drawable.cart_plus);
        }

        holder.itemwish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(myprefs.contains(id)) {
                    SharedPreferences.Editor myprefsEditor = myprefs.edit();
                    myprefsEditor.remove(id);
                    myprefsEditor.apply();
                    holder.itemwish.setImageResource(R.drawable.cart_plus);

                    String tt = mitems.get(position).getTitle();

                    String st = "";

                    String fintit;

                    if (tt.length()>50){
                        st = st.concat(tt.substring(0,50));
                        int end = st.lastIndexOf(" ");
                        fintit = st.substring(0,end);
                        fintit = fintit.concat("...");
                    }else{
                        fintit = tt;
                    }


                    String tos = fintit;
                    tos = tos.concat(" was removed from wish list");
                    Toast.makeText(mContext, tos,Toast.LENGTH_SHORT).show();
                }
                else{
                    SharedPreferences.Editor prefsEditor = myprefs.edit();
                    Gson gson = new Gson();
                    prefsEditor.putString(id,gson.toJson(curr).toString());
                    prefsEditor.apply();
                    holder.itemwish.setImageResource(R.drawable.cart_remove);

                    String tt = mitems.get(position).getTitle();

                    String st = "";

                    String fintit;

                    if (tt.length()>50){
                        st = st.concat(tt.substring(0,50));
                        int end = st.lastIndexOf(" ");
                        fintit = st.substring(0,end);
                        fintit = fintit.concat("...");
                    }else{
                        fintit = tt;
                    }


                    String tos = fintit;
                    tos = tos.concat(" was added to wish list");
                    Toast.makeText(mContext, tos,Toast.LENGTH_SHORT).show();
                }

            }
        });




    }

    @Override
    public int getItemCount() {
        return mitems.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tv_item_title;
        ImageView item_image;
        TextView tv_item_zipcode;
        TextView tv_item_shipping;
        TextView tv_item_condition;
        TextView tv_item_price;
        CardView cardView;
        ImageButton itemwish;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_item_title = (TextView) itemView.findViewById(R.id.item_title);
            item_image = (ImageView) itemView.findViewById(R.id.item_image);
            tv_item_zipcode = (TextView) itemView.findViewById(R.id.item_zip);
            tv_item_shipping = (TextView) itemView.findViewById(R.id.item_shipping);
            tv_item_condition = (TextView) itemView.findViewById(R.id.item_condition);
            tv_item_price = (TextView) itemView.findViewById(R.id.item_price);
            cardView = (CardView) itemView.findViewById(R.id.card_item);
            itemwish = (ImageButton) itemView.findViewById(R.id.item_wish);

        }
    }
}
