package fileWriter;

import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;

import config.ConfigReader;
import edu.uci.ics.crawler4j.crawler.Page;

public class HTMLFileWriter {
	
	private final String INDEX_NAME = ConfigReader.getIndexFileName();
	
	private String storagePath;
	
	public HTMLFileWriter(String storagePath) {
		this.storagePath = storagePath;
	}
	
	public void writeToHTMLFile(String docID, Page page) {
		
		String domain = page.getWebURL().getDomain();
		String path = page.getWebURL().getPath();
		String subDomain = page.getWebURL().getSubDomain();
		String url = "";
		if (subDomain != null && !subDomain.equals(""))
			url = subDomain + ".";
		url += (domain + path);
		
		try {
			File file = new File(storagePath + docID + ".html");
			InputStream inStr = new ByteArrayInputStream(page.getContentData());
			BufferedInputStream buffInStr = new BufferedInputStream(inStr);
			FileOutputStream fileOutStr = new FileOutputStream(file);
			int c;
			while ((c = buffInStr.read()) != -1) {
				fileOutStr.write(c);
			}
			fileOutStr.close();
			buffInStr.close();
			inStr.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		this.writeOnIndex(docID, url);
	}
	
	private void writeOnIndex(String docID, String url) {
		try {			
			File file = new File(storagePath + INDEX_NAME);
			
			if (!file.exists()) {
				file.createNewFile();
			}						
		
			FileWriter fw = new FileWriter(file.getAbsoluteFile(), true);
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(docID + " --> " + url);
			bw.newLine();
			bw.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		}		
	}
}
