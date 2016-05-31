package test.com.testapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.net.Uri;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Display;
import android.view.WindowManager;
import android.widget.ImageView;

import com.facebook.drawee.view.SimpleDraweeView;

public class PhotoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActionBar actionBar = getSupportActionBar();
        setContentView(R.layout.activity_photo);
        actionBar.setTitle("Photo");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Bundle extras = getIntent().getExtras();

        Uri uri = Uri.parse(extras.getString("fullImage"));

        SimpleDraweeView image = (SimpleDraweeView) findViewById(R.id.imgPost);
        image.setImageURI(uri);
    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }
}
