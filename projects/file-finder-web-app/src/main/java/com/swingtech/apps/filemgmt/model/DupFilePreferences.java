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
import java.util.List;

/**
 * @DOCME
 *
 * @author CUSTOMER
 *
 */
public class DupFilePreferences {
    List<String> defaultSearchDirectoryNames = new ArrayList<String>();
    String defaultTargetDupDirectory = null;
    String defaultTargetPartDirectory = null;
    List<String> defaultFolderMatchStrings = new ArrayList<String>();

    /**
     * @return the defaultTargetDupDirectory
     */
    public String getDefaultTargetDupDirectory() {
        return defaultTargetDupDirectory;
    }

    /**
     * @param defaultTargetDupDirectory
     *            the defaultTargetDupDirectory to set
     */
    public void setDefaultTargetDupDirectory(String defaultTargetDupDirectory) {
        this.defaultTargetDupDirectory = defaultTargetDupDirectory;
    }

    /**
     * @return the defaultTargetPartDirectory
     */
    public String getDefaultTargetPartDirectory() {
        return defaultTargetPartDirectory;
    }

    /**
     * @param defaultTargetPartDirectory
     *            the defaultTargetPartDirectory to set
     */
    public void setDefaultTargetPartDirectory(String defaultTargetPartDirectory) {
        this.defaultTargetPartDirectory = defaultTargetPartDirectory;
    }

    /**
     * @return the defaultFolderMatchStrings
     */
    public List<String> getDefaultFolderMatchStrings() {
        return defaultFolderMatchStrings;
    }

    /**
     * @param defaultFolderMatchStrings
     *            the defaultFolderMatchStrings to set
     */
    public void setDefaultFolderMatchStrings(List<String> defaultFolderMatchStrings) {
        this.defaultFolderMatchStrings = defaultFolderMatchStrings;
    }

    /**
     * @return the defaultSearchDirectoryNames
     */
    public List<String> getDefaultSearchDirectoryNames() {
        return defaultSearchDirectoryNames;
    }

    /**
     * @return the defaultSearchDirectoryNames
     */
    public List<File> getDefaultSearchDirectories() {
        List<File> defaultSearchDirectories = new ArrayList<File>();

        if (defaultSearchDirectoryNames == null || defaultSearchDirectoryNames.isEmpty()) {
            return defaultSearchDirectories;
        }

        for (String defaultSearchDirectoryName : defaultSearchDirectoryNames) {
            defaultSearchDirectories.add(new File(defaultSearchDirectoryName));
        }

        return defaultSearchDirectories;
    }

    /**
     * @param defaultSearchDirectoryNames
     *            the defaultSearchDirectoryNames to set
     */
    public void setDefaultSearchDirectoryNames(List<String> defaultSearchDirectoryNames) {
        this.defaultSearchDirectoryNames = defaultSearchDirectoryNames;
    }

}
