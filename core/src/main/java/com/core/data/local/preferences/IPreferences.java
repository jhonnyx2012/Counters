package com.core.data.local.preferences;

/**
 * Created by jhonnybarrios on 10/23/17.
 */

public interface IPreferences {
    String getName();
    void clear();
    void save();
    void save(Enum key, int value);
    void save(Enum key, String value);
    void save(Enum key, boolean value);
    int getInt(Enum key);
    String getString(Enum key);
    boolean getBool(Enum key);
}