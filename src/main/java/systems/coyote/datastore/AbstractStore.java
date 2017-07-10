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
package systems.coyote.datastore;

import coyote.loader.component.AbstractManagedComponent;

/**
 * 
 */
public abstract class AbstractStore extends AbstractManagedComponent {

  

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
  
  
}
