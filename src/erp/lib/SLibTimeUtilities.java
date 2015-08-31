/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.lib;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;

/**
 *
 * @author Sergio Flores
 */
public abstract class SLibTimeUtilities {

    public static final TimeZone SysTimeZone = TimeZone.getTimeZone("GMT-06:00");
    public static final GregorianCalendar SysCalendar = new GregorianCalendar(SysTimeZone);

    public static int getFirstDayOfWeek(Locale locale) {
        return new GregorianCalendar(locale).getFirstDayOfWeek();
    }

    public static int getFirstDayOfWeekStd() {
        return 1;
    }

    public static String[] createDaysOfWeek(Locale locale, int calendarStyle) {
        int day = 0;
        String name = "";
        String[] names = new String[7];
        GregorianCalendar gc = new GregorianCalendar(locale);
        Set<Map.Entry<String, Integer>> set = gc.getDisplayNames(Calendar.DAY_OF_WEEK, calendarStyle, locale).entrySet();
        Object[] array = set.toArray();

        day = gc.getFirstDayOfWeek();
        for (int i = 0; i < names.length; i++) {
            name = "";
            for (Object entry : array) {
                if (day == (Integer) ((Map.Entry) entry).getValue()) {
                    name = (String) ((Map.Entry) entry).getKey();
                    break;
                }
            }

            names[i] = name;
            if (++day > names.length) {
                day = 1;
            }
        }

        return names;
    }

    public static String[] createDaysOfWeekStd(int calendarStyle) {
        String name = "";
        String[] names = new String[7];
        GregorianCalendar gc = new GregorianCalendar();
        Set<Map.Entry<String, Integer>> set = gc.getDisplayNames(Calendar.DAY_OF_WEEK, calendarStyle, Locale.getDefault()).entrySet();
        Object[] array = set.toArray();

        for (int i = 1; i <= names.length; i++) {
            name = "";
            for (Object entry : array) {
                if (i == (Integer) ((Map.Entry) entry).getValue()) {
                    name = (String) ((Map.Entry) entry).getKey();
                    break;
                }
            }

            names[i - 1] = name;
        }

        return names;
    }

    public static String[] createMonthsOfYear(Locale locale, int calendarStyle) {
        String name = "";
        String[] names = new String[12];
        GregorianCalendar calendar = new GregorianCalendar(locale);
        Set<Map.Entry<String, Integer>> set = calendar.getDisplayNames(Calendar.MONTH, calendarStyle, locale).entrySet();
        Object[] array = set.toArray();

        for (int i = 0; i < names.length; i++) {
            name = "";
            for (Object entry : array) {
                if (i == (Integer) ((Map.Entry) entry).getValue()) {
                    name = (String) ((Map.Entry) entry).getKey();
                    break;
                }
            }

            names[i] = name;
        }

        return names;
    }

    /*
     * Convert to Date Only method
     */

    public static java.util.Date convertToDateOnly(java.util.Date datetime) {
        int[] date = null;
        java.util.GregorianCalendar calendar = new GregorianCalendar();

        calendar.setTime(datetime);
        date = new int[] { calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.DATE) };

        return createDate(date[0], date[1], date[2]);
    }

    /*
     * Create Date methods
     */

    public static java.util.Date createDate(int year) {
        java.util.GregorianCalendar calendar = new GregorianCalendar();

        calendar.clear();
        calendar.set(year, 0, 1, 0, 0, 0);
        return calendar.getTime();
    }

    public static java.util.Date createDate(int year, int month) {
        java.util.GregorianCalendar calendar = new GregorianCalendar();

        calendar.clear();
        calendar.set(year, month - 1, 1, 0, 0, 0);
        return calendar.getTime();
    }

    public static java.util.Date createDate(int year, int month, int day) {
        java.util.GregorianCalendar calendar = new GregorianCalendar();

        calendar.clear();
        calendar.set(year, month - 1, day, 0, 0, 0);
        return calendar.getTime();
    }

    /*
     * Digest Date methods
     */

    public static int[] digestDate(java.util.Date date) {
        java.util.GregorianCalendar calendar = new GregorianCalendar();

        calendar.setTime(convertToDateOnly(date));
        return new int[] { calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.DATE) };
    }

    public static int[] digestYear(java.util.Date date) {
        java.util.GregorianCalendar calendar = new GregorianCalendar();

        calendar.setTime(convertToDateOnly(date));
        return new int[] { calendar.get(Calendar.YEAR) };
    }

    public static int[] digestYearMonth(java.util.Date date) {
        java.util.GregorianCalendar calendar = new GregorianCalendar();

        calendar.setTime(convertToDateOnly(date));
        return new int[] { calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1 };
    }

    /*
     * Get BOM, EOM, BOY and EOY methods
     */

    public static int getMaxDayOfMonth(java.util.Date date) {
        java.util.GregorianCalendar calendar = new GregorianCalendar();

        calendar.setTime(convertToDateOnly(date));
        return calendar.getActualMaximum(Calendar.DATE);
    }

    public static java.util.Date getBeginOfMonth(java.util.Date date) {
        java.util.GregorianCalendar calendar = new GregorianCalendar();

        calendar.setTime(convertToDateOnly(date));
        calendar.set(Calendar.DATE, 1);
        return calendar.getTime();
    }

    public static java.util.Date getEndOfMonth(java.util.Date date) {
        java.util.GregorianCalendar calendar = new GregorianCalendar();

        calendar.setTime(convertToDateOnly(date));
        calendar.set(Calendar.DATE, getMaxDayOfMonth(convertToDateOnly(date)));
        return calendar.getTime();
    }

    public static java.util.Date getBeginOfYear(java.util.Date date) {
        java.util.GregorianCalendar calendar = new GregorianCalendar();

        calendar.setTime(convertToDateOnly(date));
        calendar.set(Calendar.MONTH, 0);
        calendar.set(Calendar.DATE, 1);
        return calendar.getTime();
    }

    public static java.util.Date getEndOfYear(java.util.Date date) {
        java.util.GregorianCalendar calendar = new GregorianCalendar();

        calendar.setTime(convertToDateOnly(date));
        calendar.set(Calendar.MONTH, 11);
        calendar.set(Calendar.DATE, 31);
        return calendar.getTime();
    }

    /*
     * Week methods
     */

    public static int getWeekOfMonth(java.util.Date date) {
        java.util.GregorianCalendar calendar = new GregorianCalendar();

        calendar.setTime(convertToDateOnly(date));
        return calendar.get(Calendar.WEEK_OF_MONTH);
    }

    public static int getWeekOfYear(java.util.Date date) {
        java.util.GregorianCalendar calendar = new GregorianCalendar();

        calendar.setTime(convertToDateOnly(date));
        return calendar.get(Calendar.WEEK_OF_YEAR);
    }

    public static java.util.Date[] getWeekDateRange(int year, int week) {
        java.util.GregorianCalendar calendar = new GregorianCalendar();
        calendar.getMinimalDaysInFirstWeek();

        return null;
    }

    /*
     * Check if Date belongs to period methods
     */

    public static boolean isBelongingToPeriod(java.util.Date date, java.util.Date dateStart, java.util.Date dateEnd) {
        java.util.Date curDate = convertToDateOnly(date);

        return !(curDate.before(convertToDateOnly(dateStart)) || curDate.after(convertToDateOnly(dateEnd)));
    }

    public static boolean isBelongingToPeriod(java.util.Date date, int year, int period) {
        java.util.Date aux = createDate(year, period);
        java.util.Date auxBom = getBeginOfMonth(aux);
        java.util.Date auxEom = getEndOfMonth(aux);
        java.util.Date curDate = convertToDateOnly(date);

        return curDate.getTime() >= auxBom.getTime() && curDate.getTime() <= auxEom.getTime();
    }

    /*
     * Miscellaneous Date methods
     */

    public static java.util.Date addDate(java.util.Date date, int addYear, int addMonth, int addDay) {
        java.util.GregorianCalendar calendar = new GregorianCalendar();

        calendar.setTime(convertToDateOnly(date));
        calendar.add(Calendar.YEAR, addYear);
        calendar.add(Calendar.MONTH, addMonth);
        calendar.add(Calendar.DATE, addDay);
        return calendar.getTime();
    }

    public static long getDaysDiff(java.util.Date newer, java.util.Date older) {
        return (newer.getTime() - older.getTime()) / (1000L * 60L * 60L * 24L);
    }
}
