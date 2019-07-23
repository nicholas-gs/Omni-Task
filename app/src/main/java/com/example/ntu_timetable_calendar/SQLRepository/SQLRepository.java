package com.example.ntu_timetable_calendar.SQLRepository;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.example.ntu_timetable_calendar.Converters.CourseToEntityConverter;
import com.example.ntu_timetable_calendar.DAO.CourseDAO;
import com.example.ntu_timetable_calendar.DAO.ExamDAO;
import com.example.ntu_timetable_calendar.DAO.TimetableDAO;
import com.example.ntu_timetable_calendar.Entity.CourseEntity;
import com.example.ntu_timetable_calendar.Entity.ExamEntity;
import com.example.ntu_timetable_calendar.Entity.TimetableEntity;
import com.example.ntu_timetable_calendar.JsonModels.Course;
import com.example.ntu_timetable_calendar.JsonModels.Exam;
import com.example.ntu_timetable_calendar.Room.SQLDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SQLRepository {

    private SQLDatabase sqlDatabase;
    private TimetableDAO timetableDAO;
    private CourseDAO courseDAO;
    private ExamDAO examDAO;

    public SQLRepository(Application application) {
        this.sqlDatabase = SQLDatabase.getInstance(application);
        this.timetableDAO = this.sqlDatabase.timetableDAO();
        this.courseDAO = this.sqlDatabase.courseDAO();
        this.examDAO = this.sqlDatabase.examDAO();
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Callback method returning the auto-generated timetable id when a new timetable is inserted by timetableDAO
     */
    public interface InsertTimetableCompletedListener {
        void onInsertCallback(Long timetableId);
    }

    private InsertTimetableCompletedListener mListener;

    public void setInsertTimetableCompletedListener(InsertTimetableCompletedListener insertTimetableCompletedListener) {
        this.mListener = insertTimetableCompletedListener;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////

    public void insertTimetable(TimetableEntity timetableEntity) {
        new InsertTimetableAsyncTask(this.timetableDAO, timetableEntity, this).execute();
    }

    private static class InsertTimetableAsyncTask extends AsyncTask<Void, Void, Long> {

        private TimetableDAO timetableDAO;
        private TimetableEntity timetableEntity;
        private SQLRepository sqlRepository;

        InsertTimetableAsyncTask(TimetableDAO timetableDAO, TimetableEntity timetableEntity, SQLRepository sqlRepository) {
            this.timetableDAO = timetableDAO;
            this.timetableEntity = timetableEntity;
            this.sqlRepository = sqlRepository;
        }

        @Override
        protected Long doInBackground(Void... voids) {
            return timetableDAO.insert(timetableEntity);
        }

        @Override
        protected void onPostExecute(Long aLong) {
            super.onPostExecute(aLong);
            // Return Id of the newly inserted timetable
            sqlRepository.mListener.onInsertCallback(aLong);
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////

    public void updateTimetable(TimetableEntity timetableEntity) {
        new UpdateTimetableAsyncTask(this.timetableDAO).execute(timetableEntity);
    }

    private static class UpdateTimetableAsyncTask extends AsyncTask<TimetableEntity, Void, Void> {
        private TimetableDAO timetableDAO;

        UpdateTimetableAsyncTask(TimetableDAO timetableDAO) {
            this.timetableDAO = timetableDAO;
        }

        @Override
        protected Void doInBackground(TimetableEntity... timetableEntities) {
            timetableDAO.update(timetableEntities[0]);
            return null;
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////

    public void deleteTimetable(TimetableEntity timetableEntity) {
        new DeleteTimetableAsyncTask(this.timetableDAO).execute(timetableEntity);
    }

    private static class DeleteTimetableAsyncTask extends AsyncTask<TimetableEntity, Void, Void> {

        private TimetableDAO timetableDAO;

        DeleteTimetableAsyncTask(TimetableDAO timetableDAO) {
            this.timetableDAO = timetableDAO;
        }

        @Override
        protected Void doInBackground(TimetableEntity... timetableEntities) {
            timetableDAO.delete(timetableEntities[0]);
            return null;
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////

    public void deleteAllTimetables() {
        new DeleteAllTimetablesAsyncTask(this.timetableDAO).execute();
    }

    private static class DeleteAllTimetablesAsyncTask extends AsyncTask<Void, Void, Void> {

        private TimetableDAO timetableDAO;

        DeleteAllTimetablesAsyncTask(TimetableDAO timetableDAO) {
            this.timetableDAO = timetableDAO;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            timetableDAO.deleteAllTimetables();
            return null;
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////

    public LiveData<List<TimetableEntity>> getAllTimetables() {
        return timetableDAO.getAllTimetables();
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////

    public LiveData<TimetableEntity> getMainTimetable() {
        return timetableDAO.getMainTimetable();
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////

    public void setIsMainTimetable(int timetableId) {
        new SetIsMainTimetableAsyncTask(this.timetableDAO).execute(timetableId);
    }

    private static class SetIsMainTimetableAsyncTask extends AsyncTask<Integer, Void, Void> {

        private TimetableDAO timetableDAO;

        SetIsMainTimetableAsyncTask(TimetableDAO timetableDAO) {
            this.timetableDAO = timetableDAO;
        }

        @Override
        protected Void doInBackground(Integer... integers) {
            if (integers[0] != null) {
                timetableDAO.setIsMainTimetable(integers[0]);
            }
            return null;
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////

    public void setAllTimetablesToNotMain() {
        new SetAllTimetablesToNotMainAsyncTask(this.timetableDAO).execute();
    }

    private static class SetAllTimetablesToNotMainAsyncTask extends AsyncTask<Void, Void, Void> {

        private TimetableDAO timetableDAO;

        SetAllTimetablesToNotMainAsyncTask(TimetableDAO timetableDAO) {
            this.timetableDAO = timetableDAO;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            timetableDAO.setAllTimetablesToNotMain();
            return null;
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////

    public LiveData<List<CourseEntity>> getAllCourses() {
        return courseDAO.getAllCourses();
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////

    public LiveData<List<CourseEntity>> getTimetableCourses(int timetableId) {
        return courseDAO.getTimetableCourses(timetableId);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////

    public void insertCourses(int timetableId, List<Course> courseList, Map<String, String> indexSel) {
        new InsertCourseAsyncTask(this.courseDAO, timetableId, courseList, indexSel).execute();
    }

    private static class InsertCourseAsyncTask extends AsyncTask<Void, Void, Void> {

        private CourseDAO courseDAO;
        private int timetableId;
        private List<Course> courseList;
        private Map<String, String> indexSel;

        InsertCourseAsyncTask(CourseDAO courseDAO, int timetableId, List<Course> courseList, Map<String, String> indexSel) {
            this.courseDAO = courseDAO;
            this.timetableId = timetableId;
            this.courseList = courseList;
            this.indexSel = indexSel;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            CourseToEntityConverter courseToEntityConverter = new CourseToEntityConverter(timetableId, courseList, indexSel);
            List<CourseEntity> courseEntityList = courseToEntityConverter.converter();
            courseDAO.insert(courseEntityList);
            return null;
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////

    public void updateCourse(CourseEntity courseEntity) {
        new UpdateCourseAsyncTask(this.courseDAO).execute(courseEntity);
    }

    private static class UpdateCourseAsyncTask extends AsyncTask<CourseEntity, Void, Void> {

        private CourseDAO courseDAO;

        UpdateCourseAsyncTask(CourseDAO courseDAO) {
            this.courseDAO = courseDAO;
        }

        @Override
        protected Void doInBackground(CourseEntity... courseEntities) {
            courseDAO.update(courseEntities[0]);
            return null;
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////

    public void deleteCourse(CourseEntity courseEntity) {
        new DeleteCourseAsyncTask(this.courseDAO).execute(courseEntity);
    }

    private static class DeleteCourseAsyncTask extends AsyncTask<CourseEntity, Void, Void> {

        private CourseDAO courseDAO;

        DeleteCourseAsyncTask(CourseDAO courseDAO) {
            this.courseDAO = courseDAO;
        }

        @Override
        protected Void doInBackground(CourseEntity... courseEntities) {
            courseDAO.delete(courseEntities[0]);
            return null;
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////

    public LiveData<List<ExamEntity>> getAllExams() {
        return examDAO.getAllExams();
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////

    public LiveData<List<ExamEntity>> getTimetableExams(int timetableId) {
        return examDAO.getTimetableExams(timetableId);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////

    public void insertExams(List<Exam> examList, int timetableId) {
        new InsertExamsAsyncTask(this.examDAO, timetableId, examList).execute();
    }

    private static class InsertExamsAsyncTask extends AsyncTask<Void, Void, Void> {

        private ExamDAO examDAO;
        private int timetableId;
        private List<Exam> examList;
        private List<ExamEntity> examEntityList;

        InsertExamsAsyncTask(ExamDAO examDAO, int timetableId, List<Exam> examList) {
            this.examDAO = examDAO;
            this.timetableId = timetableId;
            this.examList = examList;
            this.examEntityList = new ArrayList<>();
        }

        @Override
        protected Void doInBackground(Void... voids) {

            for (Exam exam : examList) {
                ExamEntity examEntity = new ExamEntity(this.timetableId, exam.getDate(), exam.getDay(), exam.getTime(), exam.getCode(), exam.getName(),
                        exam.getDuration());
                this.examEntityList.add(examEntity);
            }

            examDAO.insert(this.examEntityList);
            return null;
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////

    public void updateExam(ExamEntity examEntity) {
        new UpdateExamAsyncTask(this.examDAO, examEntity).execute();
    }

    private static class UpdateExamAsyncTask extends AsyncTask<Void, Void, Void> {

        private ExamDAO examDAO;
        private ExamEntity examEntity;

        UpdateExamAsyncTask(ExamDAO examDAO, ExamEntity examEntity) {
            this.examDAO = examDAO;
            this.examEntity = examEntity;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            this.examDAO.update(this.examEntity);
            return null;
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////

    public void deleteExam(ExamEntity examEntity) {
        new DeleteExamAsyncTask(this.examDAO, examEntity).execute();
    }

    private static class DeleteExamAsyncTask extends AsyncTask<Void, Void, Void> {

        private ExamDAO examDAO;
        private ExamEntity examEntity;

        DeleteExamAsyncTask(ExamDAO examDAO, ExamEntity examEntity) {
            this.examDAO = examDAO;
            this.examEntity = examEntity;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            this.examDAO.delete(this.examEntity);
            return null;
        }
    }

}
