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
 *
 */

package org.chenillekit.tapestry.core.components;

import java.util.List;

import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.ClientElement;
import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.Link;
import org.apache.tapestry5.MarkupWriter;
import org.apache.tapestry5.RenderSupport;
import org.apache.tapestry5.annotations.Environmental;
import org.apache.tapestry5.annotations.IncludeJavaScriptLibrary;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.SupportsInformalParameters;
import org.apache.tapestry5.ioc.annotations.Inject;

/**
 * Render a button tag element and bind to it's "click" event an event on the server side.
 * The event name is customizable and it defaults to "CLICKED".
 * <p/>
 * If button is typed as "page", you must fill the parameter "pageName" with an existing
 * logical page name.
 *
 * @version $Id: Button.java 682 2008-05-20 22:00:02Z homburgs $
 */
@SupportsInformalParameters
@IncludeJavaScriptLibrary(value = {"../Chenillekit.js", "CkOnEvents.js"})
public class Button implements ClientElement
{
    static final String CLICKED_EVENT = "clicked";

    static final String BUTTON_TYPE = "button";
    static final String SUBMIT_TYPE = "submit";
    static final String CANCEL_TYPE = "cancel";
    static final String PAGE_TYPE = "page";

    /**
     * type of button.
     * possible value are "button", "submit" and "cancel".
     */
    @Parameter(defaultPrefix = BindingConstants.LITERAL, value = BUTTON_TYPE)
    private String type;

    /**
     * wich event should your application receiving.
     */
    @Parameter(defaultPrefix = BindingConstants.LITERAL, value = CLICKED_EVENT)
    private String event;

    /**
     * The logical name of the page to link to.
     */
    @Parameter(defaultPrefix = BindingConstants.LITERAL, required = false)
    private String pageName;

    /**
     * dis-/enable the button.
     */
    @Parameter(value = "false")
    private boolean disabled;

    /**
     * The id used to generate a page-unique client-side identifier for the component. If a component renders multiple
     * times, a suffix will be appended to the to id to ensure uniqueness.
     */
    @Parameter(value = "prop:componentResources.id", defaultPrefix = BindingConstants.LITERAL)
    private String clientId;

    /**
     * The context for the link (optional parameter). This list of values will be converted into strings and included in
     * the URI. The strings will be coerced back to whatever their values are and made available to event handler
     * methods.
     */
    @Parameter
    private List<?> context;

    @Environmental
    private RenderSupport renderSupport;

    @Inject
    private ComponentResources resources;

    private String assignedClientId;

    private Object[] contextArray;

    void setupRender()
    {
        assignedClientId = renderSupport.allocateClientId(clientId);
        contextArray = context == null ? new Object[0] : context.toArray();
    }

    void beginRender(MarkupWriter writer)
    {
        writer.element("button", "type", type, "id", getClientId());
        resources.renderInformalParameters(writer);
    }

    void afterRender(MarkupWriter writer)
    {
        if (!disabled && (type.equalsIgnoreCase(BUTTON_TYPE) || type.equalsIgnoreCase(PAGE_TYPE)))
        {
            Link link;

            if (!type.equalsIgnoreCase(PAGE_TYPE))
                link = resources.createEventLink(event, contextArray);
            else
            {
                if (pageName == null)
                    throw new RuntimeException("parameter 'pageName' can not be null for button typed page link");

                link = resources.createPageLink(pageName, resources.isBound("context"), contextArray);
            }
            renderSupport.addScript("new Ck.ButtonEvent('%s', '%s');",
                                    getClientId(), link.toAbsoluteURI());
        }

        // Close the button tag
        writer.end();
    }

    /**
     * Returns a unique id for the element. This value will be unique for any given rendering of a
     * page. This value is intended for use as the id attribute of the client-side element, and will
     * be used with any DHTML/Ajax related JavaScript.
     */
    public String getClientId()
    {
        return assignedClientId;
    }
}
