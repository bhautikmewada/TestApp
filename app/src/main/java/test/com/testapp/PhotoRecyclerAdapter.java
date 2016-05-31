package test.com.testapp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.net.Uri;
import android.os.Build;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.facebook.drawee.view.SimpleDraweeView;
import com.google.gson.JsonArray;

/**
 * Created by bhautik on 24/05/16.
 */
public class PhotoRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>  {

    JsonArray photoArray;

    Context context;

    public PhotoRecyclerAdapter(JsonArray result, Context appContext) {

        photoArray = new JsonArray();
        photoArray = result;
        context = appContext;
    }

    public PhotoRecyclerAdapter()
    {

    }

    class PhotoAdapterHolder extends RecyclerView.ViewHolder
    {
        SimpleDraweeView draweeView;

        public PhotoAdapterHolder(View itemView) {
            super(itemView);

            draweeView = (SimpleDraweeView) itemView.findViewById(R.id.imgView);
            CardView card = (CardView) itemView.findViewById(R.id.card_view);
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
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.photo_recycler_adapter, parent, false);

        PhotoAdapterHolder photoAdapter =  new PhotoAdapterHolder(view);
        return photoAdapter;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final PhotoAdapterHolder photoAdapter = (PhotoAdapterHolder) holder;

        String strUrl = photoArray.get(position).getAsJsonObject().get("thumbnailUrl").toString();
        strUrl = strUrl.substring(1, strUrl.length() - 1);
        Uri uri = Uri.parse(strUrl);
        photoAdapter.draweeView.setImageURI(uri);

        WindowManager wm = (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();

        Point size = new Point();
        display.getSize(size);
        int screenWidth = size.x/2;

        photoAdapter.draweeView.getLayoutParams().width = screenWidth;
        photoAdapter.draweeView.getLayoutParams().height = screenWidth;

        photoAdapter.draweeView.requestLayout();

        holder.itemView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                String strUrl = photoArray.get(position).getAsJsonObject().get("url").toString();
                strUrl = strUrl.substring(1, strUrl.length() - 1);
                final Uri uri = Uri.parse(strUrl);

                Intent act = new Intent(photoAdapter.itemView.getContext(), PhotoActivity.class);
                act.putExtra("fullImage", uri.toString());
                photoAdapter.itemView.getContext().startActivity(act);
            }
        });
    }

    @Override
    public int getItemCount() {

        int size;
        if(photoArray == null)
        {
            size = 0;
        }
        else
        {
            size = photoArray.size();
        }
        return size;
    }
}

