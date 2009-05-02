/*
 * Apache License
 * Version 2.0, January 2004
 * http://www.apache.org/licenses/
 *
 * Copyright 2008 by chenillekit.org
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 */

package org.chenillekit.hibernate.factories;

import org.chenillekit.hibernate.daos.GenericDAO;

/**
 * generic data access object factory.
 *
 * @version $Id$
 */
public interface GenericDAOFactory
{
    /**
     * gets a DAO by his class.
     *
     * @param entityClass
     */
    GenericDAO getDAO(Class entityClass);
}
