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
package coyote.commons.jdbc;

import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.junit.Assert.assertEquals;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import coyote.commons.jdbc.pool.ConnectionPool;


/**
 * 
 */
public class CoyoteDataSourceTest {

  @Test
  public void configConstructor() throws SQLException {
    DataSourceConfig config = new DataSourceConfig( "Test" )
        .setMinimumIdle( 5 )
        .setMaximumPoolSize( 10 )
        .setConnectionTestQuery( "SELECT 1" )
        .setDataSourceClassName( "org.h2.jdbcx.JdbcDataSource" )
        .addDataSourceProperty( "url", "jdbc:h2:mem:test;DB_CLOSE_DELAY=-1" );

    CoyoteDataSource datasource = new CoyoteDataSource( config );

    //datasource.getConnection().close();

    datasource.close();

  }




  @Test
  public void emptyConstructor() throws SQLException {
    DataSourceConfig config = new DataSourceConfig()
        .setMinimumIdle( 5 )
        .setMaximumPoolSize( 10 )
        .setConnectionTestQuery( "SELECT 1" )
        .setDataSourceClassName( "org.h2.jdbcx.JdbcDataSource" )
        .addDataSourceProperty( "url", "jdbc:h2:mem:test;DB_CLOSE_DELAY=-1" );

    CoyoteDataSource datasource = new CoyoteDataSource();
    datasource.setConfiguraton( config );

   // datasource.getConnection().close();

    datasource.close();

  }




  @Ignore
  public void testIdleTimeout() throws InterruptedException, SQLException {
    DataSourceConfig config = new DataSourceConfig()
        .setMinimumIdle( 5 )
        .setMaximumPoolSize( 10 )
        .setConnectionTestQuery( "SELECT 1" )
        .setDataSourceClassName( "org.h2.jdbcx.JdbcDataSource" )
        .addDataSourceProperty( "url", "jdbc:h2:mem:test;DB_CLOSE_DELAY=-1" );

    try (CoyoteDataSource ds = new CoyoteDataSource( config )) {
      SECONDS.sleep( 1 );
      ConnectionPool pool = ds.getPool();
      ds.getConfig().setIdleTimeout( 3000 );
      assertEquals( "Total connections not as expected", 5, pool.getTotalConnections() );
      assertEquals( "Idle connections not as expected", 5, pool.getIdleConnections() );

      try (Connection connection = ds.getConnection()) {
        Assert.assertNotNull( connection );
        MILLISECONDS.sleep( 1500 );
        assertEquals( "Second total connections not as expected", 6, pool.getTotalConnections() );
        assertEquals( "Second idle connections not as expected", 5, pool.getIdleConnections() );
      }

      assertEquals( "Idle connections not as expected", 6, pool.getIdleConnections() );
      SECONDS.sleep( 2 );
      assertEquals( "Third total connections not as expected", 5, pool.getTotalConnections() );
      assertEquals( "Third idle connections not as expected", 5, pool.getIdleConnections() );
    }
  }




  @Ignore
  public void anotherIdleTimeout() throws InterruptedException, SQLException {

    DataSourceConfig config = new DataSourceConfig()
        .setMaximumPoolSize( 50 )
        .setConnectionTestQuery( "SELECT 1" )
        .setDataSourceClassName( "org.h2.jdbcx.JdbcDataSource" )
        .addDataSourceProperty( "url", "jdbc:h2:mem:test;DB_CLOSE_DELAY=-1" );

    try (CoyoteDataSource ds = new CoyoteDataSource( config )) {
      SECONDS.sleep( 1 );
      ConnectionPool pool = ds.getPool();
      ds.getConfig().setIdleTimeout( 3000 );
      assertEquals( "Total connections not as expected", 5, pool.getTotalConnections() );
      assertEquals( "Idle connections not as expected", 5, pool.getIdleConnections() );

      try (Connection connection = ds.getConnection()) {
        Assert.assertNotNull( connection );
        MILLISECONDS.sleep( 1500 );
        assertEquals( "Second total connections not as expected", 6, pool.getTotalConnections() );
        assertEquals( "Second idle connections not as expected", 5, pool.getIdleConnections() );
      }

      assertEquals( "Idle connections not as expected", 6, pool.getIdleConnections() );
      SECONDS.sleep( 2 );
      assertEquals( "Third total connections not as expected", 5, pool.getTotalConnections() );
      assertEquals( "Third idle connections not as expected", 5, pool.getIdleConnections() );
    }
  }




  @Ignore
  public void useCase1() {
    DataSourceConfig config = new DataSourceConfig();
    config.setJdbcUrl( "jdbc:mysql://localhost/test" );
    config.setUsername( "root" );
    config.setPassword( "password" );
    config.setMaximumPoolSize( 10 );
    config.setAutoCommit( true );
    config.addDataSourceProperty( "cachePrepStmts", "true" );
    config.addDataSourceProperty( "prepStmtCacheSize", "250" );
    config.addDataSourceProperty( "prepStmtCacheSqlLimit", "2048" );

    try (CoyoteDataSource ds = new CoyoteDataSource( config )) {
      try (Connection connection = ds.getConnection()) {

        // this should be the proxy connection class
        System.out.println( "The Connection Object is of Class: " + connection.getClass() );

        PreparedStatement pstmt = connection.prepareStatement( "SELECT * FROM account" );

        ResultSet resultSet = pstmt.executeQuery();
        while ( resultSet.next() ) {
          System.out.println( resultSet.getString( 1 ) + "," + resultSet.getString( 2 ) + "," + resultSet.getString( 3 ) );
        }
        
      } catch ( Exception e ) {
        e.printStackTrace();
      } // connection
      
    } // datasource
    
  }

}
