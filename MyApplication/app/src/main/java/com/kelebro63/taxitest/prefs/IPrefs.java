package com.kelebro63.taxitest.prefs;

/**
 * Created by Bistrov Alexey on 09.09.2016.
 */
public interface IPrefs {

    String getSessionKey();

    void setSessionKey(String sessionKey);

    boolean isLoggedIn();

}
