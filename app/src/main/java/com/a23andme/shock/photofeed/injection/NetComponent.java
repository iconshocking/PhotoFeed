package com.a23andme.shock.photofeed.injection;

import com.a23andme.shock.photofeed.model.PhotosModel;
import com.a23andme.shock.photofeed.model.network.ApiService;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules={AppModule.class, NetModule.class})
public interface NetComponent {
    void inject(PhotosModel photosModel);
    void inject(ApiService apiService);
}
