package xyz.kushal.socialimageselect;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import ly.kite.instagramphotopicker.InstagramPhoto;
import xyz.kushal.fragments.InstagramFragment;
import xyz.kushal.fragments.SdCardFragment;
import xyz.kushal.utils.ApplicationController;
import xyz.kushal.utils.MediaStorePhoto;

public class PhotoAlbumActivity extends AppCompatActivity {
    InstagramFragment instagramFragment;
    SdCardFragment sdCardFragment;
    ArrayList<InstagramPhoto> instagramPhotoList = new ArrayList<>();
    ArrayList<MediaStorePhoto> sdCardPhotoList = new ArrayList<>();

    int sdCardImage = 0, instagramImages = 0;
    int count = 0;

    private int photoType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_album);

        photoType = getIntent().getIntExtra("photo_type", 0);

        instagramFragment = new InstagramFragment();
        sdCardFragment = new SdCardFragment();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setDisplayHomeAsUpEnabled(true);
            ab.setHomeButtonEnabled(true);
        }

        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        if (viewPager != null) {
            setupViewPager(viewPager);
        }

        sdCardFragment.setCallback(new SdCardFragment.PhotoCountChangeCallBack() {
            @Override
            public void photoCountChanged(ArrayList<MediaStorePhoto> photoList) {
                sdCardPhotoList.clear();
                for (MediaStorePhoto photo : photoList)
                    sdCardPhotoList.add(photo);

                sdCardImage = sdCardPhotoList.size();

                if (ab != null) {
                    ab.setTitle(String.valueOf(sdCardImage + instagramImages));
                }
            }
        });

        instagramFragment.setCallback(new InstagramFragment.PhotoCountChangeCallBack() {
            @Override
            public void photoCountChanged(InstagramPhoto[] instagramPhotos) {
                instagramPhotoList.clear();
                Collections.addAll(instagramPhotoList, instagramPhotos);
                instagramImages = instagramPhotoList.size();
                if (ab != null) {
                    ab.setTitle(String.valueOf(sdCardImage + instagramImages));
                }
            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (instagramImages != 0) {
                    saveInstagramImages();
                } else {
                    if (sdCardPhotoList.size() != 0)
                        try {
                            Class resultClass = Class.forName(getIntent().getStringExtra("following_class"));
                            Intent mIntent = new Intent(PhotoAlbumActivity.this, resultClass);
                            mIntent.putParcelableArrayListExtra("selected_photo_list", sdCardPhotoList);
                            mIntent.putExtra("photo_type", photoType);
                            startActivityForResult(mIntent, 3000);
                        } catch (ClassNotFoundException e) {
                            e.printStackTrace();
                        }
                    else new AlertDialog.Builder(PhotoAlbumActivity.this)
                            .setTitle("Nothing selected")
                            .setMessage("Your have not selected any pictures. Please select at least one picture to continue!")
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            })
                            .setIcon(R.drawable.ic_close_black_24dp)
                            .show();
                }
                Snackbar.make(view, "Saving Images Please Wait..", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
    }


    private void setupViewPager(ViewPager viewPager) {
        Adapter adapter = new Adapter(getFragmentManager());
        adapter.addFragment(sdCardFragment, "SD Card");
        adapter.addFragment(instagramFragment, "Instagram");
        viewPager.setAdapter(adapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == android.R.id.home) {
            new AlertDialog.Builder(PhotoAlbumActivity.this)
                    .setTitle("Cancel selection?")
                    .setMessage("Your selection will be lost. Do you want to continue?")
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    })
                    .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // do nothing
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_info)
                    .show();
        }

        return super.onOptionsItemSelected(item);
    }

    public void saveInstagramImages() {
        final String url = String.valueOf(instagramPhotoList.get(0).getFullURL());
        ImageRequest imageRequest = new ImageRequest(url, new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap response) {
                Random r = new Random();
                int no = r.nextInt(10000);
                String path = Environment.getExternalStorageDirectory().toString();
                OutputStream fOut;

                File f = new File(path + "/PrintSquareCrop");
                boolean isDirectoryCreated = f.mkdir();
                Log.e("Is Directory Created? ", String.valueOf(isDirectoryCreated));

                File file = new File(f, "PrintSquare-" + url.substring(url.lastIndexOf("/") + 1, url.lastIndexOf(".")) + ".png");
                Log.e("File", url.substring(url.lastIndexOf("/") + 1, url.lastIndexOf(".")) + ".png");
                if (!file.exists())
                    try {
                        fOut = new FileOutputStream(file);
                        response.compress(Bitmap.CompressFormat.PNG, 100, fOut);
                        fOut.flush();
                        fOut.close();
                        ContentValues values = new ContentValues();
                        values.put(MediaStore.Images.Media.TITLE, file.getName());
                        values.put(MediaStore.Images.Media.DESCRIPTION, "Cropped image for print square.");
                        values.put(MediaStore.Images.Media.DATE_TAKEN, System.currentTimeMillis());
                        values.put(MediaStore.Images.ImageColumns.BUCKET_ID, file.toString().toLowerCase(Locale.US).hashCode());
                        values.put(MediaStore.Images.ImageColumns.BUCKET_DISPLAY_NAME, "Print Square");
                        values.put("_data", file.getAbsolutePath());
                        getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                file.getAbsolutePath();
                sdCardPhotoList.add(new MediaStorePhoto("null", "null", "null", "null", "null", "instagram", file.getAbsolutePath(), "null"));
                instagramPhotoList.remove(0);
                if (instagramPhotoList.size() == 0)
                    if (sdCardPhotoList.size() != 0)
                        try {
                            Class resultClass = Class.forName(getIntent().getStringExtra("following_class"));
                            Intent mIntent = new Intent(PhotoAlbumActivity.this, resultClass);
                            mIntent.putParcelableArrayListExtra("selected_photo_list", sdCardPhotoList);
                            mIntent.putExtra("photo_type", photoType);
                            startActivityForResult(mIntent, 3000);
                        } catch (ClassNotFoundException e) {
                            e.printStackTrace();
                        }
                    else new AlertDialog.Builder(PhotoAlbumActivity.this)
                            .setTitle("Nothing selected")
                            .setMessage("Your have not selected any pictures. Please select at least one picture to continue!")
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            })
                            .setIcon(R.drawable.ic_close_black_24dp)
                            .show();
                else saveInstagramImages();
            }
        }, 0, 0, ImageView.ScaleType.CENTER_INSIDE, null, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        ApplicationController.getInstance().addToRequestQueue(imageRequest);
    }

    static class Adapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragments = new ArrayList<>();
        private final List<String> mFragmentTitles = new ArrayList<>();

        public Adapter(FragmentManager fm) {
            super(fm);
        }

        public void addFragment(Fragment fragment, String title) {
            mFragments.add(fragment);
            mFragmentTitles.add(title);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitles.get(position);
        }
    }
}
