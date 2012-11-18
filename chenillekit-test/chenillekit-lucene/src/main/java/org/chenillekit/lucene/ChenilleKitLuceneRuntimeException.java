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

package org.chenillekit.lucene;

/**
 * Specific {@link RuntimeException} for the module
 * @version $Id$
 */
public class ChenilleKitLuceneRuntimeException extends RuntimeException
{
	private static final long serialVersionUID = -8644498626258111148L;

	public ChenilleKitLuceneRuntimeException() { }

	public ChenilleKitLuceneRuntimeException(String message) { super(message); }

	public ChenilleKitLuceneRuntimeException(Throwable cause) { super(cause); }

	public ChenilleKitLuceneRuntimeException(String message, Throwable cause) { super(message, cause); }

}
