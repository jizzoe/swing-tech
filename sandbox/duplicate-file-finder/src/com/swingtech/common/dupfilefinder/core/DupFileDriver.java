package com.swingtech.common.dupfilefinder.core;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.swingtech.common.dupfilefinder.model.DupFile;
import com.swingtech.common.dupfilefinder.model.DupFileFinderResults;
import com.swingtech.common.dupfilefinder.util.DupFileUtility;

public class DupFileDriver {
	public static void main(String[] args) throws Exception {
//		tempTest();
		tempTest3();
		
		DupFileDriver dupFileDriver = null;
		List<File> searchDirectoryFiles = null;
		File targetDupDirectory = null;
		File targetPartDirectory = null;
		File targetReportDirectory = null;
		List<String> matchStrings = null;
		boolean moveDuplicates;
		boolean movePartFiles;

//		targetDupDirectory = new File("C:\\Users\\Ric\\Google Drive\\speeches\\economic\\keyes\\dups\\new\\dups");
//		targetPartDirectory = new File("C:\\Users\\Ric\\Google Drive\\speeches\\economic\\keyes\\dups\\new\\parts");
//		targetReportDirectory = new File("C:\\Users\\Ric\\Google Drive\\speeches\\economic\\keyes\\dups\\new");
//		
//		searchDirectoryFiles = new ArrayList<File>();
//		searchDirectoryFiles.add(new File("C:\\Users\\Ric\\Google Drive\\speeches\\economic\\keyes\\capitalism"));
//		searchDirectoryFiles.add(new File("C:\\Users\\Ric\\Google Drive\\speeches\\economic\\marx"));
//		
//		matchStrings = new ArrayList<String>();
//		matchStrings.add("\\rated\\");

		targetDupDirectory = new File("C:\\Users\\splas_000\\Google Drive\\speeches\\economic\\keyes\\dups\\new2\\dups");
		targetPartDirectory = new File("C:\\Users\\splas_000\\Google Drive\\speeches\\economic\\keyes\\dups\\new2\\parts");
		targetReportDirectory = new File("C:\\Users\\splas_000\\Google Drive\\speeches\\economic\\keyes\\dups\\new2");

		searchDirectoryFiles = new ArrayList<File>();
		searchDirectoryFiles.add(new File("C:\\Users\\splas_000\\Google Drive\\speeches\\economic\\keyes\\capitalism"));
		searchDirectoryFiles.add(new File("C:\\Users\\splas_000\\Google Drive\\speeches\\economic\\marx"));
		
		matchStrings = new ArrayList<String>();
		matchStrings.add("\\rated\\");
		matchStrings.add("\\projects\\");

		moveDuplicates = true;
		movePartFiles = true;
		
		dupFileDriver = new DupFileDriver();
		
		dupFileDriver.findAndMoveAllDuplicates(searchDirectoryFiles, targetDupDirectory, targetPartDirectory, targetReportDirectory, matchStrings, moveDuplicates, movePartFiles);
	}
	
	public DupFileFinderResults findAndMoveAllDuplicates(List<File> searchDirectoryFiles, File targetDupDirectory, 
			File targetPartDirectory, File targetReportDirectory, List<String> matchStrings, 
			boolean moveDuplicates, boolean movePartFiles) throws Exception 
	{
		DupFileFinderResults dupFileFinderResults = null;
		Date dateStarted = null;
		Date dateEnded = null;

		dateStarted = Calendar.getInstance().getTime();
		
		dupFileFinderResults = this.findDupFiles(searchDirectoryFiles);

		dupFileFinderResults.setDateTestRanStarted(dateStarted);

		if (movePartFiles) {
			this.movePartFiles(dupFileFinderResults, targetPartDirectory);
		}

		if (moveDuplicates) {
			this.moveDuplicates(dupFileFinderResults, targetDupDirectory, matchStrings);
		}
		
		dateEnded = Calendar.getInstance().getTime();
		dupFileFinderResults.setDateTestRanEnded(dateEnded);
		
		this.printResults(dupFileFinderResults, targetReportDirectory);
		
		return dupFileFinderResults;
	}
	
	public DupFileFinderResults findDupFiles(List<File> searchDirectoryFiles) throws Exception {
		DupFileFinderResults dupFileFinderResults = new DupFileFinderResults();
		
		if (searchDirectoryFiles == null) {
			throw new IllegalArgumentException("searchDirectoryFiles cannot be null");
		}
		
		for (File searchDirectoryFile : searchDirectoryFiles) {
			if (!searchDirectoryFile.exists()) {
				dupFileFinderResults.getDirectoriesNotSearched().add(searchDirectoryFile);
				continue;
			}
			this.walkDirectoryForDuplicates(searchDirectoryFile, dupFileFinderResults);
		}
		
		return dupFileFinderResults;
	}
	
	public void walkDirectoryForDuplicates(File searchDirectoryFile, DupFileFinderResults dupFileFinderResults) throws Exception {
		System.out.println(searchDirectoryFile.getAbsolutePath());
		Path resultPath = null;
		DupFileVisitor dupFileVisitor = new DupFileVisitor(dupFileFinderResults);
		
		dupFileFinderResults.getDirectoriesSearched().add(searchDirectoryFile);
		
		Path path = FileSystems.getDefault().getPath(searchDirectoryFile.getAbsolutePath());

		resultPath = Files.walkFileTree(path, dupFileVisitor);
	}
	
	private void moveDuplicates(DupFileFinderResults dupFileFinderResults, File targetDupDirectoryirectory, List<String> matchStrings) {
		File file = null;
		File renameTofile = null;
		DupFile dupFile = null;
		int indexOfDupFileToStay;
		
		if (!targetDupDirectoryirectory.exists()) {
			throw new IllegalArgumentException("dupDirectory, '" + targetDupDirectoryirectory.getAbsolutePath() + "', does not exist on the file system.");
		}
		
		for (String fileName : dupFileFinderResults.getDupFiles().keySet()) {
			String fileNameExtension = null;
			dupFile = dupFileFinderResults.getDupFiles().get(fileName);
			boolean renameSuccessful = false;
			
			// 1 of the duplicate files will stay right where it is.  All other dups will be moved.  Find the index of hte file to stay.
			indexOfDupFileToStay = this.findIndexOfDupFileToStay(dupFile.getDupFileLocations(), matchStrings);
			
			if (indexOfDupFileToStay < 0) {
				// if the indexOfDupFile to stay is -1, that means the file is in more than 1 director that matches one of the mathStrings, so don't move it.  
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
				} catch (Exception e) {
					renameSuccessful = false;
				}

				if (renameSuccessful) {
					dupFileFinderResults.getDuplicatesMoved().put(file, renameTofile);
				} else {
					dupFileFinderResults.getDuplicatesErrorTryingToMove().put(file, renameTofile);
				}
			}
		}
	}
	
	private void movePartFiles(DupFileFinderResults dupFileFinderResults, File targetPartDirectory) {
		File renameTofile = null;
		DupFile dupFile = null;
		
		if (!targetPartDirectory.exists()) {
			throw new IllegalArgumentException("partDirectory, '" + targetPartDirectory.getAbsolutePath() + "', does not exist on the file system.");
		}
		
		int i = -1;
		
		for (File file : dupFileFinderResults.getPartFiles()) {
			renameTofile = this.getRenameToFile(targetPartDirectory, file);
			boolean renameSuccessful = false;
			
			i++;

			try {
				renameSuccessful = file.renameTo(renameTofile);
//				renameSuccessful = true;
			} catch (Exception e) {
				renameSuccessful = false;
			}

			if (renameSuccessful) {
				dupFileFinderResults.getPartsMoved().put(file, renameTofile);
			} else {
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

		renameToFile = new File(targetDupDirectoryirectory.getAbsolutePath() + "\\" + fileNameWithoutExtension + "." + fileNameExtension);
		
		while(renameToFile.exists()) {
			index++;
			renameToFile = new File(targetDupDirectoryirectory.getAbsolutePath() + "\\" + fileNameWithoutExtension + "_Copy" + index + "." + fileNameExtension);
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
		
		// if the file is in more than 1 director that matches one of the mathStrings, then return -1, i.e. don't move it.  
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
		
		reportSuffix = DupFileUtility.getDateString(Calendar.getInstance().getTime() , reportNameDateFormat);
		
		reportName = "duplicate-file-report-" + reportSuffix + ".txt";
		
		reportFile = new File(reportDirectory.getAbsolutePath() + "\\" + reportName);
		
		reportBuffer.append("******Printing Results*****");
		reportBuffer.append("\n   Date Test Run Started = " + DupFileUtility.getDateString(dupFileFinderResults.getDateTestRanStarted(), printReportDateFormat));
		reportBuffer.append("\n   Date Test Run Ended = " + DupFileUtility.getDateString(dupFileFinderResults.getDateTestRanEnded(), printReportDateFormat));
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

		reportBuffer.append("\n******Printing " + dupFileFinderResults.getDupFilesThatCantBeMoved().size() + " Duplicates that could NOT be moved (because there was more than 1 file in a matching directory)*****");
		
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
		reportBuffer.append("\n******Printing " + dupFileFinderResults.getDuplicatesMoved().size() + " Duplicates that were moved*****");

		for (File originalFile : dupFileFinderResults.getDuplicatesMoved().keySet()) {
			File renameToFile = dupFileFinderResults.getDuplicatesMoved().get(originalFile);
			reportBuffer.append("\n   From: " + originalFile.getAbsolutePath() + "      to: " + renameToFile.getAbsolutePath());
		}

		reportBuffer.append("\n");
		reportBuffer.append("\n******Printing " + dupFileFinderResults.getDuplicatesNotMoved().size() + " Duplicates that did not move (because they are the original left behind)*****");
		
		for (File file : dupFileFinderResults.getDuplicatesNotMoved()) {
			reportBuffer.append("\n   " + file.getAbsolutePath());
		}

		reportBuffer.append("\n");
		reportBuffer.append("\n******Printing " + dupFileFinderResults.getDuplicatesErrorTryingToMove().size() + " Duplicates that had errors tryig to be moved*****");

		for (File originalFile : dupFileFinderResults.getDuplicatesErrorTryingToMove().keySet()) {
			File renameToFile = dupFileFinderResults.getDuplicatesErrorTryingToMove().get(originalFile);
			reportBuffer.append("\n   From: " + originalFile.getAbsolutePath() + "      to: " + renameToFile.getAbsolutePath());
		}

		reportBuffer.append("\n");
		reportBuffer.append("\n******Printing " + dupFileFinderResults.getPartsMoved().size() + " Part files that were moved*****");

		for (File originalFile : dupFileFinderResults.getPartsMoved().keySet()) {
			File renameToFile = dupFileFinderResults.getPartsMoved().get(originalFile);
			reportBuffer.append("\n   From: " + originalFile.getAbsolutePath() + "      to: " + renameToFile.getAbsolutePath());
		}

		reportBuffer.append("\n");
		reportBuffer.append("\n******Printing " + dupFileFinderResults.getPartsErrorTryingToMove().size() + " Part files that had errors trying to be moved*****");

		for (File originalFile : dupFileFinderResults.getPartsErrorTryingToMove().keySet()) {
			File renameToFile = dupFileFinderResults.getPartsErrorTryingToMove().get(originalFile);
			reportBuffer.append("\n   From: " + originalFile.getAbsolutePath() + "      to: " + renameToFile.getAbsolutePath());
		}

		System.out.println("");
		System.out.println("Here's the report");
		System.out.println("");
		System.out.println(reportBuffer);
		
		DupFileUtility.writeToFile(reportFile, reportBuffer);
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
			System.out.println("   fileName:          |" + DupFileUtility.getFileNameWithoutExtension(renameToFile) + "|");
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
}
