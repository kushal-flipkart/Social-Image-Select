package xyz.kushal.adapters;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.toolbox.NetworkImageView;

import java.util.ArrayList;

import ly.kite.instagramphotopicker.InstagramPhoto;
import xyz.kushal.socialimageselect.R;
import xyz.kushal.utils.AppController;

/**
 * Created by Kush on 9/28/2015.
 */
public class InstagramAdapter extends RecyclerView.Adapter {
    private ArrayList<InstagramPhoto> photoList = new ArrayList<>();
    private Activity activity;

    public InstagramAdapter(ArrayList<InstagramPhoto> photoList, Activity activity) {
        this.photoList = photoList;
        this.activity = activity;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_image_instagram, parent, false);

        return new InstagramViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((InstagramViewHolder) holder).getImageView().setImageUrl(String.valueOf(photoList.get(position).getThumbnailURL()), AppController.getInstance().getImageLoader());
    }

    @Override
    public int getItemCount() {
        return photoList.size();
    }

    public class InstagramViewHolder extends RecyclerView.ViewHolder {

        private NetworkImageView imageView;
        private int id;

        public InstagramViewHolder(View itemView) {
            super(itemView);
            imageView = (NetworkImageView) itemView.findViewById(R.id.bucket_image);
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public NetworkImageView getImageView() {
            return imageView;
        }
    }
}
