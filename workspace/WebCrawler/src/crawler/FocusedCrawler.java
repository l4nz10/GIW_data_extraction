package crawler;

import java.io.IOException;
import java.util.regex.Pattern;

import org.apache.http.Header;

import data_management.DataExtractor;
import data_management.HTMLFileWriter;
import data_management.IdManager;
import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.url.WebURL;

public class FocusedCrawler extends WebCrawler {
		
	private final static Pattern FILTERS = Pattern.compile(".*(\\.(css|js|bmp|gif|jpe?g"
															+ "|png|tiff?|mid|mp2|mp3|mp4"
															+ "|wav|avi|mov|mpeg|ram|m4v|pdf"
															+ "|rm|smil|wmv|swf|wma|zip|rar|gz))$");

	@Override
	public boolean shouldVisit(WebURL url) {
		String href = url.getURL().toLowerCase();
		return !FILTERS.matcher(href).matches()	&& (href.startsWith("http://www.novasol.it/r/") || href.startsWith("http://www.novasol.it/p/"));
	}

	@Override
	public void visit(Page page) {
//		int docid = page.getWebURL().getDocid();
//		String url = page.getWebURL().getURL();
//		String domain = page.getWebURL().getDomain();
//		String path = page.getWebURL().getPath();
//		String subDomain = page.getWebURL().getSubDomain();
//		String parentUrl = page.getWebURL().getParentUrl();
//		String anchor = page.getWebURL().getAnchor();

		if (page.getWebURL().getPath().contains("/p/"))
			try {
				
				String docID = IdManager.getID();
				HTMLFileWriter.writeToHTMLFile(docID, page);
				new DataExtractor(docID, page).extractData();
				
			} catch (IOException e) {
				e.printStackTrace();
			}

		Header[] responseHeaders = page.getFetchResponseHeaders();
		if (responseHeaders != null) {
			System.out.println("Response headers:");
			for (Header header : responseHeaders) {
				System.out.println("\t" + header.getName() + ": " + header.getValue());
			}
		}
	}

}
