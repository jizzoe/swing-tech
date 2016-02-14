/*
 * SwingTech Software - http://swing-tech.com/
 * 
 * Copyright (C) 2016 Joe Rice All rights reserved.
 * 
 * SwingTech Software is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by the Free
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

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import com.swingtech.apps.filemgmt.util.DupFileUtility;
import com.swingtech.apps.filemgmt.util.GoogleApiUtils;

import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;

/**
 * @DOCME
 *
 * @author jorice
 *
 */
public class GoogleDriveFileLocationEntity extends FileLocationEntity {
	File googleDriveFile;
	List<String> absolutePaths;
	String googleDriveFileId;
	
	public GoogleDriveFileLocationEntity(Drive driveService, File file) {
		this.setGoogleDriveFile(driveService, file);
	}

	/**
     * @param file
     *            the file to set
	 * @throws IOException 
     */
    public void setGoogleDriveFile(Drive driveService, File file) {
    	List<String> absoluteFilePaths;
		try {
			absoluteFilePaths = GoogleApiUtils.getAbsolutePaths(driveService, file);
		} catch (IOException e1) {
			throw new RuntimeException("Error trying to get absolutePaths from google drive file, '" + file + "'", e1);
		}
    	
        this.googleDriveFile = file;
        this.setAbsolutePath(absoluteFilePaths.get(0));
        this.setAbsolutePaths(absoluteFilePaths);
        this.setFileName(file.getTitle());
        this.setFileExtension(file.getFileExtension());
        this.setFileNameWithoutExtension(DupFileUtility.getFileNameWithoutExtension(this.getFileName()));
        this.setFileSize(file.getFileSize());
        this.setGoogleDriveFileId(file.getId());
        try {
            this.setUrl(new URL(file.getSelfLink()));
        }
        catch (MalformedURLException e) {
        }
    }


	/**
	 * @return the absolutePaths
	 */
	public List<String> getAbsolutePaths() {
		return absolutePaths;
	}

	/**
	 * @param absolutePaths the absolutePaths to set
	 */
	public void setAbsolutePaths(List<String> absolutePaths) {
		this.absolutePaths = absolutePaths;
	}

	/**
	 * @return the googleDriveFileId
	 */
	public String getGoogleDriveFileId() {
		return googleDriveFileId;
	}

	/**
	 * @param googleDriveFileId the googleDriveFileId to set
	 */
	public void setGoogleDriveFileId(String googleDriveFileId) {
		this.googleDriveFileId = googleDriveFileId;
	}

	/**
	 * @return the googleDriveFile
	 */
	public File getGoogleDriveFile() {
		return googleDriveFile;
	}

}
