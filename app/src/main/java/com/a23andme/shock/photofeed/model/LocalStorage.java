package com.a23andme.shock.photofeed.model;

public interface LocalStorage {
    String getStringValueForKey(String key);
    void setStringValueForKey(String key, String value);
}
