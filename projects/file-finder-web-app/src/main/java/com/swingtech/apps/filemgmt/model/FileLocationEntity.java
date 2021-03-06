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
import java.net.MalformedURLException;
import java.net.URL;

import com.swingtech.apps.filemgmt.util.DupFileUtility;

/**
 * @DOCME
 *
 * @author splas_000
 *
 */
public class FileLocationEntity {
    private String fileName;
    private String fileNameWithoutExtension;
    private String fileExtension;
    private String absolutePath;
    private File file;
    private Long fileSize;
    private URL url;

    public FileLocationEntity() {
    }

    public FileLocationEntity(File file) {
        this.setFile(file);
    }

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
     * @return the file
     */
    public File getFile() {
        return file;
    }

    /**
     * @param file
     *            the file to set
     */
    public void setFile(File file) {
        this.file = file;
        this.setAbsolutePath(file.getAbsolutePath());
        this.setFileName(DupFileUtility.getFileName(file));
        this.setFileExtension(DupFileUtility.getFileNameExtension(file));
        this.setFileNameWithoutExtension(DupFileUtility.getFileNameWithoutExtension(file));
        this.setFileSize(file.length());
        try {
            this.setUrl(file.toURI().toURL());
        }
        catch (MalformedURLException e) {
        }
    }

    /**
     * @return the fileSize
     */
    public Long getFileSize() {
        return fileSize;
    }

    /**
     * @return the url
     */
    public URL getUrl() {
        return url;
    }

    /**
     * @return the absolutePath
     */
    public String getAbsolutePath() {
        return absolutePath;
    }

    /**
     * @param absolutePath
     *            the absolutePath to set
     */
    public void setAbsolutePath(String absolutePath) {
        this.absolutePath = absolutePath;
    }

    /**
     * @param fileSize
     *            the fileSize to set
     */
    public void setFileSize(Long fileSize) {
        this.fileSize = fileSize;
    }

    /**
     * @param url
     *            the url to set
     */
    public void setUrl(URL url) {
        this.url = url;
    }

    /**
     * @return the fileNameWithoutExtension
     */
    public String getFileNameWithoutExtension() {
        return fileNameWithoutExtension;
    }

    /**
     * @param fileNameWithoutExtension
     *            the fileNameWithoutExtension to set
     */
    public void setFileNameWithoutExtension(String fileNameWithoutExtension) {
        this.fileNameWithoutExtension = fileNameWithoutExtension;
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

}
