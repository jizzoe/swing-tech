package com.swingtech.apps.filemgmt.model;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DupFileFinderResults {
	private int numFilesProcessed;
	private int numErrors;
	private int numDup;
	private long totalFilesSize;
	private long dupFilesSize;
	private int numPartFiles;
	private long totalPartFileSize;
	private long totalNumDirectories;
	private Map<String, DupFile> dupFiles = new HashMap<String, DupFile>();
	private Map<String, File> allFilesNames = new HashMap<String, File>();
	private List<File>  directories= new ArrayList<File>() ;
	private List<File> partFiles = new ArrayList<File>() ;
	private Map<String, DupFile> dupFilesThatCantBeMoved = new HashMap<String, DupFile>();
	private int numOfDuplicatesMoved;
	private Map<File, File> duplicatesMoved = new HashMap<File, File>();
	private Map<File, File> duplicatesErrorTryingToMove = new HashMap<File, File>();
	private Map<File, File> partsMoved = new HashMap<File, File>();
	private Map<File, File> partsErrorTryingToMove = new HashMap<File, File>();
	private List<File> duplicatesNotMoved = new ArrayList<File>();
	private List<File> directoriesSearched = new ArrayList<File>();
	private List<File> directoriesNotSearched = new ArrayList<File>();
	private Date dateTestRanStarted = null;
	private Date dateTestRanEnded = null;
	
	public int getNumFilesProcessed() {
		return numFilesProcessed;
	}
	public void setNumFilesProcessed(int numFilesProcessed) {
		this.numFilesProcessed = numFilesProcessed;
	}
	public int getNumErrors() {
		return numErrors;
	}
	public void setNumErrors(int numErrors) {
		this.numErrors = numErrors;
	}
	public int getNumDup() {
		return numDup;
	}
	public void setNumDup(int numDup) {
		this.numDup = numDup;
	}
	public long getTotalFilesSize() {
		return totalFilesSize;
	}
	public void setTotalFilesSize(long totalFilesSize) {
		this.totalFilesSize = totalFilesSize;
	}
	public long getDupFilesSize() {
		return dupFilesSize;
	}
	public void setDupFilesSize(long dupFilesSize) {
		this.dupFilesSize = dupFilesSize;
	}
	public Map<String, DupFile> getDupFiles() {
		return dupFiles;
	}
	public void setDupFiles(Map<String, DupFile> dupFiles) {
		this.dupFiles = dupFiles;
	}
	public long getTotalNumDirectories() {
		return totalNumDirectories;
	}
	public void setTotalNumDirectories(long totalNumDirectories) {
		this.totalNumDirectories = totalNumDirectories;
	}
	public List<File> getDirectories() {
		return directories;
	}
	public void setDirectories(List<File> directories) {
		this.directories = directories;
	}
	public Map<String, File> getAllFilesNames() {
		return allFilesNames;
	}
	public void setAllFilesNames(Map<String, File> allFilesNames) {
		this.allFilesNames = allFilesNames;
	}
	public List<File> getPartFiles() {
		return partFiles;
	}
	public void setPartFiles(List<File> partFiles) {
		this.partFiles = partFiles;
	}
	public Map<String, DupFile> getDupFilesThatCantBeMoved() {
		return dupFilesThatCantBeMoved;
	}
	public void setDupFilesThatCantBeMoved(
			Map<String, DupFile> dupFilesThatCantBeMoved) {
		this.dupFilesThatCantBeMoved = dupFilesThatCantBeMoved;
	}
	public int getNumOfDuplicatesMoved() {
		return numOfDuplicatesMoved;
	}
	public void setNumOfDuplicatesMoved(int numOfDuplicatesMoved) {
		this.numOfDuplicatesMoved = numOfDuplicatesMoved;
	}
	public Map<File, File> getDuplicatesMoved() {
		return duplicatesMoved;
	}
	public void setDuplicatesMoved(Map<File, File> duplicatesMoved) {
		this.duplicatesMoved = duplicatesMoved;
	}
	public List<File> getDuplicatesNotMoved() {
		return duplicatesNotMoved;
	}
	public void setDuplicatesNotMoved(List<File> duplicatesNotMoved) {
		this.duplicatesNotMoved = duplicatesNotMoved;
	}
	public Map<File, File> getDuplicatesErrorTryingToMove() {
		return duplicatesErrorTryingToMove;
	}
	public void setDuplicatesErrorTryingToMove(
			Map<File, File> duplicatesErrorTryingToMove) {
		this.duplicatesErrorTryingToMove = duplicatesErrorTryingToMove;
	}
	public Map<File, File> getPartsMoved() {
		return partsMoved;
	}
	public void setPartsMoved(Map<File, File> partsMoved) {
		this.partsMoved = partsMoved;
	}
	public Map<File, File> getPartsErrorTryingToMove() {
		return partsErrorTryingToMove;
	}
	public void setPartsErrorTryingToMove(Map<File, File> partsErrorTryingToMove) {
		this.partsErrorTryingToMove = partsErrorTryingToMove;
	}
	public List<File> getDirectoriesSearched() {
		return directoriesSearched;
	}
	public void setDirectoriesSearched(List<File> directoriesSearched) {
		this.directoriesSearched = directoriesSearched;
	}
	public Date getDateTestRanStarted() {
		return dateTestRanStarted;
	}
	public void setDateTestRanStarted(Date dateTestRanStarted) {
		this.dateTestRanStarted = dateTestRanStarted;
	}
	public Date getDateTestRanEnded() {
		return dateTestRanEnded;
	}
	public void setDateTestRanEnded(Date dateTestRanEnded) {
		this.dateTestRanEnded = dateTestRanEnded;
	}
	public int getNumPartFiles() {
		return numPartFiles;
	}
	public void setNumPartFiles(int numPartFiles) {
		this.numPartFiles = numPartFiles;
	}
	public long getTotalPartFileSize() {
		return totalPartFileSize;
	}
	public void setTotalPartFileSize(long totalPartFileSize) {
		this.totalPartFileSize = totalPartFileSize;
	}
	public List<File> getDirectoriesNotSearched() {
		return directoriesNotSearched;
	}
	public void setDirectoriesNotSearched(List<File> directoriesNotSearched) {
		this.directoriesNotSearched = directoriesNotSearched;
	}
}
