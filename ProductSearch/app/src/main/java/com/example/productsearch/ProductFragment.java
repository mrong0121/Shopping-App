package com.example.productsearch;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.zip.Inflater;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ProductFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ProductFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProductFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    private TextView tv;
    private RelativeLayout pb;
    private String shipping;
    private LinearLayout gallery;



    private RequestQueue myQueue;

    public ProductFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProductFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProductFragment newInstance(String param1, String param2) {
        ProductFragment fragment = new ProductFragment();
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


        return inflater.inflate(R.layout.fragment_product, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Bundle b = getActivity().getIntent().getExtras();
        String id = b.getString("id");
        String url ="http://Hw9-env.zsumhb5qdx.us-east-2.elasticbeanstalk.com/itemspec/";
        shipping = b.getString("shippingcost");
        url = url.concat(id);
        Log.e("url",url);

        myQueue = Volley.newRequestQueue(getActivity().getApplicationContext());


        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONObject result = response.getJSONObject("Item");
                    makedata(result);
                } catch (JSONException e) {
                    e.printStackTrace();
                    tv = getView().findViewById(R.id.textNoresults);
                    tv.setVisibility(View.VISIBLE);
                    pb =getView().findViewById(R.id.progresslayout);
                    pb.setVisibility(View.GONE);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                tv = getView().findViewById(R.id.textNoresults);
                tv.setVisibility(View.VISIBLE);
                pb =getView().findViewById(R.id.progresslayout);
                pb.setVisibility(View.GONE);
            }
        });
        myQueue.add(request);
    }

    public void makedata(JSONObject result){
        ArrayList<String> photos = new ArrayList<>();
        String title;
        String subtitle;
        String price;
        String shippingcost;
        String brand="";
        ArrayList<String> specs = new ArrayList<>();
        JSONArray pics;
        try {
            title = (String)result.get("Title");
        } catch (JSONException e) {
            e.printStackTrace();
            title = "N/A";
        }
        try {
            subtitle = (String)result.get("Subtitle");
        } catch (JSONException e) {
            e.printStackTrace();
            subtitle = "N/A";
        }
        shippingcost = "With ";
        if (shipping.equals("Free Shipping")){
            shippingcost = shippingcost.concat(shipping);
        }else{
            shippingcost = shippingcost.concat(shipping);
            shippingcost = shippingcost.concat(" Shipping");
        }

        try {
            price = "$";
            price = price.concat(Double.toString(result.getJSONObject("CurrentPrice").getDouble("Value")));
        } catch (JSONException e) {
            e.printStackTrace();
            price = "N/A";
        }
        try {
            pics = result.getJSONArray("PictureURL");
            for(int i=0;i<pics.length();i++){
                photos.add((String)pics.get(i));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            JSONArray itsp = result.getJSONObject("ItemSpecifics").getJSONArray("NameValueList");
            for (int i=0;i<itsp.length();i++){
                if (((String)itsp.getJSONObject(i).get("Name")).equals("Brand")){
                    brand =brand.concat((String)itsp.getJSONObject(i).getJSONArray("Value").get(0));
                    specs.add((String)itsp.getJSONObject(i).getJSONArray("Value").get(0));
                }
            }
            for (int i=0;i<itsp.length();i++){
                if (!((String)itsp.getJSONObject(i).get("Name")).equals("Brand")){
                    specs.add((String)itsp.getJSONObject(i).getJSONArray("Value").get(0));
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }


        pb =getView().findViewById(R.id.progresslayout);
        pb.setVisibility(View.GONE);
        makegallery(photos);
        maketit(title,price,shippingcost);
        makehighlight(subtitle,price,brand);
        makespec(specs);



    }

    public  void makespec(ArrayList<String> specs){
        if (specs.size()!=0){
            LinearLayout llay = getView().findViewById(R.id.speclayout);
            llay.setVisibility(View.VISIBLE);
            final ListView lv = getView().findViewById(R.id.speclistview);
            lv.setAdapter(new ArrayAdapter<String>(getActivity().getApplicationContext(),R.layout.specrow,R.id.specitem, specs));

            lv.postDelayed(new Runnable() {
                public void run() {
                    Uitility.setListViewHeightBasedOnChildren(lv);
                }
            }, 400);


        }

    }

    public void makehighlight(String subtitle, String price, String brand){
        if(!(subtitle.equals("N/A")&&price.equals("N/A")&&brand.equals(""))){
            LinearLayout lay = getView().findViewById(R.id.highlayout);
            lay.setVisibility(View.VISIBLE);
            if (!subtitle.equals("N/A")){
                LinearLayout ly = getView().findViewById(R.id.spsubtitle);
                TextView tt = getView().findViewById(R.id.subtitletext);
                tt.setText(subtitle);
                ly.setVisibility(View.VISIBLE);
            }
            if (!price.equals("N/A")){
                LinearLayout ly = getView().findViewById(R.id.spprice);
                TextView tt = getView().findViewById(R.id.pricetext);
                tt.setText(price);
                ly.setVisibility(View.VISIBLE);
            }
            if (!brand.equals("")){
                LinearLayout ly = getView().findViewById(R.id.spbrand);
                TextView tt = getView().findViewById(R.id.brandtext);
                tt.setText(brand);
                ly.setVisibility(View.VISIBLE);
            }
        }
    }

    public void maketit(String title, String price, String shippingcost){
        TextView tit = getView().findViewById(R.id.texttitle);
        tit.setText(title);
        TextView pri = getView().findViewById(R.id.textprice);
        pri.setText(price);
        TextView ship = getView().findViewById(R.id.textshipping);
        ship.setText(shippingcost);
    }

    public void makegallery(ArrayList<String> photos){
        gallery = getView().findViewById(R.id.gallery);
        LayoutInflater inflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        for (int i = 0; i < photos.size(); i++)
        {
            View view = inflater.inflate(R.layout.gallery_item,gallery,false);
            ImageView img = view.findViewById(R.id.gallery_item_image);
            Picasso.get().load(photos.get(i)).into(img);
            gallery.addView(view);

        }

    }


    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
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

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
