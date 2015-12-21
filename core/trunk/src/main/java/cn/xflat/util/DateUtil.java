package cn.xflat.util;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Utility class for handling java.util.Date, the java.sql data/time classes and related information
 *
 * @version    $Revision: 1.2 $
 * @since      2.0
 */
public class DateUtil {

    public static Calendar calendar = Calendar.getInstance();
    
    /** Return a Timestamp for right now
     * @return Timestamp for right now
     */
    public static java.sql.Timestamp nowTimestamp() {
        return new java.sql.Timestamp(System.currentTimeMillis());
    }

    /**
     * Return a string formatted as yyyyMMddHHmmss
     * @return String formatted for right now
     */
    public static String nowDateString() {
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
        return df.format(new Date());
    }

    /** Return a Date for right now
     * @return Date for right now
     */
    public static java.util.Date nowDate() {
        return new java.util.Date();
    }
    
    public static int get(Date date, int field) {
        calendar.setTime(date);
        if (field == Calendar.MONTH) {
            return calendar.get(field) + 1;
        }
        else {
            return calendar.get(field);
        }
    }
    
    /**
     * 返回下一个日期的开始(时间为00:00)
     * @param date 基准日期
     * @param skippedDays 跳过天数
     * @return
     */
    public static Date nextDateStart(Date date, int skippedDays) {
        calendar.setTime(date);
        calendar.set(calendar.get(Calendar.YEAR), 
        		calendar.get(Calendar.MONTH), 
        		calendar.get(Calendar.DAY_OF_MONTH), 
        		0, 0, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        calendar.add(Calendar.DAY_OF_MONTH, skippedDays);
        return calendar.getTime();
    }
    
    public static Date nextDateStart(Date date) {
        return nextDateStart(date, 1);
    }
    
    public static Date dateStart(Date date) {
        return nextDateStart(date, 0);
    }
    
    /**
     * 返回下一个日期的最后时刻(时间为23:59)
     * @param date
     * @param skippedDays
     * @return
     */
    public static Date nextDateEnd(Date date, int skippedDays) {
        calendar.setTime(date);
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), 23, 59, 59);
        calendar.set(Calendar.MILLISECOND, 0);
        calendar.add(Calendar.DAY_OF_MONTH, skippedDays);
        return calendar.getTime();
    }
    
    public static Date nextDateEnd(Date date) {
        return nextDateEnd(date, 1);
    }
    
    public static Date dateEnd(Date date) {
        return nextDateEnd(date, 0);
    }
    
    /**
     * 返回指定日期间隔后的日期(时间不变)
     * @param date
     * @param days
     * @return
     */
    public static Date dateAdd(Date date, int days) {
        calendar.setTime(new java.util.Date(date.getTime()));
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
        calendar.add(Calendar.DAY_OF_MONTH, days);
        return calendar.getTime();
    }
    
    public static Date dateAdd(Date date, int field, int val) {
    	calendar.setTime(new java.util.Date(date.getTime()));
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
        calendar.add(field, val);
        return calendar.getTime();
    }
    
    public static void main(String[] args) {
        Timestamp ts = new Timestamp(System.currentTimeMillis());
        System.out.println(">>>date: " + new Date());
        System.out.println(">>>ts: " + ts);
        System.out.println(">>>ts 3 days later: " + get(ts, Calendar.YEAR));
        System.out.println(">>>ts 3 days later: " + get(ts, Calendar.MONTH));
        System.out.println(">>>ts 3 days later: " + get(ts, Calendar.DAY_OF_MONTH));
    }
    
    //------------------------------------------------------------------------------------------

    /**
     * Return the date for the first day of the month
     * @param stamp
     * @return
     */

    public static java.sql.Timestamp getMonthStart(java.sql.Timestamp stamp) {
        return getMonthStart(stamp, 0);
    }

    public static long getMonthStart(long s) {
    	java.sql.Timestamp stamp = new java.sql.Timestamp(s);
        return getMonthStart(stamp, 0).getTime();
    }
    
    public static java.sql.Timestamp getMonthStart(java.sql.Timestamp stamp, int daysLater) {
        Calendar tempCal = Calendar.getInstance();
        tempCal.setTime(new java.util.Date(stamp.getTime()));
        tempCal.set(tempCal.get(Calendar.YEAR), tempCal.get(Calendar.MONTH), 1, 0, 0, 0);
        tempCal.add(Calendar.DAY_OF_MONTH, daysLater);
        java.sql.Timestamp retStamp = new java.sql.Timestamp(tempCal.getTime().getTime());
        retStamp.setNanos(0);
        return retStamp;
    }

    /**
    * Return the date for the last day of the month
    * @param stamp
    * @return
    */
    public static java.sql.Timestamp getMonthEnd(java.sql.Timestamp stamp) {
        Calendar tempCal = Calendar.getInstance();
        tempCal.setTime(new java.util.Date(stamp.getTime()));
        tempCal.set(tempCal.get(Calendar.YEAR), tempCal.get(Calendar.MONTH), 1, 23, 59, 59);
        tempCal.add(Calendar.MONTH, 1);
        tempCal.add(Calendar.DAY_OF_MONTH, -1);
        java.sql.Timestamp retStamp = new java.sql.Timestamp(tempCal.getTime().getTime());
        retStamp.setNanos(999999999);
        return retStamp;
    }
    
    public static long getMonthEnd(long stamp) {
        Calendar tempCal = Calendar.getInstance();
        tempCal.setTime(new java.util.Date(stamp));
        tempCal.set(tempCal.get(Calendar.YEAR), tempCal.get(Calendar.MONTH), 1, 23, 59, 59);
        tempCal.add(Calendar.MONTH, 1);
        tempCal.add(Calendar.DAY_OF_MONTH, -1);
        java.sql.Timestamp retStamp = new java.sql.Timestamp(tempCal.getTime().getTime());
        retStamp.setNanos(999999999);
        return retStamp.getTime();
    }

    public static java.sql.Timestamp getMonthStart(int yyear, int mmonth) {
        Calendar tempCal = Calendar.getInstance();
        tempCal.set(yyear, mmonth-1, 1, 0, 0, 0);
        java.sql.Timestamp retStamp = new java.sql.Timestamp(tempCal.getTime().getTime());
        retStamp.setNanos(0);
        return retStamp;
    }

    public static java.sql.Timestamp getMonthEnd(int yyear, int mmonth) {
        Calendar tempCal = Calendar.getInstance();
        tempCal.set(yyear, mmonth, 1, 23, 59, 59);
        tempCal.add(Calendar.DAY_OF_MONTH, -1);
        java.sql.Timestamp retStamp = new java.sql.Timestamp(tempCal.getTime().getTime());
        retStamp.setNanos(999999999);
        return retStamp;
    }

    /**
     * Return the date for the first day of the week
     * @param stamp
     * @return
     */
    public static java.sql.Timestamp getWeekStart(java.sql.Timestamp stamp) {
        return getWeekStart(stamp, 0);
    }

    public static java.sql.Timestamp getWeekStart(java.sql.Timestamp stamp, int daysLater) {
        Calendar tempCal = Calendar.getInstance();
        tempCal.setTime(new java.util.Date(stamp.getTime()));
        tempCal.set(tempCal.get(Calendar.YEAR), tempCal.get(Calendar.MONTH), tempCal.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
        tempCal.add(Calendar.DAY_OF_MONTH, daysLater);
        tempCal.set(Calendar.DAY_OF_WEEK, tempCal.getFirstDayOfWeek());
        java.sql.Timestamp retStamp = new java.sql.Timestamp(tempCal.getTime().getTime());
        retStamp.setNanos(0);
        return retStamp;
    }

    /** Converts a date String into a java.sql.Date
     * @param date The date String: MM/DD/YYYY
     * @return A java.sql.Date made from the date String
     */
    public static java.sql.Date toSqlDate(String date) {
        java.util.Date newDate = toDate(date, "00:00:00");

        if (newDate != null)
            return new java.sql.Date(newDate.getTime());
        else
            return null;
    }

    /** Makes a java.sql.Date from separate Strings for month, day, year
     * @param monthStr The month String
     * @param dayStr The day String
     * @param yearStr The year String
     * @return A java.sql.Date made from separate Strings for month, day, year
     */
    public static java.sql.Date toSqlDate(String monthStr, String dayStr, String yearStr) {
        java.util.Date newDate = toDate(monthStr, dayStr, yearStr, "0", "0", "0");

        if (newDate != null)
            return new java.sql.Date(newDate.getTime());
        else
            return null;
    }

    /** Makes a java.sql.Date from separate ints for month, day, year
     * @param month The month int
     * @param day The day int
     * @param year The year int
     * @return A java.sql.Date made from separate ints for month, day, year
     */
    public static java.sql.Date toSqlDate(int month, int day, int year) {
        java.util.Date newDate = toDate(month, day, year, 0, 0, 0);

        if (newDate != null)
            return new java.sql.Date(newDate.getTime());
        else
            return null;
    }

    /** Converts a time String into a java.sql.Time
     * @param time The time String: either HH:MM or HH:MM:SS
     * @return A java.sql.Time made from the time String
     */
    public static java.sql.Time toSqlTime(String time) {
        java.util.Date newDate = toDate("1/1/1970", time);

        if (newDate != null)
            return new java.sql.Time(newDate.getTime());
        else
            return null;
    }

    /** Makes a java.sql.Time from separate Strings for hour, minute, and second.
     * @param hourStr The hour String
     * @param minuteStr The minute String
     * @param secondStr The second String
     * @return A java.sql.Time made from separate Strings for hour, minute, and second.
     */
    public static java.sql.Time toSqlTime(String hourStr, String minuteStr, String secondStr) {
        java.util.Date newDate = toDate("0", "0", "0", hourStr, minuteStr, secondStr);

        if (newDate != null)
            return new java.sql.Time(newDate.getTime());
        else
            return null;
    }

    /** Makes a java.sql.Time from separate ints for hour, minute, and second.
     * @param hour The hour int
     * @param minute The minute int
     * @param second The second int
     * @return A java.sql.Time made from separate ints for hour, minute, and second.
     */
    public static java.sql.Time toSqlTime(int hour, int minute, int second) {
        java.util.Date newDate = toDate(0, 0, 0, hour, minute, second);

        if (newDate != null)
            return new java.sql.Time(newDate.getTime());
        else
            return null;
    }

    /** Converts a date and time String into a Timestamp
     * @param dateTime A combined data and time string in the format "MM/DD/YYYY HH:MM:SS", the seconds are optional
     * @return The corresponding Timestamp
     */
    public static java.sql.Timestamp toTimestamp(String dateTime) {
        java.util.Date newDate = toDate(dateTime);
        if (newDate != null)
            return new java.sql.Timestamp(newDate.getTime());
        else
            return null;
    }

    public static java.sql.Timestamp toTimestampCST(String dateTime) {
        java.util.Date newDate = toDateCST(dateTime);
        if (newDate != null)
            return new java.sql.Timestamp(newDate.getTime());
        else
            return null;
    }
    
    /** Converts a date String and a time String into a Timestamp
     * @param date The date String: MM/DD/YYYY
     * @param time The time String: either HH:MM or HH:MM:SS
     * @return A Timestamp made from the date and time Strings
     */
    public static java.sql.Timestamp toTimestamp(String date, String time) {
        java.util.Date newDate = toDate(date, time);

        if (newDate != null)
            return new java.sql.Timestamp(newDate.getTime());
        else
            return null;
    }

    /** Makes a Timestamp from separate Strings for month, day, year, hour, minute, and second.
     * @param monthStr The month String
     * @param dayStr The day String
     * @param yearStr The year String
     * @param hourStr The hour String
     * @param minuteStr The minute String
     * @param secondStr The second String
     * @return A Timestamp made from separate Strings for month, day, year, hour, minute, and second.
     */
    public static java.sql.Timestamp toTimestamp(String monthStr, String dayStr, String yearStr, String hourStr,
        String minuteStr, String secondStr) {
        java.util.Date newDate = toDate(monthStr, dayStr, yearStr, hourStr, minuteStr, secondStr);

        if (newDate != null)
            return new java.sql.Timestamp(newDate.getTime());
        else
            return null;
    }

    /** Makes a Timestamp from separate ints for month, day, year, hour, minute, and second.
     * @param month The month int
     * @param day The day int
     * @param year The year int
     * @param hour The hour int
     * @param minute The minute int
     * @param second The second int
     * @return A Timestamp made from separate ints for month, day, year, hour, minute, and second.
     */
    public static java.sql.Timestamp toTimestamp(int month, int day, int year, int hour, int minute, int second) {
        java.util.Date newDate = toDate(month, day, year, hour, minute, second);

        if (newDate != null)
            return new java.sql.Timestamp(newDate.getTime());
        else
            return null;
    }

    /** Converts a date and time String into a Date
     * @param dateTime A combined data and time string in the format "MM/DD/YYYY HH:MM:SS", the seconds are optional
     * @return The corresponding Date
     */
    public static java.util.Date toDate(String dateTime) {
        return toDate(dateTime, 0);
    }
    
    public static java.util.Date toDateCST(String dateTime) {
    	//Sat Dec 19 14:03:27 CST 2015
    	SimpleDateFormat  cstFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.US);
    	try {
	    	Date d = cstFormat.parse(dateTime);
	    	return d;
    	} catch (Exception ex) {
    		return null;
    	}
    }
    
    /**
     * 
     * @param dateTime
     * @param sep -- 0 ：'/'分隔日期, 1: '-' 分隔日期 
     * @return
     */
    public static java.util.Date toDate(String dateTime, int sep) {
        // dateTime must have one space between the date and time...
    	// dateTime must have one space between the date and time...
    	int pos = dateTime.indexOf(" ");
    	String date = dateTime;
    	String time = "00:00";
    	if (pos > 0) { 
    		date = dateTime.substring(0, pos);
    		time = dateTime.substring(pos + 1);
    	}
        return toDate(date, time, sep);
        /*
        String date = dateTime.substring(0, dateTime.indexOf(" "));
        String time = dateTime.substring(dateTime.indexOf(" ") + 1);

        return toDate(date, time);*/
    }
    
    /**
     * 
     * @param date
     * @param time
     * @param sep -- 0 ：'/'分隔日期, 1: '-' 分隔日期
     * @return
     */
    public static java.util.Date toDate(String date, String time) {
    	return toDate(date, time, 0);
    }
    
    /** Converts a date String and a time String into a Date
     * @param date The date String: MM/DD/YYYY
     * @param time The time String: either HH:MM or HH:MM:SS
     * @return A Date made from the date and time Strings
     */
    public static java.util.Date toDate(String date, String time, int sep) {
    	
        if (date == null || time == null) return null;
        String month;
        String day;
        String year;
        String hour;
        String minute;
        String second;

        String sepStr = "/";
        if (sep == 1)
        	sepStr = "-";
        int dateSlash1 = date.indexOf(sepStr);
        int dateSlash2 = date.lastIndexOf(sepStr);

        if (dateSlash1 <= 0 || dateSlash1 == dateSlash2) return null;
        int timeColon1 = time.indexOf(":");
        int timeColon2 = time.lastIndexOf(":");

        if (timeColon1 <= 0) return null;
        
        if (sep == 1) {
        	year = date.substring(0, dateSlash1);
	        month = date.substring(dateSlash1 + 1, dateSlash2);
	        day = date.substring(dateSlash2 + 1);
        } else {
	        month = date.substring(0, dateSlash1);
	        day = date.substring(dateSlash1 + 1, dateSlash2);
	        year = date.substring(dateSlash2 + 1);
        }
        
        hour = time.substring(0, timeColon1);

        if (timeColon1 == timeColon2) {
            minute = time.substring(timeColon1 + 1);
            second = "0";
        } else {
            minute = time.substring(timeColon1 + 1, timeColon2);
            second = time.substring(timeColon2 + 1);
        }

        return toDate(month, day, year, hour, minute, second);
    }

    /** Makes a Date from separate Strings for month, day, year, hour, minute, and second.
     * @param monthStr The month String
     * @param dayStr The day String
     * @param yearStr The year String
     * @param hourStr The hour String
     * @param minuteStr The minute String
     * @param secondStr The second String
     * @return A Date made from separate Strings for month, day, year, hour, minute, and second.
     */
    public static java.util.Date toDate(String monthStr, String dayStr, String yearStr, String hourStr,
        String minuteStr, String secondStr) {
        int month, day, year, hour, minute, second;

        try {
            month = Integer.parseInt(monthStr);
            day = Integer.parseInt(dayStr);
            year = Integer.parseInt(yearStr);
            hour = Integer.parseInt(hourStr);
            minute = Integer.parseInt(minuteStr);
            second = Integer.parseInt(secondStr);
        } catch (Exception e) {
            return null;
        }
        return toDate(month, day, year, hour, minute, second);
    }

    /** Makes a Date from separate ints for month, day, year, hour, minute, and second.
     * @param month The month int
     * @param day The day int
     * @param year The year int
     * @param hour The hour int
     * @param minute The minute int
     * @param second The second int
     * @return A Date made from separate ints for month, day, year, hour, minute, and second.
     */
    public static java.util.Date toDate(int month, int day, int year, int hour, int minute, int second) {
        Calendar calendar = Calendar.getInstance();

        try {
            calendar.set(year, month - 1, day, hour, minute, second);
            calendar.set(Calendar.MILLISECOND, 0);
        } catch (Exception e) {
            return null;
        }
        return new java.util.Date(calendar.getTime().getTime());
    }

    /** Makes a date String in the format MM/DD/YYYY from a Date
     * @param date The Date
     * @return A date String in the format MM/DD/YYYY
     */
    public static String toDateString(java.util.Date date) {
        if (date == null) return "";
        Calendar calendar = Calendar.getInstance();

        calendar.setTime(date);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int year = calendar.get(Calendar.YEAR);
        String monthStr;
        String dayStr;
        String yearStr;

        if (month < 10) {
            monthStr = "0" + month;
        } else {
            monthStr = "" + month;
        }
        if (day < 10) {
            dayStr = "0" + day;
        } else {
            dayStr = "" + day;
        }
        yearStr = "" + year;
        return monthStr + "/" + dayStr + "/" + yearStr;
    }

    /** Makes a time String in the format HH:MM:SS from a Date. If the seconds are 0, then the output is in HH:MM.
     * @param date The Date
     * @return A time String in the format HH:MM:SS or HH:MM
     */
    public static String toTimeString(java.util.Date date) {
        if (date == null) return "";
        Calendar calendar = Calendar.getInstance();

        calendar.setTime(date);
        return (toTimeString(calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), calendar.get(Calendar.SECOND)));
    }

    /** Makes a time String in the format HH:MM:SS from a separate ints for hour, minute, and second. If the seconds are 0, then the output is in HH:MM.
     * @param hour The hour int
     * @param minute The minute int
     * @param second The second int
     * @return A time String in the format HH:MM:SS or HH:MM
     */
    public static String toTimeString(int hour, int minute, int second) {
        String hourStr;
        String minuteStr;
        String secondStr;

        if (hour < 10) {
            hourStr = "0" + hour;
        } else {
            hourStr = "" + hour;
        }
        if (minute < 10) {
            minuteStr = "0" + minute;
        } else {
            minuteStr = "" + minute;
        }
        if (second < 10) {
            secondStr = "0" + second;
        } else {
            secondStr = "" + second;
        }
        if (second == 0)
            return hourStr + ":" + minuteStr;
        else
            return hourStr + ":" + minuteStr + ":" + secondStr;
    }

    /** Makes a combined data and time string in the format "MM/DD/YYYY HH:MM:SS" from a Date. If the seconds are 0 they are left off.
     * @param date The Date
     * @return A combined data and time string in the format "MM/DD/YYYY HH:MM:SS" where the seconds are left off if they are 0.
     */
    public static String toDateTimeString(java.util.Date date) {
        if (date == null) return "";
        String dateString = toDateString(date);
        String timeString = toTimeString(date);

        if (dateString != null && timeString != null)
            return dateString + " " + timeString;
        else
            return "";
    }

    /** Makes a Timestamp for the beginning of the month
     * @return A Timestamp of the beginning of the month
     */
    public static java.sql.Timestamp monthBegin() {
        Calendar mth = Calendar.getInstance();

        mth.set(Calendar.DAY_OF_MONTH, 1);
        mth.set(Calendar.HOUR_OF_DAY, 0);
        mth.set(Calendar.MINUTE, 0);
        mth.set(Calendar.SECOND, 0);
        mth.set(Calendar.AM_PM, Calendar.AM);
        return new java.sql.Timestamp(mth.getTime().getTime());
    }

    //--------------------------------------------------------------------------
    //--------------------------------------------------------------------------
    public static java.sql.Date toSqlDate(java.sql.Timestamp stamp) {
        if (stamp != null) {
            return new java.sql.Date(stamp.getTime());
        } else {
            return null;
        }
    }

    public static java.sql.Timestamp toTimestamp(java.util.Date date) {
        if (date != null) {
            return new java.sql.Timestamp(date.getTime());
        } else {
            return null;
        }
    }
    
    public static String formatDate(Date date) {
    	return formatDate(date, "yyyy-MM-dd HH:mm");
    }
    
    /**
     *  
     * @param date
     * @param format 如: yyyy-MM-dd HH:mm:ss,  yyyy-MM-dd hh:mm:ss
     * @return
     */
    public static String formatDate(Date date, String format) {
    	SimpleDateFormat df = new SimpleDateFormat(format);
        return df.format(date);
    }
    
    //----------------------------------------------------------
    public static Date parseDateVal(String key, String s) {
    	Date val = null;
		int len = s.length();
		if (s.startsWith("$Date{")) {
			long v = Long.valueOf(s.substring(6, len-1));
			java.util.Date d = new java.util.Date(v);
			if (key.endsWith("_ge") || key.endsWith("_lt")) {
				val = DateUtil.dateStart(d);
			} else if (key.endsWith("_gt") || key.endsWith("_le")) {
				val = DateUtil.dateEnd(d);
			}
		}
		else if (s.startsWith("$Timestamp{")) {
			long v = Long.valueOf(s.substring(11, len-1));
			val = new java.sql.Timestamp(v);
		}else if(s.startsWith("$Datetime{")) {
			long v = Long.valueOf(s.substring(10, len-1));
			val = new java.sql.Timestamp(v);
		}
		return val;
    }
}
