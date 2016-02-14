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

import com.swingtech.apps.filemgmt.model.FileSearchPreferences;
import com.swingtech.apps.filemgmt.util.DupFileUtility;
import com.swingtech.apps.filemgmt.util.FileMgmtConstants;
import com.swingtech.apps.filemgmt.util.JsonUtil;

/**
 * @DOCME
 *
 * @author CUSTOMER
 *
 */
public class FileSearchPreferencesDao {
    private File preferencesFile = new File(FileMgmtConstants.FILE_SEARCH_PREFERENCES_FILE_LOCATION);

    public FileSearchPreferences retrieveFileSearchPreferences() throws IOException {
        FileSearchPreferences fileSearchPreferences = null;

        if (!preferencesFile.exists() || preferencesFile.length() == 0) {
            File preferencesFileDir = new File(FileMgmtConstants.FILE_MGMT_HOME_DIR);
            preferencesFileDir.mkdirs();

            preferencesFile.createNewFile();
            
            fileSearchPreferences = new FileSearchPreferences();
            this.saveFileSearchPreferences(fileSearchPreferences);

            return fileSearchPreferences;
        }

        fileSearchPreferences = JsonUtil.unmarshalJsonToObject(preferencesFile, FileSearchPreferences.class);

        return fileSearchPreferences;
    }

    public void saveFileSearchPreferences(FileSearchPreferences fileSearchPreferences) throws IOException {
        String fileSearchPreferencesString = null;

        if (!preferencesFile.exists()) {
            preferencesFile.createNewFile();
        }

        fileSearchPreferencesString = JsonUtil.marshalObjectToJson(fileSearchPreferences);

        DupFileUtility.writeToFile(preferencesFile, fileSearchPreferencesString);
    }
}
