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

package org.chenillekit.tapestry.core;

import org.apache.tapestry5.internal.services.ResourceCache;
import org.apache.tapestry5.ioc.Configuration;
import org.apache.tapestry5.ioc.MappedConfiguration;
import org.apache.tapestry5.ioc.ServiceBinder;
import org.apache.tapestry5.ioc.annotations.Marker;
import org.apache.tapestry5.ioc.annotations.Symbol;
import org.apache.tapestry5.services.AssetFactory;
import org.apache.tapestry5.services.BindingFactory;
import org.apache.tapestry5.services.BindingSource;
import org.apache.tapestry5.services.LibraryMapping;

import org.chenillekit.tapestry.core.factories.ListBindingFactory;
import org.chenillekit.tapestry.core.factories.LoopBindingFactory;
import org.chenillekit.tapestry.core.factories.MessageFormatBindingFactory;
import org.chenillekit.tapestry.core.factories.OgnlBindingFactory;
import org.chenillekit.tapestry.core.factories.URIAssetFactory;
import org.chenillekit.tapestry.core.services.URIAssetAliasManager;
import org.chenillekit.tapestry.core.services.URIProvider;
import org.chenillekit.tapestry.core.services.impl.URIAssetAliasManagerImpl;

/**
 * module for chenillekit web module.
 *
 * @version $Id$
 */
public class ChenilleKitCoreModule
{
	public void contributeComponentClassResolver(Configuration<LibraryMapping> configuration)
	{
		configuration.add(new LibraryMapping("chenillekit", "org.chenillekit.tapestry.core"));
		configuration.add(new LibraryMapping("ck", "org.chenillekit.tapestry.core"));
	}

	public void contributeFactoryDefaults(MappedConfiguration<String, String> configuration)
	{
		// This is designed to make it easy to keep syncrhonized with FCKeditor. As we
		// support a new version, we create a new folder, and update the path entry. We can then
		// delete the old version folder (or keep it around). This should be more manageable than
		// ovewriting the local copy with updates. There's also a ClasspathAliasManager
		// contribution based on the path.
		configuration.add("ck.components", "org/chenillekit/tapestry/core/components");
		configuration.add("ck.fckeditor", "classpath:${ck.components}/fckeditor");
	}

	public void contributeClasspathAssetAliasManager(MappedConfiguration<String, String> configuration,
															@Symbol("ck.components")String scriptPath)
	{
		configuration.add("fckeditor/", scriptPath + "/fckeditor/");
		configuration.add("window/", scriptPath + "/window/");
	}

	public void contributeBindingSource(MappedConfiguration<String, BindingFactory> configuration,
											   BindingSource bindingSource)
	{
		configuration.add("messageformat", new MessageFormatBindingFactory(bindingSource));
		configuration.add("list", new ListBindingFactory(bindingSource));
		configuration.add("loop", new LoopBindingFactory(bindingSource));
		configuration.add("ognl", new OgnlBindingFactory());
	}

	@Marker(URIProvider.class)
	public AssetFactory buildURIAssetFactory(ResourceCache resourceCache, URIAssetAliasManager aliasManager)
	{
		return new URIAssetFactory(resourceCache, aliasManager);
	}

	public void contributeAssetSource(MappedConfiguration<String, AssetFactory> configuration,
									  @URIProvider AssetFactory uriAssetFactory)
	{
		configuration.add("uri", uriAssetFactory);
	}

	public static void bind(ServiceBinder binder)
	{
		binder.bind(URIAssetAliasManager.class, URIAssetAliasManagerImpl.class);
	}

}
