package novasol_data_management;

import edu.uci.ics.crawler4j.crawler.Page;

public class PageAnalyzer {
	
	private static final String PREFIX_1 = "http://www.novasol.it/r/",
								PREFIX_2 = "http://www.novasol.it/p/";
	private static final String FILTER_PATH = "/p/I";
	
	public static boolean shouldVisit(String href) {
		return href.startsWith(PREFIX_1) || href.startsWith(PREFIX_2);
	}
		
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
