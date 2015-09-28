package xyz.kushal.fragments;


import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.ArrayList;

import ly.kite.instagramphotopicker.InstagramPhoto;
import ly.kite.instagramphotopicker.InstagramPhotoPicker;
import xyz.kushal.adapters.InstagramAdapter;
import xyz.kushal.socialimageselect.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class InstagramFragment extends Fragment {

    static final String CLIENT_ID = "4ea8653722ff4cb5b9bc3f3564fda611";
    static final String REDIRECT_URI = "http://printsquare.co";
    static final int REQUEST_CODE_INSTAGRAM_PICKER = 1;


    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private LinearLayoutManager mLayoutManager;

    private ArrayList<InstagramPhoto> photoList = new ArrayList<>();

    private PhotoCountChangeCallBack photoCountChangeCallBack;

    public InstagramFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_instagram, container, false);
        Button selectImagesButton = (Button) v.findViewById(R.id.instagram_button);
        selectImagesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InstagramPhotoPicker.startPhotoPickerForResult(InstagramFragment.this, CLIENT_ID, REDIRECT_URI, REQUEST_CODE_INSTAGRAM_PICKER);
            }
        });

        mRecyclerView = (RecyclerView) v.findViewById(R.id.my_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mAdapter = new InstagramAdapter(photoList, getActivity());
        mLayoutManager = new GridLayoutManager(getActivity(), 3);

        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(mLayoutManager);
        return v;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                photoList.clear();
                InstagramPhoto[] instagramPhotos = InstagramPhotoPicker.getResultPhotos(data);
                Log.e("Info", "User selected " + instagramPhotos.length + " Instagram photos");
                for (int i = 0; i < instagramPhotos.length; ++i) {
                    photoList.add(instagramPhotos[i]);
                }
                mAdapter.notifyDataSetChanged();
                if (photoCountChangeCallBack != null) {
                    photoCountChangeCallBack.photoCountChanged(instagramPhotos);
                }
            }
        }
    }

    public void setCallback(PhotoCountChangeCallBack photoCountChangeCallBack) {
        this.photoCountChangeCallBack = photoCountChangeCallBack;
    }

    public interface PhotoCountChangeCallBack {
        void photoCountChanged(InstagramPhoto[] instagramPhotos);
    }

}
