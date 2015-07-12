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

/**
 * @DOCME
 *
 * @author splas_000
 *
 */
public class VideoPlayerInputs {
    public String videoFileNameAbsolutePath = null;
    public String videoFileName = null;
    public String videoFileNameWithoutExtension = null;
    public String fileType = null;
    public String fileMimeType = null;
    public File videoFile = null;

    /**
     * @return the videoFileNameAbsolutePath
     */
    public String getVideoFileNameAbsolutePath() {
        return videoFileNameAbsolutePath;
    }

    /**
     * @param videoFileNameAbsolutePath
     *            the videoFileNameAbsolutePath to set
     */
    public void setVideoFileNameAbsolutePath(String videoFileName) {
        this.videoFileNameAbsolutePath = videoFileName;
    }

    /**
     * @return the fileType
     */
    public String getFileType() {
        return fileType;
    }

    /**
     * @param fileType
     *            the fileType to set
     */
    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    /**
     * @return the fileMimeType
     */
    public String getFileMimeType() {
        return fileMimeType;
    }

    /**
     * @param fileMimeType
     *            the fileMimeType to set
     */
    public void setFileMimeType(String fileMimeType) {
        this.fileMimeType = fileMimeType;
    }

    /**
     * @return the videoFile
     */
    public File getVideoFile() {
        return videoFile;
    }

    /**
     * @param videoFile
     *            the videoFile to set
     */
    public void setVideoFile(File videoFile) {
        this.videoFile = videoFile;
    }

    /**
     * @return the videoFileName
     */
    public String getVideoFileName() {
        return videoFileName;
    }

    /**
     * @param videoFileName
     *            the videoFileName to set
     */
    public void setVideoFileName(String videoFileName) {
        this.videoFileName = videoFileName;
    }

    /**
     * @return the videoFileNameWithoutExtension
     */
    public String getVideoFileNameWithoutExtension() {
        return videoFileNameWithoutExtension;
    }

    /**
     * @param videoFileNameWithoutExtension
     *            the videoFileNameWithoutExtension to set
     */
    public void setVideoFileNameWithoutExtension(String videoFileNameWithoutExtension) {
        this.videoFileNameWithoutExtension = videoFileNameWithoutExtension;
    }
}
