package com.swingtech.apps.filemgmt.model;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.swingtech.apps.filemgmt.util.DupFileUtility;

public class DupFileFinderResults extends BaseResults {
    private int numDup;
    private long dupFilesSize;
    private Map<String, DupFile> dupFiles = new HashMap<String, DupFile>();
    private Map<String, File> allFilesNames = new HashMap<String, File>();
    private List<File> partFiles = new ArrayList<File>();
    private Map<String, DupFile> dupFilesThatCantBeMoved = new HashMap<String, DupFile>();
    private int numOfDuplicatesMoved;
    private Map<File, File> duplicatesMoved = new HashMap<File, File>();
    private Map<File, File> duplicatesErrorTryingToMove = new HashMap<File, File>();
    private Map<File, File> partsMoved = new HashMap<File, File>();
    private Map<File, File> partsErrorTryingToMove = new HashMap<File, File>();
    private List<File> duplicatesNotMoved = new ArrayList<File>();

    /**
     * @return the dupMapSize
     */
    public int getDupMapSize() {
        return getDupFiles().size();
    }

    public int getNumDup() {
        return numDup;
    }

    public void setNumDup(int numDup) {
        this.numDup = numDup;
    }

    public long getDupFilesSize() {
        return dupFilesSize;
    }

    public void setDupFilesSize(long dupFilesSize) {
        this.dupFilesSize = dupFilesSize;
    }

    public Map<String, DupFile> getDupFiles() {
        return dupFiles;
    }

    public void setDupFiles(Map<String, DupFile> dupFiles) {
        this.dupFiles = dupFiles;
    }

    public Map<String, File> getAllFilesNames() {
        return allFilesNames;
    }

    public void setAllFilesNames(Map<String, File> allFilesNames) {
        this.allFilesNames = allFilesNames;
    }

    public List<File> getPartFiles() {
        return partFiles;
    }

    public void setPartFiles(List<File> partFiles) {
        this.partFiles = partFiles;
    }

    public Map<String, DupFile> getDupFilesThatCantBeMoved() {
        return dupFilesThatCantBeMoved;
    }

    public void setDupFilesThatCantBeMoved(Map<String, DupFile> dupFilesThatCantBeMoved) {
        this.dupFilesThatCantBeMoved = dupFilesThatCantBeMoved;
    }

    public int getNumOfDuplicatesMoved() {
        return numOfDuplicatesMoved;
    }

    public void setNumOfDuplicatesMoved(int numOfDuplicatesMoved) {
        this.numOfDuplicatesMoved = numOfDuplicatesMoved;
    }

    public Map<File, File> getDuplicatesMoved() {
        return duplicatesMoved;
    }

    public void setDuplicatesMoved(Map<File, File> duplicatesMoved) {
        this.duplicatesMoved = duplicatesMoved;
    }

    public List<File> getDuplicatesNotMoved() {
        return duplicatesNotMoved;
    }

    public void setDuplicatesNotMoved(List<File> duplicatesNotMoved) {
        this.duplicatesNotMoved = duplicatesNotMoved;
    }

    public Map<File, File> getDuplicatesErrorTryingToMove() {
        return duplicatesErrorTryingToMove;
    }

    public void setDuplicatesErrorTryingToMove(Map<File, File> duplicatesErrorTryingToMove) {
        this.duplicatesErrorTryingToMove = duplicatesErrorTryingToMove;
    }

    public Map<File, File> getPartsMoved() {
        return partsMoved;
    }

    public void setPartsMoved(Map<File, File> partsMoved) {
        this.partsMoved = partsMoved;
    }

    public Map<File, File> getPartsErrorTryingToMove() {
        return partsErrorTryingToMove;
    }

    public void setPartsErrorTryingToMove(Map<File, File> partsErrorTryingToMove) {
        this.partsErrorTryingToMove = partsErrorTryingToMove;
    }

    /**
     * @return the dupFilesSizeDisplay
     */
    public String getDupFilesSizeDisplay() {
        return DupFileUtility.getBinarySizeDisplayString(dupFilesSize);
    }
}
