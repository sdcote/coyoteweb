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

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import coyote.commons.GUID;
import coyote.commons.network.MimeType;
import coyote.commons.network.http.Body;
import coyote.commons.network.http.HTTPD;
import coyote.commons.network.http.IHTTPSession;
import coyote.commons.network.http.Response;
import coyote.commons.network.http.ResponseException;
import coyote.commons.network.http.Status;
import coyote.commons.network.http.responder.Resource;
import coyote.dataframe.DataField;
import coyote.dataframe.DataFrame;
import coyote.dataframe.marshal.JSONMarshaler;
import coyote.dataframe.marshal.MarshalException;
import coyote.loader.log.Log;


/**
 * Sessions store data for an identity while using the system.
 * 
 * <p>This is a complete separation of logic from presentation. Any component 
 * can make a call to this server and obtain a session identifier based on the 
 * credentials presented.
 * 
 * <p>PUT expects a body of named credentials. If an Identity is found with 
 * those credentials, a Session is created and populated with that Identity. 
 * The provided data are expected to be only the name-value pairs of 
 * credentials. Normally these are username and password.
 *
 * <p>Sessions are nothing more than an identified DataFrame in the system. It 
 * may contain any data structure need by the application.
 * 
 * <p>The data store behind the entity only need know of an ID and the data 
 * frame. Some may store it as a JSON string, other use a document data model 
 * common in NoSQL databases. Relational databases use a variety of strategies 
 * to support the data frame.
 */
public class SessionService extends AbstractJsonResponder {
  
  private static final String STATUS = "status";


  /**
   * Components can post any data to identified sessions.
   * 
   * <p>Sessions are created on successful authentication (AuthN).
   * 
   * <p>Other components can use data stored in a session to determine what to do next. 
   */
  public SessionService() {
    // TODO Auto-generated constructor stub
  }




  /**
   * Create a new session using the given credentials.
   * 
   * @see systems.coyote.responder.AbstractJsonResponder#put(coyote.commons.network.http.responder.Resource, java.util.Map, coyote.commons.network.http.IHTTPSession)
   */
  @Override
  public Response put( final Resource resource, final Map<String, String> urlParams, final IHTTPSession session ) {
    Body body = null;
    try {
      body = session.parseBody();

      if ( Log.isLogging( Log.DEBUG_EVENTS ) ) {
        for ( String name : body.keySet() ) {
          String strdata = body.getAsString( name );
          Log.debug( this.getClass().getSimpleName() + " marshaling '" + name + "' type " + body.getEntityType( name ) + " body of '" + strdata.substring( 0, strdata.length() > 500 ? 500 : strdata.length() ) + ( strdata.length() <= 500 ? "'" : " ...'" ) );
        }
      }
    } catch ( IOException | ResponseException e ) {
      Log.append( HTTPD.EVENT, "ERROR: Could not parse body: " + e.getClass().getSimpleName() + " - " + e.getMessage() );
    }

    if ( body != null ) {
      try {
        List<DataFrame> frames = JSONMarshaler.marshal( body.getContent() );
        if ( frames.size() > 0 ) {
          DataFrame frame = frames.get( 0 );
          
          String sessionId = GUID.randomGUID().toString();
          
          // Successful authentication results in just a session identifier being returned
          return Response.createFixedLengthResponse( getStatus(), MimeType.TEXT.getType(), sessionId );
        } else {
          setStatus( Status.BAD_REQUEST );
          results.set( STATUS, "No objects found in request" );
        }
      } catch ( MarshalException e ) {
        setStatus( Status.BAD_REQUEST );
        results.set( STATUS, "Problems parsing body of the request: " + e.getMessage() );
      }
    } else {
      setStatus( Status.BAD_REQUEST );
      results.set( STATUS, "Problems parsing body of the request" );
    }

    // send errors as a JSON response
    return Response.createFixedLengthResponse( getStatus(), getMimeType(), getText() );
  }
    



  /**
   * Get the session object.
   * 
   * @see systems.coyote.responder.AbstractJsonResponder#get(coyote.commons.network.http.responder.Resource, java.util.Map, coyote.commons.network.http.IHTTPSession)
   */
  @Override
  public Response get( final Resource resource, final Map<String, String> urlParams, final IHTTPSession session ) {
    return Response.createFixedLengthResponse( Status.METHOD_NOT_ALLOWED, getMimeType(), METHOD_NOT_ALLOWED.toString() );
  }




  /**
   * Delete the session (e.g. log-out)
   * @see systems.coyote.responder.AbstractJsonResponder#delete(coyote.commons.network.http.responder.Resource, java.util.Map, coyote.commons.network.http.IHTTPSession)
   */
  @Override
  public Response delete( final Resource resource, final Map<String, String> urlParams, final IHTTPSession session ) {
    return Response.createFixedLengthResponse( Status.METHOD_NOT_ALLOWED, getMimeType(), METHOD_NOT_ALLOWED.toString() );
  }




  /**
   * Modify the session by placing data in it.
   * 
   * @see systems.coyote.responder.AbstractJsonResponder#post(coyote.commons.network.http.responder.Resource, java.util.Map, coyote.commons.network.http.IHTTPSession)
   */
  @Override
  public Response post( final Resource resource, final Map<String, String> urlParams, final IHTTPSession session ) {
    return Response.createFixedLengthResponse( Status.METHOD_NOT_ALLOWED, getMimeType(), METHOD_NOT_ALLOWED.toString() );
  }

}
