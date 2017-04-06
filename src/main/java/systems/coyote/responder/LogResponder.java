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
package systems.coyote.responder;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URI;
import java.util.Enumeration;
import java.util.Map;

import coyote.commons.StringUtil;
import coyote.commons.UriUtil;
import coyote.commons.network.MimeType;
import coyote.commons.network.http.HTTPD;
import coyote.commons.network.http.IHTTPSession;
import coyote.commons.network.http.Response;
import coyote.commons.network.http.Status;
import coyote.commons.network.http.auth.Auth;
import coyote.commons.network.http.responder.Responder;
import coyote.commons.network.http.responder.UriResource;
import coyote.dataframe.DataFrame;
import coyote.loader.cfg.Config;
import coyote.loader.log.Log;
import coyote.loader.log.LogTool;
import coyote.loader.log.Logger;
import systems.coyote.WebServer;


/**
 * Access logs on the system
 * offset = number of bytes from the beginning of the log
 * TODO: search = regex for a pattern to search for in the logs. The results will be complete lines of data containing the pattern (for each line, if line.matchesXXX, send)
 * TODO: parameters can be combined, search starting from and offset,
 * 
 * Configuration:
 * "/api/log/" : { "Class" : "systems.coyote.handler.LogHandler" },
 * "/api/log/:name" : { "Class" : "systems.coyote.handler.LogHandler" },
 * 
 * API Overview:
 * get / profile of the logging system
 *       name of each logger and its details
 * 
 * get /name return the contents of that logs target
 * The response will contain a chunk of the log output, as well as the {@code X-Log-Size} header that represents the bytes offset (of the raw log file). This is the number you want to use as the {@code offset} parameter for the next call.
 * get /name?offset=0 (raw text of the file)
 * 
 * TODO: Controlling Loggers and Logging by setting attributes:
 * TODO: put / change global attributes of the logging system (e.g. log mask) - use GET / to see what can be set
 * TODO: put /name change the attribute of the logger (apply the name value pair of the parameters) - use GET / to see what can be set
 * TODO: post / create a logger with the given (JSON) configuration
 * TODO: delete /name  cycle the log target, archiving the existing data and starting with a new log
 * 
 * Auth annotation ensures only authenticated users can access this handler
 */
@Auth
public class LogResponder extends AbstractJsonResponder implements Responder {
  private static final String HDR_LOG_SIZE = "X-Log-Size";
  private static final String OFFSET = "offset";
  private static final long DEFAULT_OFFSET_FROM_END = 1024;
  private long offset = 0;




  /**
   * 
   */
  @Override
  public Response get( UriResource uriResource, Map<String, String> urlParams, IHTTPSession session ) {
    WebServer loader = uriResource.initParameter( 0, WebServer.class );
    Config config = uriResource.initParameter( 1, Config.class );
    setFormattingJson( true );

    // Get the name of the logger from the URL parameters specified when we were registered with the router 
    String name = urlParams.get( "name" );

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
      if ( logger != null ) {
        String position = session.getParms().get( OFFSET );
        if ( StringUtil.isNotBlank( position ) ) {
          try {
            offset = Long.parseLong( position );
          } catch ( NumberFormatException e ) {
            results.set( "error", "offset is not a valid number" );
            setStatus( Status.BAD_REQUEST );
          }
        }
        return textResponse( session, logger, offset );
      } else {
        results.set( "error", "Logger not found with that name" );
        setStatus( Status.NOT_FOUND );
      }
    }

    return Response.createFixedLengthResponse( getStatus(), getMimeType(), getText() );
  }




  /**
   * @param session the session requesting the response
   * @param logger the logger requested
   * @param offset number of bytes to skip before returning the log data
   * @return
   */
  private Response textResponse( IHTTPSession session, Logger logger, long offset ) {
    File log = null;
    URI target = logger.getTarget();
    if ( UriUtil.isFile( target ) ) {
      log = UriUtil.getFile( target );

      if ( log.exists() ) {
        if ( log.isDirectory() ) {
          results.set( "error", "File '" + log + "' exists but is a directory" );
          setStatus( Status.INTERNAL_ERROR );
          return Response.createFixedLengthResponse( getStatus(), getMimeType(), getText() );
        }
        if ( log.canRead() == false ) {
          results.set( "error", "File '" + log + "' cannot be read" );
          setStatus( Status.INTERNAL_ERROR );
          return Response.createFixedLengthResponse( getStatus(), getMimeType(), getText() );
        }
      } else {
        results.set( "error", "File '" + log + "' does not exist" );
        setStatus( Status.INTERNAL_ERROR );
        return Response.createFixedLengthResponse( getStatus(), getMimeType(), getText() );
      }

      long size = log.length();
      session.getResponseHeaders().put( HDR_LOG_SIZE, Long.toString( size ) );

      // sanity checks on offset
      if ( offset >= size ) {
        offset = size - DEFAULT_OFFSET_FROM_END;
      }
      if ( offset < 0 ) {
        offset = 0;
      }

      try {
        InputStream fis = new FileInputStream( log );
        fis.skip( offset );
        return Response.createChunkedResponse( Status.OK, MimeType.TEXT.getType(), fis );
      } catch ( Exception e ) {
        Log.append( HTTPD.EVENT, getClass().getSimpleName() + " error sending '" + log.getAbsolutePath() + "' - " + e.getMessage() );
        results.set( "error", "Problems processing '" + log + "' - " + e.getMessage() );
        setStatus( Status.INTERNAL_ERROR );
        return Response.createFixedLengthResponse( getStatus(), getMimeType(), getText() );
      }
    } else {
      results.set( "error", "Logger target '" + target.toASCIIString() + "' is not a file" );
      setStatus( Status.BAD_REQUEST );
      return Response.createFixedLengthResponse( getStatus(), getMimeType(), getText() );
    }
  }

}
