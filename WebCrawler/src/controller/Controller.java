package controller;

import config.ConfigReader;
import crawler.NovasolCrawler;
import crawler.MultiplayerCrawler;
import crawler.AllmusicCrawler;
import crawler.EspnCrawler;
import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtConfig;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtServer;

public class Controller {

	public static void main(String[] args) throws Exception {

		/*
		 * numberOfCrawlers shows the number of concurrent threads that should
		 * be initiated for crawling.
		 */
		int numberOfCrawlers = 2;

		// configuring crawler for novasol.it
		CrawlConfig config = new CrawlConfig();

		config.setPolitenessDelay(500);
		config.setMaxDepthOfCrawling(-1);
		config.setMaxPagesToFetch(-1);
		config.setResumableCrawling(true);
		
		String crawlRootFolder = ConfigReader.getCrawlFolderPath();

		config.setCrawlStorageFolder(crawlRootFolder + ConfigReader.getNovasolFolderPath());

		PageFetcher pageFetcher = new PageFetcher(config);
		RobotstxtConfig robotstxtConfig = new RobotstxtConfig();
		RobotstxtServer robotstxtServer = new RobotstxtServer(robotstxtConfig, pageFetcher);
		CrawlController novaController = new CrawlController(config, pageFetcher, robotstxtServer);

		novaController.addSeed("http://www.novasol.it/r/380?wt.seg_4=NS_IT_CON2_MAP_380&SD=24-05-2014&ED=31-05-2014");

		// configuring crawler for multiplayer.it
		config.setCrawlStorageFolder(crawlRootFolder + ConfigReader.getMultiplayerFolderPath());

		pageFetcher = new PageFetcher(config);
		robotstxtConfig = new RobotstxtConfig();
		robotstxtServer = new RobotstxtServer(robotstxtConfig, pageFetcher);
		CrawlController multipController = new CrawlController(config, pageFetcher, robotstxtServer);

		multipController.addSeed("http://multiplayer.it/articoli/notizie/");

		// configuring crawler for allmusic.com
		config.setCrawlStorageFolder(crawlRootFolder + ConfigReader.getAllmusicFolderPath());

		pageFetcher = new PageFetcher(config);
		robotstxtConfig = new RobotstxtConfig();
		robotstxtConfig.setEnabled(false);
		robotstxtServer = new RobotstxtServer(robotstxtConfig, pageFetcher);
		CrawlController allmusicController = new CrawlController(config, pageFetcher, robotstxtServer);

		allmusicController.addSeed("http://www.allmusic.com/genres");

		// configuring crawler for allmusic.com
		config.setCrawlStorageFolder(crawlRootFolder + ConfigReader.getEspnFolderPath());

		pageFetcher = new PageFetcher(config);
		robotstxtConfig = new RobotstxtConfig();
		robotstxtServer = new RobotstxtServer(robotstxtConfig, pageFetcher);
		CrawlController espnController = new CrawlController(config, pageFetcher, robotstxtServer);

		espnController.addSeed("http://espn.go.com/nba/players");

		/*
		 * Start the crawl. This is a blocking operation, meaning that your code
		 * will reach the line after this only when crawling is finished.
		 */
//		novaController.startNonBlocking(NovasolCrawler.class, numberOfCrawlers);
//		multipController.startNonBlocking(MultiplayerCrawler.class, numberOfCrawlers);
//		allmusicController.startNonBlocking(AllmusicCrawler.class, numberOfCrawlers);
		espnController.startNonBlocking(EspnCrawler.class, numberOfCrawlers);
		
		System.out.println("Crawling has begun. Press any key to stop the process.");
		
		System.in.read();
		
//		novaController.shutdown();
//		multipController.shutdown();
//		allmusicController.shutdown();
		espnController.shutdown();
		
		System.out.println("Shutting down. Waiting crawlers to finish...");
		
//		novaController.waitUntilFinish();
//		multipController.waitUntilFinish();
//		allmusicController.waitUntilFinish();
		espnController.waitUntilFinish();
		
		System.out.println("Shutdown complete.");
	}
}
