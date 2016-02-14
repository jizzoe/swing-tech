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
package com.swingtech.apps.filemgmt.util;

/**
 * @DOCME
 *
 * @author splas_000
 *
 */
public class FileMgmtConstants {
    public static final String FILE_MGMT_HOME_DIR = "/tmp/swingtech/filemgmt";
    public static final String INDEX_FILE_LOCATION = FILE_MGMT_HOME_DIR + "/fileIndex.json";
    public static final String DUP_FILE_PREFERENCES_FILE_LOCATION = FILE_MGMT_HOME_DIR + "/dupFilePreferences.json";
    public static final String FILE_INDEX_PREFERENCES_FILE_LOCATION = FILE_MGMT_HOME_DIR + "/FileIndexPreferences.json";
    public static final String FILE_SEARCH_PREFERENCES_FILE_LOCATION = FILE_MGMT_HOME_DIR
            + "/fileSearchPreferences.json";
    public static final String INDEX_REPORT_FILE_LOCATION = FILE_MGMT_HOME_DIR + "/fileIndexReport.json";
    
    public static final String GOOGLE_FOLDER_MIME_TYPE = "application/vnd.google-apps.folder";
}
