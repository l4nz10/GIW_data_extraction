package config;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class ConfigReader {
	
	private final String FILE_NAME = "C:\\config.txt";
	private BufferedReader reader;
	
	public ConfigReader() {
		try {
			reader = new BufferedReader(new FileReader(FILE_NAME));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} 
	}
	
	private String findAndReturn(String caption) {
		String line = null;
		try {
			do {
				line = reader.readLine();
				if (line != null && line.equals(caption));
					return reader.readLine();
			} while (line != null);
		} catch (IOException e) { e.printStackTrace(); }
		return null;
	}
	
	public String getStorageFolderPath() {
		return findAndReturn("CRAWL STORAGE FOLDER PATH:");
	}
	
	public String getDataPath() {
		return findAndReturn("DATA FOLDER PATH:");  
	}
	
	public String getIndexFileName() {
		return findAndReturn("INDEX FILE NAME:");
	}
	
	public String getCSVFileName() {
		return findAndReturn("CSV FILE NAME:");
	}
	
	
}
