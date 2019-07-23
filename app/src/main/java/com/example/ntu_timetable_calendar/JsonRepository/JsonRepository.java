package com.example.ntu_timetable_calendar.JsonRepository;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.MutableLiveData;

import com.example.ntu_timetable_calendar.JsonDAO.JsonDAO;
import com.example.ntu_timetable_calendar.JsonDatabase.JsonDatabase;
import com.example.ntu_timetable_calendar.JsonModels.Course;
import com.example.ntu_timetable_calendar.JsonModels.Exam;

import java.util.List;

/**
 * To Use : JsonRepository repository = new JsonRepository(getApplicationContext())
 * Instantiate an instance of this class in order to access the courses and exams data
 */
public class JsonRepository {

    private JsonDatabase jsonDatabase;
    private JsonDAO jsonDAO;

    public JsonRepository(Application application) {
        jsonDatabase = JsonDatabase.getJsonDatabaseInstance(application);
        jsonDAO = JsonDatabase.jsonDAO;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////

    public MutableLiveData<List<Course>> getFilteredCourseList() {
        return jsonDAO.getFilteredCourseList();
    }

    public MutableLiveData<List<Exam>> getFilteredExamList() {
        return jsonDAO.getFilteredExamList();
    }

    public MutableLiveData<List<String>> getAllCourseCode() {
        return jsonDAO.getAllCourseCode();
    }

    public MutableLiveData<List<Course>> getTimetablePlanningCourseList() {
        return jsonDAO.getTimetablePlanningCourseList();
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Send a query (String) for course objects and asynchronously sends the query to the DAO
     *
     * @param queryStr String of course to query
     */
    public void queryCourseData(String queryStr) {
        new QueryCourseDataAsyncTask(jsonDAO, queryStr).execute();
    }

    private static class QueryCourseDataAsyncTask extends AsyncTask<Void, Void, Void> {

        private JsonDAO jsonDAO;
        private String queryStr;

        QueryCourseDataAsyncTask(JsonDAO jsonDAO, String queryStr) {
            this.jsonDAO = jsonDAO;
            this.queryStr = queryStr;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            jsonDAO.queryCourseData(queryStr);
            return null;
        }
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Send a query (String) for exam objects and asynchronously sends the query to the DAO
     *
     * @param queryStrList List of string of course codes to query
     */
    public void queryExamData(List<String> queryStrList) {
        new QueryExamDataAsyncTask(jsonDAO, queryStrList).execute();
    }

    private static class QueryExamDataAsyncTask extends AsyncTask<Void, Void, Void> {

        private JsonDAO jsonDAO;
        private List<String> queryStrList;

        QueryExamDataAsyncTask(JsonDAO jsonDAO, List<String> queryStrList) {
            this.jsonDAO = jsonDAO;
            this.queryStrList = queryStrList;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            jsonDAO.queryExamData(queryStrList);
            return null;
        }
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Send a query for the entire list of all course code
     */
    public void queryAllCourseCode() {
        new QueryAllCourseCodeAsyncTask(jsonDAO).execute();
    }

    private static class QueryAllCourseCodeAsyncTask extends AsyncTask<Void, Void, Void> {

        private JsonDAO jsonDAO;

        QueryAllCourseCodeAsyncTask(JsonDAO jsonDAO) {
            this.jsonDAO = jsonDAO;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            jsonDAO.queryAllCourseCode();
            return null;
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////

    public void queryPlanningTimetableCourses(List<String> listOfQueries) {
        new QueryPlanningTimetableCoursesAsyncTaks(jsonDAO, listOfQueries).execute();
    }

    private static class QueryPlanningTimetableCoursesAsyncTaks extends AsyncTask<Void, Void, Void> {

        private JsonDAO jsonDAO;
        private List<String> listOfCourseCodes;

        QueryPlanningTimetableCoursesAsyncTaks(JsonDAO jsonDAO, List<String> listOfCourseCodes) {
            this.jsonDAO = jsonDAO;
            this.listOfCourseCodes = listOfCourseCodes;
        }

        @Override
        protected Void doInBackground(Void... voids) {

            jsonDAO.queryPlanningTimetableCourseList(listOfCourseCodes);
            return null;
        }
    }

}
