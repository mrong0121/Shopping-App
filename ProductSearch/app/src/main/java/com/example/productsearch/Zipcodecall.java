package com.example.productsearch;


import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

/**
 * Created by MG on 04-03-2018.
 */

public class Zipcodecall {
    private static Zipcodecall mInstance;
    private RequestQueue mRequestQueue;
    private static Context mCtx;

    public Zipcodecall(Context ctx) {
        mCtx = ctx;
        mRequestQueue = getRequestQueue();
    }

    public static synchronized Zipcodecall getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new Zipcodecall(context);
        }
        return mInstance;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(mCtx.getApplicationContext());
        }
        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req) {
        getRequestQueue().add(req);
    }

    public static void make(Context ctx, String query, Response.Listener<String>
            listener, Response.ErrorListener errorListener) {
        String url = "http://Hw9-env.zsumhb5qdx.us-east-2.elasticbeanstalk.com/autozip/" + query;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                listener, errorListener);
        Zipcodecall.getInstance(ctx).addToRequestQueue(stringRequest);
    }
}