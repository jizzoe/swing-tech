package com.swingtech.apps.filemgmt.controller;

import java.util.List;

import com.swingtech.apps.filemgmt.model.DupFileFinderResults;
import com.swingtech.apps.filemgmt.model.FileSearchPreferences;
import com.swingtech.apps.filemgmt.model.FileSearchResults;
import com.swingtech.apps.filemgmt.service.FileSearchService;

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

    @RequestMapping("/")
    public String index(Model model) {
        return "index";
    }

    @RequestMapping("/find-duplicates-input")
    public String viewFindDuplicates(Model model) {
    	DupFileFinderResults dupFileFinderResults = null;
    	
        model.addAttribute("dupFileFinderResults", dupFileFinderResults);

        return "duplicate-file-input";
    }

    @RequestMapping("/search-files-input")
    public String viewSearchFiles(@RequestParam(value = "vacationStyle", required = false) List<String> vacationStyles, Model model) {
    	FileSearchPreferences fileSearchPreferences = null;
    	
    	fileSearchPreferences = fileSearchService.retrieveFileSearchPreferences();
    	
        model.addAttribute("fileSearchPreferences", fileSearchPreferences);

        return "file-search-input";
    }

    @RequestMapping("/find-duplicates")
    public String findDuplicates(Model model) {
    	DupFileFinderResults dupFileFinderResults = null;
    	
        model.addAttribute("dupFileFinderResults", dupFileFinderResults);

        return "duplicate-file-results";
    }

    @RequestMapping("/search-files")
    public String searchFiles(
    		@RequestParam(value = "searchDirectories", required = false) List<String> searchDirectories, 
    		@RequestParam(value = "searchTerms", required = false) List<String> searchTerms, 
    		Model model) 
    {
    	FileSearchResults fileSearchResults = null;
    	
    	this.tempPrintSearchInputs(searchDirectories, searchTerms);
    	
    	try {
			fileSearchResults = fileSearchService.findSearchMatchesWithStrings(searchDirectories, searchTerms);
			
			this.tempPrintSearchResults(fileSearchResults);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    	
        model.addAttribute("fileSearchResults", fileSearchResults);

        return "file-search-results";
    }
    
    private void tempPrintSearchInputs(List<String> searchDirectories, List<String> searchTerms) {
    	System.out.println("\n\n\nIncoming Search Directories");
    	
    	for (String searchDirectory : searchDirectories) {
    		System.out.println("   '" + searchDirectory + "'");
    	}

    	System.out.println("\nIncoming Search Terms");
    	
    	for (String searchTerm : searchTerms) {
    		System.out.println("   '" + searchTerm + "'");
    	}
    	
    	System.out.println("\n\n\n");
    }

    private void tempPrintSearchResults(FileSearchResults fileSearchResults) throws Exception {
    	fileSearchService.printResults(fileSearchResults, null);
    }

    
}