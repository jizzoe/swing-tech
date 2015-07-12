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
package com.swingtech.apps.filemgmt.dao.impl.file.model;

import java.util.HashMap;
import java.util.Map;

import com.swingtech.apps.filemgmt.model.FileEntity;

/**
 * @DOCME
 *
 * @author splas_000
 *
 */
public class FileEntityData {
    Map<String, FileEntity> fileEntityMap = new HashMap<String, FileEntity>();

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
}
