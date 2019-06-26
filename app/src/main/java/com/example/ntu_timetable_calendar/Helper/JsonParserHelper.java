package com.example.ntu_timetable_calendar.Helper;

import java.io.IOException;
import java.io.InputStream;

public abstract class JsonParserHelper {

    /**
     * Reads the json file in ./res/raw and returns it as a string
     * To use : String jsonStr = inputStreamToString(getContext().getResources()
     * .openRawResource(R.raw.course_2019_1_data));
     *
     * @param inputStream
     * @return
     */
    public static String inputStreamToString(InputStream inputStream) {
        try {
            byte[] bytes = new byte[inputStream.available()];
            inputStream.read(bytes, 0, bytes.length);
            String json = new String(bytes);
            return json;
        } catch (IOException e) {
            return null;
        }
    }
}
