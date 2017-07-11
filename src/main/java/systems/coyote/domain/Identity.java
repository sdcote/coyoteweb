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
 * 
 */
public class Identity extends DomainObject<Identity> {
  private static final String NAME = "Name";




  public Identity() {
    super();
  }




  public Identity( DataFrame record ) {
    super( record );
  }




  public String getName() { return getAsString( NAME ); }
  public Identity setName( String value ) { set( NAME, value ); return getThis(); }




  /**
   * @see systems.coyote.domain.DomainObject#getThis()
   */
  @Override
  protected Identity getThis() {
    return this;
  }

}
