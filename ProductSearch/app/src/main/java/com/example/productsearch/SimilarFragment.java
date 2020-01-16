package com.example.productsearch;

import android.content.Context;
import android.net.Uri;
import android.net.sip.SipSession;
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
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
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
import java.util.Collections;
import java.util.Comparator;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SimilarFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SimilarFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SimilarFragment extends Fragment implements SimilarAdapter.ListItemClickListener{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private RequestQueue myQueue;
    private TextView tv;
    private RelativeLayout pb;
    private LinearLayout ly;
    private Spinner spinitem;
    private Spinner spinorder;

    private int sortitem;
    private int sortorder;

    private SimilarAdapter adapter;

    public SimilarFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static SimilarFragment newInstance(String param1, String param2) {
        SimilarFragment fragment = new SimilarFragment();
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
        return inflater.inflate(R.layout.fragment_similar, container, false);
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
        String url ="http://Hw9-env.zsumhb5qdx.us-east-2.elasticbeanstalk.com/similarid/";
        url = url.concat(id);

        myQueue = Volley.newRequestQueue(getActivity().getApplicationContext());


        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray result = response.getJSONObject("getSimilarItemsResponse").getJSONObject("itemRecommendations").getJSONArray("item");
                    makesimilardata(result);
                } catch (JSONException e) {
                    e.printStackTrace();
                    tv = getView().findViewById(R.id.textNoresultss);
                    tv.setVisibility(View.VISIBLE);
                    pb =getView().findViewById(R.id.progresslayout);
                    pb.setVisibility(View.GONE);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                tv = getView().findViewById(R.id.textNoresultss);
                tv.setVisibility(View.VISIBLE);
                pb =getView().findViewById(R.id.progresslayout);
                pb.setVisibility(View.GONE);
            }
        });
        myQueue.add(request);

    }

    public void makesimilardata(JSONArray result){
        final ArrayList<SimilarItem> similardata = new ArrayList<>();
        if(result.length() == 0){
            tv = getView().findViewById(R.id.textNoresultss);
            tv.setVisibility(View.VISIBLE);
            pb =getView().findViewById(R.id.progresslayout);
            pb.setVisibility(View.GONE);
        }else{
            for (int i=0;i<result.length();i++){
                String image;
                String title;
                Double shipping;
                int daysleft;
                Double price;
                String viewurl;
                try {
                    image = (String)result.getJSONObject(i).get("imageURL");
                } catch (JSONException e) {
                    e.printStackTrace();
                    image = "N/A";
                }
                try {
                    title = (String)result.getJSONObject(i).get("title");
                } catch (JSONException e) {
                    e.printStackTrace();
                    title = "N/A";
                }
                try {
                    shipping = Double.parseDouble((String)result.getJSONObject(i).getJSONObject("shippingCost").get("__value__"));
                } catch (JSONException e) {
                    e.printStackTrace();
                    shipping = -1.0;
                }
                try {
                    String days = (String)result.getJSONObject(i).get("timeLeft");
                    daysleft = Integer.parseInt(days.substring(days.indexOf('P')+1,days.indexOf("D")));
                } catch (JSONException e) {
                    e.printStackTrace();
                    daysleft = -1;
                }
                try {
                    price = Double.parseDouble((String)result.getJSONObject(i).getJSONObject("buyItNowPrice").get("__value__"));
                } catch (JSONException e) {
                    e.printStackTrace();
                    price = -1.0;
                }
                try {
                    viewurl = (String)result.getJSONObject(i).get("viewItemURL");
                } catch (JSONException e) {
                    e.printStackTrace();
                    viewurl = "N/A";
                }
                similardata.add(new SimilarItem(i,image,title,shipping,daysleft,price,viewurl));
            }


            pb =getView().findViewById(R.id.progresslayout);
            pb.setVisibility(View.GONE);

            ly = getView().findViewById(R.id.simspinlay);
            ly.setVisibility(View.VISIBLE);

            RecyclerView recyclerView = getView().findViewById(R.id.similarrecycler);

            adapter = new SimilarAdapter(similardata, this);
            recyclerView.setLayoutManager(new GridLayoutManager(getContext(),1));

            recyclerView.setAdapter(adapter);

            spinitem = getView().findViewById(R.id.sortitemspin);
            spinorder = getView().findViewById(R.id.sortorderspin);
            spinitem.setSelection(0);
            spinorder.setSelection(0);
            spinorder.setEnabled(false);
            sortitem = 0;
            sortorder = 1;
            spinitem.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    switch (position){
                        case 0:
                            sortitem = 0;
                            spinorder.setEnabled(false);
                            sortorder = 1;
                            spinorder.setSelection(0);
                            Collections.sort(similardata, new Comparator<SimilarItem>() {
                                @Override
                                public int compare(SimilarItem r1, SimilarItem r2) {
                                    if (r1.getIndex() > r2.getIndex()) {
                                        return sortorder;
                                    } else if (r1.getIndex() < r2.getIndex()) {
                                        return -1 * sortorder;
                                    } else {
                                        return 0;
                                    }
                                }
                            });
                            break;
                        case 1:
                            sortitem = 1;
                            Collections.sort(similardata, new Comparator<SimilarItem>() {
                                @Override
                                public int compare(SimilarItem r1, SimilarItem r2) {
                                    if (r1.getTitle().compareTo(r2.getTitle())>0) {
                                        return sortorder;
                                    } else if (r1.getTitle().compareTo(r2.getTitle())<0) {
                                        return -1 * sortorder;
                                    } else {
                                        return 0;
                                    }
                                }
                            });
                            spinorder.setEnabled(true);
                            break;
                        case 2:
                            sortitem = 2;
                            Collections.sort(similardata, new Comparator<SimilarItem>() {
                                @Override
                                public int compare(SimilarItem r1, SimilarItem r2) {
                                    if (r1.getPrice() > r2.getPrice()) {
                                        return sortorder;
                                    } else if (r1.getPrice() < r2.getPrice()) {
                                        return -1 * sortorder;
                                    } else {
                                        return 0;
                                    }
                                }
                            });
                            spinorder.setEnabled(true);
                            break;
                        case 3:
                            sortitem = 3;
                            Collections.sort(similardata, new Comparator<SimilarItem>() {
                                @Override
                                public int compare(SimilarItem r1, SimilarItem r2) {
                                    if (r1.getDaysleft() > r2.getDaysleft()) {
                                        return sortorder;
                                    } else if (r1.getDaysleft() < r2.getDaysleft()) {
                                        return -1 * sortorder;
                                    } else {
                                        return 0;
                                    }
                                }
                            });
                            spinorder.setEnabled(true);
                            break;
                    }
                    adapter.notifyDataSetChanged();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

            spinorder.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    switch (position) {
                        case 0:
                            sortorder = 1;
                            if (sortitem == 1) {
                                Collections.sort(similardata, new Comparator<SimilarItem>() {
                                    @Override
                                    public int compare(SimilarItem r1, SimilarItem r2) {
                                        if (r1.getTitle().compareTo(r2.getTitle()) > 0) {
                                            return sortorder;
                                        } else if (r1.getTitle().compareTo(r2.getTitle()) < 0) {
                                            return -1 * sortorder;
                                        } else {
                                            return 0;
                                        }
                                    }
                                });
                            } else if (sortitem == 2) {
                                Collections.sort(similardata, new Comparator<SimilarItem>() {
                                    @Override
                                    public int compare(SimilarItem r1, SimilarItem r2) {
                                        if (r1.getPrice() > r2.getPrice()) {
                                            return sortorder;
                                        } else if (r1.getPrice() < r2.getPrice()) {
                                            return -1 * sortorder;
                                        } else {
                                            return 0;
                                        }
                                    }
                                });
                            } else if (sortitem == 3) {
                                Collections.sort(similardata, new Comparator<SimilarItem>() {
                                    @Override
                                    public int compare(SimilarItem r1, SimilarItem r2) {
                                        if (r1.getDaysleft() > r2.getDaysleft()) {
                                            return sortorder;
                                        } else if (r1.getDaysleft() < r2.getDaysleft()) {
                                            return -1 * sortorder;
                                        } else {
                                            return 0;
                                        }
                                    }
                                });
                            }
                            break;
                        case 1:
                            sortorder = -1;
                            if (sortitem == 1) {
                                Collections.sort(similardata, new Comparator<SimilarItem>() {
                                    @Override
                                    public int compare(SimilarItem r1, SimilarItem r2) {
                                        if (r1.getTitle().compareTo(r2.getTitle()) > 0) {
                                            return sortorder;
                                        } else if (r1.getTitle().compareTo(r2.getTitle()) < 0) {
                                            return -1 * sortorder;
                                        } else {
                                            return 0;
                                        }
                                    }
                                });
                            } else if (sortitem == 2) {
                                Collections.sort(similardata, new Comparator<SimilarItem>() {
                                    @Override
                                    public int compare(SimilarItem r1, SimilarItem r2) {
                                        if (r1.getPrice() > r2.getPrice()) {
                                            return sortorder;
                                        } else if (r1.getPrice() < r2.getPrice()) {
                                            return -1 * sortorder;
                                        } else {
                                            return 0;
                                        }
                                    }
                                });
                            } else if (sortitem == 3) {
                                Collections.sort(similardata, new Comparator<SimilarItem>() {
                                    @Override
                                    public int compare(SimilarItem r1, SimilarItem r2) {
                                        if (r1.getDaysleft() > r2.getDaysleft()) {
                                            return sortorder;
                                        } else if (r1.getDaysleft() < r2.getDaysleft()) {
                                            return -1 * sortorder;
                                        } else {
                                            return 0;
                                        }
                                    }
                                });
                            }
                            break;

                    }
                    adapter.notifyDataSetChanged();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });


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

    @Override
    public void onListItemClick(int clickedItemIndex) {

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
