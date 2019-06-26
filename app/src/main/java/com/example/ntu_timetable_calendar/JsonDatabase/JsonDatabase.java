package com.example.ntu_timetable_calendar.JsonDatabase;

import android.content.Context;

import com.example.ntu_timetable_calendar.CourseModels.Course;
import com.example.ntu_timetable_calendar.DAO.JsonDAO;
import com.example.ntu_timetable_calendar.ExamModels.Exam;
import com.example.ntu_timetable_calendar.Helper.JsonParserHelper;
import com.example.ntu_timetable_calendar.R;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 1) This is the database containing all the courses and all the exams.
 * Here, the two respective json strings are converted into POJO using GSON
 * <p>
 * 2) We will use the singleton pattern to ensure that there is only one instance of this JsonDatabase, in order
 * to improve performance and reduces memory space usage. This is because deserializing the json string is quite
 * costly as they are quite large.
 * 3) The database is only responsible fo converting JSON String into POJO. All database access methods are defined
 * in JsonDAO.java class
 */
public class JsonDatabase {

    /*Private Constructor so that you cannot instantiate this class from outside, hence
    you have to get the single instance of this database through "getJsonDatabaseInstance()"*/
    private static JsonDatabase jsonDatabase;
    private static List<Course> allCourses;
    private static List<Exam> allExams;
    private static Gson gson;
    public static JsonDAO jsonDAO;

    private JsonDatabase(Context context) {
        gson = new GsonBuilder().serializeNulls().create();
        getAllCourses(context);
        getAllExams(context);
        jsonDAO = new JsonDAO(allCourses, allExams);
    }

    public static synchronized JsonDatabase getJsonDatabaseInstance(Context context) {
        if (jsonDatabase == null) {
            jsonDatabase = new JsonDatabase(context);
        }
        return jsonDatabase;
    }

    /**
     * Singleton pattern in converting the json of all courses into a map to improve performance,
     * since the json file is quite large
     *
     * @param context
     * @return
     */
    private static synchronized void getAllCourses(Context context) {
        if (allCourses == null) {
            String courseJsonStr = JsonParserHelper.inputStreamToString(context.getResources()
                    .openRawResource(R.raw.course_2019_1_data));
            Type mapType = new TypeToken<Map<String, Course>>() {
            }.getType();
            // Gson returns a Map<String, Course> with the key being the course code!
            Map<String, Course> allCoursesMap = gson.fromJson(courseJsonStr, mapType);

            // Convert that Map<String, Course> into a list of single Course item (i.e insert the course code into the course
            // object) to make our lives easier later on!
            if (allCoursesMap != null) {
                // Convert Map into two separate lists
                allCourses = new ArrayList<>(allCoursesMap.values());
                List<String> keyList = new ArrayList<>(allCoursesMap.keySet());

                // Insert course code into each course object
                int i = 0;
                for (Course c : allCourses) {
                    c.setCourseCode(keyList.get(i));
                    i++;
                }
            }
        }
    }

    /**
     * Singleton pattern in converting the json of all exams into a map to improve performance.
     *
     * @param context
     * @return
     */
    private static synchronized void getAllExams(Context context) {
        if (allExams == null) {
            String examJsonStr = JsonParserHelper.inputStreamToString(context.getResources()
                    .openRawResource(R.raw.exam_2019_1_data));
            Type mapType = new TypeToken<Map<String, Exam>>() {
            }.getType();
            // Gson returns a Map<String, Exam> with the key being the course code!
            // Actually, in each value, there is already the course code inside -- hence the key is actually useless
            Map<String, Exam> allExamsMap = gson.fromJson(examJsonStr, mapType);

            if(allExamsMap != null){
                allExams = new ArrayList<>(allExamsMap.values());
            }
        }
    }

}
