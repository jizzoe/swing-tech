/*
 * SwingTech Software - http://swing-tech.com/
 * 
 * Copyright (C) 2015 Joe Rice All rights reserved.
 * 
 * SwingTech Software is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by the Free
 * Software Foundation; either version 3 of the License, or (at your option) any
 * later version.
 * 
 * SwingTech Software is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * SwingTech Software; If not, see <http://www.gnu.org/licenses/>.
 */
package com.swingtech.common.dupfilefinder.model;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.swingtech.common.dupfilefinder.util.Timer;

/**
 * @DOCME
 *
 * @author splas_000
 *
 */
public class FileSearchResults {
	private int numFilesProcessed;
	private int numFilesMatchSearch;
	private int numErrors;
	private long totalFilesSize;
	private long totalNumDirectories;
	private int numPartFiles;
	private long totalPartFileSize;
	private Date dateTestRanStarted = null;
	private Date dateTestRanEnded = null;
	private Timer timer = null;
	private Map<String, List<File>> searchResults = new HashMap<String, List<File>>();
	private List<File> directoriesSearched = new ArrayList<File>();
	private List<File> directoriesNotSearched = new ArrayList<File>();
	
	/**
	 * @return the numPartFiles
	 */
	public int getNumPartFiles() {
		return numPartFiles;
	}
	/**
	 * @param numPartFiles the numPartFiles to set
	 */
	public void setNumPartFiles(int numPartFiles) {
		this.numPartFiles = numPartFiles;
	}
	/**
	 * @return the totalPartFileSize
	 */
	public long getTotalPartFileSize() {
		return totalPartFileSize;
	}
	/**
	 * @param totalPartFileSize the totalPartFileSize to set
	 */
	public void setTotalPartFileSize(long totalPartFileSize) {
		this.totalPartFileSize = totalPartFileSize;
	}
	/**
	 * @return the totalNumDirectories
	 */
	public long getTotalNumDirectories() {
		return totalNumDirectories;
	}
	/**
	 * @param totalNumDirectories the totalNumDirectories to set
	 */
	public void setTotalNumDirectories(long totalNumDirectories) {
		this.totalNumDirectories = totalNumDirectories;
	}
	private List<File> fileSearchMatches = new ArrayList<File>();
	
	/**
	 * @return the numFilesProcessed
	 */
	public int getNumFilesProcessed() {
		return numFilesProcessed;
	}
	/**
	 * @param numFilesProcessed the numFilesProcessed to set
	 */
	public void setNumFilesProcessed(int numFilesProcessed) {
		this.numFilesProcessed = numFilesProcessed;
	}
	/**
	 * @return the numFilesMatchSearch
	 */
	public int getNumFilesMatchSearch() {
		return numFilesMatchSearch;
	}
	/**
	 * @param numFilesMatchSearch the numFilesMatchSearch to set
	 */
	public void setNumFilesMatchSearch(int numFilesMatchSearch) {
		this.numFilesMatchSearch = numFilesMatchSearch;
	}
	/**
	 * @return the numErrors
	 */
	public int getNumErrors() {
		return numErrors;
	}
	/**
	 * @param numErrors the numErrors to set
	 */
	public void setNumErrors(int numErrors) {
		this.numErrors = numErrors;
	}
	/**
	 * @return the totalFilesSize
	 */
	public long getTotalFilesSize() {
		return totalFilesSize;
	}
	/**
	 * @param totalFilesSize the totalFilesSize to set
	 */
	public void setTotalFilesSize(long totalFilesSize) {
		this.totalFilesSize = totalFilesSize;
	}
	/**
	 * @return the dateTestRanStarted
	 */
	public Date getDateTestRanStarted() {
		return dateTestRanStarted;
	}
	/**
	 * @param dateTestRanStarted the dateTestRanStarted to set
	 */
	public void setDateTestRanStarted(Date dateTestRanStarted) {
		this.dateTestRanStarted = dateTestRanStarted;
	}
	/**
	 * @return the dateTestRanEnded
	 */
	public Date getDateTestRanEnded() {
		return dateTestRanEnded;
	}
	/**
	 * @param dateTestRanEnded the dateTestRanEnded to set
	 */
	public void setDateTestRanEnded(Date dateTestRanEnded) {
		this.dateTestRanEnded = dateTestRanEnded;
	}
	/**
	 * @return the fileSearchMatches
	 */
	public List<File> getFileSearchMatches() {
		return fileSearchMatches;
	}
	/**
	 * @param fileSearchMatches the fileSearchMatches to set
	 */
	public void setFileSearchMatches(List<File> fileSearchMatches) {
		this.fileSearchMatches = fileSearchMatches;
	}
	/**
	 * @return the searchResults
	 */
	public Map<String, List<File>> getSearchResults() {
		return searchResults;
	}
	/**
	 * @param searchResults the searchResults to set
	 */
	public void setSearchResults(Map<String, List<File>> searchResults) {
		this.searchResults = searchResults;
	}
	/**
	 * @return the directoriesNotSearched
	 */
	public List<File> getDirectoriesNotSearched() {
		return directoriesNotSearched;
	}
	/**
	 * @param directoriesNotSearched the directoriesNotSearched to set
	 */
	public void setDirectoriesNotSearched(List<File> directoriesNotSearched) {
		this.directoriesNotSearched = directoriesNotSearched;
	}
	/**
	 * @return the directoriesSearched
	 */
	public List<File> getDirectoriesSearched() {
		return directoriesSearched;
	}
	/**
	 * @param directoriesSearched the directoriesSearched to set
	 */
	public void setDirectoriesSearched(List<File> directoriesSearched) {
		this.directoriesSearched = directoriesSearched;
	}
	/**
	 * @return the timer
	 */
	public Timer getTimer() {
		return timer;
	}
	/**
	 * @param timer the timer to set
	 */
	public void setTimer(Timer timer) {
		this.timer = timer;
	}
}
