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
package coyote.datastore;

/**
 * Navidations are links to things grouped by logical names, contexts and 
 * protected by groups.
 * 
 * <p>This data store is intended to enable any web page to query menu items 
 * for a particular identity so as to create a menu of links within the page. 
 * Scripts in the page can make web service calls to a responder using this 
 * store to retrive menu items by thier logical name (e.g. sidebar, header, 
 * navbar, etc.) and build the menu according to the pages style options and 
 * formatting. This allows all presentation logic to be containd in the web 
 * page and not creep into the service tier. Web services will not have to 
 * change just because the presentation logic does. Also, the service tier can 
 * support multiple client styles simultaneously. 
 */
public interface NavigationStore {

}
