package com.a23andme.shock.photofeed;

import android.support.annotation.NonNull;

import com.a23andme.shock.photofeed.Network.ApiRequester;
import com.a23andme.shock.photofeed.Network.ApiResponseSubscriber;
import com.a23andme.shock.photofeed.Network.Response;

public class PhotosModel implements ApiResponseSubscriber {
    private PhotoPresenter presenter;
    private ApiRequester apiRequester;

    public PhotosModel(@NonNull PhotoPresenter presenter, @NonNull ApiRequester apiRequester) {
        this.presenter = presenter;
        this.apiRequester = apiRequester;
        apiRequester.setSubscriber(this);
        requestPhotoData();
    }

    public void requestPhotoData() {
        apiRequester.getRecentUserPhotoPhotos();
    }

    @Override
    public void onPhotoDataReceived(Response.Data data) {
        presenter.onPhotoDataReceived(data);
    }

    public void likeOrUnlikePhoto(Response.Photo photo) {
        apiRequester.postLike(photo.getMedia_id());
//        apiRequester.removeLike(photo.getMedia_id());
    }
}
