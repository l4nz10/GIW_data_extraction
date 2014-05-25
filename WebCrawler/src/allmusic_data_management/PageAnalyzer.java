package allmusic_data_management;

import config.ConfigReader;
import edu.uci.ics.crawler4j.crawler.Page;

public class PageAnalyzer {
	
	private static final String PREFIX_1 = "http://www.allmusic.com/album/",
								PREFIX_2 = "http://www.allmusic.com/genre/",
								PREFIX_3 = "http://www.allmusic.com/artist/";
	private static final String FILTER_PATH = "/album/";
	
	public static boolean shouldVisit(String href) {
		return href.startsWith(PREFIX_1) ||
			   href.startsWith(PREFIX_2) ||
			   href.startsWith(PREFIX_3);
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
		if (mustProcess(page) && IdManager.getId()<ConfigReader.getMaxPage())
			process(page);
	}
}
