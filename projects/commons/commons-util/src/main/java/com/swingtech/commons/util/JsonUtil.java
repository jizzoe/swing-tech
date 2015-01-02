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
package com.swingtech.commons.util;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.joda.JodaModule;

/**
 * DOCME
 *
 * @author jizzo_000
 *
 */
public class JsonUtil {
    private final static ObjectMapper MAPPER = new ObjectMapper();

    static {
        MAPPER.registerModule(new JodaModule());
        MAPPER.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    /**
* DOCME - JavaDoc this constructor: JsonUtil
*
*/
    private JsonUtil() {
        
    }

    public static String marshalObjectToJson(Object obj) {
        try {
            return MAPPER.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error trying to marshall Object to Json", e);
        }
    }

    public static <T> T unmarshalJsonToObject(String json, Class<T> valueType) {
        try {
            return MAPPER.readValue(json, valueType);
        } catch (Exception e) {
            throw new RuntimeException("Error trying to unmarshall Object to Json", e);
        }
    }

    public static <T> T unmarshalJsonToObject(URL jsonRul, Class<T> valueType) {
        try {
            return MAPPER.readValue(jsonRul, valueType);
        } catch (Exception e) {
            throw new RuntimeException("Error trying to marshall json to object", e);
        } 
    }

    public static <T> T unmarshalJsonToObject(File jsonFile, Class<T> valueType) {
        try {
            return MAPPER.readValue(jsonFile, valueType);
        } catch (Exception e) {
            throw new RuntimeException("Error trying to marshall json to object", e);
        } 
    }
}
