/**
 * Licensed to MKS Group under one or more contributor license
 * agreements. See the NOTICE file distributed with this work
 * for additional information regarding copyright ownership.
 * MKS Group licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file
 * except in compliance with the License. You may obtain a
 * copy of the License at:
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package mks.myworkspace.cvhub.common;

import java.io.File;
import java.io.IOException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import lombok.extern.slf4j.Slf4j;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

/**
 * Utility class for common XML-related operations.
 * 
 * @author Thach Ngoc Le (ThachLN@mks.com.vn)
 */
@Slf4j
public class AppUtility {

    /**
     * Parse a XML file into a Document object.
     * 
     * @param xmlFile The XML file to be parsed.
     * @return Document object or null if there is an error during parsing.
     */
    public static Document parseXML(File xmlFile) {
        if (xmlFile == null || !xmlFile.exists()) {
            log.error("XML file is null or does not exist.");
            return null;
        }

        DocumentBuilder db = createDocumentBuilder();
        if (db == null) {
            return null;
        }

        return parseWithBuilder(db, xmlFile);
    }

    /**
     * Creates a new DocumentBuilder instance.
     * 
     * @return A DocumentBuilder instance or null if creation fails.
     */
    private static DocumentBuilder createDocumentBuilder() {
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            return dbf.newDocumentBuilder();
        } catch (ParserConfigurationException ex) {
            log.error("Could not create DocumentBuilder.", ex);
            return null;
        }
    }

    /**
     * Parse the XML file using the provided DocumentBuilder.
     * 
     * @param db       The DocumentBuilder instance.
     * @param xmlFile  The XML file to be parsed.
     * @return Document object or null if an error occurs.
     */
    private static Document parseWithBuilder(DocumentBuilder db, File xmlFile) {
        try {
            log.info("Parsing XML file: {}", xmlFile.getAbsolutePath());
            return db.parse(xmlFile);
        } catch (SAXException ex) {
            log.error("Error during XML parsing.", ex);
        } catch (IOException ex) {
            log.error("Error opening or reading the XML file.", ex);
        }
        return null;
    }
}
