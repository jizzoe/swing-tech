package com.swingtech.apps.filemgmt.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.swingtech.apps.filemgmt.model.FileSearchPreferences;
import com.swingtech.apps.filemgmt.model.FileSearchPreferencesDao;
import com.swingtech.apps.filemgmt.model.FileSearchResult;
import com.swingtech.apps.filemgmt.model.FileSearchResults;
import com.swingtech.apps.filemgmt.model.SearchTermResults;
import com.swingtech.apps.filemgmt.util.DupFileUtility;
import com.swingtech.apps.filemgmt.util.Timer;

public class FileSearchService {
	FileSearchPreferencesDao fileSearchPreferencesDao = new FileSearchPreferencesDao();

	public FileSearchPreferences retrieveFileSearchPreferences() {
		try {
			return fileSearchPreferencesDao.retrieveFileSearchPreferences();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	public void saveFileSearchPreferences(FileSearchPreferences fileSearchPreferences) {
		try {
			fileSearchPreferencesDao.saveFileSearchPreferences(fileSearchPreferences);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	public FileSearchResults findSearchMatchesWithStrings(List<String> searchDirectoryFileNames,
			List<String> matchStrings) throws Exception 
	{
		List<File> searchDirectoryFiles = new ArrayList<File>();
		File searchDirectoryFile = null;
		
		for (String searchDirectoryFileName : searchDirectoryFileNames) {
			searchDirectoryFile = new File(searchDirectoryFileName);
			searchDirectoryFiles.add(searchDirectoryFile);
		}
		
		return this.findSearchMatchesWithFiles(searchDirectoryFiles, matchStrings);
	}

	public FileSearchResults findSearchMatchesWithFiles(List<File> searchDirectoryFiles,
			List<String> matchStrings) throws Exception 
	{
		FileSearchResults fileSearchResults = null;
		Timer timer = new Timer();
		
		timer.startTiming();
		
		fileSearchResults = this.searchDirectoriesForMatches(searchDirectoryFiles, matchStrings);

		timer.stopTiming();

		fileSearchResults.setSearchTermsSearched(matchStrings);
		fileSearchResults.setTimer(timer);
		
		return fileSearchResults;
	}
	
	private FileSearchResults searchDirectoriesForMatches(List<File> searchDirectoryFiles, List<String> matchStrings) throws Exception {
		FileSearchResults fileSearchResults = new FileSearchResults();
		
		if (searchDirectoryFiles == null) {
			throw new IllegalArgumentException("searchDirectoryFiles cannot be null");
		}
		
		for (File searchDirectoryFile : searchDirectoryFiles) {
			if (!searchDirectoryFile.exists()) {
				fileSearchResults.getDirectoriesNotSearched().add(searchDirectoryFile);
				continue;
			}
			this.walkDirectoryForSearchResults(searchDirectoryFile, fileSearchResults, matchStrings);
		}
		
		return fileSearchResults;
	}
	
	private void walkDirectoryForSearchResults(File searchDirectoryFile, FileSearchResults fileSearchResults, List<String> matchStrings) throws Exception {
		System.out.println(searchDirectoryFile.getAbsolutePath());
		Path resultPath = null;
		FileSearchVisitor fileSearchVisitor = new FileSearchVisitor(fileSearchResults, matchStrings);
		
		fileSearchResults.getDirectoriesSearched().add(searchDirectoryFile);
		
		Path path = FileSystems.getDefault().getPath(searchDirectoryFile.getAbsolutePath());

		resultPath = Files.walkFileTree(path, fileSearchVisitor);
	}
	
	public void printResults(FileSearchResults fileSearchResults, File reportDirectory) throws Exception {
		String reportSuffix = null;
		String reportName = null;
		StringBuffer reportBuffer = new StringBuffer();
		File reportFile = null;
		String reportNameDateFormat = "MM.dd.yyyy-HH.mm.ss";
		String printReportDateFormat = "MM/dd/yyyy HH:mm:ss";
		
		reportSuffix = DupFileUtility.getDateString(Calendar.getInstance().getTime() , reportNameDateFormat);
		
		reportName = "duplicate-file-report-" + reportSuffix + ".txt";
		
		reportBuffer.append("******Printing Results*****");
		reportBuffer.append("\n   Date Test Run Started = " + fileSearchResults.getDateTestRanStartedDisplayString());
		reportBuffer.append("\n   Date Test Run Ended = " + fileSearchResults.getDateTestRanEndedDisplayString());
		reportBuffer.append("\n   Test Ran in = " + fileSearchResults.getTestDurationDisplayString());
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
		reportBuffer.append("\n   Total File Size = " + fileSearchResults.getTotalFilesSize());
		reportBuffer.append("\n   Total Number of Directories = " + fileSearchResults.getTotalNumDirectories());
		reportBuffer.append("\n   Number of Part Files = " + fileSearchResults.getNumPartFiles());
		reportBuffer.append("\n   Size of Part Files = " + fileSearchResults.getTotalPartFileSize());
		reportBuffer.append("\n   Number of Errors = " + fileSearchResults.getNumErrors());
		reportBuffer.append("\n");
		
		reportBuffer.append("\n******Printing Search Results for " + fileSearchResults.getSearchTermResults().size() + " Search Terms *****");
		
		for (SearchTermResults searchTermResults : fileSearchResults.getSearchTermResults()) {
			List<FileSearchResult> matchingFileList = searchTermResults.getFileSearchMatches();
			String searchTermPattern = searchTermResults.getSearchTerm();

			reportBuffer.append("\n");
			reportBuffer.append("\n   There were " + matchingFileList.size() + " matches for search term, '" + searchTermPattern + "'.  Printing Now");
			
			for (FileSearchResult fileMatchResult : matchingFileList) {
				reportBuffer.append("\n         " + fileMatchResult.getMatchFileName() + " - " + fileMatchResult.getMatchFileFullPath());
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
		System.out.println("  defaultMoveToDirectory:  " + fileSearchPreferences.getDefaultMoveToDirectory());
		System.out.println("  # of default directories:  " + fileSearchPreferences.getDefaultSearchDirectories().size());
		
		fileSearchPreferences.setDefaultMoveToDirectory("C:\\git\\swingtech\\swing-tech");
		
		fileSearchPreferences.getDefaultSearchDirectoryNames().add("C:\\git\\swingtech\\swing-tech");
		fileSearchPreferences.getDefaultSearchDirectoryNames().add("C:\\git\\wdpro");
		
		fileSearchService.saveFileSearchPreferences(fileSearchPreferences);
		
		fileSearchPreferences = null;
		
		fileSearchPreferences = fileSearchService.retrieveFileSearchPreferences();
		
		System.out.println("Just pulled the preferences AFTER save.  Here's values");
		System.out.println("  defaultMoveToDirectory:  " + fileSearchPreferences.getDefaultMoveToDirectory());
		System.out.println("  # of default directories:  " + fileSearchPreferences.getDefaultSearchDirectories().size());
		
	}
	
	public static void testService(String[] args) throws Exception {
		FileSearchService fileSearchService = null;
		List<File> searchDirectoryFiles = null;
		File targetReportDirectory = null;
		List<String> matchStrings = null;
		FileSearchResults fileSearchResults = null;

		targetReportDirectory = new File("C:\\git\\swingtech\\swing-tech");
//		targetReportDirectory = new File("C:\\Users\\splas_000\\Google Drive\\speeches\\economic\\keyes\\search");

		searchDirectoryFiles = new ArrayList<File>();
		searchDirectoryFiles.add(new File("C:\\git\\swingtech\\swing-tech"));
		searchDirectoryFiles.add(new File("C:\\git\\wdpro"));
		
//		searchDirectoryFiles.add(new File("C:\\Users\\splas_000\\Google Drive\\speeches\\economic\\keyes\\capitalism"));
//		searchDirectoryFiles.add(new File("C:\\Users\\splas_000\\Google Drive\\speeches\\economic\\marx"));
		
		matchStrings = new ArrayList<String>();
		matchStrings.add("~index~");
		matchStrings.add("~image~jpg");

		fileSearchService = new FileSearchService();
		
		fileSearchResults = fileSearchService.findSearchMatchesWithFiles(searchDirectoryFiles, matchStrings);
		
		fileSearchService.printResults(fileSearchResults, targetReportDirectory);
	}
	
}
