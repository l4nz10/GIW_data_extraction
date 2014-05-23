package data_management;

import edu.uci.ics.crawler4j.crawler.Page;

public class PageAnalyzer {
	
	private static final String FILTER_PATH = "/p/I"; 
		
	public static boolean mustProcess(Page page) {
		return page.getWebURL().getPath().startsWith(FILTER_PATH);
	}
	
	private static void process(Page page) {
		String docID = IdManager.getID();
		HTMLFileWriter.writeToHTMLFile(docID, page);
		new DataExtractor(docID, page).extractData();
	}
	
	public static void checkAndProcess(Page page) {
		if (mustProcess(page))
			process(page);
	}
}
