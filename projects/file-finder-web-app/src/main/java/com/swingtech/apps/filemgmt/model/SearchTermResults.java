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
import java.util.List;

/**
 * @DOCME
 *
 * @author CUSTOMER
 *
 */
public class SearchTermResults {
    String searchTerm = null;
    List<FileSearchResult> fileSearchMatches = new ArrayList<FileSearchResult>();

    /**
     * @return the numberOfFileSearchResults
     */
    public int getNumberOfFileSearchResults() {
        return fileSearchMatches.size();
    }

    /**
     * @return the searchTerm
     */
    public String getSearchTerm() {
        return searchTerm;
    }

    /**
     * @param searchTerm
     *            the searchTerm to set
     */
    public void setSearchTerm(String searchTerm) {
        this.searchTerm = searchTerm;
    }

    /**
     * @return the fileSearchMatches
     */
    public List<FileSearchResult> getFileSearchMatches() {
        return fileSearchMatches;
    }

    /**
     * @param fileSearchMatches
     *            the fileSearchMatches to set
     */
    public void setFileSearchMatches(List<FileSearchResult> fileSearchMatches) {
        this.fileSearchMatches = fileSearchMatches;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        SearchTermResults compareSearchTermResults = (SearchTermResults) obj;
        boolean matches = this.getSearchTerm().equals(compareSearchTermResults.getSearchTerm());
        return matches;
    }
}
