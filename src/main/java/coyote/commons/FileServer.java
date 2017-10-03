/*
 * Copyright (c) 2017 Stephan D. Cote' - All rights reserved.
 * 
 * This program and the accompanying materials are made available under the 
 * terms of the MIT License which accompanies this distribution, and is 
 * available at http://creativecommons.org/licenses/MIT/
 */
package coyote.commons;

import java.io.File;

import coyote.loader.cfg.Config;
import coyote.loader.cfg.ConfigurationException;
import coyote.loader.log.Log;
import coyote.responder.FileResponder;


/**
 * This is a specific type of web server designed to serve files from the file 
 * system.
 * 
 * <p>The only difference betwen this and a web server, is this configures the 
 * web server to serve from a document root and requires only minimal 
 * configuration.
 */
public class FileServer extends WebServer {

  /**
   * @see coyote.commons.WebServer#configure(coyote.loader.cfg.Config)
   */
  @Override
  public void configure(Config config) throws ConfigurationException {
    super.configure(config);

    if (server.getMappings().size() == 0 && config != null) {
      // look for a document root configuration parameter
      // If no root, use the current working directory

      boolean listFiles = true;
      try {
        if (config.containsIgnoreCase(FileResponder.LIST_FILES_TAG)) {
          listFiles = config.getBoolean(FileResponder.LIST_FILES_TAG);
        }
      } catch (Exception e) {
        throw new ConfigurationException("Invalid boolean value for '" + FileResponder.LIST_FILES_TAG + "' configuration attribute: " + e.getMessage());
      }

      String documentRoot = System.getProperty("user.dir");
      if (config.containsIgnoreCase(FileResponder.ROOT_TAG)) {
        documentRoot = config.getString(FileResponder.ROOT_TAG);
      }
      File root = new File(documentRoot);
      if (root.exists()) {
        if (root.isDirectory()) {
          if (root.canRead()) {
            Log.debug("Using a document root of '" + root.getAbsolutePath() + "'");
          } else {
            throw new ConfigurationException("The document root of '" + documentRoot + "' is not readable");
          }
        } else {
          throw new ConfigurationException("The document root of '" + documentRoot + "' is not a directory");
        }
      } else {
        throw new ConfigurationException("The document root of '" + documentRoot + "' does not exist");
      }

      Config cfg = new Config();
      cfg.set(FileResponder.ROOT_TAG, root.getAbsolutePath());
      cfg.set(FileResponder.LIST_FILES_TAG, listFiles);

      server.addRoute("/", FileResponder.class, this, cfg);
      server.addRoute("/(.)+", FileResponder.class, this, cfg);
    }

    // If there are no ACL rules, default to allow everything.
    if (server.getIpAcl().size() == 0) {
      server.getIpAcl().setDefaultAllow(true);
    }
    Log.info("Access Control List: "+server.getIpAcl().toString());
  }

}
