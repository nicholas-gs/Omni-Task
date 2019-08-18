package com.example.ntu_timetable_calendar.converters.objectconverters;

/**
 * For whatever reason, the time in the course_2019_1_data raw json files is in 24H, while in exam_2019_1_data it is in 12H.
 * This helper class converts the 12H string into 24H string
 */
public abstract class TimeConventionParser {

    private static String AM = "AM";
    private static String PM = "PM";

    public static String to24hString(String str) {

        if (str.length() == 7) {
            str = "0" + str;
        }

        String timePeriod = str.substring(str.length() - 2).toUpperCase();

        String hourStr = str.substring(0, 2);
        int hourInt = Integer.parseInt(hourStr);

        if (hourInt == 12) {
            hourInt = 0;
        }

        if (timePeriod.equals(PM)) {
            hourInt += 12;
        }
        String resultStr = (hourInt) + str.substring(3, 5);
        resultStr = resultStr.trim();
        if (resultStr.length() == 3) {
            resultStr = "0" + resultStr;
        }
        return resultStr;
    }

}