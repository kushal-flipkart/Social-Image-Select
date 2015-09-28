package xyz.kushal.utils;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by kushal on 13/08/15.
 * Model for photo object
 */


public class MediaStorePhoto implements Parcelable {

    public static final Creator CREATOR = new Creator() {
        public MediaStorePhoto createFromParcel(Parcel in) {
            return new MediaStorePhoto(in);
        }

        public MediaStorePhoto[] newArray(int size) {
            return new MediaStorePhoto[size];
        }
    };
    private String id;
    private String displayName;
    private String bucket;
    private String date;
    private String size;
    private String status;
    private String dataUri;
    private String bucketId;

    public MediaStorePhoto(String id, String displayName, String bucket, String date, String size, String status, String dataUri, String bucketId) {
        this.id = id;
        this.displayName = displayName;
        this.bucket = bucket;
        this.date = date;
        this.size = size;
        this.status = status;
        this.dataUri = dataUri;
        this.bucketId = bucketId;
    }

    public MediaStorePhoto(Parcel in) {
        String[] data = new String[8];

        in.readStringArray(data);
        this.id = data[0];
        this.displayName = data[1];
        this.bucket = data[2];
        this.date = data[3];
        this.size = data[4];
        this.status = data[5];
        this.dataUri = data[6];
        this.bucketId = data[7];
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringArray(new String[]{this.id,
                this.displayName,
                this.bucket,
                this.date,
                this.size,
                this.status,
                this.dataUri,
                this.bucketId});
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getBucket() {
        return bucket;
    }

    public void setBucket(String bucket) {
        this.bucket = bucket;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDataUri() {
        return dataUri;
    }

    public void setDataUri(String dataUri) {
        this.dataUri = dataUri;
    }

    public String getBucketId() {
        return bucketId;
    }

    public void setBucketId(String bucketId) {
        this.bucketId = bucketId;
    }
}
