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

import java.util.ArrayList;
import java.util.List;

/**
 * @DOCME
 *
 * @author splas_000
 *
 */
public class FileEntity {
    private String fileName;
    private String fileExtension;
    private String description;
    List<FileLocationEntity> fileLocations = new ArrayList<FileLocationEntity>();

    /**
     * @return the fileName
     */
    public String getFileName() {
        return fileName;
    }

    /**
     * @param fileName
     *            the fileName to set
     */
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    /**
     * @return the fileExtension
     */
    public String getFileExtension() {
        return fileExtension;
    }

    /**
     * @param fileExtension
     *            the fileExtension to set
     */
    public void setFileExtension(String fileExtension) {
        this.fileExtension = fileExtension;
    }

    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description
     *            the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return the metaTags
     */
    public List<String> getMetaTags() {
        return metaTags;
    }

    /**
     * @param metaTags
     *            the metaTags to set
     */
    public void setMetaTags(List<String> metaTags) {
        this.metaTags = metaTags;
    }

    private List<String> metaTags = new ArrayList<String>();

    /**
     * @return the fileLocations
     */
    public List<FileLocationEntity> getFileLocations() {
        return fileLocations;
    }

    /**
     * @param fileLocations
     *            the fileLocations to set
     */
    public void setFileLocations(List<FileLocationEntity> fileLocations) {
        this.fileLocations = fileLocations;
    }

    /**
     * @return the totalFileSize
     */
    public long getTotalFileSize() {
        long totalFileSize = 0;

        if (fileLocations.isEmpty()) {
            return 0;
        }

        for (FileLocationEntity fileLocation : fileLocations) {
            totalFileSize = totalFileSize + fileLocation.getFile().getTotalSpace();
        }

        return totalFileSize;
    }

    /**
     * @return the numberOfLocations
     */
    public long getNumberOfLocations() {
        return fileLocations.size();
    }
}
