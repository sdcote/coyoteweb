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
package systems.coyote;

import java.util.Map;

import coyote.commons.network.MimeType;
import coyote.commons.network.http.IHTTPSession;
import coyote.commons.network.http.IStatus;
import coyote.commons.network.http.Response;
import coyote.commons.network.http.Status;
import coyote.commons.network.http.handler.DefaultHandler;
import coyote.commons.network.http.handler.UriResource;
import coyote.loader.cfg.Config;


/**
 * A simple handler for testing
 */
public class TestHandler extends DefaultHandler {
  private String testData = "Hello";




  @Override
  public Response get( final UriResource uriResource, final Map<String, String> urlParams, final IHTTPSession session ) {

    // These initialization parameters always exist
    WebServer loader = uriResource.initParameter( 0, WebServer.class );
    Config config = uriResource.initParameter( 1, Config.class );
    
    // if there are more, then assume the next one is our test data
    if ( uriResource.getInitParameterLength() > 2 ) {
      testData = uriResource.initParameter( 2, String.class );
    }
    
    return Response.createFixedLengthResponse( getStatus(), getMimeType(), getText() );
  }




  @Override
  public String getMimeType() {
    return MimeType.TEXT.getType();
  }




  @Override
  public IStatus getStatus() {
    return Status.OK;
  }




  @Override
  public String getText() {
    return testData;
  }

}