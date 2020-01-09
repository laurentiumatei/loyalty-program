package com.exercise.loyalty.helper;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

public class DateTimeHelper {

    public static LocalDateTime convertToLocalDateTime(Date dateToConvert) {
        return dateToConvert.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();
    }
    
    public static Date getStartOfDayDaysAgo(int daysAgo) {
        LocalDateTime beginningOfDay = LocalDate.now().minusDays(daysAgo).atStartOfDay();
        return Date.from(beginningOfDay.atZone(ZoneId.systemDefault()).toInstant());
    }
}
