package com.nest.common.util;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component("FormatDate")
public class FormatDate {
    public String formatDate(LocalDateTime dateTime){
        if(dateTime == null){
            return "날짜없음";
        }
        return dateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }
}
