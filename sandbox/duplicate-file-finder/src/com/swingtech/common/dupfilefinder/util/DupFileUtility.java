package com.swingtech.common.dupfilefinder.util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DupFileUtility {
	public static String getFileNameWithoutExtension(File file) {
		String fileName = null;
		
		if (file == null) {
			throw new IllegalArgumentException("file cannot be null");
		}
		
		int fileExtensionIndex = file.getName().lastIndexOf(".");
		
		if (fileExtensionIndex <= 0) {
			return file.getName();
		}
		
		fileName = file.getName().substring(0, fileExtensionIndex);
		
		return fileName;
	}

	public static String getFileNameExtension(File file) {
		String fileName = null;
		
		if (file == null) {
			throw new IllegalArgumentException("file cannot be null");
		}
		
		int fileExtensionIndex = file.getName().lastIndexOf(".");
		
		if (fileExtensionIndex <= 0) {
			return "";
		}
		
		if (fileExtensionIndex == file.getName().length()) {
			return "";
		}
		
		fileName = file.getName().substring(fileExtensionIndex + 1);
		
		return fileName;
	}
	
	public static String getDateString(Date date, String format) {
		DateFormat df = new SimpleDateFormat(format);
		
		return df.format(date);
	}

	public static void writeToFile(File file, StringBuffer text) throws IOException {
		writeToFile(file, text.toString());
	}
	
	public static void writeToFile(File file, String text) throws IOException {
		FileWriter fw = null;

		try {
            fw = new FileWriter(file);
            fw.write(text);
		} finally {
            fw.close();
		}
	}
}
