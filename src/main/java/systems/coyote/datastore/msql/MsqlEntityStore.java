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
package systems.coyote.datastore.msql;

import coyote.loader.component.AbstractManagedComponent;
import systems.coyote.datastore.EntityStore;


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
public class MsqlEntityStore extends AbstractManagedComponent implements EntityStore {

  /**
   * Keeps the connection fresh, replacing it if necessary
   * 
   * @see coyote.loader.thread.ThreadJob#doWork()
   */
  @Override
  public void doWork() {
    synchronized( mutex ) {
      super.doWork();
    }
  }




  /**
   * 
   */
  public MsqlEntityStore() {
    // TODO Auto-generated constructor stub
  }

}
