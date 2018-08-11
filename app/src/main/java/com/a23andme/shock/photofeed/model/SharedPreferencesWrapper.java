package com.a23andme.shock.photofeed.model;

public interface SharedPreferencesWrapper {
    String getStringValueForKey(String key);
    void setStringValueForKey(String key, String value);
}
