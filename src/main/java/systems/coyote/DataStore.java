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
package systems.coyote;

import javax.sql.DataSource;

import coyote.commons.jdbc.CoyoteDataSource;
import coyote.loader.ConfigTag;
import systems.coyote.datastore.AbstractStore;
import systems.coyote.datastore.EntityStore;
import systems.coyote.datastore.GroupStore;
import systems.coyote.datastore.IdentityStore;
import systems.coyote.datastore.NavigationStore;
import systems.coyote.datastore.SessionStore;
import systems.coyote.domain.Identity;


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
   * @see systems.coyote.datastore.IdentityStore#getIdentity(java.lang.String)
   */
  @Override
  public Identity getIdentity( String sysid ) {
    // TODO Auto-generated method stub
    return null;
  }

}
