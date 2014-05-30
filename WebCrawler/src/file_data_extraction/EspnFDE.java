package file_data_extraction;

import java.io.File;

import espn_data_management.DataExtractor;

public class EspnFDE {
	
	public static void readDir(File file) {
		if (file.canRead()) {
			if (file.isDirectory()) {
				DataExtractor extractor = new DataExtractor("C:\\data\\espn\\");
				for (String fileName : file.list())
					if (fileName.endsWith(".html")) {
						System.out.println("Processing " + fileName);
						extractor.extractDataFromFile(new File(file, fileName));
					}
			}
		}
	}
	
	public static void main(String[] args) {
		readDir(new File("D:\\GIW_Data_Extraction\\espn2"));
	}
	
}
