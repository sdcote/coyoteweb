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

import coyote.commons.StringUtil;
import coyote.loader.cfg.Config;


/**
 * 
 */
public class DataSourceConfig extends Config {

  private static final long serialVersionUID = -9164882163438697554L;
  private static final String MAX_POOL_SIZE_TAG = "MaximumPoolSize";
  private static final String MIN_IDLE_TAG = "MinimumIdle";




  public DataSourceConfig( String name ) {
    if ( StringUtil.isNotEmpty( name ) ) {
      setName( name );
    }
  }




  public DataSourceConfig() {
  }




 


  public int getMaxConnections() {
    try {
      return getInt( MAX_POOL_SIZE_TAG );
    } catch ( Exception e ) {
      return 0;
    }
  }




 


  public int getMinimumIdle() {
    try {
      return getInt( MIN_IDLE_TAG );
    } catch ( Exception e ) {
      return 0;
    }
  }




  public DataSourceConfig setMinimumIdle( int num ) {
    int value = num;
    if ( num < 0 ) {
      value = 0;
    }
    put( MIN_IDLE_TAG, value );
    return this;  }




  public DataSourceConfig setMaximumPoolSize( int num ) {
    int value = num;
    if ( num < 0 ) {
      value = 0;
    }
    put( MAX_POOL_SIZE_TAG, value );
     return this;

  }




  public DataSourceConfig setConnectionTestQuery( String sql ) {
    // TODO Auto-generated method stub
    return this;

  }




  public DataSourceConfig setDataSourceClassName( String className ) {
    // TODO Auto-generated method stub
    return this;

  }




  public DataSourceConfig addDataSourceProperty( String key, String value ) {
    // TODO Auto-generated method stub
    return this;

  }




  public void setIdleTimeout( int i ) {
    // TODO Auto-generated method stub
    
  }




  public void setAutoCommit( boolean b ) {
    // TODO Auto-generated method stub
    
  }




  public void setJdbcUrl( String string ) {
    // TODO Auto-generated method stub
    
  }




  public void setUsername( String string ) {
    // TODO Auto-generated method stub
    
  }




  public void setPassword( String string ) {
    // TODO Auto-generated method stub
    
  }

}
