package com.a23andme.shock.photofeed;

public interface SharedPreferencesWrapper {
    String getStringValueForKey(String key);
    void setStringValueForKey(String key, String value);
}
