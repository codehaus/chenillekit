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
 * Implements indexer based on <a href="http://lucene.apache.org/java/docs/index.html">lucene</a>.
 *
 * @version $Id$
 */
public interface IndexerService
{
    /**
     * Add a document to the standard index.
     *
     * @param document
     * @deprecated use {@link addDocument(String jsonDocument)}
     */
    void addDocument(Document document);
    
    /**
	 * Add a document, in the <a href="http://json.org">JSON</a>
	 * string form, to the index.
	 * Use the supplied {@link String} to build up a {@link JSONObject}.
	 * The format is as follows:
	 * <ul>
	 * <li>A simple key/value pairs to store {@link Fieldable} keys and values</li>
	 * <li>If the value of the {@link Fieldable} can be parsed as a {@link JSONObject}
	 * it will and a specific <code>value</code> key is searched for the value
	 * of the key and <code>boost, store, index, termvector</code> to specify their
	 * own values</li>
	 * <li>A specific <code>boost</code> key parameter to boost value
	 * for the whole {@link Document}</li>
	 * </ul>ann use the specific <code>index_parameters</code> key value to
	 * build a second {@link JSONObject} with specific {@link Document}
	 * parameters (.
	 * 
	 * @param document
	 */
	void addDocument(String jsonDocument);

    /**
     * Delete documents by the given field name and the query.
     *
     * @param field       name of the field
     * @param queryString
     */
    void delDocuments(String field, String queryString);

    /**
     * Get the number of documents stored in the disk index. Be aware the
     * presumably this methods delegates to a <code>synchronized</code>
     * methods.
     *
     * @return the number of documents indexed
     */
    int getDocCount();
    
    /**
     * Get the number of documents stored in the index counting
     * the deletions. Be aware the
     * presumably this methods delegates to a <code>synchronized</code>
     * methods.
     * This methods should never return a checked exception.
     * 
     * @return the number of documents indexed
     */
    int getDocCountWithDeletions();

    /**
     * Force a commit of changes to the index
     */
    void commit();
}
