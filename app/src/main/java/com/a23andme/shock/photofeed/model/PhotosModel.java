package com.a23andme.shock.photofeed.model;

import android.support.annotation.NonNull;

import com.a23andme.shock.photofeed.model.network.ApiRequester;
import com.a23andme.shock.photofeed.model.network.ApiResponseSubscriber;
import com.a23andme.shock.photofeed.model.network.NetworkApi;
import com.a23andme.shock.photofeed.model.network.NetworkService;
import com.a23andme.shock.photofeed.model.network.Response;
import com.a23andme.shock.photofeed.presenter.PhotoPresenter;

import java.util.List;

import static com.a23andme.shock.photofeed.view.FeedActivity.AUTH_TOKEN_EXTRA;

public class PhotosModel implements ApiResponseSubscriber {
    private PhotoPresenter presenter;
    private ApiRequester apiRequester;
    private SharedPreferencesWrapper preferencesWrapper;

    private List<Response.Photo> photos;

    public PhotosModel(@NonNull PhotoPresenter presenter, @NonNull SharedPreferencesWrapper preferencesWrapper) {
        this.presenter = presenter;
        this.preferencesWrapper = preferencesWrapper;
        String cachedAuthToken = preferencesWrapper.getStringValueForKey(AUTH_TOKEN_EXTRA);
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
        apiRequester = NetworkService.createApiService(NetworkApi.class, authToken);
        apiRequester.setSubscriber(this);
        if (photos == null) {
            requestPhotoData();
        }
    }

    public void newAuthTokenReceived(String authToken) {
        preferencesWrapper.setStringValueForKey(AUTH_TOKEN_EXTRA, authToken);
        setupApiService(authToken);
    }

    public void clearAuthToken() {
        preferencesWrapper.setStringValueForKey(AUTH_TOKEN_EXTRA, null);
    }
}
