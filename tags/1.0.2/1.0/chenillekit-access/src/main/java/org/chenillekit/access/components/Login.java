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

package org.chenillekit.access.components;

import org.apache.tapestry5.ValidationTracker;
import org.apache.tapestry5.annotations.ApplicationState;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.ioc.annotations.Local;
import org.chenillekit.access.ChenilleKitAccessConstants;
import org.chenillekit.access.WebSessionUser;
import org.chenillekit.access.services.AuthenticationService;

/**
 * Login component
 *
 * @version $Id$
 */
public class Login
{
	@ApplicationState
	private WebSessionUser webSessionUser;
	
	@Inject
	private Messages messages;
	
	@Inject @Local
	private AuthenticationService authenticationService;
	
	@Component
	private Form chenillekitLoginForm;
	
	@Property
	private String username;

	@Property
	private String password;
	
	private WebSessionUser tmpUser;
	
	void onValidateForm()
	{
		ValidationTracker tracker = chenillekitLoginForm.getDefaultTracker();
		
		tmpUser = authenticationService.doAuthenticate(username, password);
		
		if (tmpUser == null)
			tracker.recordError(messages.format(ChenilleKitAccessConstants.NOT_AUTHENTICATED_ERROR_MESSAGE, username));
		
	}
	
	void onFailure()
	{
		// TODO What to do in here?
	}
	
	void onSuccess()
	{
		webSessionUser = tmpUser;
	}

}
