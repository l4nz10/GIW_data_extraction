package espn_data_management;

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
				csvWriter.write("NAME");
				csvWriter.write("IMAGE");
				csvWriter.write("NUMBER");
				csvWriter.write("MEASURES");
				csvWriter.write("TEAM");
				csvWriter.write("INFO");
				csvWriter.endRecord();
				csvWriter.flush();
			}

			csvWriter.write(docID);
			csvWriter.write(this.extractName(doc));
			csvWriter.write(this.extractImage(doc));
			csvWriter.write(this.extractGeneralInfo(doc, 0));
			csvWriter.write(this.extractGeneralInfo(doc, 1));
			csvWriter.write(this.extractGeneralInfo(doc, 2));
			csvWriter.write(this.extractInfoList(doc));
			csvWriter.endRecord();
			csvWriter.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	
	private String extractGeneralInfo(Document doc, int index) {
		NodeList nodes = this.compileXPathAndReturn(doc, "//ul[@class=\"general-info\"]/li");
		if (nodes.item(index) != null) 
			return nodes.item(index).getTextContent().trim();
		return null;
	}

	private String extractInfoList(Document doc) {
		NodeList nodes = this.compileXPathAndReturn(doc, "//ul[starts-with(@class, \"player-metadata\")]/li");
		if (nodes.getLength() != 0) {
			String[] stringArray = new String[nodes.getLength()];
			for (int i = 0; i < nodes.getLength(); i++) {
				stringArray[i] = nodes.item(i).getTextContent().trim();				
			}
			return CsvFormatter.formatStringList(stringArray);
		}
		return null;
	}

	private String extractImage(Document doc) {
		NodeList nodes = this.compileXPathAndReturn(doc, "//div[starts-with(@class, \"main-headshot\")]/img/@src");
		if (nodes.getLength() != 0) {
			String imgURL = nodes.item(0).getTextContent().trim();
			return imgURL;
		}
		return null;
	}

	private String extractName(Document doc) {
		NodeList nodes = this.compileXPathAndReturn(doc, "//meta[@property=\"og:title\"]/@content");
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
