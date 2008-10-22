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

import org.apache.lucene.document.Document;

/**
 * implements indexer based on <a href="http://lucene.apache.org/java/docs/index.html">lucene</a>.
 *
 * @author <a href="mailto:homburgs@gmail.com">S.Homburg</a>
 * @version $Id$
 */
public interface IndexerService
{
    /**
     * add a document to the standard index.
     *
     * @param document
     */
    void addDocument(Document document);

    /**
     * delete documents by the given field name and the query.
     *
     * @param field       name of the field
     * @param queryString
     */
    void delDocuments(String field, String queryString);

    /**
     * get the amount of documents stored in the disk index.
     *
     * @return amount of documents
     */
    int getDocCount();
    
    /**
     * Force a commit of changes to the index 
     */
    void commit();
}
