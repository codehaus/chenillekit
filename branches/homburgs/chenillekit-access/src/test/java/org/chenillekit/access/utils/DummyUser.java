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

/**
 *
 */

package org.chenillekit.access.utils;

/**
 * JavaBean used to store infos about the user currently logged
 * in. Typically the final user want to extend this one.
 *
 * @author <a href="mailto:mlusetti@gmail.com">M.Lusetti</a>
 * @version $Id: WebUser.java 88 2008-06-16 16:43:40Z homburgs $
 */

public class DummyUser implements WebSessionUser
{
    private int userId;
    private String name;

    // Both used for security constraints
    private int[] roles;
    private String[] groups;

    public DummyUser()
    {
        this.userId = 2;
        this.name = "dummy";
        this.roles = new int[]{};
        this.groups = new String[]{"dau"};
    }

    public int getUserId()
    {
        return userId;
    }

    public String getName()
    {
        return name;
    }

    /**
     * get the role ids.
     *
     * @return role ids
     */
    public int[] getRoles()
    {
        return roles;
    }

    /**
     * get the group names.
     *
     * @return group names
     */
    public String[] getGroups()
    {
        return groups;
    }
}
