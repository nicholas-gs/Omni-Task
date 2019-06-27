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

    private MutableLiveData<List<Course>> filteredList = new MutableLiveData<>();
    private static final int PAGING_SIZE = 5;

    public JsonDAO(List<Course> allCourses, List<Exam> allExams) {
        this.allCourses = allCourses;
        this.allExams = allExams;
    }

    public List<Course> getAllCourses() {
        return allCourses;
    }

    public List<Exam> getAllExams() {
        return allExams;
    }

    public MutableLiveData<List<Course>> getFilteredList() {
        return filteredList;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Paging method call to request course data 50 at a time - don't even know if need this method
     */
    public void queryCourseData(String queryStr) {
        new QueryCourseDataAsyncTask(allCourses, filteredList, queryStr).execute();
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
     * Takes in a list of courses (String) you want to select, and then searches through the map of all courses,
     * and then returns the selected courses.
     * If a course cannot be found, null is assigned to the key in the map.
     *
     * @param courseSelection
     * @return
     */
    public Map<String, Course> getSelectedCourses(@NonNull List<String> courseSelection) {
        // TODO
        return null;
    }

    /**
     * Takes in a list of exams (String) you want to select, and then searches through the map of all exams,
     * and then returns the selected exams.
     * If a exam cannot be found, null is assigned to the key in the map.
     *
     * @param examSelection
     * @return
     */
    public Map<String, Exam> getSelectedExams(@NonNull List<String> examSelection) {
        // TODO
        return null;
    }

}
