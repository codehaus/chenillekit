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

package org.chenillekit.access.integration.app3.pages;

import java.util.Collections;
import java.util.List;

import org.apache.tapestry5.annotations.SessionState;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.ioc.annotations.Local;
import org.apache.tapestry5.ioc.internal.util.CollectionFactory;

import org.chenillekit.access.WebSessionUser;
import org.chenillekit.access.annotations.Restricted;
import org.chenillekit.access.services.AuthenticationService;

/**
 * @version $Id: Start.java 686 2010-08-04 15:42:29Z mlusetti $
 */
@Restricted
public class Start
{
	@Inject @Local @SuppressWarnings("unused")
	private AuthenticationService authenticationService;

	@SessionState @SuppressWarnings("unused")
	private WebSessionUser<?> webSessionUser;

	public static class Item implements Comparable<Item>
	{
		private final String _pageName;
		private final String _label;
		private final String _description;

		public Item(String pageName, String label, String description)
		{
			_pageName = pageName;
			_label = label;
			_description = description;
		}

		public String getPageName()
		{
			return _pageName;
		}

		public String getLabel()
		{
			return _label;
		}

		public String getDescription()
		{
			return _description;
		}

		public int compareTo(Item o)
		{
			return _label.compareTo(o._label);
		}
	}

	private static final List<Item> ITEMS = CollectionFactory.newList(
			new Item("RestrictedPage", "Restricted", "tests Restricted page"),
			new Item("Logout", "Logout", "Logout user"),
			new Item("Login", "Login", "Login access form")
	);

	static
	{
		Collections.sort(ITEMS);
	}

	private Item _item;

	public List<Item> getItems()
	{
		return ITEMS;
	}

	public Item getItem()
	{
		return _item;
	}

	public void setItem(Item item)
	{
		_item = item;
	}
}
