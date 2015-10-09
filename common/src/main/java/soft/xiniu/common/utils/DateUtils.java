package soft.xiniu.common.utils;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Properties;

/**
 *
 * @author <a href="mailt:wenlin56@sina.com"> wjh </a>
 */
public class DateUtils {
	public final static long DayMilliseconds = 86400000L;

	/**
	 * 比较两个时间对象。得出比较结果字符串。所得到的结果可能是以下几种情况：<br/>
	 *   ##30天前 (大于或等于30天) <br/>
	 *   ##N天前 (N不大于30) <br/>
	 *   ##N分钟前 (N不大于59) <br/>
	 *   ##1分钟前 (差值等于或小于1——包括负值的情况（###1）)<br/>
	 *   —— ###1 负值的情况： 当after时间在before时间之前时。例如，调用者这样传入参数：between(2011-10-8 12:12:00, 2011-10-1 12:12:00)<br>
	 *   ##未知 （before==null || after ==null）
	 *
	 * @param before 较靠前的那个时间，
	 * @param after 较靠后的那个时间。
	 * @return
	 */
	public static String between(Date before, Date after) {
		final int maxDay = 30;
		if(before==null || after ==null ) {
			return "未知";
		}

		if(after.before(before)) {
			return "1分钟前";
		}

		long afterL = after.getTime();
		long beforeL = before.getTime();

		long n = Math.abs(afterL-beforeL);

		n /= 1000;
		long minute = n / 60;
		long hour = minute / 60; // 最终相差小时
		minute %= 60;  // 最终相差分钟


		if(hour >= maxDay*24) {
			return "30天前";
		}

		if(hour >= 24) {
			return  (int)hour/24 +"天前";  //(int)(Math.max(hour/24f, hour/24+0.9f)) + "天前";
		}


		if(hour > 0) {
			return hour +"小时前";
		}


		if(minute < 1) {
			minute = 1;
		}

		return minute + "分钟前";

	}

	/**
	 * 通过Date格式化一个 科学倒序 的时间字符串
	 * @param before 需要格式化的时间 1分钟前 /三小时前/ 昨天19：30 / 09-30
	 * @return
	 */
	public static String getTimeLineString(Date before) {
		return getTimeLineString(before, false);
	}

	/**
	 * 通过Date格式化一个 科学倒序 的时间字符串 
	 * @param before 需要格式化的时间
	 * @return
	 */
	public static String getTimeLineString(Date before,boolean isProfile) {
		if(before==null) {
			return "未知";
		}

		Date after = new Date();
		if(after.before(before)) {
			return "1分钟前";
		}

		Calendar calendarBefore = Calendar.getInstance();
		calendarBefore.setTime(before);
		int yearbefore = calendarBefore.get(Calendar.YEAR);
		int dayBefore = calendarBefore.get(Calendar.DAY_OF_YEAR);

		Calendar calendarNow = Calendar.getInstance();
		calendarNow.setTime(after);
		int yearNow = calendarNow.get(Calendar.YEAR);
		int dayNow = calendarNow.get(Calendar.DAY_OF_YEAR);


		if(yearNow > yearbefore){
			if(isProfile){
				return formateDateTimeWithOutYear(before);
			}else{
				return formateDate3(before);
			}
		}

		if(dayNow - dayBefore > 1){
			if(isProfile){
				return formateDateTimeWithOutYear(before);
			}else{
				return formateDate3(before);
			}
		}

		if(dayNow - dayBefore == 1){
			return  "昨天 "+formateTime(before);
		}


		long afterL = after.getTime();
		long beforeL = before.getTime();
		long n = Math.abs(afterL-beforeL);

		n /= 1000;
		long minute = n / 60;
		long hour = minute / 60; // 最终相差小时
		minute %= 60;  // 最终相差分钟

		if(hour >= 1) {
			return  formateTime(before);
		}

		if(minute < 1) {
			minute = 1;
		}

		return minute + "分钟前";

	}

	/**
	 * 用一个较靠前的时间对象，和当前时间对比。<br/>
	 * 详细规则查看：{@link #between(Date, Date)}
	 *
	 * @param before
	 * @return
	 */
	public static String betweenWithCurDate(Date before) {
		return between(before, new Date());
	}

	/**
	 * 获取某一天所在的周的第一天(星期一)
	 *
	 * @param date
	 * @return
	 */
	public static Date getFirstDayByWeek(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		int day = calendar.get(Calendar.DAY_OF_WEEK);
		if (day == 1) {
			calendar.add(Calendar.DAY_OF_MONTH, -6);
		} else {
			calendar.add(Calendar.DAY_OF_MONTH, -(day - 2));
		}
		return calendar.getTime();
	}

	/**
	 * 获取某一天所在的周的最后一天(星期日)
	 *
	 * @param date
	 * @return
	 */
	public static Date getLastDayByWeek(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		int day = calendar.get(Calendar.DAY_OF_WEEK);
		if (day != 1) {
			calendar.add(Calendar.DAY_OF_MONTH, 7 - (day - 1));
		}
		return calendar.getTime();
	}

	/**
	 * 获取时间对象的时间戳表示，即1970年以来的秒数
	 *
	 * @param date
	 * @return
	 */
	public static long formateTimestamp(Date date) {
		return date.getTime() / 1000;
	}

	/**
	 * 格式化日期对象为字符串，格式为：yyyyMMddHHmmss
	 *
	 * @param date
	 * @return
	 */
	public static String formateDateTime2(Date date) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
		return dateFormat.format(date);
	}

	/**
	 * 格式化日期对象为字符串，格式为：yyyyMMddHHmmssSSS
	 *
	 * @param date
	 * @return
	 */
	public static String formateDateTime5(Date date) {
		if(date == null) {
			return "UNKNOWN";
		}
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmssSSS");
		return dateFormat.format(date);
	}

	/**
	 * 格式化日期对象为字符串，格式为：yyyyMMdd
	 *
	 * @param date
	 * @return
	 */
	public static String formateDateTime11(Date date) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
		return dateFormat.format(date);
	}

	/**
	 * 格式化日期对象为字符串，格式为：HHmmss
	 *
	 * @param date
	 * @return
	 */
	public static String formateDateTime12(Date date) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("HHmmss");
		return dateFormat.format(date);
	}

	/**
	 * 格式化日期对象为字符串，格式为：MM-dd HH:mm
	 *
	 * @param date
	 * @return
	 */
	public static String formateDateTime3(Date date) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd HH:mm");
		return dateFormat.format(date);
	}



	/**
	 * 格式化一个日期对象，格式为：yyyy-MM-dd
	 *
	 * @param date
	 * @return
	 */
	public static String formatDate(Date date) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		return dateFormat.format(date);
	}

	/**
	 * 格式化一个字符串为日期对象，字符串格式为：yyyy-MM-dd HH:mm:ss
	 *
	 * @param date
	 * @return
	 */
	public static String formateDateTime(Date date) {
		if(date == null || date.getTime() == 0){
			return "未知时间";
		}
		SimpleDateFormat dateFormat = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss");
		return dateFormat.format(date);
	}

	/**
	 * 格式化一个字符串为日期对象，字符串格式为：MM-dd HH:mm
	 *
	 * @param date
	 * @return
	 */
	public static String formateDateTimeWithOutYear(Date date) {
		if(date == null || date.getTime() == 0){
			return "未知时间";
		}
		SimpleDateFormat dateFormat = new SimpleDateFormat(
				"MM-dd HH:mm");
		return dateFormat.format(date);
	}

	/**
	 * 格式化一个字符串为日期对象，字符串格式为：yyyy-MM-dd HH:mm
	 *
	 * @param date
	 * @return
	 */
	public static String formateDateTime4(Date date) {
		SimpleDateFormat dateFormat = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm");
		return dateFormat.format(date);
	}

	/**
	 * 格式日期，格式为：yyyyMMdd，不包含时间
	 *
	 * @param date
	 *            日期对象
	 * @return
	 */
	public static String formateDate2(Date date) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
		return dateFormat.format(date);
	}

	/**
	 * 格式日期，格式为：MM-dd，不包含时间
	 *
	 * @param date
	 *            日期对象
	 * @return
	 */
	public static String formateDate3(Date date) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd");
		return dateFormat.format(date);
	}

	/**
	 * 格式时间，格式为：HH:mm，不包含日期
	 *
	 * @param date
	 *            日期对象
	 * @return
	 */
	public static String formateTime(Date date) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");
		return dateFormat.format(date);
	}

	/**
	 * 将一个 yyyy-MM-dd 格式的字符串转换为 Date 对象，忽略时间
	 *
	 * @param dateStr
	 * @return
	 */
	public static Date parseStringToDate(String dateStr) {
		if(StringUtils.isEmpty(dateStr)) {
			return null;
		}

		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Date date = null;
		try {
			date = dateFormat.parse(dateStr);
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
		return date;
	}

	/**
	 * 将时间戳(秒)字符串转换为 Date 对象
	 *
	 * @param timestamp
	 *            时间戳，单位是“秒”
	 * @return
	 */
	public static Date parseTimeStampToDate(long timestamp) {
		if(timestamp <= 0) {
			return null;
		}

		Date date = new Date();
		date.setTime(timestamp * 1000); // JAVA的Date对象以毫秒来表示时间戳
		return date;
	}


	/*public static Date parseTimeStampToDate2(long milSeconds){
		if(milSeconds <= 0) {
			return null;
		}

		Date date = new Date();
		date.setTime(milSeconds); // JAVA的Date对象以毫秒来表示时间戳
		return date;
	}*/

	/**
	 * 将一个 yyyyMMdd 格式的字符串转换为 Date 对象，忽略时间
	 *
	 * @param dateStr
	 * @return
	 */
	public static Date parseStringToDate2(String dateStr) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
		Date date = null;
		try {
			date = dateFormat.parse(dateStr);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}

	/**
	 * 将一个 yyyy-MM-dd HH:mm:ss 格式的字符串转换为 Date 对象，对象必须包含时间细节
	 *
	 * @param dateStr
	 * @return
	 */
	public static Date parseStringToDateTime(String dateStr) {
		SimpleDateFormat dateFormat = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss");
		Date date = null;
		try {
			date = dateFormat.parse(dateStr);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}

	/**
	 * 将一个 yyyyMMddHHmmss 格式的字符串转换为 Date 对象，对象必须包含时间细节
	 *
	 * @param dateStr
	 * @return
	 */
	public static Date parseStringToDateTime2(String dateStr) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
		Date date = null;
		try {
			date = dateFormat.parse(dateStr);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}

	/**
	 * 将一个 yyyyMMddHHmmssSSS 格式的字符串转换为 Date 对象，对象必须包含时间细节
	 *
	 * @param dateStr
	 * @return
	 */
	public static Date parseStringToDateTime5(String dateStr) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmssSSS");
		Date date = null;
		try {
			date = dateFormat.parse(dateStr);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}

	/**
	 * 获取日期所在月的第一天(一号)
	 *
	 * @param date
	 * @return
	 */
	public static Date getFirstDayInMonth(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		return calendar.getTime();
	}

	/**
	 * 获取日期所在月的最后一天
	 *
	 * @param date
	 * @return
	 */
	public static Date getLastDayInMonth(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.DATE, 1);
		calendar.roll(Calendar.DATE, -1);

		Calendar c = Calendar.getInstance();
		c.setTime(new Date());
		int month = c.get(Calendar.MONTH);

		return calendar.getTime();
	}

	/**
	 * 获取某一日期的星期表示（星期一为1，星期天为7）
	 *
	 * @param date
	 * @return
	 */
	public static int getWeekDay(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);

		int day = calendar.get(Calendar.DAY_OF_WEEK) - 1;

		if (day == 0)
			day = 7;

		return day;
	}

	/**
	 * 获取今天的星期表示（星期一为1，星期天为7）
	 *
	 * @return
	 */
	public static int getWeekDay() {
		Calendar calendar = Calendar.getInstance();

		int day = calendar.get(Calendar.DAY_OF_WEEK) - 1;

		if (day == 0)
			day = 7;

		return day;
	}

	/**
	 * 获取今天是几号
	 * @return
	 */
	public static int getMonthDay(){
		Calendar calendar = Calendar.getInstance();
		int day = calendar.get(Calendar.DAY_OF_MONTH);
		return day;
	}
	
	/**
	 * 获取昨天的日期
	 *
	 * @param date
	 * @return
	 */
	public static Date getYesterday(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.DATE, calendar.get(Calendar.DATE) - 1);

		return calendar.getTime();
	}

	/**
	 * 明天
	 *
	 * @param date
	 * @return
	 */
	public static Date getTomorrow(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.DATE, calendar.get(Calendar.DATE) + 1);
		return calendar.getTime();
	}

	/**
	 * 某天的开始时间，即零点
	 *
	 * @param date
	 * @return
	 */
	public static Date getDateOfBegin(Date date) {
		String dateStr = formatDate(date);

		dateStr += " 00:00:00";

		return parseStringToDateTime(dateStr);
	}

	/**
	 * 某天的结束时间，即第二天的零点
	 *
	 * @param date
	 * @return
	 */
	public static Date getDateOfEnd(Date date) {
		String dateStr = formatDate(getTomorrow(date));

		dateStr += " 00:00:00";

		return parseStringToDateTime(dateStr);
	}

	/**
	 * 计算年龄。如果生日在当前系统时间之前，则返回-1
	 * @param birthDay
	 * @return
	 */
	public static int getAge(Date birthDay) {
        Calendar cal = Calendar.getInstance();

        if (cal.before(birthDay)) {
            return -1;
        }

        int yearNow = cal.get(Calendar.YEAR);
        int monthNow = cal.get(Calendar.MONTH);
        int dayOfMonthNow = cal.get(Calendar.DAY_OF_MONTH);

        cal.setTime(birthDay);
        int yearBirth = cal.get(Calendar.YEAR);
        int monthBirth = cal.get(Calendar.MONTH);
        int dayOfMonthBirth = cal.get(Calendar.DAY_OF_MONTH);

        int age = yearNow - yearBirth;

        if (monthNow <= monthBirth) {
            if (monthNow == monthBirth) {
                if (dayOfMonthNow < dayOfMonthBirth) {
                    age--;
                }
            } else {
                age--;
            }
        } else {
        }

        return age;
    }


	public static Date getServerDate() {
		Calendar c = Calendar.getInstance();
		try {
			URL url = new URL("http://www.beijing-time.org/time.asp");
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setConnectTimeout(3000);
			conn.connect();
			InputStream is = conn.getInputStream();
			Properties properties = new Properties();
			properties.load(is);
			is.close();

			c.set(Calendar.YEAR, Integer.valueOf(((String)(properties.get("nyear"))).replace(";", "")));
			c.set(Calendar.MONTH, Integer.valueOf(((String)(properties.get("nmonth"))).replace(";", ""))-1);
			c.set(Calendar.DATE, Integer.valueOf(((String)(properties.get("nday"))).replace(";", ""))-1);
			c.set(Calendar.HOUR, Integer.valueOf(((String)(properties.get("nhrs"))).replace(";", "")));
			c.set(Calendar.MINUTE, Integer.valueOf(((String)(properties.get("nmin"))).replace(";", "")));
			c.set(Calendar.SECOND, Integer.valueOf(((String)(properties.get("nsec"))).replace(";", "")));

		} catch (Throwable e) {
			e.printStackTrace();
		}

		return c.getTime();
	}

	public static boolean isSameDay(Date date1, Date date2) {
		if(date1 == null || date2 == null) {
			return false;
		}

		if(date1.getYear() != date2.getYear()) {
			return false;
		}

		if(date1.getDate() != date2.getDate()) {
			return false;
		}

		if(date1.getMonth() != date2.getMonth()) {
			return false;
		}

		return true;

	}
}
