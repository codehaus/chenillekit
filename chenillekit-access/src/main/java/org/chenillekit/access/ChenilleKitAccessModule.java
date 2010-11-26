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

package org.chenillekit.access;

import org.apache.tapestry5.ioc.Configuration;
import org.apache.tapestry5.ioc.MappedConfiguration;
import org.apache.tapestry5.ioc.OrderedConfiguration;
import org.apache.tapestry5.ioc.ServiceBinder;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.ioc.annotations.InjectService;
import org.apache.tapestry5.ioc.annotations.Marker;
import org.apache.tapestry5.ioc.annotations.Symbol;
import org.apache.tapestry5.ioc.services.PipelineBuilder;
import org.apache.tapestry5.ioc.services.TypeCoercer;
import org.apache.tapestry5.services.ApplicationStateContribution;
import org.apache.tapestry5.services.ApplicationStateCreator;
import org.apache.tapestry5.services.ApplicationStateManager;
import org.apache.tapestry5.services.ComponentClassResolver;
import org.apache.tapestry5.services.ComponentClassTransformWorker;
import org.apache.tapestry5.services.ComponentRequestFilter;
import org.apache.tapestry5.services.ComponentSource;
import org.apache.tapestry5.services.Cookies;
import org.apache.tapestry5.services.LibraryMapping;
import org.chenillekit.access.annotations.ChenilleKitAccess;
import org.chenillekit.access.dao.JDBCProtectionRuleDAO;
import org.chenillekit.access.dao.ProtectionRuleDAO;
import org.chenillekit.access.services.AccessValidator;
import org.chenillekit.access.services.AuthenticationService;
import org.chenillekit.access.services.AuthenticationServiceFilter;
import org.chenillekit.access.services.RedirectService;
import org.chenillekit.access.services.SecurityService;
import org.chenillekit.access.services.impl.AccessValidatorImpl;
import org.chenillekit.access.services.impl.ComponentRequestAccessFilter;
import org.chenillekit.access.services.impl.CookieRedirectAccessFilter;
import org.chenillekit.access.services.impl.ManagedRestrictedWorker;
import org.chenillekit.access.services.impl.RedirectServiceImpl;
import org.chenillekit.access.services.impl.RestrictedWorker;
import org.chenillekit.access.services.impl.SecurityServiceImpl;
import org.slf4j.Logger;

import java.io.IOException;
import java.sql.Connection;
import java.util.List;
import java.util.Properties;
import java.util.Set;

/**
 * Main Module class for ChenilleKitAccess.
 *
 * @version $Id$
 */
public class ChenilleKitAccessModule
{
	/**
	 * Binding via fluent API by T5 {@link ServiceBinder}
	 *
	 * @param binder
	 */
	@SuppressWarnings("unchecked")
	public static void bind(ServiceBinder binder)
	{
		binder.bind(ComponentRequestFilter.class, ComponentRequestAccessFilter.class).withMarker(ChenilleKitAccess.class);
		binder.bind(RedirectService.class, RedirectServiceImpl.class);
		binder.bind(SecurityService.class, SecurityServiceImpl.class);
	}

	public static ProtectionRuleDAO<?> buildJDBCProtectionRuleDAO(Logger logger,
															   Connection connection,
															   @Inject @Symbol("tableName") String tableName)
	{
		return new JDBCProtectionRuleDAO(logger, connection, tableName);
	}

	/**
	 * Build a pipeline service around the {@link AuthenticationService} so we
	 * may have more then one implementation decide how and when to authenticate.
	 *
	 * @param configuration {@link List} of filters to insert into the pipeline
	 * @param builder	   {@link PipelineBuilder} from Tapestr5 IoC
	 * @param logger		{@link Logger} configured by the Tapestry5 IoC
	 *
	 * @return the facade service acting as a pipeline through the contributed
	 *         implementations
	 */
	public static AuthenticationService build(@InjectService("PipelineBuilder") PipelineBuilder builder,
											  final ApplicationStateManager stateManager,
											  final List<AuthenticationServiceFilter> configuration,
											  Logger logger)
	{
		AuthenticationService terminator = new AuthenticationService()
		{
			public WebSessionUser<?> doAuthenticate(String userName, String password)
			{
				// Return a null so the service can fail if no other contributions are made
				return null;
			}

			public boolean isAuthenticate()
			{
				// Return false so the service can fail if no other contributions are made
				WebSessionUser<?> webSessionUser = stateManager.getIfExists(WebSessionUser.class);
				return webSessionUser != null;
			}
		};

		return builder.build(logger, AuthenticationService.class, AuthenticationServiceFilter.class, configuration, terminator);
	}

	/**
	 * @param configuration
	 */
	public static void contributeApplicationStateManager(
			MappedConfiguration<Class<?>, ApplicationStateContribution> configuration)
	{
		ApplicationStateCreator<WebSessionUser<?>> creator = new ApplicationStateCreator<WebSessionUser<?>>()
		{
			public WebSessionUser<?> create()
			{
				// It sounds better to throw an IllegaAccess
				// but Error and Exceptions are for other use case
				// as declared in the respective javadocs
				throw new IllegalStateException("WebSessionUser must be provided, not instantiated");
			}
		};

		// FIXME Is "session" string available as a constants from Tapestry?
		configuration.add(WebSessionUser.class, new ApplicationStateContribution("session", creator));
	}

	/**
	 * Contribute our {@link ComponentClassTransformWorker} to transformation pipeline to add our code to
	 * loaded classes
	 *
	 * @param configuration component class transformer configuration
	 */
	public static void contributeComponentClassTransformWorker(
			OrderedConfiguration<ComponentClassTransformWorker> configuration)
	{
		configuration.addInstance("Restricted", RestrictedWorker.class, "after:Secure");
		configuration.addInstance("ManagedRestricted", ManagedRestrictedWorker.class, "after:Restricted");
	}

	/**
	 * Contribute our virtual folder to {@link ComponentClassResolver} service
	 *
	 * @param configuration configuration for the service where we contribute to
	 */
	public static void contributeComponentClassResolver(Configuration<LibraryMapping> configuration)
	{
		configuration.add(new LibraryMapping("ckaccess", "org.chenillekit.access"));
	}

	/**
	 * @param logger		  system logger
	 * @param componentSource component source
	 * @param manager		 application state manager
	 *
	 * @return build access validator
	 */
	@Marker(ChenilleKitAccess.class)
	public static AccessValidator buildAccessValidator(ComponentSource componentSource,
													   Logger logger,
													   ApplicationStateManager manager)
	{
		return new AccessValidatorImpl(logger, componentSource, manager);
	}

	/**
	 * @param configuration
	 * @param accessFilter
	 */
	public static void contributeComponentRequestHandler(OrderedConfiguration<ComponentRequestFilter> configuration,
														 @ChenilleKitAccess ComponentRequestFilter accessFilter,
														 Cookies cookies, RedirectService redirect, TypeCoercer coercer,
														 ComponentSource componentSource)
	{
		configuration.add("AccessControl", accessFilter, "before:*");

		CookieRedirectAccessFilter cookieFilter = new CookieRedirectAccessFilter(cookies, redirect, coercer, componentSource);

		configuration.add("CookieRedirect", cookieFilter, "after:AccessControl");
	}

	/**
	 * @param configuration
	 */
	public static void contributeFactoryDefaults(MappedConfiguration<String, String> configuration)
	{
		Properties prop = new Properties();
		try
		{
			prop.load(ChenilleKitAccessModule.class.getResourceAsStream("/chenillekit-access.properties"));
		}
		catch (IOException e)
		{
			throw new RuntimeException(e);
		}

		Set<Object> keys = prop.keySet();
		for (Object key : keys)
		{
			Object value = prop.get(key);
			configuration.add(key.toString(), value.toString());
		}
	}
}
