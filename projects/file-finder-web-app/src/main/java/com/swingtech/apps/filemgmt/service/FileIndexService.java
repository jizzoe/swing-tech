package com.swingtech.apps.filemgmt.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.swingtech.apps.filemgmt.dao.FileIndexDao;
import com.swingtech.apps.filemgmt.dao.FileIndexPreferencesDao;
import com.swingtech.apps.filemgmt.dao.impl.file.FileIndexhDaoImplFileBased;
import com.swingtech.apps.filemgmt.model.FileEntity;
import com.swingtech.apps.filemgmt.model.FileIndexPreferences;
import com.swingtech.apps.filemgmt.model.FileIndexResults;
import com.swingtech.apps.filemgmt.model.FileLocationEntity;
import com.swingtech.apps.filemgmt.util.DupFileUtility;
import com.swingtech.apps.filemgmt.util.Timer;

public class FileIndexService {
    FileIndexDao fileIndexDao = new FileIndexhDaoImplFileBased();
    FileIndexPreferencesDao fileIndexPreferencesDao = new FileIndexPreferencesDao();

    public FileIndexPreferences retrieveFileIndexPreferences() {
        try {
            return fileIndexPreferencesDao.retrieveDupFilePreferences();
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void saveFileIndexPreferences(FileIndexPreferences fileIndexPreferences) {
        try {
            fileIndexPreferencesDao.saveFileIndexPreferences(fileIndexPreferences);
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * DOCME
     *
     * @param fileName
     * @return
     * @throws IOException
     * @see com.swingtech.apps.filemgmt.dao.FileIndexDao#retrieveFileEntity(java.lang.String)
     */
    public FileEntity retrieveFileEntity(String fileName) {
        try {
            return fileIndexDao.retrieveFileEntity(fileName);
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * DOCME
     *
     * @param fileEntity
     * @throws IOException
     * @see com.swingtech.apps.filemgmt.dao.FileIndexDao#saveFileEntity(com.swingtech.apps.filemgmt.model.FileEntity)
     */
    public void saveFileEntity(FileEntity fileEntity) {
        try {
            fileIndexDao.saveFileEntity(fileEntity);
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public FileIndexResults indexAllFilesWithStrings(List<String> searchDirectoryFileNames,
            List<String> excludeDirStrings) throws Exception {
        List<File> searchDirectoryFiles = new ArrayList<File>();
        File searchDirectoryFile = null;

        for (String searchDirectoryFileName : searchDirectoryFileNames) {
            searchDirectoryFile = new File(searchDirectoryFileName);
            searchDirectoryFiles.add(searchDirectoryFile);
        }

        return this.indexAllFiles(searchDirectoryFiles, excludeDirStrings);
    }

    public FileIndexResults indexAllFiles(List<File> searchDirectoryFiles, List<String> excludeDirStrings)
            throws Exception {
        FileIndexResults fileIndexResults = null;
        Timer timer = new Timer();

        timer.startTiming();

        fileIndexResults = this.performIndex(searchDirectoryFiles, excludeDirStrings);

        fileIndexDao.saveFileIndexResults(fileIndexResults);

        timer.stopTiming();

        fileIndexResults.setExcludeDirStrings(excludeDirStrings);
        fileIndexResults.setTimer(timer);

        return fileIndexResults;
    }

    private FileIndexResults performIndex(List<File> searchDirectoryFiles, List<String> matchStrings) throws Exception {
        FileIndexResults fileIndexResults = new FileIndexResults();

        if (searchDirectoryFiles == null) {
            throw new IllegalArgumentException("searchDirectoryFiles cannot be null");
        }

        for (File searchDirectoryFile : searchDirectoryFiles) {
            if (!searchDirectoryFile.exists()) {
                fileIndexResults.getDirectoriesNotSearched().add(searchDirectoryFile);
                continue;
            }
            this.walkDirectoryForSearchResults(searchDirectoryFile, fileIndexResults, matchStrings);
        }

        return fileIndexResults;
    }

    private void walkDirectoryForSearchResults(File searchDirectoryFile, FileIndexResults fileIndexResults,
            List<String> matchStrings) throws Exception {
        System.out.println(searchDirectoryFile.getAbsolutePath());
        Path resultPath = null;
        FileIndexVisitor fileSearchVisitor = new FileIndexVisitor(fileIndexResults);

        fileIndexResults.getDirectoriesSearched().add(searchDirectoryFile);

        Path path = FileSystems.getDefault().getPath(searchDirectoryFile.getAbsolutePath());

        resultPath = Files.walkFileTree(path, fileSearchVisitor);
    }

    public void printResults(FileIndexResults fileIndexResults, File reportDirectory) throws Exception {
        String reportSuffix = null;
        String reportName = null;
        StringBuffer reportBuffer = new StringBuffer();
        File reportFile = null;
        String reportNameDateFormat = "MM.dd.yyyy-HH.mm.ss";
        String printReportDateFormat = "MM/dd/yyyy HH:mm:ss";

        reportSuffix = DupFileUtility.getDateString(Calendar.getInstance().getTime(), reportNameDateFormat);

        reportName = "file-index-report-" + reportSuffix + ".txt";

        reportBuffer.append("******Printing Results*****");
        reportBuffer.append("\n   Date Test Run Started = " + fileIndexResults.getDateTestRanStartedDisplayString());
        reportBuffer.append("\n   Date Test Run Ended = " + fileIndexResults.getDateTestRanEndedDisplayString());
        reportBuffer.append("\n   Test Duration = " + fileIndexResults.getTestDurationDisplayString());
        reportBuffer.append("\n   Search Terms Searched:");
        reportBuffer.append("\n   Directories Searched:");
        for (File directorySearched : fileIndexResults.getDirectoriesSearched()) {
            reportBuffer.append("\n      " + directorySearched.getAbsolutePath());
        }
        reportBuffer.append("\n   Directories NOT Searched:");
        for (File directoryNotSearched : fileIndexResults.getDirectoriesNotSearched()) {
            reportBuffer.append("\n      " + directoryNotSearched.getAbsolutePath());
        }
        reportBuffer.append("\n   Total # files processed = " + fileIndexResults.getNumFilesProcessed());
        reportBuffer.append("\n   Total # file entities = " + fileIndexResults.getNumberOfFileEntities());
        reportBuffer.append("\n   Total File Size = " + fileIndexResults.getTotalFilesSize());
        reportBuffer.append("\n   Total Number of Directories = " + fileIndexResults.getTotalNumDirectories());
        reportBuffer.append("\n   Number of Part Files = " + fileIndexResults.getNumPartFiles());
        reportBuffer.append("\n   Size of Part Files = " + fileIndexResults.getTotalPartFileSize());
        reportBuffer.append("\n   Number of Errors = " + fileIndexResults.getNumErrors());
        reportBuffer.append("\n   File names Removed:");
        for (String key : fileIndexResults.getRemovedFileEntityMap().keySet()) {
            reportBuffer.append("\n      " + key);
        }
        reportBuffer.append("\n   File names Added:");
        for (String key : fileIndexResults.getAddedFileEntityMap().keySet()) {
            reportBuffer.append("\n      " + key);
        }
        reportBuffer.append("\n   Files Locations Removed:");
        for (FileLocationEntity fileLocation : fileIndexResults.getRemovedFileLocations()) {
            reportBuffer.append("\n      " + fileLocation.getAbsolutePath());
        }
        reportBuffer.append("\n   Files Locations Added:");
        for (FileLocationEntity fileLocation : fileIndexResults.getAddedFileLocations()) {
            reportBuffer.append("\n      " + fileLocation.getAbsolutePath());
        }
        reportBuffer.append("\n");

        reportBuffer.append("\n******Printing Index Results for " + fileIndexResults.getFileEntityMap().size()
                + " File Names *****");

        for (String fileName : fileIndexResults.getFileEntityMap().keySet()) {
            FileEntity fileEntity = fileIndexResults.getFileEntityMap().get(fileName);
            List<FileLocationEntity> fileLocations = null;

            fileLocations = fileEntity.getFileLocations();

            reportBuffer.append("\n");
            reportBuffer.append("\n   There were " + fileLocations.size() + " file locations for fileName, '"
                    + fileEntity.getFileName() + "'.  Printing Now");

            for (FileLocationEntity fileLocation : fileLocations) {
                reportBuffer.append("\n         " + fileLocation.getAbsolutePath());
            }
        }

        System.out.println("");
        System.out.println("Here's the report");
        System.out.println("");
        System.out.println(reportBuffer);

        if (reportDirectory != null) {
            reportFile = new File(reportDirectory.getAbsolutePath() + "\\" + reportName);

            DupFileUtility.writeToFile(reportFile, reportBuffer);
        }
    }

    public static void main(String[] args) throws Exception {
        // testPreferences(args);
        testIndexFiles(args);
    }

    public static void testIndexFiles(String[] args) throws Exception {
        FileIndexService fileSearchService = null;
        List<File> searchDirectoryFiles = null;
        File targetReportDirectory = null;
        List<String> matchStrings = null;
        FileIndexResults fileIndexResults = null;

        targetReportDirectory = new File("C:\\Users\\splas_000\\speeches\\indexes");
        // targetReportDirectory = new
        // File("C:\\Users\\splas_000\\Google Drive\\speeches\\economic\\keyes\\search");

        searchDirectoryFiles = new ArrayList<File>();
        searchDirectoryFiles.add(new File("C:\\Users\\splas_000\\Google Drive\\speeches\\economic\\keyes\\capitalism"));
        searchDirectoryFiles.add(new File("C:\\Users\\splas_000\\Google Drive\\speeches\\economic\\marx"));

        // searchDirectoryFiles.add(new
        // File("C:\\Users\\splas_000\\Google Drive\\speeches\\economic\\keyes\\capitalism"));
        // searchDirectoryFiles.add(new
        // File("C:\\Users\\splas_000\\Google Drive\\speeches\\economic\\marx"));

        matchStrings = new ArrayList<String>();
        matchStrings.add("~index~");
        matchStrings.add("~image~jpg");

        fileSearchService = new FileIndexService();

        fileIndexResults = fileSearchService.indexAllFiles(searchDirectoryFiles, matchStrings);

        fileSearchService.printResults(fileIndexResults, targetReportDirectory);

        Timer timer = new Timer();

        timer.startTiming();
        FileEntity searchEntitty = fileSearchService.retrieveFileEntity("sheron-106");
        timer.stopTiming();

        System.out.println("Time it took to retrieve an entity:  " + searchEntitty.getFileName() + ".  "
                + timer.getDurationString());
    }

    public static void testPreferences(String[] args) throws Exception {
        FileIndexPreferences fileIndexPreferences = null;

        FileIndexService fileIndexService = new FileIndexService();

        fileIndexPreferences = fileIndexService.retrieveFileIndexPreferences();

        System.out.println("Just pulled the preferences BEFORE save.  Here's values");
        System.out.println("  # of default directories:  " + fileIndexPreferences.getDefaultSearchDirectories().size());
        for (File defaultSearchDirectory : fileIndexPreferences.getDefaultSearchDirectories()) {
            System.out.println("    defaultSearchDirectory:  " + defaultSearchDirectory.getAbsolutePath());
        }
        System.out.println("  # of default folder match strings:  "
                + fileIndexPreferences.getDefaultExcludeDirectoryNames().size());
        for (String defaultFolderMatchString : fileIndexPreferences.getDefaultExcludeDirectoryNames()) {
            System.out.println("    defaultFolderMatchString:  " + defaultFolderMatchString);
        }

        fileIndexPreferences.getDefaultSearchDirectoryNames().add(
                "C:\\Users\\splas_000\\Google Drive\\speeches\\economic\\keyes\\capitalism");
        fileIndexPreferences.getDefaultSearchDirectoryNames().add(
                "C:\\Users\\splas_000\\Google Drive\\speeches\\economic\\marx");

        // fileIndexPreferences.getDefaultExcludeDirectoryNames().add("\\rated\\");
        // fileIndexPreferences.getDefaultExcludeDirectoryNames().add("\\projects\\");

        fileIndexPreferences.setDefaultReportDirectory("C:\\Users\\splas_000\\speeches\\indexes");

        fileIndexService.saveFileIndexPreferences(fileIndexPreferences);

        fileIndexPreferences = null;

        fileIndexPreferences = fileIndexService.retrieveFileIndexPreferences();

        System.out.println("\n");

        System.out.println("Just pulled the preferences AFTER save.  Here's values");
        System.out.println("  default Report Directory:  " + fileIndexPreferences.getDefaultReportDirectory());
        System.out.println("  # of default directories:  " + fileIndexPreferences.getDefaultSearchDirectories().size());
        for (File defaultSearchDirectory : fileIndexPreferences.getDefaultSearchDirectories()) {
            System.out.println("    defaultSearchDirectory:  " + defaultSearchDirectory.getAbsolutePath());
        }
        System.out.println("  # of default Exclude Directory strings:  "
                + fileIndexPreferences.getDefaultExcludeDirectoryNames().size());
        for (String defaultFolderMatchString : fileIndexPreferences.getDefaultExcludeDirectoryNames()) {
            System.out.println("    defaultFolderMatchString:  " + defaultFolderMatchString);
        }

    }

}
