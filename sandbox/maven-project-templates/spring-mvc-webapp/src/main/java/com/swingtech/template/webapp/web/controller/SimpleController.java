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
package com.swingtech.template.webapp.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @DOCME
 *
 * @author wdpro_000
 *
 */
@Controller
public class SimpleController {
    @RequestMapping("/hello")
    public String hello(@RequestParam(value = "name", required = false, defaultValue = "World") String name, Model model) {
        System.out
        .println("\n\n\n\n\n***********************************************************\n\n    HELLO!!!!   \n\n\n******************************************************************\n\n\n\n");
        model.addAttribute("name", name);
        return "simple";
    }
}
