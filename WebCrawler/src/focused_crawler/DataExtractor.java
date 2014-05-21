package focused_crawler;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.csvreader.CsvWriter;

import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.parser.HtmlParseData;

public class DataExtractor {
	
	private static String docID;
	private static Page page;
	private static CsvWriter csvWriter;

	public DataExtractor(String docID, Page page) {
		this.docID = docID;
		this.page = page;
	}
	
	public void extractData() {
		String outputFile = "C:\\page_list\\data.csv";
		try {
			boolean alreadyExists = new File(outputFile).exists();
			
			csvWriter = new CsvWriter(new FileWriter(outputFile, true), ';');
						
			if (!alreadyExists) {
				csvWriter.write("ID");
				csvWriter.write("TITLE");
				csvWriter.write("IMAGE");
				csvWriter.write("INFO");
				csvWriter.endRecord();
				csvWriter.flush();
			}
			
			if (page.getParseData() instanceof HtmlParseData) {
				csvWriter.write(docID);
				HtmlParseData htmlParseData = (HtmlParseData) page.getParseData();
				Document doc = Jsoup.parse(htmlParseData.getHtml());
				String title = doc.select("#house-top > div.grid.g4 > div.shadow.grid.g4.gallery-area > h1").text();
				csvWriter.write(title);
				String image = doc.select("#largeHouseImage").attr("src").toString();
				csvWriter.write(image);
				String info = ul2String(doc.select("#overview-tab-content > div:nth-child(3) > ul:nth-child(2)"));
				System.err.println(info);
				csvWriter.write(info);
				csvWriter.endRecord();
			}
			
			csvWriter.close();
			
		} catch (IOException e) { e.printStackTrace(); } 
		
	}
	
	private String ul2String(Elements e) {
		StringBuilder builder = new StringBuilder();
		Element ul = e.first();
		builder.append("[");
		for (Element li : ul.children()) {
			builder.append(li.text())
				   .append(",");
		}
		builder.replace(builder.length()-1, builder.length(), "]");
		return builder.toString();
	}
	
}
