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
package coyote.commons.jdbc;

import java.io.Closeable;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Logger;

import javax.sql.DataSource;

import coyote.commons.CipherUtil;
import coyote.commons.ExceptionUtil;
import coyote.commons.jdbc.pool.ConnectionPool;
import coyote.dataframe.DataFrameException;
import coyote.loader.Loader;
import coyote.loader.cfg.Config;
import coyote.loader.log.Log;


/**
 * A datasource which uses connection pooling.
 */
public class CoyoteDataSource implements DataSource, Closeable {

  // Configuration tags
  private static final String NAME = "Name";
  private static final String LIBRARY = "Library";
  private static final String USERNAME = "Username";
  private static final String PASSWORD = "Password";
  private static final String TARGET = "Target";
  private static final String DRIVER = "Driver";
  private static final String AUTO_CREATE = "AutoCreate";

  private final Config configuration = new Config();
  private final AtomicBoolean isClosed = new AtomicBoolean();
  private volatile ConnectionPool pool = null;

  private Driver driver = null;




  public void setConfiguration( Config cfg ) {
    configuration.merge( cfg );
  }




  public void open() {
    if ( getName() == null ) {
      setName( "CoyoteDataSource." + getInstanceCount() );
    }
  }




  /**
   * Shutdown the DataSource and its associated pool.
   */
  @Override
  public void close() {
    if ( isClosed.getAndSet( true ) ) {
      return;
    }

    ConnectionPool p = pool;
    if ( p != null ) {
      try {
        Log.info( getName() + " - Shutdown initiated..." );
        p.shutdown();
        Log.info( getName() + " - Shutdown completed." );
      } catch ( InterruptedException e ) {
        Log.warn( getName() + " - Interrupted during closing: " + e.getMessage() );
        Thread.currentThread().interrupt();
      }
    }
  }




  private void setName( String value ) {
    configuration.put( NAME, value );
  }




  private String getName() {
    return configuration.getString( NAME );
  }




  @Override
  public PrintWriter getLogWriter() throws SQLException {
    // TODO Auto-generated method stub
    return null;
  }




  @Override
  public void setLogWriter( PrintWriter out ) throws SQLException {
    // TODO Auto-generated method stub

  }




  @Override
  public void setLoginTimeout( int seconds ) throws SQLException {
    // TODO Auto-generated method stub

  }




  @Override
  public int getLoginTimeout() throws SQLException {
    // TODO Auto-generated method stub
    return 0;
  }




  @Override
  public Logger getParentLogger() throws SQLFeatureNotSupportedException {
    // TODO Auto-generated method stub
    return null;
  }




  @Override
  public <T> T unwrap( Class<T> iface ) throws SQLException {
    // TODO Auto-generated method stub
    return null;
  }




  @Override
  public boolean isWrapperFor( Class<?> iface ) throws SQLException {
    // TODO Auto-generated method stub
    return false;
  }




  @Override
  public Connection getConnection() throws SQLException {

    if ( isClosed() ) {
      throw new SQLException( "CoyoteDataSource " + this + " has been closed." );
    }
    pool = new ConnectionPool( this );

    return pool.getConnection();
  }




  /**
   * Not supported as our configuration controls how we operate.
   * 
   * @see javax.sql.DataSource#getConnection(java.lang.String, java.lang.String)
   */
  @Override
  public Connection getConnection( String username, String password ) throws SQLException {
    throw new SQLFeatureNotSupportedException();
  }




  /**
   * @return the number of DataSources in this runtime.
   */
  private int getInstanceCount() {
    synchronized( System.getProperties() ) {
      final int next = Integer.getInteger( "CoyoteDataSource.instance_count", 0 ) + 1;
      System.setProperty( "CoyoteDataSource.instance_count", String.valueOf( next ) );
      return next;
    }
  }




  /**
   * Determine if the DataSource has been closed.
   *
   * @return true if the CoyoteDataSource has been closed, false otherwise
   */
  public boolean isClosed() {
    return isClosed.get();
  }




  @Override
  public String toString() {
    return getName();
  }




  /**
   * Create a connection to the database.
   * 
   * <p>Caller is responsible for closing the connection when done with it.
   * 
   * @return the connection to the database or null if there were problems
   */
  private Connection createConnection() {
    Connection retval = null;
    try {
      if ( driver == null ) {
        String url = getLibrary();
        URL u = new URL( url );
        URLClassLoader ucl = new URLClassLoader( new URL[] { u } );
        driver = (Driver)Class.forName( getDriver(), true, ucl ).newInstance();
        DriverManager.registerDriver( new DriverDelegate( driver ) );
      }
      retval = DriverManager.getConnection( getTarget(), getUsername(), getPassword() );
    } catch ( InstantiationException | IllegalAccessException | ClassNotFoundException | SQLException | MalformedURLException e ) {
      Log.error( "Could not connect to database: " + e.getClass().getSimpleName() + " - " + e.getMessage() );
      Log.debug( "ERROR: Could not connect to database: " + e.getClass().getSimpleName() + " - " + e.getMessage() + "\n" + ExceptionUtil.stackTrace( e ) );
    }
    return retval;
  }




  /**
   * @param value
   */
  public void setAutoCreate( boolean value ) {
    configuration.put( AUTO_CREATE, value );
  }




  public boolean isAutoCreate() {
    try {
      return configuration.getAsBoolean( AUTO_CREATE );
    } catch ( DataFrameException ignore ) {}
    return false;
  }




  public String getDriver() {
    return configuration.getString( DRIVER );
  }




  /**
   * @return the target URI to which the writer will write
   */
  public String getTarget() {
    return configuration.getString( TARGET );
  }




  /**
   * Set the URI to where the connection should be made.
   * 
   * @param value the URI to where the writer should write its data
   */
  public void setTarget( final String value ) {
    configuration.put( TARGET, value );
  }




  public String getPassword() {
    if ( configuration.containsIgnoreCase( PASSWORD ) ) {
      return configuration.getAsString( PASSWORD );
    } else if ( configuration.containsIgnoreCase( Loader.ENCRYPT_PREFIX + PASSWORD ) ) {
      return CipherUtil.decryptString( configuration.getAsString( Loader.ENCRYPT_PREFIX + PASSWORD ) );
    } else {
      return null;
    }
  }




  public String getUsername() {
    if ( configuration.containsIgnoreCase( USERNAME ) ) {
      return configuration.getFieldIgnoreCase( USERNAME ).getStringValue();
    } else if ( configuration.containsIgnoreCase( Loader.ENCRYPT_PREFIX + USERNAME ) ) {
      return CipherUtil.decryptString( configuration.getFieldIgnoreCase( Loader.ENCRYPT_PREFIX + USERNAME ).getStringValue() );
    } else {
      return null;
    }
  }




  /**
   * @param value
   */
  public void setUsername( String value ) {
    configuration.put( USERNAME, value );
  }




  public String getLibrary() {
    return configuration.getString( LIBRARY );
  }

}
