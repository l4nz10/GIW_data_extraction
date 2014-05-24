package novasol_data_management;

public class CsvFormatter {
	
	public static String formatString(String string) {
		return "\"" + string.replaceAll("\"", "\\\"") + "\"";
	}
	
	public static String deFormatString(String string) {
		String s = string.substring(1, string.length()-1);
		return s.replace("\\\"", "\"");
	}
}
