package com.example.tageatproject;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class RecentSearchManager {
    private static final String PREFS_NAME = "recent_searches";
    private static final String KEY_SEARCHES = "search_list";
    private static final int MAX_ITEMS = 10;

    public static void saveSearch(Context context, String keyword) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        Set<String> set = prefs.getStringSet(KEY_SEARCHES, new LinkedHashSet<>());
        List<String> list = new ArrayList<>(set);

        list.remove(keyword);
        list.add(0, keyword);

        while (list.size() > MAX_ITEMS) {
            list.remove(list.size() - 1);
        }

        prefs.edit().putStringSet(KEY_SEARCHES, new LinkedHashSet<>(list)).apply();
    }

    public static List<String> getRecentSearches(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        Set<String> set = prefs.getStringSet(KEY_SEARCHES, new LinkedHashSet<>());
        List<String> list = new ArrayList<>(set);
        return list;
    }
}
