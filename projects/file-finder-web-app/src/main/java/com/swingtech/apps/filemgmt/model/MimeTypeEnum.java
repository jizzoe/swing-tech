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

/**
 * @DOCME
 *
 * @author splas_000
 *
 */
public enum MimeTypeEnum {

    VIDEO_MP4("mp4", "video/mp4", FileTypeEnum.VIDEO), VIDEO_WMV("wmv", "video/x-ms-wmv", FileTypeEnum.VIDEO), VIDEO_AVI(
            "avi", "video/x-msvideo", FileTypeEnum.VIDEO), VIDEO_FLV("flv", "video/x-flv", FileTypeEnum.VIDEO), IMAGE_JPG(
                    "jpg", "image/jpeg", FileTypeEnum.IMAGE), IMAGE_PNG("png", "image/png", FileTypeEnum.IMAGE), IMAGE_GIF(
                            "gif", "image/gif", FileTypeEnum.IMAGE);

    private String fileExtension;
    private String mimeType;
    private FileTypeEnum fileType;

    private MimeTypeEnum(String fileExtension, String mimeType, FileTypeEnum fileType) {
        this.fileExtension = fileExtension;
        this.fileType = fileType;
        this.mimeType = mimeType;
    }

    public static MimeTypeEnum getByFileExtension(String fileExtension) {
        for (MimeTypeEnum mimeTypeEnum : MimeTypeEnum.values()) {
            if (mimeTypeEnum.getFileExtension().equals(fileExtension)) {
                return mimeTypeEnum;
            }
        }

        return null;
    }

    /**
     * @return the fileExtension
     */
    public String getFileExtension() {
        return fileExtension;
    }

    /**
     * @return the mimeType
     */
    public String getMimeType() {
        return mimeType;
    }

    /**
     * @return the fileType
     */
    public FileTypeEnum getFileType() {
        return fileType;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Enum#toString()
     */
    @Override
    public String toString() {
        return this.mimeType;
    }
}
