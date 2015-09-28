package xyz.kushal.fragments;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import ly.kite.instagramphotopicker.InstagramPhoto;
import xyz.kushal.adapters.SdCardAlbumAdapter;
import xyz.kushal.socialimageselect.R;
import xyz.kushal.utils.DividerItemDecoration;
import xyz.kushal.utils.MediaStoreHelperMethods;
import xyz.kushal.utils.MediaStorePhoto;

/**
 * A placeholder fragment containing a simple view.
 */
public class SdCardFragment extends Fragment {

    private ArrayList<MediaStorePhoto> selectedPhotoList = new ArrayList<>();
    private ArrayList<MediaStorePhoto> bucketItemList = new ArrayList<>();
    private ArrayList<Integer> bucketTotalImageCount = new ArrayList<>();
    private ArrayList<Integer> bucketSelectedImageCount = new ArrayList<>();

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private LinearLayoutManager mLayoutManager;
    private PhotoCountChangeCallBack photoCountChangeCallBack;

    public SdCardFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_sd_card, container, false);

        mRecyclerView = (RecyclerView) v.findViewById(R.id.my_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mAdapter = new SdCardAlbumAdapter(bucketItemList, selectedPhotoList, getActivity(), this);
        mLayoutManager = new LinearLayoutManager(getActivity());

        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST));

        getPhotoAlbumData();
        return v;
    }


    public void getPhotoAlbumData() {
        bucketItemList.clear();
        bucketTotalImageCount.clear();
        bucketSelectedImageCount.clear();

        bucketTotalImageCount = getBucketTotalImageCount();
        bucketSelectedImageCount = getBucketSelectedImageCount();

        ArrayList<MediaStorePhoto> mList = new ArrayList<>(MediaStoreHelperMethods.getBucketCoverItems(getActivity().getContentResolver()));

        for (int i = 0; i < mList.size(); i++) {
            MediaStorePhoto photo = mList.get(i);
            photo.setBucket(photo.getBucket() + "\n" + bucketSelectedImageCount.get(i) + "/" + bucketTotalImageCount.get(i) + " Selected");
            bucketItemList.add(photo);
        }

        mAdapter.notifyDataSetChanged();
    }

    public ArrayList<Integer> getBucketTotalImageCount() {
        ArrayList<Integer> countList = new ArrayList<>();
        for (MediaStorePhoto photo : MediaStoreHelperMethods.getBucketCoverItems(getActivity().getContentResolver())) {
            countList.add(MediaStoreHelperMethods.getAllPhotosInBucket(photo.getBucketId(), getActivity().getContentResolver()).size());
        }
        return countList;
    }

    public ArrayList<Integer> getBucketSelectedImageCount() {
        ArrayList<Integer> countList = new ArrayList<>();
        for (MediaStorePhoto photo : MediaStoreHelperMethods.getBucketCoverItems(getActivity().getContentResolver())) {
            int count = 0;
            for (MediaStorePhoto mPhoto : selectedPhotoList) {
                if (photo.getBucketId().equals(mPhoto.getBucketId()))
                    count++;
            }
            countList.add(count);
        }
        return countList;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1000 && resultCode == getActivity().RESULT_OK) {
            selectedPhotoList.clear();
            ArrayList<MediaStorePhoto> photoList = data.getParcelableArrayListExtra("selected_photo_list");
            for (MediaStorePhoto mPhoto : photoList) {
                selectedPhotoList.add(mPhoto);
            }
            getPhotoAlbumData();
            if (photoCountChangeCallBack != null) {
                photoCountChangeCallBack.photoCountChanged(selectedPhotoList);
            }
        }
        if (requestCode == 3000 && resultCode == getActivity().RESULT_OK) {
            Intent intent = new Intent();
            getActivity().setResult(getActivity().RESULT_OK, intent);
            getActivity().finish();
        }
    }


    public void setCallback(PhotoCountChangeCallBack photoCountChangeCallBack) {
        this.photoCountChangeCallBack = photoCountChangeCallBack;
    }

    public interface PhotoCountChangeCallBack {
        void photoCountChanged(ArrayList<MediaStorePhoto> photoList);
    }

}
