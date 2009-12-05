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

package org.chenillekit.tapestry.core.mixins;

import org.apache.tapestry5.annotations.Parameter;

import org.chenillekit.tapestry.core.base.AbstractEventMixin;

/**
 * OnChange mixin catch the browser event "onChange" from a select component
 * and redirect it to your application via tapestry event "change".
 *
 * @version $Id$
 */
public class OnEvent extends AbstractEventMixin
{
    @Parameter(required = true, defaultPrefix = "literal")
    private String event;

    /**
     * set the event name.
     *
     * @return the event name
     */
    public String getEventName()
    {
        return event;
    }
}