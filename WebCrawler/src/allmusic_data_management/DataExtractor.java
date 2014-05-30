package allmusic_data_management;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.htmlcleaner.CleanerProperties;
import org.htmlcleaner.DomSerializer;
import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.TagNode;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import com.csvreader.CsvWriter;

import csv_formatter.CsvFormatter;
import edu.uci.ics.crawler4j.crawler.Page;

public class DataExtractor {

	private final String CSV_FILE = "data.csv";
	private CsvWriter csvWriter;
	private XPath xpath;
	private String storagePath;
	

	public DataExtractor(String storagePath) {
		this.storagePath = storagePath;
		this.xpath = XPathFactory.newInstance().newXPath();		
	}
	
	public void extractDataFromFile(File file) {
		String fileName = file.getName();
		String docID = fileName.substring(0, fileName.indexOf('.'));
		try {
			
			HtmlCleaner cleaner = new HtmlCleaner();
			TagNode root = cleaner.clean(file);
			Document doc = new DomSerializer(new CleanerProperties()).createDOM(root);
			this.extractData(docID, doc);
			
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}		
	}
	
	public void extractDataFromPage(String docID, Page page) {
		try {
			
			HtmlCleaner cleaner = new HtmlCleaner();
			TagNode root = cleaner.clean(new ByteArrayInputStream(page.getContentData()));
			Document doc = new DomSerializer(new CleanerProperties()).createDOM(root);
			this.extractData(docID, doc);
			
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}
	}
	
	public void extractData(String docID, Document doc) {

		String outputFile = this.storagePath + CSV_FILE;

		try {
			boolean alreadyExists = new File(outputFile).exists();

			csvWriter = new CsvWriter(new FileWriter(outputFile, true), ';');

			if (!alreadyExists) {
				csvWriter.write("ID");
				csvWriter.write("TITLE");
				csvWriter.write("ARTIST");
				csvWriter.write("IMAGE");
				csvWriter.write("TRACKS");
				csvWriter.endRecord();
				csvWriter.flush();
			}

			csvWriter.write(docID);
			csvWriter.write(this.extractTitle(doc));
			csvWriter.write(this.extractArtist(doc));
			csvWriter.write(this.extractImage(doc));
			csvWriter.write(this.extractTracks(doc));
			csvWriter.endRecord();
			csvWriter.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private String extractArtist(Document doc) {
		NodeList nodes = this.compileXPathAndReturn(doc, "//h3[@class=\"album-artist\" or @class=\"release-artist\"]");
		String description = nodes.item(0).getTextContent().trim();
		return CsvFormatter.formatString(description);
	}

	private String extractTracks(Document doc) {
		NodeList nodes = this.compileXPathAndReturn(doc, "//section[@class=\"track-listing\"]//td[@class=\"title-composer\"]/div[@class=\"title\"]");		
		if (nodes != null) {
			String[] stringArray = new String[nodes.getLength()];
			for (int i = 0; i < nodes.getLength(); i++) {
				stringArray[i] = nodes.item(i).getTextContent().trim();				
			}
			return CsvFormatter.formatStringList(stringArray);
		}
		return null;
	}

	private String extractImage(Document doc) {
		NodeList nodes = this.compileXPathAndReturn(doc, "//div[@class=\"album-cover\" or @class=\"release-cover\"]//img/@src");
		String imgURL = nodes.item(0).getTextContent().trim();
		return imgURL;
	}

	private String extractTitle(Document doc) {
		NodeList nodes = this.compileXPathAndReturn(doc, "//h2[@class=\"album-title\" or @class=\"release-title\"]");
		String title = nodes.item(0).getTextContent().trim();
		return CsvFormatter.formatString(title);
	}
	
	private NodeList compileXPathAndReturn(Document doc, String query) {
		try {
			XPathExpression expr = xpath.compile(query);
			return (NodeList) expr.evaluate(doc, XPathConstants.NODESET);
		} catch (XPathExpressionException e) {
			e.printStackTrace();
		}
		return null;
	}
}
