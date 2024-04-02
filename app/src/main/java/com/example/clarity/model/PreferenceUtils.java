package com.example.clarity.model;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashSet;
import java.util.Set;

public class PreferenceUtils {
    /*
     * Class for managing user preferences (persistent storage)
     * --------------------------------------------------------
     * Designed to be instantiated (non-static) for efficiency purposes
     * Instantiate a single instance and use it to manage all fetches/updates to user preferences
     */


    // Static attributes (define your keys here):
    private static final String PREF_NAME = "ClarityAppPreferences";
    private static final String KEY_CAL_POST_IDS = "calendarPostIds";
    private static final String KEY_THEME = "appTheme";

    // Instance attributes:
    private final SharedPreferences sharedPreferences;
    private final Set<Integer> calendarPostIds;

    /*
     * Methods for events (posts) saved to Calendar
     * --------------------------------------------
     * Events (posts) that users add to their calendar are saved locally in sharedPrefs
     * as a String set with the key "calendarPostIds" (KEY_CAL_POST_IDS)
     * Changes made by addToCalendar and removeFromCalendar must be committed with commitCalendarUpdates
     * in order to save those changes to the sharedPrefs.
     * Note: when these methods are executed from a fragment, pass in getActivity() as context
     */

    public PreferenceUtils(Context context) {
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        calendarPostIds = new HashSet<>();

        // Load in posts (events) saved to calendar
        Set<String> stringSet = sharedPreferences.getStringSet(KEY_CAL_POST_IDS, new HashSet<>());
        for (String postIdString: stringSet) {
            calendarPostIds.add(Integer.parseInt(postIdString));
        }
    }

    public void addToCalendar(int postId) {
        calendarPostIds.add(postId);
    }

    public void removeFromCalendar(int postId) {
        calendarPostIds.remove(postId);
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
}
