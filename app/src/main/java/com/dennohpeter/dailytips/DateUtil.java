package com.dennohpeter.dailytips;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DateUtil {

    public String getCurrentDate(){
        Calendar calendar = Calendar.getInstance();
        return  formatDate(calendar.getTime(), "yyyy-MM-dd");
    }

    public String formatDate(Date date, String pattern){
        SimpleDateFormat format = new SimpleDateFormat(pattern, Locale.getDefault());

        return format.format(date);
    }
}
