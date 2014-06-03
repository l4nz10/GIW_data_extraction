package data_extraction;

import java.io.ByteArrayInputStream;
import java.io.File;
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

import edu.uci.ics.crawler4j.crawler.Page;

public abstract class GenericDataExtractor {
	
	protected final String CSV_FILE = "data.csv";

	protected XPath xpath;
	protected String storagePath;
	protected String outputFile;
	
	public GenericDataExtractor(String storagePath) {
		this.storagePath = storagePath;
		this.xpath = XPathFactory.newInstance().newXPath();
		this.outputFile = storagePath + CSV_FILE;
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
	
	protected NodeList compileXPathAndReturn(Document doc, String query) {
		try {
			XPathExpression expr = xpath.compile(query);
			return (NodeList) expr.evaluate(doc, XPathConstants.NODESET);
		} catch (XPathExpressionException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public abstract void extractData(String docID, Document doc);
	
}
