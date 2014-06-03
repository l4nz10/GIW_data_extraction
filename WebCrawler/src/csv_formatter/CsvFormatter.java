package csv_formatter;

import java.util.List;

public class CsvFormatter {
	public static String formatString(String s) {
		return s.replaceAll("\\s", " ").replaceAll(";", "");
	}
	
	public static String formatListItem(String s) {
		return formatString(s).replaceAll("|", "");
	}
	
	public static String formatStringList(String[] stringArray) {
		StringBuilder builder = new StringBuilder();
		builder.append("[");
		for (String s : stringArray) {
			builder.append(formatListItem(s)).append("|");
		}
		builder.append("]");
		return builder.toString();
	}

	public static String formatItem(Object o) {
		if (o instanceof String)
			return formatString((String) o);
		if (o instanceof List)
			return formatStringList((String[]) o);
		return null;
	}
	
	
}
