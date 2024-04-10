package com.example.clarity.model;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.lifecycle.MutableLiveData;

import java.util.HashSet;
import java.util.Set;

public class PreferenceUtils {
    /*
     * Singleton class for managing user preferences (persistent storage)
     * -----------------------------------------------------------------
     * Fetch instance and use it to manage all fetches/updates to user preferences
     */


    // Static attributes (define your keys here):
    private static final String PREF_NAME = "ClarityAppPreferences";
    private static final String KEY_CAL_POST_IDS = "calendarPostIds";
    private static final String SESSION_TOKEN = "sessionToken";
    private static final String KEY_THEME = "appTheme";
    private static PreferenceUtils instance;

    // Instance attributes:
    private final SharedPreferences sharedPreferences;
    private final MutableLiveData<Set<Integer>> calendarPostIdsLiveData;
    private final Set<Integer> calendarPostIds; // Set prevents duplicates
    private String sessionToken;

    /*
     * Methods for events (posts) saved to Calendar
     * --------------------------------------------
     * Events (posts) that users add to their calendar are saved locally in sharedPrefs
     * as a String set with the key "calendarPostIds" (KEY_CAL_POST_IDS)
     * Changes made by addToCalendar and removeFromCalendar must be committed with commitCalendarUpdates
     * in order to save those changes to the sharedPrefs.
     * Note: when these methods are executed from a fragment, pass in getActivity() as context
     */

    public static PreferenceUtils getInstance(Context context) {
        if (instance == null) {
            instance = new PreferenceUtils(context.getApplicationContext());
        }
        return instance;
    }

    private PreferenceUtils(Context context) {
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        calendarPostIdsLiveData = new MutableLiveData<>(new HashSet<>());

        // Load in posts (events) saved to calendar from local storage (user prefs)
        Set<String> stringSet = sharedPreferences.getStringSet(KEY_CAL_POST_IDS, new HashSet<>());

        calendarPostIds = calendarPostIdsLiveData.getValue();
        for (String postIdString: stringSet) {
            assert calendarPostIds != null;
            calendarPostIds.add(Integer.parseInt(postIdString));
        }
        calendarPostIdsLiveData.setValue(calendarPostIds); // Simply to trigger observer(s) for calendarPostIdsLiveData

        // Load in login session token
        sessionToken = sharedPreferences.getString(SESSION_TOKEN, ""); // empty string returned if no session token found
    }

    // CALENDAR METHODS //
    // Note: run commitCalendarUpdates() to commit changes made by addToCalendar, removeFromCalendar and resetCalendar.
    public void addToCalendar(int postId) {
        calendarPostIds.add(postId);
    }

    public void removeFromCalendar(int postId) {
        calendarPostIds.remove(postId);
    }

    public void resetCalendar(){
        // Resets userPrefs (you will still need to commit changes after)
        calendarPostIds.clear();
    }

    public boolean inCalendar(int postId) {
        return calendarPostIds.contains(postId);
    }

    public void commitCalendarUpdates() {
        // Commits changes made by addToCalendar and removeFromCalendar to local storage (sharedPreferences file)
        // and triggers all observers for calendarPostIdsLiveData (i.e. there is an update to saved ids)

        Set<String> stringSet = new HashSet<>();
        for (int postId: calendarPostIds) {
            stringSet.add(String.valueOf(postId));
        }

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putStringSet(KEY_CAL_POST_IDS, stringSet);
        editor.apply();
        calendarPostIdsLiveData.setValue(calendarPostIds); // Trigger observers
    }

    public Set<Integer> getCalendarPostIds() {
        return calendarPostIds;
    }
    public MutableLiveData<Set<Integer>> getCalendarLiveData() { return calendarPostIdsLiveData; }


    // SESSION TOKEN METHODS //
    public String getSessionToken() {return sessionToken;}
    private void saveSessionToken(String sessionToken) {
        this.sessionToken = sessionToken;

        // Save to local storage
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(SESSION_TOKEN, sessionToken);
        editor.apply();
    }


}
