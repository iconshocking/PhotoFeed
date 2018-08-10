package com.a23andme.shock.photofeed;

import com.a23andme.shock.photofeed.Network.Response;

import java.util.List;

public interface PhotoView {
    void displayLogin(boolean logoutFirst);
    void likeChangedForPhoto(Response.Photo photo, boolean newValue);
    void displayPhotosData(List<Response.Photo> photos);
}
