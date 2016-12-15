package com.xinyuan.utils;


import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

public class DateUtil {
	/**
     * String(yyyy-MM-dd HH:mm:ss) 转 Date
     *
     * @param time
     * @return
     * @throws ParseException
     */
    public static Date StringToDate(String time) throws ParseException {
         
        Date date = new Date();
        // 注意format的格式要与日期String的格式相匹配
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            date = dateFormat.parse(time);
        } catch (Exception e) {
            e.printStackTrace();
        }
 
        return date;
    }
 
    /**
     * Date转为String(yyyy-MM-dd HH:mm:ss)
     * 
     * @param time
     * @return
     */
    public static String DateToString(Date time) {
        String dateStr = "";
//        Date date = new Date();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            dateStr = dateFormat.format(time);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dateStr;
    }
    /**
     * Date转为String(yyyy-MM-dd HH:mm:ss)
     * 
     * @param time
     * @return
     */
    public static String DateToString(Date time, String format) {
        String dateStr = "";
//        Date date = new Date();
        DateFormat dateFormat = new SimpleDateFormat(format);
        try {
            dateStr = dateFormat.format(time);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dateStr;
    }
    /**
     * String(yyyy-MM-dd HH:mm:ss)转10位时间戳
     * @param time
     * @return
     */
    public static Integer StringToTimestamp(String time){
        int times = 0;
        try {  
            times = (int) ((Timestamp.valueOf(time).getTime())/1000);  
        } catch (Exception e) {  
            e.printStackTrace();  
        }
        if(times==0){
            System.out.println("String转10位时间戳失败");
        }
        return times; 
         
    }
    /**
     * 10位int型的时间戳转换为String(yyyy-MM-dd HH:mm:ss)
     * @param time
     * @return
     */
    public static String timestampToString(Integer time){
        //int转long时，先进行转型再进行计算，否则会是计算结束后在转型
        long temp = (long)time*1000;
        Timestamp ts = new Timestamp(temp);  
        String tsStr = "";  
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
        try {  
            //方法一  
            tsStr = dateFormat.format(ts);  
            System.out.println(tsStr);  
        } catch (Exception e) {  
            e.printStackTrace();  
        }
        return tsStr;  
    }
    /**
     * 10位时间戳转Date
     * @param time
     * @return
     */
    public static Date TimestampToDate(Integer time){
        long temp = (long)time*1000;
        Timestamp ts = new Timestamp(temp);  
        Date date = new Date();  
        try {  
            date = ts;  
            //System.out.println(date);  
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
        return date;
    }
    /**
     * Date类型转换为10位时间戳
     * @param time
     * @return
     */
    public static Integer DateToTimestamp(Date time){
        Timestamp ts = new Timestamp(time.getTime());
         
        return (int) ((ts.getTime())/1000);
    }
    
    public static void main(String [] args){
//        System.out.println(StringToTimestamp("2011-05-09 11:49:45"));
//        System.out.println(DateToTimestamp(TimestampToDate(StringToTimestamp("2011-05-09 11:49:45"))));
//        System.out.println(DateToTimestamp(new Date()));
//        while(true){
//        	String dateToString = DateToString(new Date(), "yyyyMMddHHmmssS");
//        	System.out.println(dateToString + "   " + dateToString.length());
//        } 
    	Long i=(long) (1441182716979l/1000);
    	System.out.println(timestampToString(i.intValue()));
    }

    
    public static Integer secondsToNextDay(){	//现在到凌晨的毫秒书
	    final Calendar cal = Calendar.getInstance();
	    cal.set(Calendar.DAY_OF_MONTH, cal.get(Calendar.DAY_OF_MONTH) + 1);
	    cal.set(Calendar.HOUR_OF_DAY, 0);
	    cal.set(Calendar.MINUTE, 0);
	    cal.set(Calendar.SECOND, 0);
	    cal.set(Calendar.MILLISECOND, 0);
	    final double diff = cal.getTimeInMillis() - System.currentTimeMillis();
	    return (int) (diff/1000);
    }
	public static Date StringToDate(String dateStr, String format) {
		Date date = new Date();
		try {
			DateFormat parser = DateFormat.getDateInstance();
			if(format!=null){
				parser = new SimpleDateFormat(format);
			}
			date = parser.parse(dateStr);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}

	/**
	 * 给定时间的前一个月
	 * @param date
	 * @return
	 */
	public static Date previousMonth(Date date){
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.MONTH, -1);	//减一个月
		return calendar.getTime();
	}
	
	/**
	 * 给定时间的后一个月
	 * @param date
	 * @return
	 */
	public static Date nextMonth(Date date){
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.MONTH, 1);	//减一个月
		return calendar.getTime();
	}
	
	public static Date currentShiftYear(int year) {
		Calendar c = new GregorianCalendar();
    	c.add(Calendar.YEAR, year);
    	c.add(Calendar.DAY_OF_MONTH, -1);
    	return c.getTime();
	}
	
	public static Date DateShiftDay(Date date, int day) {
		Calendar c = new GregorianCalendar();
		c.setTime(date);
    	c.add(Calendar.DAY_OF_MONTH, day);
    	return c.getTime();
	}
	
	public static Date addSeconds(int t){
		Date date=new Date();//取时间
		if(t==0)
		{
			return date;
		}
 		Calendar calendar = new GregorianCalendar();
		calendar.setTime(date);
		calendar.add(calendar.SECOND,t); 
		date=calendar.getTime();  
 		return date;
	}
	
	public static Date addSeconds(Date date ,int t){
 		if(t==0)
		{
			return date;
		}
 		Calendar calendar = new GregorianCalendar();
		calendar.setTime(date);
		calendar.add(calendar.SECOND,t); 
		date=calendar.getTime();  
 		return date;
	}
	
	public static Date addHours(Date date ,int h){
 		if(h==0)
		{
			return date;
		}
 		Calendar calendar = new GregorianCalendar();
		calendar.setTime(date);
		calendar.add(calendar.HOUR,h); 
		date=calendar.getTime();  
 		return date;
	}
	
	
	/****************************/
	
	public static Date getDate(Integer seconds) {
		Calendar calendar = Calendar.getInstance();//获得当前时间
		calendar.add(Calendar.SECOND, seconds);//加20秒
		return calendar.getTime();//返回Date类
	}
	
	/**
	 * 现在到指定时间的秒数
	 * @param endDate
	 * @return
	 */
	public static long getSecondToTime(Date endDate) {
		  long a = new Date().getTime();
		  long b = endDate.getTime();
		  long c =((b - a) / 1000);
		  return c;
	}
	
	/**
	 * 现在到指定时间的秒数
	 * @param startDate
	 * @return
	 */
	public static long getSecondToTime(Date startDate,Date endDate) {
		  long a = startDate.getTime();
		  long b = endDate.getTime();
		  long c = ((b - a) / 1000);
		  return c;
	}
	
	
	 /**
	  * 判断二个时间是否在同一个周
	  * 
	  * @param date1
	  * @param date2
	  * @return
	  */
	 public static boolean isSameWeekDates(Date date1, Date date2) {
	  Calendar cal1 = Calendar.getInstance();
	  Calendar cal2 = Calendar.getInstance();
	  cal1.setTime(date1);
	  cal2.setTime(date2);
	  int subYear = cal1.get(Calendar.YEAR) - cal2.get(Calendar.YEAR);
	  if (0 == subYear) {
	   if (cal1.get(Calendar.WEEK_OF_YEAR) == cal2.get(Calendar.WEEK_OF_YEAR))
	    return true;
	  } else if (1 == subYear && 11 == cal2.get(Calendar.MONTH)) {
	   // 如果12月的最后一周横跨来年第一周的话则最后一周即算做来年的第一周
	   if (cal1.get(Calendar.WEEK_OF_YEAR) == cal2.get(Calendar.WEEK_OF_YEAR))
	    return true;
	  } else if (-1 == subYear && 11 == cal1.get(Calendar.MONTH)) {
	   if (cal1.get(Calendar.WEEK_OF_YEAR) == cal2.get(Calendar.WEEK_OF_YEAR))
	    return true;
	  }
	  return false;
	 }
	 
	 /**
	  * 判断是否润年
	  * 详细设计： 1.被400整除是闰年，否则： 2.不能被4整除则不是闰年 3.能被4整除同时不能被100整除则是闰年
	  * 3.能被4整除同时能被100整除则不是闰年
	  * @param dateStr
	  * @return
	 * @throws ParseException 
	  */
	 public static boolean isLeapYear(String dateStr) throws ParseException {
	  Date d = StringToDate(dateStr);
	  GregorianCalendar gc = (GregorianCalendar) Calendar.getInstance();
	  gc.setTime(d);
	  int year = gc.get(Calendar.YEAR);
	  if ((year % 400) == 0)
	   return true;
	  else if ((year % 4) == 0) {
	   if ((year % 100) == 0)
	    return false;
	   else
	    return true;
	  } else
	   return false;
	 }
	 
	 /**
	  * 产生周序列,即得到当前时间所在的年度是第几周
	  * 
	  * @return
	  */
	 public static String getWeek() {
	  Calendar c = Calendar.getInstance(Locale.CHINA);
	  String week = Integer.toString(c.get(Calendar.WEEK_OF_YEAR));
	  if (week.length() == 1)
	   week = "0" + week;
	  String year = Integer.toString(c.get(Calendar.YEAR));
	  return year + week;
	 }
	 
	 /**
	  * 获得一个日期所在的周的星期几的日期，如要找出2002年2月3日所在周的星期一是几号
	  * 
	  * @param sdate
	  * @param num
	  * @return
	 * @throws ParseException 
	  */
	 public static String getWeek(String sdate, String num) throws ParseException {
	  // 再转换为时间
	  Date dd = StringToDate(sdate);
	  Calendar c = Calendar.getInstance();
	  c.setTime(dd);
	  if (num.equals("1")) // 返回星期一所在的日期
	   c.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
	  else if (num.equals("2")) // 返回星期二所在的日期
	   c.set(Calendar.DAY_OF_WEEK, Calendar.TUESDAY);
	  else if (num.equals("3")) // 返回星期三所在的日期
	   c.set(Calendar.DAY_OF_WEEK, Calendar.WEDNESDAY);
	  else if (num.equals("4")) // 返回星期四所在的日期
	   c.set(Calendar.DAY_OF_WEEK, Calendar.THURSDAY);
	  else if (num.equals("5")) // 返回星期五所在的日期
	   c.set(Calendar.DAY_OF_WEEK, Calendar.FRIDAY);
	  else if (num.equals("6")) // 返回星期六所在的日期
	   c.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
	  else if (num.equals("0")) // 返回星期日所在的日期
	   c.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
	  return new SimpleDateFormat("yyyy-MM-dd").format(c.getTime());
	 }
}
