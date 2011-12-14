/*
 * Apache License
 * Version 2.0, January 2004
 * http://www.apache.org/licenses/
 *
 * Copyright 2008-2011 by chenillekit.org
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 */

package org.chenillekit.tapestry.core.components.yui;

import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.MarkupWriter;
import org.apache.tapestry5.ValidationTracker;
import org.apache.tapestry5.annotations.Environmental;
import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.annotations.Mixin;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.corelib.mixins.RenderDisabled;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.json.JSONObject;
import org.apache.tapestry5.services.Request;
import org.apache.tapestry5.services.javascript.JavaScriptSupport;

import org.chenillekit.tapestry.core.base.AbstractYuiField;

/**
 * @version $Id$
 */
@Import(stylesheet = {"${yahoo.yui}/button/assets/skins/sam/button.css"},
		library = {"${yahoo.yui}/button/button${yahoo.yui.mode}.js",
				"../../Chenillekit.js", "StateButton.js"})
public class StateButton extends AbstractYuiField
{
	/**
	 * The value to be read or updated. If not bound, the Checkbox will attempt to edit a property of its container
	 * whose name matches the component's id.
	 */
	@Parameter(required = true, autoconnect = true)
	private boolean value;

	@Inject
	private Request request;

	@SuppressWarnings("unused")
	@Mixin
	private RenderDisabled renderDisabled;

	@Inject
	private ComponentResources resources;

	@Environmental
	private ValidationTracker tracker;

	/**
	 * RenderSupport to get unique client side id.
	 */
	@Environmental
	private JavaScriptSupport javascriptSupport;

	/**
	 * Tapestry render phase method.
	 * Start a tag here, end it in afterRender
	 *
	 * @param writer the markup writer
	 */
	void beginRender(MarkupWriter writer)
	{
		writer.element("span", "class", "yui-button", "id", getClientId());
		writer.element("span", "class", "first-child");
		writer.element("button", "id", getClientId() + "Button");
		writer.write(getLabel());
		writer.end();
		writer.end();
		writer.end();
	}

	/**
	 * Tapestry render phase method. End a tag here.
	 *
	 * @param writer the markup writer
	 */
	void afterRender(MarkupWriter writer)
	{
		JSONObject options = new JSONObject();

		options.put("name", getControlName());
		options.put("type", "checkbox");
		options.put("disabled", isDisabled());
		options.put("checked", value);

		configure(options);

		javascriptSupport.addScript("new Ck.YuiStateButton('%s', %s);", getClientId(), options);
	}

	/**
	 * Invoked to allow subclasses to further configure the parameters passed to this mixin's javascript
	 * options. Subclasses may override this method to configure additional features of this mixin.
	 * <p/>
	 * This implementation does nothing.
	 *
	 * @param options option object
	 */
	protected void configure(JSONObject options)
	{
	}

	/**
	 * Method implemented by subclasses to actually do the work of processing the submission of the form. The element's
	 * elementName property will already have been set. This method is only invoked if the field is <strong>not {@link
	 * #isDisabled() disabled}</strong>.
	 *
	 * @param elementName the name of the element (used to find the correct parameter in the request)
	 */
	protected void processSubmission(String elementName)
	{
		String rawValue = request.getParameter(elementName);

		if (rawValue == null)
			value = false;
		else if (rawValue.equalsIgnoreCase("undefined"))
			value = true;
	}
}
