package com.swingtech.apps.filemgmt.util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DupFileUtility {
    private final static long KILO_BYTES = 1000;
    private final static long MEGA_BYTES = KILO_BYTES * KILO_BYTES;
    private final static long GIGA_BYTES = MEGA_BYTES * KILO_BYTES;
    private final static long TERRA_BYTES = GIGA_BYTES * KILO_BYTES;
    private final static String WILDCARD_REGEX_PATTERN = "[\\S\\s]*";

    public static String getFileMimeTypeFromFileExtension(String fileExtension) {
        Map<String, String> fileExtensionToMapTypeMap = new HashMap<String, String>();

        fileExtensionToMapTypeMap.put("mp4", "video/mp4");
        fileExtensionToMapTypeMap.put("wmv", "video/x-ms-wmv");
        fileExtensionToMapTypeMap.put("avi", "video/x-msvideo");
        fileExtensionToMapTypeMap.put("flv", "video/x-flv");
        fileExtensionToMapTypeMap.put("jpg", "image/jpeg");
        fileExtensionToMapTypeMap.put("png", "image/png");
        fileExtensionToMapTypeMap.put("gif", "image/gif");

        return fileExtensionToMapTypeMap.get(fileExtension);
    }

    public static String getBinarySizeDisplayString(long bytes) {
        if (bytes <= KILO_BYTES) {
            return bytes + " bytes";
        }

        if (bytes > KILO_BYTES && bytes <= MEGA_BYTES) {
            return bytes / KILO_BYTES + " KB";
        }

        if (bytes > MEGA_BYTES && bytes <= GIGA_BYTES) {
            return bytes / MEGA_BYTES + " MB";
        }

        if (bytes > GIGA_BYTES && bytes <= TERRA_BYTES) {
            return bytes / GIGA_BYTES + " GB";
        }

        return bytes / TERRA_BYTES + " TB";
    }

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

    public static String getFileName(File file) {
        return file.getName();
    }

    public static boolean fileMatchesPattern(File file, String searchTermPattern, boolean caseSensitive) {
        boolean matches = false;
        String modifiedSearchTermPattern = null;
        String fileName = null;
        String fileNameWithoutExtension = null;

        fileName = DupFileUtility.getFileName(file);
        fileNameWithoutExtension = DupFileUtility.getFileNameWithoutExtension(file);

        modifiedSearchTermPattern = searchTermPattern.trim();

        if (modifiedSearchTermPattern.startsWith("\"") && modifiedSearchTermPattern.endsWith("\"")) {

            modifiedSearchTermPattern = modifiedSearchTermPattern.replace("\"", "");

            matches = fileNameWithoutExtension.equalsIgnoreCase(modifiedSearchTermPattern);

            return matches;
        }

        if (modifiedSearchTermPattern.startsWith("'") && modifiedSearchTermPattern.endsWith("'")) {
            modifiedSearchTermPattern = modifiedSearchTermPattern.replace("\"", "");
        }

        if (modifiedSearchTermPattern.contains("~")) {
            modifiedSearchTermPattern = modifiedSearchTermPattern.replace("~", WILDCARD_REGEX_PATTERN);
        }
        else {
            modifiedSearchTermPattern = WILDCARD_REGEX_PATTERN + modifiedSearchTermPattern + WILDCARD_REGEX_PATTERN;
        }

        matches = matchesPattern(fileName, modifiedSearchTermPattern, caseSensitive);

        return matches;
    }

    public static boolean matchesPattern(String targetString, String matchPattern, boolean caseSensitive) {
        Pattern pattern = null;
        Matcher matcher = null;

        if (caseSensitive) {
            pattern = Pattern.compile(matchPattern);
        }
        else {
            pattern = Pattern.compile(matchPattern, Pattern.CASE_INSENSITIVE);
        }
        matcher = pattern.matcher(targetString);

        if (matcher.matches()) {
            return true;
        }

        if (targetString.matches(matchPattern)) {
            return true;
        }

        return false;
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
        }
        finally {
            fw.close();
        }
    }
}
