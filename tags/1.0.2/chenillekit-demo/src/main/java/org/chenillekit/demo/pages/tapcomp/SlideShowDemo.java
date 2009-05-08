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

package org.chenillekit.demo.pages.tapcomp;

import org.apache.tapestry5.annotations.Component;

import org.chenillekit.demo.components.LeftSideMenu;
import org.chenillekit.tapestry.core.components.SlideShow;

/**
 * @version $Id$
 */
public class SlideShowDemo
{
    @Component(parameters = {"menuName=demo"})
    private LeftSideMenu menu;

    @Component(parameters = {"interval=3", "loop=true", "controls=false"})
    private SlideShow slideShow;
}