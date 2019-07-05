package com.example.ntu_timetable_calendar.EventModel

import com.alamkanak.weekview.WeekViewDisplayable
import com.alamkanak.weekview.WeekViewEvent
import java.util.*

/**
 * Class of object that you want to display in Android-Week-View
 */
class Event(
        val id: Long,
        val title: String,
        val startTime: Calendar,
        val endTime: Calendar,
        val location: String,
        val color: Int,
        val isAllDay: Boolean,
        val isCanceled: Boolean
) : WeekViewDisplayable<Event> {

    override fun toWeekViewEvent(): WeekViewEvent<Event> {
        val style = WeekViewEvent.Style.Builder()
                .setBackgroundColor(color)
                .setTextStrikeThrough(isCanceled)
                .build()

        return WeekViewEvent.Builder<Event>()
                .setId(id)
                .setTitle(title)
                .setStartTime(startTime)
                .setEndTime(endTime)
                .setLocation(location)
                .setAllDay(isAllDay)
                .setStyle(style)
                .setData(this) // This is necessary for onEventClick(data) to work
                .build()
    }

}