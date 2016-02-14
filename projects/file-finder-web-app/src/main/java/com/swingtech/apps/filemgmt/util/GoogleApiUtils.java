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
package com.swingtech.apps.filemgmt.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.ParentReference;

/**
 * @DOCME
 *
 * @author jorice
 *
 */
public class GoogleApiUtils {
	/** Global instance of the {@link FileDataStoreFactory}. */
	private static FileDataStoreFactory DATA_STORE_FACTORY;

	/** Global instance of the JSON factory. */
	private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();

	/** Global instance of the HTTP transport. */
	private static HttpTransport HTTP_TRANSPORT;
	
	private Drive driveService = null;

	private static void initGoogleApi(java.io.File dataStoreDirectory) {
		try {
			if (HTTP_TRANSPORT == null) {
				HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
			}
			if (DATA_STORE_FACTORY == null) {
				DATA_STORE_FACTORY = new FileDataStoreFactory(dataStoreDirectory);
			}
		} catch (Throwable t) {
			throw new RuntimeException("Error Initializing Google API.  dataStoreDirectory:  '"
					+ dataStoreDirectory.getAbsolutePath() + "'", t);
		}
	}

	/**
	 * Build and return an authorized Drive client service.
	 * 
	 * @return an authorized Drive client service
	 * @throws IOException
	 */
	public static Drive getDriveService(java.io.File clientSecretFile, java.io.File dataStoreDirectory,
			List<String> scopes, String applicationName) throws IOException {
		Credential credential = authorize(clientSecretFile, dataStoreDirectory, scopes);

		return new Drive.Builder(HTTP_TRANSPORT, JSON_FACTORY, credential).setApplicationName(applicationName).build();
	}

	private static Credential authorize(java.io.File clientSecretFile, java.io.File dataStoreDirectory,
			List<String> scopes) throws IOException {
		initGoogleApi(dataStoreDirectory);

		// Load client secrets.
		FileInputStream in = new FileInputStream(clientSecretFile);
		GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

		// Build flow and trigger user authorization request.
		GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(HTTP_TRANSPORT, JSON_FACTORY,
				clientSecrets, scopes).setDataStoreFactory(DATA_STORE_FACTORY).setAccessType("offline").build();
		Credential credential = new AuthorizationCodeInstalledApp(flow, new LocalServerReceiver()).authorize("user");
		System.out.println("Credentials saved to " + dataStoreDirectory.getAbsolutePath());
		return credential;
	}
	
	public static List<String> getAbsolutePaths(Drive driveService, File file) throws IOException {
		List<String> absoluteFilePaths = new ArrayList<String>();

		List<ParentReference> parents = file.getParents();

		for (ParentReference parent : parents) {
			List<String> parentFolderPaths = new ArrayList<String>();
			String fullFolderPath = null;
			
			File parentFile = driveService.files().get(parent.getId()).execute();
			parentFolderPaths.add(parentFile.getTitle());
			
			if (parentFile.getParents() != null) {
				getParentFolderPaths(parentFolderPaths, driveService, parentFile);
			}
			
			fullFolderPath = getFullFolderName(parentFolderPaths) + "/" + file.getTitle();
			
			absoluteFilePaths.add(fullFolderPath);
		}

		return absoluteFilePaths;
	}
	
	private static String getFullFolderName(List<String> parentFolderPaths) {
		StringBuffer folderBuf = new StringBuffer();
		
		for (int i = parentFolderPaths.size() - 1; i > 0; i--) {
			folderBuf.append("/");
			folderBuf.append(parentFolderPaths.get(i));
		}
		
		return folderBuf.toString();
	}

	private static void getParentFolderPaths(List<String> parentFolderPaths, Drive driveService, File parentFile)
			throws IOException {
		List<ParentReference> parents = parentFile.getParents();

		ParentReference parent = parents.get(0);
		File parentFilePath = driveService.files().get(parent.getId()).execute();

		parentFolderPaths.add(parentFilePath.getTitle());
		
		if (parentFilePath.getParents() != null && !parentFilePath.getParents().isEmpty()) {
			getParentFolderPaths(parentFolderPaths, driveService, parentFilePath);
		}
		
	}
	
	public static Boolean isFolder(File file) {
		if (file.getMimeType().equals(FileMgmtConstants.GOOGLE_FOLDER_MIME_TYPE)) {
			return true;
		}
		
		return false;
	}
}
