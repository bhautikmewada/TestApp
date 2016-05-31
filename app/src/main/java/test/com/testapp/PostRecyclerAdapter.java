package test.com.testapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 * Created by bhautik on 22/05/16.
 */

public class PostRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    JsonArray responseData;
    Activity activity;

    PostRecyclerAdapter(JsonArray result, Activity act)
    {
        responseData = new JsonArray();
        responseData = result;
        activity = act;
    }

    PostRecyclerAdapter()
    {

    }

    class PostAdapterHolder extends RecyclerView.ViewHolder
    {
        TextView textView_post_title;

        public PostAdapterHolder(View itemView) {
            super(itemView);
            textView_post_title = (TextView) itemView.findViewById(R.id.textview_post_title);
            CardView card = (CardView) itemView.findViewById(R.id.card_post_view);
            float density = itemView.getResources().getDisplayMetrics().density;

            if(density == 0.75)
            {
                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(new RecyclerView.LayoutParams(RecyclerView.LayoutParams.WRAP_CONTENT, RecyclerView.LayoutParams.WRAP_CONTENT));
                params.setMargins(10, 0, 10, 0);
                textView_post_title.setLayoutParams(params);
            }

            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                int margin = (int) dipToPixels(activity.getApplicationContext(), 5);
                params.setMargins(margin, margin, margin, margin);
                card.setLayoutParams(params);
            }
        }
    }

    public static float dipToPixels(Context context, int dipValue) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dipValue, metrics);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.post_recycler_adapter, parent, false);
        PostAdapterHolder postAdapter =  new PostAdapterHolder(view);

        return postAdapter;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final PostAdapterHolder postAdapter = (PostAdapterHolder) holder;

        String finalStrComment = "";
        final JsonObject object = responseData.get(position).getAsJsonObject();
        String stringTitle = responseData.get(position).getAsJsonObject().get("title").toString();
        stringTitle = stringTitle.substring(1, stringTitle.length() - 1);
        finalStrComment = stringTitle;
        postAdapter.textView_post_title.setText(finalStrComment);

        holder.itemView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                Intent act = new Intent(postAdapter.itemView.getContext(), PostCommentActivity.class);
                act.putExtra("post_id", object.get("id").toString());
                postAdapter.itemView.getContext().startActivity(act);
            }
        });
    }

    @Override
    public int getItemCount() {

        int size;
        if(responseData == null)
        {
            size = 0;
        }
        else
        {
            size = responseData.size();
        }
        return size;
    }
}