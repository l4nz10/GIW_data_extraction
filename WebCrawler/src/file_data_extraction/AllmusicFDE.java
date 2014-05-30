package file_data_extraction;

import java.io.File;

import allmusic_data_management.DataExtractor;

public class AllmusicFDE {
	
	public static void readDir(File file) {
		if (file.canRead()) {
			if (file.isDirectory()) {
				DataExtractor extractor = new DataExtractor("C:\\data\\allmusic\\");
				for (String fileName : file.list())
					if (fileName.endsWith(".html")) {
						System.out.println("Processing " + fileName);
						extractor.extractDataFromFile(new File(file, fileName));
					}
			}
		}
	}
	
	public static void main(String[] args) {
		readDir(new File("D:\\GIW_Data_Extraction\\allmusic"));
	}
	
}
