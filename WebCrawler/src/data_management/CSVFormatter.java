package data_management;

public class CSVFormatter {
	public static String format(String s){
		if(s.contains(";")){
			return "\""+s+"\"";
		}else{
			return s;
		}
	}
}
