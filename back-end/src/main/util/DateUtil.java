package main.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author Stanislav Valov <Stanislav.Valov@experian.com>
 */
public class DateUtil {

    public static String formatToString(Date date) {
        if (date == null) {
            return "";
        }

        DateFormat format = new SimpleDateFormat("mm-dd-yyyy");

        return format.format(date);
    }

    public static Date formatToDate(String date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-mm-dd");
        Date dateOfBirth;

        try {
            dateOfBirth = format.parse(date);
        } catch (Exception e) {
            return new Date();
        }

        return dateOfBirth;
    }
}
