package utils.collector.uums;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DateUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 
 */
@Slf4j
public class DateUtil {

	public static Date parseDate(String dateStr, String parsePatterns) {
		Date date = null;
		try {
			date = DateUtils.parseDate(dateStr, parsePatterns);
		} catch (ParseException e) {
			log.error("日期转换失败");
		}
		return date;
	}

	public static Date cutHourMinAndSecond(Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String str = sdf.format(date);
		try {
			return sdf.parse(str);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}

}
