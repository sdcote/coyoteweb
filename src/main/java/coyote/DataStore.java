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
package coyote;

import coyote.datastore.AbstractStore;
import coyote.datastore.EntityStore;
import coyote.datastore.GroupStore;
import coyote.datastore.IdentityStore;
import coyote.datastore.NavigationStore;
import coyote.datastore.SessionStore;
import coyote.domain.Identity;


/**
 * Facade to data storage of business domain objects.
 * 
 * <p>Data Stores handle the storage and retrieval of domain objects. This 
 * implements all domain object DataStores and uses the configured database to 
 * determine what database dialect to use.
 * 
 * <p>This does not use a connection pool to manage connections as it shares 
 * the connection with all components in the runtime. This should be fine as 
 * the instance is expected to be lightweight with other instances spun-up if 
 * load requires more throughput.
 */
public class DataStore extends AbstractStore implements EntityStore, IdentityStore, GroupStore, SessionStore, NavigationStore {

  public DataStore() {}




  /**
   * @see coyote.loader.thread.ThreadJob#initialize()
   */
  @Override
  public void initialize() {
    super.initialize();

    getContext().set( getName(), this );
  }




  /**
   * @see coyote.datastore.IdentityStore#getIdentity(java.lang.String)
   */
  @Override
  public Identity getIdentity( String sysid ) {
    // TODO Auto-generated method stub
    return null;
  }

}
