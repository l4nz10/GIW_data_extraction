package focused_crawler;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import edu.uci.ics.crawler4j.crawler.Page;

public class HTMLFileWriter {
	
	private static Map<String, String> id2url = new HashMap<String, String>(); 
	
	public static void writeToHTMLFile(String docID, Page page) throws IOException {
		String domain = page.getWebURL().getDomain();
		String path = page.getWebURL().getPath();
		String subDomain = page.getWebURL().getSubDomain();
		String url = subDomain + "." + domain + path;
				
		InputStream ins = new ByteArrayInputStream(page.getContentData());
		BufferedInputStream bis = new BufferedInputStream(ins);
		FileOutputStream fostreame = new FileOutputStream("C:\\page_list\\"+ docID + ".html");
		int c;
		while ((c = bis.read()) != -1) {
			fostreame.write(c);
		}
		fostreame.close();
		bis.close();
		ins.close();
		id2url.put(docID, url);
	}
	
	public static void writeMap() {
		try {
			PrintWriter writer = new PrintWriter("C:\\page_list\\id2url.txt", "UTF-8");
			for (String i : id2url.keySet()) {
				writer.write(i + " --> " + id2url.get(i)+"\n");
			}
			writer.close();
			System.out.println("file written.");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}		
	}
	
//	private static String idFormatter (int  docId, int numOfDigits) {
//		String stringifiedId = ""+docId;
//		String finalString = "";
//		for (int i = 0; i < numOfDigits -stringifiedId.length(); i++) {
//			finalString += "0";
//		}
//		return (finalString + docId);
//	}
}
