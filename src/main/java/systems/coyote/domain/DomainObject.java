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
package systems.coyote.domain;

import coyote.dataframe.DataFrame;


/**
 * DomainObjects provide a decoupled wrapper around a dataframe, providing 
 * accessors to named fields.
 * 
 * <p>Field naming is case-sensitive. If a column name in the table changes, 
 * the domain object will not find and/or ignore it.
 */
public abstract class DomainObject<T extends DomainObject<T>> extends DataFrame {

  private static final String SYSID = "SysId";
  private static final String CREATED_BY = "CreatedBy";
  private static final String CREATED = "Created";
  private static final String MODIFIED_BY = "ModifiedBy";
  private static final String MODIFIED = "Modified";




  public DomainObject() {}




  public DomainObject( DataFrame record ) {
    merge( record );
  }




  /**
   * Enables setter chaining.
   * 
   * <p>Enables the ability so chain setters:
   * <pre>new Thing().setThis(x).setThat(y).setAnother(z);</pre>
   * 
   * @return a reference to the domain object.
   */
  protected abstract T getThis();




  public String getSystemId() { return getAsString( SYSID ); }
  public T setSystemId( String value ) { set( SYSID, value ); return getThis(); }

  public String getCreatedBy() { return getAsString( CREATED_BY ); }
  public T setCreatedBy( String value ) { set( CREATED_BY, value ); return getThis(); }

  public String getCreated() { return getAsString( CREATED ); }
  public T setCreated( String value ) { set( CREATED, value ); return getThis(); }

  public String getModifiedBy() { return getAsString( MODIFIED_BY ); }
  public T setModifiedBy( String value ) { set( MODIFIED_BY, value ); return getThis(); }

  public String getModified() { return getAsString( MODIFIED ); }
  public T setModified( String value ) { set( MODIFIED, value ); return getThis(); }

}
