package file_data_extraction;

import java.io.File;

import multiplayer_data_management.DataExtractor;

public class MultiplayerFDE {
	
	public static void readDir(File file) {
		if (file.canRead()) {
			if (file.isDirectory()) {
				DataExtractor extractor = new DataExtractor("C:\\data\\multiplayer\\");
				for (String fileName : file.list())
					if (fileName.endsWith(".html")) {
						System.out.println("Processing " + fileName);
						extractor.extractDataFromFile(new File(file, fileName));
					}
				System.out.println("Extraction of directory " + file.getName() + " complete.");
			} else {
				System.err.println("ERROR: File " + file.getName() + " is not a directory.");
			} 
		} else {
			System.err.println("ERROR: File " + file.getName() + " cannot be read.");
		}
	}
	
	public static void main(String[] args) {
		readDir(new File("D:\\GIW_Data_Extraction\\multiplayer"));
	}
	
}
