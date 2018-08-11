package com.a23andme.shock.photofeed.view;

import com.a23andme.shock.photofeed.model.network.Response;

import java.util.List;

public interface PhotoView {
    void displayLogin(boolean logoutFirst);
    void likeChangedForPhoto(Response.Photo photo, boolean newValue);
    void displayPhotoDetailView(Response.Photo photo);
    void displayPhotosData(List<Response.Photo> photos);
}
