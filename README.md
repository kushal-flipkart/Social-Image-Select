# Social-Image-Select
Pick images from external storage and social profiles made easy!

How to use :
---

        Intent intent = new Intent(this, PhotoAlbumActivity.class);
        intent.putExtra("following_class", "xyz.kushal.imageselectgallery.ResultActivity");
        intent.putExtra("photo_type", 1);
        startActivity(intent);
