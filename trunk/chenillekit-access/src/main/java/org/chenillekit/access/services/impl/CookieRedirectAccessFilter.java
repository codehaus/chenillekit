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

package org.chenillekit.access.services.impl;

import java.io.IOException;

import org.apache.tapestry5.services.ComponentEventRequestParameters;
import org.apache.tapestry5.services.ComponentRequestFilter;
import org.apache.tapestry5.services.ComponentRequestHandler;
import org.apache.tapestry5.services.Cookies;
import org.apache.tapestry5.services.PageRenderRequestParameters;
import org.chenillekit.access.ChenilleKitAccessConstants;
import org.chenillekit.access.services.RedirectService;

/**
* Filter to catch request coming in after a successfull login which should
* be redirected to the actual request made before the login request.
* 
* @version $Id: AuthenticationService.java 380 2008-12-30 10:21:52Z mlusetti $
*/
public class CookieRedirectAccessFilter implements ComponentRequestFilter
{
	private final Cookies cookies;
	
	private final RedirectService redirect;
	
	/**
	 * 
	 * @param cookies
	 * @param response
	 * @param redirect
	 */
	public CookieRedirectAccessFilter(Cookies cookies, RedirectService redirect)
	{
		this.cookies = cookies;
		this.redirect = redirect;
	}

	/* (non-Javadoc)
	 * @see org.apache.tapestry5.services.ComponentRequestFilter#handleComponentEvent(org.apache.tapestry5.services.ComponentEventRequestParameters, org.apache.tapestry5.services.ComponentRequestHandler)
	 */
	public void handleComponentEvent(
			ComponentEventRequestParameters parameters,
			ComponentRequestHandler handler) throws IOException
	{
		// We don't need to interfere here since we do redirect just after a successful
		// login from our own component which after all do a page render redirect
		handler.handleComponentEvent(parameters);
	}

	/* (non-Javadoc)
	 * @see org.apache.tapestry5.services.ComponentRequestFilter#handlePageRender(org.apache.tapestry5.services.PageRenderRequestParameters, org.apache.tapestry5.services.ComponentRequestHandler)
	 */
	public void handlePageRender(PageRenderRequestParameters parameters,
			ComponentRequestHandler handler) throws IOException
	{
		String successfulLogin = cookies.readCookieValue(ChenilleKitAccessConstants.LOGIN_SUCCESSFUL_COOKIE_NAME);
		String ckAccessId = cookies.readCookieValue(ChenilleKitAccessConstants.ACCESS_ID_COOKIE_NAME);
		
		PageRenderRequestParameters actualParameters = null;
		
		if (successfulLogin != null && successfulLogin.equals(ChenilleKitAccessConstants.LOGIN_SUCCESSFUL_COOKIE_NAME_OK))
		{	
			// We have just done a successfull login
			if (ckAccessId == null)
			{
				// We don't have the hook for stored parameters so proceed...
				handler.handlePageRender(parameters);
			}
			else
			{
				// We have the hook so check for stored page render parameter
				actualParameters = redirect.removePageRenderParamter(ckAccessId);
				
				if (actualParameters != null)
				{
					cookies.removeCookieValue(ChenilleKitAccessConstants.LOGIN_SUCCESSFUL_COOKIE_NAME);
					cookies.removeCookieValue(ChenilleKitAccessConstants.ACCESS_ID_COOKIE_NAME);
					
					redirect.redirectTo(actualParameters.getLogicalPageName(), actualParameters.getActivationContext());
				}
				else
				{
					// We don't have page render parameter check for component event one
					ComponentEventRequestParameters eventParameters = redirect.removeComponentEventParameter(ckAccessId);
					if (eventParameters != null)
					{				
						cookies.removeCookieValue(ChenilleKitAccessConstants.LOGIN_SUCCESSFUL_COOKIE_NAME);
						cookies.removeCookieValue(ChenilleKitAccessConstants.ACCESS_ID_COOKIE_NAME);
						
						redirect.redirectTo(eventParameters.getActivePageName(), eventParameters.getEventContext());
					}
					else
					{
						// We don't have anything and something wired has happened...
						handler.handlePageRender(parameters);
					}
				}
			}
		}
		else
		{
			// Normally we should continue down the pipeline...
			handler.handlePageRender(parameters);
		}
	}

}
