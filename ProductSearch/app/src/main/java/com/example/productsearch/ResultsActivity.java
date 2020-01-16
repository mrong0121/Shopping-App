package com.example.productsearch;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
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

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class ResultsActivity extends AppCompatActivity {
    private String searchUrl;
    private RequestQueue myQueue;
    List<item> myitems;
    public static RecycleViewAdapter myAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_results);
        assert getSupportActionBar() != null;   //null check
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        myQueue = Volley.newRequestQueue(this);


        String keyword = getIntent().getStringExtra("keyword");
        TextView tx = findViewById(R.id.textit);
        tx.setText(keyword);
        String category = getIntent().getStringExtra("category");
        String distance = getIntent().getStringExtra("distance");
        ArrayList<String> condition = getIntent().getStringArrayListExtra("condition");
        ArrayList<String> shipping = getIntent().getStringArrayListExtra("shipping");
        String cc = getIntent().getStringExtra("enable");
        String zipcode = "";

        if (cc.equals("checked")){
            zipcode = getIntent().getStringExtra("zipcode");
        }

        makeURL(keyword,category,distance,condition,shipping,zipcode,cc);

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, searchUrl, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray rresults = response.getJSONArray("findItemsAdvancedResponse");
                    JSONObject rrresults = rresults.getJSONObject(0);
                    JSONArray rrrresults = rrresults.getJSONArray("searchResult");
                    JSONObject results = rrrresults.getJSONObject(0);
                    makeresults(results);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                TextView noresults = findViewById(R.id.textNoresults);
                noresults.setVisibility(View.VISIBLE);
                RelativeLayout pb =findViewById(R.id.progresslayout);
                pb.setVisibility(View.GONE);

            }
        });
        myQueue.add(request);
    }
    public void makeresults(JSONObject results){
        try {
            JSONArray result = results.getJSONArray("item");
            makecard(result);
        }
        catch (JSONException e) {
            e.printStackTrace();
            RelativeLayout pb =findViewById(R.id.progresslayout);
            pb.setVisibility(View.GONE);
            TextView noresults = findViewById(R.id.textNoresults);
            noresults.setVisibility(View.VISIBLE);
        }

    }
    public void makecard(JSONArray result){
        RelativeLayout pb =findViewById(R.id.progresslayout);
        pb.setVisibility(View.GONE);
        myitems = new ArrayList<>();

        int number = result.length();
        TextView tt = findViewById(R.id.textnumber);
        String nn = Integer.toString(number);
        tt.setText(nn);

        RelativeLayout ly = findViewById(R.id.shownumberlayout);
        ly.setVisibility(View.VISIBLE);

        for(int i=0;i<number;i++){
            String title;
            String zipcode;
            String photo;
            String shipping;
            String price;
            String id;
            String condition;
            JSONArray shipinfo = null;
            try {
                id = (String)result.getJSONObject(i).getJSONArray("itemId").get(0);
            } catch (JSONException e) {
                e.printStackTrace();
                id = "N/A";
            }

            try {
                title = (String)result.getJSONObject(i).getJSONArray("title").get(0);
            } catch (JSONException e) {
                e.printStackTrace();
                title = "N/A";
            }
            try{
                zipcode = "zip: ";
                zipcode = zipcode.concat((String)result.getJSONObject(i).getJSONArray("postalCode").get(0));
            }catch (JSONException e) {
                e.printStackTrace();
                zipcode = "N/A";
            }
            try{
                photo = (String)result.getJSONObject(i).getJSONArray("galleryURL").get(0);
            }catch (JSONException e) {
                e.printStackTrace();
                photo = "N/A";
            }
            try{

                if (((String)result.getJSONObject(i).getJSONArray("shippingInfo").getJSONObject(0).getJSONArray("shippingServiceCost").getJSONObject(0).get("__value__")).equals("0.0")){
                    shipping = "Free Shipping";
                }else{
                    shipping = "$";
                    shipping = shipping.concat((String)result.getJSONObject(i).getJSONArray("shippingInfo").getJSONObject(0).getJSONArray("shippingServiceCost").getJSONObject(0).get("__value__"));
                }
            }catch (JSONException e) {
                e.printStackTrace();
                shipping = "N/A";
            }
            try{
                price = "$";
                price = price.concat((String)result.getJSONObject(i).getJSONArray("sellingStatus").getJSONObject(0).getJSONArray("currentPrice").getJSONObject(0).get("__value__"));
            }catch (JSONException e) {
                e.printStackTrace();
                price = "N/A";
            }
            try{
                condition = (String)result.getJSONObject(i).getJSONArray("condition").getJSONObject(0).getJSONArray("conditionDisplayName").get(0);
            }catch (JSONException e) {
                e.printStackTrace();
                condition = "N/A";
            }
            try {
                shipinfo = result.getJSONObject(i).getJSONArray("shippingInfo");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            myitems.add(new item(title,zipcode,shipping,condition,price,photo,id));
        }


        RecyclerView myrv = (RecyclerView) findViewById(R.id.recyclerview_item);
        myAdapter = new RecycleViewAdapter(this,myitems);
        myrv.setLayoutManager(new GridLayoutManager(this,2));
        myrv.setAdapter(myAdapter);
    }

    public void makeURL(String keyword, String category, String distance, ArrayList<String> condition, ArrayList<String> shipping, String zipcode, String cc){
        searchUrl = "http://Hw9-env.zsumhb5qdx.us-east-2.elasticbeanstalk.com/keyword/".concat(keyword);
        if (category.equals("All")){
            searchUrl = searchUrl.concat("/category/-1");
        }else{
            String categoryId = "";
            if (category.equals("Art")){categoryId="550";}
            if (category.equals("Baby")){categoryId="2984";}
            if (category.equals("Books")){categoryId="267";}
            if (category.equals("Clothing, Shoes & Accessories")){categoryId="11450";}
            if (category.equals("Computers/Tablets & Networking")){categoryId="58058";}
            if (category.equals("Health & Beauty")){categoryId="26395";}
            if (category.equals("Music")){categoryId="11233";}
            if (category.equals("Video Games & Consoles")){categoryId="1249";}
            searchUrl = searchUrl.concat("/category/");
            searchUrl = searchUrl.concat(categoryId);
        }
        if (condition.size() == 0){
            searchUrl = searchUrl.concat("/condition/-1");
        }else{
            searchUrl = searchUrl.concat("/condition/");
            String kk = "";
            for (int i=0;i<condition.size();i++){
                kk = kk.concat(condition.get(i));
                if (i!=condition.size()-1){
                    kk = kk.concat(",");
                }
            }
            searchUrl = searchUrl.concat(kk);
        }
        if (shipping.size() == 0){
            searchUrl = searchUrl.concat("/shipoption/-1");
        }else{
            searchUrl = searchUrl.concat("/shipoption/");
            String kk = "";
            for (int i=0;i<shipping.size();i++){
                kk = kk.concat(shipping.get(i));
                if (i!=condition.size()-1){
                    kk = kk.concat(",");
                }
            }
            searchUrl = searchUrl.concat(kk);
        }
        if (cc.equals("checked")){
            searchUrl = searchUrl.concat("/distance/");
            searchUrl = searchUrl.concat(distance);
            searchUrl = searchUrl.concat("/zipcode/");
            searchUrl = searchUrl.concat(zipcode);
        }
    }
    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }


}
