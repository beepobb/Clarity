package com.example.clarity.model.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * This class provides methods to format items for card view to be used in adapters.
 */
public class CardFormatter {
    private static final String[] monthMap = {"Jan","Feb","Mar","Apr","May","Jun","July",
    "Aug","Sep","Oct","Nov","Dec"};
    public enum EventCardType {DISCOVER, AGENDA_VIEW}

    /**
     *  Formats the original post title such that it can fit in the card view
     * @param fullTitle original post title
     * @return shortened post title
     */
    public static String formatTitleDiscover(String fullTitle, EventCardType eventCardType) {
        int LENGTH = 0;
        if (eventCardType == EventCardType.DISCOVER){
            LENGTH = 22;
        } else if (eventCardType == EventCardType.AGENDA_VIEW) {
            LENGTH = 20;
        }
        int str_len = fullTitle.length();
        if (str_len > LENGTH) {
            // reformat string
            String subString = fullTitle.substring(0, LENGTH);
            return subString + "...";
        }
        return fullTitle;
    }

    /**
     * Formats start and end time for event
     * @param eventStart format: yyyy-MM-dd HH:mm:ss
     * @param eventEnd format: yyyy-MM-dd HH:mm:ss
     * @return time in start - end time, format HH:mm - HH:mm
     */
    public static String formatTime(String eventStart, String eventEnd) {
        String startTime = getTime(eventStart);
        String endTime = getTime(eventEnd);
        return startTime + " - " + endTime;
    }

    /**
     * Formats the full date time string
     * @param eventStart format: yyyy-MM-dd HH:mm:ss
     * @param eventEnd format: yyyy-MM-dd HH:mm:ss
     * @return date in dd/MM/yy
     */
    public static String formatDate(String eventStart, String eventEnd) {
        String startDate = getDate(eventStart);
        String endDate = getDate(eventEnd);

        if (startDate.equals(endDate)) {
            return startDate;
        } else {
            return startDate + " - " + endDate;
        }
    }

    /**
     * Formats time from date time string
     * @param fullDateTime format: yyyy-MM-dd HH:mm:ss
     * @return time in format HH:mm
     */
    private static String getTime(String fullDateTime) {
        return fullDateTime.substring(11,16); // gets HH:mm part of time
    }

    /**
     * Formats the full date time string
     * @param fullDateTime format: yyyy-MM-dd HH:mm:ss
     * @return date in dd/MM/yy
     */
    public static String getDate(String fullDateTime) {
        String dateOnly = fullDateTime.substring(0, 10);
        String[] tmpDateOnly = dateOnly.split("-", 3); // split limit-1 = 2 times
        String year = tmpDateOnly[0];
        String month = tmpDateOnly[1];
        String day = tmpDateOnly[2];
        return day+"/"+month+"/"+year;
    }

    // Formatting Calendar object:
    public static String formatCalendarObject(Calendar eventStart, Calendar eventEnd) {
        return formatCalendarObject(eventStart, eventEnd, false);
    }

    /**
     * Turn start and end Calendar objects into a presentable String
     * @param eventStart
     * @param eventEnd
     * @param retainDate
     * @return formatted String used in Calendar fragment
     */
    public static String formatCalendarObject(Calendar eventStart, Calendar eventEnd, boolean retainDate) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");

        String startTime = dateFormat.format(eventStart.getTime());
        String endTime = dateFormat.format(eventEnd.getTime());
        String startDay = String.valueOf(eventStart.get(Calendar.DAY_OF_MONTH));
        String endDay = String.valueOf(eventEnd.get(Calendar.DAY_OF_MONTH));
        String startMonth = monthMap[eventStart.get(Calendar.MONTH)];
        String endMonth = monthMap[eventEnd.get(Calendar.MONTH)];

        if (!retainDate && startDay.equals(endDay) && startMonth.equals(endMonth)){
            return startTime + " - " + endTime;
        } else {
            return (startDay+" "+startMonth+", "+startTime)+" - "+(endDay+" "+endMonth+", "+endTime);
        }
    }
}
