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
package com.swingtech.apps.filemgmt.dao.impl.file;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import com.swingtech.apps.filemgmt.dao.FileIndexDao;
import com.swingtech.apps.filemgmt.dao.impl.file.model.FileEntityData;
import com.swingtech.apps.filemgmt.dao.impl.file.model.FileIndexResultsReportData;
import com.swingtech.apps.filemgmt.model.FileEntity;
import com.swingtech.apps.filemgmt.model.FileIndexResults;
import com.swingtech.apps.filemgmt.model.FileLocationEntity;
import com.swingtech.apps.filemgmt.util.DupFileUtility;
import com.swingtech.apps.filemgmt.util.FileMgmtConstants;
import com.swingtech.apps.filemgmt.util.JsonUtil;

/**
 * @DOCME
 *
 * @author splas_000
 *
 */
public class FileIndexhDaoImplFileBased implements FileIndexDao {
    private File indexFile = new File(FileMgmtConstants.INDEX_FILE_LOCATION);
    private File indexReportFile = new File(FileMgmtConstants.INDEX_REPORT_FILE_LOCATION);

    /**
     * DOCME
     *
     */
    public FileIndexhDaoImplFileBased() {
        super();
        createNewFileIfNotPresent();
    }

    @Override
    public Map<String, FileEntity> retrieveFileEntityMap() throws IOException {
        FileEntityData fileEntityData = null;

        fileEntityData = this.retrieveFileEntityData();

        if (fileEntityData == null) {
            return null;
        }

        return fileEntityData.getFileEntityMap();
    }

    @Override
    public FileIndexResults saveFileIndexResults(FileIndexResults fileIndexResults) throws IOException {
        FileEntityData fileEntityData = null;
        Map<String, FileEntity> existingFileEntityMap = null;
        Map<String, FileEntity> newFileEntityMap = null;
        FileIndexResultsReportData fileIndexResultsReportData = null;

        fileEntityData = this.retrieveFileEntityData();

        if (fileEntityData == null) {
            fileEntityData = new FileEntityData();
        }

        existingFileEntityMap = fileEntityData.getFileEntityMap();
        newFileEntityMap = fileIndexResults.getFileEntityMap();

        this.removeDeletedFileNames(existingFileEntityMap, newFileEntityMap, fileIndexResults);

        for (String key : newFileEntityMap.keySet()) {
            FileEntity newFileEntity = newFileEntityMap.get(key);

            if (!existingFileEntityMap.containsKey(key)) {
                fileIndexResults.getAddedFileEntityMap().put(key, newFileEntity);
            }

            this.updateFileEntity(newFileEntity, fileEntityData, fileIndexResults);
        }

        this.saveFileEntityData(fileEntityData);

        fileIndexResultsReportData = this.retrieveFileIndexResultsReportData();

        if (fileIndexResultsReportData == null) {
            fileIndexResultsReportData = new FileIndexResultsReportData();
        }

        fileIndexResultsReportData.getFileIndexResultsList().add(fileIndexResults);

        this.saveFileIndexResultsReportData(fileIndexResultsReportData);

        return fileIndexResults;
    }

    private void removeDeletedFileNames(Map<String, FileEntity> existingFileEntityMap,
            Map<String, FileEntity> newFileEntityMap, FileIndexResults fileIndexResults) {
        for (String key : existingFileEntityMap.keySet()) {
            FileEntity existingFileEntity = existingFileEntityMap.get(key);

            if (!newFileEntityMap.containsKey(key)) {
                existingFileEntityMap.remove(key);
                fileIndexResults.getRemovedFileEntityMap().put(key, existingFileEntity);
            }
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.swingtech.apps.filemgmt.dao.FileIndexDao#retrieveFileEntity(java.
     * lang.String)
     */
    @Override
    public FileEntity retrieveFileEntity(String fileName) throws IOException {
        FileEntityData fileEntityData = null;

        fileEntityData = this.retrieveFileEntityData();

        if (fileEntityData == null || fileEntityData.getFileEntityMap() == null) {
            return null;
        }

        return fileEntityData.getFileEntityMap().get(fileName);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.swingtech.apps.filemgmt.dao.FileIndexDao#saveFileEntity(com.swingtech
     * .apps.filemgmt.model.FileEntity)
     */
    public void updateFileEntity(FileEntity newIndexFileEntity, FileEntityData fileEntityData,
            FileIndexResults fileIndexResults) throws IOException {
        FileEntity oldFileEntity = null;
        String fileName = null;

        if (newIndexFileEntity == null || newIndexFileEntity.getFileName() == null
                || newIndexFileEntity.getFileName().trim().isEmpty()) {
            throw new IllegalArgumentException(
                    "Illegal ARgument;  newIndexFileEntity cannot be null and it must contain a fileName");
        }

        fileName = newIndexFileEntity.getFileName();

        oldFileEntity = fileEntityData.getFileEntityMap().get(fileName);

        if (oldFileEntity == null) {
            oldFileEntity = newIndexFileEntity;
        }
        else {
            this.mergeFileEntities(oldFileEntity, newIndexFileEntity, fileIndexResults);
        }

        fileEntityData.getFileEntityMap().put(fileName, oldFileEntity);
    }

    private void mergeFileEntities(FileEntity oldFileEntity, FileEntity newIndexFileEntity,
            FileIndexResults fileIndexResults) {
        List<FileLocationEntity> oldFileLocations = null;
        List<FileLocationEntity> newFileLocations = null;

        // Assuming the newIndexFileEntity has just been indexed and it has the
        // latest and greatest file locations. So, just replace all the file
        // locations in the oldFileEntity and leave all the meta-data and
        // user-entered data alone.
        oldFileEntity.setFileLocations(newIndexFileEntity.getFileLocations());

        oldFileLocations = oldFileEntity.getFileLocations();
        newFileLocations = newIndexFileEntity.getFileLocations();

        for (FileLocationEntity newFileLocation : newFileLocations) {
            if (!this.containsFileLocation(oldFileLocations, newFileLocation)) {
                fileIndexResults.getRemovedFileLocations().add(newFileLocation);
            }
        }

        for (FileLocationEntity oldFileLocation : oldFileLocations) {
            if (!this.containsFileLocation(newFileLocations, oldFileLocation)) {
                fileIndexResults.getAddedFileLocations().add(oldFileLocation);
            }
        }
    }

    private boolean containsFileLocation(List<FileLocationEntity> fileLocationsList, FileLocationEntity fileLocation) {
        for (FileLocationEntity location : fileLocationsList) {
            if (location.getAbsolutePath().trim().equals(fileLocation.getAbsolutePath().trim())) {
                return true;
            }
        }
        return false;
    }

    private void createNewFileIfNotPresent() {

    }

    protected FileEntityData retrieveFileEntityData() throws IOException {
        FileEntityData fileEntityData = null;

        if (!indexFile.exists()) {
            File indexFileDir = new File(FileMgmtConstants.FILE_MGMT_HOME_DIR);
            indexFileDir.mkdirs();

            indexFile.createNewFile();
            FileEntityData newFileEntityData = new FileEntityData();
            this.saveFileEntityData(newFileEntityData);

            return null;
        }

        fileEntityData = JsonUtil.unmarshalJsonToObject(indexFile, FileEntityData.class);

        return fileEntityData;
    }

    protected FileIndexResultsReportData retrieveFileIndexResultsReportData() throws IOException {
        FileIndexResultsReportData fileIndexResultsReportData = null;

        if (!indexReportFile.exists()) {
            File indexReportFileDir = new File(FileMgmtConstants.FILE_MGMT_HOME_DIR);
            indexReportFileDir.mkdirs();

            indexReportFile.createNewFile();
            FileIndexResultsReportData newFileEntityData = new FileIndexResultsReportData();
            this.saveFileIndexResultsReportData(newFileEntityData);

            return null;
        }

        fileIndexResultsReportData = JsonUtil.unmarshalJsonToObject(indexReportFile, FileIndexResultsReportData.class);

        return fileIndexResultsReportData;
    }

    protected void saveFileIndexResultsReportData(FileIndexResultsReportData fileIndexResultsReportData)
            throws IOException {
        String fileIndexResultsReportDataString = null;

        if (!indexReportFile.exists()) {
            indexReportFile.createNewFile();
        }

        fileIndexResultsReportDataString = JsonUtil.marshalObjectToJson(fileIndexResultsReportData);

        DupFileUtility.writeToFile(indexReportFile, fileIndexResultsReportDataString);
    }

    protected void saveFileEntityData(FileEntityData fileEntityData) throws IOException {
        String fileSearchPreferencesString = null;

        if (!indexFile.exists()) {
            indexFile.createNewFile();
        }

        fileSearchPreferencesString = JsonUtil.marshalObjectToJson(fileEntityData);

        DupFileUtility.writeToFile(indexFile, fileSearchPreferencesString);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.swingtech.apps.filemgmt.dao.FileIndexDao#saveFileEntity(com.swingtech
     * .apps.filemgmt.model.FileEntity)
     */
    @Override
    public void saveFileEntity(FileEntity fileEntity) throws IOException {
        // TODO Auto-generated method stub

    }

}
