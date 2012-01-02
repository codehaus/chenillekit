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

package org.chenillekit.quartz.tests;

import org.apache.tapestry5.ioc.OrderedConfiguration;
import org.apache.tapestry5.ioc.ServiceBinder;
import org.apache.tapestry5.ioc.annotations.SubModule;
import org.chenillekit.quartz.ChenilleKitQuartzModule;
import org.chenillekit.quartz.services.JobSchedulingBundle;
import org.chenillekit.quartz.services.impl.SimpleJobSchedulingBundleImpl;
import org.chenillekit.quartz.tests.services.DummyService;
import org.chenillekit.quartz.tests.services.impl.DummyServiceImpl;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.Trigger;
import org.quartz.TriggerUtils;

import java.net.URL;

/**
 * @version $Id$
 */
@SubModule(value = {ChenilleKitQuartzModule.class})
public class ChenilleKitQuartzTestModule
{
	public static final String TEST_STRING_KEY = "TestString";
	public static final String TEST_STRING_VALUE = "Wow that's nice";

	public static final String DUMMY_SERVICE = "DummyService";


	public static void bind(ServiceBinder binder)
	{
		binder.bind(DummyService.class, DummyServiceImpl.class);
	}

	/**
	 * @param configuration
	 */
    public static void contributeQuartzSchedulerManager(OrderedConfiguration<JobSchedulingBundle> configuration,
    				DummyService dummy)
    {
        JobDetail myTestDetail = new JobDetail("MyTestJob", "MyTestGroup", MyTestJob.class);

        JobDataMap map = new JobDataMap();
        map.put(TEST_STRING_KEY, TEST_STRING_VALUE);
        map.put(DUMMY_SERVICE, dummy);

        myTestDetail.setJobDataMap(map);

        Trigger myTestTrigger = TriggerUtils.makeSecondlyTrigger();
        myTestTrigger.setName("MyTestTrigger");

        configuration.add("MyFirstJob", new SimpleJobSchedulingBundleImpl(myTestDetail, myTestTrigger));
    }

    public static void contributeSchedulerFactory(OrderedConfiguration<URL> configuration)
    {
    	URL propfile = ChenilleKitQuartzTestModule.class.getResource("/quartz.properties");

    	configuration.add("TestConfig", propfile);
    }


}
