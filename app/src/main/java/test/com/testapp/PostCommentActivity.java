package test.com.testapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.google.gson.JsonArray;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

public class PostCommentActivity extends AppCompatActivity {

    ProgressDialog pDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();

        Intent intent = getIntent();
        String post_id = intent.getStringExtra("post_id");

        if (actionBar != null) {
            actionBar.setTitle("Post "+ post_id +" Comments");
        }
        setContentView(R.layout.activity_post_comment);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        final RecyclerView commentRecyclerView = (RecyclerView) findViewById(R.id.post_comment_recycler_view);
        pgDialog();
        RecyclerView.LayoutManager recyclerViewLayoutManager = new LinearLayoutManager(this);
        commentRecyclerView.setLayoutManager(recyclerViewLayoutManager);

        Ion.with(getApplicationContext())
                .load(Constants.URL + "/posts/"+ post_id +"/comments")
                .asJsonArray()
                .setCallback(new FutureCallback<JsonArray>() {
                    @Override
                    public void onCompleted(Exception e, JsonArray result) {
                        // do stuff with the result or error
                        PostCommentRecyclerAdapter postCommentAdapter = new PostCommentRecyclerAdapter(result, getApplicationContext());
                        commentRecyclerView.setAdapter(postCommentAdapter);
                        pDialog.dismiss();
                    }
                });
    }

    void pgDialog()
    {
        if(pDialog == null) {
            pDialog = new ProgressDialog(this);
            pDialog.setMessage("Loading...");
            pDialog.setCancelable(false);
            pDialog.show();
        }
    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }
}
