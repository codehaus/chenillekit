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

package org.chenillekit.ldap;

import java.util.Map;

import org.apache.tapestry5.ioc.Resource;
import org.apache.tapestry5.ioc.services.RegistryShutdownHub;

import org.chenillekit.core.services.ConfigurationService;
import org.chenillekit.ldap.services.SearcherService;
import org.chenillekit.ldap.services.impl.SimpleSearcherServiceImpl;
import org.slf4j.Logger;

/**
 * @author <a href="mailto:homburgs@gmail.com">shomburg</a>
 * @version $Id: ChenilleKitMailModule.java 132 2008-07-27 22:18:54Z homburgs@gmail.com $
 */
public class ChenilleKitLDAPModule
{
    public static SearcherService buildSimpleLdapSearcherService(Logger logger,
                                                                 ConfigurationService configurationService,
                                                                 Map<String, Resource> configuration,
                                                                 RegistryShutdownHub shutdownHub)
    {
        SimpleSearcherServiceImpl service =
                new SimpleSearcherServiceImpl(logger,
                                              configurationService.getConfiguration(configuration.get(SearcherService.CONFIG_KEY)));
        shutdownHub.addRegistryShutdownListener(service);
        return service;
    }
}
