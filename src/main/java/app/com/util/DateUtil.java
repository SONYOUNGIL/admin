package app.com.util;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

import org.springframework.util.StringUtils;

public class DateUtil {

    /**
     * 주어진 날짜 달의 마지막 일자 구하기
     * @param dateString - 일자(YYYYMMDD)
     * @return String - 말일
     * @throws Exception
     */
    public static String lastDayOfMonth(String dateString) throws Exception
    {
        return lastDayOfMonth(dateString, "yyyyMMdd");
    }

    /**
     * 주어진 날짜 달의 마지막 일자 구하기
     * @param dateString - 일자
     * @param format - 날짜 형식
     * @return String - 말일
     * @throws Exception
     */
    public static String lastDayOfMonth(String dateString, String format) throws Exception
    {
        SimpleDateFormat formatter = new SimpleDateFormat(format, Locale.KOREA);
        Date date = formatter.parse(dateString);

        SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy", Locale.KOREA);
        SimpleDateFormat monthFormat = new SimpleDateFormat("MM", Locale.KOREA);

        int year = Integer.parseInt(yearFormat.format(date));
        int month = Integer.parseInt(monthFormat.format(date));
        int day = lastDay(year, month);

        DecimalFormat fourDf = new DecimalFormat("0000");
        DecimalFormat twoDf = new DecimalFormat("00");
        String tempDate = String.valueOf(fourDf.format(year)) + String.valueOf(twoDf.format(month))
                + String.valueOf(twoDf.format(day));
        date = formatter.parse(tempDate);

        return formatter.format(date);
    }

    /**
     * 주어진 년 월의 마지막 일자(월말) 구하기
     * @param year - 연도
     * @param month - 월
     * @return int - 말일
     * @throws Exception
     */
    public static int lastDay(int year, int month) throws ParseException
    {
        int day = 0;
        switch (month)
        {
            case 1:
            case 3:
            case 5:
            case 7:
            case 8:
            case 10:
            case 12:
                day = 31;
                break;
            case 2:
                if ((year % 4) == 0)
                {
                    if ((year % 100) == 0 && (year % 400) != 0)
                    {
                        day = 28;
                    }
                    else
                    {
                        day = 29;
                    }
                }
                else
                {
                    day = 28;
                }
                break;
            default:
                day = 30;
        }
        return day;
    }

    /**
     * 날짜체크
     * @param dateString - 날짜(YYYYMMDD)
     * @return true - 날짜 형식이 맞고, 존재하는 날짜일 때, false - 날짜 형식이 맞지 않거나, 존재하지 않는 날짜일 때
     * @throws Exception
     */
    public static boolean isValid(String dateString) throws Exception
    {
        return isValid(dateString, "yyyyMMdd");
    }

    /**
     * 날짜체크
     * @param dateString - 날짜
     * @param format - 날짜 형식
     * @return boolean - true: 날짜 형식이 맞고 존재하는 날짜일 때, false: 날짜 형식이 맞지 않거나 존재하지 않는 날짜일 때
     * @throws Exception
     */
    public static boolean isValid(String dateString, String format) throws Exception
    {
        SimpleDateFormat formatter = new SimpleDateFormat(format, Locale.KOREA);
        Date date = formatter.parse(dateString);

        if (!formatter.format(date).equals(dateString))
            return false;

        return true;
    }

    /**
     * 주어진 날짜의 년수를 addYear 만큼 증가 또는 감소 시킴
     * @param dateString - 날짜(YYYYMMDD)
     * @param addYear - 더할 년의 수 (증가시 +, 감소시 -)
     * @return String
     * @throws Exception
     */
    public static String addYear(String dateString, int addYear) throws Exception
    {
        return addYear(dateString, addYear, "yyyyMMdd");
    }

    /**
     * 주어진 날짜의 년수를 addYear 만큼 증가 또는 감소 시킴
     * @param dateString - 날짜 데이터
     * @param addYear - 더할 년의 수 (증가시 +, 감소시 -)
     * @param format - 날짜 형식
     * @return String
     */
    public static String addYear(String dateString, int addYear, String format)
    {
        if (dateString == null)
        {
            return null;
        }
        int year = 0;
        int month = 2;
        int day = 1;

        if (dateString.length() == 10)
        {
            //format = "yyyy-MM-dd";
            year = Integer.parseInt(dateString.substring(0, 4));
            month = Integer.parseInt(dateString.substring(4, 6));
            day = Integer.parseInt(dateString.substring(6));
        }
        if (dateString.length() == 8)
        {
            //format = "yyyyMMdd";
            year = Integer.parseInt(dateString.substring(0, 4));
            month = Integer.parseInt(dateString.substring(4, 6));
            day = Integer.parseInt(dateString.substring(6));
        }
        else if (dateString.length() == 7)
        {
            //format = "yyyy-MM";
            year = Integer.parseInt(dateString.substring(0, 4));
            month = Integer.parseInt(dateString.substring(5));
        }
        else if (dateString.length() == 6)
        {
            //format = "yyyyMM";
            year = Integer.parseInt(dateString.substring(0, 4));
            month = Integer.parseInt(dateString.substring(4));
        }
        else if (dateString.length() == 4)
        {
            //format = "yyyy";
            year = Integer.parseInt(dateString.substring(0, 4));
        }
        else
        {
            return null;
        }
        
        GregorianCalendar cal = new GregorianCalendar();
        
        //MONTH 는 0 ~ 11 이므로 실제 데이터의 마이너스 1
        cal.set(year, month - 1, day);
        cal.add(GregorianCalendar.YEAR, addYear);

        SimpleDateFormat formatter = new SimpleDateFormat(format);
        return formatter.format(cal.getTime());
    }

    /**
     * 주어진 날짜 DATE 의 달수를 addCount 만큼 증가 또는 감소 시킴
     * @param dateString - 8자리(YYYYMMDD) 또는 6자리(YYYYMM)의 날짜 데이터
     * @param addYear - 더할 달의 수 (증가시 +, 감소시 -)
     * @return String
     * @throws Exception
     */
    public static String addMonth(String dateString, int addMonth) throws Exception
    {
        return addMonth(dateString, addMonth, "yyyyMMdd");
    }

    /**
     * 주어진 날짜의 달수를 addMonth 만큼 증가 또는 감소 시킴
     * @param dateString - 날짜 데이터
     * @param addMonth - 더할 달의 수 (증가시 +, 감소시 -)
     * @param format - 날짜 형식
     * @return String
     */
    public static String addMonth(String dateString, int addMonth, String format)
    {
        if (dateString == null)
        {
            return null;
        }
        int year = 0;
        int month = 2;
        int day = 1;

        if (dateString.length() == 10)
        {
            //format = "yyyy-MM-dd";
            year = Integer.parseInt(dateString.substring(0, 4));
            month = Integer.parseInt(dateString.substring(4, 6));
            day = Integer.parseInt(dateString.substring(6));
        }
        if (dateString.length() == 8)
        {
            //format = "yyyyMMdd";
            year = Integer.parseInt(dateString.substring(0, 4));
            month = Integer.parseInt(dateString.substring(4, 6));
            day = Integer.parseInt(dateString.substring(6));
        }
        else if (dateString.length() == 7)
        {
            //format = "yyyy-MM";
            year = Integer.parseInt(dateString.substring(0, 4));
            month = Integer.parseInt(dateString.substring(5));
        }
        else if (dateString.length() == 6)
        {
            //format = "yyyyMM";
            year = Integer.parseInt(dateString.substring(0, 4));
            month = Integer.parseInt(dateString.substring(4));
        }
        else
        {
            return null;
        }
        
        GregorianCalendar cal = new GregorianCalendar();

        //MONTH 는 0 ~ 11 이므로 실제 데이터의 마이너스 1
        cal.set(year, month - 1, day);
        cal.add(GregorianCalendar.MONTH, addMonth);

        SimpleDateFormat formatter = new SimpleDateFormat(format);
        return formatter.format(cal.getTime());
    }

    /**
     * 주어진 날짜 DATE 의 일수를 addDay 만큼 증가 또는 감소 시킴
     * @param dateString - 8자리(YYYYMMDD) 날짜 데이터
     * @param addYear - 더할 일의 수 (증가시 +, 감소시 -)
     * @return String
     * @throws Exception
     */
    public static String addDay(String dateString, int addDay) throws Exception
    {
        return addDay(dateString, addDay, "yyyyMMdd");
    }

    /**
     * 주어진 날짜 DATE 의 일수를 addCount 만큼 증가 또는 감소 시킴
     * @param dateString - 8자리(YYYYMMDD) 날짜 데이터
     * @param addDay - 더할 일의 수 (증가시 +, 감소시 -)
     * @param format - 날짜 형식
     * @return String
     */
    public static String addDay(String dateString, int addDay, String format)
    {
        if (dateString == null)
        {
            return null;
        }
        int year = 0;
        int month = 2;
        int day = 1;

        if (dateString.length() == 10)
        {
            //format = "yyyy-MM-dd";
            year = Integer.parseInt(dateString.substring(0, 4));
            month = Integer.parseInt(dateString.substring(4, 6));
            day = Integer.parseInt(dateString.substring(6));
        }
        if (dateString.length() == 8)
        {
            //format = "yyyyMMdd";
            year = Integer.parseInt(dateString.substring(0, 4));
            month = Integer.parseInt(dateString.substring(4, 6));
            day = Integer.parseInt(dateString.substring(6));
        }
        else
        {
            return null;
        }
        GregorianCalendar cal = new GregorianCalendar();

        //MONTH 는 0 ~ 11 이므로 실제 데이터의 마이너스 1
        cal.set(year, month - 1, day);
        cal.add(GregorianCalendar.DAY_OF_YEAR, addDay);

        SimpleDateFormat formatter = new SimpleDateFormat(format);
        return formatter.format(cal.getTime());
    }

    /**
     * 입력된 날짜의 요일을 출력해 주는 함수
     * @param dayStr  - 날짜
     * @param String format - 날짜 형식
     * @return String
     */
    public static String getDayOfTheWeek(String dayStr, String format)
    {
        DateFormat df1 = new SimpleDateFormat(format);
        DateFormat df2 = new SimpleDateFormat("EEE");
        String yoil = df2.format(df1.parse(dayStr, new ParsePosition(0)));

        return yoil;
    }

    /**
     * 지난달의 마지막 일자 구하기
     * @param dateString - 일자
     * @param format - 날짜 형식
     * @return String
     * @throws Exception
     */
    public static String lastDayOfLastMonth(String dateString, String format) throws Exception
    {
        String lastDate = null;
        if (dateString == null)
        {
            return lastDate;
        }
        String tmpDate = addMonth(dateString, -1, format);
        if (format.equals("yyyyMM"))
        {
            tmpDate = tmpDate + "01";
            format = "yyyyMMdd";
        }
        else if (format.equals("yyyy-MM"))
        {
            tmpDate = tmpDate + "-01";
            format = "yyyy-MM-dd";
        }
        lastDate = lastDayOfMonth(tmpDate, format);

        return lastDate;
    }

    /**
     * 주어진 날짜의 기간 구하기 to - from
     * @param from - 시작일자
     * @param to - 종료일자
     * @return int - 기간(일수)
     * @throws Exception
     */
    public static int daysBetween(String from, String to) throws Exception
    {
        return daysBetween(from, to, "yyyyMMdd");
    }

    /**
     * 주어진 날짜의 기간(일) 구하기
     * @param from - 시작일자
     * @param to - 종료일자
     * @param format - 날짜 형식
     * @return int - 기간(일수)
     * @throws Exception
     */
    public static int daysBetween(String from, String to, String format) throws Exception
    {
        SimpleDateFormat formatter = new SimpleDateFormat(format, java.util.Locale.KOREA);
        Date d1 = formatter.parse(from);
        Date d2 = formatter.parse(to);

        if (!formatter.format(d1).equals(from))
            return -1;
        if (!formatter.format(d2).equals(to))
            return -1;

        long duration = d2.getTime() - d1.getTime();

        if (duration < 0)
            return -1;

        return (int) (duration / (1000 * 60 * 60 * 24));
    }

    /**
     * 주어진 날짜의 기간(달) 구하기
     * @param from - 시작일자
     * @param to - 종료일자
     * @return int - int - 기간(달수)
     * @throws Exception
     */
    public static int monthsBetween(String from, String to) throws Exception
    {
        return monthsBetween(from, to, "yyyyMMdd");
    }

    /**
     * 주어진 날짜의 기간(달) 구하기
     * @param from - 시작일자
     * @param to - 종료일자
     * @param format - 날짜 형식
     * @return int - int - 기간(달수)
     * @throws Exception
     */
    public static int monthsBetween(String from, String to, String format) throws Exception
    {
        SimpleDateFormat formatter = new SimpleDateFormat(format, Locale.KOREA);
        Date fromDate = formatter.parse(from);
        Date toDate = formatter.parse(to);

        // if two date are same, return 0.
        if (fromDate.compareTo(toDate) == 0)
            return 0;

        SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy", Locale.KOREA);
        SimpleDateFormat monthFormat = new SimpleDateFormat("MM", Locale.KOREA);
        SimpleDateFormat dayFormat = new SimpleDateFormat("dd", Locale.KOREA);

        int fromYear = Integer.parseInt(yearFormat.format(fromDate));
        int toYear = Integer.parseInt(yearFormat.format(toDate));
        int fromMonth = Integer.parseInt(monthFormat.format(fromDate));
        int toMonth = Integer.parseInt(monthFormat.format(toDate));
        int fromDay = Integer.parseInt(dayFormat.format(fromDate));
        int toDay = Integer.parseInt(dayFormat.format(toDate));

        int result = 0;
        result += ((toYear - fromYear) * 12);
        result += (toMonth - fromMonth);

        //          if (((toDay - fromDay) < 0) ) result += fromDate.compareTo(toDate);
        // ceil과 floor의 효과
        if (((toDay - fromDay) > 0))
            result += toDate.compareTo(fromDate);

        return result;
    }

    /**
     * 만년수 계산
     * @param dateString - 날짜
     * @param from - 시작일자(YYYYMMDD)
     * @param to - 종료일자(YYYYMMDD)
     * @return int - 만년수
     * @throws Exception
     */
    public static int yearBetween(String from, String to)
    {
        int yearCnt = 0;
        int toYYYY = Integer.parseInt(to.substring(0, 4));
        int toMMDD = Integer.parseInt(to.substring(4));
        int fromYYYY = Integer.parseInt(from.substring(0, 4));
        int fromMMDD = Integer.parseInt(from.substring(4));

        yearCnt = toYYYY - fromYYYY;
        if ((toMMDD - fromMMDD) < 0)
        {
            yearCnt--;
        }

        return yearCnt;
    }

    /**
     * 주차를 구하는 함수
     * @param setDate - 날짜
     * @param format - 날짜형식 (yyyyMMdd or yyyy-MM-dd)
     * @param dateGuboon - 주차 구분 (M:월주차, Y:연주차)
     * @return int - 해당주차
     * @throws Exception
     */
    public static int getWeeks(String setDate, String format, String dateGuboon) throws Exception
    {
        int rDateInfo = 0;
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat formatter = new SimpleDateFormat(format);
        Date d = formatter.parse(setDate);
        calendar.setTime(d);

        //Calendar Class는 날짜를 0달부터 인식하므로 반드시 +1을 해주어야함
        int monadd = calendar.get(Calendar.MONTH) + 1;

        //해당년도의 달에 속하는 주차를 알고 싶은경우
        if (dateGuboon.toUpperCase().compareTo("M") == 0)
        {
            int rWeekInMonth = calendar.get(Calendar.WEEK_OF_MONTH);
            rDateInfo = rWeekInMonth;
        }
        //해당년도에 속하는 주차를 알고 싶을 경우
        else if (dateGuboon.toUpperCase().compareTo("Y") == 0)
        {
            int rWeekInYear = calendar.get(Calendar.WEEK_OF_YEAR);
            rDateInfo = rWeekInYear;
            if (monadd == 12 && rWeekInYear == 1)
                rDateInfo = 53;
        }

        return rDateInfo;

    }

    /**
     * 날짜 비교
     * @param dateString - 날짜
     * @param from - 시작일자(YYYYMMDD)
     * @param to - 종료일자(YYYYMMDD)
     * @param format - 날짜형식 (yyyyMMddHHmm or yyyy-MM-dd HH:mm)
     * @return int - 0(같을때), -1(from > to), 1(from < to)
     * @throws Exception
     */
    public static int compareDate(String from, String to, String format) throws Exception
    {
        SimpleDateFormat formatter = new SimpleDateFormat(format, java.util.Locale.US);
        Date d1 = formatter.parse(from);
        Date d2 = formatter.parse(to);
        
        System.out.println("from:" + from + ", to:" + to + ", compare result:" + d2.compareTo(d1));

        return d2.compareTo(d1);
    }
	
    /**
     * 
     * @param date
     * @param format
     * @return
     * @throws Exception
     */
    @SuppressWarnings("deprecation")
	public static String dateFormatChange(String date, String fromFormat) throws Exception
    {
		String yyyy,mm,dd, rtn = "";
    	if(!StringUtils.isEmpty(date)) {
			if("mm/dd/yyyy".equals(fromFormat)) {
				if(date.lastIndexOf("/") != -1 || date.lastIndexOf("-") != -1) {
					date = date.replaceAll("[_/:-]", "");
					yyyy = date.substring(4, 8);
					mm = date.substring(0, 2);
					dd = date.substring(2, 4);
					rtn = yyyy+mm+dd;
				}else rtn = date;
			}else rtn = date;
    	}
		
        return rtn; 
    }
}