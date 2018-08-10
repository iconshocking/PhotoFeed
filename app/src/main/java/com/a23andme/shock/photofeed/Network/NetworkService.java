package com.a23andme.shock.photofeed.Network;

import android.support.annotation.NonNull;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.a23andme.shock.photofeed.Network.NetworkApi.API_BASE_URL;
import static com.a23andme.shock.photofeed.Network.NetworkApi.RESPONSE_CODE_UNAUTHORIZED;

import com.a23andme.shock.photofeed.Network.Response.*;

// followed oauth tutorial from https://futurestud.io/tutorials/oauth-2-on-android-with-retrofit

public class NetworkService implements ApiRequester {
    private static OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

    private static NetworkService networkService = new NetworkService();

    private static NetworkApi networkApi;

    private ApiResponseSubscriber subscriber;

    private NetworkService() {}

    public static NetworkService getInstance() {
        return networkService;
    }

    public static NetworkService createApiService(Class<NetworkApi> serviceClass, final String authToken) {
        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(API_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create());

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

            builder.client(httpClient.build());
        }

        networkApi = builder.build().create(serviceClass);

        return getInstance();
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
