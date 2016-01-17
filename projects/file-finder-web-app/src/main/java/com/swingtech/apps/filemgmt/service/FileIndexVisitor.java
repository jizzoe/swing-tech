package com.swingtech.apps.filemgmt.service;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;

import com.swingtech.apps.filemgmt.model.FileEntity;
import com.swingtech.apps.filemgmt.model.FileIndexResults;
import com.swingtech.apps.filemgmt.model.FileLocationEntity;
import com.swingtech.apps.filemgmt.util.DupFileUtility;

public class FileIndexVisitor implements FileVisitor<Path> {
    private final FileIndexResults fileIndexResults;
    private int index = 0;

    public FileIndexVisitor(final FileIndexResults fileIndexResults) {
        super();
        this.fileIndexResults = fileIndexResults;
    }

    @Override
    public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
        // TODO Auto-generated method stub
        return FileVisitResult.CONTINUE;
    }

    @Override
    public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
        // TODO Auto-generated method stub
        return FileVisitResult.CONTINUE;
    }

    @Override
    public FileVisitResult visitFile(Path path, BasicFileAttributes attrs) throws IOException {
        return this.visitFile(path.toFile());
    }

    public FileVisitResult visitFile(File file) throws IOException {
        String fileName = null;
        String fullFileName = null;

        index++;

        if (file.isDirectory()) {
            this.processDirectory(file);
            return FileVisitResult.CONTINUE;
        }

        fileName = DupFileUtility.getFileNameWithoutExtension(file);// file.getName().substring(0,
        // file.getName().indexOf("."));
        fullFileName = file.getAbsolutePath();

        fileIndexResults.setTotalFilesSize(fileIndexResults.getTotalFilesSize() + file.length());
        fileIndexResults.setNumFilesProcessed(fileIndexResults.getNumFilesProcessed() + 1);

        if (fullFileName.endsWith(".part")) {
            this.processPartFile(file);
        }

        this.processFile(fileName, file);

        if (index % 1000 == 0) {
            System.out.println("Processing row " + index);
        }

        return FileVisitResult.CONTINUE;
    }

    public void processPartFile(File file) {
        fileIndexResults.setTotalPartFileSize(fileIndexResults.getTotalPartFileSize() + file.length());
        fileIndexResults.setNumPartFiles(fileIndexResults.getNumPartFiles() + 1);
    }

    public void processFile(String fileName, File file) {
        FileEntity fileEntity = null;
        FileLocationEntity fileLocation = null;

        fileEntity = fileIndexResults.getFileEntityMap().get(fileName);

        if (fileEntity == null) {
            fileEntity = new FileEntity();
            fileEntity.setFileName(fileName);
            fileIndexResults.getFileEntityMap().put(fileName, fileEntity);
        }

        fileLocation = new FileLocationEntity(file);

        try {
            fileLocation.setUrl(new URL(file.getAbsolutePath()));
        }
        catch (MalformedURLException e) {
            fileLocation.setUrl(null);
        }

        fileEntity.getFileLocations().add(fileLocation);
    }

    public void processDirectory(File file) {
        // System.out.println("   directory:  " + file.getAbsolutePath());
        fileIndexResults.setTotalNumDirectories(fileIndexResults.getTotalNumDirectories() + 1);
        fileIndexResults.getDirectories().add(file);
    }

    @Override
    public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {

        fileIndexResults.setNumErrors(fileIndexResults.getNumErrors() + 1);

        return FileVisitResult.CONTINUE;
    }

}
