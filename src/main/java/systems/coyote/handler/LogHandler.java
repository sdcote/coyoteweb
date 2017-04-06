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
package systems.coyote.handler;

import java.util.Enumeration;
import java.util.Map;

import coyote.commons.StringUtil;
import coyote.commons.network.http.IHTTPSession;
import coyote.commons.network.http.Response;
import coyote.commons.network.http.Status;
import coyote.commons.network.http.auth.Auth;
import coyote.commons.network.http.handler.UriResource;
import coyote.commons.network.http.handler.UriResponder;
import coyote.dataframe.DataFrame;
import coyote.loader.cfg.Config;
import coyote.loader.log.LogTool;
import coyote.loader.log.Logger;
import systems.coyote.WebServer;


/**
 * Access logs on the system
 * offset = number of bytes from the beginning of the log
 * search = regex for a pattern to search for in the logs. The results will be complete lines of data containing the pattern (for each line, if line.matchesXXX, send)
 * limit = stop after limit bytes of data (default is 4096 bytes)
 * 
 * Configuration:
 * "/api/log/" : { "Class" : "systems.coyote.handler.LogHandler" },
 * "/api/log/:name" : { "Class" : "systems.coyote.handler.LogHandler" },
 * "/api/log/:name/:type" : { "Class" : "systems.coyote.handler.LogHandler" },
 * 
 * API Overview:
 * get / profile of the logging system
 *       name of each logger and its details
 * 
 * get /name return the contents of that logs target
 * The response will contain a chunk of the log output, as well as the {@code X-Log-Size} header that represents the bytes offset (of the raw log file). This is the number you want to use as the {@code offset} parameter for the next call.
 * get /name/text?offset=0 (raw text of the file)
 * get /name/html?offset=0 (if you want HTML that can be put into <pre> tag.)
 * 
 * Controlling Loggers and Logging by setting attributes:
 * put / change global attributes of the logging system (e.g. log mask) - use GET / to see what can be set
 * put /name change the attribute of the logger (apply the name value pair of the parameters) - use GET / to see what can be set
 * 
 * post / create a logger with the given (JSON) configuration
 * 
 * delete /name  cycle the log target, archiving the existing data and starting with a new log
 * 
 * Auth annotation ensures only authenticated users can access this handler
 */
@Auth
public class LogHandler extends AbstractJsonHandler implements UriResponder {

  /**
   * 
   */
  @Override
  public Response get( UriResource uriResource, Map<String, String> urlParams, IHTTPSession session ) {
    WebServer loader = uriResource.initParameter( 0, WebServer.class );
    Config config = uriResource.initParameter( 1, Config.class );
    setFormattingJson( true );

    // Get the command from the URL parameters specified when we were registered with the router 
    String name = urlParams.get( "name" );
    String type = urlParams.get( "type" );

    if ( StringUtil.isBlank( name ) ) {
      DataFrame loggers = new DataFrame();
      for ( Enumeration<String> e = LogTool.getLoggerNames(); e.hasMoreElements(); ) {
        String loggername = e.nextElement();
        Logger logger = LogTool.getLogger( loggername );
        loggers.add( loggername, logger.getConfig() );
      }
      results.add( "Loggers", loggers );
    } else {
      Logger logger = LogTool.getLogger( name );
      if( logger != null ){
      if ( "html".equalsIgnoreCase( type ) ) {

      } else {

      }
      } else {
        setStatus(Status.NOT_FOUND);
      }
    }

    return Response.createFixedLengthResponse( super.getStatus(), super.getMimeType(), super.getText() );
  }

  /**
   * @param status the status to set in this handler
   */
  private void setStatus( IStatus status ) {
    super.st
  }

}
