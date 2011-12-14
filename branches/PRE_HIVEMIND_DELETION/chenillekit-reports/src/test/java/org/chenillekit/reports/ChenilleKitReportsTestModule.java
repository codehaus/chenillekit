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

package org.chenillekit.reports;

import java.net.URL;

import org.apache.tapestry5.ioc.OrderedConfiguration;
import org.apache.tapestry5.ioc.annotations.SubModule;

/**
 * @version $Id$
 */
@SubModule(value = {ChenilleKitReportsModule.class})
public class ChenilleKitReportsTestModule
{
    public static void contributeReportsService(OrderedConfiguration<URL> configuration)
    {
    	URL config = ChenilleKitReportsTestModule.class.getResource("/jasperreports.properties");
        configuration.add("DefaultJaspterConfig", config);
    }

}
