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
package com.swingtech.projects.cheebie.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.Date;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.Properties;
import java.util.Random;
import java.util.StringTokenizer;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.swingtech.projects.cheebie.util.MailUtil;

/**
 * DOCME
 * 
 * @author Joe Rice
 * 
 */
public class GuestBookServlet extends HttpServlet {
    private final Properties connectionProperties = new Properties();

    private Driver driver = null;

    private String driverName = null;

    private String jdbcURL = null;

    private final Random random = new Random();

    /**
     * Provides the servlet with the chance to get runtime configuration values and initialize itself. For a database
     * servlet, you want to grab the driver name, URL, and any connection information. For this example, I assume a
     * driver that requires a user name and password. For an example of more database independent configuration, see
     * Chapter 4.
     * 
     * @param cfg
     *        the servlet configuration information
     * @throws javax.servlet.ServletException
     *         could not load the specified JDBC driver
     */
    @Override
    public void init(final ServletConfig cfg) throws ServletException {
        super.init(cfg);
        {
            String user, pw;

            this.driverName = "com.mysql.jdbc.Driver";
            this.jdbcURL = "jdbc:mysql://localhost:3306/cheebie";
            user = "cheebie_user";
            if (user != null) {
                this.connectionProperties.put("user", user);
            }
            pw = "cheebie";

            if (pw != null) {
                this.connectionProperties.put("password", pw);
            }
            try {
                this.driver = (Driver) Class.forName(this.driverName).newInstance();
            }
            catch (final Exception e) {
                throw new ServletException("Unable to load driver: "
                        + e.getMessage());
            }
        }
    }

    /**
     * Performs the HTTP GET. This is where we print out a form and a random sample of the comments.
     * 
     * @param req
     *        the servlet request information
     * @param res
     *        the servlet response information
     * @throws javax.servlet.ServletException
     *         an error occurred talking to the database
     * @throws java.io.IOException
     *         a socket error occurred
     */
    @Override
    public void doGet(final HttpServletRequest req, final HttpServletResponse res)
            throws ServletException, IOException
    {
        final PrintWriter out = res.getWriter();
        final Locale loc = this.getLocale(req);

        res.setContentType("text/html");
        this.printCommentForm(out, loc);
        this.printComments(out, loc);
    }

    @Override
    public void doPost(final HttpServletRequest req, final HttpServletResponse res)
            throws ServletException, IOException
    {
        final PrintWriter out = res.getWriter();
        final Locale loc = this.getLocale(req);
        String name, email, comment;
        Connection conn = null;
        Exception err = null;
        int id = -1;
        String[] tmp;

        // get the form valuesRr
        tmp = req.getParameterValues("name");
        if (tmp == null || tmp.length != 1) {
            name = null;
        }
        else {
            name = tmp[0];
        }
        tmp = req.getParameterValues("email");
        if (tmp == null || tmp.length != 1) {
            email = null;
        }
        else {
            email = tmp[0];
        }
        tmp = req.getParameterValues("comments");
        if (tmp == null || tmp.length != 1) {
            comment = null;
        }
        else {
            comment = tmp[0];
        }
        res.setContentType("text/html");
        // validate values
        if (name.length() < 1) {
            this.printCommentForm("You must specify a valid name!", out, loc);
            this.printComments(out, loc);
            return;
        }
        if (email != null && !email.isEmpty() && email.length() < 3) {
            this.printCommentForm("You must specify a valid email address!", out, loc);
            this.printComments(out, loc);
            return;
        }
        if (email != null && !email.isEmpty() && email.indexOf("@") < 1) {
            this.printCommentForm("You must specify a valid email address!", out, loc);
            this.printComments(out, loc);
            return;
        }
        if (comment.length() < 1) {
            this.printCommentForm("You left no comments!", out, loc);
            this.printComments(out, loc);
            return;
        }
        try {
            final SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
            final java.util.Date date = new java.util.Date();
            ResultSet result;
            Statement stmt;

            conn = DriverManager.getConnection(this.jdbcURL, this.connectionProperties);
            // remove the "setAutoCommit(false)" line for mSQL or MySQL
            conn.setAutoCommit(false);
            stmt = conn.createStatement();
            // generate a new comment ID
            // more on ID generation in Chapter 4
            result = stmt.executeQuery("SELECT NEXT_SEQ " + "FROM SEQGEN "
                    + "WHERE NAME = 'COMMENT_ID'");
            if (!result.next()) {
                throw new ServletException("Failed to generate id.");
            }
            id = result.getInt(1) + 1;
            stmt.close();
            // closing the statement closes the result
            stmt = conn.createStatement();
            stmt.executeUpdate("UPDATE SEQGEN SET NEXT_SEQ = " + id
                    + " WHERE NAME = 'COMMENT_ID'");
            stmt.close();
            stmt = conn.createStatement();
            comment = this.fixComment(comment);
            stmt.executeUpdate("INSERT INTO COMMENT "
                    + "(COMMENT_ID, EMAIL, NAME, COMMENT, " + "CMT_DATE) "
                    + "VALUES (" + id + ", '" + email + "', '" + name + "', '"
                    + comment + "', '" + fmt.format(date) + "')");
            conn.commit();
            stmt.close();
            this.sendEmail(email, name, fmt.format(date), comment);
        }
        catch (final SQLException e) {
            e.printStackTrace();
            err = e;
        }
        finally {
            if (conn != null) {
                try {
                    conn.close();
                }
                catch (final Exception e) {
                }
            }
        }
        if (err != null) {
            out.println("An error occurred on save: " + err.getMessage());
        }
        else {
            this.printCommentForm(out, loc);
            this.printComments(out, loc);
        }
    }

    protected void sendEmail(final String email, final String name, final String dateFormat, final String comment) {
        final String emailMessageBody = "Here are the details of the message.\n\nFrom:  " + name + "\nEmail Address:  " + email + "\nDate:  " + dateFormat + "\nMessage:  " + comment;
        MailUtil.sendEmail("admin@cheebiejenkinsrip.com", "jizzoerice@gmail.com", "New Message posted on Grandma's site from :  " + name, emailMessageBody);
    }

    /**
     * Find the desired locale from the HTTP header.
     * 
     * @param req
     *        the servlet request from which the header is read
     * @return the locale matching the first accepted language
     */
    private Locale getLocale(final HttpServletRequest req) {
        final String hdr = req.getHeader("Accept-Language");
        StringTokenizer toks;

        if (hdr == null) {
            return Locale.getDefault();
        }
        toks = new StringTokenizer(hdr, ",");
        if (toks.hasMoreTokens()) {
            String lang = toks.nextToken();
            int ind = lang.indexOf(';');
            Locale loc;

            if (ind != -1) {
                lang = lang.substring(0, ind);
            }
            lang = lang.trim();
            ind = lang.indexOf("-");
            if (ind == -1) {
                loc = new Locale(lang, "");
            }
            else {
                loc = new Locale(lang.substring(0, ind), lang
                        .substring(ind + 1));
            }
            return loc;
        }
        return Locale.getDefault();
    }

    @Override
    public String getServletInfo() {
        return "Guest Book Servlet\nFrom Database Programming with JDBC "
                + "and Java";
    }

    private void printCommentForm(final PrintWriter out, final Locale loc) throws IOException
    {
        this.printCommentForm(null, out, loc);
    }

    private void printCommentForm(final String error, final PrintWriter out, final Locale loc)
            throws IOException
    {
        out.println("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">");
        out.println("<html xmlns=\"http://www.w3.org/1999/xhtml\">");
        out.println("<head>");
        out.println("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\" />");
        out.println("<title>Cheebie Jenkins - Rememberences</title>");
        out.println("<style type=\"text/css\">");
        out.println("body {");
        out.println("   background-image: url();");
        out.println("   margin-left: 0px;");
        out.println("   margin-top: 0px;");
        out.println("   margin-right: 0px;");
        out.println("   margin-bottom: 0px;");
        out.println("   background-color: #c0eaf1;");
        out.println("}");
        out.println(".Arial {");
        out.println("   font-family: Arial, Helvetica, sans-serif;");
        out.println("}");
        out.println(".bold {");
        out.println("   font-weight: bold;");
        out.println("   font-size: 20px;");
        out.println("}");
        out.println(".color {");
        out.println("   color: #F00;");
        out.println("}");
        out.println("</style>");
        out.println("<script src=\"SpryAssets/SpryMenuBar.js\" type=\"text/javascript\"></script>");
        out.println("<link href=\"SpryAssets/SpryMenuBarVertical.css\" rel=\"stylesheet\" type=\"text/css\" />");
        out.println("<link href=\"SpryAssets/SpryMenuBarHorizontal.css\" rel=\"stylesheet\" type=\"text/css\" />");
        out.println("</head>");
        out.println("");
        out.println("<body>");
        out.println("");
        out.println("<h1 align=\"center\" class=\"Arial\">Rememberences</h1>");
        out.println("");
        out.println("<table width=\"100%\" border=\"0\">");
        out.println("  <tr>");
        out.println("    <td width=\"21%\" height=\"366\" valign=\"top\"><table width=\"100%\" height=\"215\" border=\"0\">");
        out.println("      <tr>");
        out.println("        <td width=\"71%\" height=\"211\" valign=\"top\">");
        out.println("          <ul id=\"MenuBar1\" class=\"MenuBarVertical\">");
        out.println("            <li><a href=\"index.html\">Home</a></li>");
        out.println("            <li><a href=\"obituary.html\">Obituary</a></li>");
        out.println("");
        out.println("            <li><a href=\"video.html\">Video</a>");
        out.println("<!--");
        out.println("    <ul>");
        out.println("      <li><a href=\"wake.html\">The Wake</a></li>");
        out.println("      <li><a href=\"funeral.html\">Funeral Service</a></li>");
        out.println("      <li><a href=\"burial.html\">Burial</a></li>");
        out.println("    </ul>");
        out.println("-->");
        out.println("            </li>");
        out.println("            <li><a href=\"pictures.html\">Pictures</a></li>");
        out.println("            <li><a href=\"rememberences\">Rememberences</a></li>");
        out.println("          </ul></td>");
        out.println("        </tr>");
        out.println("    </table>      <p align=\"left\">&nbsp;</p></td>");
        out.println("");
        out.println("    <td width=\"59%\" align=\"left\" valign=\"top\"><pre>I thank my God upon every <strong>remembrance</strong> of you");
        out.println("Philippians 1:3");
        out.println("    </pre>");
        out.println("    <p>Let's remember our Mother, Grandmaw, Great Grandmaw, Aunt, Friend, Ms. Cheebie Jenkins.  Please leave thoughts of rememberence.</p>");
        out.println("    ");
        out.println(" <form action=\"/rememberences\" method=\"POST\">");
        if (error != null && !error.trim().isEmpty()) {
            out.println("<span class=\"color\">" + error + "</span>");
        }
        out.println("<table>");
        out.println("<tr>");
        out.println("<td>Name:</td>");
        out.println("<td><input type=\"text\" name=\"name\" size=\"30\"/></td>");
        out.println("<td><input type=\"submit\" value=\"Save\"/></td>");
        out.println("");
        out.println("</tr>");
        out.println("<tr>");
        out.println("<td>Email:</td>");
        out.println("<td><input type=\"text\" name=\"email\" size=\"30\"/></td>");
        out.println("<tr>");
        out.println("<tr>");
        out.println("<td>Comments:</td>");
        out.println("");
        out.println("<tr>");
        out.println("<tr>");
        out.println("<td colspan=\"3\">");
        out.println("<textarea name=\"comments\" cols=\"40\" rows=\"7\">");
        out.println("</textarea></td>");
        out.println("<tr>");
        out.println("</table>");
        out.println("");
        out.println("</form>");
    }

    private void printComments(final PrintWriter out, final Locale loc) throws IOException {
        Connection conn = null;

        try {
            final DateFormat fmt = DateFormat.getDateInstance(DateFormat.FULL, loc);
            ResultSet results;
            Statement stmt;
            int rows, count;

            conn = DriverManager.getConnection(this.jdbcURL, this.connectionProperties);
            stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);
            results = stmt.executeQuery("SELECT NAME, EMAIL, CMT_DATE, "
                    + "COMMENT, COMMENT_ID " + "FROM COMMENT "
                    + "ORDER BY CMT_DATE");

            out.println("    ");
            out.println("<dl>");

            results.last();
            results.next();
            rows = results.getRow();

            if (rows > 0) {
                out.println("<No Results>");
            }

            final int random = this.random.nextInt();

            System.out.println("---> random #:  " + random);

            System.out.println("---> rows:  " + rows);

            results.afterLast();

            count = 0;

            // print up to 5 rows going backwards from the randomly
            // selected row
            while (results.previous()) {
                String name, email, cmt;
                Date date;

                count++;
                name = results.getString(1);
                if (results.wasNull()) {
                    name = "Unknown User";
                }
                email = results.getString(2);
                if (results.wasNull()) {
                    email = "user@host";
                }
                date = results.getDate(3);
                if (results.wasNull()) {
                    date = new Date(new java.util.Date().getTime());
                }
                cmt = results.getString(4);
                if (results.wasNull()) {
                    cmt = "No comment.";
                }

                out.println("<dt><b>" + name + "</b> (" + email + ") on " + fmt.format(date) + "</dt>");
                out.println("<dd>" + cmt + "</dd>");
                out.println("<hr />");
            }
            out.println("</dl>");
            out.println("");
            out.println("");
            out.println("    </td>");
            out.println("    <td width=\"20%\" align=\"center\" valign=\"top\"><img src=\"grandma.jpg\" alt=\"\" width=\"154\" height=\"186\" />      <p align=\"center\">&nbsp;</p></td>");
            out.println("  </tr>");
            out.println("</table>");
            out.println("<h1 align=\"center\" class=\"Arial\">Your Spirit and Memory Lives On!!!</h1>");
            out.println("<script type=\"text/javascript\">");
            out.println("var MenuBar1 = new Spry.Widget.MenuBar(\"MenuBar1\", {imgRight:\"SpryAssets/SpryMenuBarRightHover.gif\"});");
            out.println("var MenuBar2 = new Spry.Widget.MenuBar(\"MenuBar2\", {imgDown:\"SpryAssets/SpryMenuBarDownHover.gif\", imgRight:\"SpryAssets/SpryMenuBarRightHover.gif\"});");
            out.println("</script>");
            out.println("</body>");
            out.println("");
            out.println("</html>");
        }
        catch (final SQLException e) {
            out.println("A database error occurred: " + e.getMessage());
        }
        finally {
            if (conn != null) {
                try {
                    conn.close();
                }
                catch (final SQLException e) {
                }
            }
        }
    }

    /**
     * Removes any XML-sensitive characters from a comment and replaces them with their character entities.
     * 
     * @param cmt
     *        the raw comment
     * @return the XML-safe comment
     */
    private String noXML(final String cmt) {
        final StringBuffer buff = new StringBuffer();

        for (int i = 0; i < cmt.length(); i++) {
            final char c = cmt.charAt(i);

            switch (c)
                {
                    case '<':
                        buff.append("&lt;");
                        break;
                    case '>':
                        buff.append("&gt;");
                        break;
                    case '&':
                        buff.append("&amp;");
                        break;
                    case '"':
                        buff.append("&quot;");
                        break;
                    default:
                        buff.append(c);
                        break;
                }
        }
        return buff.toString();
    }

    /**
     * This method escapes single quotes so that database statements are not messed up.
     * 
     * @param comment
     *        the raw comment
     * @return a comment with any quotes escaped
     */
    private String fixComment(String comment) {
        if (comment.indexOf("'") != -1) {
            String tmp = "";

            for (int i = 0; i < comment.length(); i++) {
                final char c = comment.charAt(i);

                if (c == '\'') {
                    tmp = tmp + "\\'";
                }
                else {
                    tmp = tmp + c;
                }
            }
            comment = tmp;
        }
        return comment;
    }
}
