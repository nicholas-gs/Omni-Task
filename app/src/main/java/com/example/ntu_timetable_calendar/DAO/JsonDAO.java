package com.example.ntu_timetable_calendar.DAO;

import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.example.ntu_timetable_calendar.CourseModels.Course;
import com.example.ntu_timetable_calendar.ExamModels.Exam;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * This class is instantiated in JsonDatabase.java and acts as the API for access the json database contents.
 * All business logic, such as filtering results are defined here!
 */
public class JsonDAO {

    private List<Course> allCourses;
    private List<Exam> allExams;

    private MutableLiveData<List<Course>> filteredCourseList = new MutableLiveData<>();
    private MutableLiveData<List<Exam>> filteredExamList = new MutableLiveData<>();

    public JsonDAO(List<Course> allCourses, List<Exam> allExams) {
        this.allCourses = allCourses;
        this.allExams = allExams;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    public List<Course> getAllCourses() {
        return allCourses;
    }

    public List<Exam> getAllExams() {
        return allExams;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////

    public MutableLiveData<List<Course>> getFilteredCourseList() {
        return filteredCourseList;
    }

    public MutableLiveData<List<Exam>> getFilteredExamList() {
        return filteredExamList;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Asynchronously searches through the entire list of all courses for courses with SIMILAR names/code, then
     * posts the result to MutableLiveData<List<Course>> filteredCourseList and triggers the observer.
     *
     * NOTE : When the queryStr is empty, i.e is "", it returns the entire list
     * @param queryStr
     */
    public void queryCourseData(String queryStr) {
        new QueryCourseDataAsyncTask(allCourses, filteredCourseList, queryStr).execute();
    }

    private static class QueryCourseDataAsyncTask extends AsyncTask<Void, Void, Void> {

        List<Course> allCourses;
        private MutableLiveData<List<Course>> filteredList;
        private String queryStr;

        QueryCourseDataAsyncTask(List<Course> allCourses,
                                 MutableLiveData<List<Course>> filteredList, String queryStr) {
            this.allCourses = allCourses;
            this.filteredList = filteredList;
            this.queryStr = queryStr;
        }

        @Override
        protected Void doInBackground(Void... voids) {

            if (queryStr.equals("")) {
                List<Course> filteredListTemp = new ArrayList<>(allCourses);
                filteredList.postValue(filteredListTemp);
            } else {
                List<Course> filteredListTemp = new ArrayList<>();
                for (Course c : allCourses) {
                    if (c.getCourseCode().contains(queryStr) || c.getName().contains(queryStr)) {
                        filteredListTemp.add(c);
                    }
                }
                filteredList.postValue(filteredListTemp);
            }

            return null;
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Asynchronously searches through the entire list of all exams for exams with the SAME code, then
     * posts the result to MutableLiveData<List<Exam>> filteredExamList and triggers the observer.
     *
     * NOTE : When the queryStr is empty, i.e is "", it returns the entire list
     * @param queryStr
     */
    public void queryExamData(String queryStr) {
        new QueryExamDataAsyncTask(allExams, filteredExamList, queryStr).execute();
    }


    private static class QueryExamDataAsyncTask extends AsyncTask<Void, Void, Void> {

        List<Exam> allExams;
        private MutableLiveData<List<Exam>> filteredList;
        private String queryStr;

        QueryExamDataAsyncTask(List<Exam> allExams, MutableLiveData<List<Exam>> filteredList, String queryStr) {
            this.allExams = allExams;
            this.filteredList = filteredList;
            this.queryStr = queryStr;
        }

        @Override
        protected Void doInBackground(Void... voids) {

            if (queryStr.equals("")) {
                List<Exam> filteredListTemp = new ArrayList<>(allExams);
                filteredList.postValue(filteredListTemp);
            } else {
                List<Exam> filteredListTemp = new ArrayList<>();
                for (Exam e : allExams) {
                    if (e.getCode().equals(queryStr)) {
                        filteredListTemp.add(e);
                    }
                }
                filteredList.postValue(filteredListTemp);
            }

            return null;
        }
    }

}
