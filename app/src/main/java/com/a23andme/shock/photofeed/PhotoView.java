package com.a23andme.shock.photofeed;

import com.a23andme.shock.photofeed.Network.Response;

import java.util.List;

public interface PhotoView {
    void likeClicked(Response.Photo photo);
    void displayPhotoData(List<Response.Photo> photos);
}
