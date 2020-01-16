package com.example.productsearch;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonArray;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link WishlistFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link WishlistFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class WishlistFragment extends Fragment implements WishlistAdapter.ListItemClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private SharedPreferences myprefs;

    public static List<item> wishdata;

    public static TextView num;

    public static TextView toprice;

    public static TextView nowishes;

    public static RecyclerView mywishrv;
    public static WishlistAdapter mywishAdapter;
    private GridLayoutManager myLayoutManager;
    private SharedPreferences.Editor myprefsEditor;
    private List<item> wisharray;




    public WishlistFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment WishlistFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static WishlistFragment newInstance(String param1, String param2) {
        WishlistFragment fragment = new WishlistFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =inflater.inflate(R.layout.fragment_wishlist, container, false);

        myprefs = getContext().getSharedPreferences("Myprefs",Context.MODE_PRIVATE);
        myprefsEditor = myprefs.edit();
        wishdata = new ArrayList<>();


        mywishrv = (RecyclerView) v.findViewById(R.id.recyclerview_wishlistitem);

        Map<String,?> allEntries = myprefs.getAll();



        int itemsnum = 0;
        Double total = 0.00;


        for(Map.Entry<String,?> entry :allEntries.entrySet()){
            String json = (String) entry.getValue();
            try {
                JSONObject jsonData = new JSONObject(json);
                String id = (String) jsonData.get("Id");
                String title = (String) jsonData.get("Title");
                String zip = (String) jsonData.get("Zip");
                String shipping = (String) jsonData.get("Shipping");
                String condition = (String) jsonData.get("Condition");
                String image = (String) jsonData.get("Image");
                String price = (String) jsonData.get("Price");
                itemsnum = itemsnum + 1;
                total = total +Double.parseDouble(price.replace("$",""));
                wishdata.add(new item(title,zip,shipping,condition,price,image,id));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        num = v.findViewById(R.id.wishlistitemnum);
        toprice = v.findViewById(R.id.wishlistprice);


        String tt = Integer.toString(itemsnum);
        num.setText(tt);
        String kk = "$"+String.format("%.2f",total);
        toprice.setText(kk);

        nowishes = v.findViewById(R.id.textNowishes);
        if (allEntries.size()==0){
            mywishrv.setVisibility(View.GONE);
            nowishes.setVisibility(View.VISIBLE);
            num.setText("0");
            toprice.setText("$0.00");
        }else{
            mywishrv.setVisibility(View.VISIBLE);
            nowishes.setVisibility(View.GONE);
        }



        mywishAdapter = new WishlistAdapter(wishdata,this);
        myLayoutManager = new GridLayoutManager(getContext(),2);
        mywishrv.setLayoutManager(myLayoutManager);
        mywishrv.setAdapter(mywishAdapter);





        return v;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }



    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);



    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onListItemDetailClicked(int clickedItemIndex) {
        Intent intent = new Intent(getContext(),DetailsActivity.class);
        intent.putExtra("title",wishdata.get(clickedItemIndex).getTitle());
        intent.putExtra("id",wishdata.get(clickedItemIndex).getId());
        intent.putExtra("shippingcost",wishdata.get(clickedItemIndex).getShipping());
        intent.putExtra("price",wishdata.get(clickedItemIndex).getPrice());
        getContext().startActivity(intent);

    }

    @Override
    public void onListItemWishClicked(int clickedItemIndex) {

        myprefsEditor.remove(wishdata.get(clickedItemIndex).getId());
        myprefsEditor.apply();

        String ttt = wishdata.get(clickedItemIndex).getTitle();

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


        String tos = fintit;
        tos = tos.concat(" was removed from wish list");
        Toast.makeText(getContext(), tos,Toast.LENGTH_SHORT).show();

        wishdata.remove(clickedItemIndex);

        mywishAdapter.notifyDataSetChanged();

        Map<String,?> allEntries = myprefs.getAll();



        int itemnum = 0;
        Double itemstotal = 0.00;


        for (int i=0;i<wishdata.size();i++){
            itemnum = itemnum+1;
            itemstotal = itemstotal + Double.parseDouble(wishdata.get(i).getPrice().replace("$",""));
        }

        String tt = Integer.toString(itemnum);
        String kk = "$"+String.format("%.2f",itemstotal);
        toprice.setText(kk);
        num.setText(tt);


        if (allEntries.size()==0){
            mywishrv.setVisibility(View.GONE);
            nowishes.setVisibility(View.VISIBLE);
            num.setText("0");
            toprice.setText("$0.00");
        }else{
            mywishrv.setVisibility(View.VISIBLE);
            nowishes.setVisibility(View.GONE);
        }

    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            getFragmentManager().beginTransaction().detach(this).attach(this).commit();
        }
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
