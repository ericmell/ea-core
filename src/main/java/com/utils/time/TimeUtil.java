package com.utils.time;

import java.lang.reflect.Array;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class TimeUtil {
	private static Log log = LogFactory.getLog(TimeUtil.class);

	static Pattern days = Pattern.compile("^([0-9]+)d$");
	static Pattern hours = Pattern.compile("^([0-9]+)h$");
	static Pattern minutes = Pattern.compile("^([0-9]+)mi?n$");
	static Pattern seconds = Pattern.compile("^([0-9]+)s$");

	/**
	 * 字符串转日期
	 * 
	 * @param var
	 * @param format
	 * @return
	 */
	public static Date string2Date(String var, SimpleDateFormat format) {
		Date date = null;
		try {
			date = format.parse(var);

		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}

	/**
	 * 日期转换字符串（yyyy-MM-dd HH:mm:ss）
	 * 
	 * @param datetime
	 * @return
	 */
	public static String formatTime(Date datetime) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		if (datetime != null) {
			String formatString = format.format(datetime);
			return formatString;
		}
		return null;
	}

	public static int getHowManyYearToNow(String var1) throws Exception {

		Date date1 = string2Date(var1, new SimpleDateFormat("yyyy-MM-dd"));
		Date dNow = new Date();
		int yNow = dNow.getYear();
		return yNow - date1.getYear();

	}

	public static String getStrHowManyYearToNow(String var1) {
		try {
			int result = getHowManyYearToNow(var1);
			return Integer.toString(result);
		} catch (Exception e) {
			return "";
		}
	}

	/**
	 * Parse a duration
	 * 
	 * @param duration
	 *            3h, 2mn, 7s
	 * @return The number of seconds
	 */
	public static int parseDuration(String duration) {
		if (duration == null) {
			return 60 * 60 * 24 * 30;
		}
		int toAdd = -1;
		if (days.matcher(duration).matches()) {
			Matcher matcher = days.matcher(duration);
			matcher.matches();
			toAdd = Integer.parseInt(matcher.group(1)) * (60 * 60) * 24;
		} else if (hours.matcher(duration).matches()) {
			Matcher matcher = hours.matcher(duration);
			matcher.matches();
			toAdd = Integer.parseInt(matcher.group(1)) * (60 * 60);
		} else if (minutes.matcher(duration).matches()) {
			Matcher matcher = minutes.matcher(duration);
			matcher.matches();
			toAdd = Integer.parseInt(matcher.group(1)) * (60);
		} else if (seconds.matcher(duration).matches()) {
			Matcher matcher = seconds.matcher(duration);
			matcher.matches();
			toAdd = Integer.parseInt(matcher.group(1));
		}
		if (toAdd == -1) {
			throw new IllegalArgumentException("Invalid duration pattern : "
					+ duration);
		}
		return toAdd;
	}

	/**
	 * "yyyy-MM-dd HH:mm:ss"
	 * */
	public static String DateToStr(Date date, String formatString) {

		SimpleDateFormat format = new SimpleDateFormat(formatString);
		String str = format.format(date);
		return str;
	}

	public static String getConstellation(String key)

	{
		try {
			String s = "魔羯水瓶双鱼牡羊金牛双子巨蟹狮子处女天秤天蝎射手魔羯";
			int num = Integer.parseInt(getConstellationNumber(key));
			return s.substring(num, num + 2);
		} catch (Exception e) {
			return "";
		}
	}

	/* 星座 */
	public static String getConstellationNumber(String key) {
		try {
			int month = Integer.valueOf(key.substring(5, 7));
			int day = Integer.valueOf(key.substring(8, 10));
			int[] arr = { 20, 19, 21, 21, 21, 22, 23, 23, 23, 23, 22, 22 };
			int num = month * 2 - (day < arr[month - 1] ? 2 : 0);
			return String.valueOf(num);
		} catch (Exception e) {
			return "";
		}
	}

	/**
	 * 得到另一天的字符串，如果是前几天，就是负�，后几天就加正数
	 */
	public static String getOhterDayByNumnber(String dateStr, int daynum)
			throws Exception {
		GregorianCalendar rili = new GregorianCalendar();
		// DateFormat df = DateFormat.getDateInstance("yyyy-MM-dd");
		java.text.SimpleDateFormat df = new java.text.SimpleDateFormat(
				"yyyy-MM-dd"); //$NON-NLS-1$	
		Date begindate = df.parse(dateStr);
		rili.setTime(begindate);
		rili.add(GregorianCalendar.DATE, daynum);
		begindate = rili.getTime();
		String s = df.format(begindate);
		return s;
	}

	public static Date getOtherDateByNumnber(Date begindate, int daynum)
			throws Exception {
		GregorianCalendar rili = new GregorianCalendar();
		rili.setTime(begindate);
		rili.add(GregorianCalendar.DATE, daynum);
		begindate = rili.getTime();
		return begindate;
	}

	// 根据当天的日期求相对日期
	public static String getDay2Today(int daynum) throws Exception {
		GregorianCalendar rili = new GregorianCalendar();
		// DateFormat df = DateFormat.getDateInstance("yyyy-MM-dd");
		java.text.SimpleDateFormat df = new java.text.SimpleDateFormat(
				"yyyy-MM-dd"); //$NON-NLS-1$	
		try {
			Date begindate = new java.util.Date();

			rili.setTime(begindate);
			rili.add(GregorianCalendar.DATE, daynum);
			begindate = rili.getTime();
			String s = df.format(begindate);
			return s;
		} catch (Exception e) {
			// Auto-generated catch block

			throw e;
		}

	}

	public static String getTimeStr() throws Exception {
		java.util.Date date = new java.util.Date();
		java.text.SimpleDateFormat df = new java.text.SimpleDateFormat(
				"yyyyMMddhhmmss"); //$NON-NLS-1$
		String timestr = df.format(date);
		return timestr;

	}

	/**
	 * 根据格式得到日期字符串
	 * 
	 * @param date
	 * @param format
	 *            "yyyyMMddhhmmss"类似
	 * @return
	 */
	public static String getTimeStrByDate(Date date, String format) {

		java.text.SimpleDateFormat df = new java.text.SimpleDateFormat(format); //$NON-NLS-1$
		String timestr = df.format(date);
		return timestr;

	}

	/**
	 * 根据格式得到当前日期字符串
	 * 
	 * @param date
	 * @param format
	 *            "yyyyMMddhhmmss"类似
	 * @return
	 */
	public static String getTimeStr(String format) throws Exception {
		java.util.Date date = new java.util.Date();
		java.text.SimpleDateFormat df = new java.text.SimpleDateFormat(format); //$NON-NLS-1$
		String timestr = df.format(date);
		return timestr;

	}

	//
	public static boolean compareDayStr(String dateBegin, String dateEnd)
			throws Exception {
		try {
			DateFormat df = DateFormat.getDateInstance();
			Date begindate = df.parse(dateBegin);
			Date enddate = df.parse(dateEnd);

			if (begindate.before(enddate) || begindate.equals(enddate)) {
				return true;
			}
			return false;
		} catch (Exception e) {
			// Auto-generated catch block

			throw e;
		}

	}

	/*
	 * 求所少天 * public static String getHowManyYear(String dateStr, int daynum)
	 * throws Exception { GregorianCalendar rili = new GregorianCalendar();
	 * DateFormat df = DateFormat.getDateInstance(); try { Date begindate =
	 * df.parse(dateStr); rili.setTime(begindate);
	 * rili.add(GregorianCalendar.DATE, daynum);// ��һ�� begindate =
	 * rili.getTime(); df = DateFormat.getDateInstance(); String s =
	 * df.format(begindate); return s; } catch (ParseException e) { //
	 * Auto-generated catch block
	 * 
	 * throw e; }
	 * 
	 * }
	 */
	/*
	 * 求道今天为止，多少年 *
	 */
	public static int getHowManyYear(String date_str, String expression)
			throws Exception {

		SimpleDateFormat sdf = new SimpleDateFormat(expression);
		Date birth_date = null;
		Date current_date = new Date();
		try {
			sdf.parse(date_str);
			birth_date = sdf.parse(date_str);
		} catch (Exception e) {
			throw e;
		}
		// long birth_date_long=birth_date.getTime();
		// long current_date_long=current_date.getTime();
		int birth_year = 1900 + birth_date.getYear();
		int current_year = 1900 + current_date.getYear();
		int birth_month = birth_date.getMonth();
		int current_month = current_date.getMonth();
		int birth_day = birth_date.getDate();
		int current_day = current_date.getDate();
		int pd = 0;
		if (current_month < birth_month) {
			pd = 1;
		} else if (current_month == birth_month) {
			if (current_day < birth_day) {
				pd = 1;
			}
		}

		return current_year - birth_year - pd;
	}

	public static int currentAge(String date_str, String expression) {
		SimpleDateFormat sdf = new SimpleDateFormat(expression);
		Date birth_date = null;
		Date current_date = new Date();
		try {
			birth_date = sdf.parse(date_str);
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		int birth_year = 1900 + birth_date.getYear();
		int current_year = 1900 + current_date.getYear();

		int birth_month = birth_date.getMonth();
		int current_month = current_date.getMonth();

		int birth_day = birth_date.getDate();
		int current_day = current_date.getDate();

		int pd = 0;
		if (current_month < birth_month) {
			pd = 1;
		} else if (current_month == birth_month) {
			if (current_day < birth_day) {
				pd = 1;
			}
		}
		System.out.println("birth_day" + birth_day);
		System.out.println("current_day" + current_day);

		return current_year - birth_year - pd;
	}

	/**
	 * 得到当前时间是当前年的第几周
	 * 
	 * @param date
	 * @return
	 */
	public static int getWeekOfYear(Date date) {
		Calendar c = new GregorianCalendar();
		c.setFirstDayOfWeek(Calendar.MONDAY);
		c.setMinimalDaysInFirstWeek(7);
		c.setTime(date);
		return c.get(Calendar.WEEK_OF_YEAR);
	}

	/**
	 * 得到指定年份最大周数
	 * 
	 * @param year
	 * @return
	 */
	public static int getMaxWeekNumOfYear(int year) {
		Calendar c = new GregorianCalendar();
		c.set(year, Calendar.DECEMBER, 31, 23, 59, 59);
		return getWeekOfYear(c.getTime());
	}

	/**
	 * 通过指定年和周，得到这几周的第一天
	 * 
	 * @param year
	 * @param week
	 * @return
	 */
	public static Date getFirstDayOfWeek(int year, int week) {
		Calendar c = new GregorianCalendar();
		c.set(Calendar.YEAR, year);
		c.set(Calendar.MONTH, Calendar.JANUARY);
		c.set(Calendar.DATE, 1);
		Calendar cal = (GregorianCalendar) c.clone();
		cal.add(Calendar.DATE, week * 7);
		return getFirstDayOfWeek(cal.getTime());
	}

	/**
	 * 通过指定年或周，得到这周最后一天
	 * 
	 * @param year
	 * @param week
	 * @return
	 */
	public static Date getLastDayOfWeek(int year, int week) {
		Calendar c = new GregorianCalendar();
		c.set(Calendar.YEAR, year);
		c.set(Calendar.MONTH, Calendar.JANUARY);
		c.set(Calendar.DATE, 1);
		Calendar cal = (GregorianCalendar) c.clone();
		cal.add(Calendar.DATE, week * 7);
		return getLastDayOfWeek(cal.getTime());
	}

	/**
	 * 通过传入时间，得到传入时间所在周的第一天
	 * 
	 * @param date
	 * @return
	 */
	public static Date getFirstDayOfWeek(Date date) {
		Calendar c = new GregorianCalendar();
		c.setFirstDayOfWeek(Calendar.MONDAY);
		c.setTime(date);
		c.set(Calendar.DAY_OF_WEEK, c.getFirstDayOfWeek()); // Monday
		return c.getTime();
	}

	/**
	 * 通过传入时间，得到传入时间所在周的最后一天
	 * 
	 * @param date
	 * @return
	 */
	public static Date getLastDayOfWeek(Date date) {
		Calendar c = new GregorianCalendar();
		c.setFirstDayOfWeek(Calendar.MONDAY);
		c.setTime(date);
		c.set(Calendar.DAY_OF_WEEK, c.getFirstDayOfWeek() + 6); // c
		return c.getTime();
	}

	/**
	 * 得到当前年分
	 * 
	 * @return
	 */
	public static Integer getCurrentYear() {
		Calendar c = new GregorianCalendar();
		return c.get(Calendar.YEAR);
	}

	/**
	 * 得到当前日
	 * 
	 * @return
	 */
	public static Integer getCurrentDay() {
		Calendar c = new GregorianCalendar();
		return c.get(Calendar.DAY_OF_MONTH);
	}

	/**
	 * 得到传入时间的年份
	 * 
	 * @param date
	 * @return
	 */
	public static Integer getCurrentYear(Date date) {
		Calendar c = new GregorianCalendar();
		c.setTime(date);
		return c.get(Calendar.YEAR);
	}

	public static String formatDate(Date date, String expression) {
		SimpleDateFormat sdf = new SimpleDateFormat(expression);
		return sdf.format(date);
	}

	public static Date parseDate(String source, String expression) {
		SimpleDateFormat sdf = new SimpleDateFormat(expression);
		try {
			return sdf.parse(source);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * @param date1
	 *            需要比较的时间 不能为空(null),需要正确的日期格式 ,如：2009-09-12
	 * @param date2
	 *            被比较的时间 为空(null)则为当前时间
	 * @param stype
	 *            返回值类型 0为多少天，1为多少个月，2为多少年
	 * @return 举例： compareDate("2009-09-12", null, 0);//比较天
	 *         compareDate("2009-09-12", null, 1);//比较月
	 *         compareDate("2009-09-12", null, 2);//比较年
	 */
	public static String compareDate(String startDay, String endDay, int stype) {
		int n = 0;
		String[] u = { "天", "月", "年" };
		String formatStyle = stype == 1 ? "yyyy-MM" : "yyyy-MM-dd";

		endDay = endDay == null ? getCurrentDate("yyyy-MM-dd") : endDay;

		DateFormat df = new SimpleDateFormat(formatStyle);
		Calendar c1 = Calendar.getInstance();
		Calendar c2 = Calendar.getInstance();
		try {
			c1.setTime(df.parse(startDay));
			c2.setTime(df.parse(endDay));
		} catch (Exception e3) {
			System.out.println("wrong occured");
		}
		// List list = new ArrayList();
		while (!c1.after(c2)) { // 循环对比，直到相等，n 就是所要的结果
			// list.add(df.format(c1.getTime())); // 这里可以把间隔的日期存到数组中 打印出来
			n++;
			if (stype == 1) {
				c1.add(Calendar.MONTH, 1); // 比较月份，月份+1
			} else {
				c1.add(Calendar.DATE, 1); // 比较天数，日期+1
			}
		}
		n = n - 1;
		if (stype == 2) {
			n = (int) n / 365;
		}
		System.out.println(startDay + " -- " + endDay + " 相差多少" + u[stype]
				+ ":" + n);
		return String.valueOf(n);
	}

	public static String getCurrentDate(String format) {
		Calendar day = Calendar.getInstance();
		day.add(Calendar.DATE, 0);
		SimpleDateFormat sdf = new SimpleDateFormat(format);// "yyyy-MM-dd"
		String date = sdf.format(day.getTime());
		return date;
	}

	public static long getHowManyDayFromNow(String date){
		long daysBetween=(new Date().getTime()-parseDate(date, "yyyy-MM-dd").getTime()+1000000)/(3600*24*1000);
	return daysBetween;
	}
	public static void main(String[] args) {
		try {
			// log.debug(DateUtil.getTimeStr("yyyy-MM-dd"));
			// log.debug(DateUtil.getOhterDayByNumnber("2010-01-27", 5));
			// log.debug(DateUtil.compareDayStr("2010-01-27", "2010-01-3"));
		//	log.debug(TimeUtil.compareDate("2013-07-23", "2013-07-11", 0));
			System.out.println("天数"+TimeUtil.getHowManyDayFromNow("2013-10-01"));
			// log.debug(DateUtil.compareDate("2013-08-25",null,0) );
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static Integer getDayNumByYearMonth(String Year, int month) {
		int year = Integer.parseInt(Year);
		int days;
		int a[] = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
		if(month==1){
			days=((0==year%4)&&(0!=(year%100)))||(0==year%400)?29:28;
		}else{
			days=a[month];
		}
		return days;
	}
	
	public static boolean ifFirstDayOfWeek(String year, int month, int day) {
		Date date = new Date(Integer.parseInt(year), month, day);
		Calendar c = new GregorianCalendar();
		c.setFirstDayOfWeek(Calendar.MONDAY);
		c.setTime(date);
		c.set(Calendar.DAY_OF_WEEK, c.getFirstDayOfWeek()); // Monday
		Date firstDateOfWeek = c.getTime();
		if(firstDateOfWeek.getTime() == date.getTime()){
			return true; 
		}
		else
			return false;
	}

}
