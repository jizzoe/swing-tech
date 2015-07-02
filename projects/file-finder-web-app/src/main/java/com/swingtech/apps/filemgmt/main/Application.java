/********************************************************
 * FileName - Application.java
 * 
 * (c) Disney. All rights reserved.
 * 
 * $Author: CUSTOMER $ 
 * $Revision: #10 $ 
 * $Change: 816680 $ 
 * $Date: 2011/07/01 $
 ********************************************************/

package com.swingtech.apps.filemgmt.main;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;

/**
 * DOCME - JavaDoc this class:  Application
 * 
 * @author CUSTOMER
 *
 */
@SpringBootApplication
@ComponentScan(value = "com.swingtech.apps.filemgmt.controller")
public class Application {
    public static void main(String[] args) {
        ApplicationContext ctx = SpringApplication.run(Application.class, args);

        System.out.println("\n\n**************************************\n Startup Complete\n**************************************\n\n");
    }
}
