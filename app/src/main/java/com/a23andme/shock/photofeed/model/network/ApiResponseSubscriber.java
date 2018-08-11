package com.a23andme.shock.photofeed.model.network;

public interface ApiResponseSubscriber {
    void onPhotoDataReceived(Response.Data data);
    void newAuthTokenNeeded();
}
