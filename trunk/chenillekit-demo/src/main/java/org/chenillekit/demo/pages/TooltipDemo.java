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

package org.chenillekit.demo.pages;

import java.util.List;

import org.apache.tapestry5.annotations.Cached;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.internal.util.CollectionFactory;

import org.chenillekit.demo.components.BarChart;
import org.chenillekit.demo.components.LeftSideMenu;
import org.chenillekit.demo.components.LineChart;
import org.chenillekit.tapestry.core.components.Chart;
import org.chenillekit.tapestry.core.components.Editor;
import org.chenillekit.tapestry.core.components.Tooltip;
import org.chenillekit.tapestry.core.utils.XYDataItem;

/**
 * @author <a href="mailto:homburgs@gmail.com">S.Homburg</a>
 * @version $Id$
 */
public class TooltipDemo
{
    @Component(parameters = {"menuName=demo"})
    private LeftSideMenu menu;

    @Component(parameters = {"title=Info", "value=... and here comes the info text ...", "effect=blind"})
    private Tooltip tooltip1;

    @Component(parameters = {"title=Info", "value=... and here comes the info text ...", "effect=slide"})
    private Tooltip tooltip2;

}