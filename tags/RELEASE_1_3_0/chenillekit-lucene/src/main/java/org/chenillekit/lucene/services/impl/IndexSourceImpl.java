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

package org.chenillekit.lucene.services.impl;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Properties;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.apache.tapestry5.ioc.services.RegistryShutdownListener;
import org.chenillekit.lucene.ChenilleKitLuceneConstants;
import org.chenillekit.lucene.ChenilleKitLuceneRuntimeException;
import org.chenillekit.lucene.services.IndexSource;
import org.slf4j.Logger;


/**
 *
 *
 * @version $Id$
 */
public class IndexSourceImpl implements IndexSource, RegistryShutdownListener
{
	private final Logger logger;
	
	private final Directory directory;
	
	private final Analyzer analyzer;
	
	private final IndexWriter indexWriter;
	
	
	/**
	 * 
	 * @param logger
	 * @param configResource
	 */
	public IndexSourceImpl(final Logger logger, final List<URL> configuration, Version version)
	{
        this.logger = logger;

        if (configuration.isEmpty())
            throw new ChenilleKitLuceneRuntimeException("At least one configuration is needed for IndexSource service");
        
        try
        {
        	Properties prop = new Properties();
        	
        	for (URL url : configuration)
        	{
				prop.load(url.openStream());
			}
            
            File indexFolderFile = new File(prop.getProperty(ChenilleKitLuceneConstants.PROPERTIES_KEY_IF));
            
            boolean createFolder = Boolean.valueOf(prop.getProperty(ChenilleKitLuceneConstants.PROPERTIES_KEY_OIF, "false"));
            
            boolean enableLuceneOutput = Boolean.valueOf(prop.getProperty(ChenilleKitLuceneConstants.PROPERTIES_KEY_ELO, "false"));
            
            int maxFieldLength = Integer.valueOf(prop.getProperty(ChenilleKitLuceneConstants.PROPERTIES_KEY_MFL, "250000"));
 
            this.directory = FSDirectory.open(indexFolderFile);
            
            this.analyzer = new StandardAnalyzer(version);

            if (enableLuceneOutput)
                IndexWriter.setDefaultInfoStream(System.out);
            
            this.indexWriter = new IndexWriter(this.directory,
            		this.analyzer, createFolder,
            		new IndexWriter.MaxFieldLength(maxFieldLength));
            
        }
        catch (IOException ioe)
        {
            throw new ChenilleKitLuceneRuntimeException(ioe);
        }
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.apache.tapestry5.ioc.services.RegistryShutdownListener#registryDidShutdown()
	 */
	public void registryDidShutdown()
	{
		try
        {   
            if (this.indexWriter != null)
            	this.indexWriter.close();

            if (this.directory != null)
                this.directory.close();
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
	}



	/*
	 * (non-Javadoc)
	 * @see org.chenillekit.lucene.services.IndexSource#createIndexSearcher()
	 */
	public IndexSearcher createIndexSearcher()
	{
		try
		{
			// This way when we close the searcher we close the reader too
			return new IndexSearcher(this.directory);
		}
		catch (CorruptIndexException cie)
		{
			this.logger.error(String.format("The index result corrupted: '%s'", cie.getMessage()), cie);
			throw new ChenilleKitLuceneRuntimeException(cie);
		}
		catch (IOException ioe)
		{
			this.logger.error(String.format("Unable to access the index for building new reader, reason: '%s'", ioe.getMessage()), ioe);
			throw new ChenilleKitLuceneRuntimeException(ioe);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see org.chenillekit.lucene.services.IndexSource#getAnalyzer()
	 */
	public Analyzer getAnalyzer()
	{
		return this.analyzer;
	}

	/*
	 * (non-Javadoc)
	 * @see org.chenillekit.lucene.services.IndexSource#getIndexWriter()
	 */
	public IndexWriter getIndexWriter()
	{
		return this.indexWriter;
	}
	

}
