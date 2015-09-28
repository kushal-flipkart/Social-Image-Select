package xyz.kushal.utils;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by Kush on 9/10/2015.
 * Helper methods for accessing media store
 */

public class MediaStoreHelperMethods {

    public static ArrayList<MediaStorePhoto> getAllPhotosFromExternalStorage(ContentResolver mContentResolver) {

        MediaStorePhoto photo;
        ArrayList<MediaStorePhoto> photoList = new ArrayList<>();

        // which image properties are we querying
        String[] projection = new String[]{
                MediaStore.Images.Media.BUCKET_ID,
                MediaStore.Images.Media.BUCKET_DISPLAY_NAME,
                MediaStore.Images.Media._ID,
                MediaStore.Images.Media.DATE_TAKEN,
                MediaStore.Images.Media.SIZE,
                MediaStore.Images.Media.DISPLAY_NAME,
                MediaStore.Images.Media.DATA
        };

        // content:// style URI for the "primary" external storage volume
        Uri images = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;

        // Make the query.
        Cursor cur = mContentResolver.query(images,
                projection, // Which columns to return
                null,       // Which rows to return (all rows)
                null,       // Selection arguments (none)
                null        // Ordering
        );

        Log.i("ListingImages", " query count=" + cur.getCount());

        if (cur.moveToFirst()) {
            String bucketId;
            long id;
            long size;
            String bucket;
            String date;
            String name;
            String dataUri;

            int bucketIdColumn = cur.getColumnIndex(
                    MediaStore.Images.Media.BUCKET_ID);

            int idColumn = cur.getColumnIndex(
                    MediaStore.Images.Media._ID);

            int bucketColumn = cur.getColumnIndex(
                    MediaStore.Images.Media.BUCKET_DISPLAY_NAME);

            int dateColumn = cur.getColumnIndex(
                    MediaStore.Images.Media.DATE_TAKEN);

            int sizeColumn = cur.getColumnIndex(
                    MediaStore.Images.Media.SIZE);

            int nameColumn = cur.getColumnIndex(
                    MediaStore.Images.Media.DISPLAY_NAME);

            int dataColumn = cur.getColumnIndex(
                    MediaStore.Images.Media.DATA);
            do {
                // Get the field values
                bucketId = cur.getString(bucketIdColumn);
                bucket = cur.getString(bucketColumn);
                date = cur.getString(dateColumn);
                size = cur.getLong(sizeColumn);
                id = cur.getLong(idColumn);
                name = cur.getString(nameColumn);
                dataUri = cur.getString(dataColumn);

                // Store photo in Photo object
                photo = new MediaStorePhoto(String.valueOf(id), name,
                        bucket, date, String.valueOf(size), "null", dataUri, bucketId);

                // Add photo to photo list
                photoList.add(photo);
            } while (cur.moveToNext());

        }
        cur.close();
        return photoList;
    }

    public static ArrayList<MediaStorePhoto> getBucketCoverItems(ContentResolver mContentResolver) {
        MediaStorePhoto photo;
        ArrayList<MediaStorePhoto> bucketCoverItemList = new ArrayList<>();

        // which image properties are we querying
        String[] projection = new String[]{

                MediaStore.Images.Media.BUCKET_ID,
                MediaStore.Images.Media.BUCKET_DISPLAY_NAME,
                MediaStore.Images.Media.DATE_TAKEN,
                MediaStore.Images.Media._ID,
                MediaStore.Images.Media.SIZE,
                MediaStore.Images.Media.DISPLAY_NAME,
                MediaStore.Images.Media.DATA
        };

        String BUCKET_GROUP_BY =
                "1) GROUP BY 1,(2";
        String BUCKET_ORDER_BY = "MAX(datetaken) DESC";

        // content:// style URI for the "primary" external storage volume
        Uri images = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;

        // Make the query.
        Cursor cur = mContentResolver.query(images,
                projection, // Which columns to return
                BUCKET_GROUP_BY,       // Which rows to return (all rows)
                null,       // Selection arguments (none)
                BUCKET_ORDER_BY        // Ordering
        );

        Log.i("ListingImages", " query count=" + cur.getCount());

        if (cur.moveToFirst()) {
            String bucketId;
            long id;
            long size;
            String bucket;
            String date;
            String name;
            String dataUri;

            int bucketIdColumn = cur.getColumnIndex(
                    MediaStore.Images.Media.BUCKET_ID);

            int idColumn = cur.getColumnIndex(
                    MediaStore.Images.Media._ID);

            int bucketColumn = cur.getColumnIndex(
                    MediaStore.Images.Media.BUCKET_DISPLAY_NAME);

            int dateColumn = cur.getColumnIndex(
                    MediaStore.Images.Media.DATE_TAKEN);

            int sizeColumn = cur.getColumnIndex(
                    MediaStore.Images.Media.SIZE);

            int nameColumn = cur.getColumnIndex(
                    MediaStore.Images.Media.DISPLAY_NAME);

            int dataColumn = cur.getColumnIndex(
                    MediaStore.Images.Media.DATA);

            do {
                // Get the field values
                bucketId = cur.getString(bucketIdColumn);
                bucket = cur.getString(bucketColumn);
                date = cur.getString(dateColumn);
                size = cur.getLong(sizeColumn);
                id = cur.getLong(idColumn);
                name = cur.getString(nameColumn);
                dataUri = cur.getString(dataColumn);

                // Store photo in Photo object
                photo = new MediaStorePhoto(String.valueOf(id), name,
                        bucket, date, String.valueOf(size), "null", dataUri, bucketId);

                // Do something with the values.
                bucketCoverItemList.add(photo);
            } while (cur.moveToNext());
        }
        cur.close();
        return bucketCoverItemList;
    }

    public static ArrayList<MediaStorePhoto> getAllPhotosInBucket(String bucketID, ContentResolver mContentResolver) {
        MediaStorePhoto photo;
        ArrayList<MediaStorePhoto> allPhotosInBucketList = new ArrayList<>();

        // which image properties are we querying
        String[] projection = new String[]{

                MediaStore.Images.Media.BUCKET_ID,
                MediaStore.Images.Media.BUCKET_DISPLAY_NAME,
                MediaStore.Images.Media.DATE_TAKEN,
                MediaStore.Images.Media._ID,
                MediaStore.Images.Media.SIZE,
                MediaStore.Images.Media.DISPLAY_NAME,
                MediaStore.Images.Media.DATA
        };

        String BUCKET_GROUP_BY = MediaStore.Images.Media.BUCKET_ID + " LIKE ?";

        // content:// style URI for the "primary" external storage volume
        Uri images = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;

        // Make the query.
        Cursor cur = mContentResolver.query(images,
                projection, // Which columns to return
                BUCKET_GROUP_BY,       // Which rows to return (all rows)
                new String[]{bucketID},       // Selection arguments (none)
                null        // Ordering
        );

        Log.i("ListingImages", " query count=" + cur.getCount());

        if (cur.moveToFirst()) {
            String bucketId;
            long id;
            long size;
            String bucket;
            String date;
            String name;
            String dataUri;

            int bucketIdColumn = cur.getColumnIndex(
                    MediaStore.Images.Media.BUCKET_ID);

            int idColumn = cur.getColumnIndex(
                    MediaStore.Images.Media._ID);

            int bucketColumn = cur.getColumnIndex(
                    MediaStore.Images.Media.BUCKET_DISPLAY_NAME);

            int dateColumn = cur.getColumnIndex(
                    MediaStore.Images.Media.DATE_TAKEN);

            int sizeColumn = cur.getColumnIndex(
                    MediaStore.Images.Media.SIZE);

            int nameColumn = cur.getColumnIndex(
                    MediaStore.Images.Media.DISPLAY_NAME);

            int dataColumn = cur.getColumnIndex(
                    MediaStore.Images.Media.DATA);

            do {
                // Get the field values
                bucketId = cur.getString(bucketIdColumn);
                bucket = cur.getString(bucketColumn);
                date = cur.getString(dateColumn);
                size = cur.getLong(sizeColumn);
                id = cur.getLong(idColumn);
                name = cur.getString(nameColumn);
                dataUri = cur.getString(dataColumn);

                // Store photo in Photo object
                photo = new MediaStorePhoto(String.valueOf(id), name,
                        bucket, date, String.valueOf(size), "null", dataUri, bucketId);

                // Do something with the values.
                allPhotosInBucketList.add(photo);
            } while (cur.moveToNext());
        }
        cur.close();
        return allPhotosInBucketList;
    }
}
