package com.swingtech.apps.filemgmt.main;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.swingtech.apps.filemgmt.model.FileSearchResults;
import com.swingtech.apps.filemgmt.service.GoogleDriveFileVisitor;
import com.swingtech.apps.filemgmt.service.GoogleFileWalker;
import com.swingtech.apps.filemgmt.util.GoogleApiUtils;

import org.springframework.core.io.ClassPathResource;

import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;
import com.google.api.services.drive.model.ParentReference;
import com.google.api.services.drive.model.PropertyList;


public class DriveQuickStart {
    /** Application name. */
    private static final String APPLICATION_NAME =
        "Drive API Java Quickstart";

    /** Directory to store user credentials for this application. */
    private static final java.io.File DATA_STORE_DIR = new java.io.File(
        System.getProperty("user.home"), ".credentials/drive-java-quickstart");

    /** Global instance of the scopes required by this quickstart. */
    private static final List<String> SCOPES =
        Arrays.asList(DriveScopes.DRIVE_METADATA_READONLY);

    public static void main(String[] args) throws IOException {
    	ClassPathResource cpr = new ClassPathResource("client_secret.json");
    	java.io.File clientSecretFile = cpr.getFile();
    	
//    	testGettingFiles(clientSecretFile);
//    	testGettingFolders(clientSecretFile);
    	testGoogleDriveWalking(clientSecretFile);
    }
    
    public static void testGoogleDriveWalking(java.io.File clientSecretFile) throws IOException {
        // Build a new authorized API client service.
        Drive driveService = GoogleApiUtils.getDriveService(clientSecretFile, DATA_STORE_DIR, SCOPES, APPLICATION_NAME);
        FileSearchResults fileSearchResults = new FileSearchResults();
        List<String> searchTermPatterns = new ArrayList<String>();
        
        GoogleDriveFileVisitor visitor = new GoogleDriveFileVisitor(fileSearchResults, searchTermPatterns, driveService);
    
        GoogleFileWalker.walkPath(driveService, "", visitor);
    }
    
    public static void testGettingFolders(java.io.File clientSecretFile) throws IOException {
        // Build a new authorized API client service.
        Drive driveService = GoogleApiUtils.getDriveService(clientSecretFile, DATA_STORE_DIR, SCOPES, APPLICATION_NAME);

    	String pageToken = null;
    	do {
    	    FileList result = driveService.files().list()
//    	            .setQ("mimeType='" + FileMgmtConstants.GOOGLE_FOLDER_MIME_TYPE + "'")
    	            .setSpaces("drive")
//    	            .setFields("properties")
    	            .setPageToken(pageToken)
    	            .execute();
    	    for(File file: result.getItems()) {
    	    	java.util.List<ParentReference> parents = file.getParents();
            	PropertyList properties = driveService.properties().list(file.getId()).execute();
                System.out.printf("%s (%s), %s, %s, %s\n", file.getTitle(), file.getId(), file.getMimeType(), file.getSelfLink(), GoogleApiUtils.getAbsolutePaths(driveService, file));
    	    }
    	    pageToken = result.getNextPageToken();
    	} while (pageToken != null);

    }
    
    public static void testGettingFiles(java.io.File clientSecretFile) throws IOException {

        // Build a new authorized API client service.
        Drive service = GoogleApiUtils.getDriveService(clientSecretFile, DATA_STORE_DIR, SCOPES, APPLICATION_NAME);

        // Print the names and IDs for up to 10 files.
        FileList result = service.files().list()
//             .setMaxResults(10)
             .execute();
        List<File> files = result.getItems();
        if (files == null || files.size() == 0) {
            System.out.println("No files found.");
        } else {
            System.out.println("Files (" + files.size() + "):");
            for (File file : files) {
            	PropertyList properties = service.properties().list(file.getId()).execute();
                System.out.printf("%s (%s), %s, %s\n", file.getTitle(), file.getId(), file.getMimeType(), file.getSelfLink());
            }
        }
    }

}
