package espn_data_management;


import config.ConfigReader;
import edu.uci.ics.crawler4j.crawler.Page;
import file_writer.HTMLFileWriter;

public class PageAnalyzer {
	
	private static final String PATH = ConfigReader.getDataPath();
	private static final String SITE_PATH = ConfigReader.getEspnFolderPath();
	private static final String STORAGE_PATH = PATH + SITE_PATH;

	private static final String REGEX_VISIT = "^http://espn.go.com/nba/(player/|team/|players).*$";
	private static final String REGEX_FILTER = "^/nba/player/_/id/.*$";

	public static boolean shouldVisit(String href) {
		return href.matches(REGEX_VISIT);
	}

	private static boolean mustProcess(Page page) {
		return page.getWebURL().getPath().matches(REGEX_FILTER);
	}

	private static void process(Page page) {
		String docID = IdManager.getID();
		new HTMLFileWriter(STORAGE_PATH).writeToHTMLFile(docID, page);
//		new DataExtractor(STORAGE_PATH).extractDataFromPage(docID, page);
	}

	public static void checkAndProcess(Page page) {
		if (mustProcess(page))
			process(page);
	}
}
