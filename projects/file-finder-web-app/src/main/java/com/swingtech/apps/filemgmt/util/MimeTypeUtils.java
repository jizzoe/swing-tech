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
package com.swingtech.apps.filemgmt.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.FilenameUtils;
import org.apache.tika.Tika;

/**
 * @DOCME
 *
 * @author splas_000
 *
 */
public class MimeTypeUtils {
    private static final Tika tika = new Tika();
    private static Map<String, String> MimeMap;
    static {
        MimeMap = new HashMap<String, String>();
        MimeMap.put("mp4", "video/mp4");
        MimeMap.put("mp3", "audio/mp3");
        MimeMap.put("flv", "video/flv");
        MimeMap.put("webm", "video/webm");
        MimeMap.put("", "video/mp4");
    }

    public static String getMimeType(String extension) {
        if (extension.isEmpty())
            return "application/octet-stream";

        if (MimeMap.containsKey(extension)) {
            return MimeMap.get(extension);
        }
        else {
            return "unknown/" + extension;
        }
    }

    // https://odoepner.wordpress.com/2013/07/29/transparently-improve-java-7-mime-type-recognition-with-apache-tika/
    public static String probeContentType(Path file) throws IOException {
        String mimetype = Files.probeContentType(file);
        if (mimetype != null)
            return mimetype;

        mimetype = tika.detect(file.toFile());
        if (mimetype != null)
            return mimetype;

        return getMimeType(FilenameUtils.getExtension(String.valueOf(file.getFileName())));
    }
}
