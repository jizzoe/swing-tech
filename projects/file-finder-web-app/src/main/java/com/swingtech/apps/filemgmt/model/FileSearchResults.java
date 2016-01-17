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
package com.swingtech.apps.filemgmt.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @DOCME
 *
 * @author splas_000
 *
 */
public class FileSearchResults extends BaseResults {
    private Map<String, List<FileSearchResult>> searchResults = new HashMap<String, List<FileSearchResult>>();
    private int numFilesMatchSearch;
    private List<SearchTermResults> searchTermResults = new ArrayList<SearchTermResults>();
    private List<String> searchTermsSearched = new ArrayList<String>();

    /**
     * @return the searchTermResults
     */
    public List<SearchTermResults> getSearchTermResults() {
        return searchTermResults;
    }

    /**
     * @param searchTermResults
     *            the searchTermResults to set
     */
    public void setSearchTermResults(List<SearchTermResults> searchTermResults) {
        this.searchTermResults = searchTermResults;
    }

    /**
     * @return the searchTermsSearched
     */
    public List<String> getSearchTermsSearched() {
        return searchTermsSearched;
    }

    /**
     * @param searchTermsSearched
     *            the searchTermsSearched to set
     */
    public void setSearchTermsSearched(List<String> searchTermsSearched) {
        this.searchTermsSearched = searchTermsSearched;
    }

    /**
     * @return the numFilesMatchSearch
     */
    public int getNumFilesMatchSearch() {
        return numFilesMatchSearch;
    }

    /**
     * @param numFilesMatchSearch
     *            the numFilesMatchSearch to set
     */
    public void setNumFilesMatchSearch(int numFilesMatchSearch) {
        this.numFilesMatchSearch = numFilesMatchSearch;
    }
}
