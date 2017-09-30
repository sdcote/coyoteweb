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
package coyote.datastore.msql;

import coyote.datastore.AbstractStore;
import coyote.datastore.EntityStore;


/**
 * A component which provides EntityStore functions backed with a Microsoft 
 * SQL Server database.
 * 
 * <p>This managed component creates a connection to the database and binds a 
 * reference to itself in the Loader Context to be used by other components in 
 * the runtime.
 * 
 * <p>It is thread safe, synchronized on the instance of this component.
 */
public class MsqlEntityStore extends AbstractStore implements EntityStore {

  /**
   * 
   */
  public MsqlEntityStore() {
    // TODO Auto-generated constructor stub
  }

}
