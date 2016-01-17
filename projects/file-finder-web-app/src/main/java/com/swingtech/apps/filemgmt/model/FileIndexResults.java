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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @DOCME
 *
 * @author splas_000
 *
 */
public class FileIndexResults extends BaseResults {
    Map<String, FileEntity> fileEntityMap = new HashMap<String, FileEntity>();
    List<String> excludeDirStrings = new ArrayList<String>();
    Map<String, FileEntity> removedFileEntityMap = new HashMap<String, FileEntity>();
    Map<String, FileEntity> addedFileEntityMap = new HashMap<String, FileEntity>();
    List<FileLocationEntity> addedFileLocations = new ArrayList<FileLocationEntity>();
    List<FileLocationEntity> removedFileLocations = new ArrayList<FileLocationEntity>();
    int numberOfFileEntities = 0;

    /**
     * @return the fileEntityMap
     */
    public Map<String, FileEntity> getFileEntityMap() {
        return fileEntityMap;
    }

    /**
     * @param fileEntityMap
     *            the fileEntityMap to set
     */
    public void setFileEntityMap(Map<String, FileEntity> fileEntityMap) {
        this.fileEntityMap = fileEntityMap;
    }

    /**
     * @return the excludeDirStrings
     */
    public List<String> getExcludeDirStrings() {
        return excludeDirStrings;
    }

    /**
     * @param excludeDirStrings
     *            the excludeDirStrings to set
     */
    public void setExcludeDirStrings(List<String> excludeDirStrings) {
        this.excludeDirStrings = excludeDirStrings;
    }

    /**
     * @return the numberOfFileEntities
     */
    public int getNumberOfFileEntities() {
        if (numberOfFileEntities > 0) {
            return numberOfFileEntities;
        }
        if (fileEntityMap == null) {
            return 0;
        }
        return fileEntityMap.size();
    }

    /**
     * @return the removedFileEntityMap
     */
    public Map<String, FileEntity> getRemovedFileEntityMap() {
        return removedFileEntityMap;
    }

    /**
     * @param removedFileEntityMap
     *            the removedFileEntityMap to set
     */
    public void setRemovedFileEntityMap(Map<String, FileEntity> removedFileEntityMap) {
        this.removedFileEntityMap = removedFileEntityMap;
    }

    /**
     * @return the addedFileEntityMap
     */
    public Map<String, FileEntity> getAddedFileEntityMap() {
        return addedFileEntityMap;
    }

    /**
     * @param addedFileEntityMap
     *            the addedFileEntityMap to set
     */
    public void setAddedFileEntityMap(Map<String, FileEntity> addedFileEntityMap) {
        this.addedFileEntityMap = addedFileEntityMap;
    }

    /**
     * @return the addedFileLocations
     */
    public List<FileLocationEntity> getAddedFileLocations() {
        return addedFileLocations;
    }

    /**
     * @param addedFileLocations
     *            the addedFileLocations to set
     */
    public void setAddedFileLocations(List<FileLocationEntity> addedFileLocations) {
        this.addedFileLocations = addedFileLocations;
    }

    /**
     * @return the removedFileLocations
     */
    public List<FileLocationEntity> getRemovedFileLocations() {
        return removedFileLocations;
    }

    /**
     * @param removedFileLocations
     *            the removedFileLocations to set
     */
    public void setRemovedFileLocations(List<FileLocationEntity> removedFileLocations) {
        this.removedFileLocations = removedFileLocations;
    }

    /**
     * @param numberOfFileEntities
     *            the numberOfFileEntities to set
     */
    public void setNumberOfFileEntities(int numberOfFileEntities) {
        this.numberOfFileEntities = numberOfFileEntities;
    }

}
