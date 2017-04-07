/*
 * Copyright (c) 2004 Stephan D. Cote' - All rights reserved.
 * 
 * This program and the accompanying materials are made available under the 
 * terms of the MIT License which accompanies this distribution, and is 
 * available at http://creativecommons.org/licenses/MIT/
 *
 * Contributors:
 *   Stephan D. Cote 
 */
package systems.coyote.responder;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Map;

import coyote.commons.network.MimeType;
import coyote.commons.network.http.HTTPD;
import coyote.commons.network.http.IHTTPSession;
import coyote.commons.network.http.IStatus;
import coyote.commons.network.http.Response;
import coyote.commons.network.http.Status;
import coyote.commons.network.http.responder.DefaultResponder;
import coyote.commons.network.http.responder.Error404Responder;
import coyote.commons.network.http.responder.HTTPDRouter;
import coyote.commons.network.http.responder.Resource;
import coyote.loader.cfg.Config;
import coyote.loader.log.Log;


/**
 * Generic handler to retrieve the requested page from a file root of a file 
 * system.
 * 
 * <p>This allows for serving from the file system like a regular web server.
 */
public class FileResponder extends DefaultResponder {

  private static final String ROOT = "Root";
  private static final String DEFAULT_ROOT = "content";




  protected BufferedInputStream fileToInputStream( final File fileOrdirectory ) throws IOException {
    return new BufferedInputStream( new FileInputStream( fileOrdirectory ) );
  }




  @Override
  public Response get( final Resource resource, final Map<String, String> urlParams, final IHTTPSession session ) {
    // WebServer loader = uriResource.initParameter( 0, WebServer.class );
    Config config = resource.initParameter( 1, Config.class );

    // Retrieve the base directory in the classpath for our content
    String parentdirectory = config.getString( ROOT );
    File docroot = new File( parentdirectory );

    final String baseUri = resource.getUri();
    String realUri = HTTPDRouter.normalizeUri( session.getUri() );
    for ( int index = 0; index < Math.min( baseUri.length(), realUri.length() ); index++ ) {
      if ( baseUri.charAt( index ) != realUri.charAt( index ) ) {
        realUri = HTTPDRouter.normalizeUri( realUri.substring( index ) );
        break;
      }
    }

    File requestedFile = new File( docroot, realUri );

    // if they asked for a directory, look for an index file
    if ( requestedFile.isDirectory() ) {
      requestedFile = new File( requestedFile, "index.html" );
      // if that does not exist, look for the DOS version of the index file
      if ( !requestedFile.exists() ) {
        requestedFile = new File( requestedFile.getParentFile(), "index.htm" );
      }
    }

    // if the file does not exist or is not a file...
    if ( !requestedFile.exists() || !requestedFile.isFile() ) {
      Log.append( HTTPD.EVENT, "404 NOT FOUND - '" + realUri + "' LOCAL: " + requestedFile.getAbsolutePath() );
      return new Error404Responder().get( resource, urlParams, session );
    } else {

      // return the found file
      try {
        return Response.createChunkedResponse( getStatus(), HTTPD.getMimeTypeForFile( requestedFile.getName() ), fileToInputStream( requestedFile ) );
      } catch ( final IOException ioe ) {
        return Response.createFixedLengthResponse( Status.REQUEST_TIMEOUT, MimeType.TEXT.getType(), null );
      }
    }
  }




  @Override
  public String getMimeType() {
    throw new IllegalStateException( "This method should not be called" );
  }




  @Override
  public IStatus getStatus() {
    return Status.OK;
  }




  @Override
  public String getText() {
    throw new IllegalStateException( "This method should not be called" );
  }
}