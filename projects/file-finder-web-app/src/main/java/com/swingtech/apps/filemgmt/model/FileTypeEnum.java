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
public enum FileTypeEnum {
    IMAGE("image"), VIDEO("video");

    String fileType = null;

    private FileTypeEnum(String fileType) {
        this.fileType = fileType;
    }

    /**
     * @return the fileType
     */
    public String getFileType() {
        return fileType;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Enum#toString()
     */
    @Override
    public String toString() {
        return fileType;
    }

}
