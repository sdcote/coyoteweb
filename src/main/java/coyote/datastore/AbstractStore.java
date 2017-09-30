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

import java.io.Closeable;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.DriverPropertyInfo;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.logging.Logger;

import coyote.commons.CipherUtil;
import coyote.commons.ExceptionUtil;
import coyote.dataframe.DataFrameException;
import coyote.loader.Loader;
import coyote.loader.component.AbstractManagedComponent;
import coyote.loader.log.Log;


/**
 * 
 */
public abstract class AbstractStore extends AbstractManagedComponent implements Closeable {

  private static final String AUTO_CREATE = "autocreate";
  private static final String DRIVER = "Driver";
  private static final String TARGET = "Target";
  private static final String PASSWORD = "Password";
  private static final String USERNAME = "Username";
  private static final String LIBRARY = "Library";

  private final List<Connection> connections = new ArrayList<Connection>();
  private Driver driver = null;




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
   * Create a new connection using the configuration.
   * 
   * <p>This does not share nor pool connections, but creates a new connection 
   * on each request. This should be fine for this toolkit as it is expected 
   * that maybe two connections (one for a reader and one for a writer) might be 
   * created.
   * 
   * <p>The primary benefit of this class is that many components can 
   * reference one database configuration in the job and not have to duplicate 
   * the configuration in each component. Additionally, this class will keep a 
   * reference to all the connections and make sure they are closed when the 
   * JRE exits.
   * 
   * <p>Each connection is tracked and closed when this component is closed.
   * 
   * @return a new connection
   */
  public Connection getConnection() {
    Connection connection = null;
    connection = createConnection();
    if ( connection != null ) {
      connections.add( connection );
    }
    return connection;
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




  /**
   * @see java.io.Closeable#close()
   */
  @Override
  public void close() throws IOException {
    for ( Connection connection : connections ) {
      if ( connection != null ) {
        try {
          connection.close();
        } catch ( SQLException ignore ) {}
      }
    }
  }




  /**
   * @see coyote.loader.thread.ThreadJob#terminate()
   */
  @Override
  public void terminate() {
    try {
      close();
    } catch ( IOException ignore ) {}
  }




  /**
   * Retrieves the name of this database product.
   * 
   * <p>This is converted to uppercase for uniformity with various versions of 
   * the drivers and API usages.
   *
   * @return database product name
   */
  public String getProductName( Connection connection ) {
    String retval = null;
    DatabaseMetaData meta = null;
    try {
      meta = connection.getMetaData();
    } catch ( SQLException e ) {
      getContext().setError( "Could not connect to database: " + e.getClass().getSimpleName() + " - " + e.getMessage() );
    }

    if ( meta != null ) {
      try {
        retval = meta.getDatabaseProductName();
      } catch ( SQLException ignore ) {}
      if ( retval != null ) {
        return retval.toUpperCase();
      }
    }
    return retval;
  }




  /**
   * Retrieves the version number of this database product.
   *
   * @return database version number, null if problems occurred or not supported
   */
  public String getProductVersion( Connection connection ) {
    String retval = null;
    DatabaseMetaData meta = null;
    try {
      meta = connection.getMetaData();
    } catch ( SQLException e ) {
      getContext().setError( "Could not connect to database: " + e.getClass().getSimpleName() + " - " + e.getMessage() );
    }

    if ( meta != null ) {
      try {
        retval = meta.getDatabaseProductVersion();
      } catch ( SQLException ignore ) {}
      if ( retval != null ) {
        return retval.toUpperCase();
      }
    }
    return retval;
  }




  /**
   * Retrieves the user name as known to this database.
   *
   * @return the database user name
   */
  public String getConnectedUser( Connection connection ) {
    String retval = null;
    DatabaseMetaData meta = null;
    try {
      meta = connection.getMetaData();
    } catch ( SQLException e ) {
      getContext().setError( "Could not connect to database: " + e.getClass().getSimpleName() + " - " + e.getMessage() );
    }

    if ( meta != null ) {
      try {
        retval = meta.getDatabaseProductVersion();
      } catch ( SQLException ignore ) {}
      if ( retval != null ) {
        return retval.toUpperCase();
      }
    }
    return retval;
  }

  /**
   * The delegate class which allows the class loader to find and handle 
   * dynamically loaded JDBC drivers.
   */
  public class DriverDelegate implements Driver {
    private Driver driver;




    public DriverDelegate( Driver d ) {
      this.driver = d;
    }




    public boolean acceptsURL( String u ) throws SQLException {
      return this.driver.acceptsURL( u );
    }




    public Connection connect( String u, Properties p ) throws SQLException {
      return this.driver.connect( u, p );
    }




    public int getMajorVersion() {
      return this.driver.getMajorVersion();
    }




    public int getMinorVersion() {
      return this.driver.getMinorVersion();
    }




    public DriverPropertyInfo[] getPropertyInfo( String u, Properties p ) throws SQLException {
      return this.driver.getPropertyInfo( u, p );
    }




    public boolean jdbcCompliant() {
      return this.driver.jdbcCompliant();
    }




    public Logger getParentLogger() throws SQLFeatureNotSupportedException {
      return this.driver.getParentLogger();
    }
  }

}
