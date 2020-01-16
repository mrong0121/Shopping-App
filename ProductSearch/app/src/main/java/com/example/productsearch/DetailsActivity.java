package com.example.productsearch;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

import static com.example.productsearch.MainActivity.mviewPager;
import static com.example.productsearch.ResultsActivity.myAdapter;
import static com.example.productsearch.WishlistFragment.mywishAdapter;
import static com.example.productsearch.WishlistFragment.mywishrv;
import static com.example.productsearch.WishlistFragment.nowishes;
import static com.example.productsearch.WishlistFragment.num;
import static com.example.productsearch.WishlistFragment.toprice;
import static com.example.productsearch.WishlistFragment.wishdata;

public class DetailsActivity extends AppCompatActivity implements ProductFragment.OnFragmentInteractionListener, ShippingFragment.OnFragmentInteractionListener, PhotosFragment.OnFragmentInteractionListener, SimilarFragment.OnFragmentInteractionListener {
    private String title;
    private String price;
    private String id;
    private String shippingcost;
    private String image;
    private String zip;
    private String condition;
    private RequestQueue myQueue;
    private String itemurl;
    private FloatingActionButton fab;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        TabLayout tblay = (TabLayout) findViewById(R.id.detabLayout);
        final ViewPager viewPager =(ViewPager) findViewById(R.id.pager);
        final PageAdapter2 pageAdapter2 = new PageAdapter2(getSupportFragmentManager(),tblay.getTabCount());
        viewPager.setAdapter(pageAdapter2);
        viewPager.setOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tblay));
        tblay.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }
            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });

        Intent intent = getIntent();
        title = intent.getExtras().getString("title");
        id = intent.getExtras().getString("id");
        price = intent.getExtras().getString("price");
        zip = intent.getExtras().getString("zip");
        condition = intent.getExtras().getString("condition");
        image = intent.getExtras().getString("image");
        shippingcost = intent.getExtras().getString("shippingcost");


        ImageButton ibut =(ImageButton) findViewById(R.id.go);
        ibut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                //mviewPager.setCurrentItem(0);
                if(myAdapter!=null){
                    myAdapter.notifyDataSetChanged();
                }
                mywishAdapter.notifyDataSetChanged();
            }
        });
        TextView tit = (TextView) findViewById(R.id.producttit);
        tit.setText(title);

        ImageButton facebut = (ImageButton) findViewById(R.id.facebut);

        String url ="http://Hw9-env.zsumhb5qdx.us-east-2.elasticbeanstalk.com/itemspec/";

        url = url.concat(id);

        myQueue = Volley.newRequestQueue(getApplicationContext());


        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONObject result = response.getJSONObject("Item");
                    itemurl = (String)result.get("ViewItemURLForNaturalSearch");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        myQueue.add(request);


        facebut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String shareUrl= "https://www.facebook.com/dialog/share?app_id=2306384399603627"+"&href="+itemurl+
                        "&hashtag=%23CSCI571Spring2019Ebay"+"&redirect_uri="+itemurl+"&quote="+"Buy "+title+"for "+price+" from Ebay!";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(shareUrl));
                startActivity(i);
            }
        });

        fab = findViewById(R.id.fab);



        final SharedPreferences myprefs = getSharedPreferences("Myprefs", Context.MODE_PRIVATE);

        if(myprefs.contains(id)){
            fab.setImageResource(R.drawable.cart_remove_2);
        }
        else{
            fab.setImageResource(R.drawable.cart_plus_2);
        }

        final item curr = new item(title,zip,shippingcost,condition,price,image,id);

        final Context context = this;
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(myprefs.contains(id)) {
                    SharedPreferences.Editor myprefsEditor = myprefs.edit();
                    myprefsEditor.remove(id);
                    myprefsEditor.apply();
                    wishdata.clear();
                    Map<String,?> allEntries = myprefs.getAll();

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
                            wishdata.add(new item(title,zip,shipping,condition,price,image,id));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

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


                    fab.setImageResource(R.drawable.cart_plus_2);

                    String ttt = title;

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
                    Toast.makeText(context,tos,Toast.LENGTH_SHORT).show();
                    if(myAdapter!=null){
                        myAdapter.notifyDataSetChanged();
                    }
                    mywishAdapter.notifyDataSetChanged();
                }
                else{
                    SharedPreferences.Editor prefsEditor = myprefs.edit();
                    Gson gson = new Gson();
                    prefsEditor.putString(id,gson.toJson(curr).toString());
                    prefsEditor.apply();

                    wishdata.clear();
                    Map<String,?> allEntries = myprefs.getAll();

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
                            wishdata.add(new item(title,zip,shipping,condition,price,image,id));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

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


                    fab.setImageResource(R.drawable.cart_remove_2);
                    String ttt = title;

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
                    tos = tos.concat(" was added to wish list");
                    Toast.makeText(context, tos,Toast.LENGTH_SHORT).show();
                    if(myAdapter!=null){
                        myAdapter.notifyDataSetChanged();
                    }
                    mywishAdapter.notifyDataSetChanged();
                }

            }
        });





    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
