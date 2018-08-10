package com.a23andme.shock.photofeed.Network;

public interface ApiRequester {
    void getRecentUserPhotoPhotos();
    void postLike(String mediaId);
    void removeLike(String mediaId);
    void setSubscriber(ApiResponseSubscriber subscriber);
}
