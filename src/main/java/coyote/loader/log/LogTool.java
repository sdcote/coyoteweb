/*
 * Copyright (c) 2017 Stephan D. Cote' - All rights reserved.
 * 
 * This program and the accompanying materials are made available under the 
 * terms of the MIT License which accompanies this distribution, and is 
 * available at http://creativecommons.org/licenses/MIT/
 *
 * Contributors:
 *   Stephan D. Cote 
 *      - Initial concept and implementation
 */
package coyote.loader.log;

import java.util.Enumeration;


/**
 * A public facade to the Log kernel to access some basic data about the 
 * current state of the logging system.
 */
public class LogTool {

  /**
   * @return an enumeration over all the current logger names.
   */
  public static Enumeration<String> getLoggerNames() {
    return LogKernel.getLoggerNames();

  }




  /**
   * Access all the named categories currently registered with the logging
   * subsystem.
   * 
   * <p>This is a good way to discover what categories have been registered by
   * components &quot;behind the scenes&quot;. When used in development trouble 
   * shooting activities, a developer may discover logging categories to enable 
   * and give new insight into the operation of the application.</p>
   *
   * @return An array of category names.
   */
  public static String[] getCategoryNames() {
    return LogKernel.getCategoryNames();
  }




  /**
   * @return The default logger, or null if there is no logger named "default".
   */
  public static Logger getDefaultLogger() {
    return LogKernel.getDefaultLogger();
  }




  /**
   * @return the number of logger in the static collection.
   */
  public static int getLoggerCount() {
    return LogKernel.getLoggerCount();
  }




  /**
   * @return an enumeration over all the current loggers.
   */
  public static Enumeration<Logger> getLoggers() {
    return LogKernel.getLoggers();
  }




  /**
   * Return the Logger object with the given name.
   *
   * @param name The name of the logger to retrieve.
   *
   * @return The reference to the Logger object with the given name or null if 
   *         it does not exist.
   */
  public static Logger getLogger( String name ) {
    return LogKernel.getLogger( name );
  }

}
