package com.a23andme.shock.photofeed;

import android.app.Application;

import com.a23andme.shock.photofeed.injection.AppModule;
import com.a23andme.shock.photofeed.injection.DaggerNetComponent;
import com.a23andme.shock.photofeed.injection.NetComponent;
import com.a23andme.shock.photofeed.injection.NetModule;

public class App extends Application {

    private static App app;
    private NetComponent mNetComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        app = this;
        mNetComponent = DaggerNetComponent.builder()
                .appModule(new AppModule(this))
                .netModule(new NetModule())
                .build();
    }

    public NetComponent getNetComponent() {
        return mNetComponent;
    }

    public static App getApp() {
        return app;
    }
}