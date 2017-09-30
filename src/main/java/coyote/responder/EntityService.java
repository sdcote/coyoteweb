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
package coyote.responder;

import java.util.Map;

import coyote.commons.WebServer;
import coyote.commons.network.http.IHTTPSession;
import coyote.commons.network.http.Response;
import coyote.commons.network.http.Status;
import coyote.commons.network.http.responder.Resource;
import coyote.datastore.EntityStore;
import coyote.loader.cfg.Config;


/**
 *
 */
public class EntityService extends AbstractJsonResponder {

  /**
   * 
   */
  public EntityService() {
    // TODO Auto-generated constructor stub
  }




  /**
   * @see coyote.responder.AbstractJsonResponder#get(coyote.commons.network.http.responder.Resource, java.util.Map, coyote.commons.network.http.IHTTPSession)
   */
  @Override
  public Response get( final Resource resource, final Map<String, String> urlParams, final IHTTPSession session ) {
    WebServer loader = resource.initParameter( 0, WebServer.class );
    Config config = resource.initParameter( 1, Config.class );

    // the name to which our datasource is mapped in the context
    String datasource = config.getString( "DataSource" );
    
    EntityStore store = (EntityStore)loader.getContext().get( datasource );
    
    
    // Get the command from the URL parameters specified when we were registered with the router 
    String key = urlParams.get( "key" );
    
    
    
    return Response.createFixedLengthResponse( Status.METHOD_NOT_ALLOWED, getMimeType(), METHOD_NOT_ALLOWED.toString() );
  }




  /**
   * @see coyote.responder.AbstractJsonResponder#delete(coyote.commons.network.http.responder.Resource, java.util.Map, coyote.commons.network.http.IHTTPSession)
   */
  @Override
  public Response delete( final Resource resource, final Map<String, String> urlParams, final IHTTPSession session ) {
    return Response.createFixedLengthResponse( Status.METHOD_NOT_ALLOWED, getMimeType(), METHOD_NOT_ALLOWED.toString() );
  }




  /**
   * @see coyote.responder.AbstractJsonResponder#post(coyote.commons.network.http.responder.Resource, java.util.Map, coyote.commons.network.http.IHTTPSession)
   */
  @Override
  public Response post( final Resource resource, final Map<String, String> urlParams, final IHTTPSession session ) {
    return Response.createFixedLengthResponse( Status.METHOD_NOT_ALLOWED, getMimeType(), METHOD_NOT_ALLOWED.toString() );
  }

}
