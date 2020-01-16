package com.example.productsearch;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class PhotosFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private RequestQueue myQueue;
    private TextView tv;
    private RelativeLayout pb;

    List<photoit> lstphoto;

    private OnFragmentInteractionListener mListener;

    public PhotosFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static PhotosFragment newInstance(String param1, String param2) {
        PhotosFragment fragment = new PhotosFragment();
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



        return inflater.inflate(R.layout.fragment_photos, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Bundle b = getActivity().getIntent().getExtras();
        String title = b.getString("title");
        String url ="http://Hw9-env.zsumhb5qdx.us-east-2.elasticbeanstalk.com/phototitle/";
        url = url.concat(title);

        myQueue = Volley.newRequestQueue(getActivity().getApplicationContext());


        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray result = response.getJSONArray("items");
                    makephotodata(result);
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
    public void makephotodata(JSONArray result){
        ArrayList<String> photos = new ArrayList<>();
        int number;
        number = result.length();
        for (int i=0;i<number;i++){
            try {
                photos.add((String)result.getJSONObject(i).get("link"));
                Log.e("photo",(String)result.getJSONObject(i).get("link"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        lstphoto = new ArrayList<>();

        for (int j=0;j<photos.size();j++){
            lstphoto.add(new photoit(photos.get(j)));
        }
        pb = getView().findViewById(R.id.progresslayout);
        pb.setVisibility(View.GONE);

        RecyclerView myrv = (RecyclerView) getActivity().findViewById(R.id.photorecycle);
        PhotoRecycleAdapter myAdapter = new PhotoRecycleAdapter(getContext(),lstphoto);
        myrv.setLayoutManager(new GridLayoutManager(getContext(),1));
        myrv.setAdapter(myAdapter);


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

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
