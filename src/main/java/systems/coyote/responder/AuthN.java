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

import coyote.commons.WebServer;
import coyote.commons.network.http.IHTTPSession;
import coyote.commons.network.http.Response;
import coyote.commons.network.http.Status;
import coyote.commons.network.http.responder.Resource;
import coyote.loader.cfg.Config;


/**
 * Return a session identifer based on the credentials provided.
 * 
 * <p>This is a complete separation of logic from presentation. Any component 
 * can make a call to this server and obtain a session identifier based on the 
 * credentials presented.
 */
public class AuthN extends AbstractJsonResponder {

  /**
   * 
   */
  public AuthN() {
    // TODO Auto-generated constructor stub
  }




  /**
   * Return a session identifier based on the contents of the request.
   * 
   * @see systems.coyote.responder.AbstractJsonResponder#get(coyote.commons.network.http.responder.Resource, java.util.Map, coyote.commons.network.http.IHTTPSession)
   */
  @Override
  public Response get( final Resource resource, final Map<String, String> urlParams, final IHTTPSession session ) {
    WebServer loader = resource.initParameter( 0, WebServer.class );
    Config config = resource.initParameter( 1, Config.class );

    return Response.createFixedLengthResponse( Status.METHOD_NOT_ALLOWED, getMimeType(), METHOD_NOT_ALLOWED.toString() );
  }
  
}
