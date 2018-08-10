package com.a23andme.shock.photofeed;

import android.support.annotation.NonNull;

import com.a23andme.shock.photofeed.Network.Response;

import java.util.List;

public class PhotoPresenter {
    private PhotoView photoView;
    private PhotosModel photosModel;

    public PhotoPresenter(@NonNull PhotoView photoView, @NonNull SharedPreferencesWrapper preferencesWrapper) {
        this.photoView = photoView;
        photosModel = new PhotosModel(this, preferencesWrapper);
    }

    public void newAuthTokenReceived(String authToken) {
        if (authToken != null && authToken.length() != 0) {
            photosModel.newAuthTokenReceived(authToken);
        }
    }

    public void needNewAuthToken() {
        photoView.displayLogin();
    }

    public void onPhotosReceived(List<Response.Photo> photos) {
        photoView.displayPhotosData(photos);
    }

    public void likeClicked(Response.Photo photo) {
        photosModel.likeOrUnlikePhoto(photo);
    }
}
