package com.example.productsearch;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.wssholmes.stark.circular_score.CircularScoreView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ShippingFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ShippingFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ShippingFragment extends Fragment {
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
    private String shipcost;

    private RequestQueue myQueue;

    public ShippingFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ShippingFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ShippingFragment newInstance(String param1, String param2) {
        ShippingFragment fragment = new ShippingFragment();
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
        return inflater.inflate(R.layout.fragment_shipping, container, false);
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
        Bundle b = getActivity().getIntent().getExtras();
        String id = b.getString("id");
        shipcost = b.getString("shippingcost");
        String url ="http://Hw9-env.zsumhb5qdx.us-east-2.elasticbeanstalk.com/itemspec/";
        url = url.concat(id);

        myQueue = Volley.newRequestQueue(getActivity().getApplicationContext());


        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONObject result = response.getJSONObject("Item");
                    makeshipdata(result);

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
    public void makeshipdata(JSONObject result){
        String shipmoney = shipcost;
        String globalship;
        String handlingtime;
        String condition;
        String storename;
        String storeurl;
        String feedbackscore;
        String popularity;
        String feedbackstar;
        String returnpolicy;
        String returnwithin;
        String returnrefund;
        String returnshipby;
        String title;

        try {
            title = (String)result.get("Title");
        } catch (JSONException e) {
            e.printStackTrace();
            title="N/A";
        }

        try {
            String gg = String.valueOf(result.get("GlobalShipping"));
            if (gg.equals("true")){
                globalship = "Yes";
            }else{
                globalship = "No";
            }
        } catch (JSONException e) {
            e.printStackTrace();
            globalship = "N/A";
        }
        try {
            String handling = Integer.toString(result.getInt("HandlingTime"));
            handlingtime = handling;
            if (handling.equals("0")||handling.equals("1")){
                handlingtime = handlingtime.concat(" day");
            }else{
                handlingtime = handlingtime.concat(" days");
            }
        } catch (JSONException e) {
            e.printStackTrace();
            handlingtime = "N/A";
        }
        try {
            condition = (String)result.get("ConditionDescription");
        } catch (JSONException e) {
            e.printStackTrace();
            condition="N/A";
        }
        try {
            storename = (String)result.getJSONObject("Storefront").get("StoreName");
        } catch (JSONException e) {
            e.printStackTrace();
            storename = "N/A";
        }
        try {
            storeurl = (String)result.getJSONObject("Storefront").get("StoreURL");
        } catch (JSONException e) {
            e.printStackTrace();
            storeurl = "N/A";
        }
        try {
            feedbackscore = Integer.toString(result.getJSONObject("Seller").getInt("FeedbackScore"));
        } catch (JSONException e) {
            e.printStackTrace();
            feedbackscore = "N/A";
        }
        try {
            popularity = Double.toString(result.getJSONObject("Seller").getDouble("PositiveFeedbackPercent"));
        } catch (JSONException e) {
            e.printStackTrace();
            popularity = "N/A";
        }
        try {
            feedbackstar = (String)result.getJSONObject("Seller").get("FeedbackRatingStar");
        } catch (JSONException e) {
            e.printStackTrace();
            feedbackstar = "N/A";
        }
        try {
            returnpolicy = (String)result.getJSONObject("ReturnPolicy").get("ReturnsAccepted");
        } catch (JSONException e) {
            e.printStackTrace();
            returnpolicy = "N/A";
        }
        try {
            returnwithin = (String)result.getJSONObject("ReturnPolicy").get("ReturnsWithin");
        } catch (JSONException e) {
            e.printStackTrace();
            returnwithin = "N/A";
        }
        try {
            returnrefund = (String)result.getJSONObject("ReturnPolicy").get("Refund");
        } catch (JSONException e) {
            e.printStackTrace();
            returnrefund = "N/A";
        }

        try {
            returnshipby = (String)result.getJSONObject("ReturnPolicy").get("ShippingCostPaidBy");
        } catch (JSONException e) {
            e.printStackTrace();
            returnshipby = "N/A";
        }
        pb =getView().findViewById(R.id.progresslayout);
        pb.setVisibility(View.GONE);
        if (storename.equals("N/A")&&feedbackscore.equals("N/A")&&feedbackstar.equals("N/A")&&popularity.equals("N/A")
           &&shipmoney.equals("N/A")&&globalship.equals("N/A")&&handlingtime.equals("N/A")&&condition.equals("N/A")
           &&returnpolicy.equals("N/A")&&returnwithin.equals("N/A")&&returnrefund.equals("N/A")&&returnshipby.equals("N/A"))
        {} else{
            makesell(storename,storeurl,feedbackscore,feedbackstar,popularity);
            makeship(shipmoney, globalship,handlingtime,condition);
            makereturn(returnpolicy,returnwithin,returnrefund,returnshipby);
        }

    }

    public void makereturn(String returnpolicy,String returnwithin,String returnrefund,String returnshipby){
        if(returnpolicy.equals("N/A")&&returnwithin.equals("N/A")&&returnrefund.equals("N/A")&&returnshipby.equals("N/A")){
            LinearLayout ly = getView().findViewById(R.id.returnlay);
            ly.setVisibility(View.GONE);
        }else{
            LinearLayout ly = getView().findViewById(R.id.returnlay);
            ly.setVisibility(View.VISIBLE);
            if (returnpolicy.equals("N/A")){
                TableRow tr = getView().findViewById(R.id.policyrow);
                tr.setVisibility(View.GONE);
            }else{
                TextView tv = getView().findViewById(R.id.policytext);
                tv.setText(returnpolicy);
            }
            if (returnwithin.equals("N/A")){
                TableRow tr = getView().findViewById(R.id.returnswithinrow);
                tr.setVisibility(View.GONE);
            }else{
                TextView tv = getView().findViewById(R.id.returnswithintext);
                tv.setText(returnwithin);
            }
            if (returnrefund.equals("N/A")){
                TableRow tr = getView().findViewById(R.id.refundmoderow);
                tr.setVisibility(View.GONE);
            }else{
                TextView tv = getView().findViewById(R.id.refundmodetext);
                tv.setText(returnrefund);
            }
            if (returnshipby.equals("N/A")){
                TableRow tr = getView().findViewById(R.id.shippiedbyrow);
                tr.setVisibility(View.GONE);
            }else{
                TextView tv = getView().findViewById(R.id.shippedbytext);
                tv.setText(returnshipby);
            }

        }
    }

    public void makeship(String shipmoney, String globalship,String handlingtime,String condition){
        if(shipmoney.equals("N/A")&&globalship.equals("N/A")&&handlingtime.equals("N/A")&&condition.equals("N/A")){
            LinearLayout ly = getView().findViewById(R.id.shipinfolay);
            ly.setVisibility(View.GONE);
        }else{
            LinearLayout ly = getView().findViewById(R.id.shipinfolay);
            ly.setVisibility(View.VISIBLE);
            if (shipmoney.equals("N/A")){
                TableRow tr = getView().findViewById(R.id.shippingcostrow);
                tr.setVisibility(View.GONE);
            }else{
                TextView tv = getView().findViewById(R.id.shippingcosttext);
                tv.setText(shipmoney);
            }
            if (globalship.equals("N/A")){
                TableRow tr = getView().findViewById(R.id.globalshippingrow);
                tr.setVisibility(View.GONE);
            }else{
                TextView tv = getView().findViewById(R.id.globalshippingtext);
                tv.setText(globalship);
            }
            if (handlingtime.equals("N/A")){
                TableRow tr = getView().findViewById(R.id.handlingtimerow);
                tr.setVisibility(View.GONE);
            }else{
                TextView tv = getView().findViewById(R.id.handlingtimetext);
                tv.setText(handlingtime);
            }
            if (condition.equals("N/A")){
                TableRow tr = getView().findViewById(R.id.conditionrow);
                tr.setVisibility(View.GONE);
            }else{
                TextView tv = getView().findViewById(R.id.conditiontext);
                tv.setText(condition);
            }

        }
    }

    public void makesell(String storename,String storeurl,String feedbackscore,String feedbackstar,String popularity){
        if(storename.equals("N/A")&&feedbackscore.equals("N/A")&&feedbackstar.equals("N/A")&&popularity.equals("N/A")){
            LinearLayout ly = getView().findViewById(R.id.soldlay);
            ly.setVisibility(View.GONE);
        }else{
            LinearLayout ly = getView().findViewById(R.id.soldlay);
            ly.setVisibility(View.VISIBLE);
            if (storename.equals("N/A")){
                TableRow tr = getView().findViewById(R.id.storenamerow);
                tr.setVisibility(View.GONE);
            }else{
                TextView tv = getView().findViewById(R.id.storenametext);
                if(storeurl.equals("N/A")){
                    tv.setText(storename);
                }else{
                    tv.setText(Html.fromHtml("<a href=\""+ storeurl + "\">" + storename + "</a>"));
                    tv.setMovementMethod (LinkMovementMethod.getInstance());
                }
            }
            if(feedbackscore.equals("N/A")){
                TableRow tr = getView().findViewById(R.id.feedbackscorerow);
                tr.setVisibility(View.GONE);
            }else{
                TextView tv = getView().findViewById(R.id.feedbackscoretext);
                tv.setText(feedbackscore);
            }
            if(popularity.equals("N/A")){
                TableRow tr = getView().findViewById(R.id.popularityrow);
                tr.setVisibility(View.GONE);
            }else{
                CircularScoreView circularScoreView = getView().findViewById(R.id.popularitycir);
                circularScoreView.setScore((int)Double.parseDouble(popularity));

            }
            if(feedbackstar.equals("N/A")){
                TableRow tr = getView().findViewById(R.id.feedbackstarrow);
                tr.setVisibility(View.GONE);
            }else{
                ImageView img = getView().findViewById(R.id.feedbackstarimg);
                if (feedbackstar.indexOf("Shooting")!=-1){
                    img.setImageResource(R.drawable.star);
                }else{
                    img.setImageResource(R.drawable.star_outline);
                }
                String col = feedbackstar.replace("Shooting","");
                if(col.equals("Blue")){img.setColorFilter(getContext().getResources().getColor(R.color.Blue));}
                if(col.equals("Yellow")){img.setColorFilter(getContext().getResources().getColor(R.color.Yellow));}
                if(col.equals("Red")){img.setColorFilter(getContext().getResources().getColor(R.color.Red));}
                if(col.equals("Purple")){img.setColorFilter(getContext().getResources().getColor(R.color.Purple));}
                if(col.equals("Silver")){img.setColorFilter(getContext().getResources().getColor(R.color.Silver));}
                if(col.equals("Turquoise")){img.setColorFilter(getContext().getResources().getColor(R.color.Turquoise));}
                if(col.equals("Green")){img.setColorFilter(getContext().getResources().getColor(R.color.Green));}
            }
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
