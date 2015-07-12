package com.swingtech.apps.filemgmt.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.swingtech.apps.filemgmt.dao.FileIndexDao;
import com.swingtech.apps.filemgmt.dao.FileSearchPreferencesDao;
import com.swingtech.apps.filemgmt.dao.impl.file.FileIndexhDaoImplFileBased;
import com.swingtech.apps.filemgmt.model.FileEntity;
import com.swingtech.apps.filemgmt.model.FileSearchPreferences;
import com.swingtech.apps.filemgmt.model.FileSearchResult;
import com.swingtech.apps.filemgmt.model.FileSearchResults;
import com.swingtech.apps.filemgmt.model.MoveFilesResults;
import com.swingtech.apps.filemgmt.model.SearchTermResults;
import com.swingtech.apps.filemgmt.model.VideoPlayerInputs;
import com.swingtech.apps.filemgmt.util.DupFileUtility;
import com.swingtech.apps.filemgmt.util.FileMapWalker;
import com.swingtech.apps.filemgmt.util.Timer;

public class FileSearchService {
    FileSearchPreferencesDao fileSearchPreferencesDao = new FileSearchPreferencesDao();
    FileIndexDao fileIndexDao = new FileIndexhDaoImplFileBased();

    public FileSearchPreferences retrieveFileSearchPreferences() {
        try {
            return fileSearchPreferencesDao.retrieveFileSearchPreferences();
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void saveFileSearchPreferences(FileSearchPreferences fileSearchPreferences) {
        try {
            fileSearchPreferencesDao.saveFileSearchPreferences(fileSearchPreferences);
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public VideoPlayerInputs getVideoPlayerInputs(String videoFileName) {
        VideoPlayerInputs videoPlayerInputs = new VideoPlayerInputs();
        File videoFile = new File(videoFileName);

        String fileExtension = DupFileUtility.getFileNameExtension(videoFile);
        String fileName = DupFileUtility.getFileName(videoFile);
        String fileNameWithoutExtesion = DupFileUtility.getFileNameWithoutExtension(videoFile);

        videoPlayerInputs.setVideoFileNameAbsolutePath(videoFileName);
        videoPlayerInputs.setVideoFileName(fileName);
        videoPlayerInputs.setVideoFileNameWithoutExtension(fileNameWithoutExtesion);
        videoPlayerInputs.setFileType(fileExtension);
        videoPlayerInputs.setFileMimeType(DupFileUtility.getFileMimeTypeFromFileExtension(fileExtension));
        videoPlayerInputs.setVideoFile(videoFile);

        return videoPlayerInputs;
    }

    public MoveFilesResults moveFiles(List<String> fileNamesToMove, String moveToDirectory) {
        MoveFilesResults moveToFileResults = null;
        Map<String, String> errors = new HashMap<String, String>();
        File directoryToMoveFile = null;
        File existingFile = null;
        File moveToFile = null;
        boolean renameSuccessful;
        Timer timer = new Timer();
        String fileName = null;

        timer.startTiming();

        if (fileNamesToMove == null || fileNamesToMove.isEmpty()) {
            throw new IllegalArgumentException("fileNamesToMove passed in was null or empty");
        }

        if (moveToDirectory == null || moveToDirectory.isEmpty()) {
            throw new IllegalArgumentException("moveToDirectory passed in was null or empty");
        }

        directoryToMoveFile = new File(moveToDirectory);

        if (!directoryToMoveFile.exists()) {
            throw new IllegalArgumentException("moveToDirectory, '" + directoryToMoveFile.getAbsolutePath()
                    + "', does not exist.");
        }

        if (!directoryToMoveFile.isDirectory()) {
            throw new IllegalArgumentException("moveToDirectory, '" + directoryToMoveFile.getAbsolutePath()
                    + "', is not a directory");
        }

        moveToFileResults = new MoveFilesResults();
        moveToFileResults.setErrors(errors);

        for (String fileNameToBeMoved : fileNamesToMove) {
            existingFile = new File(fileNameToBeMoved);

            fileName = DupFileUtility.getFileName(existingFile);

            moveToFile = new File(directoryToMoveFile.getAbsolutePath() + "\\" + fileName);

            moveToFileResults.setNumFilesProcessed(moveToFileResults.getNumFilesProcessed() + 1);

            if (!existingFile.exists()) {
                errors.put(fileNameToBeMoved, "File to move, '" + existingFile.getAbsolutePath() + "' does not exist");
                moveToFileResults.getFilesNotMoved().put(existingFile, moveToFile);
                continue;
            }

            try {
                renameSuccessful = existingFile.renameTo(moveToFile);
            }
            catch (Exception e) {
                errors.put(
                        fileNameToBeMoved,
                        "Error trying to rename file from '" + existingFile.getAbsolutePath() + "' to '"
                                + moveToFile.getAbsolutePath() + "'.  Error:  " + e.getClass().getName() + ":  "
                                + e.getMessage());
                moveToFileResults.getFilesNotMoved().put(existingFile, moveToFile);
                continue;
            }

            if (!renameSuccessful) {
                errors.put(fileNameToBeMoved,
                        "Unknown Error trying to rename file from '" + existingFile.getAbsolutePath() + "' to '"
                                + moveToFile.getAbsolutePath() + "'.  The return operation returned false");
                moveToFileResults.getFilesNotMoved().put(existingFile, moveToFile);
                continue;
            }

            moveToFileResults.getFilesMoved().put(existingFile, moveToFile);
        }

        timer.stopTiming();

        moveToFileResults.setTimer(timer);

        return moveToFileResults;
    }

    public FileSearchResults findSearchMatchesWithStrings(List<String> searchDirectoryFileNames,
            List<String> matchStrings, boolean walkFileSystem) throws Exception {
        List<File> searchDirectoryFiles = new ArrayList<File>();
        File searchDirectoryFile = null;

        for (String searchDirectoryFileName : searchDirectoryFileNames) {
            searchDirectoryFile = new File(searchDirectoryFileName);
            searchDirectoryFiles.add(searchDirectoryFile);
        }

        return this.findSearchMatchesWithFiles(searchDirectoryFiles, matchStrings, walkFileSystem);
    }

    public FileSearchResults findSearchMatchesWithFiles(List<File> searchDirectoryFiles, List<String> matchStrings,
            boolean walkFileSystem) throws Exception {
        FileSearchResults fileSearchResults = null;
        Timer timer = new Timer();

        timer.startTiming();

        fileSearchResults = this.searchDirectoriesForMatches(searchDirectoryFiles, matchStrings, walkFileSystem);

        timer.stopTiming();

        fileSearchResults.setSearchTermsSearched(matchStrings);
        fileSearchResults.setTimer(timer);

        return fileSearchResults;
    }

    private FileSearchResults searchDirectoriesForMatches(List<File> searchDirectoryFiles, List<String> matchStrings,
            boolean walkFileSystem) throws Exception {
        FileSearchResults fileSearchResults = new FileSearchResults();

        if (searchDirectoryFiles == null) {
            throw new IllegalArgumentException("searchDirectoryFiles cannot be null");
        }

        if (walkFileSystem) {
            for (File searchDirectoryFile : searchDirectoryFiles) {
                if (!searchDirectoryFile.exists()) {
                    fileSearchResults.getDirectoriesNotSearched().add(searchDirectoryFile);
                    continue;
                }
                this.walkDirectoryForSearchResults(searchDirectoryFile, fileSearchResults, matchStrings, walkFileSystem);
            }
        }
        else {
            this.walkDirectoryForSearchResults(null, fileSearchResults, matchStrings, walkFileSystem);
        }

        return fileSearchResults;
    }

    private void walkDirectoryForSearchResults(File searchDirectoryFile, FileSearchResults fileSearchResults,
            List<String> matchStrings, boolean walkFileSystem) throws Exception {
        Path resultPath = null;
        FileSearchVisitor fileSearchVisitor = new FileSearchVisitor(fileSearchResults, matchStrings);
        Timer timer = null;

        if (walkFileSystem) {
            fileSearchResults.getDirectoriesSearched().add(searchDirectoryFile);

            Path path = FileSystems.getDefault().getPath(searchDirectoryFile.getAbsolutePath());

            timer = new Timer();

            timer.startTiming();
            resultPath = Files.walkFileTree(path, fileSearchVisitor);
            timer.stopTiming();

            System.out.println("\n\n");
            System.out.println("time it took to retrieve fileEntityMap (walking fileSystem):  "
                    + timer.getDurationString());
        }
        else {
            timer = new Timer();

            timer.startTiming();
            Map<String, FileEntity> fileEntityMap = fileIndexDao.retrieveFileEntityMap();
            timer.stopTiming();

            System.out.println("\n\n");
            System.out.println("time it took to retrieve fileEntityMap:  " + timer.getDurationString());

            timer = new Timer();

            timer.startTiming();
            FileMapWalker.walkFileEntityMapForSearch(fileEntityMap, fileSearchVisitor, fileSearchResults);
            timer.stopTiming();

            System.out.println("time it took to retrieve fileEntityMap:  " + timer.getDurationString());

            System.out.println("\n\n");

        }
    }

    public void printSearchResults(FileSearchResults fileSearchResults, File reportDirectory) throws Exception {
        String reportSuffix = null;
        String reportName = null;
        StringBuffer reportBuffer = new StringBuffer();
        File reportFile = null;
        String reportNameDateFormat = "MM.dd.yyyy-HH.mm.ss";
        String printReportDateFormat = "MM/dd/yyyy HH:mm:ss";

        reportSuffix = DupFileUtility.getDateString(Calendar.getInstance().getTime(), reportNameDateFormat);

        reportName = "duplicate-file-report-" + reportSuffix + ".txt";

        reportBuffer.append("******Printing Results*****");
        reportBuffer.append("\n   Date Test Run Started = " + fileSearchResults.getDateTestRanStartedDisplayString());
        reportBuffer.append("\n   Date Test Run Ended = " + fileSearchResults.getDateTestRanEndedDisplayString());
        reportBuffer.append("\n   Test Duration = " + fileSearchResults.getTestDurationDisplayString());
        reportBuffer.append("\n   Search Terms Searched:");
        for (String searchTermSearched : fileSearchResults.getSearchTermsSearched()) {
            reportBuffer.append("\n      " + searchTermSearched);
        }
        reportBuffer.append("\n   Directories Searched:");
        for (File directorySearched : fileSearchResults.getDirectoriesSearched()) {
            reportBuffer.append("\n      " + directorySearched.getAbsolutePath());
        }
        reportBuffer.append("\n   Directories NOT Searched:");
        for (File directoryNotSearched : fileSearchResults.getDirectoriesNotSearched()) {
            reportBuffer.append("\n      " + directoryNotSearched.getAbsolutePath());
        }
        reportBuffer.append("\n   Total # files processed = " + fileSearchResults.getNumFilesProcessed());
        reportBuffer.append("\n   Total # Search Matches = " + fileSearchResults.getNumFilesMatchSearch());
        reportBuffer.append("\n   Total File Size = " + fileSearchResults.getTotalFileSizeDisplayString());
        reportBuffer.append("\n   Total Number of Directories = " + fileSearchResults.getTotalNumDirectories());
        reportBuffer.append("\n   Number of Part Files = " + fileSearchResults.getNumPartFiles());
        reportBuffer.append("\n   Size of Part Files = " + fileSearchResults.getTotalPartFileSizeDisplayString());
        reportBuffer.append("\n   Number of Errors = " + fileSearchResults.getNumErrors());
        reportBuffer.append("\n");

        reportBuffer.append("\n******Printing Search Results for " + fileSearchResults.getSearchTermResults().size()
                + " Search Terms *****");

        for (SearchTermResults searchTermResults : fileSearchResults.getSearchTermResults()) {
            List<FileSearchResult> matchingFileList = searchTermResults.getFileSearchMatches();
            String searchTermPattern = searchTermResults.getSearchTerm();

            reportBuffer.append("\n");
            reportBuffer.append("\n   There were " + matchingFileList.size() + " matches for search term, '"
                    + searchTermPattern + "'.  Printing Now");

            for (FileSearchResult fileMatchResult : matchingFileList) {
                reportBuffer.append("\n         " + fileMatchResult.getMatchFileName() + " - "
                        + fileMatchResult.getMatchFileFullPath());
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

    public void printMoveFileResults(MoveFilesResults moveFilesResults, File reportDirectory) throws Exception {
        String reportSuffix = null;
        String reportName = null;
        StringBuffer reportBuffer = new StringBuffer();
        File reportFile = null;
        String reportNameDateFormat = "MM.dd.yyyy-HH.mm.ss";
        String printReportDateFormat = "MM/dd/yyyy HH:mm:ss";

        reportSuffix = DupFileUtility.getDateString(Calendar.getInstance().getTime(), reportNameDateFormat);

        reportName = "duplicate-file-report-" + reportSuffix + ".txt";

        reportBuffer.append("******Printing Results*****");
        reportBuffer.append("\n   Date Test Run Started = " + moveFilesResults.getDateTestRanStartedDisplayString());
        reportBuffer.append("\n   Date Test Run Ended = " + moveFilesResults.getDateTestRanEndedDisplayString());
        reportBuffer.append("\n   Test Duration = " + moveFilesResults.getTestDurationDisplayString());
        reportBuffer.append("\n   Directories Searched:");
        for (File directorySearched : moveFilesResults.getDirectoriesSearched()) {
            reportBuffer.append("\n      " + directorySearched.getAbsolutePath());
        }
        reportBuffer.append("\n   Directories NOT Searched:");
        for (File directoryNotSearched : moveFilesResults.getDirectoriesNotSearched()) {
            reportBuffer.append("\n      " + directoryNotSearched.getAbsolutePath());
        }
        reportBuffer.append("\n   Total # files processed = " + moveFilesResults.getNumFilesProcessed());
        reportBuffer.append("\n   Total File Size = " + moveFilesResults.getTotalFileSizeDisplayString());
        reportBuffer.append("\n   Total Number of Directories = " + moveFilesResults.getTotalNumDirectories());
        reportBuffer.append("\n   Number of Part Files = " + moveFilesResults.getNumPartFiles());
        reportBuffer.append("\n   Size of Part Files = " + moveFilesResults.getTotalPartFileSizeDisplayString());
        reportBuffer.append("\n   Number of Errors = " + moveFilesResults.getNumErrors());
        reportBuffer.append("\n");

        reportBuffer.append("\n");
        reportBuffer.append("\n******Printing " + moveFilesResults.getFilesMoved().size()
                + " Part files that were moved*****");

        for (File originalFile : moveFilesResults.getFilesMoved().keySet()) {
            File renameToFile = moveFilesResults.getFilesMoved().get(originalFile);
            reportBuffer.append("\n   From: " + originalFile.getAbsolutePath() + "      to: "
                    + renameToFile.getAbsolutePath());
        }

        reportBuffer.append("\n");
        reportBuffer.append("\n******Printing " + moveFilesResults.getFilesNotMoved().size()
                + " Part files that were NOT moved*****");

        for (File originalFile : moveFilesResults.getFilesNotMoved().keySet()) {
            File renameToFile = moveFilesResults.getFilesNotMoved().get(originalFile);
            reportBuffer.append("\n   From: " + originalFile.getAbsolutePath() + "      to: "
                    + renameToFile.getAbsolutePath());
        }

        if (moveFilesResults.getErrors().isEmpty()) {

        }
        else {
            reportBuffer.append("\n******Printing " + moveFilesResults.getErrors().size() + " Errors *****");

            for (String key : moveFilesResults.getErrors().keySet()) {
                String errorMessage = moveFilesResults.getErrors().get(key);
                reportBuffer.append("\n      " + key + "  - " + errorMessage);
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

    public static void testPreferences(String[] args) throws Exception {
        FileSearchPreferences fileSearchPreferences = null;

        FileSearchService fileSearchService = new FileSearchService();

        fileSearchPreferences = fileSearchService.retrieveFileSearchPreferences();

        System.out.println("Just pulled the preferences BEFORE save.  Here's values");
        System.out.println("  defaultTargetDupDirectory:  " + fileSearchPreferences.getDefaultMoveToDirectory());
        System.out
                .println("  # of default directories:  " + fileSearchPreferences.getDefaultSearchDirectories().size());

        fileSearchPreferences.setDefaultMoveToDirectory("C:\\git\\swingtech\\swing-tech");

        fileSearchPreferences.getDefaultSearchDirectoryNames().add("C:\\git\\swingtech\\swing-tech");
        fileSearchPreferences.getDefaultSearchDirectoryNames().add("C:\\git\\wdpro");

        fileSearchService.saveFileSearchPreferences(fileSearchPreferences);

        fileSearchPreferences = null;

        fileSearchPreferences = fileSearchService.retrieveFileSearchPreferences();

        System.out.println("Just pulled the preferences AFTER save.  Here's values");
        System.out.println("  defaultTargetDupDirectory:  " + fileSearchPreferences.getDefaultMoveToDirectory());
        System.out
                .println("  # of default directories:  " + fileSearchPreferences.getDefaultSearchDirectories().size());

    }

    public static void testMoveFiles() throws Exception {
        FileSearchService fileSearchService = null;
        String moveToDirector = "C:\\Users\\splas_000\\Google Drive\\speeches\\economic\\keyes\\capitalism\\youp\\new\\New folder\\later";
        List<String> moveFiles = new ArrayList<String>();
        Map<String, String> errors = new HashMap<String, String>();
        MoveFilesResults moveToFileResults = null;

        moveFiles
                .add("C:\\Users\\splas_000\\Google Drive\\speeches\\economic\\keyes\\capitalism\\youp\\new\\new21\\YouPorn - cute young girl for an older black man Pt 4 7.mp4");
        moveFiles
                .add("C:\\Users\\splas_000\\Google Drive\\speeches\\economic\\keyes\\capitalism\\youp\\new\\new21\\YouPorn - cute young girl for an older black man Pt 7 7.mp4");
        moveFiles
                .add("C:\\Users\\splas_000\\Google Drive\\speeches\\economic\\keyes\\capitalism\\youp\\new\\new21\\YouPorn - Kitty plays with a black guy.mp4");
        moveFiles
                .add("C:\\Users\\splas_000\\Google Drive\\speeches\\economic\\keyes\\capitalism\\youp\\new\\new21\\YouPorn - Maureen McCormick s Cousin.mp4");

        System.out.println("Moving " + moveFiles.size() + " to " + moveToDirector);

        fileSearchService = new FileSearchService();

        moveToFileResults = fileSearchService.moveFiles(moveFiles, moveToDirector);

        errors = moveToFileResults.getErrors();

        if (errors.isEmpty()) {
            System.out.println("No Errors.");
        }
        else {
            System.out.println("Printing " + errors.size() + "errors....");

            for (String key : errors.keySet()) {
                String errorMessage = errors.get(key);

                System.out.println("  " + key + " - " + errorMessage);
            }
        }
    }

    public static void testSearchFiles() throws Exception {
        FileSearchService fileSearchService = null;
        List<File> searchDirectoryFiles = null;
        File targetReportDirectory = null;
        List<String> matchStrings = null;
        FileSearchResults fileSearchResults = null;

        targetReportDirectory = new File("C:\\git\\swingtech\\swing-tech");
        // targetReportDirectory = new
        // File("C:\\Users\\splas_000\\Google Drive\\speeches\\economic\\keyes\\search");

        searchDirectoryFiles = new ArrayList<File>();
        // searchDirectoryFiles.add(new File("C:\\git\\swingtech\\swing-tech"));
        // searchDirectoryFiles.add(new File("C:\\git\\wdpro"));

        searchDirectoryFiles.add(new File("C:\\Users\\splas_000\\Google Drive\\speeches\\economic\\keyes\\capitalism"));
        searchDirectoryFiles.add(new File("C:\\Users\\splas_000\\Google Drive\\speeches\\economic\\marx"));

        matchStrings = new ArrayList<String>();
        matchStrings.add("~index~");
        matchStrings.add("~image~jpg");

        fileSearchService = new FileSearchService();

        fileSearchResults = fileSearchService.findSearchMatchesWithFiles(searchDirectoryFiles, matchStrings, false);

        fileSearchService.printSearchResults(fileSearchResults, targetReportDirectory);
    }

    public static void main(String[] args) throws Exception {
        testMoveFiles();
        // testSearchFiles();
        // testPreferences(args);
    }
}
