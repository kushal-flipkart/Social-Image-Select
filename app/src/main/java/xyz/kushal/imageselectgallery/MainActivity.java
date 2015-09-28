package xyz.kushal.imageselectgallery;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import xyz.kushal.socialimageselect.PhotoAlbumActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intent = new Intent(this, PhotoAlbumActivity.class);
        intent.putExtra("following_class", "xyz.kushal.imageselectgallery.ResultActivity");
        intent.putExtra("photo_type", 1);
        startActivity(intent);
        finish();
    }

}
