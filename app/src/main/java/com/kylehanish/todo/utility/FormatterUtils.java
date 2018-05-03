package com.kylehanish.todo.utility;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Kyle Hanish on 4/30/18.
 */

public class FormatterUtils {

    public static final String DATE_TIME_FORMAT_CONVERSION = "yyyy-mm-dd hh:mm:ss";
    public static DateFormat dateFormatter = new SimpleDateFormat();

    public static Date TryParseStringToDate(String formattedDateString){
        try{
            return dateFormatter.parse(formattedDateString);
        }catch(Exception e){
        }
        return null;
    }

    public static String GetFormattedDate(Date date){
        return dateFormatter.format(date);
    }

}
