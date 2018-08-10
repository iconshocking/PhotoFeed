package com.a23andme.shock.photofeed;

import com.a23andme.shock.photofeed.Network.Response;

import java.util.List;

public interface PhotoView {
    void displayLogin();
    void likeClicked(Response.Photo photo);
    void displayPhotosData(List<Response.Photo> photos);
}
