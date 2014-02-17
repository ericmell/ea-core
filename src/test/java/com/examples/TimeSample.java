package com.examples;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.utils.time.TimeUtil;

public class TimeSample {
	static Logger log = LoggerFactory.getLogger(TimeSample.class);

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String time1 = "2008-06-30";
		Date date1 = TimeUtil.string2Date(time1, new SimpleDateFormat(
				"yyyy-MM-dd"));
		Date dNow = new Date();
		Calendar cNow = Calendar.getInstance();
		log.debug(cNow.getTime().toString());
		Calendar c1 = Calendar.getInstance();
		c1.setTime(date1);
		System.out.println(c1.getTime());
		System.out.println(c1.get(c1.DAY_OF_YEAR));

		int year1 = date1.getYear();
		int yNow = dNow.getYear();
		System.out.println(yNow + "-" + year1 + "=" + (yNow - year1));

	}

}
