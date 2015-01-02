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

import static org.junit.Assert.fail;

import java.util.List;

import junit.framework.Assert;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class StringUtilTest {
	private static final Log logger = LogFactory.getLog(StringUtilTest.class);

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void testGetFirstRegExGroups() {
		String string = "01:02:2012";
		String regex = "([0-9]+):([0-9]+):([0-9]+)";
		
		// Test valid RegEx
		List<String> groups = StringUtil.getFirstRegExGroups(string, regex);

		for (String group : groups) {
			System.out.println(group);
		}
		
		Assert.assertNotNull(groups);
		Assert.assertTrue(!groups.isEmpty());
		Assert.assertEquals(groups.get(0), "01");

		string = "01:02:2012";
		regex = "(([0-9]+):([0-9]+):([0-9]+))";

		groups = StringUtil.getFirstRegExGroups(string, regex);

		for (String group : groups) {
			System.out.println(group);
		}
		
		Assert.assertNotNull(groups);
		Assert.assertTrue(!groups.isEmpty());
		Assert.assertEquals(groups.get(0), "01:02:2012");

		groups = StringUtil.getFirstRegExGroups(string, regex);

		// Test non-matching string
		string = "01-02-2012";
		regex = "([0-9]+):([0-9]+):([0-9]+)";
		
		groups = StringUtil.getFirstRegExGroups(string, regex);

		Assert.assertNull(groups);

		string = null;
		regex = "([0-9]+):([0-9]+):([0-9]+)";
		
		try {
			groups = StringUtil.getFirstRegExGroups(string, regex);
			fail("Should raise an RuntimeException");
		}
		catch(Exception e) {
		}

		string = "01-02-2012";
		regex = null;
		
		try {
			groups = StringUtil.getFirstRegExGroups(string, regex);
			fail("Should raise an RuntimeException");
		}
		catch(Exception e) {
		}
		
	}

	@Test
	public void testGetRegExGroups() {
		String string = "01:02:2012";
		String regex = "([0-9]+):([0-9]+):([0-9]+)";

		// Test Valid RegEx
		List<String> groups = StringUtil.getRegExGroups(string, regex);

		Assert.assertNotNull(groups);
		Assert.assertTrue(!groups.isEmpty());
		Assert.assertEquals(groups.get(0), "01");
		Assert.assertEquals(groups.get(1), "02");
		Assert.assertEquals(groups.get(2), "2012");
		
		string = "01:02:2012";
		regex = "(([0-9]+):([0-9]+):([0-9]+))";
		
		groups = StringUtil.getRegExGroups(string, regex);

		for (String group : groups) {
			System.out.println("group:  " + group);
		}
		Assert.assertNotNull(groups);
		Assert.assertTrue(!groups.isEmpty());
		Assert.assertEquals(groups.get(0), "01:02:2012");
		Assert.assertEquals(groups.get(1), "01");
		Assert.assertEquals(groups.get(2), "02");
		Assert.assertEquals(groups.get(3), "2012");

		// Test non-matching string
		string = "01-02-2012";
		regex = "(([0-9]+):([0-9]+):([0-9]+))";
		
		groups = StringUtil.getRegExGroups(string, regex);

		Assert.assertNull(groups);
	}

	@Test
	public void testContainsRegExStringString() {
		String string = "01:02:2012";
		String regex = "([0-9])+:([0-9]+):([0-9]+)";
		boolean contains = false;
		
		contains = StringUtil.containsRegEx(string, regex);
		Assert.assertTrue(contains);

		string = "01-2012";
		regex = "(([0-9]+):([0-9]+):([0-9]+))";

		contains = StringUtil.containsRegEx(string, regex);
		Assert.assertFalse(contains);
		
		string = "John Doe is Good!!";
		regex = "Doe";

		contains = StringUtil.containsRegEx(string, regex);
		Assert.assertTrue(contains);
		
		string = "John Doe is Good!!";
		regex = "Bad";

		contains = StringUtil.containsRegEx(string, regex);
		Assert.assertFalse(contains);
	}
/*
	@Test
	public void testContainsRegExStringListOfStringBoolean() {
		String string = "01:02:2012";
		fail("Not yet implemented");
	}

	@Test
	public void testPadStringStringIntBoolean() {
		fail("Not yet implemented");
	}

	@Test
	public void testPadStringStringCharacterIntBoolean() {
		fail("Not yet implemented");
	}

	@Test
	public void testPadStringStringString() {
		fail("Not yet implemented");
	}

	@Test
	public void testLowerFirstCharacter() {
		fail("Not yet implemented");
	}

	@Test
	public void testTrimNewline() {
		fail("Not yet implemented");
	}
	*/
}
