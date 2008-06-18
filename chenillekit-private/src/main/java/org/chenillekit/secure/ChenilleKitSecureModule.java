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

package org.chenillekit.secure;

import java.io.IOException;
import java.util.Properties;
import java.util.Set;

import org.apache.tapestry5.ioc.MappedConfiguration;
import org.apache.tapestry5.ioc.OrderedConfiguration;
import org.apache.tapestry5.ioc.ServiceBinder;
import org.apache.tapestry5.services.ComponentClassTransformWorker;
import org.apache.tapestry5.services.Dispatcher;
import org.chenillekit.secure.annotations.AccessControlDispatcher;
import org.chenillekit.secure.services.impl.AccessControlWorker;
import org.chenillekit.secure.services.impl.AccessController;

/**
 * @author <a href="mailto:homburgs@gmail.com">S.Homburg</a>
 * @version $Id$
 */
public class ChenilleKitSecureModule
{
	/**
	 * 
	 * @param binder
	 */
	public static void bind(ServiceBinder binder)
    {
        binder.bind(Dispatcher.class, AccessController.class).withMarker(AccessControlDispatcher.class);
    }

	/**
	 * 
	 * @param configuration
	 * @param accessController
	 */
	public void contributeMasterDispatcher(OrderedConfiguration<Dispatcher> configuration,
            @AccessControlDispatcher Dispatcher accessController)
    {
            configuration.add("AccessController", accessController, "before:ComponentEvent");
    }

	/**
	 * 
	 * @param configuration
	 */
    public static void contributeComponentClassTransformWorker(
            OrderedConfiguration<ComponentClassTransformWorker> configuration)
    {
        configuration.add("Private", new AccessControlWorker(), "after:Secure");
    }
    
    
    /**
     * 
     * @param configuration
     */
    public static void contributeApplicationDefaults(
            MappedConfiguration<String, String> configuration)
    {
    	Properties prop = new Properties();
    	try
    	{
    		prop.load(ChenilleKitSecureModule.class.getResourceAsStream("/chenillekit-secure.properties"));
    	}
    	catch (IOException e)
    	{
    		throw new RuntimeException(e);
    	}

    	Set<Object> keys = prop.keySet();
    	for (Object key : keys)
    	{
    		Object value = prop.get(key);
    		configuration.add(key.toString(), value.toString());
    	}
    }
    
	
}
