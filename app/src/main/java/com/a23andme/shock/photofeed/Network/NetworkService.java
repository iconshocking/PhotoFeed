package com.a23andme.shock.photofeed.Network;

import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

import static com.a23andme.shock.photofeed.Network.NetworkApi.API_BASE_URL;

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

    public static void createApiService(Class<NetworkApi> serviceClass, final String authToken) {
        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(API_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create());

        if (!TextUtils.isEmpty(authToken)) {
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

//        createTestApiService(serviceClass, authToken);
    }

//    private static NetworkApi testApi;
//
//    private static void createTestApiService(Class<NetworkApi> serviceClass, final String authToken) {
//        Retrofit.Builder builder = new Retrofit.Builder()
//                .baseUrl(API_BASE_URL)
//                .addConverterFactory(GsonConverterFactory.create());
//
//        if (!TextUtils.isEmpty(authToken)) {
//            Interceptor interceptor = new Interceptor() {
//                private String mAuthToken = authToken;
//
//                @Override
//                public Response intercept(Chain chain) throws IOException {
//                    Request original = chain.request();
//
//                    Request.Builder builder = original.newBuilder()
//                            .header("Authorization", mAuthToken);
//
//                    Request request = builder.build();
//                    return chain.proceed(request);
//                }
//            };
//
//            if (!httpClient.interceptors().contains(interceptor)) {
//                httpClient.addInterceptor(interceptor);
//                builder.client(httpClient.build());
//            }
//        }
//
//        testApi = builder.build().create(serviceClass);
//    }
//
//    private NetworkApi getTestApi() {
//        return testApi;
//    }

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
                if (subscriber != null) {
                    subscriber.onPhotoDataReceived(response.body());
                }
            }

            @Override
            public void onFailure(@NonNull Call<Data> call, @NonNull Throwable t) {}
        });
    }

    @Override
    public void postLike(String mediaId) {
        getApi().postLike(mediaId).enqueue(new Callback<StatusResponse>() {
            @Override
            public void onResponse(@NonNull Call<StatusResponse> call,
                                   @NonNull retrofit2.Response<StatusResponse> response) { }

            @Override
            public void onFailure(@NonNull Call<StatusResponse> call, @NonNull Throwable t) { }
        });
    }

    @Override
    public void removeLike(String mediaId) {
        getApi().removeLike(mediaId).enqueue(new Callback<StatusResponse>() {
            @Override
            public void onResponse(@NonNull Call<StatusResponse> call,
                                   @NonNull retrofit2.Response<StatusResponse> response) {}

            @Override
            public void onFailure(@NonNull Call<StatusResponse> call, @NonNull Throwable t) { }
        });
    }
}
