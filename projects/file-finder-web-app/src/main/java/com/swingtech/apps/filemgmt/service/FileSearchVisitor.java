package com.swingtech.apps.filemgmt.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;

import com.swingtech.apps.filemgmt.model.FileLocationEntity;
import com.swingtech.apps.filemgmt.model.FileSearchResult;
import com.swingtech.apps.filemgmt.model.FileSearchResults;
import com.swingtech.apps.filemgmt.model.SearchTermResults;
import com.swingtech.apps.filemgmt.util.DupFileUtility;

public class FileSearchVisitor implements FileVisitor<Path> {
    private final FileSearchResults fileSearchResults;
    private int index = 0;
    private List<String> searchTermPatterns = null;

    public FileSearchVisitor(final FileSearchResults fileSearchResults, List<String> searchTermPatterns) {
        super();
        this.fileSearchResults = fileSearchResults;
        this.searchTermPatterns = searchTermPatterns;
    }

    @Override
    public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
        // TODO Auto-generated method stub
        return FileVisitResult.CONTINUE;
    }

    @Override
    public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
        // TODO Auto-generated method stub
        return FileVisitResult.CONTINUE;
    }

    @Override
    public FileVisitResult visitFile(Path path, BasicFileAttributes attrs) throws IOException {
        FileLocationEntity fileLocationEntity = new FileLocationEntity();
        File file = path.toFile();

        fileLocationEntity.setFile(file);

        return this.visitFile(fileLocationEntity);
    }

    public FileVisitResult visitFile(FileLocationEntity fileLocationEntity) throws IOException {
        String fullFileName = null;
        File file = null;

        file = fileLocationEntity.getFile();

        index++;

        if (file.isDirectory()) {
            this.processDirectory(file);
            return FileVisitResult.CONTINUE;
        }

        fullFileName = file.getAbsolutePath();

        fileSearchResults.setTotalFilesSize(fileSearchResults.getTotalFilesSize() + fileLocationEntity.getFileSize());
        fileSearchResults.setNumFilesProcessed(fileSearchResults.getNumFilesProcessed() + 1);

        if (index % 1000 == 0) {
            System.out.println("Processing row " + index);
        }

        if (fullFileName.endsWith(".part")) {
            this.processPartFile(fileLocationEntity);
        }

        for (String searchTermPattern : searchTermPatterns) {
            if (searchTermPattern == null || searchTermPattern.trim().isEmpty()) {
                continue;
            }

            if (DupFileUtility.fileMatchesPattern(file, searchTermPattern, false)) {
                this.processSearchMatch(searchTermPattern, file);
            }
        }

        return FileVisitResult.CONTINUE;
    }

    @Override
    public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {

        fileSearchResults.setNumErrors(fileSearchResults.getNumErrors() + 1);

        return FileVisitResult.CONTINUE;
    }

    public void processDirectory(File file) {
        // System.out.println("   directory:  " + file.getAbsolutePath());
        fileSearchResults.setTotalNumDirectories(fileSearchResults.getTotalNumDirectories() + 1);
    }

    public void processPartFile(FileLocationEntity fileLocationEntity) {
        fileSearchResults.setTotalPartFileSize(fileSearchResults.getTotalPartFileSize()
                + fileLocationEntity.getFileSize());
        fileSearchResults.setNumPartFiles(fileSearchResults.getNumPartFiles() + 1);
    }

    private void processSearchMatch(String searchTermPattern, File matchFile) {
        List<FileSearchResult> matchingFileList = null;
        SearchTermResults matchingSearchTerm = null;
        FileSearchResult fileSearchResult = null;
        int searchTermIndex = -1;
        SearchTermResults searchSearchTermResults = null;

        // First find an existing Search Term if one already exists
        searchSearchTermResults = new SearchTermResults();
        searchSearchTermResults.setSearchTerm(searchTermPattern);
        searchTermIndex = fileSearchResults.getSearchTermResults().indexOf(searchSearchTermResults);

        if (searchTermIndex < 0) {
            matchingSearchTerm = new SearchTermResults();
            matchingFileList = new ArrayList<FileSearchResult>();
            matchingSearchTerm.setFileSearchMatches(matchingFileList);
            matchingSearchTerm.setSearchTerm(searchTermPattern);
            fileSearchResults.getSearchTermResults().add(matchingSearchTerm);
        }
        else {
            matchingSearchTerm = fileSearchResults.getSearchTermResults().get(searchTermIndex);
            matchingFileList = matchingSearchTerm.getFileSearchMatches();
        }

        fileSearchResult = this.getFileSearchResultFromFile(matchFile);

        matchingFileList.add(fileSearchResult);

        fileSearchResults.setNumFilesMatchSearch(fileSearchResults.getNumFilesMatchSearch() + 1);
    }

    private FileSearchResult getFileSearchResultFromFile(File matchFile) {
        FileSearchResult fileSearchResult = new FileSearchResult();

        fileSearchResult.setMatchFile(matchFile);

        return fileSearchResult;
    }

    /**
     * @return the fileMatchStrings
     */
    public List<String> getFileMatchStrings() {
        return searchTermPatterns;
    }

    /**
     * @param fileMatchStrings
     *            the fileMatchStrings to set
     */
    public void setFileMatchStrings(List<String> fileMatchStrings) {
        this.searchTermPatterns = fileMatchStrings;
    }
}
