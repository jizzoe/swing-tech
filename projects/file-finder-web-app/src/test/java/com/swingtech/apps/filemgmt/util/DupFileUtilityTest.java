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
package com.swingtech.apps.filemgmt.util;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

/**
 * @DOCME
 *
 * @author splas_000
 *
 */
public class DupFileUtilityTest {

    @Test
    public void test() {
        Map<String, Boolean> matchStringList;
        String matchFileString = null;
        File matchFile = null;

        matchFileString = "C:\\drives\\Google Drive\\capitalism\\new\\new25-\\VideoDrive - Video of a Certain Quality.mp4";
        matchFile = new File(matchFileString);

        matchStringList = this.getNormalSuccessData();
        this.executeFileMatchTest(matchFile, matchStringList, false);

        matchStringList = this.getCaseSensitiveSuccessData();
        this.executeFileMatchTest(matchFile, matchStringList, false);

        matchStringList = this.getFailureData();
        this.executeFileMatchTest(matchFile, matchStringList, false);
    }

    protected void executeFileMatchTest(File matchFile, Map<String, Boolean> matchStringList, boolean caseSensitive) {
        Boolean fileMatches = null;

        for (Map.Entry<String, Boolean> matchEntry : matchStringList.entrySet()) {
            String matchString = matchEntry.getKey();
            Boolean expectedFileMatch = matchEntry.getValue();

            fileMatches = DupFileUtility.fileMatchesPattern(matchFile, matchString, caseSensitive);

            // System.out.println("   does match String:  '" + matchString +
            // "' match target String:  '"
            // + matchFile.getAbsolutePath() + "'?  " + fileMatches);

            Assert.assertEquals("Operation did not match expected results.  Expected = '" + expectedFileMatch
                    + "'.  Actual = '" + fileMatches + "'.  Trying to match this string '" + matchString
                    + "' to this fileName '" + matchFile.getAbsolutePath() + "'.", expectedFileMatch, fileMatches);
        }
    }

    protected Map<String, Boolean> getNormalSuccessData() {
        Map<String, Boolean> matchStringList = new HashMap<String, Boolean>();

        matchStringList.put("Video", true);
        matchStringList.put("Video of a Certain", true);
        matchStringList.put("VideoDrive - ", true);
        matchStringList.put("mp4", true);
        matchStringList.put(".mp4", true);
        matchStringList.put("VideoDrive - Video of a Certain Quality", true);
        matchStringList.put("VideoDrive - Video of a Certain Quality.mp4", true);
        matchStringList.put("VideoDrive - Video of a Certain Quality~mp4", true);
        matchStringList.put("~VideoDrive -~", true);
        matchStringList.put("VideoDrive -~", true);
        matchStringList.put("~Certain Quality.mp4", true);
        matchStringList.put("~Video~of~a~Certain~Quality~", true);

        return matchStringList;
    }

    protected Map<String, Boolean> getCaseSensitiveSuccessData() {
        Map<String, Boolean> matchStringList = new HashMap<String, Boolean>();

        matchStringList.put("video", true);
        matchStringList.put("video of a certain", true);
        matchStringList.put("videodrive - ", true);
        matchStringList.put("MP4", true);
        matchStringList.put(".MP4", true);
        matchStringList.put("VIDEODRIVE - VIDEO OF A CERTAIN QUALITY", true);
        matchStringList.put("videodrive - video of a certain quality.mp4", true);
        matchStringList.put("vIDEOdrive - vIDEO OF A cERTAIN qUALITY~mp4", true);
        matchStringList.put("~videodrive -~", true);
        matchStringList.put("VIDEODRIVE -~", true);
        matchStringList.put("~certain quality.mp4", true);
        matchStringList.put("~video~of~a~certain~quality~", true);

        return matchStringList;
    }

    protected Map<String, Boolean> getFailureData() {
        Map<String, Boolean> matchStringList = new HashMap<String, Boolean>();

        matchStringList.put("video", true);

        return matchStringList;
    }
}
