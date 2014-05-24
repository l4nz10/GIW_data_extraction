package novasol_data_management;

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

import config.ConfigReader;
import data_management.CSVFormatter;
import edu.uci.ics.crawler4j.crawler.Page;

public class DataExtractor {

	private final String PATH = ConfigReader.getDataPath(); 
	private final String SITE_PATH = ConfigReader.getNovasolFolderPath();
	private final String FILE_NAME = ConfigReader.getCSVFileName();

	private String docID;
	private Page page;
	private CsvWriter csvWriter;
	private XPath xpath;

	public DataExtractor(String docID, Page page) {
		this.docID = docID;
		this.page = page;
		this.xpath = XPathFactory.newInstance().newXPath();		
	}

	public void extractData() {

		String outputFile = PATH + SITE_PATH + FILE_NAME;

		try {
			boolean alreadyExists = new File(outputFile).exists();

			csvWriter = new CsvWriter(new FileWriter(outputFile, true), ';');

			if (!alreadyExists) {
				csvWriter.write("ID");
				csvWriter.write("TITLE");
				csvWriter.write("DESCRIPTION");
				csvWriter.write("IMAGE");
				csvWriter.write("INFO");
				csvWriter.endRecord();
				csvWriter.flush();
			}

			HtmlCleaner cleaner = new HtmlCleaner();
			TagNode root = cleaner.clean(new ByteArrayInputStream(page.getContentData()));
			Document doc = new DomSerializer(new CleanerProperties()).createDOM(root);
			
			csvWriter.write(docID);
			csvWriter.write(CSVFormatter.format(this.extractTitle(doc)));
			csvWriter.write(CSVFormatter.format(this.extractDescription(doc)));
			csvWriter.write(CSVFormatter.format(this.extractImage(doc)));
			csvWriter.write(CSVFormatter.format(this.extractInfoList(doc)));
			csvWriter.endRecord();
			csvWriter.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}

	}

	private String extractDescription(Document doc) {
		NodeList nodes = this.compileXPathAndReturn(doc, "//meta[@name=\"description\"]/@content");
		String description = nodes.item(0).getTextContent().trim();
		return description;
	}

	private String extractInfoList(Document doc) {
		NodeList nodes = this.compileXPathAndReturn(doc, "//div[div[text()=\"Informazioni sull'alloggio\"]]//li");
		if (nodes != null) {
			StringBuilder builder = new StringBuilder();
			builder.append("[");
			for (int i = 0; i < nodes.getLength(); i++) {
				String listItemText = nodes.item(i).getTextContent().trim();
				builder.append(listItemText)
					   .append(",");
			}
			builder.deleteCharAt(builder.length()-1).append("]");
			return builder.toString();
		}
		return null;
	}
	private String extractImage(Document doc) {
		NodeList nodes = this.compileXPathAndReturn(doc, "//meta[@property=\"og:image\"]/@content");
		String imgURL = nodes.item(0).getTextContent().trim();
		return imgURL;
	}

	private String extractTitle(Document doc) {
		NodeList nodes = this.compileXPathAndReturn(doc, "//meta[@name=\"title\"]/@content");
		String title = nodes.item(0).getTextContent().trim();
		return title;
	}
//
//	private String extractImage(Document doc) {
//		NodeList nodes = this.compileXPathAndReturn(doc, "//*[@id=\"largeHouseImage\"]");
//		NamedNodeMap attributes = nodes.item(0).getAttributes();
//		String imgUrl = attributes.getNamedItem("src").getTextContent();
//		String subDomain = page.getWebURL().getSubDomain();
//		String domain = page.getWebURL().getDomain();
//		return subDomain + "." + domain + imgUrl;
//	}
//
//	private String extractTitle(Document doc) {
//		NodeList nodes = this.compileXPathAndReturn(doc, "//h1[@class=\"l-header\"]");
//		String title = nodes.item(0).getTextContent().trim();
//		return title;
//	}
	
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
