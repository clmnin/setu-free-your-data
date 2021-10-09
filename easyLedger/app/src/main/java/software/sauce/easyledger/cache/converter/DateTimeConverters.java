package software.sauce.easyledger.cache.converter;

import androidx.room.TypeConverter;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import software.sauce.easyledger.utils.Constants;

public class DateTimeConverters {

    @TypeConverter
    public static Date stringToDate(String value) {
        DateFormat df = new SimpleDateFormat(Constants.SQLITE_DATE_TIMEFORMAT, Locale.US);
        if (value != null) {
            try {
                return df.parse(value);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @TypeConverter
    public static String dateToString(Date value) {
        DateFormat df = new SimpleDateFormat(Constants.SQLITE_DATE_TIMEFORMAT, Locale.US);
        if (value != null) {
            return df.format(value);
        } else {
            return null;
        }
    }
}
