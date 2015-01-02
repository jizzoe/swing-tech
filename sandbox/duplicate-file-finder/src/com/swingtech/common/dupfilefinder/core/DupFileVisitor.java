package com.swingtech.common.dupfilefinder.core;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;

import com.swingtech.common.dupfilefinder.model.DupFile;
import com.swingtech.common.dupfilefinder.model.DupFileFinderResults;
import com.swingtech.common.dupfilefinder.util.DupFileUtility;

public class DupFileVisitor implements FileVisitor<Path> {
	private final DupFileFinderResults dupFileFinderResults;
	private int index = 0;

	public DupFileVisitor(final DupFileFinderResults dupFileFinderResults) {
		super();
		this.dupFileFinderResults = dupFileFinderResults;
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

		dupFileFinderResults.setTotalFilesSize(dupFileFinderResults.getTotalFilesSize() + file.getTotalSpace());
		dupFileFinderResults.setNumFilesProcessed(dupFileFinderResults.getNumFilesProcessed() + 1);

		if (index % 10 == 0) {
			System.out.println("Processing row " + index);
		}

		if (fullFileName.endsWith(".part")) {
			this.processPartFile(file);
		}
		else if (dupFileFinderResults.getAllFilesNames().containsKey(fileName)) {
			this.processDupFile(file, fileName, fullFileName);
		} else {
			this.processNonDupFile(file, fileName);
		}
		
		return FileVisitResult.CONTINUE;
	}
	
	public void processPartFile(File file) {
		dupFileFinderResults.getPartFiles().add(file);
		dupFileFinderResults.setTotalPartFileSize(dupFileFinderResults.getTotalPartFileSize() + file.getTotalSpace());
		dupFileFinderResults.setNumPartFiles(dupFileFinderResults.getNumPartFiles() + 1);
	}
	
	public void processDirectory(File file) {
		System.out.println("   directory:  " + file.getAbsolutePath());
		dupFileFinderResults.setTotalNumDirectories(dupFileFinderResults.getTotalNumDirectories() + 1);
		dupFileFinderResults.getDirectories().add(file);
	}
	
	public void processNonDupFile(File file, String fileName) {
		dupFileFinderResults.getAllFilesNames().put(fileName, file);
	}
	
	public void processDupFile(File file, String fileName, String fullFileName) {
		DupFile dupFile = null;
		
		dupFileFinderResults.setNumDup(dupFileFinderResults.getNumDup() + 1);
		dupFileFinderResults.setDupFilesSize(dupFileFinderResults.getDupFilesSize() + file.getTotalSpace());
		
		if (dupFileFinderResults.getDupFiles().containsKey(fileName)) {
			dupFile = dupFileFinderResults.getDupFiles().get(fileName);
		} else {
			dupFile = new DupFile();
			dupFileFinderResults.getDupFiles().put(fileName, dupFile);

			dupFile.setDupFileName(fileName);
			
			// If this is the first time coming across a dup, then find the previous file and add that one too.
			File previousDupFile = dupFileFinderResults.getAllFilesNames().get(fileName);
			dupFile.getDupFileLocations().add(previousDupFile);
			dupFile.setNumDups(dupFile.getNumDups() + 1);
			dupFile.setTotalDupFileSize(dupFile.getTotalDupFileSize() + file.getTotalSpace());
		}
		
		dupFile.setNumDups(dupFile.getNumDups() + 1);
		dupFile.getDupFileLocations().add(file);
		dupFile.setTotalDupFileSize(dupFile.getTotalDupFileSize() + file.getTotalSpace());
	}
	

	@Override
	public FileVisitResult visitFileFailed(Path file, IOException exc)
			throws IOException {
		
		dupFileFinderResults.setNumErrors(dupFileFinderResults.getNumErrors() + 1);
		
		return FileVisitResult.CONTINUE;
	}

}
