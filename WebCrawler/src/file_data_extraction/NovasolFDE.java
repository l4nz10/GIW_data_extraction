package file_data_extraction;

import java.io.File;

import novasol_data_management.DataExtractor;

public class NovasolFDE {
	
	public static void readDir(File file) {
		if (file.canRead()) {
			if (file.isDirectory()) {
				DataExtractor extractor = new DataExtractor("C:\\data\\novasol\\");
				for (String fileName : file.list())
					if (fileName.endsWith(".html")) {
						System.out.println("Processing " + fileName);
						extractor.extractDataFromFile(new File(file, fileName));
					}
			}
		}
	}
	
	public static void main(String[] args) {
		readDir(new File("D:\\GIW_Data_Extraction\\novasol"));
	}
	
}