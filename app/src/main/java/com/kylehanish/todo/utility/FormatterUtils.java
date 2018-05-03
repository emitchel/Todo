package com.kylehanish.todo.utility;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Kyle Hanish on 4/30/18.
 */

public class FormatterUtils {

    public static final String DATE_TIME_FORMAT_CONVERSION = "yyyy-MM-dd HH:mm:ss";
    public static final String PRETTY_DATE_TIME_FORMAT_CONVERSION = "EEEE, MMMM d, yyyy 'at' hh:mm a";
    public static DateFormat sqlDateFormatter = new SimpleDateFormat(DATE_TIME_FORMAT_CONVERSION,Locale.US);
    public static DateFormat outputDateFormatter = new SimpleDateFormat(PRETTY_DATE_TIME_FORMAT_CONVERSION,Locale.US);

    public static Date TryParseStringToDate(String formattedDateString){
        try{
            return sqlDateFormatter.parse(formattedDateString);
        }catch(Exception e){
        }
        return null;
    }

    public static String GetSQLFormattedDate(Date date){
        return sqlDateFormatter.format(date);
    }

    public static String GetDisplayDateFormat(Date date){
            return outputDateFormatter.format(date);
    }

}
