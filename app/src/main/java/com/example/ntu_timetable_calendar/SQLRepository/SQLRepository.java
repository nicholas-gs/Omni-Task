package com.example.ntu_timetable_calendar.SQLRepository;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.example.ntu_timetable_calendar.Converters.CourseEventToCourseEventEntityConverter;
import com.example.ntu_timetable_calendar.Converters.CourseToCourseEntityConverter;
import com.example.ntu_timetable_calendar.Converters.ExamToExamEventConverter;
import com.example.ntu_timetable_calendar.DAO.AlarmDAO;
import com.example.ntu_timetable_calendar.DAO.CourseDAO;
import com.example.ntu_timetable_calendar.DAO.CourseEventDAO;
import com.example.ntu_timetable_calendar.DAO.ExamDAO;
import com.example.ntu_timetable_calendar.DAO.ExamEventDAO;
import com.example.ntu_timetable_calendar.DAO.TaskDAO;
import com.example.ntu_timetable_calendar.DAO.TimetableDAO;
import com.example.ntu_timetable_calendar.Entity.AlarmEntity;
import com.example.ntu_timetable_calendar.Entity.CourseEntity;
import com.example.ntu_timetable_calendar.Entity.CourseEventEntity;
import com.example.ntu_timetable_calendar.Entity.ExamEntity;
import com.example.ntu_timetable_calendar.Entity.ExamEventEntity;
import com.example.ntu_timetable_calendar.Entity.TaskEntity;
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
    private CourseEventDAO courseEventDAO;
    private ExamEventDAO examEventDAO;
    private TaskDAO taskDAO;
    private AlarmDAO alarmDAO;

    public SQLRepository(Application application) {
        this.sqlDatabase = SQLDatabase.getInstance(application);
        this.timetableDAO = this.sqlDatabase.timetableDAO();
        this.courseDAO = this.sqlDatabase.courseDAO();
        this.examDAO = this.sqlDatabase.examDAO();
        this.courseEventDAO = this.sqlDatabase.courseEventDAO();
        this.examEventDAO = this.sqlDatabase.examEventDAO();
        this.taskDAO = this.sqlDatabase.taskDAO();
        this.alarmDAO = this.sqlDatabase.alarmDAO();
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Callback method returning the auto-generated timetable id when a new timetable is inserted by timetableDAO
     */
    public interface InsertTimetableCompletedListener {
        void onInsertTimetableCallback(Long timetableId);
    }

    private InsertTimetableCompletedListener mListener;

    public void setInsertTimetableCompletedListener(InsertTimetableCompletedListener insertTimetableCompletedListener) {
        this.mListener = insertTimetableCompletedListener;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Callback method returning the auto-generated task id when a new task is inserted by TaskDao
     */
    public interface InsertTaskCompletedListener {
        void onInsertTaskCallback(Long taskId);
    }

    private InsertTaskCompletedListener insertTaskCompletedListener;

    public void setInsertTaskCompletedListener(InsertTaskCompletedListener insertTaskCompletedListener) {
        this.insertTaskCompletedListener = insertTaskCompletedListener;
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
            sqlRepository.mListener.onInsertTimetableCallback(aLong);
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
            CourseToCourseEntityConverter courseToCourseEntityConverter = new CourseToCourseEntityConverter(timetableId, courseList, indexSel);
            List<CourseEntity> courseEntityList = courseToCourseEntityConverter.convert();
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

    ////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////

    public LiveData<List<CourseEventEntity>> getAllCourseEvents() {
        return courseEventDAO.getAllCourseEvents();
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////

    public LiveData<List<CourseEventEntity>> getTimetableCourseEvents(int timetableId) {
        return courseEventDAO.getTimetableCourseEvents(timetableId);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////

    public LiveData<CourseEventEntity> getCourseEvent(int id) {
        return courseEventDAO.getCourseEvent(id);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////

    public void insertCourseEvents(List<Course> courseList, Map<String, String> indexSel, int timetableId, int startYear, int startMonth, int startDate) {
        new InsertCourseEventsAsyncTask(this.courseEventDAO, courseList, indexSel, timetableId, startYear, startMonth, startDate).execute();
    }

    private static class InsertCourseEventsAsyncTask extends AsyncTask<Void, Void, Void> {

        private CourseEventDAO eventDAO;
        private List<Course> courseList;
        private int timetableId;
        private Map<String, String> indexSel;
        private int startYear;
        private int startMonth;
        private int startDate;

        InsertCourseEventsAsyncTask(CourseEventDAO eventDAO, List<Course> courseList, Map<String, String> indexSel, int timetableId, int startYear, int startMonth, int startDate) {
            this.eventDAO = eventDAO;
            this.courseList = courseList;
            this.indexSel = indexSel;
            this.timetableId = timetableId;
            this.startYear = startYear;
            this.startMonth = startMonth;
            this.startDate = startDate;
        }

        @Override
        protected Void doInBackground(Void... voids) {

            // Convert the list of courses into a list of CourseEntity using CourseToCourseEntityConverter helper class
            CourseToCourseEntityConverter courseToCourseEntityConverter = new CourseToCourseEntityConverter(timetableId, courseList, indexSel);
            List<CourseEntity> courseEntityList = courseToCourseEntityConverter.convert();

            // Convert the list of CourseEntity into a list of CourseEventEntity to save in Room
            List<CourseEventEntity> courseEventEntity = new CourseEventToCourseEventEntityConverter(courseEntityList, startYear, startMonth, startDate).convert();

            eventDAO.insert(courseEventEntity);
            return null;
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////

    public void updateCourseEvent(CourseEventEntity courseEventEntity) {
        new UpdateCourseEventAsyncTask(this.courseEventDAO, courseEventEntity).execute();
    }

    private static class UpdateCourseEventAsyncTask extends AsyncTask<Void, Void, Void> {

        private CourseEventDAO eventDAO;
        private CourseEventEntity courseEventEntity;

        UpdateCourseEventAsyncTask(CourseEventDAO eventDAO, CourseEventEntity courseEventEntity) {
            this.eventDAO = eventDAO;
            this.courseEventEntity = courseEventEntity;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            eventDAO.update(courseEventEntity);
            return null;
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////

    public void deleteCourseEvent(CourseEventEntity courseEventEntity) {
        new DeleteCourseEventAsyncTask(this.courseEventDAO, courseEventEntity).execute();
    }

    private static class DeleteCourseEventAsyncTask extends AsyncTask<Void, Void, Void> {

        private CourseEventDAO eventDAO;
        private CourseEventEntity courseEventEntity;

        DeleteCourseEventAsyncTask(CourseEventDAO eventDAO, CourseEventEntity courseEventEntity) {
            this.eventDAO = eventDAO;
            this.courseEventEntity = courseEventEntity;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            eventDAO.delete(courseEventEntity);
            return null;
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////

    public LiveData<List<ExamEventEntity>> getAllExamEvents() {
        return examEventDAO.getAllExamEvents();
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////

    public LiveData<List<ExamEventEntity>> getTimetableExamEvents(int timetableId) {
        return examEventDAO.getTimetableExamEvents(timetableId);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////

    public void deleteExamEvent(ExamEventEntity examEventEntity) {
        new DeleteExamEventAsyncTask(examEventDAO, examEventEntity).execute();
    }

    private static class DeleteExamEventAsyncTask extends AsyncTask<Void, Void, Void> {

        private ExamEventDAO examEventDAO;
        private ExamEventEntity examEventEntity;

        DeleteExamEventAsyncTask(ExamEventDAO examEventDAO, ExamEventEntity examEventEntity) {
            this.examEventDAO = examEventDAO;
            this.examEventEntity = examEventEntity;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            examEventDAO.delete(this.examEventEntity);
            return null;
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////

    public void updateExamEvent(ExamEventEntity examEventEntity) {
        new UpdateExamEventAsyncTask(examEventDAO, examEventEntity).execute();
    }

    private static class UpdateExamEventAsyncTask extends AsyncTask<Void, Void, Void> {

        private ExamEventDAO examEventDAO;
        private ExamEventEntity examEventEntity;

        UpdateExamEventAsyncTask(ExamEventDAO examEventDAO, ExamEventEntity examEventEntity) {
            this.examEventDAO = examEventDAO;
            this.examEventEntity = examEventEntity;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            examEventDAO.update(this.examEventEntity);
            return null;
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////

    public void insertExamEvents(List<Exam> examList, int timetableId) {
        new InsertExamEventsAsyncTask(examEventDAO, examList, timetableId).execute();
    }

    private static class InsertExamEventsAsyncTask extends AsyncTask<Void, Void, Void> {

        private ExamEventDAO examEventDAO;
        private List<Exam> examList;
        private int timetableId;

        InsertExamEventsAsyncTask(ExamEventDAO examEventDAO, List<Exam> examList, int timetableId) {
            this.examEventDAO = examEventDAO;
            this.examList = examList;
            this.timetableId = timetableId;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            List<ExamEventEntity> eventEntityList = new ExamToExamEventConverter(this.examList, this.timetableId).convert();
            examEventDAO.insert(eventEntityList);
            return null;
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////

    public LiveData<List<TaskEntity>> getAllTasks() {
        return taskDAO.getAllTasks();
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////

    public LiveData<List<TaskEntity>> getTimetableTasks(int timetableId) {
        return taskDAO.getTimetableTasks(timetableId);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////

    public LiveData<List<TaskEntity>> getClassTasks(int courseEventEntityId) {
        return taskDAO.getClassTasks(courseEventEntityId);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////

    public LiveData<TaskEntity> getTask(int id) {
        return taskDAO.getTask(id);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////

    public LiveData<List<TaskEntity>> getTasksWithinTime(long nowTime, long deadLineTime) {
        return taskDAO.getTasksWithinTime(nowTime, deadLineTime);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////

    public void clearAllClassesInTasks() {
        new ClearAllClassesInTasksAsyncTask(this.taskDAO).execute();
    }

    private static class ClearAllClassesInTasksAsyncTask extends AsyncTask<Void, Void, Void> {

        private TaskDAO taskDAO;

        ClearAllClassesInTasksAsyncTask(TaskDAO taskDAO) {
            this.taskDAO = taskDAO;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            this.taskDAO.clearAllClassesInTasks();
            return null;
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////

    public void insertTask(TaskEntity taskEntity) {
        new InsertTaskAsyncTask(this.taskDAO, taskEntity, this).execute();
    }

    private static class InsertTaskAsyncTask extends AsyncTask<Void, Void, Long> {

        private TaskDAO taskDAO;
        private TaskEntity taskEntity;
        private SQLRepository sqlRepository;

        InsertTaskAsyncTask(TaskDAO taskDAO, TaskEntity taskEntity, SQLRepository sqlRepository) {
            this.taskDAO = taskDAO;
            this.taskEntity = taskEntity;
            this.sqlRepository = sqlRepository;
        }

        @Override
        protected Long doInBackground(Void... voids) {
            return this.taskDAO.insert(this.taskEntity);
        }

        @Override
        protected void onPostExecute(Long aLong) {
            super.onPostExecute(aLong);
            if (sqlRepository.insertTaskCompletedListener != null) {
                sqlRepository.insertTaskCompletedListener.onInsertTaskCallback(aLong);
            }
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////

    public void deleteTask(TaskEntity taskEntity) {
        new DeleteTaskAsyncTask(this.taskDAO, taskEntity).execute();
    }

    private static class DeleteTaskAsyncTask extends AsyncTask<Void, Void, Void> {

        private TaskDAO taskDAO;
        private TaskEntity taskEntity;

        DeleteTaskAsyncTask(TaskDAO taskDAO, TaskEntity taskEntity) {
            this.taskDAO = taskDAO;
            this.taskEntity = taskEntity;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            this.taskDAO.delete(this.taskEntity);
            return null;
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////

    public void updateTask(TaskEntity taskEntity) {
        new UpdateTaskAsyncTask(this.taskDAO, taskEntity).execute();
    }

    private static class UpdateTaskAsyncTask extends AsyncTask<Void, Void, Void> {

        private TaskDAO taskDAO;
        private TaskEntity taskEntity;

        UpdateTaskAsyncTask(TaskDAO taskDAO, TaskEntity taskEntity) {
            this.taskDAO = taskDAO;
            this.taskEntity = taskEntity;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            this.taskDAO.update(this.taskEntity);
            return null;
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////

    public void completeTask(int taskId) {
        new CompleteTaskAsyncTask(this.taskDAO, taskId).execute();
    }

    private static class CompleteTaskAsyncTask extends AsyncTask<Void, Void, Void> {

        private TaskDAO taskDAO;
        private int taskId;

        CompleteTaskAsyncTask(TaskDAO taskDAO, int taskId) {
            this.taskDAO = taskDAO;
            this.taskId = taskId;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            taskDAO.completeTask(taskId);
            return null;
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////

    public void insertAlarm(AlarmEntity alarmEntity) {
        new InsertAlarmAsyncTask(this.alarmDAO, alarmEntity, this).execute();
    }

    private static class InsertAlarmAsyncTask extends AsyncTask<Void, Void, Void> {

        private AlarmDAO alarmDAO;
        private AlarmEntity alarmEntity;
        private SQLRepository sqlRepository;

        InsertAlarmAsyncTask(AlarmDAO alarmDAO, AlarmEntity alarmEntity, SQLRepository sqlRepository) {
            this.alarmDAO = alarmDAO;
            this.alarmEntity = alarmEntity;
            this.sqlRepository = sqlRepository;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            alarmDAO.insert(alarmEntity);
            return null;
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////

    public void updateAlarm(AlarmEntity alarmEntity) {
        new UpdateAlarmAsyncTask(this.alarmDAO, alarmEntity).execute();
    }

    private static class UpdateAlarmAsyncTask extends AsyncTask<Void, Void, Void> {

        private AlarmDAO alarmDAO;
        private AlarmEntity alarmEntity;

        UpdateAlarmAsyncTask(AlarmDAO alarmDAO, AlarmEntity alarmEntity) {
            this.alarmDAO = alarmDAO;
            this.alarmEntity = alarmEntity;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            alarmDAO.update(alarmEntity);
            return null;
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////

    public void deleteAlarm(AlarmEntity alarmEntity) {
        new DeleteAlarmAsyncTask(this.alarmDAO, alarmEntity).execute();
    }

    private static class DeleteAlarmAsyncTask extends AsyncTask<Void, Void, Void> {

        private AlarmDAO alarmDAO;
        private AlarmEntity alarmEntity;

        DeleteAlarmAsyncTask(AlarmDAO alarmDAO, AlarmEntity alarmEntity) {
            this.alarmDAO = alarmDAO;
            this.alarmEntity = alarmEntity;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            alarmDAO.delete(alarmEntity);
            return null;
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////

    public void deleteAllAlarms() {
        new DeleteAllAlarmsAsyncTask(this.alarmDAO).execute();
    }

    private static class DeleteAllAlarmsAsyncTask extends AsyncTask<Void, Void, Void> {

        private AlarmDAO alarmDAO;

        DeleteAllAlarmsAsyncTask(AlarmDAO alarmDAO) {
            this.alarmDAO = alarmDAO;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            alarmDAO.deleteAllAlarms();
            return null;
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////

    public LiveData<List<AlarmEntity>> getAllAlarms() {
        return this.alarmDAO.getAllAlarms();
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////

    public LiveData<List<AlarmEntity>> getAllTaskAlarms(int taskId){
        return this.alarmDAO.getAllTaskAlarms(taskId);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////

    public void deleteTaskAlarms(int taskId) {
        new DeleteTaskAlarmsAsyncTask(this.alarmDAO, taskId).execute();
    }

    private static class DeleteTaskAlarmsAsyncTask extends AsyncTask<Void, Void, Void> {

        private AlarmDAO alarmDAO;
        private int taskId;

        DeleteTaskAlarmsAsyncTask(AlarmDAO alarmDAO, int taskId) {
            this.alarmDAO = alarmDAO;
            this.taskId = taskId;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            this.alarmDAO.deleteTaskAlarms(this.taskId);
            return null;
        }
    }
}