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
package com.swingtech.common.dupfilefinder.util;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.net.URL;
import java.util.Collection;
import java.util.Map;

/**
 * Collection of general-purpose Convenience methods that make programming life easy.  
 *
 * @author jorice
 *
 */
public class Utility {

    /**
     * Convenience method to test if a 'TestObject' is null or empty. NOTE: the check for empty applies only to Strings,
     * Collections, Maps,and Arrays. All other types will test only for NULL.
     * 
     * @param pTestObject -
     *            The object to test for NULL or empty.
     * @return - true if the TestObject is NULL. If the TestObject is a String, Collection, Map, or Array, will also
     *         check to see if the value is empty (not null, but doesn't contain any values). If so, returns true.
     *         Returns false if testObject is NOT NULL and not empty.
     */

    public static boolean isNullOrEmpty(Object pTestObject) {
        // First test for Null
        if (pTestObject == null) {
            return true;
        }

        // Now Test for Empty. Test for Strings, Collections, Maps, and Arrays

        if (pTestObject instanceof String) {
            if ("".equals(((String) pTestObject).trim())) {
                return true;
            } else {
                return false;
            }
        }

        if (pTestObject instanceof Collection) {
            if (((Collection) pTestObject).isEmpty()) {
                return true;
            } else {
                return false;
            }
        }

        if (pTestObject instanceof Map) {
            if (((Map) pTestObject).isEmpty()) {
                return true;
            } else {
                return false;
            }
        }

        if (pTestObject instanceof Object[]) {
            if (((Object[]) pTestObject).length <= 0) {
                return true;
            } else {
                return false;
            }
        }

        if (pTestObject instanceof int[]) {
            if (((int[]) pTestObject).length <= 0) {
                return true;
            } else {
                return false;
            }
        }

        if (pTestObject instanceof char[]) {
            if (((char[]) pTestObject).length <= 0) {
                return true;
            } else {
                return false;
            }
        }

        if (pTestObject instanceof double[]) {
            if (((double[]) pTestObject).length <= 0) {
                return true;
            } else {
                return false;
            }
        }

        if (pTestObject instanceof float[]) {
            if (((float[]) pTestObject).length <= 0) {
                return true;
            } else {
                return false;
            }
        }

        if (pTestObject instanceof byte[]) {
            if (((byte[]) pTestObject).length <= 0) {
                return true;
            } else {
                return false;
            }
        }

        if (pTestObject instanceof long[]) {
            if (((long[]) pTestObject).length <= 0) {
                return true;
            } else {
                return false;
            }
        }

        if (pTestObject instanceof short[]) {
            if (((short[]) pTestObject).length <= 0) {
                return true;
            } else {
                return false;
            }
        }

        // If pTestObject is not a String, Collection, Map, or Array, then don't test
        // for empty. Just return false (since it's not null).
        return false;
    }
    
    public static String readStringFromURL(URL url) {
    	InputStream is = null;
    	
    	try {
			is = url.openStream();
		} catch (IOException e) {
			return "";
		}
    	
    	return readStringFromInputStream(is);
    }

    public static String readStringFromFile(String fileName) {
    	File file = null;
    	StringBuilder contents = new StringBuilder();
    	
    	if (fileName == null) {
    		throw new IllegalArgumentException("fileName or string passed in was NULL.  Must have both these values.");
    	}
    	
    	file = new File(fileName);
    	
    	if (!file.exists()) {
    	    throw new RuntimeException("Could open create file of name:  " + fileName);
    	}
    	
    	//use buffering, reading one line at a time
    	//FileReader always assumes default encoding is OK!
    	 BufferedInputStream input;
		try {
			input = new BufferedInputStream(new FileInputStream(file));
		} catch (FileNotFoundException e) {
			return "";
		}
    	 
    	 return readStringFromInputStream(input);
    }
    
    public static String readStringFromInputStream(InputStream input) {
    	Writer writer = null;

             try {
                 if (input != null) {
                	 try {
	                     writer = new StringWriter();
	          
	                     char[] buffer = new char[1024];
	                     Reader reader = new BufferedReader(
	                             new InputStreamReader(input, "UTF-8"));
	                     int n;
	                     while ((n = reader.read(buffer)) != -1) {
	                         writer.write(buffer, 0, n);
	                     }
                	 }
                     finally {
                         input.close();
                     }
                 }
             }
             catch (IOException ex){
                 ex.printStackTrace();
               }
    
       return writer.toString();
    }
        
    public static void writeStringToFile(String string, String fileName) throws IOException {
    	File file = null;
    	boolean fileCreated = false;
    	
    	if (fileName == null || string == null) {
    		throw new IllegalArgumentException("fileName or string passed in was NULL.  Must have both these values.");
    	}
    	
    	file = new File(fileName);
    	
    	if (!file.exists()) {
    		fileCreated = file.createNewFile();
    		if (!fileCreated) {
    			throw new RuntimeException("Could not create file of name:  " + fileName);
    		}
    	}
    	//use buffering
        Writer output = new BufferedWriter(new FileWriter(file));
        try {
          //FileWriter always assumes default encoding is OK!
          output.write( string );
        }
        finally {
          output.close();
        }
    }

    public static double decimalRound(double value, int roundPlaces) {
    	int temp=(int)((value*Math.pow(10,roundPlaces)));
    	return (((double)temp)/Math.pow(10,roundPlaces));
    }
    
    // END OF CLASS FILE ************************************************************************************
}
