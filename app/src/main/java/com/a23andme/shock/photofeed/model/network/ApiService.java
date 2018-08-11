package com.a23andme.shock.photofeed.model.network;

import android.support.annotation.NonNull;

import com.a23andme.shock.photofeed.App;
import com.a23andme.shock.photofeed.model.network.Response.Data;
import com.a23andme.shock.photofeed.model.network.Response.StatusResponse;

import java.io.IOException;

import javax.inject.Inject;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;

import static com.a23andme.shock.photofeed.model.network.NetworkApi.RESPONSE_CODE_UNAUTHORIZED;

// followed oauth tutorial from https://futurestud.io/tutorials/oauth-2-on-android-with-retrofit

public class ApiService implements ApiRequester {
    private static ApiService networkApiService = new ApiService();

    private NetworkApi networkApi;

    private ApiResponseSubscriber subscriber;

    @Inject
    protected OkHttpClient.Builder httpClient;

    @Inject
    protected Retrofit.Builder retrofitBuilder;

    private ApiService() {
        App.getApp().getNetComponent().inject(this);
    }

    public static ApiService getInstance() {
        return networkApiService;
    }

    public void setupApiService(Class<NetworkApi> apiClass, final String authToken) {
        if (authToken != null && authToken.length() != 0) {
            Interceptor interceptor = new Interceptor() {
                private String mAuthToken = authToken;

                @Override
                public Response intercept(Chain chain) throws IOException {
                    Request original = chain.request();

                    Request.Builder builder = original.newBuilder()
                            .header("Authorization", mAuthToken);

                    Request request = builder.build();
                    return chain.proceed(request);
                }
            };

            if (!httpClient.interceptors().contains(interceptor)) {
                httpClient.addInterceptor(interceptor);
            }

            retrofitBuilder.client(httpClient.build());
        }

        networkApi = retrofitBuilder.build().create(apiClass);
    }

    private NetworkApi getApi() {
        return networkApi;
    }

    public void setSubscriber(ApiResponseSubscriber subscriber) {
        this.subscriber = subscriber;
    }

    @Override
    public void getRecentUserPhotoPhotos() {
        getApi().getRecentUserPhotos().enqueue(new Callback<Data>() {
            @Override
            public void onResponse(@NonNull Call<Data> call, @NonNull retrofit2.Response<Data> response) {
                getNewAuthTokenIfNeeded(response);
                if (subscriber != null && response.body() != null) {
                    subscriber.onPhotoDataReceived(response.body());
                }
            }

            @Override
            public void onFailure(@NonNull Call<Data> call, @NonNull Throwable t) { }
        });
    }

    @Override
    public void postLike(String mediaId) {
        getApi().postLike(mediaId).enqueue(new Callback<StatusResponse>() {
            @Override
            public void onResponse(@NonNull Call<StatusResponse> call,
                                   @NonNull retrofit2.Response<StatusResponse> response) {
                getNewAuthTokenIfNeeded(response);
            }

            @Override
            public void onFailure(@NonNull Call<StatusResponse> call, @NonNull Throwable t) { }
        });
    }

    @Override
    public void removeLike(String mediaId) {
        getApi().removeLike(mediaId).enqueue(new Callback<StatusResponse>() {
            @Override
            public void onResponse(@NonNull Call<StatusResponse> call,
                                   @NonNull retrofit2.Response<StatusResponse> response) {
                getNewAuthTokenIfNeeded(response);
            }

            @Override
            public void onFailure(@NonNull Call<StatusResponse> call, @NonNull Throwable t) { }
        });
    }

    private <T> void getNewAuthTokenIfNeeded(retrofit2.Response<T> response) {
        if (response.code() == RESPONSE_CODE_UNAUTHORIZED) {
            if (subscriber != null) {
                subscriber.newAuthTokenNeeded();
            }
        }
    }
}
