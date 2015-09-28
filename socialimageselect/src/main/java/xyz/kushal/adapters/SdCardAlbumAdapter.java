package xyz.kushal.adapters;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import xyz.kushal.socialimageselect.R;
import xyz.kushal.socialimageselect.SelectPhotoActivity;
import xyz.kushal.utils.MediaStorePhoto;
import xyz.kushal.utils.Typefaces;

/**
 * Created by Kush on 9/26/2015.
 * SdCard Album Adapter
 */

public class SdCardAlbumAdapter extends RecyclerView.Adapter {
    private ArrayList<MediaStorePhoto> bucketItemList;
    private ArrayList<MediaStorePhoto> selectedPhotoList;
    private Activity mAct;
    private Fragment mFrag;

    public SdCardAlbumAdapter(ArrayList<MediaStorePhoto> bucketItemList, ArrayList<MediaStorePhoto> selectedPhotoList, Activity mActivity, Fragment mFrag) {
        this.bucketItemList = bucketItemList;
        this.selectedPhotoList = selectedPhotoList;
        this.mAct = mActivity;
        this.mFrag = mFrag;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.layout_bucket_item, viewGroup, false);

        return new BucketItemViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        ((BucketItemViewHolder) holder).setId(position);
        ((BucketItemViewHolder) holder).getImageView().setImageBitmap(null);

        new AsyncTask<Void, Void, Bitmap>() {

            @Override
            protected Bitmap doInBackground(Void... params) {
                return MediaStore.Images.Thumbnails.getThumbnail(
                        mAct.getContentResolver(),
                        Long.parseLong(bucketItemList.get(position).getId()),
                        MediaStore.Images.Thumbnails.MICRO_KIND,
                        null);
            }

            @Override
            protected void onPostExecute(Bitmap bitmap) {
                super.onPostExecute(bitmap);
                if (((BucketItemViewHolder) holder).getId() == position) {
                    ((BucketItemViewHolder) holder).getImageView().setImageBitmap(bitmap);


                }
            }
        }.execute();
        ((BucketItemViewHolder) holder).getNameView().setText(bucketItemList.get(position).getBucket());
        ((BucketItemViewHolder) holder).getNameView().setTypeface(Typefaces.get(mAct, "RobotoSlab-Regular.ttf"));

        ((BucketItemViewHolder) holder).getNameView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mIntent = new Intent(mAct, SelectPhotoActivity.class);
                mIntent.putExtra("bucket_id", bucketItemList.get(position).getBucketId());
                mIntent.putParcelableArrayListExtra("selected_photo_list", selectedPhotoList);
                mFrag.startActivityForResult(mIntent, 1000);
            }
        });
    }

    @Override
    public int getItemCount() {
        return bucketItemList.size();
    }

    public class BucketItemViewHolder extends RecyclerView.ViewHolder {

        private ImageView imageView;
        private TextView nameView;
        private int id;

        public BucketItemViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.bucket_image);
            nameView = (TextView) itemView.findViewById(R.id.bucket_name);
        }


        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public ImageView getImageView() {
            return imageView;
        }

        public TextView getNameView() {
            return nameView;
        }
    }
}
