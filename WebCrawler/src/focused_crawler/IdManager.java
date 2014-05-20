package focused_crawler;

public class IdManager {
	
	private static Integer docID = 1;
	private static final String DIGITS = "00000"; 
	
	private static synchronized Integer getIDAndIncrement() {
		Integer returnValue = docID;
		docID++;
		return returnValue;
	}
	
	public static String getID() {
		Integer intID = getIDAndIncrement();
		String stringID = ""+intID;
		return DIGITS.substring(stringID.length())+stringID;
	}
	
}
