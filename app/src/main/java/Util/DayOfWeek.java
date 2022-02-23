package Util;


import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DayOfWeek {
    //day
    public final String MONDAY = "MONDAY";
    public final String TUESDAY = "TUESDAY";
    public final String WEDNESDAY = "WEDNESDAY";
    public final String THURSDAY = "THURSDAY";
    public final String FRIDAY = "FRIDAY";
    public final String SATURDAY = "SATURDAY";
    public final String SUNDAY = "SUNDAY";
    //

    public String getFullDayName(int day) {
        Calendar c = Calendar.getInstance();
        // date doesn't matter - it has to be a Monday
        // I new that first August 2011 is one ;-)
        c.set(2011, 7, 1, 0, 0, 0);
        c.add(Calendar.DAY_OF_MONTH, day);
        return String.format("%tA", c);
    }

    public String getShortDayName(int day) {
        Calendar c = Calendar.getInstance();
        c.set(2011, 7, 1, 0, 0, 0);
        c.add(Calendar.DAY_OF_MONTH, day);
        return String.format("%ta", c);
    }

    public String getCurrentDay() {
        //in onStart()
        Calendar calendar = Calendar.getInstance();
        //date format is:  "Date-Month-Year Hour:Minutes am/pm"
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy HH:mm a"); //Date and time
        String currentDate = sdf.format(calendar.getTime());
        return currentDate;
    }

    public String getCurrentDayByName() {
        //Day of Name in full form like,"Saturday", or if you need the first three characters you have to put "EEE" in the date format and your result will be "Sat".
        SimpleDateFormat sdf_ = new SimpleDateFormat("EEEE");
        Date date = new Date();
        String dayName = sdf_.format(date);
        return dayName;
    }

    public String getCurrentTime() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("HH a"); //Date and time
        String currentTime = sdf.format(calendar.getTime());
        return currentTime;
    }

    public String getCurrentTimeAdv() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd G 'at' HH:mm:ss z"); //Date and time
        String currentTime = sdf.format(calendar.getTime());
        return currentTime;
    }
}
