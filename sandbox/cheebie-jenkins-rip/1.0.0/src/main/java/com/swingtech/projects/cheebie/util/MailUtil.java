/*
 * SwingTech Software - http://cooksarm.sourceforge.net/
 * 
 * Copyright (C) 2011 Joe Rice All rights reserved.
 * 
 * SwingTech Cooks Arm is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by the Free
 * Software Foundation; either version 3 of the License, or (at your option) any
 * later version.
 * 
 * SwingTech Cooks Arm is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * SwingTech Cooks Arm; If not, see <http://www.gnu.org/licenses/>.
 */
package com.swingtech.projects.cheebie.util;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 * DOCME
 * 
 * @author Joe Rice
 * 
 */
public class MailUtil {
    static Properties props = new Properties();

    static {
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.socketFactory.class",
                "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", "465");
    }

    public static void sendEmail(final String aFromEmailAddr, final String aToEmailAddr,
            final String aSubject, final String aBody)
    {
        Session session = null;
        session = Session.getDefaultInstance(props,
                new javax.mail.Authenticator() {
                    @Override
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication("jizzoerice@gmail.com", "!amdaman01");
                    }
                });

        try {
            final Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(aFromEmailAddr));
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(aToEmailAddr));
            message.setSubject(aSubject);
            message.setText(aBody);

            Transport.send(message);

            System.out.println("Done");
        }
        catch (final MessagingException e) {
            System.out.println("WARNING!!!!  tried unsucessfully to send email.  Details:  from:  " + aFromEmailAddr + ".  to:  " + aToEmailAddr + ".  subject:  " + aSubject + ".  body:  " + aBody);
        }
    }

    public static void main(final String[] args) {
        MailUtil.sendEmail("jizzoerice@gmail.com", "jizzoerice@hotmail.com", "New Message left for Grandma", "Here are the details.  ");
    }
}