package test.com.testapp;

import android.content.Context;
import android.os.Build;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.JsonArray;

import org.w3c.dom.Text;

/**
 * Created by bhautik on 26/05/16.
 */
public class PostCommentRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    JsonArray postcommentsArray;
    Context context;

    PostCommentRecyclerAdapter(JsonArray postComment, Context con)
    {
        postcommentsArray = new JsonArray();
        postcommentsArray = postComment;
        context = con;
    }

    class PostCommentHolder extends RecyclerView.ViewHolder
    {
        public TextView textview_post_comment_title;
        public PostCommentHolder(View itemView) {
            super(itemView);

            textview_post_comment_title = (TextView) itemView.findViewById(R.id.textview_post_title);
            CardView card = (CardView) itemView.findViewById(R.id.card_post_view);
            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                int margin = (int) dipToPixels(context.getApplicationContext(), 5);
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

        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.post_recycler_adapter, parent, false);

        PostCommentHolder postAdapter =  new PostCommentHolder(v);

        return postAdapter;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final PostCommentHolder postCommentAdapter = (PostCommentHolder) holder;

        String strComment = postcommentsArray.get(position).getAsJsonObject().get("body").toString();
        strComment = strComment.substring(1, strComment.length() - 1);
        final String finalStrComment = strComment.replace("\\n","\n");
        postCommentAdapter.textview_post_comment_title.setText(finalStrComment);
    }

    @Override
    public int getItemCount() {
        return postcommentsArray.size();
    }
}
