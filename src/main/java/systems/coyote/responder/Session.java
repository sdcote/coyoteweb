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

import java.util.Map;

import coyote.commons.network.http.IHTTPSession;
import coyote.commons.network.http.Response;
import coyote.commons.network.http.Status;
import coyote.commons.network.http.responder.Resource;


/**
 * 
 */
public class Session extends AbstractJsonResponder {

  /**
   * Components can post any data to identified sessions.
   * 
   * <p>Sessions are created on successful authentication (AuthN).
   * 
   * <p>Other components can use data stored in a session to determine what to do next. 
   */
  public Session() {
    // TODO Auto-generated constructor stub
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
