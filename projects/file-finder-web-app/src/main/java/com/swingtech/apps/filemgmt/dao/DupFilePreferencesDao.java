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

import com.swingtech.apps.filemgmt.model.DupFilePreferences;
import com.swingtech.apps.filemgmt.util.DupFileUtility;
import com.swingtech.apps.filemgmt.util.FileMgmtConstants;
import com.swingtech.apps.filemgmt.util.JsonUtil;

/**
 * @DOCME
 *
 * @author CUSTOMER
 *
 */
public class DupFilePreferencesDao {
    private File preferencesFile = new File(FileMgmtConstants.DUP_FILE_PREFERENCES_FILE_LOCATION);

    public DupFilePreferences retrieveDupFilePreferences() throws IOException {
        DupFilePreferences dupFilePreferences = null;

        if (!preferencesFile.exists()) {
            File preferencesFileDir = new File(FileMgmtConstants.FILE_MGMT_HOME_DIR);
            preferencesFileDir.mkdirs();

            preferencesFile.createNewFile();

            return new DupFilePreferences();
        }

        dupFilePreferences = JsonUtil.unmarshalJsonToObject(preferencesFile, DupFilePreferences.class);

        return dupFilePreferences;
    }

    public void saveDupFilePreferences(DupFilePreferences dupFilePreferences) throws IOException {
        String dupFilePreferencesString = null;

        if (!preferencesFile.exists()) {
            preferencesFile.createNewFile();
        }

        dupFilePreferencesString = JsonUtil.marshalObjectToJson(dupFilePreferences);

        DupFileUtility.writeToFile(preferencesFile, dupFilePreferencesString);
    }
}
