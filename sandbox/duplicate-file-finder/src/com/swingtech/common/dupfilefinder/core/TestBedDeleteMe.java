package com.swingtech.common.dupfilefinder.core;

import java.util.ArrayList;
import java.util.List;

import com.swingtech.common.dupfilefinder.util.DupFileUtility;

public class TestBedDeleteMe {

	public static void main(String[] args) {
		MatchStringListModel matchStringListModel = null;
		
		matchStringListModel = getTestData1();
		
		printMatches("Test 1", matchStringListModel);
		
		
		matchStringListModel = getTestData2();
		
		printMatches("Test 2", matchStringListModel);
		
		matchStringListModel = getTestData3();
		
		printMatches("Test 3", matchStringListModel);
	}

	public static void printMatches(String label, MatchStringListModel matchStringListModel) {
		
		System.out.println("\nMatches for " + label);

		for (String matchString : matchStringListModel.getMatchStringList()) {
			boolean matches = DupFileUtility.matchesPattern(matchStringListModel.getTargetString(), matchString);
			
			System.out.println("   does match String:  '" + matchString +  "' match target String:  '" + matchStringListModel.getTargetString() + "'?  " + matches);
		}
	}
	
	public static MatchStringListModel getTestData1() {
		MatchStringListModel matchStringListModel = new  MatchStringListModel();
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
		MatchStringListModel matchStringListModel = new  MatchStringListModel();
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
		MatchStringListModel matchStringListModel = new  MatchStringListModel();
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
		 * @param matchStringList the matchStringList to set
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
		 * @param targetString the targetString to set
		 */
		public void setTargetString(String targetString) {
			this.targetString = targetString;
		}
	
	}
}
