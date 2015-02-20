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
package com.swingtech.template.library.core;

import com.swingtech.template.webapp.core.SimpleClass;

import junit.framework.Assert;

import org.junit.Test;

/**
 * @DOCME
 *
 * @author wdpro_000
 *
 */
public class SimpleClassTest {

    /**
     * Test method for
     * {@link com.swingtech.template.webapp.core.SimpleClass#returnHelloWorld()}
     * .
     */
    @Test
    public void testReturnHelloWorld() {
        SimpleClass simpleClass = new SimpleClass();
        String text = null;

        text = simpleClass.returnHelloWorld();

        Assert.assertEquals("String does not equal hello.  how odd.", "hello", text);
    }

}
