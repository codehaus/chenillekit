/*
 * Apache License
 * Version 2.0, January 2004
 * http://www.apache.org/licenses/
 *
 * Copyright 1996-2008 by Sven Homburg
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 */

package org.chenillekit.tapestry.core.factories;

import org.apache.tapestry5.Binding;
import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.ioc.Location;
import org.apache.tapestry5.services.BindingFactory;

import org.chenillekit.tapestry.core.bindings.OgnlBinding;

/**
 * Binding factory where the expression evaluated by OGNL.
 *
 * @version $Id: OgnlBindingFactory.java 682 2008-05-20 22:00:02Z homburgs $
 */
public class OgnlBindingFactory implements BindingFactory
{
    public Binding newBinding(String description, ComponentResources container,
                              ComponentResources component,
                              String expression, Location location)
    {
        return new OgnlBinding(location, container.getComponent(), expression);
    }
}