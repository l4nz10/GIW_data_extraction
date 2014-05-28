package allmusic_data_management;

import config.ConfigReader;
import edu.uci.ics.crawler4j.crawler.Page;
import fileWriter.HTMLFileWriter;

public class PageAnalyzer {
	
	private static final String PATH = ConfigReader.getDataPath();
	private static final String SITE_PATH = ConfigReader.getAllmusicFolderPath();
	private static final String STORAGE_PATH = PATH + SITE_PATH;
	
	private static final String PREFIX_1 = "http://www.allmusic.com/album/",
								PREFIX_2 = "http://www.allmusic.com/genre/",
								PREFIX_3 = "http://www.allmusic.com/artist/";
	private static final String FILTER_PATH = "/album/";
	
	public static boolean shouldVisit(String href) {
		return href.startsWith(PREFIX_1) || href.startsWith(PREFIX_2) || href.startsWith(PREFIX_3);
	}
		
	public static boolean mustProcess(Page page) {
		return page.getWebURL().getPath().startsWith(FILTER_PATH);
	}
	
	private static void process(Page page) {
		String docID = IdManager.getID();
		new HTMLFileWriter(STORAGE_PATH).writeToHTMLFile(docID, page);
		new DataExtractor(STORAGE_PATH).extractDataFromPage(docID, page);
	}
	
	public static void checkAndProcess(Page page) {
		if (mustProcess(page))
			process(page);
	}
}
