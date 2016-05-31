package test.com.testapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link PhotosFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link PhotosFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PhotosFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    PhotoRecyclerAdapter photoAdapter;
    RecyclerView photoRecyclerView;

    private OnFragmentInteractionListener mListener;
    private boolean reverseLayout;
    SwipeRefreshLayout swipeRefreshLayout;
    JsonArray photoArray;
    TestApplication app = null;
    ProgressDialog pDialog;

    public PhotosFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        getActivity().setTitle("Photos");
        reverseLayout = false;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_photos, container, false);

        // back arrow in action bar
        getActivity().setProgressBarIndeterminateVisibility(true);

        photoArray = new JsonArray();
        photoRecyclerView = (RecyclerView) v.findViewById(R.id.photo_recycler_view);
        RecyclerView.LayoutManager recyclerViewLayoutManager = new GridLayoutManager(getActivity().getApplicationContext(), 2);
        photoRecyclerView.addItemDecoration(new GridSpacingItemDecoration(2, 0, true));
        photoRecyclerView.setLayoutManager(recyclerViewLayoutManager);

        pgDialog();

        swipeRefreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.photo_swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener(){
            @Override
            public void onRefresh() {
                getPhotos();
            }
        });

        getPhotos();

        return v;
    }

    void getPhotos()
    {
        if(app == null) {
            app = (TestApplication) getActivity().getApplicationContext();
        }

        if(app.isNetworkAvailable()) {
            Ion.with(getActivity().getApplicationContext())
                    .load(Constants.URL + "/photos")
                    .asJsonArray()
                    .setCallback(new FutureCallback<JsonArray>() {
                        @Override
                        public void onCompleted(Exception e, JsonArray result) {

                            /** array of result is passing to Photo Adapter */
                            photoArray = result;
                            photoAdapter = new PhotoRecyclerAdapter(result, getActivity().getApplicationContext());
                            photoRecyclerView.setAdapter(photoAdapter);

                            if (pDialog.isShowing()) {
                                pDialog.dismiss();
                            }

                            swipeRefreshLayout.setRefreshing(false);
                        }
                    });
        }
        else
        {
            photoAdapter = new PhotoRecyclerAdapter();
            photoRecyclerView.setAdapter(photoAdapter);

            if (pDialog.isShowing()) {
                pDialog.dismiss();
            }

            if(swipeRefreshLayout.isRefreshing()) {
                swipeRefreshLayout.setRefreshing(false);
            }

            Toast.makeText(getActivity().getApplicationContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();
        }
    }

    // create Progress Dialog
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
        savedInstanceState.putString("photos", photoArray.toString());
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

    // Manage Spacing of Grid
    public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

        private int spanCount;
        private int spacing;
        private boolean includeEdge;

        public GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
            this.spanCount = spanCount;
            this.spacing = spacing;
            this.includeEdge = includeEdge;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view); // item position
            int column = position % spanCount; // item column

            if (includeEdge) {
                outRect.left = spacing - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
                outRect.right = (column + 1) * spacing / spanCount; // (column + 1) * ((1f / spanCount) * spacing)

                if (position < spanCount) { // top edge
                    outRect.top = spacing;
                }
                outRect.bottom = spacing; // item bottom
            } else {
                outRect.left = column * spacing / spanCount; // column * ((1f / spanCount) * spacing)
                outRect.right = spacing - (column + 1) * spacing / spanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)
                if (position >= spanCount) {
                    outRect.top = spacing; // item top
                }
            }
        }
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
