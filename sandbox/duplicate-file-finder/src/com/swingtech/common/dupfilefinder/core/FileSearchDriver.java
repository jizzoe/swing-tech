package com.swingtech.common.dupfilefinder.core;

import java.io.File;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.swingtech.common.dupfilefinder.model.FileSearchResults;
import com.swingtech.common.dupfilefinder.util.DupFileUtility;
import com.swingtech.common.dupfilefinder.util.Timer;

public class FileSearchDriver {
	public static void main(String[] args) throws Exception {
		FileSearchDriver dupFileDriver = null;
		List<File> searchDirectoryFiles = null;
		File targetReportDirectory = null;
		List<String> matchStrings = null;

		targetReportDirectory = new File("C:\\Users\\splas_000\\Google Drive\\speeches\\economic\\keyes\\search");

		searchDirectoryFiles = new ArrayList<File>();
		searchDirectoryFiles.add(new File("C:\\Users\\splas_000\\Google Drive\\speeches\\economic\\keyes\\capitalism"));
		searchDirectoryFiles.add(new File("C:\\Users\\splas_000\\Google Drive\\speeches\\economic\\marx"));
		
		matchStrings = new ArrayList<String>();
		matchStrings.add("[\\S]*keira[\\S]*");
		matchStrings.add("[\\S]*tammy[\\S]*");
		matchStrings.add("[\\S]*tammi[\\S]*");

		dupFileDriver = new FileSearchDriver();
		
		dupFileDriver.findSearchMatchesw(searchDirectoryFiles, targetReportDirectory, matchStrings);
	}
	
	public FileSearchResults findSearchMatchesw(List<File> searchDirectoryFiles, File targetReportDirectory, 
			List<String> matchStrings) throws Exception 
	{
		FileSearchResults fileSearchResults = null;
		Timer timer = new Timer();
		
		timer.startTiming();
		
		fileSearchResults = this.searchDirectoriesForMatches(searchDirectoryFiles, matchStrings);

		fileSearchResults.setDateTestRanStarted(timer.getStartTimeDate());
		
		timer.stopTiming();
		
		fileSearchResults.setDateTestRanEnded(timer.getEndTimeDate());
		fileSearchResults.setTimer(timer);
		
		this.printResults(fileSearchResults, targetReportDirectory);
		
		return fileSearchResults;
	}
	
	public FileSearchResults searchDirectoriesForMatches(List<File> searchDirectoryFiles, List<String> matchStrings) throws Exception {
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
	
	public void walkDirectoryForSearchResults(File searchDirectoryFile, FileSearchResults fileSearchResults, List<String> matchStrings) throws Exception {
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
		
		reportFile = new File(reportDirectory.getAbsolutePath() + "\\" + reportName);
		
		reportBuffer.append("******Printing Results*****");
		reportBuffer.append("\n   Date Test Run Started = " + DupFileUtility.getDateString(fileSearchResults.getDateTestRanStarted(), printReportDateFormat));
		reportBuffer.append("\n   Date Test Run Ended = " + DupFileUtility.getDateString(fileSearchResults.getDateTestRanEnded(), printReportDateFormat));
		reportBuffer.append("\n   Test Ran in = " + fileSearchResults.getTimer().getDurationString());
		reportBuffer.append("\n   Directories Searched:");
		for (File directorySearched : fileSearchResults.getDirectoriesSearched()) {
			reportBuffer.append("\n      " + directorySearched.getAbsolutePath());
		}
		reportBuffer.append("\n   Total # files processed = " + fileSearchResults.getNumFilesProcessed());
		reportBuffer.append("\n   Total File Size = " + fileSearchResults.getTotalFilesSize());
		reportBuffer.append("\n   Total Number of Directories = " + fileSearchResults.getTotalNumDirectories());
		reportBuffer.append("\n   Number of Part Files = " + fileSearchResults.getNumPartFiles());
		reportBuffer.append("\n   Size of Part Files = " + fileSearchResults.getTotalPartFileSize());
		reportBuffer.append("\n   Number of Errors = " + fileSearchResults.getNumErrors());
		reportBuffer.append("\n");
		
		reportBuffer.append("\n******Printing Search Results for " + fileSearchResults.getSearchResults().size() + " Search Terms *****");
		
		for (String searchTermPattern : fileSearchResults.getSearchResults().keySet()) {
			List<File> matchingFileList = fileSearchResults.getSearchResults().get(searchTermPattern);

			reportBuffer.append("\n");
			reportBuffer.append("\n   There were " + matchingFileList.size() + " matches for search term, '" + searchTermPattern + "'.  Printing Now");
			
			for (File file : matchingFileList) {
				reportBuffer.append("\n         " + file.getAbsolutePath());
			}
		}


		System.out.println("");
		System.out.println("Here's the report");
		System.out.println("");
		System.out.println(reportBuffer);
		
		DupFileUtility.writeToFile(reportFile, reportBuffer);
	}
	
}
