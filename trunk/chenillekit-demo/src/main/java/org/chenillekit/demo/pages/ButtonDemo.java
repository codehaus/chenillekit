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

import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.OnEvent;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.Persist;

import org.chenillekit.demo.components.LeftSideMenu;
import org.chenillekit.tapestry.core.components.Button;

/**
 * @author <a href="mailto:mlusetti@gmail.com">Massimo Lusetti</a>
 * @version $Id: ButtonPage.java 396 2008-02-07 20:55:40Z homburgs $
 */
public class ButtonDemo
{
    @Persist("flash")
    @Property
    private String lastEventMessage;

    @Component(parameters = {"menuName=demo"})
    private LeftSideMenu menu;

    @Component(parameters = {"type=button"})
    private Button button1;

    @Component(parameters = {"type=button"})
    private Button button2;

    @OnEvent(component = "button1", value = "clicked")
    void onButton1Clicked()
    {
        lastEventMessage = "Button1 has been clicked!";
    }

    @OnEvent(component = "button2", value = "clicked")
    void onButton2Clicked()
    {
        lastEventMessage = "Button2 has been clicked!";
    }
}
