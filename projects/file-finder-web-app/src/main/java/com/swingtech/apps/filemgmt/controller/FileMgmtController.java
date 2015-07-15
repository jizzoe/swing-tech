package com.swingtech.apps.filemgmt.controller;

import java.io.File;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.swingtech.apps.filemgmt.model.DupFileFinderResults;
import com.swingtech.apps.filemgmt.model.DupFilePreferences;
import com.swingtech.apps.filemgmt.model.FileSearchPreferences;
import com.swingtech.apps.filemgmt.model.FileSearchResults;
import com.swingtech.apps.filemgmt.model.MoveFilesResults;
import com.swingtech.apps.filemgmt.model.VideoPlayerInputs;
import com.swingtech.apps.filemgmt.service.DupFileService;
import com.swingtech.apps.filemgmt.service.FileSearchService;
import com.swingtech.apps.filemgmt.util.MultiPartFileSender;
import com.swingtech.apps.filemgmt.util.Timer;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class FileMgmtController {
    @Value("${rest-url-prefix}")
    private final String restUrlPrefix = null;

    private FileSearchService fileSearchService = new FileSearchService();
    private DupFileService dupFileService = new DupFileService();

    @RequestMapping("/")
    public String index(Model model) {
        return "index";
    }

    @RequestMapping("/find-duplicates-input")
    public String viewFindDuplicates(Model model) {
        DupFilePreferences dupFilePreferences = null;

        dupFilePreferences = dupFileService.retrieveDupFilePreferences();

        model.addAttribute("dupFilePreferences", dupFilePreferences);

        return "duplicate-file-input";
    }

    @RequestMapping("/video-player")
    public String viewVideoPlayer(@RequestParam(value = "videoFileName", required = false) String videoFileName,
            Model model) {
        VideoPlayerInputs videoPlayerInputs = null;

        System.out.println("\n\n\n");

        System.out.println("Viewing video File.  Incoming fileName to view:  " + videoFileName);

        System.out.println("\n\n\n");

        videoPlayerInputs = fileSearchService.getVideoPlayerInputs(videoFileName);

        model.addAttribute("videoPlayerInputs", videoPlayerInputs);

        return "video-player";
    }

    @RequestMapping("/stream-video")
    public void streamVideo(@RequestParam(value = "videoFileName", required = false) String videoFileName,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        Timer timer = new Timer();
        File videoFile = null;
        VideoPlayerInputs videoPlayerInputs = null;

        System.out.println("\n\n\n");

        System.out.println("Streaming File.  Incoming fileNameToStream:  " + videoFileName);

        videoPlayerInputs = fileSearchService.getVideoPlayerInputs(videoFileName);

        videoFile = videoPlayerInputs.getVideoFile();

        System.out.println("Streaming file, '" + videoFileName + "'.  About to read bytes");

        timer.startTiming();

        MultiPartFileSender.fromFile(videoFile).with(request).with(response).serveResource();

        timer.stopTiming();

        System.out.println("It took " + timer.getDurationString() + " to server the '" + videoFile.getAbsolutePath()
                + "' file resource");

        System.out.println("\n\n\n");
    }

    @RequestMapping("/search-files-input")
    public String viewSearchFiles(@RequestParam(value = "vacationStyle", required = false) List<String> vacationStyles,
            Model model) {
        FileSearchPreferences fileSearchPreferences = null;

        fileSearchPreferences = fileSearchService.retrieveFileSearchPreferences();

        model.addAttribute("fileSearchPreferences", fileSearchPreferences);

        return "file-search-input";
    }

    @RequestMapping("/find-duplicates")
    public String findDuplicates(
            @RequestParam(value = "searchDirectories", required = false) List<String> searchDirectories,
            @RequestParam(value = "targetDupDirectory", required = false) String targetDupDirectory,
            @RequestParam(value = "targetPartDirectory", required = false) String targetPartDirectory,
            @RequestParam(value = "targetReportDirectory", required = false) String targetReportDirectory,
            @RequestParam(value = "folderMatchStrings", required = false) List<String> folderMatchStrings,
            @RequestParam(value = "searchFileSystem", required = false) Boolean searchFileSystem,
            @RequestParam(value = "moveDuplicates", required = false) Boolean moveDuplicates,
            @RequestParam(value = "movePartFiles", required = false) Boolean movePartFiles, Model model) {
        DupFileFinderResults dupFileFinderResults = null;

        if (searchFileSystem == null) {
            searchFileSystem = false;
        }

        if (moveDuplicates == null) {
            moveDuplicates = false;
        }

        if (movePartFiles == null) {
            movePartFiles = false;
        }

        this.tempPrintDupFileInputs(searchDirectories, targetDupDirectory, targetPartDirectory, targetReportDirectory,
                folderMatchStrings, searchFileSystem, moveDuplicates, movePartFiles);

        try {
            dupFileFinderResults = dupFileService.findAndMoveAllDuplicatesWithStrings(searchDirectories,
                    targetDupDirectory, targetPartDirectory, folderMatchStrings, moveDuplicates, movePartFiles,
                    searchFileSystem);

            if (targetReportDirectory == null || targetReportDirectory.trim().isEmpty()) {
                targetReportDirectory = null;
            }

            this.printDupFileResults(dupFileFinderResults, targetReportDirectory);
        }
        catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        model.addAttribute("dupFileFinderResults", dupFileFinderResults);

        return "duplicate-file-results";
    }

    @RequestMapping("/search-files")
    public String searchFiles(
            @RequestParam(value = "searchDirectories", required = false) List<String> searchDirectories,
            @RequestParam(value = "searchTerms", required = false) List<String> searchTerms,
            @RequestParam(value = "searchFileSystem", required = false) Boolean searchFileSystem, Model model) {
        FileSearchResults fileSearchResults = null;
        FileSearchPreferences fileSearchPreferences = null;

        if (searchFileSystem == null) {
            searchFileSystem = false;
        }

        this.tempPrintSearchInputs(searchDirectories, searchTerms, searchFileSystem);

        try {
            fileSearchResults = fileSearchService.findSearchMatchesWithStrings(searchDirectories, searchTerms,
                    searchFileSystem);

            this.tempPrintSearchResults(fileSearchResults);
        }
        catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        model.addAttribute("fileSearchResults", fileSearchResults);

        fileSearchPreferences = fileSearchService.retrieveFileSearchPreferences();

        model.addAttribute("fileSearchPreferences", fileSearchPreferences);

        return "file-search-results";
    }

    @RequestMapping("/move-files")
    public String moveFiles(@RequestParam(value = "filesToMove", required = false) List<String> filesToMove,
            @RequestParam(value = "moveToDirectory", required = false) String moveToDirectory, Model model) {
        MoveFilesResults moveFilesResults = null;

        this.tempPrintMoveFileInputs(filesToMove, moveToDirectory);

        try {
            moveFilesResults = fileSearchService.moveFiles(filesToMove, moveToDirectory);

            this.tempPrintMoveFileResults(moveFilesResults);
        }
        catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        model.addAttribute("moveFileResults", moveFilesResults);

        return "move-files-results";
    }

    private void tempPrintDupFileInputs(List<String> searchDirectories, String targetDupDirectory,
            String targetPartDirectory, String targetReportDirectory, List<String> folderMatchStrings,
            Boolean searchFileSystem, Boolean moveDuplicates, boolean movePartFiles) {
        System.out.println("\n\n\nIncoming Search Directories");

        for (String searchDirectory : searchDirectories) {
            System.out.println("   '" + searchDirectory + "'");
        }

        System.out.println("\nIncoming Target Dup Directory:  " + targetDupDirectory);

        System.out.println("\nIncoming Target Parts Directory:  " + targetPartDirectory);

        System.out.println("\nIncoming Target Report Directory:  " + targetReportDirectory);

        System.out.println("\nIncoming Folder Match  Strings");

        for (String searchTerm : folderMatchStrings) {
            System.out.println("   '" + searchTerm + "'");
        }

        System.out.println("\nsearchFileSystem:  " + searchFileSystem);

        System.out.println("\nmoveDuplicates:  " + moveDuplicates);

        System.out.println("\nmovePartFiles:  " + movePartFiles);

        System.out.println("\n\n\n");
    }

    private void tempPrintMoveFileInputs(List<String> filesToMove, String moveToDirectory) {
        System.out.println("\n\n\nIncoming Files To Move");

        for (String searchDirectory : filesToMove) {
            System.out.println("   '" + searchDirectory + "'");
        }

        System.out.println("\nIncoming Search Terms:  " + moveToDirectory);

        System.out.println("\n\n\n");
    }

    private void tempPrintSearchInputs(List<String> searchDirectories, List<String> searchTerms,
            Boolean searchFileSystem) {
        System.out.println("\n\n\nIncoming Search Directories");

        for (String searchDirectory : searchDirectories) {
            System.out.println("   '" + searchDirectory + "'");
        }

        System.out.println("\nIncoming Search Terms");

        for (String searchTerm : searchTerms) {
            System.out.println("   '" + searchTerm + "'");
        }

        System.out.println("\nsearchFileSystem:  " + searchFileSystem);

        System.out.println("\n\n\n");
    }

    private void tempPrintSearchResults(FileSearchResults fileSearchResults) throws Exception {
        fileSearchService.printSearchResults(fileSearchResults, null);
    }

    private void tempPrintMoveFileResults(MoveFilesResults moveFilesResults) throws Exception {
        fileSearchService.printMoveFileResults(moveFilesResults, null);
    }

    private void printDupFileResults(DupFileFinderResults dupFileFinderResults, String targetReportDirectory)
            throws Exception {
        dupFileService.printResults(dupFileFinderResults, new File(targetReportDirectory));
    }

}