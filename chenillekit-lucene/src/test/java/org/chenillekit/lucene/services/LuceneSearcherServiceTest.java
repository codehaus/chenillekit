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

package org.chenillekit.lucene.services;

import java.io.IOException;
import java.util.Iterator;

import org.apache.lucene.search.Hit;
import org.apache.lucene.search.Hits;

import org.testng.annotations.Test;

/**
 * @author <a href="mailto:homburgs@googlemail.com">shomburg</a>
 * @version $Id$
 */
public class LuceneSearcherServiceTest extends AbstractTestSuite
{
    @Test
    public void query_records() throws IOException
    {
        SearcherService service = registry.getService(SearcherService.class);
        Hits hits = (Hits) service.search("name", "Sven OR Lusetti");
        Iterator<Hit> hitsIterator = hits.iterator();
        while (hitsIterator.hasNext())
        {
            Hit hit = hitsIterator.next();
            System.err.println(String.format("%f - %f - %s", hit.getBoost(), hit.getScore(), hit.getDocument().get("name")));
        }

        assertEquals(hits.length(), 2);
    }
}