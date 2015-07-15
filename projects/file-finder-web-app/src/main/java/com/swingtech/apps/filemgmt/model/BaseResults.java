/*
 * SwingTech Software - http://swing-tech.com/
 * 
 * Copyright (C) 2015 Joe Rice All rights reserved.
 * 
 * SwingTech Software is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the Free
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
package com.swingtech.apps.filemgmt.model;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.swingtech.apps.filemgmt.util.DupFileUtility;
import com.swingtech.apps.filemgmt.util.Timer;

/**
 * @DOCME
 *
 * @author splas_000
 *
 */
public class BaseResults {
    private int numFilesProcessed;
    private int numErrors;
    private long totalFilesSize;
    private long totalNumDirectories;
    private int numPartFiles;
    private long totalPartFileSize;
    private Date dateTestRanStarted = null;
    private Date dateTestRanEnded = null;
    private String dateTestRanStartedDisplayString = null;
    private String dateTestRanEndedDisplayString = null;
    private String testDurationDisplayString = null;
    private Timer timer = null;
    private List<File> directories = new ArrayList<File>();
    private List<File> directoriesSearched = new ArrayList<File>();
    private List<File> directoriesNotSearched = new ArrayList<File>();
    private List<String> errorMessages = null;

    /**
     * @param timer
     *            the timer to set
     */
    public void setTimer(Timer timer) {
        String printReportDateFormat = "MM/dd/yyyy HH:mm:ss";
        this.timer = timer;
        this.dateTestRanEnded = timer.getEndTimeDate();
        this.dateTestRanStarted = timer.getStartTimeDate();
        this.dateTestRanEndedDisplayString = DupFileUtility.getDateString(this.dateTestRanEnded, printReportDateFormat);
        this.dateTestRanStartedDisplayString = DupFileUtility.getDateString(this.dateTestRanStarted,
                printReportDateFormat);
        this.testDurationDisplayString = timer.getDurationString();
    }

    /**
     * @return the numFilesProcessed
     */
    public int getNumFilesProcessed() {
        return numFilesProcessed;
    }

    /**
     * @param numFilesProcessed
     *            the numFilesProcessed to set
     */
    public void setNumFilesProcessed(int numFilesProcessed) {
        this.numFilesProcessed = numFilesProcessed;
    }

    /**
     * @return the numErrors
     */
    public int getNumErrors() {
        return numErrors;
    }

    /**
     * @param numErrors
     *            the numErrors to set
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
     * @param totalFilesSize
     *            the totalFilesSize to set
     */
    public void setTotalFilesSize(long totalFilesSize) {
        this.totalFilesSize = totalFilesSize;
    }

    /**
     * @return the totalNumDirectories
     */
    public long getTotalNumDirectories() {
        return totalNumDirectories;
    }

    /**
     * @param totalNumDirectories
     *            the totalNumDirectories to set
     */
    public void setTotalNumDirectories(long totalNumDirectories) {
        this.totalNumDirectories = totalNumDirectories;
    }

    /**
     * @return the numPartFiles
     */
    public int getNumPartFiles() {
        return numPartFiles;
    }

    /**
     * @param numPartFiles
     *            the numPartFiles to set
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
     * @param totalPartFileSize
     *            the totalPartFileSize to set
     */
    public void setTotalPartFileSize(long totalPartFileSize) {
        this.totalPartFileSize = totalPartFileSize;
    }

    /**
     * @return the dateTestRanStarted
     */
    public Date getDateTestRanStarted() {
        return dateTestRanStarted;
    }

    /**
     * @param dateTestRanStarted
     *            the dateTestRanStarted to set
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
     * @param dateTestRanEnded
     *            the dateTestRanEnded to set
     */
    public void setDateTestRanEnded(Date dateTestRanEnded) {
        this.dateTestRanEnded = dateTestRanEnded;
    }

    /**
     * @return the dateTestRanStartedDisplayString
     */
    public String getDateTestRanStartedDisplayString() {
        return dateTestRanStartedDisplayString;
    }

    /**
     * @param dateTestRanStartedDisplayString
     *            the dateTestRanStartedDisplayString to set
     */
    public void setDateTestRanStartedDisplayString(String dateTestRanStartedDisplayString) {
        this.dateTestRanStartedDisplayString = dateTestRanStartedDisplayString;
    }

    /**
     * @return the dateTestRanEndedDisplayString
     */
    public String getDateTestRanEndedDisplayString() {
        return dateTestRanEndedDisplayString;
    }

    /**
     * @param dateTestRanEndedDisplayString
     *            the dateTestRanEndedDisplayString to set
     */
    public void setDateTestRanEndedDisplayString(String dateTestRanEndedDisplayString) {
        this.dateTestRanEndedDisplayString = dateTestRanEndedDisplayString;
    }

    /**
     * @return the testDurationDisplayString
     */
    public String getTestDurationDisplayString() {
        return testDurationDisplayString;
    }

    /**
     * @param testDurationDisplayString
     *            the testDurationDisplayString to set
     */
    public void setTestDurationDisplayString(String testDurationDisplayString) {
        this.testDurationDisplayString = testDurationDisplayString;
    }

    /**
     * @return the timer
     */
    public Timer getTimer() {
        return timer;
    }

    /**
     * @return the directories
     */
    public List<File> getDirectories() {
        return directories;
    }

    /**
     * @param directories
     *            the directories to set
     */
    public void setDirectories(List<File> directories) {
        this.directories = directories;
    }

    /**
     * @return the directoriesSearched
     */
    public List<File> getDirectoriesSearched() {
        return directoriesSearched;
    }

    /**
     * @param directoriesSearched
     *            the directoriesSearched to set
     */
    public void setDirectoriesSearched(List<File> directoriesSearched) {
        this.directoriesSearched = directoriesSearched;
    }

    /**
     * @return the directoriesNotSearched
     */
    public List<File> getDirectoriesNotSearched() {
        return directoriesNotSearched;
    }

    /**
     * @param directoriesNotSearched
     *            the directoriesNotSearched to set
     */
    public void setDirectoriesNotSearched(List<File> directoriesNotSearched) {
        this.directoriesNotSearched = directoriesNotSearched;
    }

    /**
     * @return the errorMessages
     */
    public List<String> getErrorMessages() {
        return errorMessages;
    }

    /**
     * @param errorMessages
     *            the errorMessages to set
     */
    public void setErrorMessages(List<String> errorMessages) {
        this.errorMessages = errorMessages;
    }

    /**
     * @return the totalFileSizeDisplayString
     */
    public String getTotalFileSizeDisplayString() {
        return DupFileUtility.getBinarySizeDisplayString(totalFilesSize);
    }

    /**
     * @return the totalPartFileSizeDisplayString
     */
    public String getTotalPartFileSizeDisplayString() {
        return DupFileUtility.getBinarySizeDisplayString(totalPartFileSize);
    }
}