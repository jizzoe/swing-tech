/*
 * SwingTech Software - http://swing-tech.com/
 * 
 * Copyright (C) 2015 Joe Rice All rights reserved.
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

import java.io.File;

import com.swingtech.apps.filemgmt.util.DupFileUtility;

/**
 * @DOCME
 *
 * @author CUSTOMER
 *
 */
public class FileSearchResult {
	private File matchFile = null;
	private String matchFileName = null;
	private String matchFileNameUrl = null;
	private String matchFileNameWithoutExtension = null;
	private String matchFileFullPath = null;
	
	/**
	 * @return the matchFile
	 */
	public File getMatchFile() {
		return matchFile;
	}
	/**
	 * @param matchFile the matchFile to set
	 */
	public void setMatchFile(File matchFile) {
		this.matchFile = matchFile;
		this.matchFileFullPath = matchFile.getAbsolutePath();
		this.matchFileName = DupFileUtility.getFileName(matchFile);
		this.matchFileNameWithoutExtension = DupFileUtility.getFileNameWithoutExtension(matchFile);
		this.matchFileNameUrl = this.matchFileFullPath.replaceAll("\\\\", "/");
	}
	/**
	 * @return the matchFileName
	 */
	public String getMatchFileName() {
		return matchFileName;
	}
	/**
	 * @return the matchFileNameUrl
	 */
	public String getMatchFileNameUrl() {
		return matchFileNameUrl;
	}
	/**
	 * @return the matchFileNameWithoutExtension
	 */
	public String getMatchFileNameWithoutExtension() {
		return matchFileNameWithoutExtension;
	}
	/**
	 * @return the matchFileFullPath
	 */
	public String getMatchFileFullPath() {
		return matchFileFullPath;
	}
}
