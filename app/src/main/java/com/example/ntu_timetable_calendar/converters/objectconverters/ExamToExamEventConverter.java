package com.example.ntu_timetable_calendar.converters.objectconverters;

import com.example.ntu_timetable_calendar.models.entities.ExamEventEntity;
import com.example.ntu_timetable_calendar.converters.CalendarParser;
import com.example.ntu_timetable_calendar.utils.constants.EventColors;
import com.example.ntu_timetable_calendar.models.jsonmodels.Exam;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ExamToExamEventConverter {

    private List<Exam> examList;
    private int timetableId;
    private List<ExamEventEntity> finalList = new ArrayList<>();

    public ExamToExamEventConverter(List<Exam> examList, int timetableId) {
        this.examList = examList;
        this.timetableId = timetableId;
    }

    public List<ExamEventEntity> convert() {

        int i = 1;
        for (Exam exam : examList) {
            List<Calendar> calendarList = CalendarParser.parseExamTime(exam);
            String title = "Final Exam : " + exam.getCode().trim() + " - " + exam.getName().trim();
            ExamEventEntity examEventEntity = new ExamEventEntity(timetableId, title, "", EventColors.colors()[i % 4], false, false,
                    calendarList.get(0).getTimeInMillis(), calendarList.get(1).getTimeInMillis());
            this.finalList.add(examEventEntity);
            i++;
        }

        return this.finalList;
    }
}