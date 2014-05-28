package config;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class ConfigReader {

	private static final String FILE_NAME = "C:\\config.txt";
	
	private static BufferedReader reader = null;
	private static String allmusicStorageFolderPath = null;
	private static String dataPath = null;
	private static String indexFileName = null;
	private static String csvFileName = null;
	private static String novasolFolderPath = null;
	private static int timeout = 0, maxPage = 0;
	private static String allmusicFolderPath = null;
	private static String espnFolderPath = null;
	private static String novasolStorageFolderPath;
	private static String multiplayerStorageFolderPath;
	private static String multiplayerFolderPath;
	private static String espnStorageFolderPath;

	private static String findAndReturn(String caption) {
		String line = null;
		if (reader == null)
			try {
				reader = new BufferedReader(new FileReader(FILE_NAME));
			} catch (FileNotFoundException e1) {
				e1.printStackTrace();
			}
		try {
			do {
				line = reader.readLine();
				if (line != null && line.equals(caption)) {
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

	public static String getAllmusicStorageFolderPath() {
		if (allmusicStorageFolderPath == null) {
			allmusicStorageFolderPath = findAndReturn("ALLMUSIC CRAWL STORAGE FOLDER PATH:");
		}
		return allmusicStorageFolderPath;
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

	public static String getNovasolFolderPath() {
		if (novasolFolderPath == null) {
			novasolFolderPath = findAndReturn("NOVASOL FOLDER PATH:");
		}
		return novasolFolderPath;
	}
	
	public static String getMultiplayerFolderPath() {
		if (multiplayerFolderPath == null) {
			multiplayerFolderPath = findAndReturn("MULTIPLAYER FOLDER PATH:");
		}
		return multiplayerFolderPath;
	}

	public static String getAllmusicFolderPath() {
		if (allmusicFolderPath == null) {
			allmusicFolderPath  = findAndReturn("ALLMUSIC FOLDER PATH:");
		}
		return allmusicFolderPath;
	}
	public static int getMaxPage() {
		if (maxPage == 0) {
			maxPage  = Integer.parseInt(findAndReturn("MAX PAGE:"));
		}
		return maxPage;
	}

	public static String getNovasolStorageFolderPath() {
		if (novasolStorageFolderPath == null) {
			novasolStorageFolderPath = findAndReturn("NOVASOL CRAWL STORAGE FOLDER PATH:");
		}
		return novasolStorageFolderPath;
	}

	public static String getMultiplayerStorageFolderPath() {
		if (multiplayerStorageFolderPath == null) {
			multiplayerStorageFolderPath = findAndReturn("MULTIPLAYER CRAWL STORAGE FOLDER PATH:");
		}
		return multiplayerStorageFolderPath;
	}

	public static String getEspnFolderPath() {
		if (espnFolderPath == null) {
			espnFolderPath  = findAndReturn("ESPN FOLDER PATH:");
		}
		return espnFolderPath;
	}

	public static String getEspnStorageFolderPath() {
		if (espnStorageFolderPath == null) {
			espnStorageFolderPath = findAndReturn("ESPN CRAWL STORAGE FOLDER PATH:");
		}
		return espnStorageFolderPath;
	}

}
