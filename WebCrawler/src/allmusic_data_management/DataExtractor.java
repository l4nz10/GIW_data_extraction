package allmusic_data_management;

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
}
