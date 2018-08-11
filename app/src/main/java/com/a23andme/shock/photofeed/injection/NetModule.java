package com.a23andme.shock.photofeed.injection;

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.a23andme.shock.photofeed.model.LocalStorage;
import com.google.gson.Gson;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.a23andme.shock.photofeed.model.network.NetworkApi.API_BASE_URL;

@Module
public class NetModule {

    public NetModule() { }

    @Provides
    @Singleton
    SharedPreferences providesSharedPreferences(Application application) {
        return PreferenceManager.getDefaultSharedPreferences(application);
    }

    @Provides
    @Singleton
    Gson provideGson() {
        return new Gson();
    }

    @Provides
    @Singleton
    OkHttpClient.Builder provideOkHttpClientBuilder() {
        return new OkHttpClient.Builder();
    }

    // followed tutorial from https://stackoverflow.com/questions/46401184/wrapping-sharedpreferences-with-dagger-2-and-rxjava2

    @Provides
    @Singleton
    public LocalStorage provideLocalStorage(final SharedPreferences sharedPreferences) {
        return new LocalStorage() {
            @Override
            public String getStringValueForKey(String key) {
                return sharedPreferences.getString(key, null);
            }

            @Override
            public void setStringValueForKey(String key, String value) {
                sharedPreferences.edit().putString(key, value).apply();
            }
        };
    }

    @Provides
    @Singleton
    Retrofit.Builder provideRetrofitBuilder(Gson gson) {
        return new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(gson))
                .baseUrl(API_BASE_URL);
    }
}
