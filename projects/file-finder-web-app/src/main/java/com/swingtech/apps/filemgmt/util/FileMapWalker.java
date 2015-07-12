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

import java.io.IOException;
import java.util.Map;

import com.swingtech.apps.filemgmt.model.DupFileFinderResults;
import com.swingtech.apps.filemgmt.model.FileEntity;
import com.swingtech.apps.filemgmt.model.FileLocationEntity;
import com.swingtech.apps.filemgmt.model.FileSearchResults;
import com.swingtech.apps.filemgmt.service.DupFileVisitor;
import com.swingtech.apps.filemgmt.service.FileSearchVisitor;

/**
 * @DOCME
 *
 * @author splas_000
 *
 */
public class FileMapWalker {
    public static void walkFileEntityMapForSearch(Map<String, FileEntity> fileEntityMap,
            FileSearchVisitor fileSearchVisitor, FileSearchResults fileSearchResults) {
        for (String key : fileEntityMap.keySet()) {
            FileEntity fileEntity = fileEntityMap.get(key);

            for (FileLocationEntity fileLocationEntity : fileEntity.getFileLocations()) {
                try {
                    fileSearchVisitor.visitFile(fileLocationEntity);
                }
                catch (IOException e) {
                    fileSearchResults.setNumErrors(fileSearchResults.getNumErrors() + 1);
                    fileSearchResults.getErrorMessages().add(
                            "Error trying to visit file:  " + fileLocationEntity.getFile().getAbsolutePath() + ":  "
                                    + e.getClass().getName() + " - " + e.getMessage());
                }
            }
        }
    }

    public static void walkFileEntityMapForDup(Map<String, FileEntity> fileEntityMap, DupFileVisitor dupFileVisitor,
            DupFileFinderResults dupFileResults) {
        for (String key : fileEntityMap.keySet()) {
            FileEntity fileEntity = fileEntityMap.get(key);

            for (FileLocationEntity fileLocationEntity : fileEntity.getFileLocations()) {
                try {
                    dupFileVisitor.visitFile(fileLocationEntity.getFile());
                }
                catch (IOException e) {
                    dupFileResults.setNumErrors(dupFileResults.getNumErrors() + 1);
                    dupFileResults.getErrorMessages().add(
                            "Error trying to visit file:  " + fileLocationEntity.getFile().getAbsolutePath() + ":  "
                                    + e.getClass().getName() + " - " + e.getMessage());
                }
            }
        }
    }
}