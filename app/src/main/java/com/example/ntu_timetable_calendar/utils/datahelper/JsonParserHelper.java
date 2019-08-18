package com.example.ntu_timetable_calendar.utils.datahelper;

import java.io.IOException;
import java.io.InputStream;

public abstract class JsonParserHelper {

    /**
     * Reads the json file in ./res/raw and returns it as a string
     * To use : String jsonStr = inputStreamToString(getContext().getResources()
     * .openRawResource(R.raw.course_2019_1_data));
     *
     * @param inputStream Input stream from raw file
     * @return Json string read from the raw data file
     */
    public static String inputStreamToString(InputStream inputStream) {
        try {
            byte[] bytes = new byte[inputStream.available()];
            //noinspection ResultOfMethodCallIgnored
            inputStream.read(bytes, 0, bytes.length);
            return new String(bytes);
        } catch (IOException e) {
            return null;
        }
    }
}