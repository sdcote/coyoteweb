/*
 * Copyright (c) 2017 Stephan D. Cote' - All rights reserved.
 * 
 * This program and the accompanying materials are made available under the 
 * terms of the MIT License which accompanies this distribution, and is 
 * available at http://creativecommons.org/licenses/MIT/
 *
 * Contributors:
 *   Stephan D. Cote 
 *      - Initial implementation
 */
package coyote.commons.jdbc.pool;

import java.sql.Connection;

import coyote.commons.jdbc.CoyoteDataSource;


/**
 * Implementation of a connection pool.
 */
public class ConnectionPool extends AbstractConnectionPool {

  public ConnectionPool( CoyoteDataSource datasource ) {
    super( datasource );
  }




  public Connection getConnection() {
    
    // TODO need to return a wrapper around a the provided connections so we have the ability to intercept calls to close
    return null;
  }




  /**
   * Shutdown the pool, closing all idle connections and aborting or closing
   * active connections.
   *
   * @throws InterruptedException thrown if the thread is interrupted during shutdown
   */
  public synchronized void shutdown() throws InterruptedException {

    // perform a soft shutdown of all connections
    
    // wait a short time
    
    // perform a hard shutdown
        

  }




  public Object getTotalConnections() {
    // TODO Auto-generated method stub
    return null;
  }




  public Object getIdleConnections() {
    // TODO Auto-generated method stub
    return null;
  }

}
