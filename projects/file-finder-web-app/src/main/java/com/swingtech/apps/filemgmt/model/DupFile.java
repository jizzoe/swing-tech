package com.swingtech.apps.filemgmt.model;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.swingtech.apps.filemgmt.util.DupFileUtility;

public class DupFile {
    private String dupFileName;
    private int numDups;
    private List<File> filesThatStayLocations = new ArrayList<File>();
    private List<File> dupFileLocations = new ArrayList<File>();
    private long totalDupFileSize;

    public String getDupFileName() {
        return dupFileName;
    }

    public void setDupFileName(String dupFileName) {
        this.dupFileName = dupFileName;
    }

    public List<File> getDupFileLocations() {
        return dupFileLocations;
    }

    public void setDupFileLocations(List<File> dupFileLocations) {
        this.dupFileLocations = dupFileLocations;
    }

    public long getTotalDupFileSize() {
        return totalDupFileSize;
    }

    public void setTotalDupFileSize(long totalDupFileSize) {
        this.totalDupFileSize = totalDupFileSize;
    }

    public int getNumDups() {
        return numDups;
    }

    public void setNumDups(int numDups) {
        this.numDups = numDups;
    }

    /**
     * @return the totalDupFileSizeDisplayString
     */
    public String getTotalDupFileSizeDisplayString() {
        return DupFileUtility.getBinarySizeDisplayString(totalDupFileSize);
    }

    /**
     * @return the filesThatStayLocations
     */
    public List<File> getFilesThatStayLocations() {
        return filesThatStayLocations;
    }

    /**
     * @param filesThatStayLocations
     *            the filesThatStayLocations to set
     */
    public void setFilesThatStayLocations(List<File> filesThatStayLocations) {
        this.filesThatStayLocations = filesThatStayLocations;
    }
}
