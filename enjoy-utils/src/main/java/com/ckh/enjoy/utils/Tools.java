package com.ckh.enjoy.utils;

import org.apache.commons.lang.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;





public class Tools {
	
	
	// HOUR SECOND
	public static final long HOUR = 3600L * 1000L;

	// DAY SECOND
	public static final long DAY = 24L * HOUR;

			
	public Tools() {
	}


	public static boolean isBlank(String str) {
		return (str == null || str.trim().length() == 0);
	}
	
	public static boolean isBlank(Collection<?> list) {
        return (list == null || list.isEmpty());
    }
	private boolean isDouble(String str) {  
	    if (null == str || "".equals(str)) {  
	        return false;  
	    }  
	    Pattern pattern = Pattern.compile("^[-\\+]?[.\\d]*$");  
	    return pattern.matcher(str).matches();  
	}  
	public static boolean isNumeric(String str) {
		if (str == null || str.trim().length() == 0) {
			return false;
		}
		int offset = 0;
		if (str.startsWith("-") || str.startsWith("+")) {
			if (str.length() > 1) {
				offset = 1;
			} else {
				return false;
			}
		}
		for (int i = offset; i < str.length(); i++) {
			if (!Character.isDigit(str.charAt(i))) {
				return false;
			}
		}
		return true;
	}
	
	//是否闰年
	public static boolean isLeapYear(Date date) {
		Calendar cal = Calendar.getInstance();
		if(date != null){
			cal.setTime(date);
		}
		int yearNow = cal.get(Calendar.YEAR);
		if(yearNow % 400 == 0 || (yearNow % 4 == 0 && yearNow % 100 != 0) ){
			return true;
		}
		return false;
	}
	
	public static String toUTF8(String arg)
    {
        byte b[] = arg.getBytes();
        char c[] = new char[b.length];
        for(int x = 0; x < b.length; x++)
            c[x] = (char)(b[x] & 0xff);

        return new String(c);
    }
	
	public static String toString(int i){
	    return Integer.valueOf(i).toString();
	}
	
    public static String toString(long l){
        return Long.valueOf(l).toString();
    }

	//JS的ESCAPE, JAVA版
	public static String escape(String src) {  
	    if(isBlank(src)) return "";
		int i;  
	    char j;  
	    StringBuffer tmp = new StringBuffer();  
	    tmp.ensureCapacity(src.length() * 6);  
	    for (i = 0; i < src.length(); i++) {  
	        j = src.charAt(i);  
	        if (Character.isDigit(j) || Character.isLowerCase(j)  
	                || Character.isUpperCase(j))  
	            tmp.append(j);  
	        else if (j < 256) {  
	            tmp.append("%");  
	            if (j < 16)  
	                tmp.append("0");  
	            tmp.append(Integer.toString(j, 16));  
	        } else {  
	            tmp.append("%u");  
	            tmp.append(Integer.toString(j, 16));  
	        }  
	    }  
	    return tmp.toString();  
	}  
	 
	public static String unescape(String src) {  
		if(isBlank(src)) return "";
	    StringBuffer tmp = new StringBuffer();  
	    tmp.ensureCapacity(src.length());  
	    int lastPos = 0, pos = 0;  
	    char ch;  
	    while (lastPos < src.length()) {  
	        pos = src.indexOf("%", lastPos);  
	        if (pos == lastPos) {  
	            if (src.charAt(pos + 1) == 'u') {  
	                ch = (char) Integer.parseInt(src  
	                        .substring(pos + 2, pos + 6), 16);  
	                tmp.append(ch);  
	                lastPos = pos + 6;  
	            } else {  
	                ch = (char) Integer.parseInt(src  
	                        .substring(pos + 1, pos + 3), 16);  
	                tmp.append(ch);  
	                lastPos = pos + 3;  
	            }  
	        } else {  
	            if (pos == -1) {  
	                tmp.append(src.substring(lastPos));  
	                lastPos = src.length();  
	            } else {  
	                tmp.append(src.substring(lastPos, pos));  
	                lastPos = pos;  
	            }  
	        }  
	    }  
	    return tmp.toString();  
	}

	// max 是负数的话就全部替换
	public static String replace(String text, String repl, String with, int max) {
		if (text == null || repl == null || with == null || repl.length() == 0
				|| max == 0) {
			return text;
		}
		StringBuffer buf = new StringBuffer(text.length());
		int start = 0, end = 0;
		while ((end = text.indexOf(repl, start)) != -1) {
			buf.append(text.substring(start, end)).append(with);
			start = end + repl.length();
			if (--max == 0) {
				break;
			}
		}
		buf.append(text.substring(start));
		return buf.toString();
	}

	// 全部替换
	public static String replace(String text, String repl, String with) {
		return replace(text, repl, with, -1);
	}

	// finalLength 是替换后总的字符串长度.
	public static String getAB(String source, String ab, int replaceShowCount,
			int finalLength) {
		if (isBlank(source)) {
			return source;
		}
		if (source.length() <= finalLength) {
			return source;
		}
		if (ab == null) {
			ab = ".";
		}
		StringBuffer sb = new StringBuffer();

		// 已经是str.length > num;
		int length = ab.length() * replaceShowCount;
		int position = finalLength;
		if (finalLength - length < finalLength) {
			position = finalLength - length;
		}
		if (position < 0) {
			while (position < 0 && replaceShowCount > 0) {
				position += ab.length();
				replaceShowCount--;
			}
		}
		// 证明num一开始就为<0
		if (position < 0) {
			return source;
		}
		sb.append(source.substring(0, position));

		for (int i = 0; i < replaceShowCount; i++) {
			sb.append(ab);
		}
		return sb.toString();
	}

	public static String getAB(String source, int replaceShowCount,
			int finalLength) {
		return getAB(source, ".", replaceShowCount, finalLength);
	}

	public static String getAB(String source, int finalLength) {
		return getAB(source, ".", 3, finalLength);
	}

	public static String stringValue(String v, String def) {
		if (v == null || v.length() == 0)
			return def;
		return v.trim();
	}

	public static String[] stringArrayValue(String[] v, String[] def) {
		if (v == null || v.length == 0)
			return def;
		return v;
	}

	public static byte byteValue(String v, byte def) {
		if (v == null || v.length() == 0)
			return def;
		try {
			return Byte.parseByte(v);
		} catch (Exception e) {
			return def;
		}
	}

	public static char charValue(String v, char def) {
		if (v == null || v.length() == 0)
			return def;
		try {
			return (char) Integer.parseInt(v);
		} catch (Exception e) {
			return def;
		}
	}

	public static int intValue(String v, int def) {
		if (v == null || v.length() == 0)
			return def;
		try {
			return Integer.parseInt(v.trim());
		} catch (Exception e) {
			return def;
		}
	}
        
    /**
    * 初始化变量 (min <= result <= max)
    * @param v 数字字符串
    * @param def 默认值
    * @param min 最小值
    * @param max 最大值
    * @return result
    */
    public static int intValue(String v, int def, int min, int max) {
        return Math.min(Math.max(intValue(v, def), min), max);
    }

	public static Integer integerValue(String v) {
		return integerValue(v, null);
	}
	
	public static Integer integerValue(String v, int def) {
		if (isBlank(v))
			return new Integer(def);
		try {
			return Integer.valueOf(v);
		} catch (Exception e) {
			return new Integer(def);
		}
	}
	public static Integer integerValue(String v, Integer def) {
		if (isBlank(v))
			return def;
		try {
			return Integer.valueOf(v);
		} catch (Exception e) {
			return def;
		}
	}

	public static long longValue(String v, long def) {
		if (v == null || v.length() == 0)
			return def;
		try {
			return Long.parseLong(v.trim());
		} catch (Exception e) {
			return def;
		}
	}
	
	public static long[] longValue(String[] v, long def) {
        if (v == null || v.length == 0)
            return new long[0];
        try {
            long[] lv = new long[v.length];
            for(int i = 0,s = v.length;i<s;i++){
                lv[i] = Tools.longValue(v[i], def);
            }
            return lv;
        } catch (Exception e) {
            return new long[0];
        }
    }
	
	public static long[] longValue(String[] v, long[] def) {
		if (v == null || v.length == 0)
			return def;
		try {
			long[] lv = new long[v.length];
			for(int i = 0,s = v.length;i<s;i++){
				lv[i] = (Long.parseLong(v[i].trim()));
			}
			return lv;
		} catch (Exception e) {
			return def;
		}
	}

	public static boolean booleanValue(String v, boolean def) {
		if (v == null || v.length() == 0)
			return def;

		if (v.equalsIgnoreCase("true") || v.equalsIgnoreCase("yes")
				|| v.equalsIgnoreCase("1")) {
			return true;
		} else if (v.equalsIgnoreCase("false") || v.equalsIgnoreCase("no")
				|| v.equalsIgnoreCase("0")) {
			return false;
		} else {
			return def;
		}
	}

	public static float floatValue(String v, float def) {
		if (v == null || v.length() == 0)
			return def;
		try {
			return Float.parseFloat(v.trim());
		} catch (Exception e) {
			return def;
		}
	}
	
	public static float floatValue(String v ,int remain, float def) {
		try {
			BigDecimal bd = new BigDecimal(v);
			bd = bd.setScale(remain,BigDecimal.ROUND_HALF_UP);
			return bd.floatValue();
		} catch (Exception e) {
			return def;
		}
	}

	public static double doubleValue(String v, double def) {
		if (v == null || v.length() == 0)
			return def;
		try {
			return Double.parseDouble(v.trim());
		} catch (Exception e) {
			return def;
		}
	}

	public static Date dateValue(String v, String fm, Date def) {
		if (v == null || v.length() == 0)
			return def;
		try {
			return new SimpleDateFormat(fm).parse(v.trim());
		} catch (Exception e) {
			return def;
		}
	}

    public static Date dateValue(String v, String fm) {
        if (v == null || v.length() == 0)
            return null;
        try {
            return new SimpleDateFormat(fm).parse(v.trim());
        } catch (Exception e) {
            return null;
        }
    }

	public static Date dateValue(String v, Date def) {
		return dateValue(v, "yyyy-MM-dd", def);
	}


	public static Date datetimeValue(String v, Date def) {
		return dateValue(v, "yyyy-MM-dd HH:mm:ss", def);
	}

	//自动判断时间格式
	//timeStart=true,精确到时间的开始，timeStart=false精确到时间的结束，用于没有指定时分秒的情况
	public static Date dateValue2(String v, Date def, boolean timeStart) {
		if (v == null || v.length() == 0)return def;
		if(v.matches("\\d+-\\d+-\\d+ \\d+:\\d+")){
			if(timeStart){
				v += ":00";
			}else{
				v += ":59";
			}
			//System.out.println("-----2-"+v);
		}else if(v.matches("\\d+-\\d+-\\d+ \\d+")){
			if(timeStart){
				v += ":00:00";
			}else{
				v += ":59:59";
			}
		}else if(v.matches("\\d+-\\d+-\\d+")){
			if(timeStart){
				v += " 00:00:00";
			}else{
				v += " 23:59:59";
			}
		}
		return dateValue(v, "yyyy-MM-dd HH:mm:ss", def);
	}
	
	public static long periodValue(String value, long deflt) {
		if (value == null)
			return deflt;

		long period = 0;
		long sign = 1;// 正负标识
		int i = 0;// 其实位置
		int length = value.length();

		if (length > 0 && value.charAt(i) == '-') {
			sign = -1;
			i++;
		}

		while (i < length) {
			long delta = 0;
			char ch;

			for (; i < length && (ch = value.charAt(i)) >= '0' && ch <= '9'; i++) {
				delta = 10 * delta + ch - '0';
			}

			if (i >= length) {
				period += 1000 * delta;// 毫秒数
			} else {
				switch (value.charAt(i++)) {
				case 's':
				case 'S':
					period += 1000 * delta;
					break;
				case 'm':
					period += 60 * 1000 * delta;
					break;
				case 'h':
				case 'H':
					period += 60L * 60 * 1000 * delta;
					break;
				case 'd':
				case 'D':
					period += DAY * delta;
					break;
				case 'w':
				case 'W':
					period += 7L * DAY * delta;
					break;
				default:
					return deflt;
				}
			}
		}
		return sign * period;
	}

	/** ************************取默认value值的结束********************************** */

	public static Date getNow() {
		return new Date(System.currentTimeMillis());
	}

	// 得到今天凌晨的时间.
	public static Date getToday() {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		return cal.getTime();
	}
	//得到今日23：59 时间
	public static Date getTodayLast() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        cal.set(Calendar.MILLISECOND, 999);
        return cal.getTime();
    }
	//得到某一天的凌晨时间
	public static Date getToday(Date date){
	    Calendar cal = Calendar.getInstance();
	    cal.setTime(date);
	    cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
	}
	//得到某一天的23：59 时间
	public static Date getTodayLast(Date date){
	    Calendar cal = Calendar.getInstance();
	    cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        cal.set(Calendar.MILLISECOND, 999);
        return cal.getTime();
	}
	//得到几天前当天时间===================================================================
	public static Date getSomeDate(Date date,int dayNum){	    
	    Calendar cal = Calendar.getInstance();
	    long DAY = 1000*3600*24;
	    cal.setTimeInMillis(date.getTime()+DAY*(long)dayNum);
	    return cal.getTime();
	}
	//得到几月前当天时间===================================================================
    public static Date getSomeMonthDate(Date date,int monthNum){
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.MONTH, cal.get(Calendar.MONTH)+monthNum);
        return cal.getTime();
    }
	//得到24小时内某小时的开始时间
	public static Date getSubsectionHourBegin(Date date,int sub){
	    Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, sub);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
	}
	//得到24小时内某小时的末尾时间
	public static Date getSubsectionHourEnd(Date date,int sub){
	    Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, sub);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        cal.set(Calendar.MILLISECOND, 999);
        return cal.getTime();
	}
	//得到一个月内所有天
	public static List<Date> getAllDayByMonth(Date date){
	    List<Date> list = new ArrayList<Date>();
        //得到本月的开始
        Date d1 =getMonthStart(date);
        list.add(d1);
        //得到下月的开始时间
        Date d2 =getMonthStart(getSomeMonthDate(date,1));
        //得到本月结束时间
        Date d3 =getSomeDate(d2,-1);
        Date d4 = d1;
        while(d4.getDate()!=d3.getDate()){
            //从本月开始时间计算+1  到结束时间
            d4 = getSomeDate(d4,1);
            list.add(d4);
        }
	    return list;
	}
	//得到本月开始之到当前日期的时间
    public static List<Date> getAllDayByMonthAndDay(Date date){
        List<Date> list = new ArrayList<Date>();
        //得到本月的开始
        Date d1 =getMonthStart(date);
        list.add(d1);
        Date d4 = d1;
        while(d4.getDate()!=date.getDate()){
            //从本月开始时间计算+1  到结束时间
            d4 = getSomeDate(d4,1);
            list.add(d4);
        }
        return list;
    }
	
	public static Date getYesterday() {
		Date today = Tools.getNow();
	 	long t = today.getTime();
	 	Date date = new Date(t - 24 * 60 * 60 *1000l );
	 	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	    try{
	    	date = sdf.parse(sdf.format(date));
	    }catch (ParseException e) {

	    }
	    return date;
	}

	public static Date getTheDay(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		return cal.getTime();
	}

	public static int getYear(Date date) {
		Calendar cld = Calendar.getInstance();
		cld.setTime(date);
		return cld.get(Calendar.YEAR);
	}

	// 返回的月数是 自然月-1 也就是说返回的月是从 0--11
	public static int getMonth(Date date) {
		Calendar cld = Calendar.getInstance();
		cld.setTime(date);
		return cld.get(Calendar.MONTH);
	}

	public static int getDate(Date date) {
		Calendar cld = Calendar.getInstance();
		cld.setTime(date);
		return cld.get(Calendar.DAY_OF_MONTH);
	}

	public static int getHour(Date date) {
		Calendar cld = Calendar.getInstance();
		cld.setTime(date);
		return cld.get(Calendar.HOUR_OF_DAY);
	}
	
	public static int getMin(Date date) {
		Calendar cld = Calendar.getInstance();
		cld.setTime(date);
		return cld.get(Calendar.MINUTE);
	}

	public static String format(Date date, String fmt) {
		DateFormat formatter = new SimpleDateFormat(fmt);
		return formatter.format(date);
	}

	public static String format(Date date) {
		return format(date, "yyyy-MM-dd");
	}

	public static String format() {
		return format(new Date(System.currentTimeMillis()), "yyyy-MM-dd");
	}
	
	public static String formatDateTime(Date date){
		return format(date,"yyyy-MM-dd HH:mm:ss");
	}
	
	public static String formatDateTime() {
		return format(new Date(System.currentTimeMillis()), "yyyy-MM-dd HH:mm:ss");
	}

	// 得到这个星期开始的时间,星期的开始从getFirstDayOfWeek()得到
	public static Date getThisWeekStart() {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, -(cal.get(Calendar.DAY_OF_WEEK) - 1));
		return getTheDay(cal.getTime());
	}
	//设置时间 yyyy-MM-dd HH:mm:ss.SSS 相应的字段
	public static Date createDate(int year,int month,int day,int hour,int minute,int second, int milliSecond){
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, year);
		cal.set(Calendar.MONTH,month);
		cal.set(Calendar.DAY_OF_MONTH,day);
		cal.set(Calendar.HOUR_OF_DAY,hour);
		cal.set(Calendar.MINUTE, minute);
		cal.set(Calendar.SECOND,second);
		cal.set(Calendar.MILLISECOND,milliSecond);
		return cal.getTime();
	}

	// 本月的开始
	public static Date getThisMonthStart() {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.DAY_OF_MONTH, 1);
		return getTheDay(cal.getTime());
	}
	// 本月的开始
    public static Date getMonthStart(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.DAY_OF_MONTH, 1);
        return getTheDay(cal.getTime());
    }

	public static Date getTheMonthStart(int amount) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(getThisMonthStart());
		cal.add(Calendar.MONTH, amount);
		return cal.getTime();
	}
	
	// 本月的结束
	public static Date getThisMonthEnd() {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.DATE, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
		return getTodayLast(cal.getTime());
	}
	// 本月的结束
	public static Date getThisMonthEnd(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.set(Calendar.DATE, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
		return getTodayLast(cal.getTime());
	}
	
	//n分钟前或后 + -
	public static Date addMinute(Date date, int minute){
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.MINUTE, minute);
		return new Date(cal.getTime().getTime());
	}
	
	public static Date addSeconds(Date date, int seconds) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.SECOND, seconds);
        return calendar.getTime();
    }

	//n小时前或后 + -
    public static Date addHour(Date date, int hour) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.HOUR_OF_DAY, hour);
        return new Date(cal.getTime().getTime());
    }
	//n天前或后 + -
    public static Date addDay(Date date, int day) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE, day);

        return new Date(cal.getTime().getTime());
    }

	//n月前或后 + -
    public static Date addMonth(Date date, int month) {
    	Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.MONTH, month);

        return new Date(cal.getTime().getTime());
    }

	//n年前或后 + -
    public static Date addYear(Date date, int year) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.YEAR, year);

        return new Date(cal.getTime().getTime());
    }
	
    //计算两个日期之间的天数   
    public static int getDays(Date  date1,Date  date2)   {   
        int   days   =   0;
        days   =   (int)   (   Math.abs((date2.getTime()   -   date1.getTime()))   /   (24   *   60   *   60   *   1000));   
        return   days;   
    }
    
    public static int getDays(String  date1,String  date2)   {
        int   days   =   0;
        days   =   (int)   (   Math.abs((dateValue(date1,"yyyy-Mm-dd",new Date()).getTime()   -   dateValue(date2,"yyyy-Mm-dd",new Date()).getTime()))   /   (24   *   60   *   60   *   1000));
        return   days;   
    }
    //计算两个日期之间的时间差 详细到秒 返回类型为String
    public static String getDayDif(Date date1,Date date2){
        long DAY=24*60*60*1000;
        long between  = Math.abs((date2.getTime()   -   date1.getTime()));
        long day=between/DAY;
        long hour=(between/(60*60*1000)-day*24);
        long min=((between/(60*1000))-day*24*60-hour*60);
        long s=(between/1000-day*24*60*60-hour*60*60-min*60);
        return ""+day+"天"+hour+"小时"+min+"分"+s+"秒";
    }
    //时间格式为yyyy-MM-dd HH:mm:ss
    public static String getDayDif(String date1,String date2){
        long DAY=24*60*60*1000;
        long between  = Math.abs(  dateValue(date1,"yyyy-MM-dd HH:mm:ss",new Date()).getTime()  -   dateValue(date2,"yyyy-MM-dd HH:mm:ss",new Date()).getTime());
        long day=between/DAY;
        long hour=(between/(60*60*1000)-day*24);
        long min=((between/(60*1000))-day*24*60-hour*60);
        long s=(between/1000-day*24*60*60-hour*60*60-min*60);
        return ""+day+"天"+hour+"小时"+min+"分"+s+"秒";
    }
    
    public static java.sql.Date d2d(Date date){
		return new java.sql.Date(date.getTime());
	}
    
	public static URL getClassURL(Class clasz) {
		String strName = clasz.getName();
		strName = strName.replace('.', '/');
		strName = "/" + strName + ".class";
		return clasz.getResource(strName);
	}	
	
	static public boolean empty(String e)
	{
		return e==null || e.length()==0;
	}
	
	static public String splitString(String[] str){
		String result = "";
		if(str == null || str.length == 0){
			return result;
		}
		for(int i=0; i<str.length; i++){
			result += str[i];
			if(i != (str.length - 1)){
				result += ",";
			}
		}
		return result;
	}
	
	static public List string2List(String str,String split){
		List list = null;
		String temp[] = string2Array(str,split);
		if(temp == null)return list;
		list = Arrays.asList(temp);
		return list;
	}
	
	static public String[] string2Array(String str,String split){
		List list = new ArrayList();
		if(str == null || str.equals("")){
			return null;
		}
		String temp[] = str.split(split);
		if(split.equals("")){
			String result[] = new String[temp.length-1];
			for(int i=0;i<result.length;i++){
				result[i]=temp[i+1];
			}
			return result;
		}else{
			return temp;
		}
	}
	
	public static  List<String> string2ListNoBlank(String str,String split){
		List<String> list = new ArrayList<String>(1);
		
		String temp[] = str.split(split);
		if(!split.equals("")){
			list = new ArrayList<String>(temp.length);
			for(int i=0;i<temp.length;i++){
				if(!isBlank(temp[i])){
					list.add(temp[i]);
				}
			}
		}else{
			list.add(str);
		}
				
		return list;
	}
	
	
	static public List string2LongArray(String str,String split){
		List list = string2List(str,split);
		if(list==null || list.isEmpty()) return null;
		
		List result = new ArrayList();
		for(int i=0; i<list.size(); i++){
			result.add(Long.parseLong(list.get(i).toString()));
		}
		return result;
	}
	
	static public boolean equalLongArray(List first, List second){
		if(first==null || second==null || first.size()!=second.size()) return false;
		Map tmp = new HashMap();
		for(int i=0; i<first.size(); i++){
			tmp.put(first.get(i).toString(), 0);
		}
		for(int i=0; i<second.size(); i++){
			if(!tmp.containsKey(second.get(i).toString())){
				return false;
			}
		}
		tmp.clear();
		return true;
	}
	
	static public String array2String(String str[],String split){		
		if(str == null){
			return "";
		}
		String result = "";
		for(int i=0;i<str.length;i++){
			if(!result.equals("")){
				result += split;
			}
			result += str[i];
		}
				
		return result;
	}
	
	static public String list2String(List list,String split){		
		if(list == null){
			return "";
		}
		String result = "";
		for(int i=0;i<list.size();i++){
			if(!result.equals("")){
				result += split;
			}
			result += String.valueOf(list.get(i));
		}				
		return result;
	}
	
	//可以保留表情的cutstring，一个表情算两个中文，并已经过滤html标签
	public static String cutString(String value, int length,String suffix) {
        String strValue = value;
        int iLength;
        boolean cut = false;
        iLength = length;
        int lengthByte = length * 2;
        if (value == null || value.equals("")) return "";
        

        try {
        	//一个表情算两个中文
        	String temp = "~@@@$1";
        	strValue=strValue.replaceAll("<img src=http://www1.pcbaby.com.cn/sns/images/comment/face([0-9]*).gif>", temp);
            
            if (strValue.length() < length){
            	strValue = strValue.replaceAll("~@@@([0-9][0-9][0-9])", "<img src=http://www1.pcbaby.com.cn/sns/images/comment/face$1.gif>");
            	//strValue = toHTML(strValue);
            	strValue = toHTML(strValue,"<br>",true);
            	return strValue;
            }
            if (strValue.length() > lengthByte){
                strValue = strValue.substring(0, lengthByte);
                cut = true;
            }

            byte[] bytes = strValue.getBytes("GBK");
            if (bytes.length <= lengthByte) {
            } else {
            	cut = true;
                byte[] newBytes = new byte[lengthByte];
                System.arraycopy(bytes, 0, newBytes, 0, lengthByte);
                strValue = new String(newBytes, "GBK");              
                if (strValue.charAt(strValue.length() - 1) == (char) 65533) {
                    strValue = strValue.substring(0, strValue.length() - 1);
                }            
            }
            strValue = strValue.replaceAll("~@@@[0-9]{0,2}$", "");
            strValue = strValue.replaceAll("~@@@([0-9][0-9][0-9])", "<img src=http://www1.pcbaby.com.cn/sns/images/comment/face$1.gif>");
            //strValue = toHTML(strValue);
            strValue = toHTML(strValue,"<br>",true);

        } catch (Exception ex) {
        	//System.out.println("截取字符串出错"+ex);
            return "";
        }
        if(!cut)suffix="";
        return strValue+suffix;
    }
	
	//去除html标签和空格，只保留文本部分
	public static String justContent(String strValue, int length,String suffix) {
		if (strValue == null || strValue.equals("")) return "";
		//去掉img标签属性里的">"符号
        strValue = strValue.replaceAll("width>screen", "");
        Pattern p = null;
        Matcher m = null;
        String pattern = "<[^<>]*>?";
        p = Pattern.compile(pattern,Pattern.CASE_INSENSITIVE);
	    m=p.matcher(strValue);
	    strValue = m.replaceAll("");
	    strValue = strValue.replaceAll("(\\s|　|&nbsp;)*", "");
	    
        int iLength;
        boolean cut = false;
        iLength = length;
        int lengthByte = length * 2;
        

        try {
        	//一个表情算两个中文
        	//String temp = "~$1";
        	//strValue=strValue.replaceAll("<img src=\\\"http://www1.pckids.com.cn/2008/mykids/blog/images/face/f([0-9]*).gif\\\"/>", temp);
            
            if (strValue.length() < length){
            	//strValue = strValue.replaceAll("~([0-9][0-9][0-9])", "<img src=\"http://www1.pckids.com.cn/2008/mykids/blog/images/face/f$1.gif\"/>");
            	strValue = startPreChange(strValue);
                strValue = toHTML(strValue,"",false);
                strValue = endPreChange(strValue);
            	return strValue;
            }
            if (strValue.length() > lengthByte){
                strValue = strValue.substring(0, lengthByte);
                cut = true;
            }

            byte[] bytes = strValue.getBytes("GBK");
            if (bytes.length <= lengthByte) {
            } else {
            	cut = true;
                byte[] newBytes = new byte[lengthByte];
                System.arraycopy(bytes, 0, newBytes, 0, lengthByte);
                strValue = new String(newBytes, "GBK");              
                if (strValue.charAt(strValue.length() - 1) == (char) 65533) {
                    strValue = strValue.substring(0, strValue.length() - 1);
                }            
            }
            //strValue = strValue.replaceAll("~[0-9]{0,2}$", "");
            //strValue = strValue.replaceAll("~([0-9][0-9][0-9])", "<img src=\"http://www1.pckids.com.cn/2008/mykids/blog/images/face/f$1.gif\"/>");
            strValue = startPreChange(strValue);
            strValue = toHTML(strValue,"",false);
            strValue = endPreChange(strValue);
            

        } catch (Exception ex) {
        	//System.out.println("justContent出错"+ex);
            return "";
        }
        if(!cut)suffix="";
        return strValue+suffix;
    }
	
	
	//计算实际内容的长度，去除html标签对长度的影响
	public static String subContentString(String str,int length,String suffix){
		if(str == null || str.equals(""))return "";
		//先去除onload事件的影响
		str = str.replaceAll("onload=\"javascript:if\\(this\\.width>screen\\.width-600\\)this\\.width=screen\\.width-600;\"", "");
		int cnt = 0;
		int cntTemp = 0;
		int lengthByte = length * 2;
		Matcher m = null;
        Pattern p = Pattern.compile(">([^<>]+)<",Pattern.CASE_INSENSITIVE);
        m=p.matcher(str);
        while(m.find()){
        	String s = m.group().replaceAll(">|<", "").replaceAll("&nbsp;", " ").trim();
        	if(s != null && s.length() > 0){
				try {
					byte[] b = s.getBytes("GBK");
					cntTemp = cnt;
					cnt += b.length;
	        		if(cnt >= lengthByte && cntTemp < lengthByte){
	        			int i = m.start();
	        			i += (lengthByte - cntTemp)/2;
	        			String result = str.substring(0,i>str.length()?str.length():i);
	        			return result+suffix;
	        		}
	        		
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
					return str;
				}
        		
        	}
        }
        
		return str;
	}
	
	//去除html标签
	public static String trimHtmlTag(String strValue) {
		if (strValue == null || strValue.equals("")) return "";
		//去掉img标签属性里的">"符号
        strValue = strValue.replaceAll("width>screen", "");
        Pattern p = null;
        Matcher m = null;
        String pattern = "<[^<>]*>?";
        p = Pattern.compile(pattern,Pattern.CASE_INSENSITIVE);
	    m=p.matcher(strValue);
	    strValue = m.replaceAll("");       
        return strValue;
    }
	
	
	//截取中文长度，不过滤html标签，用于日记
	public static String cutStringWithoutFilter(String value, int length,String suffix) {
        String strValue = value.trim();
        int iLength;
        boolean cut = false;
        iLength = length;
        int lengthByte = length * 2;
        if (value == null || value.equals("")) return "";
        try {
            if (strValue.length() < length){
            	return strValue;
            }
            if (strValue.length() > lengthByte){
                strValue = strValue.substring(0, lengthByte);
                cut = true;
            }

            byte[] bytes = strValue.getBytes("GBK");
            if (bytes.length <= lengthByte) {
            } else {
            	cut = true;
                byte[] newBytes = new byte[lengthByte];
                System.arraycopy(bytes, 0, newBytes, 0, lengthByte);
                strValue = new String(newBytes, "GBK");              
                if (strValue.charAt(strValue.length() - 1) == (char) 65533) {
                    strValue = strValue.substring(0, strValue.length() - 1);
                }            
            }
            strValue = reFillHtml(strValue);
            
        } catch (Exception ex) {
        	//System.out.println("截取字符串出错"+ex);
            return "";
        }
        if(!cut)suffix="";
        return strValue+suffix;
    }
	
	/**
	 * @param string
	 * @return
	 */
	//补回裁掉的html标签,用于截取日记
	public static String reFillHtml(String string) {
		if(string == null || string.trim().equals("")){
             return string;
        }
		//System.out.println("s----:"+string);
		//去除img属性的>号
		string = string.replaceAll("width>screen", "width#_gt_#screen");
		//去除最后的不完整的标签
		string = string.replaceAll("<[^<>]*$", "");
		//替换所有单标签
		string = string.replaceAll("<([^<>]*)/>", "#_lt_#$1/#_gt_#");
		string = string.replaceAll("<br>", "#_lt_#br#_gt_#");
		//替换所有成对标签
		while(true){
			String temp = string;
			//System.out.println("---t--:"+temp);
			string = string.replaceAll("<([^/][^<>]*)>([^<>]*)</([^<>]*)>", "#_lt_#$1#_gt_#$2#_lt_#/$3#_gt_#");
			//System.out.println("---s--:"+string);
			if(temp.equals(string)){
				//处理异常情况多出来的结束标签
				string = string.replaceAll("</[^<>]*>", "");
				break;
			}
		}
		//补回不完整的标签
		while(true){
			String temp = string;
			string = string.replaceAll("<([^/][^\\s<>]*)([^<>]*)>([^<>]*)$", "#_lt_#$1$2#_gt_#$3#_lt_#/$1#_gt_#");
			//System.out.println("---2--:"+string);
			if(temp.equals(string)){
				//处理异常情况多出来的开始标签
				string = string.replaceAll("<[^<>]*>", "");
				break;
			}
		}
		string = string .replaceAll("#_lt_#", "<").replaceAll("#_gt_#", ">");
		try {
			//System.out.println("bytes----:"+string.getBytes("GBK").length);
			while(string.getBytes("GBK").length >= 2000){
				//System.out.println(string.getBytes("GBK").length+" >= 2000 ");
				string = string.replaceFirst("<([^/][^<>]*)>([^<>]*)</([^<>]*)>", "");
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	    //System.out.println("e----:"+string);	    
	    return string;
    }
	

	
	public static String toHTML(String str) {
        return toHTML(str,"<br>",false);
    }
	
	public static String toHTML(String str,String enter) {
        return toHTML(str,enter,false);
    }
	//连续超过2个以上的<br> 换成一个。
	public static String toBrs(String str){
	    if(str==null)return str;
		str = str.replaceAll("(<br>){3,}", "<br>");
		return str;
	}
	
	public static String toHTML(String str, String enter, boolean exceptEm) {
        if(str == null || str.equals(""))return "";
        //不过滤表情
        if(exceptEm){
        	str=str.replaceAll("<img src=http://www1.pcbaby.com.cn/sns/images/comment/face([0-9]*).gif>", "#img$1#");
        }
        str = Tools.replace(str, "&", "&amp;");
        str = Tools.replace(str, "<", "&lt;");
        str = Tools.replace(str, ">", "&gt;");
        //str = T.replace(str, "  ", "&nbsp;&nbsp;");
        str = Tools.replace(str, " ", "&nbsp;");
        str = Tools.replace(str, "\r\n", "\n");
        str = Tools.replace(str, "\n", enter);
        str = Tools.replace(str, "\t", "&nbsp;&nbsp;&nbsp;&nbsp;");
//        str = T.replace(str, "'", "‘");
//        str = T.replace(str, "\"", "“");
        str = Tools.replace(str, "'", "&#039;");
        str = Tools.replace(str, "\"", "&#034;");
        if(exceptEm){
        	str=str.replaceAll("#img([0-9]*)#", "<img src=http://www1.pcbaby.com.cn/sns/images/comment/face$1.gif>");
        }
        return str;
    }

    public static String startPreChange(String str) {
    	str = Tools.replace(str, "&times;", "#times;");
    	str = Tools.replace(str, "&hellip;", "#hellip;");
        str = Tools.replace(str, "&amp;", "#amp;");
        str = Tools.replace(str, "&lt;", "#lt;");
        str = Tools.replace(str, "&gt;", "#gt;");
        str = Tools.replace(str, "&nbsp;", "#nbsp;");
        str = Tools.replace(str,"<br>","#br#");
        str = Tools.replace(str, "&ldquo;", "#ldquo;");//转换左双引号
        str = Tools.replace(str, "&rdquo;", "#rdquo;");//转换右双引号
        str = Tools.replace(str, "&divide;", "#divide;");//转换除于号
        str = Tools.replace(str, "&mdash;", "#mdash;");//转换破折号
        return str;
    }

    public static String endPreChange(String str) {
    	str = Tools.replace(str, "#times;", "&times;");
    	str = Tools.replace(str, "#hellip;", "&hellip;");
        str = Tools.replace(str, "#amp;", "&amp;");
        str = Tools.replace(str, "#lt;", "&lt;");
        str = Tools.replace(str, "#gt;", "&gt;");
        str = Tools.replace(str, "#nbsp;", "&nbsp;");
        str = Tools.replace(str,"#br#","<br>");
        str = Tools.replace(str, "#ldquo;", "&ldquo;");
        str = Tools.replace(str, "#rdquo;", "&rdquo;");
        str = Tools.replace(str, "#divide;", "&divide;");
        str = Tools.replace(str, "#mdash;", "&mdash;");
        return str;
    }
    
    public static String encode(String str){
    	if(str == null || str.equals(""))return "";
    	try {
			str = java.net.URLEncoder.encode(str,"GBK");
			
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return str;
    }
    public static String decode(String str){
    	if(str == null || str.equals(""))return "";
    	try {
			str = java.net.URLDecoder.decode(str,"GBK");
			
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return str;
    }
    public static String encode(String str, String code){
    	if(str == null || str.equals(""))return "";
    	try {
			str = java.net.URLEncoder.encode(str,code);
			
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return str;
    }
    public static String decode(String str, String code){
    	if(str == null || str.equals(""))return "";
    	try {
			str = java.net.URLDecoder.decode(str,code);
			
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return str;
    }
    //计算字符串的长度，一个中文算2
    public static int getLength(String str){
    	if(empty(str)){
    		return 0;
    	}
    	int reInt = 0;
    	char[] chars = str.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            reInt += String.valueOf(chars[i]).getBytes().length;
        }
        return reInt;
    }
    
    /** 将URL转换为可以拼到提交参数中的字符串 */
	public static String encodeURL(String s) throws Exception {
		return s == null ? null : java.net.URLEncoder.encode(s, "GBK");
	}
	
	public static String encodeURL(String s,String charset) throws Exception {
		return s == null ? null : java.net.URLEncoder.encode(s, charset);
	}
	
	/**
	 * 将字符串截取指定字节数长度。
	 * 因为java里面存放字符是使用unicdoe，一个字符占两个字节。
	 * 而在页面显示的时候，中文字符占的宽度是英文的两倍，所以为了整齐，
	 * 需要进行字节级别的字符串截取。
	 * 如果需要截取的长度，刚好把一个中文字符切开了，那么返回结果就减少一个字符
	 * @param str 需要处理的字符串
	 * @param cutLen 需要截取的字节数
	 * @return 经过截取的字符串
	 */
	public static String byteSubstring(String str, int cutLen) {
		return byteSubstring(str, cutLen, null);
	}
	
	/**
	 * 根据字节数截取字符串，请参考byteSubstring(String, int)
	 * 不同的是，如果截取得到的字符串长度小于原字符串，会加入省略符号
	 * 不过如果省略符号是null或者空串，也直接返回截取后的字符串
	 * @param str 需要处理的字符串
	 * @param cutLen 需要截取的长度（字节数）
	 * @param suspensionSymbol 省略符号
	 * @return 经过截取的字符串
	 */
	public static String byteSubstring(String str, int cutLen, String suspensionSymbol) {
        if (empty(str))
        	return str;
        if (cutLen <= 0)
        	return "";
        int reInt = 0;
        if (!empty(suspensionSymbol)) {
        	char[] sArray = suspensionSymbol.toCharArray();
        	int suspensionSymbolLength = 0;
        	for (int i = 0; i < sArray.length; i++) {
        		suspensionSymbolLength += String.valueOf(sArray[i]).getBytes().length;
        	}
        	reInt += suspensionSymbolLength;
        }
        String reStr = "";
        char[] chars = str.toCharArray();
        for (int i = 0; (i < chars.length && cutLen > reInt); i++) {
            reInt += String.valueOf(chars[i]).getBytes().length;
            reStr += chars[i];
        }
        reStr = reInt > cutLen ? reStr.substring(0, reStr.length() -1) : reStr;
		return !empty(suspensionSymbol) && str.length() > reStr.length() ? reStr + suspensionSymbol : reStr;
	}
	
	/**
	 * 根据字节数截取字符串，请参考byteSubstring(String, int)
	 * 不同的是，如果截取得到的字符串长度小于原字符串，会加入省略符号
	 * 不过如果省略符号是null或者空串，也直接返回截取后的字符串
	 * @param str 需要处理的字符串
	 * @param cutLen 需要截取的长度（字节数）
	 * @param suspensionSymbol 省略符号
	 * @param isFilterUBB 是否过滤掉UBB代码
	 * @return 经过截取的字符串
	 */
	public static String byteSubstring(String str, int cutLen, String suspensionSymbol, boolean isFilterUBB) {
		if(isFilterUBB){
			String s = byteSubstring(str,cutLen,suspensionSymbol);
			s = simpleStringFilter(s,"img");
			s = simpleStringFilter(s,"url");
			s = simpleStringFilter(s,"email");
			s = simpleStringFilter(s,"FLASH");
			s = simpleStringFilter(s,"RM");
			s = simpleStringFilter(s,"code");
			s = simpleStringFilter(s,"quote");
			s = simpleStringFilter(s,"color");
			s = simpleStringFilter(s,"fly");
			s = simpleStringFilter(s,"move");
			s = simpleStringFilter(s,"glow");
			s = simpleStringFilter(s,"shadow");
			s = simpleStringFilter(s,"b");
			s = simpleStringFilter(s,"i");
			s = simpleStringFilter(s,"u");
			s = simpleStringFilter(s,"center");
			s = simpleStringFilter(s,"size");
			s = simpleStringFilter(s,"face");
			s = simpleStringFilter(s,"align");
			s = simpleStringFilter(s,"em");
			s = simpleStringFilter(s,"font");
			s = simpleStringFilter(s,"WMA");
			s = simpleStringFilter(s,"WMV");
			return s;
		}else{
			return byteSubstring(str,cutLen,suspensionSymbol);
		}
	}
	
	/**
	 * 过滤掉某个UBB的tag
	 * @param string 需要处理的字符串
	 * @param filter 需要过滤的UBB的tag
	 * @return 经过处理的字符串
	 */
	public static String simpleStringFilter(String string,String filter) {
        if(string == null || string.trim().equals("")){
             return string;
        }

        Pattern p = null;
        Matcher m = null;
        Pattern p1 = null;
        Matcher m1 = null;
        String pStr1 = "\\[unknown\\]";
        String pStr2 = "\\[/unknown\\]";
        if(filter.equalsIgnoreCase("url")) {
            pStr1 = "\\[url=[^\\[\\]]*\\]";
            pStr2 = "\\[/url\\]";
        }else if(filter.equalsIgnoreCase("email")) {
            pStr1 = "\\[email=[^\\[\\]]*\\]";
            pStr2 = "\\[/email\\]";
        }else if(filter.equalsIgnoreCase("color")) {
            pStr1 = "\\[\\s*color\\s*=\\s*(#?[a-z0-9].*?)\\]";
            pStr2 = "\\[\\s*/color\\s*\\]";
        }else if(filter.equalsIgnoreCase("glow")) {
            pStr1 = "\\[glow=[^\\[\\]]*\\]";
            pStr2 = "\\[/glow\\]";
        }else if(filter.equalsIgnoreCase("shadow")) {
            pStr1 = "\\[shadow=[^\\[\\]]*\\]";
            pStr2 = "\\[/shadow\\]";
        }else if(filter.equalsIgnoreCase("size")) {
            pStr1 = "\\[size=[^\\[\\]]*\\]";
            pStr2 = "\\[/size\\]";
        }else if(filter.equalsIgnoreCase("face")) {
            pStr1 = "\\[face=[^\\[\\]]*\\]";
            pStr2 = "\\[/face\\]";
        }else if(filter.equalsIgnoreCase("align")) {
            pStr1 = "\\[align=[^\\[\\]]*\\]";
            pStr2 = "\\[/align\\]";
        }else if(filter.equalsIgnoreCase("font")) {
            pStr1 = "\\[font=[^\\[\\]]*\\]";
            pStr2 = "\\[/font\\]";
        }else if(filter.equalsIgnoreCase("em")) {
            pStr1 = "\\[em([0-9]*)\\]";
            
        }else {
           pStr1 = pStr1.replaceAll("unknown",filter);
           pStr2 = pStr2.replaceAll("unknown",filter);
        }

         int start = 0 ;
         int end = 0;

        if(!filter.equals("em")) {
            p = Pattern.compile(pStr1,Pattern.CASE_INSENSITIVE);
            m=p.matcher(string);
            p1=Pattern.compile(pStr2,Pattern.CASE_INSENSITIVE);
            m1=p1.matcher(string);
            while(m.find()) {
                start = m.start();
                end = m.end();
                if(end < string.length()) {
                    if(!m1.find(end)){
                        string = string.substring(0,start) + string.substring(end, string.length());
                    }
                }else {
                	if(start < string.length()){ 
                        string=string.substring(0,start);
                	}
                }
            }
        }

        int left = string.lastIndexOf("[");
        int right = string.lastIndexOf("]");
        if(left >= 0) {
            if(left > right) {
                string = string.substring(0,left);
            }
        }
        return string;
    }
	
	public static boolean regulaxDomain(String domain){
		return Pattern.matches("[a-z_0-9]{2,20}", domain);
	}
    
	public boolean isShow(String StringSet, int index){  
		String[] sets = string2Array(StringSet,"");
		return sets[index].equals("1")? true:false;	
	}	
	
	/**
	 * 计算星座，返回的是星座的id，可以用于寻找同一星座的对象
	 * @param date
	 * @return
	 */
	public static int calConstellation(Date date) {
		if(date==null){
			//date = T.getNow();
			return -1;
		}
        SimpleDateFormat monthDayFormat = new SimpleDateFormat("MM-dd");
        String monthDay = monthDayFormat.format(date);
        if (monthDay.compareTo("03-21") >= 0 && monthDay.compareTo("04-20") <= 0) {
//            System.out.println("白羊座");
            return 1;
        }
        if (monthDay.compareTo("04-21") >= 0 && monthDay.compareTo("05-21") <= 0) {
//            System.out.println("金牛座");
            return 2;
        }
        if (monthDay.compareTo("05-22") >= 0 && monthDay.compareTo("06-21") <= 0) {
//            System.out.println("双子座");
            return 3;
        }
        if (monthDay.compareTo("06-22") >= 0 && monthDay.compareTo("07-22") <= 0) {
//            System.out.println("巨蟹座");
            return 4;
        }

        if (monthDay.compareTo("07-23") >= 0 && monthDay.compareTo("08-23") <= 0) {
//            System.out.println("狮子座");
            return 5;
        }

        if (monthDay.compareTo("08-24") >= 0 && monthDay.compareTo("09-23") <= 0) {
//            System.out.println("处女座");
            return 6;
        }

        if (monthDay.compareTo("09-24") >= 0 && monthDay.compareTo("10-23") <= 0) {
//            System.out.println("天秤座");
            return 7;
        }

        if (monthDay.compareTo("10-24") >= 0 && monthDay.compareTo("11-22") <= 0) {
//            System.out.println("天蝎座");
            return 8;
        }

        if (monthDay.compareTo("11-23") >= 0 && monthDay.compareTo("12-21") <= 0) {
//            System.out.println("射手座");
            return 9;
        }

        if (monthDay.compareTo("12-22") >= 0 && monthDay.compareTo("12-31") <= 0 ||
                monthDay.compareTo("01-01") >= 0 && monthDay.compareTo("01-20") <= 0) {
//            System.out.println("摩羯座");
            return 10;
        }
        if (monthDay.compareTo("01-21") >= 0 && monthDay.compareTo("02-18") <= 0) {
//            System.out.println("水瓶座");
            return 11;
        }

        if (monthDay.compareTo("02-19") >= 0 && monthDay.compareTo("03-20") <= 0) {
//            System.out.println("双鱼座");
            return 12;
        }
        return -1;
    }
	
	public static String calConstellationDesc(Date date) {
		if(date==null){
			return "";
		}
		int num = calConstellation(date);
		switch(num){
		case 1: return "白羊座";
		case 2: return "金牛座";
		case 3: return "双子座";
		case 4: return "巨蟹座";
		case 5: return "狮子座";
		case 6: return "处女座";
		case 7: return "天秤座";
		case 8: return "天蝎座";
		case 9: return "射手座";
		case 10: return "摩羯座";
		case 11: return "水瓶座";
		case 12: return "双鱼座";
		default: return "";
		}
	}
	
	public static final String[] zodiacArr = { "猴", "鸡", "狗", "猪", "鼠", "牛", "虎", "兔", "龙", "蛇", "马", "羊" };  
	
	/**   
	 * 根据日期获取生肖   
	 * @return   
	 */   
	public static String date2Zodiac(Calendar time) {    
	    return zodiacArr[time.get(Calendar.YEAR) % 12];  
	} 
	
	
	public static String age(Date birthday,Date baseDate) {   
		if(birthday==null)return  "0岁0个月";
		
	    Calendar now = Calendar.getInstance(); 
	    if(baseDate!=null){
	    	now.setTime(Tools.getTheDay(baseDate));
	    }else{
	    	now.setTime(Tools.getTheDay(new Date()));
	    }
	    Calendar birth = Calendar.getInstance();
	    birth.setTimeInMillis(now.getTimeInMillis()-birthday.getTime());
	    return  (birth.get(Calendar.YEAR)-1970)+"岁"+(birth.get(Calendar.MONTH))+"个月";
	} 
	
   /** 取得一个含有中文的字符串的字节长度 */
	public static int getByteLength(String s){
		return s==null?0:s.replaceAll("[^\\x00-\\xff]","12").length();
	}

	public static String calTimeInterval(Date date) {
		String str = "";		
		Date now = new Date();
		long interval = now.getTime() - date.getTime();
		long sec = interval/1000;
		long min = interval/(60*1000);
		if(sec >=1 && min<1){
			str = sec+"秒前";
		}else if(min >= 1 && min < 60){//大于1分钟，小于1小时
			str = min+"分钟前";
		}else if(min >= 60 && min < 24*60 ){//大于1小时，小于1天
			long hour = min/60;
			str = hour+"小时"+((min-hour*60) > 0 ? (min-hour*60)+"分钟前" : "前");
		}else if(min >= 24*60){//大于1天
			long day = min/(24*60);
			str = day+"天前";
		}
		return str;
	}
	
	public static String calTimeInterval2(Date date) {
		String str = "";		
		Date now = new Date();
		long interval = now.getTime() - date.getTime();
		long sec = interval/1000;
		long min = interval/(60*1000);
		if(sec >=1 && min<1){
			str = sec+"秒前";
		}else if(min >= 1 && min < 60){//大于1分钟，小于1小时
			str = min+"分钟前";
		}else if(min >= 60 && min < 24*60 ){//大于1小时，小于1天
			long hour = min/60;
			str = hour+"小时"+((min-hour*60) > 0 ? (min-hour*60)+"分钟前" : "前");
		}else if(min >= 24*60&&min < 24*60*30*12){//大于1天小于1年
			str = Tools.format(date, "MM-dd");
		}else{
			str = Tools.format(date);
		}
		return str;
	}
	
	public static String calTimeInterval3(Date date) {
		Calendar today = Calendar.getInstance();
		
		today.setTime(Tools.getTheDay(new Date()));
		if(date.compareTo(today.getTime())==1){
			return "今天";
		}
		today.add(Calendar.DAY_OF_MONTH , -1 );
		if(date.compareTo(today.getTime())==1){
			return "昨天";
		}
		today.setTime(Tools.getTheDay(new Date()));
		today.add(Calendar.DAY_OF_MONTH , -2 );
		if(date.compareTo(today.getTime())==1){
			return "前天";
		}
		today.setTime(Tools.getTheDay(new Date()));
		today.add(Calendar.DAY_OF_MONTH , -3 );
		if(date.compareTo(today.getTime())==1){
			return "三天前";
		}
		today.setTime(Tools.getTheDay(new Date()));
		today.add(Calendar.DAY_OF_MONTH , -7 );
		if(date.compareTo(today.getTime())==1){
			return "一周前";
		}
		today.setTime(Tools.getTheDay(new Date()));
		today.add(Calendar.MONTH , -1 );
		if(date.compareTo(today.getTime())==1){
			return "一个月前";
		}
		
		return "三个月前";
	}
	
	//获得包含数字和字母的随机字符串,bit为位数
	public static String getRandomStr(int bit){
        String[] str = {"0","1","2","3","4","5","6","7","8","9","A","B","C","D","E","F",
        		"G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z"};
        Random rdm = new Random();
        StringBuilder sb = new StringBuilder();
    	for(int j=0;j<bit;j++){
    		sb.append(str[rdm.nextInt(36)]);
    	}
        return sb.toString();	
	}
	
	//获得包含数字的随机字符串,bit为位数
	public static String getNumberRandomStr(int bit){
        String[] str = {"0","1","2","3","4","5","6","7","8","9"};
        Random rdm = new Random();
        StringBuilder sb = new StringBuilder();
    	for(int j=0;j<bit;j++){
    		sb.append(str[rdm.nextInt(10)]);
    	}
        return sb.toString();	
	}
	
	public static String getLocalHostIp(){
		InetAddress addr = null;
		String address = "";
		try{
			addr = InetAddress.getLocalHost();
			address = addr.getHostAddress().toString();
			
			if(address.indexOf("127.0.0.1")!=-1){
				Enumeration netInterfaces=NetworkInterface.getNetworkInterfaces();
				InetAddress ip = null;
				while(netInterfaces.hasMoreElements()){
					NetworkInterface ni=(NetworkInterface)netInterfaces.nextElement();
//					System.out.println(ni.getName());
					ip=(InetAddress)ni.getInetAddresses().nextElement();
					if(!ip.isSiteLocalAddress() && !ip.isLoopbackAddress() && ip.getHostAddress().indexOf(":")==-1){
						address = ip.getHostAddress();
						//System.out.println("本机的ip=" + ip.getHostAddress());
						break;
					}else{
						ip=null;
					}
				}
			}
		}catch(UnknownHostException uhe){
		}catch(SocketException se){
		}
		return address;//获得本机IP
	}
	
	//冒泡法排序
	public static void bubleSort(List<Integer> list,boolean asc){
		if(list==null)return;
		int n = list.size();
		for(int i=0; i<n; i++){
			for(int j=n-1; j>i; j--){
				int int1 = list.get(j);
				int int2 = list.get(j-1);
				if(asc){
					if(int1 < int2){
						list.set(j, int2);
						list.set(j-1, int1);
					}
				}else{
					if(int1 > int2){
						list.set(j, int2);
						list.set(j-1, int1);
					}
				}
			}
		}		  
	}
	
	//百分比,分子,分母,几位小数点
	public static String percent(double numerator, double denominator ,int radixPoint){
        String str;
        double  p3 = numerator / denominator;
        NumberFormat nf = NumberFormat.getPercentInstance();
        nf.setMinimumFractionDigits(radixPoint);
        str = nf.format(p3);
        return  str;
    }
	
	//转换成符合domain格式的字符串
	public static String toDomain(String domain){
		int length = domain.length();
		StringBuilder sb = new StringBuilder();
		for(int i=0;i<length;i++){
			char c = domain.charAt(i);
			if(Pattern.matches("[a-z_0-9]", String.valueOf(c))){
				sb.append(c);
			}
		}
		String result = sb.toString();
		if(result.length() < 2){
			result = getRandomStr(6).toLowerCase();
		}
		if(result.length() > 20){
			result = result.substring(0,20);
		}
		return result;
	}
	
	//根据静态化参数串获取相应参数值(支持斜杠和下划线)
	public static String getParam(String paramStr,String paramName){
		if(paramStr == null || paramStr.trim().equals("")){
			return "";
		}
		if(paramName == null || paramName.trim().equals("")){
			return "";
		}
		paramStr = paramStr.replaceAll("^(/||_)*","/").replaceAll("(/||_)*$","");
		Pattern p = Pattern.compile("(/||_)"+paramName+"[^(/||_)]*", Pattern.CASE_INSENSITIVE);
		Matcher m = p.matcher(paramStr);
		String result = "";
		if(m.find()){
			result = m.group().replaceFirst("(/||_)"+paramName, "");
		}
		return result;
	}
	/**
	 * 在据静态化参数串中增加变量,重复设置,覆盖现有值
	 * <code> addParam("/k%E5%A4%A9%E5%A4%A9/p4/","k更多") </code>
	 * @param param 现有变量串 不以/开头,以/结尾 多个 nvvvvv/ 连接成
	 * @param addParam 增加的变量串   nvvvvv
	 **/
	public static String addParam(String param,String addParam){
		if(Tools.isBlank(addParam))return param;
		if(Tools.isBlank(param))return addParam+"/";
		String k = addParam.substring(0, 1);
		if(!param.startsWith("/"))param = "/" + param;
		param = param.replaceAll("/"+k+"[^/]*/", "/");
		if(!param.endsWith("/"))param = param + "/";
		param = param+ addParam+"/";
		if(param.startsWith("/")){
			param = param.replaceFirst("/", "");
		}
		return param;
	}
	
	public static String toLowercaseStrHead(String str){
		if(str == null){
			return null;
		}
		if(str.length() > 0){
			return str.substring(0, 1).toLowerCase() + (str.length() > 1 ? str.substring(1) : "");
		}else{
			return "";
		}
	}
	
	public static String filtCode(String str) {
        if(str == null || str.equals(""))return "";
        str = Tools.replace(str, "<", "&lt;");
        str = Tools.replace(str, ">", "&gt;");
        //以下方式ie不支持
//        str = T.replace(str, "'", "&apos;");
//        str = T.replace(str, "\"", "&quot;");
        str = Tools.replace(str, "'", "&#039;");
        str = Tools.replace(str, "\"", "&#034;");
        return str;
    }
	
	public static String filtCode(String str,boolean onlyScript) {
        if(str == null || str.equals(""))return "";
        if(onlyScript){
        	str=replaceAll(str,"<(script)([^/]?[^<>]*)/>", "&lt;$1$2/&gt;",Pattern.CASE_INSENSITIVE);
        	str=replaceAll(str,"<(script)([^/]?[^<>]*)>([^<>]*)</?(script)>", "&lt;$1$2&gt;$3&lt;/$4&gt;",Pattern.CASE_INSENSITIVE);
        	//<meta http-equiv="refresh" content="0;url=http://www.zm345.cn/" target="_blank" />
        	str=Tools.replaceAll(str,"<(m|M)(e|E)(t|T)(a|A)([^/]?[^<>]*)/>", "&lt;m-e-t-a$5$2/&gt;",Pattern.CASE_INSENSITIVE);
        }else{
        	str = filtCode(str);
        }
        return str;
    }
	
	public static String replaceAll(String text,String regex,String with,int flag){
		return Pattern.compile(regex,flag).matcher(text).replaceAll(with);
	}
	
	/**
	 * 判断是否在容器里
	 */
    public static boolean isContains(List list, Object obj){
    	return list.contains(obj);
    }
	
    /**
     * 获取头像地址
     * @param id 编号
     * @param uri 图片地址前缀
     * @return
     */
    public static String face(long id, String uri){
        if(id <= 0) return "";
    	String picPath = uri + "/";
	    String aid = String.valueOf(id);
	    for(int i=0,s=aid.length();i<s;i++){
	        int k = (i+2)<=s?(i+2):(i+1);
	    	picPath += aid.substring(i, k) +"/";
	    	i++;
	    }
	    picPath = picPath+aid;
		return picPath;
    }
    

    /**
     * 分解request.getParameterMap()
     * @param params
     * @param name
     * @return
     */
	public static String getParameter(Map<String, String[]> params, String name) {
		String[] tmp = params.get(name);
		String result = null;
		if (tmp != null && tmp.length > 0) {
			result = tmp[0];
		}
		return result;
	}
	
    public static long getPendingTime(Date lastUpdate){
    	if(lastUpdate != null){
    		long now = Tools.getNow().getTime();
    		long pendingTime = now-lastUpdate.getTime();
    		return  pendingTime ;
    	}
    	else{
    		return 0;
    	}
    }


    /*转换难度星星, v=3.5 star='*' */
    public static String stars(String v, String star, String halfStar){
    	String[] vs = v.split("\\.");
		int nums = -1;
		try{
			nums = Integer.parseInt(vs[0]);
		}catch(Exception e){}
    	String stars = "";
		for(int i=0;i<nums;i++) stars += star;
    	if(vs.length>1){//has half
    		stars += halfStar;
    	}
    	return stars;
    }
    
	//获取离结束还有多少剩余时间
	public static String getRemainTimes(Date endTime) {
		Date now = Tools.getNow();
        long itv = endTime.getTime() - now.getTime();
        if(itv <= 0){
        	return "1小时";
        }
        long hours = itv /(3600*1000l);
        long days = hours / 24;
        return days+"天"+((hours - days*24) > 0?(hours - days*24)+"小时":"");
    }
	//获取离结束还有多少天
	public static long getRemainDay(Date endTime) {
		Date now = Tools.getNow();
		long itv = endTime.getTime() - now.getTime();
		if(itv <= 0){
			return 0;
		}
		long hours = itv /(3600*1000l);
		long days = hours / 24;
		return days;
	}
	
	//计算年龄，精确到月
	public static String getAge(Date birthDay,boolean showMonth)  {
        if(birthDay==null)return "保密";
		Calendar cal = Calendar.getInstance();
        if (cal.before(birthDay)) {
            return "保密";
        }

        int yearNow = cal.get(Calendar.YEAR);
        int monthNow = cal.get(Calendar.MONTH)+1;
        int dayOfMonthNow = cal.get(Calendar.DAY_OF_MONTH);
        
        cal.setTime(birthDay);

        int yearBirth = cal.get(Calendar.YEAR);
        int monthBirth = cal.get(Calendar.MONTH)+1;
        int dayOfMonthBirth = cal.get(Calendar.DAY_OF_MONTH);
        
//        showMonth = false;
//        int yearNow = 2009;
//        int monthNow = 1;
//        int dayOfMonthNow = 1;
//        
//        int yearBirth = 2008;
//        int monthBirth = 11;
//        int dayOfMonthBirth = 1;
        
        int age = yearNow - yearBirth;
        int month = monthNow - monthBirth;
        if(monthNow < monthBirth){
            age--;
            if(dayOfMonthNow < dayOfMonthBirth){
            	month--;
            }
            if(month < 0) month += 12;
        }else if(monthNow == monthBirth){
        	if(dayOfMonthNow < dayOfMonthBirth){
            	month--;
            }
        	if(month < 0){
        		age--;
        		month += 12;
        	}
        }else{
        	;
        }
        
        String str = "";
        if(age > 0){
        	str += age+"岁";
        }
        if(month >0 && month < 12){
        	str += month+"个月";
        }
        if(age <= 0 && month <=0){
        	str += "未足月";
        }
        //只显示到岁数
        if(!showMonth){
        	str = str.replaceFirst("\\d+个月", "").replaceFirst("未足月", "");
        }
        if(str.equals("")){
        	str = "保密";
        }
        return str;
    }
	
	//计算年龄，精确到日，一个月按30天计算
	public static String getAge3(Date birthDay)  {
        if(birthDay==null)return "保密";
		Calendar calNow = Calendar.getInstance();
		Calendar calBirth = Calendar.getInstance();
		calBirth.setTime(birthDay);
		//calBirth.set(Calendar.YEAR, 2007);
		//calBirth.set(Calendar.MONTH, 4);
		//calBirth.set(Calendar.DAY_OF_MONTH, 31);
        if (calNow.before(calBirth)) {
            return "保密";
        }
        int yearNow = calNow.get(Calendar.YEAR);
        int monthNow = calNow.get(Calendar.MONTH)+1;
        int dayNow = calNow.get(Calendar.DAY_OF_MONTH);
        
        int yearBirth = calBirth.get(Calendar.YEAR);
        int monthBirth = calBirth.get(Calendar.MONTH)+1;
        int dayBirth = calBirth.get(Calendar.DAY_OF_MONTH);
        
        int year = yearNow - yearBirth;
        int month = monthNow - monthBirth;
        int day = dayNow - dayBirth;
        if(day < 0){
        	month--;
        	day += 30;
        }
        if(month < 0){
        	year--;
        	month += 12;
        }
        String str = "";
        if(year > 0){
        	str += year+"岁";
        }
        if(month > 0){
        	str += month+"个月";
        }
        if(day > 0){
        	str += day+"天";
        }
        return str;
    }
	/**
	 * 年龄计算规则应该是“舍”而不应该是“进”，例如：4个半月的前台显示应是4个月，2岁7个月的应该显示2岁。
	 * @param birthDay
	 * @param showMonth
	 * @return
	 */
	public static String getAge2(Date birthDay,boolean showMonth)  {
        if(birthDay==null)return "保密";
		Calendar cal = Calendar.getInstance();
		Date now = Tools.getNow();
		cal.setTime(now);
        if (now.before(birthDay)) {
            return "保密";
        }
        

        int yearNow = cal.get(Calendar.YEAR);
        int monthNow = cal.get(Calendar.MONTH)+1;
        int dayOfMonthNow = cal.get(Calendar.DAY_OF_MONTH);
        
        cal.setTime(birthDay);

        int yearBirth = cal.get(Calendar.YEAR);
        int monthBirth = cal.get(Calendar.MONTH)+1;
        int dayOfMonthBirth = cal.get(Calendar.DAY_OF_MONTH);
        
        int age = yearNow - yearBirth;
        int month = monthNow - monthBirth;
        
        if(dayOfMonthNow < dayOfMonthBirth){
        	if( monthNow <= monthBirth){
            	age--;
            }
        	month--;
        }else{
        	if( monthNow < monthBirth){
            	age--;
            }
        }
        if(month < 0) month += 12;
        
        
        String str = "";
        if(age > 0){
        	str += age+"岁";
        }
        if(month >0 && month < 12){
        	str += month+"个月";
        }
        if(age <= 0 && month <=0){
        	str += "未满月";
        }
        //只显示到岁数
        if(!showMonth){
        	str = str.replaceFirst("\\d+个月", "").replaceFirst("未满月", "");
        }
        if(str.equals("")){
        	str = "保密";
        }
        return str;
    }
	
	public static String getAge4(Date birthDay)  {
        if(birthDay==null)return "保密";
		Calendar cal = Calendar.getInstance();
		Date now = Tools.getNow();
		cal.setTime(now);
        if (now.before(birthDay)) {
            return "保密";
        }
        

        int yearNow = cal.get(Calendar.YEAR);
        int monthNow = cal.get(Calendar.MONTH)+1;
        int dayOfMonthNow = cal.get(Calendar.DAY_OF_MONTH);
        
        cal.setTime(birthDay);

        int yearBirth = cal.get(Calendar.YEAR);
        int monthBirth = cal.get(Calendar.MONTH)+1;
        int dayOfMonthBirth = cal.get(Calendar.DAY_OF_MONTH);
        
        int age = yearNow - yearBirth;
        int month = monthNow - monthBirth;
        
        if(dayOfMonthNow < dayOfMonthBirth){
        	if( monthNow <= monthBirth){
            	age--;
            }
        	month--;
        }else{
        	if( monthNow < monthBirth){
            	age--;
            }
        }
        if(month < 0) month += 12;
        
        
        String str = "";
        if(age > 0){
        	str += age+"岁";
        }else if(month >0 && month < 12){
        	str += month+"个月";
        }else if(age <= 0 && month <=0){
        	str += "未满月";
        }
        
        if(str.equals("")){
        	str = "保密";
        }
        return str;
    }
	public static String getAge5(Date birthDay)  {
        if(birthDay==null)return "保密";
		Calendar cal = Calendar.getInstance();
		Date now = Tools.getNow();
		cal.setTime(now);
        if (now.before(birthDay)) {
            return "保密";
        }

        int yearNow = cal.get(Calendar.YEAR);
      
        cal.setTime(birthDay);

        int yearBirth = cal.get(Calendar.YEAR);
        
        int age = yearNow - yearBirth;
        
        
        
        String str = "";
        if(age > 0){
        	str = age+"";
        }
       
        if(str.equals("")){
        	str = "保密";
        }
        return str;
    }
    /**
     * 计算SNS用户经验值进度条的显示数据。
     * @param totalRank  系统中等级的总数
     * @param myRank     用户的等级
     * @param fullPixels 进度条满格时的像素值
     * @return
     * 
     */
    public static int getSNSRank(int totalRank , int myRank , int fullPixels){
        int iRet = 0 ;
        if(totalRank <= 0) return iRet ;
        double x  = ((double)myRank/(double)totalRank)*fullPixels;
        return Double.valueOf(x).intValue() ;        
    }
    
    /**
     * 过滤字符串里一般的分隔符
     * @param str
     * @return
     */
    public static String filterSeparatorForTag(String str) {
		String tag = "";
		if (str != null && !str.equals("")) {
			tag = str.replaceAll("，", ",").replaceAll(":", ",").replaceAll("、",
					",").replaceAll(";", ",").replaceAll("；", ",").replace("　",
					",").replaceAll(" ", ",");
		}
		return tag;
	}
    

    /***
     * 搜索结果,关键字增加样式.
     * @param s 
     * @param key
     * @param style
     * @return
     * 
     * eg.
     * T.addStyle(s,key,"&lt;i class=\"error\"&gl;$1&lt;/i&gl;")
     * 
     */
    public static String addStyle(String s, String key, String style){
    	String tem = "";
    	if(Tools.isBlank(s)){
    		tem = s;
    	}else{
    		if(Tools.isBlank(key)){
        		tem = s;
        	}else{
        		if(s.indexOf(key) == -1){
        			tem = s;
        		}else{
        			tem = s.replaceAll(key,style);
        		}
        	}
    	}
    	return tem;
    }
    
    public static BigDecimal toBigDecimal(String o){
    	return Tools.isBlank(o)?null: new BigDecimal(o);
    }
    
    public static Integer toInteger(String o){
    	return Tools.isBlank(o)?null: new Integer(o);
    }
    
    public int doubleToInt(double d){
    	return (int) d;
    }
    
    public static String addLegStr(String diary,String legstr){
    	return diary.replaceAll("</div>","</div>"+legstr).replaceAll("<br\\s*/?>","<br>"+legstr);
    }
    
    public static Double toDouble(String o){
    	return Tools.isBlank(o)?null: new Double(o);
    }
    
   
    /***
     * 返回当前的类加载器。
     * @return
     */
    public static ClassLoader getClassLoader() {
    	return Tools.class.getClassLoader();
    }
    

	

	public static String tHTML(String str) {
        if(Tools.isBlank(str))return "";
        str = Tools.replace(str, "&", "&amp;");
        str = Tools.replace(str, "<", "&lt;");
        str = Tools.replace(str, ">", "&gt;");
        str = Tools.replace(str, "'", "&#039;");
        str = Tools.replace(str, "\"", "&#034;");
        return str;
    }
	public static String rHTML(String str) {
        if(Tools.isBlank(str))return "";
        str = Tools.replace(str, "&amp;", "&");
        str = Tools.replace(str, "&lt;", "<");
        str = Tools.replace(str, "&gt;", ">");
        str = Tools.replace(str, "&#039;", "'");
        str = Tools.replace(str, "&#034;", "\"");
        return str;
    }
	
	public static String getIp(HttpServletRequest request){
		String ip = request.getHeader("X-Real-IP");
		if (!StringUtils.isBlank(ip) && !"unknown".equalsIgnoreCase(ip)) {
			if(ip.contains("../")||ip.contains("..\\")){
				return "";
			}
			return ip;
		}
		ip = request.getHeader("X-Forwarded-For");
		if (!StringUtils.isBlank(ip) && !"unknown".equalsIgnoreCase(ip)) {
			// 多次反向代理后会有多个IP值，第一个为真实IP。
			int index = ip.indexOf(',');
			if (index != -1) {
				ip= ip.substring(0, index);
			}
			if(ip.contains("../")||ip.contains("..\\")){
				return "";
			}
			return ip;
		} else {
			ip=request.getRemoteAddr();
			if(ip.contains("../")||ip.contains("..\\")){
				return "";
			}
			if(ip.equals("0:0:0:0:0:0:0:1")){
				ip="127.0.0.1";
			}
			return ip;
		}
	}
	public static boolean validate(HttpServletRequest request) {
		if (!request.getMethod().equalsIgnoreCase("POST")) {
			return false;
		}
		return true;
	}
	
	public static Integer[] getRandomInts(int max,int count,boolean canRepeat){
		if(max>0 && count >0){
			Random r = new Random();
			if(canRepeat){
				List<Integer> is = new ArrayList<Integer>();
				while(is.size() < count){
					is.add(r.nextInt(max));
				}
				return is.toArray(new Integer[count]);
			}else{
				Set<Integer> is = new HashSet<Integer>(count);
				count = count > max ? max :count;
				while(is.size() < count){
					is.add(r.nextInt(max));
				}
				return is.toArray(new Integer[count]);
			}
		}

		return new Integer[0];
	}
	
	public static String roundFileSize(long fs){
		if(fs<1024){  //小于1M
			return fs+"K";
		}else if(fs<1048576){ //小于1G
			BigDecimal s = new BigDecimal(fs).divide(new BigDecimal(1024),1,BigDecimal.ROUND_HALF_UP);
			return s.floatValue()+"M";
		}else{
			BigDecimal s = new BigDecimal(fs).divide(new BigDecimal(1048576),1,BigDecimal.ROUND_HALF_UP);
			return s.floatValue()+"G";
		}
	}

	/**
	 * 对付转义两次的问题
	 * @param str
	 * @return
	 */
	public static String antiFiltCode(String str) {
        if(str == null || str.equals(""))return "";
        str = Tools.replace(str, "&lt;", "<");
        str = Tools.replace(str, "&gt;", ">");
        str = Tools.replace(str, "&#039;", "'");
        str = Tools.replace(str, "&apos;", "'");
        str = Tools.replace(str, "&quot;", "\"");
        str = Tools.replace(str, "&amp;", "&");
        return str;
    }
	
	/**
	 * 去掉ubb代码
	 * @param str
	 * @param isKeepImg 是否将ubb图片标签转换成html格式
	 * @return
	 */
	public static String removeUBB(String str,boolean isKeepImg){
        if(str == null || str.equals(""))
        	return "";
        if (isKeepImg) {
			str = str.replaceAll("(?i)\\[img\\]http://", "<img class='image_link' src=\"http://").replaceAll("(?i)\\[/img\\]", "\"/>"); 
		}
        else {
			str = str.replaceAll("(?i)\\[img\\](.*?)\\[/img\\]", "");
		}
        str = str.replaceAll("\\[(\\w+)(\\=)(.*?)\\]", "");
        str = str.replaceAll("\\[(\\/\\w+)\\]", "");
        str = str.replaceAll("\\[[a-z0-9A-Z_]+\\]", "");
        return str;
	}
	

    //根据 孕几个月，几个月，几岁，几岁几个月 ，获得 时间
    public static Map<String,Date> getAgeDateBySearchWord(String searchWord){
    	Map<String,Date> birthdayMap = null;
    	
    	Date begin = null;
    	Date end = null;
    	if(searchWord==null || Tools.isBlank(searchWord)){
    		return null;
    	}
    	
    	Date theDay = Tools.getNow();
    	int number = 0;
    	if(searchWord.indexOf("孕")>=0){
    		number = Tools.intValue(searchWord.substring(1,2), 0);
    		begin = Tools.addMonth(theDay, 10-number);
    	} else if (searchWord.indexOf("个月")>=0 || searchWord.indexOf("岁")>=0) {
    		if (searchWord.indexOf("岁")>=0) {
    			if (searchWord.indexOf("个月")>=0) {
    				number = Tools.intValue(searchWord.substring(0,1), 0);
    				int month = Tools.intValue(searchWord.substring(2,3), 0);
    				begin = Tools.addYear(theDay, -number);
    				begin = Tools.addMonth(begin, -month);
    			} else {
    				number = Tools.intValue(searchWord.substring(0,1), 0);
    				begin = Tools.addYear(theDay, -number);
    			}
    		} else {
    			number = Tools.intValue(searchWord.substring(0,1), 0);
    			begin = Tools.addMonth(theDay, -number);
    		}
    	}
		
		
    	if(begin!=null){
			begin = Tools.getMonthStart(begin);
			end = begin;
			if(Tools.getYear(begin)<=2006){
				System.out.println("--"+Tools.formatDateTime(begin));
				begin = Tools.addMonth(begin, -Tools.getMonth(begin));
				end = Tools.addMonth(Tools.addYear(end, 1), -Tools.getMonth(end)-1);
			}
			birthdayMap = new HashMap<String, Date>();
			birthdayMap.put("begin", begin);
			birthdayMap.put("end", end);
		}
    	return birthdayMap;
    	//return begin;
    }
    
   
    
    /**
     * 过滤掉串中的html标签，防止js注入.
     * 
     * @param str
     *            需要处理的字符串
     * @param except
     *            是否保留<a></a>,<img>,<br>
     *            的html标签。true为保留待处理串中的<a></a>,<img>,<br>
     *            标签。
     * @return 经过截取的字符串
     */
    public static String filterHTML(String str, boolean except) {
        if (str == null || str.equals(""))
            return "";
        if (except) {
            str = str.replaceAll("<(a|A) ([^/]?[^<>]*)>(.+?)</?(a|A)>",
                    "#alink($2)($3)#");
            str = str.replaceAll("<(i|I)(m|M)(g|G) ([^/]?[^<>]*)>",
                    "!imglink($4)!");
            str = str.replaceAll("<(b|B)(r|R)([^/]?[^<>]*)>", "!brlink($3)!");
        }
        str = str.replaceAll("&", "&amp;");
        str = str.replaceAll("<", "&lt;");
        str = str.replaceAll(">", "&gt;");
        str = str.replaceAll("  ", "&nbsp;&nbsp;");
        str = str.replaceAll("\r\n", "\n");
        str = str.replaceAll("\n", "<br>");
        str = str.replaceAll("\t", "&nbsp;&nbsp;&nbsp;&nbsp;");
        if (except) {
            str = str.replaceAll("#alink\\((.*?)\\)\\((.*?)\\)#",
                    "<a $1>$2</a>");
            str = str.replaceAll("!imglink\\((.*?)\\)!", "<img $1>");
            str = str.replaceAll("!brlink\\((.*?)\\)!", "<br$1>");
        }
        return str;
    }
    
    public static String filterInput(String html) {
		if (html == null) return html;
		StringBuilder sb = new StringBuilder(html.length());
		for (int i = 0, c = html.length(); i < c; ++i) {
			char ch = html.charAt(i);
			switch (ch) {
			case '>':
				sb.append("&gt;"); break;
			case '<':
				sb.append("&lt;"); break;
			case '&':
				sb.append("&amp;"); break;
			case '"':
				sb.append("&quot;"); break;
			case '\'':
				sb.append("&#039;"); break;
			default:
				sb.append(ch); break;
			}
		}
		return sb.toString();
	}
    
    /**
     * 过滤map中字符串
     * @param params
     * @return 
     */
    public static Map<String, Object> filterInput(Map<String, Object> params) {
        Map<String, Object> result = params;
        if (result != null && !result.isEmpty()) {
            for(Map.Entry<String, Object> e : result.entrySet()) {
                if (e.getValue() instanceof String) {
                    e.setValue(filterInput((String)e.getValue()));
                }
            }
        }
        return result;
    }

    /**
     * 根据字节数截取字符串
     * 
     * @param value
     *            需要处理的字符串
     * @param byteLen
     *            需要截取的长度（字节数）
     * @return 经过截取的字符串
     */
    public static String byteSubstring2(String value, int byteLen) {
        if (value == null)
            return "";
        if (value.length() < byteLen / 2)
            return value;

        String strValue = null;
        try {
            if (value.length() > byteLen) {
                strValue = value.substring(0, byteLen);
            } else {
                strValue = value;
            }

            byte[] bytes = strValue.getBytes("GBK");
            if (bytes.length > byteLen) {
                strValue = new String(bytes, 0, byteLen, "GBK");

                if (strValue.charAt(strValue.length() - 1) == (char) 0xFFFD) {
                    strValue = strValue.substring(0, strValue.length() - 1);
                }
            }
        } catch (Exception ex) {
            return value;
        }

        return strValue;
    }
    
    public static String toHTML2(String str, String enter, boolean exceptA) {
        if(str == null || str.equals(""))return "";
        //不过滤A
        if(exceptA){
            str=str.replaceAll("<(a|A) ([^/]?[^<>]*)>(.+?)</?(a|A)>", "#alink($2)($3)#");
        }//System.out.println(str);
        /*str = replace(str, "&", "&amp;");
        str = replace(str, "<", "&lt;");
        str = replace(str, ">", "&gt;");
        str = replace(str, "  ", "&nbsp;&nbsp;");
        str = replace(str, "\r\n", "\n");
        str = replace(str, "\n", enter);
        str = replace(str, "\t", "&nbsp;&nbsp;&nbsp;&nbsp;");*/
        //str = replace(str, "'", "‘");
        //str = replace(str, "\"", "“");
        str = str.replaceAll("&", "&amp;");
        str = str.replaceAll("<", "&lt;");
        str = str.replaceAll(">", "&gt;");
        str = str.replaceAll("  ", "&nbsp;&nbsp;");
        str = str.replaceAll("\r\n", "\n");
        str = str.replaceAll("\n", enter);
        str = str.replaceAll("\t", "&nbsp;&nbsp;&nbsp;&nbsp;");
        if(exceptA){
            str=str.replaceAll("#alink\\((.*?)\\)\\((.*?)\\)#", "<a $1>$2</a>");
        }
        return str;
    }
    
    public static String md5(String text){
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] digest = md.digest(text.getBytes("utf-8"));
            StringBuilder buf = new StringBuilder();
            for (int i = 0; i < digest.length; i++) {
                String b =Integer.toHexString(0xFF & digest[i]);
                if (b.length() == 1) buf.append('0');
                buf.append(b);
            }
            return buf.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return text;
    }

	public static String md5(byte[] text){
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			byte[] digest = md.digest(text);
			StringBuilder buf = new StringBuilder();
			for (int i = 0; i < digest.length; i++) {
				String b =Integer.toHexString(0xFF & digest[i]);
				if (b.length() == 1) buf.append('0');
				buf.append(b);
			}
			return buf.toString();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return "";
	}
    
        /**
     * 根据referer来获取domain
     * @param referer
     * @return 
     */
    public static String getDomainByReferer(String referer) {
    	if(isBlank(referer)) {
            return "";
        }
        
    	String domain = referer.replaceAll(
                "^http://[^?#&]*\\.(pcpro|pcauto|pcgames|pclady|pcbaby|pchouse)\\.com\\.cn(\\:\\d+)?.*"
                , "$1");
    	return domain;
    }
    

    /**
     * 取小数点后两位
     * @param num
     * @return
     */
    public static double afterPoint2(double num){
    	 DecimalFormat formater = new DecimalFormat();
    	 formater.setMaximumFractionDigits(2);
    	 formater.setGroupingSize(0);
    	 formater.setRoundingMode(RoundingMode.FLOOR);
    	 return Tools.doubleValue(formater.format(num),0);
    }
    
    public static double myPercent(int y, int z) {  
        String baifenbi = "";// 接受百分比的值  
        double baiy = y * 1.0;  
        double baiz = z * 1.0;  
        double fen = baiy / baiz*100;  
        // NumberFormat nf = NumberFormat.getPercentInstance(); 注释掉的也是一种方法  
        // nf.setMinimumFractionDigits( 2 ); 保留到小数点后几位  
        DecimalFormat df1 = new DecimalFormat("##.00"); // ##.00%  
                                                            // 百分比格式，后面不足2位的用0补齐  
        // baifenbi=nf.format(fen);  
        baifenbi = df1.format(fen);  
     
        return Tools.doubleValue(baifenbi, 0);  
    }  
  
    public static byte[] toByteArray(InputStream input) throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        byte[] buffer = new byte[10240];
        int n = 0;
        while (-1 != (n = input.read(buffer))) {
            output.write(buffer, 0, n);
        }
        return output.toByteArray();
    }

    public static String filterEmoji(String source) {  
    	  if (source != null && source.length() > 0) {  
    	    return source.replaceAll("[\ud800\udc00-\udbff\udfff\ud800-\udfff]", "?");  
    	  } else {  
    	    return source;  
    	  }  
    }

    public static String string2Unicode(String string) {
        StringBuffer unicode = new StringBuffer();
        for (int i = 0; i < string.length(); i++) {
            // 取出每一个字符
            char c = string.charAt(i);
            // 转换为unicode
            unicode.append("\\u" + Integer.toHexString(c));
        }
        return unicode.toString();
    }
    
    public static Boolean isLinux() {
         String os = System.getProperty("os.name");
         return !os.toLowerCase().startsWith("win");
    }
    
    public static String getRandomNumCode(int number){
    		String codeNum = "";
    		int [] numbers = {0,1,2,3,4,5,6,7,8,9};
    		Random random = new Random();
    		for (int i = 0; i < number; i++) {
    			int next = random.nextInt(10000);//目的是产生足够随机的数，避免产生的数字重复率高的问题
				codeNum+=numbers[next%10];
			}
    		return codeNum; 
    }
	public static String guid(String pre) {
		return pre + UUID.randomUUID().toString().replace("-", "");
	}
    /**
     * @param resString
     * @return String
     * @throws
     * @Description 响应数据格式化
     * @author lgh
     * @date 2018/10/29-13:45
     */
    public static String responseFormat(String resString){

        StringBuffer jsonForMatStr = new StringBuffer();
        int level = 0;
        for(int index=0;index<resString.length();index++)//将字符串中的字符逐个按行输出
        {
            //获取s中的每个字符
            char c = resString.charAt(index);

            //level大于0并且jsonForMatStr中的最后一个字符为\n,jsonForMatStr加入\t
            if (level > 0  && '\n' == jsonForMatStr.charAt(jsonForMatStr.length() - 1)) {
                jsonForMatStr.append(getLevelStr(level));
            }
            //遇到"{"和"["要增加空格和换行，遇到"}"和"]"要减少空格，以对应，遇到","要换行
            switch (c) {
                case '{':
                case '[':
                    jsonForMatStr.append(c + "\n");
                    level++;
                    break;
                case ',':
                    jsonForMatStr.append(c + "\n");
                    break;
                case '}':
                case ']':
                    jsonForMatStr.append("\n");
                    level--;
                    jsonForMatStr.append(getLevelStr(level));
                    jsonForMatStr.append(c);
                    break;
                default:
                    jsonForMatStr.append(c);
                    break;
            }
        }
        return jsonForMatStr.toString();
    }
    /**
     * @param level
     * @return
     * @throws
     * @author lgh
     * @date 2018/10/29-14:29
     */
    private static String getLevelStr(int level) {
        StringBuffer levelStr = new StringBuffer();
        for (int levelI = 0; levelI < level; levelI++) {
            levelStr.append("\t");
        }
        return levelStr.toString();
    }

    public static String sortParamsToStr(Map<String, String> param){
        //签名步骤一：按字典排序参数
        List list=new ArrayList(param.keySet());
        Object[] ary =list.toArray();
        Arrays.sort(ary);
        list=Arrays.asList(ary);
        String str="";
        for(int i=0;i<list.size();i++){
            if(i == list.size() - 1){
                str+=list.get(i)+"="+param.get(list.get(i)+"");
            }else{
                str+=list.get(i)+"="+param.get(list.get(i)+"")+"&";
            }
        }

        return str;
    }

    public static String MD5Encode(String origin,String charsetName){
        String resultString=null;
        try{
            resultString=new String(origin);
            MessageDigest md=MessageDigest.getInstance("MD5");
            if(StringUtils.isBlank(charsetName)){
                resultString=byteArrayToHexString(md.digest(resultString.getBytes()));
            }else{
                resultString=byteArrayToHexString(md.digest(resultString.getBytes(charsetName)));
            }
        }catch(Exception e){

        }
        return resultString;
    }
    public static String byteArrayToHexString(byte b[]){
        StringBuffer resultSb=new StringBuffer();
        for(int i=0;i<b.length;i++){
            resultSb.append(byteToHexString(b[i]));
        }
        return resultSb.toString();
    }
    public static String byteToHexString(byte b){
        int n=b;
        if(n<0){
            n+=256;
        }
        int d1=n/16;
        int d2=n%16;
        return hexDigits[d1]+hexDigits[d2];
    }

    public static final String hexDigits[]={ "0", "1", "2", "3", "4", "5",
            "6", "7", "8", "9", "a", "b", "c", "d", "e", "f" };


}

