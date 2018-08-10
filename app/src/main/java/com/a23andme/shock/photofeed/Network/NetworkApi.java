package com.a23andme.shock.photofeed.Network;

import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface NetworkApi {
    public static final String LOGIN_URL = "https://insta23prod.auth.us-west-2.amazoncognito.com/" +
            "login?response_type=token&client_id=5khm2intordkd1jjr7rbborbfj&redirect_uri=https://www.23andme.com/";
    public static final String LOGOUT_URL = "https://insta23prod.auth.us-west-2.amazoncognito.com/" +
            "logout?response_type=token&client_id=5khm2intordkd1jjr7rbborbfj&redirect_uri=https://www.23andme.com/";
    public static final String POST_METHOD = "POST";
    public static final String ID_TOKEN_PREFIX = "id_token=";
    public static final String ACCESS_TOKEN_PREFIX = "access_token=";
    public static final String AMPERSAND = "&";

    public static final String API_BASE_URL = "https://kqlpe1bymk.execute-api.us-west-2.amazonaws.com/Prod/";

    @POST("media/{media_id}/likes")
    Call<Response.StatusResponse> postLike(@Path("media_id") String id);

    @DELETE("media/{media_id}/likes")
    Call<Response.StatusResponse> removeLike(@Path("media_id") String id);

    @GET("users/self/media/recent")
    Call<Response.Data> getRecentUserPhotos();
}
