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
package com.swingtech.apps.filemgmt.dao;

import java.io.File;
import java.io.IOException;

import com.swingtech.apps.filemgmt.model.FileIndexPreferences;
import com.swingtech.apps.filemgmt.util.DupFileUtility;
import com.swingtech.apps.filemgmt.util.FileMgmtConstants;
import com.swingtech.apps.filemgmt.util.JsonUtil;

/**
 * @DOCME
 *
 * @author CUSTOMER
 *
 */
public class FileIndexPreferencesDao {
    private File preferencesFile = new File(FileMgmtConstants.FILE_INDEX_PREFERENCES_FILE_LOCATION);

    public FileIndexPreferences retrieveDupFilePreferences() throws IOException {
        FileIndexPreferences fileIndexPreferences = null;

        if (!preferencesFile.exists()) {
            File preferencesFileDir = new File(FileMgmtConstants.FILE_MGMT_HOME_DIR);
            preferencesFileDir.mkdirs();

            preferencesFile.createNewFile();

            return new FileIndexPreferences();
        }

        fileIndexPreferences = JsonUtil.unmarshalJsonToObject(preferencesFile, FileIndexPreferences.class);

        return fileIndexPreferences;
    }

    public void saveFileIndexPreferences(FileIndexPreferences fileIndexPreferences) throws IOException {
        String fileIndexPreferencesString = null;

        if (!preferencesFile.exists()) {
            preferencesFile.createNewFile();
        }

        fileIndexPreferencesString = JsonUtil.marshalObjectToJson(fileIndexPreferences);

        DupFileUtility.writeToFile(preferencesFile, fileIndexPreferencesString);
    }
}
