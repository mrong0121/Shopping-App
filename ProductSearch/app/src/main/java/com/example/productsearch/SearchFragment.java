package com.example.productsearch;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatAutoCompleteTextView;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

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


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SearchFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SearchFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SearchFragment extends Fragment {
    private static final String TAG = "SearchFragment";
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private EditText keywordtext;
    private Spinner categoryspin;
    private CheckBox checknew;
    private CheckBox checkused;
    private CheckBox checkunspec;
    private CheckBox checklocal;
    private CheckBox checkship;
    private CheckBox checkenable;
    private RadioGroup radiogroup;
    private AppCompatAutoCompleteTextView ziptext;
    private TextView keyworderror;
    private TextView zipcodeerror;
    private RadioButton radiocurr;
    private RadioButton radiozip;
    private LinearLayout locationlayout;
    private EditText distancetext;

    private Button butsearch;
    private Button butclear;


    private String keyword;
    private String category;

    private String distance;
    private String zipcode;




    private RequestQueue myQueue;

    Intent results;

    private String searchUrl;

    private static final int TRIGGER_AUTO_COMPLETE = 100;
    private static final long AUTO_COMPLETE_DELAY = 300;
    private Handler handler;

    private AutosuggestAdapter autoSuggestAdapter;

    public SearchFragment() {
        // Required empty public constructor
    }

    public static SearchFragment newInstance(String param1, String param2) {
        SearchFragment fragment = new SearchFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        myQueue = Volley.newRequestQueue(getActivity().getApplicationContext());
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        return inflater.inflate(R.layout.fragment_search, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        keywordtext = view.findViewById(R.id.keywordText);
        categoryspin = view.findViewById(R.id.spinner);
        checknew = view.findViewById(R.id.checkNew);
        checkused = view.findViewById(R.id.checkUsed);
        checkunspec = view.findViewById(R.id.checkUnspecified);
        checklocal = view.findViewById(R.id.checkLocal);
        checkship = view.findViewById(R.id.checkFree);
        checkenable = view.findViewById(R.id.checkEnable);
        radiogroup = view.findViewById(R.id.radio_group);
        ziptext = view.findViewById(R.id.zipText);
        locationlayout = view.findViewById(R.id.locationLayout);
        radiocurr = view.findViewById(R.id.radioCurrent);
        radiozip = view.findViewById(R.id.radioZip);
        butsearch = view.findViewById(R.id.butSearch);
        butclear = view.findViewById(R.id.butClear);
        distancetext = view.findViewById(R.id.distanceText);
        keyworderror = view.findViewById(R.id.error_keyword);
        zipcodeerror = view.findViewById(R.id.error_zip);



        //Setting up the adapter for AutoSuggest
        autoSuggestAdapter = new AutosuggestAdapter(this.getContext(),
                android.R.layout.simple_dropdown_item_1line);
        ziptext.setThreshold(2);
        ziptext.setAdapter(autoSuggestAdapter);
        ziptext.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view,
                                            int position, long id) {
                    }
                });

        ziptext.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int
                    count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                handler.removeMessages(TRIGGER_AUTO_COMPLETE);
                handler.sendEmptyMessageDelayed(TRIGGER_AUTO_COMPLETE,
                        AUTO_COMPLETE_DELAY);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                if (msg.what == TRIGGER_AUTO_COMPLETE) {
                    if (!TextUtils.isEmpty(ziptext.getText())) {
                        makeApiCall(ziptext.getText().toString());
                    }
                }
                return false;
            }
        });


        categoryspin.setSelection(0);

        checkenable.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {
                    locationlayout.setVisibility(View.VISIBLE);
                }else{
                    locationlayout.setVisibility(View.GONE);
                }
            }
        });
        radiocurr.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                ziptext.setEnabled(false);
                ziptext.setText("");
            }
        });

        String url = "http://ip-api.com/json";
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    //zipcode = zipcode.concat(response.get("zip").toString());
                    zipcode = response.getString("zip");
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

        radiozip.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                ziptext.setEnabled(true);
            }
        });

        butsearch.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                performSearch();
            }
        });

        butclear.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                clearForm();
            }
        });

    }

    public void performSearch(){
        if (isFormValid()) {
            ArrayList<String> condition = new ArrayList<>();
            ArrayList<String> shipping = new ArrayList<>();
            results = new Intent(getActivity(), ResultsActivity.class);
            keyword = keywordtext.getText().toString();
            results.putExtra("keyword",keyword);
            category = categoryspin.getSelectedItem().toString();
            results.putExtra("category",category);
            distance = distancetext.getText().toString();
            try {
                Double.parseDouble(distance);
            } catch (Exception e) {
                distance = "10";
            }
            results.putExtra("distance",distance);

            if(checknew.isChecked()){
                condition.add("New");
            }
            if(checkused.isChecked()){
                condition.add("Used");
            }
            if(checkunspec.isChecked()){
                condition.add("Unspecified");
            }
            results.putExtra("condition", condition);
            if(checklocal.isChecked()){
                shipping.add("LocalPickup");
            }
            if(checkship.isChecked()){
                shipping.add("FreeShipping");
            }
            results.putExtra("shipping",shipping);

            if(checkenable.isChecked()){
                results.putExtra("enable","checked");
                if (radiocurr.isChecked()){
                    results.putExtra("zipcode",zipcode);
                }else{
                    zipcode = ziptext.getText().toString();
                    results.putExtra("zipcode",zipcode);
                }
            }else{
                results.putExtra("enable","unchecked");
            }
            startActivity(results);
        }else{
//            Toast.makeText(getActivity(), "Please fix all fields with errors",
//                    Toast.LENGTH_LONG).show();
            Toast toast = Toast.makeText(getActivity(), "     Please fix all fields with errors     ", Toast.LENGTH_LONG);
            View viewtt = toast.getView();
            GradientDrawable shape =  new GradientDrawable();
            shape.setColor(Color.parseColor("#f0f0f0"));
            shape.setCornerRadius( 100 );
            viewtt.setBackground(shape);
            //viewtt.setBackgroundColor(Color.parseColor("#f0f0f0"));
            TextView text = (TextView) viewtt.findViewById(android.R.id.message);
            text.setTextColor(Color.BLACK);
            toast.show();
        }
    }


    private void makeApiCall(String text) {
        Zipcodecall.make(this.getContext(), text, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //parsing logic, please change it as per your requirement
                List<String> stringList = new ArrayList<>();
                try {
                    JSONObject responseObject = new JSONObject(response);
                    JSONArray array = responseObject.getJSONArray("postalCodes");
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject row = array.getJSONObject(i);
                        stringList.add(row.getString("postalCode"));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                //IMPORTANT: set data here and notify
                autoSuggestAdapter.setData(stringList);
                autoSuggestAdapter.notifyDataSetChanged();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
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


    public boolean isFormValid() {
        boolean validity = true;
        if (keywordtext.getText() == null || keywordtext.getText().toString().equals("")) {
            keyworderror.setVisibility(View.VISIBLE);
            validity = false;
        }else{
            keyworderror.setVisibility(View.GONE);
        }

        String regex = "\\d{5}";

        if (radiozip.isChecked() && (ziptext.getText().toString().equals("") || ziptext.getText() == null || !ziptext.getText().toString().matches(regex))) {
            zipcodeerror.setVisibility(View.VISIBLE);
            validity = false;
        }else{
            zipcodeerror.setVisibility(View.GONE);
        }

        if (!(keywordtext.getText()==null || keywordtext.getText().toString().equals(""))&&!checkenable.isChecked()){
            validity = true;
        }

        return validity;
    }

    public void clearForm(){
        keywordtext.setText("");
        categoryspin.setSelection(0);
//        checkused.setChecked(false);
//        checknew.setChecked(false);
//        checkunspec.setChecked(false);
//        checklocal.setChecked(false);
//        checkship.setChecked(false);
        checkenable.setChecked(false);
        radiocurr.setChecked(true);
        radiozip.setChecked(false);
        distancetext.setText("");
        ziptext.setText("");
        zipcodeerror.setVisibility(View.GONE);
        keyworderror.setVisibility(View.GONE);
        locationlayout.setVisibility(View.GONE);
    }


}


