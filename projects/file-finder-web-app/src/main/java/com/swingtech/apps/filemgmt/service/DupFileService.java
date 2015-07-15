package com.swingtech.apps.filemgmt.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.swingtech.apps.filemgmt.dao.DupFilePreferencesDao;
import com.swingtech.apps.filemgmt.dao.FileIndexDao;
import com.swingtech.apps.filemgmt.dao.impl.file.FileIndexhDaoImplFileBased;
import com.swingtech.apps.filemgmt.model.DupFile;
import com.swingtech.apps.filemgmt.model.DupFileFinderResults;
import com.swingtech.apps.filemgmt.model.DupFilePreferences;
import com.swingtech.apps.filemgmt.model.FileEntity;
import com.swingtech.apps.filemgmt.util.DupFileUtility;
import com.swingtech.apps.filemgmt.util.FileMapWalker;
import com.swingtech.apps.filemgmt.util.Timer;

public class DupFileService {
    DupFilePreferencesDao dupFilePreferencesDao = new DupFilePreferencesDao();
    FileIndexDao fileIndexDao = new FileIndexhDaoImplFileBased();

    public DupFilePreferences retrieveDupFilePreferences() {
        try {
            return dupFilePreferencesDao.retrieveDupFilePreferences();
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void saveDupFilePreferences(DupFilePreferences dupFilePreferences) {
        try {
            dupFilePreferencesDao.saveDupFilePreferences(dupFilePreferences);
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public DupFileFinderResults findAndMoveAllDuplicatesWithStrings(List<String> searchDirectoryFileNames,
            String targetDupDirectoryName, String targetPartDirectoryName, List<String> matchStrings,
            boolean moveDuplicates, boolean movePartFiles, boolean walkFileSystem) throws Exception {
        DupFileFinderResults dupFileFinderResults = null;
        List<File> searchDirectoryFiles = new ArrayList<File>();
        File targetDupDirectory = null;
        File targetPartDirectory = null;

        File searchDirectoryFile = null;

        for (String searchDirectoryFileName : searchDirectoryFileNames) {
            if (searchDirectoryFileName != null && !searchDirectoryFileName.trim().isEmpty()) {
                searchDirectoryFile = new File(searchDirectoryFileName);
                searchDirectoryFiles.add(searchDirectoryFile);
            }
        }

        targetDupDirectory = new File(targetDupDirectoryName);
        targetPartDirectory = new File(targetPartDirectoryName);

        return this.findAndMoveAllDuplicates(searchDirectoryFiles, targetDupDirectory, targetPartDirectory,
                matchStrings, moveDuplicates, movePartFiles, walkFileSystem);
    }

    public DupFileFinderResults findAndMoveAllDuplicates(List<File> searchDirectoryFiles, File targetDupDirectory,
            File targetPartDirectory, List<String> matchStrings, boolean moveDuplicates, boolean movePartFiles,
            boolean walkFileSystem) throws Exception {
        DupFileFinderResults dupFileFinderResults = null;
        Timer timer = new Timer();

        timer.startTiming();

        dupFileFinderResults = this.findDupFiles(searchDirectoryFiles, walkFileSystem);

        if (movePartFiles) {
            this.movePartFiles(dupFileFinderResults, targetPartDirectory);
        }

        if (moveDuplicates) {
            this.moveDuplicates(dupFileFinderResults, targetDupDirectory, matchStrings);
        }

        timer.stopTiming();
        dupFileFinderResults.setTimer(timer);

        return dupFileFinderResults;
    }

    public DupFileFinderResults findDupFiles(List<File> searchDirectoryFiles, boolean walkFileSystem) throws Exception {
        DupFileFinderResults dupFileFinderResults = new DupFileFinderResults();

        if (searchDirectoryFiles == null) {
            throw new IllegalArgumentException("searchDirectoryFiles cannot be null");
        }

        System.out.println("\n\n\n");

        System.out.println("Printing Files Going to Search");

        for (File searchDirectoryFile : searchDirectoryFiles) {
            System.out.println("  " + searchDirectoryFile.getAbsolutePath());
        }
        System.out.println("\n\n\n");

        for (File searchDirectoryFile : searchDirectoryFiles) {

            if (!searchDirectoryFile.exists()) {
                dupFileFinderResults.getDirectoriesNotSearched().add(searchDirectoryFile);
                continue;
            }
            this.walkDirectoryForDuplicates(searchDirectoryFile, dupFileFinderResults, walkFileSystem);
        }

        return dupFileFinderResults;
    }

    public void walkDirectoryForDuplicates(File searchDirectoryFile, DupFileFinderResults dupFileFinderResults,
            boolean walkFileSystem) throws Exception {
        System.out.println(searchDirectoryFile.getAbsolutePath());
        Path resultPath = null;
        DupFileVisitor dupFileVisitor = new DupFileVisitor(dupFileFinderResults);

        if (walkFileSystem) {
            dupFileFinderResults.getDirectoriesSearched().add(searchDirectoryFile);
            Path path = FileSystems.getDefault().getPath(searchDirectoryFile.getAbsolutePath());

            resultPath = Files.walkFileTree(path, dupFileVisitor);
        }
        else {
            Map<String, FileEntity> fileEntityMap = fileIndexDao.retrieveFileEntityMap();

            FileMapWalker.walkFileEntityMapForDup(fileEntityMap, dupFileVisitor, dupFileFinderResults);
        }
    }

    private void moveDuplicates(DupFileFinderResults dupFileFinderResults, File targetDupDirectoryirectory,
            List<String> matchStrings) {
        File file = null;
        File renameTofile = null;
        DupFile dupFile = null;
        int indexOfDupFileToStay;

        if (!targetDupDirectoryirectory.exists()) {
            throw new IllegalArgumentException("dupDirectory, '" + targetDupDirectoryirectory.getAbsolutePath()
                    + "', does not exist on the file system.");
        }

        for (String fileName : dupFileFinderResults.getDupFiles().keySet()) {
            String fileNameExtension = null;
            dupFile = dupFileFinderResults.getDupFiles().get(fileName);
            boolean renameSuccessful = false;

            // 1 of the duplicate files will stay right where it is. All other
            // dups will be moved. Find the index of hte file to stay.
            indexOfDupFileToStay = this.findIndexOfDupFileToStay(dupFile.getDupFileLocations(), matchStrings);

            if (indexOfDupFileToStay < 0) {
                // if the indexOfDupFile to stay is -1, that means the file is
                // in more than 1 director that matches one of the mathStrings,
                // so don't move it.
                // Let the user decide which one stays and which one goes.
                dupFileFinderResults.getDupFilesThatCantBeMoved().put(fileName, dupFile);
                break;
            }

            for (int i = 0; i < dupFile.getDupFileLocations().size(); i++) {
                file = dupFile.getDupFileLocations().get(i);

                if (i == indexOfDupFileToStay) {
                    dupFileFinderResults.getDuplicatesNotMoved().add(file);
                    continue;
                }

                renameTofile = this.getRenameToFile(targetDupDirectoryirectory, file);

                try {
                    renameSuccessful = file.renameTo(renameTofile);
                }
                catch (Exception e) {
                    renameSuccessful = false;
                }

                if (renameSuccessful) {
                    dupFileFinderResults.getDuplicatesMoved().put(file, renameTofile);
                }
                else {
                    dupFileFinderResults.getDuplicatesErrorTryingToMove().put(file, renameTofile);
                }
            }
        }
    }

    private void movePartFiles(DupFileFinderResults dupFileFinderResults, File targetPartDirectory) {
        File renameTofile = null;
        DupFile dupFile = null;

        if (!targetPartDirectory.exists()) {
            throw new IllegalArgumentException("partDirectory, '" + targetPartDirectory.getAbsolutePath()
                    + "', does not exist on the file system.");
        }

        int i = -1;

        for (File file : dupFileFinderResults.getPartFiles()) {
            renameTofile = this.getRenameToFile(targetPartDirectory, file);
            boolean renameSuccessful = false;

            i++;

            try {
                renameSuccessful = file.renameTo(renameTofile);
                // renameSuccessful = true;
            }
            catch (Exception e) {
                renameSuccessful = false;
            }

            if (renameSuccessful) {
                dupFileFinderResults.getPartsMoved().put(file, renameTofile);
            }
            else {
                dupFileFinderResults.getPartsErrorTryingToMove().put(file, renameTofile);
            }
        }
    }

    public File getRenameToFile(File targetDupDirectoryirectory, File file) {
        File renameToFile = null;
        String fileNameWithoutExtension = null;
        String fileNameExtension = null;
        String renameToFileName = null;
        int index = 0;

        fileNameWithoutExtension = DupFileUtility.getFileNameWithoutExtension(file);
        fileNameExtension = DupFileUtility.getFileNameExtension(file);

        renameToFile = new File(targetDupDirectoryirectory.getAbsolutePath() + "\\" + fileNameWithoutExtension + "."
                + fileNameExtension);

        while (renameToFile.exists()) {
            index++;
            renameToFile = new File(targetDupDirectoryirectory.getAbsolutePath() + "\\" + fileNameWithoutExtension
                    + "_Copy" + index + "." + fileNameExtension);
        }

        return renameToFile;
    }

    private int findIndexOfDupFileToStay(List<File> dupFiles, List<String> matchStrings) {
        int index = 0;
        int matchCount = 0;

        if (matchStrings == null || matchStrings.isEmpty()) {
            throw new IllegalArgumentException("matchStringsCan't be empty");
        }

        for (int i = 0; i < dupFiles.size(); i++) {
            File file = dupFiles.get(i);

            for (String matchString : matchStrings) {
                if (file.getAbsolutePath().contains(matchString)) {
                    index = i;
                    matchCount++;
                    break;
                }
            }
        }

        // if the file is in more than 1 director that matches one of the
        // mathStrings, then return -1, i.e. don't move it.
        // Let the user decide which one stays and which one goes.
        if (matchCount > 1) {
            return -1;
        }

        return index;
    }

    public void printResults(DupFileFinderResults dupFileFinderResults, File reportDirectory) throws Exception {
        String reportSuffix = null;
        String reportName = null;
        StringBuffer reportBuffer = new StringBuffer();
        File reportFile = null;
        String reportNameDateFormat = "MM.dd.yyyy-HH.mm.ss";
        String printReportDateFormat = "MM/dd/yyyy HH:mm:ss";

        if (!reportDirectory.exists()) {
            throw new IllegalArgumentException("Report Director, '" + reportDirectory.getAbsolutePath()
                    + "', does not exist");
        }

        reportSuffix = DupFileUtility.getDateString(Calendar.getInstance().getTime(), reportNameDateFormat);

        reportName = "duplicate-file-report-" + reportSuffix + ".txt";

        reportBuffer.append("******Printing Results*****");
        reportBuffer.append("\n   Date Test Run Started = "
                + DupFileUtility.getDateString(dupFileFinderResults.getDateTestRanStarted(), printReportDateFormat));
        reportBuffer.append("\n   Date Test Run Ended = "
                + DupFileUtility.getDateString(dupFileFinderResults.getDateTestRanEnded(), printReportDateFormat));
        reportBuffer.append("\n   Test Duration = " + dupFileFinderResults.getTestDurationDisplayString());
        reportBuffer.append("\n   Date Test Run Started = "
                + DupFileUtility.getDateString(dupFileFinderResults.getDateTestRanStarted(), printReportDateFormat));
        reportBuffer.append("\n   Directories Searched:");
        for (File directorySearched : dupFileFinderResults.getDirectoriesSearched()) {
            reportBuffer.append("\n      " + directorySearched.getAbsolutePath());
        }
        reportBuffer.append("\n   Total # files processed = " + dupFileFinderResults.getNumFilesProcessed());
        reportBuffer.append("\n   Total File Size = " + dupFileFinderResults.getTotalFilesSize());
        reportBuffer.append("\n   Total Number of Directories = " + dupFileFinderResults.getTotalNumDirectories());
        reportBuffer.append("\n   Number of Dups = " + dupFileFinderResults.getNumDup());
        reportBuffer.append("\n   Size of Dups = " + dupFileFinderResults.getDupFilesSize());
        reportBuffer.append("\n   Number of Part Files = " + dupFileFinderResults.getNumPartFiles());
        reportBuffer.append("\n   Size of Part Files = " + dupFileFinderResults.getTotalPartFileSize());
        reportBuffer.append("\n   Number of Errors = " + dupFileFinderResults.getNumErrors());
        reportBuffer.append("\n   directory list size = " + dupFileFinderResults.getDirectories().size());
        reportBuffer.append("\n   dup map size = " + dupFileFinderResults.getDupFiles().size());
        reportBuffer.append("\n");

        reportBuffer.append("\n******Printing " + dupFileFinderResults.getDupFiles().size() + " Duplicates*****");

        for (String fileName : dupFileFinderResults.getDupFiles().keySet()) {
            DupFile dupFile = dupFileFinderResults.getDupFiles().get(fileName);
            reportBuffer.append("\n   " + fileName);
            reportBuffer.append("\n     File Name = " + dupFile.getDupFileName());
            reportBuffer.append("\n     Total Dup File Size = " + dupFile.getTotalDupFileSize());
            reportBuffer.append("\n     Number of Dups = " + dupFile.getNumDups());
            reportBuffer.append("\n     Size of Dup List = " + dupFile.getDupFileLocations().size());
            reportBuffer.append("\n     List of all Dup Files...");

            for (File file : dupFile.getDupFileLocations()) {
                reportBuffer.append("\n         " + file.getAbsolutePath());
            }
        }

        reportBuffer.append("\n");
        reportBuffer.append("\n******Printing " + dupFileFinderResults.getPartFiles().size() + " Part Files*****");

        for (File file : dupFileFinderResults.getPartFiles()) {
            reportBuffer.append("\n   " + file.getAbsolutePath());
        }

        reportBuffer.append("\n");

        reportBuffer
                .append("\n******Printing "
                        + dupFileFinderResults.getDupFilesThatCantBeMoved().size()
                        + " Duplicates that could NOT be moved (because there was more than 1 file in a matching directory)*****");

        for (String fileName : dupFileFinderResults.getDupFilesThatCantBeMoved().keySet()) {
            DupFile dupFile = dupFileFinderResults.getDupFilesThatCantBeMoved().get(fileName);
            reportBuffer.append("\n   " + fileName);
            reportBuffer.append("\n     File Name = " + dupFile.getDupFileName());
            reportBuffer.append("\n     Total Dup File Size = " + dupFile.getTotalDupFileSize());
            reportBuffer.append("\n     Number of Dups = " + dupFile.getNumDups());
            reportBuffer.append("\n     Size of Dup List = " + dupFile.getDupFileLocations().size());
            reportBuffer.append("\n     List of all Dup Files...");

            for (File file : dupFile.getDupFileLocations()) {
                reportBuffer.append("\n         " + file.getAbsolutePath());
            }
        }

        reportBuffer.append("\n");
        reportBuffer.append("\n******Printing " + dupFileFinderResults.getDuplicatesMoved().size()
                + " Duplicates that were moved*****");

        for (File originalFile : dupFileFinderResults.getDuplicatesMoved().keySet()) {
            File renameToFile = dupFileFinderResults.getDuplicatesMoved().get(originalFile);
            reportBuffer.append("\n   From: " + originalFile.getAbsolutePath() + "      to: "
                    + renameToFile.getAbsolutePath());
        }

        reportBuffer.append("\n");
        reportBuffer.append("\n******Printing " + dupFileFinderResults.getDuplicatesNotMoved().size()
                + " Duplicates that did not move (because they are the original left behind)*****");

        for (File file : dupFileFinderResults.getDuplicatesNotMoved()) {
            reportBuffer.append("\n   " + file.getAbsolutePath());
        }

        reportBuffer.append("\n");
        reportBuffer.append("\n******Printing " + dupFileFinderResults.getDuplicatesErrorTryingToMove().size()
                + " Duplicates that had errors tryig to be moved*****");

        for (File originalFile : dupFileFinderResults.getDuplicatesErrorTryingToMove().keySet()) {
            File renameToFile = dupFileFinderResults.getDuplicatesErrorTryingToMove().get(originalFile);
            reportBuffer.append("\n   From: " + originalFile.getAbsolutePath() + "      to: "
                    + renameToFile.getAbsolutePath());
        }

        reportBuffer.append("\n");
        reportBuffer.append("\n******Printing " + dupFileFinderResults.getPartsMoved().size()
                + " Part files that were moved*****");

        for (File originalFile : dupFileFinderResults.getPartsMoved().keySet()) {
            File renameToFile = dupFileFinderResults.getPartsMoved().get(originalFile);
            reportBuffer.append("\n   From: " + originalFile.getAbsolutePath() + "      to: "
                    + renameToFile.getAbsolutePath());
        }

        reportBuffer.append("\n");
        reportBuffer.append("\n******Printing " + dupFileFinderResults.getPartsErrorTryingToMove().size()
                + " Part files that had errors trying to be moved*****");

        for (File originalFile : dupFileFinderResults.getPartsErrorTryingToMove().keySet()) {
            File renameToFile = dupFileFinderResults.getPartsErrorTryingToMove().get(originalFile);
            reportBuffer.append("\n   From: " + originalFile.getAbsolutePath() + "      to: "
                    + renameToFile.getAbsolutePath());
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

    private void tempTest2() {
        String dupDirectory = "C:\\Users\\Ric\\Google Drive\\speeches\\economic\\keyes\\dups\\new";
        List<String> fileNames = new ArrayList<String>();
        String renameToFileName = null;
        File renameToFile;

        fileNames.add("C:\\Users\\Ric\\Google Drive\\speeches\\economic\\keyes\\capitalism\\bangb\\ff7406500k");
        fileNames.add("C:\\Users\\Ric\\Google Drive\\speeches\\economic\\keyes\\capitalism\\bangb\\.ff7406500k.wmv");
        fileNames.add("C:\\Users\\Ric\\Google Drive\\speeches\\economic\\keyes\\capitalism\\bangb\\.ff7406500k");
        fileNames.add("C:\\Users\\Ric\\Google Drive\\speeches\\economic\\keyes\\capitalism\\bangb\\.ff7406500k.");
        fileNames.add("C:\\Users\\Ric\\Google Drive\\speeches\\economic\\keyes\\capitalism\\bangb\\ff7406500k.wmv");
        fileNames.add("C:\\Users\\Ric\\Google Drive\\speeches\\economic\\keyes\\capitalism\\bangb\\ff7406500k.");
        fileNames.add("C:\\Users\\Ric\\Google Drive\\speeches\\economic\\keyes\\capitalism\\bangb\\ff740.6500k.wmv");
        fileNames.add("C:\\Users\\Ric\\Google Drive\\speeches\\economic\\keyes\\capitalism\\bangb\\ff7406500k.wmv");
        fileNames.add("C:\\Users\\Ric\\Google Drive\\speeches\\economic\\keyes\\capitalism\\bangb\\ff7406500k.w");

        for (String fileName : fileNames) {
            File file = new File(fileName);
            renameToFile = this.getRenameToFile(new File(dupDirectory), file);

            System.out.println(renameToFile.getAbsolutePath());
            System.out.println("   fileName:          |" + DupFileUtility.getFileNameWithoutExtension(renameToFile)
                    + "|");
            System.out.println("   fileNameExtension: |" + DupFileUtility.getFileNameExtension(renameToFile) + "|");
        }
    }

    private static void tempTest() {
        List<String> fileNames = new ArrayList<String>();

        fileNames.add("C:\\Users\\Ric\\Google Drive\\speeches\\economic\\keyes\\capitalism\\bangb\\ff7406500k.wmv");
        fileNames.add("C:\\Users\\Ric\\Google Drive\\speeches\\economic\\keyes\\capitalism\\bangb\\ff7406500k");
        fileNames.add("C:\\Users\\Ric\\Google Drive\\speeches\\economic\\keyes\\capitalism\\bangb\\.ff7406500k.wmv");
        fileNames.add("C:\\Users\\Ric\\Google Drive\\speeches\\economic\\keyes\\capitalism\\bangb\\.ff7406500k");
        fileNames.add("C:\\Users\\Ric\\Google Drive\\speeches\\economic\\keyes\\capitalism\\bangb\\.ff7406500k.");
        fileNames.add("C:\\Users\\Ric\\Google Drive\\speeches\\economic\\keyes\\capitalism\\bangb\\ff7406500k.wmv");
        fileNames.add("C:\\Users\\Ric\\Google Drive\\speeches\\economic\\keyes\\capitalism\\bangb\\ff7406500k.");
        fileNames.add("C:\\Users\\Ric\\Google Drive\\speeches\\economic\\keyes\\capitalism\\bangb\\ff740.6500k.wmv");
        fileNames.add("C:\\Users\\Ric\\Google Drive\\speeches\\economic\\keyes\\capitalism\\bangb\\ff7406500k.w");

        for (String fileName : fileNames) {
            File file = new File(fileName);
            System.out.println(fileName);
            System.out.println("   fileName:          |" + DupFileUtility.getFileNameWithoutExtension(file) + "|");
            System.out.println("   fileNameExtension: |" + DupFileUtility.getFileNameExtension(file) + "|");
        }
    }

    private static void tempTest3() {
        Date date = Calendar.getInstance().getTime();
        String format = "MM.dd.yyyy-HH.mm.ss";

        String dateString = DupFileUtility.getDateString(date, format);

        System.out.println("dateString:  " + dateString);
    }

    public static void testPreferences(String[] args) throws Exception {
        DupFilePreferences dupFilePreferences = null;

        DupFileService dupFileService = new DupFileService();

        dupFilePreferences = dupFileService.retrieveDupFilePreferences();

        System.out.println("Just pulled the preferences BEFORE save.  Here's values");
        System.out.println("  defaultTargetDupDirectory:  " + dupFilePreferences.getDefaultTargetDupDirectory());
        System.out.println("  defaultTargetPartDirectory:  " + dupFilePreferences.getDefaultTargetPartDirectory());
        System.out.println("  # of default directories:  " + dupFilePreferences.getDefaultSearchDirectories().size());
        for (File defaultSearchDirectory : dupFilePreferences.getDefaultSearchDirectories()) {
            System.out.println("    defaultSearchDirectory:  " + defaultSearchDirectory.getAbsolutePath());
        }
        System.out.println("  # of default folder match strings:  "
                + dupFilePreferences.getDefaultFolderMatchStrings().size());
        for (String defaultFolderMatchString : dupFilePreferences.getDefaultFolderMatchStrings()) {
            System.out.println("    defaultFolderMatchString:  " + defaultFolderMatchString);
        }

        dupFilePreferences.setDefaultTargetDupDirectory("C:\\Users\\splas_000\\speeches\\dups\\new3\\dups");
        dupFilePreferences.setDefaultTargetPartDirectory("C:\\Users\\splas_000\\speeches\\dups\\new3\\parts");

        dupFilePreferences.getDefaultSearchDirectoryNames().add(
                "C:\\Users\\splas_000\\Google Drive\\speeches\\economic\\keyes\\capitalism");
        dupFilePreferences.getDefaultSearchDirectoryNames().add(
                "C:\\Users\\splas_000\\Google Drive\\speeches\\economic\\marx");

        dupFilePreferences.getDefaultFolderMatchStrings().add("\\rated\\");
        dupFilePreferences.getDefaultFolderMatchStrings().add("\\projects\\");

        dupFileService.saveDupFilePreferences(dupFilePreferences);

        dupFilePreferences = null;

        dupFilePreferences = dupFileService.retrieveDupFilePreferences();

        System.out.println("\n");

        System.out.println("Just pulled the preferences AFTER save.  Here's values");
        System.out.println("  defaultTargetDupDirectory:  " + dupFilePreferences.getDefaultTargetDupDirectory());
        System.out.println("  defaultTargetPartDirectory:  " + dupFilePreferences.getDefaultTargetPartDirectory());
        System.out.println("  # of default directories:  " + dupFilePreferences.getDefaultSearchDirectories().size());
        for (File defaultSearchDirectory : dupFilePreferences.getDefaultSearchDirectories()) {
            System.out.println("    defaultSearchDirectory:  " + defaultSearchDirectory.getAbsolutePath());
        }
        System.out.println("  # of default folder match strings:  "
                + dupFilePreferences.getDefaultFolderMatchStrings().size());
        for (String defaultFolderMatchString : dupFilePreferences.getDefaultFolderMatchStrings()) {
            System.out.println("    defaultFolderMatchString:  " + defaultFolderMatchString);
        }

    }

    public static void testService(String[] args) throws Exception {
        // tempTest();
        // tempTest3();

        DupFileService dupFileService = null;
        List<File> searchDirectoryFiles = null;
        File targetDupDirectory = null;
        File targetPartDirectory = null;
        File targetReportDirectory = null;
        List<String> matchStrings = null;
        boolean moveDuplicates;
        boolean movePartFiles;
        DupFileFinderResults dupFileFinderResults = null;

        // targetDupDirectory = new
        // File("C:\\Users\\Ric\\Google Drive\\speeches\\economic\\keyes\\dups\\new\\dups");
        // targetPartDirectory = new
        // File("C:\\Users\\Ric\\Google Drive\\speeches\\economic\\keyes\\dups\\new\\parts");
        // targetReportDirectory = new
        // File("C:\\Users\\Ric\\Google Drive\\speeches\\economic\\keyes\\dups\\new");
        //
        // searchDirectoryFiles = new ArrayList<File>();
        // searchDirectoryFiles.add(new
        // File("C:\\Users\\Ric\\Google Drive\\speeches\\economic\\keyes\\capitalism"));
        // searchDirectoryFiles.add(new
        // File("C:\\Users\\Ric\\Google Drive\\speeches\\economic\\marx"));
        //
        // matchStrings = new ArrayList<String>();
        // matchStrings.add("\\rated\\");

        targetDupDirectory = new File("C:\\Users\\splas_000\\speeches\\dups\\new3\\dups");
        targetPartDirectory = new File("C:\\Users\\splas_000\\speeches\\dups\\new3\\parts");
        targetReportDirectory = new File("C:\\Users\\splas_000\\speeches\\dups\\new3");

        searchDirectoryFiles = new ArrayList<File>();
        searchDirectoryFiles.add(new File("C:\\Users\\splas_000\\Google Drive\\speeches\\economic\\keyes\\capitalism"));
        searchDirectoryFiles.add(new File("C:\\Users\\splas_000\\Google Drive\\speeches\\economic\\marx"));

        matchStrings = new ArrayList<String>();
        matchStrings.add("\\rated\\");
        matchStrings.add("\\projects\\");

        moveDuplicates = false;
        movePartFiles = false;

        dupFileService = new DupFileService();

        dupFileFinderResults = dupFileService.findAndMoveAllDuplicates(searchDirectoryFiles, targetDupDirectory,
                targetPartDirectory, matchStrings, moveDuplicates, movePartFiles, false);

        dupFileService.printResults(dupFileFinderResults, targetReportDirectory);
    }

}
