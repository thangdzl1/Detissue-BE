package com.DIY.Detissue.utils;

import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Component
public class DateHelper {
    public Date getInternetTime() {
        try {
            LocalDateTime localDateTime = LocalDateTime.now(ZoneId.of("GMT+07:00"));
            Date date = Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
            return date;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    public Date convertStringToDate(String dateInString) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        try {
            Date date = formatter.parse(dateInString);
            return date;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
