package com.a23andme.shock.photofeed.Network;

public interface ApiResponseSubscriber {
    void onPhotoDataReceived(Response.Data data);
}
