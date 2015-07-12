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
import java.util.HashMap;
import java.util.Map;

/**
 * @DOCME
 *
 * @author splas_000
 *
 */
public class MoveFilesResults extends BaseResults {
    Map<String, String> errors = new HashMap<String, String>();
    private Map<File, File> filesMoved = new HashMap<File, File>();
    private Map<File, File> filesNotMoved = new HashMap<File, File>();

    /**
     * @return the errors
     */
    public Map<String, String> getErrors() {
        return errors;
    }

    /**
     * @param errors
     *            the errors to set
     */
    public void setErrors(Map<String, String> errors) {
        this.errors = errors;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.swingtech.apps.filemgmt.model.BaseResults#getNumErrors()
     */
    @Override
    public int getNumErrors() {
        return errors.size();
    }

    /**
     * @return the filesMoved
     */
    public Map<File, File> getFilesMoved() {
        return filesMoved;
    }

    /**
     * @param filesMoved
     *            the filesMoved to set
     */
    public void setFilesMoved(Map<File, File> filesMoved) {
        this.filesMoved = filesMoved;
    }

    /**
     * @return the filesNotMoved
     */
    public Map<File, File> getFilesNotMoved() {
        return filesNotMoved;
    }

    /**
     * @param filesNotMoved
     *            the filesNotMoved to set
     */
    public void setFilesNotMoved(Map<File, File> filesNotMoved) {
        this.filesNotMoved = filesNotMoved;
    }
}
