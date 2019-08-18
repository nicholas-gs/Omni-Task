package com.example.ntu_timetable_calendar.models.jsonmodels;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Index {
    @SerializedName("index_number")
    private String indexNumber;
    private List<Detail> details;

    public Index(String indexNumber, List<Detail> details) {
        this.indexNumber = indexNumber;
        this.details = details;
    }

    public String getIndexNumber() {
        return indexNumber;
    }

    public List<Detail> getDetails() {
        return details;
    }
}
