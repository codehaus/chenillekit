/*
 * Apache License
 * Version 2.0, January 2004
 * http://www.apache.org/licenses/
 *
 * Copyright 2008-2010 by chenillekit.org
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 */

package org.chenillekit.tapestry.core.components;

import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.MarkupWriter;
import org.apache.tapestry5.annotations.AfterRender;
import org.apache.tapestry5.annotations.BeginRender;
import org.apache.tapestry5.annotations.Environmental;
import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.Environment;
import org.apache.tapestry5.services.javascript.JavaScriptSupport;

/**
 * shows an tooltip if mouse slides over the declared content.
 *
 * @version $Id$
 */
@Import(library = {"../Chenillekit.js", "Tooltip.js"}, stylesheet = {"Tooltip.css"})
public class Tooltip
{
	/**
	 * The id used to generate a page-unique client-side identifier for the component. If a component renders multiple
	 * times, a suffix will be appended to the to id to ensure uniqueness.
	 */
	@Parameter(value = "prop:componentResources.id", defaultPrefix = BindingConstants.LITERAL)
	private String clientId;

	/**
	 * the tooltip title.
	 */
	@Parameter(value = "", required = false, defaultPrefix = "literal")
	private String title;

	/**
	 * the tooltip content.
	 */
	@Parameter(value = "", required = false, defaultPrefix = "literal")
	private String value;

	/**
	 * the tooltip effect ("blind", "appear", "slide").
	 */
	@Parameter(required = false, defaultPrefix = "literal")
	private String effect;

	@Inject
	private ComponentResources resources;

	@Environmental
	private JavaScriptSupport javascriptSupport;

	@Inject
	private Environment environment;

	private String assignedClientId;

	void setupRender()
	{
		assignedClientId = javascriptSupport.allocateClientId(clientId);
	}

	@BeginRender
	void beginRender(MarkupWriter writer)
	{
		writer.element("span",
					   "id", assignedClientId);
	}

	@AfterRender
	void afterRender(MarkupWriter writer)
	{
		writer.end();

		String jsCommand = "new Ck.Tip('%s', '%s'";
		jsCommand += ", {className: 'ck_tooltip'";

		if (title != null)
			jsCommand += ", title: '" + replaceJSChar(title) + "'";

		if (effect != null)
			jsCommand += ", effect: '" + effect + "'";

		jsCommand += "});";
		javascriptSupport.addScript(jsCommand, assignedClientId, replaceJSChar(value));
	}

	/**
	 * replace the ' char with the " char and '%' with '%%'.
	 */
	private String replaceJSChar(String value)
	{
		if (value == null)
			return "";

		return value.replaceAll("'", "\"").replaceAll("%", "%%");
	}
}
