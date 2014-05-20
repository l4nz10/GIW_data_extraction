package focused_crawler;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import com.csvreader.CsvWriter;

import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.parser.HtmlParseData;

public class DataExtractor {
	
	private static String docID;
	private static Page page;
	private static CsvWriter csvWriter;

//	public DataExtractor(String docID, Page page) {
//		this.docID = docID;
//		this.page = page;
//	}
	
	public static synchronized void extractData(String docID, Page page) {
		String outputFile = "C:\\page_list\\data.csv";
		try {
			csvWriter = new CsvWriter(new FileWriter(outputFile, true), ';');
			boolean alreadyExists = new File(outputFile).exists();
			if (!alreadyExists) {
				csvWriter.write("ID");
				csvWriter.write("TITLE");
				csvWriter.write("IMAGE");
				csvWriter.write("INFO");
				csvWriter.endRecord();
			}
			if (page.getParseData() instanceof HtmlParseData) {
				csvWriter.write(docID);
				HtmlParseData htmlParseData = (HtmlParseData) page.getParseData();
				Document doc = Jsoup.parse(htmlParseData.getHtml());
				String title = doc.select("#house-top > div.grid.g4 > div.shadow.grid.g4.gallery-area > h1").text();
				System.out.println("TITLE WITH CSS SELECTOR: "+title);
				csvWriter.write(title);
				csvWriter.endRecord();
			}
			
		} catch (IOException e) { e.printStackTrace(); }
		
	}
	
}
