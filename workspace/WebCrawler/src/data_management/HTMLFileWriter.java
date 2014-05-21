package data_management;

import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;

import edu.uci.ics.crawler4j.crawler.Page;

public class HTMLFileWriter {
	
	private static final String PATH = "C:\\GIW_Data_Extraction\\page_list\\";
	private static final String INDEX_NAME = "id2url.txt";
	
	public static void writeToHTMLFile(String docID, Page page) throws IOException {
		
		String domain = page.getWebURL().getDomain();
		String path = page.getWebURL().getPath();
		String subDomain = page.getWebURL().getSubDomain();
		String url = subDomain + "." + domain + path;
				
		InputStream inStr = new ByteArrayInputStream(page.getContentData());
		BufferedInputStream buffInStr = new BufferedInputStream(inStr);
		FileOutputStream fileOutStr = new FileOutputStream(PATH + docID + ".html");
		int c;
		while ((c = buffInStr.read()) != -1) {
			fileOutStr.write(c);
		}
		fileOutStr.close();
		buffInStr.close();
		inStr.close();
		
		writeOnIndex(docID, url);
	}
	
	public static void writeOnIndex(String docID, String url) {
		try {			
			File file = new File(PATH + INDEX_NAME);
			
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
