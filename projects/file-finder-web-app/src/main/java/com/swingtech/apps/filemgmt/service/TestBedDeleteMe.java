package com.swingtech.apps.filemgmt.service;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.swingtech.apps.filemgmt.util.DupFileUtility;

public class TestBedDeleteMe {

    public static void main(String[] args) {
        test1();
    }

    public static void test3() {
        String string = "\"Hello\"";
        String replaceString = string.replace("\"", "");

        System.out.println("string:  " + string);
        System.out.println("replaceString:  '" + replaceString + "'");
    }

    public static void test2() {
        String string = "C:\\temp\\software-engineering-radio\\SE-Radio-Episode-221-Jez-Humble-on-Continuous-Delivery.mp3";
        File file = new File(string);

        String fileNameWithoutExtension = DupFileUtility.getFileNameWithoutExtension(file);
        String fileNameWtihExtension = DupFileUtility.getFileName(file);
        String fileNameExtension = DupFileUtility.getFileNameExtension(file);

        System.out.println("fileNameWithoutExtension = " + fileNameWithoutExtension);
        System.out.println("fileNameWtihExtension = " + fileNameWtihExtension);
        System.out.println("fileNameExtension = " + fileNameExtension);
    }

    public static void test1() {
        MatchStringListModel matchStringListModel = null;

        matchStringListModel = getTestData1();

        printMatches("Test 1", matchStringListModel);

        matchStringListModel = getTestData2();

        printMatches("Test 2", matchStringListModel);

        matchStringListModel = getTestData3();

        printMatches("Test 3", matchStringListModel);
    }

    public static void printMatches(String label, MatchStringListModel matchStringListModel) {

        System.out.println("\nMatches for " + label + " - " + matchStringListModel.getTargetString());

        for (String matchString : matchStringListModel.getMatchStringList()) {
            boolean matches = DupFileUtility.matchesPattern(matchStringListModel.getTargetString(), matchString, true);

            System.out.println("   does match String:  '" + matchString + "' match target String:  '"
                    + matchStringListModel.getTargetString() + "'?  " + matches);
        }
    }

    public static MatchStringListModel getTestData4() {
        MatchStringListModel matchStringListModel = new MatchStringListModel();
        List<String> matchStringList = new ArrayList<String>();
        String targetString = "";

        targetString = "ghg_keira_memvid.wmv";

        matchStringList.add("ghg_keira_memvid.wmv"); // true
        matchStringList.add("brf8399-720p"); // false
        matchStringList.add("brf8399-"); // false
        matchStringList.add("[\\S]*keira[\\S]*"); // true

        matchStringListModel.setMatchStringList(matchStringList);
        matchStringListModel.setTargetString(targetString);

        return matchStringListModel;
    }

    public static MatchStringListModel getTestData1() {
        MatchStringListModel matchStringListModel = new MatchStringListModel();
        List<String> matchStringList = new ArrayList<String>();
        String targetString = "";

        targetString = "brf8399-720p.mp4";

        matchStringList.add("brf8399-720p.mp4"); // true
        matchStringList.add("brf8399-720p"); // false
        matchStringList.add("brf8399-"); // false
        matchStringList.add("brf8399[a-zA-Z0-9.-]*"); // true

        matchStringListModel.setMatchStringList(matchStringList);
        matchStringListModel.setTargetString(targetString);

        return matchStringListModel;
    }

    public static MatchStringListModel getTestData3() {
        MatchStringListModel matchStringListModel = new MatchStringListModel();
        List<String> matchStringList = new ArrayList<String>();
        String targetString = "";

        targetString = "ghg_keira_memvid.wmv";

        matchStringList.add("ghg_keira_memvid.wmv"); // true
        matchStringList.add("brf8399-720p"); // false
        matchStringList.add("brf8399-"); // false
        matchStringList.add("[\\S]*keira[\\S]*"); // true

        matchStringListModel.setMatchStringList(matchStringList);
        matchStringListModel.setTargetString(targetString);

        return matchStringListModel;
    }

    public static MatchStringListModel getTestData2() {
        MatchStringListModel matchStringListModel = new MatchStringListModel();
        List<String> matchStringList = new ArrayList<String>();
        String targetString = "";

        targetString = "tbc_slumber_tammi_tori_memvid.wmv";

        matchStringList.add("ghg_keira_memvid.wmv"); // true
        matchStringList.add("brf8399-720p"); // false
        matchStringList.add("brf8399-"); // false
        matchStringList.add("[\\S]*keira[\\S]*"); // true

        matchStringListModel.setMatchStringList(matchStringList);
        matchStringListModel.setTargetString(targetString);

        return matchStringListModel;
    }

    private static class MatchStringListModel {
        List<String> matchStringList = null;
        String targetString = null;

        /**
         * @return the matchStringList
         */
        public List<String> getMatchStringList() {
            return matchStringList;
        }

        /**
         * @param matchStringList
         *            the matchStringList to set
         */
        public void setMatchStringList(List<String> matchStringList) {
            this.matchStringList = matchStringList;
        }

        /**
         * @return the targetString
         */
        public String getTargetString() {
            return targetString;
        }

        /**
         * @param targetString
         *            the targetString to set
         */
        public void setTargetString(String targetString) {
            this.targetString = targetString;
        }

    }
}
