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

package org.chenillekit.quartz;

import org.chenillekit.quartz.services.QuartzSchedulerManager;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;
import org.quartz.SchedulerException;

/**
 * @author <a href="mailto:homburgs@googlemail.com">shomburg</a>
 * @version $Id$
 */
public class TestSimpleJob extends AbstractIOCTest
{
    @BeforeSuite
    public final void setup_registry()
    {
        super.setup_registry(ChenilleKitQuartzTestModule.class);
    }

    @Test
    public void test_simple_job() throws InterruptedException
    {
        QuartzSchedulerManager manager = getService(QuartzSchedulerManager.class);

        Thread.sleep(2000);

        try
        {
            assertTrue(manager.getScheduler().getMetaData().numJobsExecuted() > 2);
        }
        catch (SchedulerException e)
        {
            throw new RuntimeException(e);
        }
    }
}
