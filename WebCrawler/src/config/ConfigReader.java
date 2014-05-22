package config;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class ConfigReader {

	private static final String FILE_NAME = "Z:\\config.txt";
	private static BufferedReader reader = null;
	private static String storageFolderPath = null;
	private static String dataPath = null;
	private static String indexFileName = null;
	private static String csvFileName = null;
	private static int timeout = 0;

	// public ConfigReader() {
	// try {
	// reader = new BufferedReader(new FileReader(FILE_NAME));
	// } catch (FileNotFoundException e) {
	// e.printStackTrace();
	// }
	// }

	private static String findAndReturn(String caption) {
		String line = null;
		if (reader == null)
			try {
				reader = new BufferedReader(new FileReader(FILE_NAME));
			} catch (FileNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		try {
			do {
				line = reader.readLine();
//				System.out.println(line);
				if (line != null && line.equals(caption)){
					String res = reader.readLine();
					reader = null;
					return res;
				}
			} while (line != null);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String getStorageFolderPath() {
		if (storageFolderPath == null) {
			storageFolderPath = findAndReturn("CRAWL STORAGE FOLDER PATH:");
		}
		return storageFolderPath;
	}

	public static String getDataPath() {
		if (dataPath == null) {
			dataPath = findAndReturn("DATA FOLDER PATH:");
		}
		return dataPath;
	}

	public static String getIndexFileName() {
		if (indexFileName == null) {
			indexFileName = findAndReturn("INDEX FILE NAME:");
		}
		return indexFileName;
	}

	public static String getCSVFileName() {
		if (csvFileName == null) {
			csvFileName = findAndReturn("CSV FILE NAME:");
		}
		return csvFileName;
	}

	public static int getTimeout() {
		if (timeout == 0) {
			timeout = Integer.parseInt(findAndReturn("TIMEOUT:"));
		}
		return timeout;
	}

}
