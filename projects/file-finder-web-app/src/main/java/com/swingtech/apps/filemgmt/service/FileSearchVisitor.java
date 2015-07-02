package com.swingtech.apps.filemgmt.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;

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
	public FileVisitResult postVisitDirectory(Path dir, IOException exc)
			throws IOException {
		// TODO Auto-generated method stub
		return FileVisitResult.CONTINUE;
	}

	@Override
	public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs)
			throws IOException {
		// TODO Auto-generated method stub
		return FileVisitResult.CONTINUE;
	}

	@Override
	public FileVisitResult visitFile(Path path, BasicFileAttributes attrs)
			throws IOException {
		File file = path.toFile();
		String fullFileName = null;
		
		index++;
		
		if (file.isDirectory()) {
			this.processDirectory(file);
			return FileVisitResult.CONTINUE;
		}
		
		fullFileName = file.getAbsolutePath();

		fileSearchResults.setTotalFilesSize(fileSearchResults.getTotalFilesSize() + file.getTotalSpace());
		fileSearchResults.setNumFilesProcessed(fileSearchResults.getNumFilesProcessed() + 1);

		if (index % 10 == 0) {
			System.out.println("Processing row " + index);
		}

		if (fullFileName.endsWith(".part")) {
			this.processPartFile(file);
		}

		for (String searchTermPattern : searchTermPatterns) {
			if (searchTermPattern == null || searchTermPattern.trim().isEmpty()) {
				continue;
			}
			
			if (this.fileMatchesPattern(file, searchTermPattern)) {
				this.processSearchMatch(searchTermPattern, file);
			}
		}
		
		return FileVisitResult.CONTINUE;
	}
	
	private boolean fileMatchesPattern(File file, String searchTermPattern) {
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
			modifiedSearchTermPattern = modifiedSearchTermPattern.replace("~", "[\\S]*");
		} else {
			modifiedSearchTermPattern = "[\\S]*" + modifiedSearchTermPattern + "[\\S]*";
		}
		
		matches = DupFileUtility.matchesPattern(fileName, modifiedSearchTermPattern);
		
		return matches;
	}
	
	@Override
	public FileVisitResult visitFileFailed(Path file, IOException exc)
			throws IOException {
		
		fileSearchResults.setNumErrors(fileSearchResults.getNumErrors() + 1);
		
		return FileVisitResult.CONTINUE;
	}

	public void processDirectory(File file) {
		System.out.println("   directory:  " + file.getAbsolutePath());
		fileSearchResults.setTotalNumDirectories(fileSearchResults.getTotalNumDirectories() + 1);
	}

	public void processPartFile(File file) {
		fileSearchResults.setTotalPartFileSize(fileSearchResults.getTotalPartFileSize() + file.getTotalSpace());
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
		} else {
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
		fileSearchResult.setMatchFileFullPath(matchFile.getAbsolutePath());
		fileSearchResult.setMatchFileName(DupFileUtility.getFileName(matchFile));
		fileSearchResult.setMatchFileNameWithoutExtension(DupFileUtility.getFileNameWithoutExtension(matchFile));
		
		return fileSearchResult;
	}

	/**
	 * @return the fileMatchStrings
	 */
	public List<String> getFileMatchStrings() {
		return searchTermPatterns;
	}

	/**
	 * @param fileMatchStrings the fileMatchStrings to set
	 */
	public void setFileMatchStrings(List<String> fileMatchStrings) {
		this.searchTermPatterns = fileMatchStrings;
	}
}
