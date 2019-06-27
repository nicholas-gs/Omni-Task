package com.example.ntu_timetable_calendar.ViewModels;

import android.app.Application;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.ntu_timetable_calendar.CourseModels.Course;
import com.example.ntu_timetable_calendar.ExamModels.Exam;
import com.example.ntu_timetable_calendar.Repository.JsonRepository;

import java.util.List;

public class SearchViewModel extends AndroidViewModel {

    private JsonRepository jsonRepository;
    /*private MutableLiveData<Map<String, Exam>> selectedExams = new MutableLiveData<>();*/

    public SearchViewModel(@NonNull Application application) {
        super(application);
        jsonRepository = new JsonRepository(application);
    }

    public List<Course> getAllCourses() {
        return jsonRepository.getAllCourses();
    }

    public List<Exam> getAllExams() {
        return jsonRepository.getAllExams();
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////

    public MutableLiveData<List<Course>> getFilteredList() {
        return jsonRepository.getFilteredList();
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////

    public void queryCourseData(String queryStr) {
        new QueryCourseDataAsyncTask(queryStr, jsonRepository).execute();
    }

    private static class QueryCourseDataAsyncTask extends AsyncTask<Void, Void, Void> {

        private String queryStr;
        private JsonRepository jsonRepository;

        public QueryCourseDataAsyncTask(String queryStr, JsonRepository jsonRepository) {
            this.queryStr = queryStr;
            this.jsonRepository = jsonRepository;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            jsonRepository.queryCourseData(queryStr);

            return null;
        }
    }


}
