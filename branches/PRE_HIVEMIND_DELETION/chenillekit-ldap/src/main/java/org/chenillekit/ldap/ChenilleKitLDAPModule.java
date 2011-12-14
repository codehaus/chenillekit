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

package org.chenillekit.ldap;

import org.apache.tapestry5.ioc.MappedConfiguration;
import org.apache.tapestry5.ioc.ServiceBinder;
import org.apache.tapestry5.ioc.ServiceResources;
import org.apache.tapestry5.ioc.services.RegistryShutdownHub;

import org.chenillekit.ldap.services.LDAPOperation;
import org.chenillekit.ldap.services.impl.LDAPOperationImpl;
import org.chenillekit.ldap.services.internal.LDAPSource;
import org.chenillekit.ldap.services.internal.LDAPSourceImpl;
import org.chenillekit.ldap.services.internal.ReadService;
import org.chenillekit.ldap.services.internal.ReadServiceImpl;
import org.chenillekit.ldap.services.internal.WriteService;
import org.chenillekit.ldap.services.internal.WriteServiceImpl;

/**
 * @version $Id$
 */
public class ChenilleKitLDAPModule
{
	
	public static void bind(ServiceBinder binder)
	{
		binder.bind(LDAPOperation.class, LDAPOperationImpl.class);
		binder.bind(ReadService.class, ReadServiceImpl.class);
		binder.bind(WriteService.class, WriteServiceImpl.class);
	}
	
    public static LDAPSource buildLDAPSource(ServiceResources resources,
                                                                 RegistryShutdownHub shutdownHub)
    {
        final LDAPSourceImpl service = resources.autobuild(LDAPSourceImpl.class);

		shutdownHub.addRegistryShutdownListener(new Runnable()
		{
			public void run()
			{
				service.shutdown();
			}
		});

        return service;
    }

    /**
     * Contributes factory defaults that may be overridden.
     */
    public static void contributeFactoryDefaults(MappedConfiguration<String, String> contribution)
    {
        contribution.add(ChenilleKitLDAPConstants.LDAP_VERSION, "3");
        contribution.add(ChenilleKitLDAPConstants.LDAP_HOSTNAME, "");
        contribution.add(ChenilleKitLDAPConstants.LDAP_HOSTPORT, "389");
        contribution.add(ChenilleKitLDAPConstants.LDAP_AUTHDN, "");
        contribution.add(ChenilleKitLDAPConstants.LDAP_AUTHPWD, "");
        
        contribution.add(ChenilleKitLDAPConstants.LDAP_SIZELIMIT, "1000");
        contribution.add(ChenilleKitLDAPConstants.LDAP_TIMELIMIT, "60000");
    }
}
