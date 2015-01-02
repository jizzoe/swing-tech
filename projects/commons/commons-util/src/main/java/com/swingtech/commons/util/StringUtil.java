/*
 * SwingTech Software - http://cooksarm.sourceforge.net/
 *
 * Copyright (C) 2011 Joe Rice
 * All rights reserved.
 * 
 * SwingTech Cooks Arm is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published
 * by the Free Software Foundation; either version 3 of the License,
 * or (at your option) any later version.
 *
 * SwingTech Cooks Arm is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with SwingTech Cooks Arm; If not, see <http://www.gnu.org/licenses/>. 
 * 
 */
package com.swingtech.commons.util;

import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Collection of general-purpose String-related Convenience methods that make programming life easy.
 *
 * @author jorice
 *
 */
public class StringUtil {

    public StringUtil() {
        super();
    }


    /**
     * DOCME
     * 
     * @param evalString
     * @param regEx
     * @return
     */
    public static List<String> getFirstRegExGroups(String evalString, String regEx) {
        Vector<String> regExGroupValues = new Vector<String>();
        Pattern pat = null;
        Matcher matcher = null;
        String matchedString = null;

        if (Utility.isNullOrEmpty(evalString) || Utility.isNullOrEmpty(regEx)) {
            throw new RuntimeException("Either evalString or regEx is null.  Both of these fields must be present.");
        }

        pat = Pattern.compile(regEx);
        matcher = pat.matcher(evalString);

        while (matcher.find()) {
            matchedString = matcher.group(1);
            regExGroupValues.add(matchedString);
        }

        if (regExGroupValues.isEmpty()) {
            return null;
        }

        return regExGroupValues;
    }


    public static List<String> getRegExGroups(String evalString, String regEx) {
        Vector<String> regExGroupValues = new Vector<String>();
        Pattern pat = null;
        Matcher matcher = null;
        String matchedString = null;

        if (Utility.isNullOrEmpty(evalString) || Utility.isNullOrEmpty(regEx)) {
            throw new RuntimeException("Either evalString or regEx is null.  Both of these fields must be present.");
        }

        pat = Pattern.compile(regEx);
        matcher = pat.matcher(evalString);

        while (matcher.find()) {
            int matchedGroups = matcher.groupCount();
            for (int i = 0; i < matchedGroups; i++) {
                matchedString = matcher.group(i + 1);
                regExGroupValues.add(matchedString);
            }
        }

        if (regExGroupValues.isEmpty()) {
            return null;
        }

        return regExGroupValues;
    }


    public static boolean containsRegEx(String evalString, List<String> regExList, boolean matchAll) {
        boolean containsRegEx = false;
        String regEx = null;

        if (Utility.isNullOrEmpty(evalString) || Utility.isNullOrEmpty(regExList)) {
            throw new RuntimeException(
                            "Either evalString or regEx List is null.  Both of these fields must be present.");
        }

        for (int i = 0; i < regExList.size(); i++) {
            regEx = (String) regExList.get(i);

            if (containsRegEx(evalString, regEx)) {
                containsRegEx = true;

                // if andCondition == false (i.e. only needs 1 of the regEx's to match), then the condition is
                // met, so break out of the loop and return true.
                if (!matchAll) {
                    break;
                }
            } else {
                if (matchAll) {
                    // if andCondition == true (i.e. ALL of the regEx's have to match), then if one doesn't match,
                    // break out of the loop and return false;
                    containsRegEx = false;
                    break;
                }
            }
        }

        return containsRegEx;
    }

    public static boolean containsRegEx(String evalString, String regEx) {
        Pattern pat = null;
        Matcher matcher = null;

        if (Utility.isNullOrEmpty(evalString) || Utility.isNullOrEmpty(regEx)) {
            throw new RuntimeException("Either evalString or regEx is null.  Both of these fields must be present.");
        }

        pat = Pattern.compile(regEx);
        matcher = pat.matcher(evalString);

        return matcher.find();
    }

    /**
     * This method will take a string and split it into a vector of strings based on a String delimiter.
     *
     * For example, given a string of "this:that:and the other" and a delimiter of ":", this method will yeild the
     * following: <code>
     *     Vector Index        Value
     *     ------------        -----
     *           0             this
     *           1             that
     *           2             and the other
     * </code>
     *
     * If the delimiter was not found in the string, this method will return a Vector with only 1 value: the entire
     * original string.
     *
     * @param -
     *            pString - the string to be split
     * @param -
     *            pDelim - the delimiter to use
     * @return - Vector - list of split values.
     */
    public static List<String> splitTokenStrings(String pString, String pDelim) {
        Vector<String> pRetVector = new Vector<String>();

        String[] result = pString.split(pDelim);
        for (int x = 0; x < result.length; x++) {
            pRetVector.add(result[x]);
        }

        return pRetVector;
    }
    /**
     * This method will take a string and split it into a vector of strings based on a String delimiter.
     *
     * For example, given a string of "fname=john;lname=doe;age=34;" and a delimiter of ";" and the nameValueDelim of "=", this 
     * method will yeild the following Map: <code>
     *     Map Name   Map Value
     *     --------   ---------
     *      fname       john
     *      lname       doe
     *      age         34
     * </code>
     *
     * If the delimiter was not found in the string, this method will return a Vector with only 1 value: the entire
     * original string.
     *
     * @param -
     *            pString - the string to be split
     * @param -
     *            pDelim - the delimiter to use
     * @return - Vector - list of split values.
     */
    public static Map<String, String> splitTokenStringsNameValuePair(String string, String delim, String nameValueDelim) {
        Map<String, String> retMap = new Hashtable<String, String>();
        String[] result = null;
        
        result = string.split(delim);
        
        for (int x = 0; x < result.length; x++) {
        	String resultString = null;
        	
        	resultString = result[x];
        	
        	String[] nameValueArray = resultString.split(nameValueDelim);
        	
        	if (nameValueArray.length == 1) {
        		retMap.put(nameValueArray[0], nameValueArray[0]);
        	}
        	else {retMap.put(nameValueArray[0], nameValueArray[1]);
        	}
        }

        return retMap;
    }

    public static String padString(String padString$, int totalLines$, boolean padRight$) {
        return padString(padString$, null, totalLines$, padRight$);
    }

    public static String padString(String padString$, Character padChars$, int totalLines$, boolean padRight$) {
        Character padChars = null;

        if (padChars$ == null) {
            padChars = Character.valueOf(' ');
        } else {
            padChars = padChars$;
        }

        if (padString$ == null) {
            return null;
        }

        int stringLength = padString$.length();
        int padLength = totalLines$ - stringLength;
        String retString = padString$;

        if (stringLength > totalLines$ || padLength <= 0) {
            padLength = 1;
        }

        for (int i = 0; i < padLength; i++) {
            if (padRight$) {
                retString += padChars;
            } else {
                retString = padChars + retString;
            }
        }

        return retString;
    }

    public static String padString(String pString, String pPadString) {
        StringBuffer vSBuf = new StringBuffer();
        String vRetString = null;

        List<String> vStringList = StringUtil.splitTokenStrings(pString, "\n");

        for (int i = 0; i < vStringList.size(); i++) {
            vSBuf.append(pPadString + ((String) vStringList.get(i)));
            vSBuf.append(i == (vStringList.size() - 1) ? "\n" : "");
        }

        vRetString = vSBuf.toString();

        return vRetString;
    }

    /**
     * @param string
     * @return
     */
    public static String lowerFirstCharacter(String string) {
        String retString = null;
        char firstCharacter;

        firstCharacter = string.charAt(0);
        firstCharacter = Character.toUpperCase(firstCharacter);

        retString = firstCharacter + string.substring(1);

        return retString;
    }

    public static String trimNewline(String string) {
        String retString = null;
        String newLine = (String) System.getProperty("line.separator");

        retString = string.replaceAll("\n", "");

        return retString;
    }

}
