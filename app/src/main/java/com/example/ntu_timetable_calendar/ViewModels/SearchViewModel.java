package com.example.ntu_timetable_calendar.ViewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

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


  /*  public MutableLiveData<Map<String, Exam>> getSelectedExams() {
        return selectedExams;
    }*/
}
