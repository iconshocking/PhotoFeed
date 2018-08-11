package com.a23andme.shock.photofeed.presenter;

import android.support.annotation.NonNull;

import com.a23andme.shock.photofeed.model.PhotosModel;
import com.a23andme.shock.photofeed.model.SharedPreferencesWrapper;
import com.a23andme.shock.photofeed.model.network.Response;
import com.a23andme.shock.photofeed.view.PhotoView;

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

    public void clearAuthToken() {
        photosModel.clearAuthToken();
    }

    public void needNewAuthToken() {
        photoView.displayLogin(false);
    }

    public void onPhotosReceived(List<Response.Photo> photos) {
        photoView.displayPhotosData(photos);
    }

    public boolean likeClicked(Response.Photo photo) {
        boolean newLikedValue = photosModel.likeOrUnlikePhoto(photo);
        photoView.likeChangedForPhoto(photo, newLikedValue);
        return newLikedValue;
    }

    public void photoClicked(Response.Photo photo) {
        photoView.displayPhotoDetailView(photo);
    }
}
