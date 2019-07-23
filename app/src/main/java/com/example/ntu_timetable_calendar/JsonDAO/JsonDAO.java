package com.example.ntu_timetable_calendar.JsonDAO;

import android.os.AsyncTask;

import androidx.lifecycle.MutableLiveData;

import com.example.ntu_timetable_calendar.JsonModels.Course;
import com.example.ntu_timetable_calendar.JsonModels.Exam;

import java.util.ArrayList;
import java.util.List;

/**
 * This class is instantiated in JsonDatabase.java and acts as the API for access the json database contents.
 * All business logic, such as filtering results are defined here!
 */
public class JsonDAO {

    private List<Course> allCourses;
    private List<Exam> allExams;

    private MutableLiveData<List<Course>> filteredCourseList = new MutableLiveData<>();
    private MutableLiveData<List<Exam>> filteredExamList = new MutableLiveData<>();
    private MutableLiveData<List<String>> allCourseCode = new MutableLiveData<>();
    private MutableLiveData<List<Course>> timetablePlanningCourseList = new MutableLiveData<>();

    public JsonDAO(List<Course> allCourses, List<Exam> allExams) {
        this.allCourses = allCourses;
        this.allExams = allExams;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////

    public MutableLiveData<List<Course>> getFilteredCourseList() {
        return filteredCourseList;
    }

    public MutableLiveData<List<Exam>> getFilteredExamList() {
        return filteredExamList;
    }

    public MutableLiveData<List<Course>> getTimetablePlanningCourseList() {
        return timetablePlanningCourseList;
    }

    public MutableLiveData<List<String>> getAllCourseCode() {
        return allCourseCode;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Asynchronously searches through the entire list of all courses for courses with SIMILAR names/code as the queryStr,
     * then posts the result to MutableLiveData<List<Course>> filteredCourseList and triggers the observer.
     * <p>
     * NOTE : When the queryStr is empty, i.e is "", it returns the entire list
     *
     * @param queryStr String of course to query
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
     * <p>
     * NOTE : When the queryStrList has a size of 0, it returns the entire list
     *
     * @param queryStrList List of string of exam code to query
     */
    public void queryExamData(List<String> queryStrList) {
        new QueryExamDataAsyncTask(allExams, filteredExamList, queryStrList).execute();
    }


    private static class QueryExamDataAsyncTask extends AsyncTask<Void, Void, Void> {

        List<Exam> allExams;
        private MutableLiveData<List<Exam>> filteredList;
        private List<String> queryStrList;

        QueryExamDataAsyncTask(List<Exam> allExams, MutableLiveData<List<Exam>> filteredList, List<String> queryStrList) {
            this.allExams = allExams;
            this.filteredList = filteredList;
            this.queryStrList = queryStrList;
        }

        @Override
        protected Void doInBackground(Void... voids) {

            if (queryStrList.size() == 0) {
                List<Exam> filteredListTemp = new ArrayList<>(allExams);
                filteredList.postValue(filteredListTemp);
            } else {
                List<Exam> filteredListTemp = new ArrayList<>();

                for (String queryStr : queryStrList) {
                    for (Exam e : allExams) {
                        if (e.getCode().equals(queryStr)) {
                            filteredListTemp.add(e);
                        }
                    }
                }
                filteredList.postValue(filteredListTemp);
            }

            return null;
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Query for a list of all the course code (String) -- useful when you don't need the list of all course POJO objects
     */

    public void queryAllCourseCode() {
        new QueryAllCourseCodeAsyncTask(allCourses, allCourseCode).execute();
    }

    private static class QueryAllCourseCodeAsyncTask extends AsyncTask<Void, Void, Void> {

        List<Course> listOfAllCourses;
        MutableLiveData<List<String>> listOfAllCourseCode;

        QueryAllCourseCodeAsyncTask(List<Course> listOfAllCourses, MutableLiveData<List<String>> listOfAllCourseCode) {
            this.listOfAllCourses = listOfAllCourses;
            this.listOfAllCourseCode = listOfAllCourseCode;
        }

        @Override
        protected Void doInBackground(Void... voids) {

            List<String> temp = new ArrayList<>();

            for (Course c : listOfAllCourses) {
                temp.add(c.getCourseCode().trim());
            }

            listOfAllCourseCode.postValue(temp);

            return null;
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Asynchronously searches through the entire list of all courses and returns a list of courses whose course code
     * matches EXACTLY the the course codes in the list sent to the query.
     * <p>
     * Unlike the queryCourseData(String queryStr) method above, it takes in a list of course code as query ONLY as query and does
     * not searches for course names. The course code also has the match EXACTLY.
     * <p>
     * Used by the plan fragment when user enters a list of course code (String).
     *
     * @param listOfCourseCodes List of course code string to query
     */
    public void queryPlanningTimetableCourseList(List<String> listOfCourseCodes) {
        new QueryPlanningTimetableCourseListAsyncTask(allCourses, listOfCourseCodes, timetablePlanningCourseList)
                .execute();
    }

    private static class QueryPlanningTimetableCourseListAsyncTask extends AsyncTask<Void, Void, Void> {

        private List<Course> listOfAllCourses;
        private List<String> listOfCourseCodes;
        private MutableLiveData<List<Course>> filteredList;

        QueryPlanningTimetableCourseListAsyncTask(List<Course> listOfAllCourses, List<String> listOfCourseCodes,
                                                  MutableLiveData<List<Course>> filteredList) {
            this.listOfAllCourses = listOfAllCourses;
            this.listOfCourseCodes = listOfCourseCodes;
            this.filteredList = filteredList;
        }

        @Override
        protected Void doInBackground(Void... voids) {

            List<Course> tempList = new ArrayList<>();

            for (String str : listOfCourseCodes) {
                for (Course course : listOfAllCourses) {
                    if (course.getCourseCode().equals(str)) {
                        tempList.add(course);
                        break;
                    }
                }
            }

            filteredList.postValue(tempList);

            return null;
        }
    }

}
