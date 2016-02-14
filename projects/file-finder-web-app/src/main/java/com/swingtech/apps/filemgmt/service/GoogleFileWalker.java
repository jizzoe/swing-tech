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

import com.swingtech.apps.filemgmt.model.GoogleDriveFileLocationEntity;
import com.swingtech.apps.filemgmt.util.Timer;

import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;
import com.google.api.services.drive.model.ParentReference;
import com.google.api.services.drive.model.PropertyList;

/**
 * @DOCME
 *
 * @author jorice
 *
 */
public class GoogleFileWalker {
	public static void walkPath(Drive driveService, String googleDrivePath, GoogleDriveFileVisitor visitor) throws IOException {
    	String pageToken = null;
    	FileVisitResult fileVisitResult = null;
    	GoogleDriveFileLocationEntity fileLocationEntity = null;
    	Timer totalProcessTimer = new Timer();
    	Timer fileVisitTimer = new Timer();
    	Timer fileListTimer = new Timer();
    	Timer getParentsTimer = new Timer();
    	Timer newEntityTimer = new Timer();
    	
    	totalProcessTimer.startTiming();
    	
    	do {
    		fileListTimer.startTiming();
        	
    	    FileList result = driveService.files().list()
//    	            .setQ("mimeType='" + FileMgmtConstants.GOOGLE_FOLDER_MIME_TYPE + "'")
    	            .setSpaces("drive")
//    	            .setFields("properties")
    	            .setPageToken(pageToken)
    	            .execute();

    	    fileListTimer.stopTiming();
        	
        	System.out.println("** Total List Time:  " + fileListTimer.getDurationString());
        	
    	    for(File file: result.getItems()) {
    	    	getParentsTimer.startTiming();
            	
    	    	java.util.List<ParentReference> parents = file.getParents();

    	    	getParentsTimer.stopTiming();
            	
            	System.out.println("\n** Time to get Parent:  " + getParentsTimer.getDurationString());

            	newEntityTimer.startTiming();
            	
                fileLocationEntity = new GoogleDriveFileLocationEntity(driveService, file);

                newEntityTimer.stopTiming();
            	
            	System.out.println("** Time to create new Entity:  " + newEntityTimer.getDurationString());

            	fileVisitTimer.startTiming();
        		
                fileVisitResult = visitor.visitFile(fileLocationEntity);

                fileVisitTimer.stopTiming();
            	
            	System.out.println("** Total Visit Time:  " + fileVisitTimer.getDurationString());
            	
                if (!fileVisitResult.equals(FileVisitResult.CONTINUE)) {
                	break;
                }
    	    }
    	    pageToken = result.getNextPageToken();
    	} while (pageToken != null && fileVisitResult.equals(FileVisitResult.CONTINUE));
    	
    	totalProcessTimer.stopTiming();
    	
    	System.out.println("** Total Process Time:  " + totalProcessTimer.getDurationString());
	}
}
