package com.a23andme.shock.photofeed.Network;

import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface NetworkApi {
    public static final String CLIENT_ID = "5khm2intordkd1jjr7rbborbfj";
    public static final String CLIENT_ID_PREFIX = "client_id";
    public static final String LOGIN_AND_LOGOUT_REDIRECT_URI = "https://www.23andme.com/";
    public static final String REDIRECT_URI_PREFIX = "redirect_uri";
    public static final String ID_TOKEN_PREFIX = "#id_token";
    public static final String AMPERSAND = "&";
    public static final String EQUALS = "=";
    public static final String POST_METHOD = "POST";

    public static final String LOGIN_URL =
            "https://insta23prod.auth.us-west-2.amazoncognito.com/login?response_type=token" +
                    AMPERSAND + CLIENT_ID_PREFIX + EQUALS + CLIENT_ID +
                    AMPERSAND + REDIRECT_URI_PREFIX + EQUALS + LOGIN_AND_LOGOUT_REDIRECT_URI;

    public static final String LOGOUT_URL =
            "https://insta23prod.auth.us-west-2.amazoncognito.com/logout?response_type=token" +
                    AMPERSAND + CLIENT_ID_PREFIX + EQUALS + CLIENT_ID +
                    AMPERSAND + REDIRECT_URI_PREFIX + EQUALS + LOGIN_AND_LOGOUT_REDIRECT_URI;

    public static final String API_BASE_URL = "https://kqlpe1bymk.execute-api.us-west-2.amazonaws.com/Prod/";

    @POST("media/{media_id}/likes")
    Call<Response.StatusResponse> postLike(@Path("media_id") String id);

    @DELETE("media/{media_id}/likes")
    Call<Response.StatusResponse> removeLike(@Path("media_id") String id);

    @GET("users/self/media/recent")
    Call<Response.Data> getRecentUserPhotos();
}
