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

import java.util.ArrayList;
import java.util.List;

import com.swingtech.apps.filemgmt.model.FileIndexResults;

/**
 * @DOCME
 *
 * @author splas_000
 *
 */
public class FileIndexResultsReportData {
    List<FileIndexResults> fileIndexResultsList = new ArrayList<FileIndexResults>();

    /**
     * @return the fileIndexResultsList
     */
    public List<FileIndexResults> getFileIndexResultsList() {
        return fileIndexResultsList;
    }

    /**
     * @param fileIndexResultsList
     *            the fileIndexResultsList to set
     */
    public void setFileIndexResultsList(List<FileIndexResults> fileIndexResultsList) {
        this.fileIndexResultsList = fileIndexResultsList;
    }
}
