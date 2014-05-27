package crawler;

import config.ConfigReader;
import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtConfig;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtServer;

public class Controller {
	
	public static void main(String[] args) throws Exception {
		
	
		String allMusicCrawlStorageFolder = ConfigReader.getAllmusicStorageFolderPath();	//"C:\\crawl_tmp";
		String novasolCrawlStorageFolder = ConfigReader.getNovasolStorageFolderPath();
		String multiplayerCrawlStorageFolder = ConfigReader.getMultiplayerStorageFolderPath();

		/*
		 * numberOfCrawlers shows the number of concurrent threads that should
		 * be initiated for crawling.
		 */
		int numberOfCrawlers = Runtime.getRuntime().availableProcessors();

		CrawlConfig configAllmusic = new CrawlConfig();
		CrawlConfig configNovasol = new CrawlConfig();
		CrawlConfig configMultiplayer = new CrawlConfig();

		configAllmusic.setCrawlStorageFolder(allMusicCrawlStorageFolder);
		configNovasol.setCrawlStorageFolder(novasolCrawlStorageFolder);
		configMultiplayer.setCrawlStorageFolder(multiplayerCrawlStorageFolder);
		/*
		 * Be polite: Make sure that we don't send more than 1 request per
		 * second (1000 milliseconds between requests).
		 */
		configAllmusic.setPolitenessDelay(ConfigReader.getTimeout());
		configNovasol.setPolitenessDelay(ConfigReader.getTimeout());
		configMultiplayer.setPolitenessDelay(ConfigReader.getTimeout());

		/*
		 * You can set the maximum crawl depth here. The default value is -1 for
		 * unlimited depth
		 */
		configAllmusic.setMaxDepthOfCrawling(-1);
		configNovasol.setMaxDepthOfCrawling(-1);
		configMultiplayer.setMaxDepthOfCrawling(-1);

		/*
		 * You can set the maximum number of pages to crawl. The default value
		 * is -1 for unlimited number of pages
		 */
		configAllmusic.setMaxPagesToFetch(ConfigReader.getMaxPage());
		configNovasol.setMaxPagesToFetch(ConfigReader.getMaxPage());
		configMultiplayer.setMaxPagesToFetch(ConfigReader.getMaxPage());

		/*
		 * This config parameter can be used to set your crawl to be resumable
		 * (meaning that you can resume the crawl from a previously
		 * interrupted/crashed crawl). Note: if you enable resuming feature and
		 * want to start a fresh crawl, you need to delete the contents of
		 * rootFolder manually.
		 */
		configAllmusic.setResumableCrawling(false);
		configNovasol.setResumableCrawling(false);
		configMultiplayer.setResumableCrawling(false);

		/*
		 * Instantiate the controller for this crawl.
		 */
		PageFetcher pageFetcherAllmusic = new PageFetcher(configAllmusic);
		RobotstxtConfig robotstxtConfig = new RobotstxtConfig();
		robotstxtConfig.setEnabled(false);
		RobotstxtServer robotstxtServerAllmusic = new RobotstxtServer(robotstxtConfig, pageFetcherAllmusic);
		CrawlController controllerAllmusic = new CrawlController(configAllmusic, pageFetcherAllmusic, robotstxtServerAllmusic);
		
		PageFetcher pageFetcherNovasol = new PageFetcher(configNovasol);
		RobotstxtServer robotstxtServerNovasol = new RobotstxtServer(robotstxtConfig, pageFetcherNovasol);
		CrawlController controllerNovasol = new CrawlController(configNovasol, pageFetcherNovasol, robotstxtServerNovasol);
		
		PageFetcher pageFetcherMultiplayer = new PageFetcher(configMultiplayer);
		RobotstxtServer robotstxtServerMultiplayer = new RobotstxtServer(robotstxtConfig, pageFetcherMultiplayer);
		CrawlController controllerMultiplayer = new CrawlController(configMultiplayer, pageFetcherMultiplayer, robotstxtServerMultiplayer);

		/*
		 * For each crawl, you need to add some seed urls. These are the first
		 * URLs that are fetched and then the crawler starts following links
		 * which are found in these pages
		 */
		
		controllerAllmusic.addSeed("http://www.allmusic.com/genres");
		controllerMultiplayer.addSeed("http://multiplayer.it/articoli/notizie/");
		controllerNovasol.addSeed("http://www.novasol.it/r/380?wt.seg_4=NS_IT_CON2_MAP_380&SD=24-05-2014&ED=31-05-2014");
		

		/*
		 * Start the crawl. This is a blocking operation, meaning that your code
		 * will reach the line after this only when crawling is finished.
		 */
		controllerAllmusic.start(FocusedCrawler.class, numberOfCrawlers);
		System.out.println("Allmusic done.");
		controllerNovasol.start(FocusedCrawler.class, numberOfCrawlers);
		System.out.println("Novasol done.");
		controllerMultiplayer.start(FocusedCrawler.class, numberOfCrawlers);
		System.out.println("Multiplayer done.");
		System.out.println("Done.");
	}

}
