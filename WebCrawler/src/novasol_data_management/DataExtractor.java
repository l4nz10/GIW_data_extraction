package novasol_data_management;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import com.csvreader.CsvWriter;

import csv_formatter.CsvFormatter;
import data_extraction.GenericDataExtractor;

public class DataExtractor extends GenericDataExtractor {

	public DataExtractor(String storagePath) {
		super(storagePath);
	}
	
	public void extractData(String docID, Document doc) {
		
		try {
			boolean alreadyExists = new File(outputFile).exists();

			CsvWriter csvWriter = new CsvWriter(new FileWriter(outputFile, true), ';');

			if (!alreadyExists) {
				csvWriter.write("ID");
				csvWriter.write("TITLE");
				csvWriter.write("DESCRIPTION");
				csvWriter.write("IMAGE");
				csvWriter.write("INFO");
				csvWriter.endRecord();
				csvWriter.flush();
			}

			csvWriter.write(docID);
			csvWriter.write(this.extractTitle(doc));
			csvWriter.write(this.extractDescription(doc));
			csvWriter.write(this.extractImage(doc));
			csvWriter.write(this.extractInfoList(doc));
			csvWriter.endRecord();
			csvWriter.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private String extractDescription(Document doc) {
		NodeList nodes = this.compileXPathAndReturn(doc, "//meta[@name=\"description\"]/@content");
		String description = nodes.item(0).getTextContent().trim();
		return CsvFormatter.formatString(description);
	}

	private String extractInfoList(Document doc) {
		NodeList nodes = this.compileXPathAndReturn(doc, "//div[div[text()=\"Informazioni sull'alloggio\"]]//li");
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
		NodeList nodes = this.compileXPathAndReturn(doc, "//meta[@property=\"og:image\"]/@content");
		String imgURL = nodes.item(0).getTextContent().trim();
		return imgURL;
	}

	private String extractTitle(Document doc) {
		NodeList nodes = this.compileXPathAndReturn(doc, "//meta[@name=\"title\"]/@content");
		String title = nodes.item(0).getTextContent().trim();
		return CsvFormatter.formatString(title);
	}
}
