package com.swingtech.common.dupfilefinder.core;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;

import com.swingtech.common.dupfilefinder.model.FileSearchResults;
import com.swingtech.common.dupfilefinder.util.DupFileUtility;

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
		String fileName = null;
		String fullFileName = null;
		
		index++;
		
		if (file.isDirectory()) {
			this.processDirectory(file);
			return FileVisitResult.CONTINUE;
		}
		
		fileName = DupFileUtility.getFileNameWithoutExtension(file);//file.getName().substring(0, file.getName().indexOf("."));
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
			if (DupFileUtility.matchesPattern(fileName, searchTermPattern)) {
				this.processSearchMatch(searchTermPattern, file);
			}
		}
		
		return FileVisitResult.CONTINUE;
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
		List<File> matchingFileList = null;
		
		matchingFileList = fileSearchResults.getSearchResults().get(searchTermPattern);
		
		if (matchingFileList == null) {
			matchingFileList = new ArrayList<File>();
			fileSearchResults.getSearchResults().put(searchTermPattern, matchingFileList);
		}
		
		matchingFileList.add(matchFile);
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
