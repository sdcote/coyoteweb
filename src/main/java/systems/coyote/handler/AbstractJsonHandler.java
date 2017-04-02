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

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import coyote.commons.network.MimeType;
import coyote.commons.network.http.IHTTPSession;
import coyote.commons.network.http.IStatus;
import coyote.commons.network.http.ResponseException;
import coyote.commons.network.http.Status;
import coyote.commons.network.http.handler.DefaultHandler;
import coyote.commons.network.http.handler.UriResponder;
import coyote.dataframe.DataFrame;
import coyote.dataframe.marshal.JSONMarshaler;


/**
 * This is a common base class for handlers which implement a JSON web service 
 * pattern.
 * 
 * <p>At the core is a {@code results} DataFrame used to hold the results of 
 * processing. Later, the {@link #getText()} can be called to retrieve the 
 * JSON formatted data contained therein. 
 */
public abstract class AbstractJsonHandler extends DefaultHandler implements UriResponder {

  protected final DataFrame results = new DataFrame();
  private boolean formattingJson = false;




  /**
   * @return the results DataFrame into which the results of processing is to 
   *         be placed for later marshaling into JSON text when the {@link 
   *         #getText()} method is called.
   */
  public DataFrame getResults() {
    return results;
  }




  /**
   * @return true if we are formatting JSON, false (default) if not.
   */
  public boolean isFormattingJson() {
    return formattingJson;
  }




  /**
   * @param flag true to cause getText() to return formatted JSON, false 
   *        (default) to represent it in compressed format.
   */
  public void setFormattingJson( boolean flag ) {
    this.formattingJson = flag;
  }




  /**
   * Parse the body of the request into parameters and or a list of named file 
   * chunks.
   * 
   * @param session the session containing the request
   * 
   * @return a list of file chunks, may be empty, but never null.
   * 
   * @throws IOException if there are problems reading the request stream
   * @throws ResponseException if there a logical HTTP issues with the format 
   *         or encoding of the body 
   */
  public Map<String, String> parseBody( final IHTTPSession session ) throws IOException, ResponseException {
    final Map<String, String> files = new HashMap<String, String>();
    session.parseBody( files );
    return files;
  }




  @Override
  public IStatus getStatus() {
    return Status.OK;
  }




  @Override
  public String getText() {
    if ( formattingJson )
      return JSONMarshaler.toFormattedString( results );
    else
      return JSONMarshaler.marshal( results );
  }




  @Override
  public String getMimeType() {
    return MimeType.JSON.getType();
  }

}
