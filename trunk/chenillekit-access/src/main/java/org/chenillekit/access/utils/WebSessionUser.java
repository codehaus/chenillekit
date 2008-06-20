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

package org.chenillekit.access.utils;

/**
 * @author <a href="mailto:shomburg@hsofttec.com">S.Homburg</a>
 * @version $Id$
 */
public interface WebSessionUser
{
    /**
     * get the role id.
     *
     * @return role id
     */
    int getRole();

    /**
     * get the group name.
     *
     * @return group name
     */
    String getGroup();
}
