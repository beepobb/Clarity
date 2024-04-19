package com.example.clarity.model;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class PreferenceUtils {
    /**
     * Singleton class for managing user preferences (persistent local storage)
     * i.e. controller in the MVC design architecture
     * -----------------------------------------------------------------
     * Fetch instance and use it to manage all fetches/updates to user preferences (locally stored)
     * - Events saved to calendar
     * - Session token
     */


    // Static attributes (define your keys here):
    private static final String PREF_NAME = "ClarityAppPreferences";
    private static final String KEY_CAL_POST_IDS = "calendarPostIds";
    private static final String SESSION_TOKEN = "sessionToken";
    private static PreferenceUtils instance;

    // Instance attributes:
    private final SharedPreferences sharedPreferences;
    private final Set<Integer> calendarPostIds; // Set prevents duplicates
    private String sessionToken;

    /**
     * Constructor for singleton class
     * @param context Context object (e.g. Activity instance)
     * @return PreferenceUtils instance
     */
    public static PreferenceUtils getInstance(Context context) {
        if (instance == null) {
            instance = new PreferenceUtils(context.getApplicationContext());
        }
        return instance;
    }

    private PreferenceUtils(Context context) {
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        calendarPostIds = new HashSet<>();

        // Load in posts (events) saved to calendar from local storage (user prefs)
        Set<String> stringSet = sharedPreferences.getStringSet(KEY_CAL_POST_IDS, new HashSet<>());
        for (String idString : stringSet) {
            calendarPostIds.add(Integer.parseInt(idString));
        }

        // Load in login session token
        sessionToken = sharedPreferences.getString(SESSION_TOKEN, ""); // empty string returned if no session token found
    }

    // CALENDAR METHODS //
    /*
     * Methods for events (posts) saved to Calendar
     * --------------------------------------------
     * The post ids of events that users add to their calendar are saved locally in sharedPrefs
     * as a String set with the key "calendarPostIds" (KEY_CAL_POST_IDS)
     * Changes made by addToCalendar, removeFromCalendar and resetCalendar must be committed with commitCalendarUpdates
     * in order to save those changes to the sharedPrefs (local storage).
     */

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

        Set<String> stringSet = new HashSet<>();
        for (int postId: calendarPostIds) {
            stringSet.add(String.valueOf(postId));
        }

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putStringSet(KEY_CAL_POST_IDS, stringSet);
        editor.apply();
    }

    public Set<Integer> getCalendarPostIds() {
        return calendarPostIds;
    }
    public ArrayList<Integer> getCalendarPostIdsArrayList() {
        return new ArrayList<>(calendarPostIds);
    }


    // SESSION TOKEN METHODS //

    /**
     * Fetches session token from local storage
     * @return serialized sessionToken string
     */
    public String getSessionToken() {return sessionToken;}

    /**
     * Saves session token
     * @param sessionToken Serialized string
     */
    public void saveSessionToken(String sessionToken) {
        this.sessionToken = sessionToken;

        // Save to local storage
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(SESSION_TOKEN, sessionToken);
        editor.apply();
    }

    /**
     * Clears session token
     */
    public void clearSessionToken() {
        saveSessionToken(""); // set empty session token
    }


}
