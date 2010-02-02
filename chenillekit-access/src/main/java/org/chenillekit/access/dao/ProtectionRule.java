/*
 * Apache License
 * Version 2.0, January 2004
 * http://www.apache.org/licenses/
 *
 * Copyright 2010 by chenillekit.org
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 */

package org.chenillekit.access.dao;

/**
 * @author <a href="mailto:shomburg@depot120.dpd.de">S.Homburg</a>
 * @version $Id$
 */
public class ProtectionRule implements IProtectionRule
{
    private String[] groups;
    private int roleWeight;

    public void setGroups(String groups)
    {
        if (groups != null)
            this.groups = groups.split(",");
    }

    /**
     * get the groups.
     */
    public String[] getGroups()
    {
        return groups;
    }

    public void setRoleWeight(int roleWeight)
    {
        this.roleWeight = roleWeight;
    }

    /**
     * get the role weight.
     */
    public int getRoleWeight()
    {
        return roleWeight;
    }
}
