package com.example.ntu_timetable_calendar.Helper;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Singleton pattern to improve performance by preventing multiple instance of GSON.
 * Make changes to Gson Builder here!
 * To Use : Gson gson = GsonInstance.getInstance();
 */
public abstract class GsonInstance {

    private static Gson gson;

    public static synchronized Gson getInstance() {
        if (gson == null) {
            gson = new GsonBuilder().serializeNulls().create();
        }
        return gson;
    }
}
