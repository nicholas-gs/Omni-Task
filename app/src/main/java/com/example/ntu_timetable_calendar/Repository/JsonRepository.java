package com.example.ntu_timetable_calendar.Repository;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.MutableLiveData;

import com.example.ntu_timetable_calendar.CourseModels.Course;
import com.example.ntu_timetable_calendar.DAO.JsonDAO;
import com.example.ntu_timetable_calendar.ExamModels.Exam;
import com.example.ntu_timetable_calendar.JsonDatabase.JsonDatabase;

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

    public List<Course> getAllCourses() {
        return jsonDAO.getAllCourses();
    }

    public List<Exam> getAllExams() {
        return jsonDAO.getAllExams();
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////

    public MutableLiveData<List<Course>> getFilteredCourseList() {
        return jsonDAO.getFilteredCourseList();
    }

    public MutableLiveData<List<Exam>> getFilteredExamList() {
        return jsonDAO.getFilteredExamList();
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////

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

    public void queryExamData(String queryStr) {
        new QueryExamDataAsyncTask(jsonDAO, queryStr).execute();
    }

    private static class QueryExamDataAsyncTask extends AsyncTask<Void, Void, Void> {

        private JsonDAO jsonDAO;
        private String querystr;

        QueryExamDataAsyncTask(JsonDAO jsonDAO, String querystr) {
            this.jsonDAO = jsonDAO;
            this.querystr = querystr;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            jsonDAO.queryExamData(querystr);
            return null;
        }
    }

}
