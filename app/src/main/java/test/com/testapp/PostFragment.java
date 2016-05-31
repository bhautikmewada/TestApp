package test.com.testapp;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.google.gson.JsonArray;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

public class PostFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    PostRecyclerAdapter postAdapter;
    RecyclerView postRecyclerView;
    JsonArray postArray;
    SwipeRefreshLayout swipeRefreshLayout;
    ProgressDialog pDialog;

    TestApplication app = null;

    public PostFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        getActivity().setTitle("Post");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_post, container, false);

        getActivity().setProgressBarIndeterminateVisibility(true);

        pgDialog();

        postRecyclerView = (RecyclerView) view.findViewById(R.id.post_recycler_view);
        postArray = new JsonArray();
        RecyclerView.LayoutManager recyclerViewLayoutManager = new LinearLayoutManager(getActivity());
        postRecyclerView.setLayoutManager(recyclerViewLayoutManager);

        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.post_swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                getPost();
            }
        });

        getPost();

        return view;
    }

    void getPost()
    {
        if(app == null) {
                    app = (TestApplication) getActivity().getApplicationContext();
        }

        if(app.isNetworkAvailable()) {
            Ion.with(getActivity().getApplicationContext())
                    .load(Constants.URL + "/posts")
                    .asJsonArray()
                    .setCallback(new FutureCallback<JsonArray>() {
                        @Override
                        public void onCompleted(Exception e, JsonArray result) {

                            postArray = result;
                            postAdapter = new PostRecyclerAdapter(result, getActivity());
                            postRecyclerView.setAdapter(postAdapter);

                            if (pDialog.isShowing()) {
                                pDialog.dismiss();
                            }
                            swipeRefreshLayout.setRefreshing(false);
                        }
                    });
        }
        else
        {
            postAdapter = new PostRecyclerAdapter();
            postRecyclerView.setAdapter(postAdapter);

            if (pDialog.isShowing()) {
                pDialog.dismiss();
            }

            if(swipeRefreshLayout.isRefreshing()) {
                swipeRefreshLayout.setRefreshing(false);
            }

            Toast.makeText(getActivity().getApplicationContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();
        }
    }

    // Create Progress Dialog
    void pgDialog()
    {
        if(pDialog == null) {
            pDialog = new ProgressDialog(getActivity());
            pDialog.setMessage("Loading...");
            pDialog.setCancelable(false);
            pDialog.show();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {

        savedInstanceState.putString("post", postArray.toString());
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
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
