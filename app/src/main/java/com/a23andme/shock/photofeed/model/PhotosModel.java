package com.a23andme.shock.photofeed.model;

import android.support.annotation.NonNull;

import com.a23andme.shock.photofeed.App;
import com.a23andme.shock.photofeed.model.network.ApiRequester;
import com.a23andme.shock.photofeed.model.network.ApiResponseSubscriber;
import com.a23andme.shock.photofeed.model.network.NetworkApi;
import com.a23andme.shock.photofeed.model.network.Response;
import com.a23andme.shock.photofeed.presenter.PhotoPresenter;

import java.util.List;

import javax.inject.Inject;

import static com.a23andme.shock.photofeed.view.FeedActivity.AUTH_TOKEN_EXTRA;

public class PhotosModel implements ApiResponseSubscriber {
    private PhotoPresenter presenter;
    private ApiRequester apiRequester;

    @Inject
    protected LocalStorage localStorage;

    private List<Response.Photo> photos;

    public PhotosModel(@NonNull PhotoPresenter presenter, @NonNull ApiRequester apiRequester) {
        this.presenter = presenter;
        this.apiRequester = apiRequester;

        App.getApp().getNetComponent().inject(this);

        String cachedAuthToken = localStorage.getStringValueForKey(AUTH_TOKEN_EXTRA);
        if (cachedAuthToken != null && cachedAuthToken.length() > 0) {
            setupApiService(cachedAuthToken);
        } else {
            newAuthTokenNeeded();
        }
    }

    public void requestPhotoData() {
        apiRequester.getRecentUserPhotoPhotos();
    }

    @Override
    public void onPhotoDataReceived(Response.Data data) {
        photos = data.getData();
        presenter.onPhotosReceived(photos);
    }

    @Override
    public void newAuthTokenNeeded() {
        presenter.needNewAuthToken();
    }

    public boolean likeOrUnlikePhoto(Response.Photo photo) {
        boolean newLikedValue = photo.getLikes().changeIfLikedOrNot();
        if (newLikedValue) {
            apiRequester.postLike(photo.getMedia_id());
        } else {
            apiRequester.removeLike(photo.getMedia_id());
        }
        return newLikedValue;
    }

    private void setupApiService(String authToken) {
        apiRequester.setupApiService(NetworkApi.class, authToken);
        apiRequester.setSubscriber(this);
        if (photos == null) {
            requestPhotoData();
        }
    }

    public void newAuthTokenReceived(String authToken) {
        localStorage.setStringValueForKey(AUTH_TOKEN_EXTRA, authToken);
        setupApiService(authToken);
    }

    public void clearAuthToken() {
        localStorage.setStringValueForKey(AUTH_TOKEN_EXTRA, null);
    }
}
