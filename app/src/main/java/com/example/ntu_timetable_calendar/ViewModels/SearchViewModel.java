package com.example.ntu_timetable_calendar.ViewModels;

import android.app.Application;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.ntu_timetable_calendar.CourseModels.Course;
import com.example.ntu_timetable_calendar.ExamModels.Exam;
import com.example.ntu_timetable_calendar.Repository.JsonRepository;

import java.util.List;

public class SearchViewModel extends AndroidViewModel {

    private JsonRepository jsonRepository;

    public SearchViewModel(@NonNull Application application) {
        super(application);
        jsonRepository = new JsonRepository(application);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    public MutableLiveData<List<Course>> getFilteredCourseData() {
        return jsonRepository.getFilteredCourseList();
    }

    public MutableLiveData<List<Exam>> getFilteredExamData() {
        return jsonRepository.getFilteredExamList();
    }


    public LiveData<List<String>> getAllCourseCode() {
        return jsonRepository.getAllCourseCode();
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////

    public void queryCourseData(String queryStr) {
        new QueryCourseDataAsyncTask(queryStr, jsonRepository).execute();
    }

    private static class QueryCourseDataAsyncTask extends AsyncTask<Void, Void, Void> {

        private String queryStr;
        private JsonRepository jsonRepository;

        QueryCourseDataAsyncTask(String queryStr, JsonRepository jsonRepository) {
            this.queryStr = queryStr;
            this.jsonRepository = jsonRepository;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            jsonRepository.queryCourseData(queryStr);
            return null;
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////

    public void queryExamData(String queryStr) {
        new QueryExamDataAsyncTask(queryStr, jsonRepository).execute();
    }

    private static class QueryExamDataAsyncTask extends AsyncTask<Void, Void, Void> {

        private String queryStr;
        private JsonRepository jsonRepository;

        QueryExamDataAsyncTask(String queryStr, JsonRepository jsonRepository) {
            this.queryStr = queryStr;
            this.jsonRepository = jsonRepository;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            jsonRepository.queryExamData(queryStr);
            return null;
        }
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////

    public void queryAllCourseCode() {
        new QueryAllCourseCodeAsyncTask(jsonRepository).execute();
    }

    private static class QueryAllCourseCodeAsyncTask extends AsyncTask<Void, Void, Void> {


        private JsonRepository jsonRepository;

        QueryAllCourseCodeAsyncTask(JsonRepository jsonRepository) {
            this.jsonRepository = jsonRepository;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            jsonRepository.queryAllCourseCode();
            return null;
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////

}
