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
package com.swingtech.apps.filemgmt.service;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.util.List;

import com.swingtech.apps.filemgmt.model.FileSearchResults;
import com.swingtech.apps.filemgmt.model.GoogleDriveFileLocationEntity;
import com.swingtech.apps.filemgmt.util.GoogleApiUtils;

import com.google.api.services.drive.Drive;

/**
 * @DOCME
 *
 * @author jorice
 *
 */
public class GoogleDriveFileVisitor {
    private final FileSearchResults fileSearchResults;
    private int index = 0;
    private final List<String> searchTermPatterns;
    private final Drive driveService;

    public GoogleDriveFileVisitor(final FileSearchResults fileSearchResults, final List<String> searchTermPatterns, final Drive driveService) {
        super();
        this.fileSearchResults = fileSearchResults;
        this.searchTermPatterns = searchTermPatterns;
        this.driveService = driveService;
    }

	public FileVisitResult visitFile(GoogleDriveFileLocationEntity fileLocationEntity) throws IOException {
		if (GoogleApiUtils.isFolder(fileLocationEntity.getGoogleDriveFile())) {
			System.out.println("** Skipping folder:  " + GoogleApiUtils.getAbsolutePaths(driveService, fileLocationEntity.getGoogleDriveFile()));
			return FileVisitResult.CONTINUE;
		}
		System.out.println("row # " + index++);
		System.out.println("   Absolute Path:  " + fileLocationEntity.getAbsolutePath());
		System.out.println("   File Name:  " + fileLocationEntity.getFileName());
		System.out.println("   File Extension:  " + fileLocationEntity.getFileExtension());
		System.out.println("   File Name without Extension:  " + fileLocationEntity.getFileNameWithoutExtension());
		System.out.println("   File Size:  " + fileLocationEntity.getFileSize());
		System.out.println("   Url:  " + fileLocationEntity.getUrl());
		System.out.println("   Id:  " + fileLocationEntity.getGoogleDriveFileId());
		
		return FileVisitResult.CONTINUE;
	}
}
