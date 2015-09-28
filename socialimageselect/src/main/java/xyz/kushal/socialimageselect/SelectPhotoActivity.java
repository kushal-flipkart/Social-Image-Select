package xyz.kushal.socialimageselect;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;

import java.util.ArrayList;

import xyz.kushal.adapters.SelectPhotoAdapter;
import xyz.kushal.utils.MediaStoreHelperMethods;
import xyz.kushal.utils.MediaStorePhoto;

public class SelectPhotoActivity extends AppCompatActivity {

    private ArrayList<MediaStorePhoto> selectedPhotoList;

    private String bucketId;
    private ArrayList<MediaStorePhoto> bucketPhotoList;

    private RecyclerView mRecyclerView;
    private SelectPhotoAdapter mAdapter;
    private LinearLayoutManager mLayoutManager;

    private int count;
    private ActionBar ab;

    private FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_photo);

        ab = getSupportActionBar();

        if (ab != null) {
            ab.setDisplayHomeAsUpEnabled(true);
            ab.setHomeButtonEnabled(true);
        }

        bucketId = getIntent().getStringExtra("bucket_id");
        selectedPhotoList = getIntent().getParcelableArrayListExtra("selected_photo_list");
        bucketPhotoList = MediaStoreHelperMethods.getAllPhotosInBucket(bucketId, getContentResolver());

        /**
         * Set status of checked photos in bucket photo list
         */

        for (int i = 0; i < bucketPhotoList.size(); i++) {
            for (MediaStorePhoto photo : selectedPhotoList) {
                if (photo.getId().equals(bucketPhotoList.get(i).getId())) {
                    bucketPhotoList.get(i).setStatus("checked");
                    count++;
                    break;
                }
            }
        }

        ab.setTitle(count + " Photos Selected");
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setImageResource(R.drawable.ic_done);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ArrayList<MediaStorePhoto> tempPhotoList = new ArrayList<>(selectedPhotoList);
                selectedPhotoList.clear();
                for (MediaStorePhoto photo : tempPhotoList) {
                    if (!photo.getBucketId().equals(bucketId)) {
                        selectedPhotoList.add(photo);
                    }
                }
                for (MediaStorePhoto photo : bucketPhotoList) {
                    if (photo.getStatus().equals("checked")) {
                        selectedPhotoList.add(photo);
                    }
                }

                Intent mIntent = new Intent();
                mIntent.putParcelableArrayListExtra("selected_photo_list", selectedPhotoList);
                setResult(RESULT_OK, mIntent);
                finish();
            }
        });

        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mAdapter = new SelectPhotoAdapter(bucketPhotoList, this);
        mLayoutManager = new GridLayoutManager(this, 3);

        mAdapter.setCallback(new SelectPhotoAdapter.SelectPhotoCallback() {
            @Override
            public void selectViewPressed(int position, String status) {
                bucketPhotoList.get(position).setStatus(status);
                if (status.equals("checked")) count++;
                else count--;
                ab.setTitle(count + " Photos Selected");
            }
        });

        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(mLayoutManager);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == android.R.id.home) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }
}
