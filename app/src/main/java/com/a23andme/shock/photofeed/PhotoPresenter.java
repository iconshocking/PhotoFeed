package com.a23andme.shock.photofeed;

import android.support.annotation.NonNull;

import com.a23andme.shock.photofeed.Network.ApiRequester;
import com.a23andme.shock.photofeed.Network.Response;

public class PhotoPresenter {
    private PhotoView photoView;
    private PhotosModel photosModel;

    public PhotoPresenter(@NonNull PhotoView photoView, @NonNull ApiRequester apiRequester) {
        this.photoView = photoView;
        photosModel = new PhotosModel(this, apiRequester);
    }

    public void onPhotoDataReceived(Response.Data data) {
        photoView.displayPhotoData(data.getData());
    }

    public void likeClicked(Response.Photo photo) {
        photosModel.likeOrUnlikePhoto(photo);
    }
}
