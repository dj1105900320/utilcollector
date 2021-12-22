package utils.collector.uums;

import java.text.FieldPosition;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 
 * @author yuli
 *
 */
public class DateSerializer extends SimpleDateFormat {

	private static final long serialVersionUID = 1L;

	@Override
	public StringBuffer format(Date date, StringBuffer toAppendTo, FieldPosition fieldPosition) {
		StringBuffer sb = new StringBuffer();
		if (date == null) {
			sb.append("");
		} else {
			sb.append(date.getTime());
		}
		return sb;
	}

}
