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
import java.util.List;

import org.apache.lucene.document.Document;

import org.chenillekit.test.AbstractTestSuite;
import org.testng.annotations.Test;

/**
 * @version $Id$
 */
public class LuceneSearcherServiceTest extends AbstractTestSuite
{
	@Test
    public void query_records() throws IOException
    {
        SearcherService service = registry.getService(SearcherService.class);
        List<Document> docs = service.search("content", "manufacturers OR \"British Government Warehouse\"", 200);
        assertEquals(docs.size(), 199);
    }
}