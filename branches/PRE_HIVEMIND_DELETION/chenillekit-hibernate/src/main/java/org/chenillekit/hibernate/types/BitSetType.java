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

package org.chenillekit.hibernate.types;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.BitSet;

import org.hibernate.HibernateException;
import org.hibernate.usertype.UserType;

/**
 * @version $Id$
 */
public class BitSetType implements UserType
{
	public int[] sqlTypes()
	{
		return new int[]{Types.INTEGER};
	}

	public Class returnedClass()
	{
		return BitSet.class;
	}

	public boolean equals(Object x, Object y)
	{
		return (x == y)
				|| (x != null
				&& y != null
				&& java.util.Arrays.equals((byte[]) x, (byte[]) y));
	}

	/**
	 * Retrieve an instance of the mapped class from a JDBC resultset. Implementors
	 * should handle possibility of null values.
	 *
	 * @param rs	a JDBC result set
	 * @param names the column names
	 * @param owner the containing entity
	 *
	 * @return Object
	 *
	 * @throws org.hibernate.HibernateException
	 *
	 * @throws java.sql.SQLException
	 */
	public Object nullSafeGet(ResultSet rs, String[] names, Object owner) throws HibernateException, SQLException
	{
		return rs.getInt(names[0]);
	}

	/**
	 * Write an instance of the mapped class to a prepared statement. Implementors
	 * should handle possibility of null values. A multi-column type should be written
	 * to parameters starting from <tt>index</tt>.
	 *
	 * @param st	a JDBC prepared statement
	 * @param value the object to write
	 * @param index statement parameter index
	 *
	 * @throws org.hibernate.HibernateException
	 *
	 * @throws java.sql.SQLException
	 */
	public void nullSafeSet(PreparedStatement st, Object value, int index) throws HibernateException, SQLException
	{
		st.setInt(index, (Integer) value);
	}

	/**
	 * Return a deep copy of the persistent state, stopping at entities and at
	 * collections. It is not necessary to copy immutable objects, or null
	 * values, in which case it is safe to simply return the argument.
	 *
	 * @param value the object to be cloned, which may be null
	 *
	 * @return Object a copy
	 */
	public Object deepCopy(Object value) throws HibernateException
	{
		return value;
	}

	/**
	 * Are objects of this type mutable?
	 *
	 * @return boolean
	 */
	public boolean isMutable()
	{
		return true;
	}

	/**
	 * Get a hashcode for the instance, consistent with persistence "equality"
	 */
	public int hashCode(Object x) throws HibernateException
	{
		return x.hashCode();
	}

	/**
	 * Transform the object into its cacheable representation. At the very least this
	 * method should perform a deep copy if the type is mutable. That may not be enough
	 * for some implementations, however; for example, associations must be cached as
	 * identifier values. (optional operation)
	 *
	 * @param value the object to be cached
	 *
	 * @return a cachable representation of the object
	 *
	 * @throws org.hibernate.HibernateException
	 *
	 */
	public Serializable disassemble(Object value) throws HibernateException
	{
		return (Serializable) deepCopy(value);
	}

	/**
	 * Reconstruct an object from the cacheable representation. At the very least this
	 * method should perform a deep copy if the type is mutable. (optional operation)
	 *
	 * @param cached the object to be cached
	 * @param owner  the owner of the cached object
	 *
	 * @return a reconstructed object from the cachable representation
	 *
	 * @throws org.hibernate.HibernateException
	 *
	 */
	public Object assemble(Serializable cached, Object owner) throws HibernateException
	{
		return deepCopy(cached);
	}

	/**
	 * During merge, replace the existing (target) value in the entity we are merging to
	 * with a new (original) value from the detached entity we are merging. For immutable
	 * objects, or null values, it is safe to simply return the first parameter. For
	 * mutable objects, it is safe to return a copy of the first parameter. For objects
	 * with component values, it might make sense to recursively replace component values.
	 *
	 * @param original the value from the detached entity being merged
	 * @param target   the value in the managed entity
	 *
	 * @return the value to be merged
	 */
	public Object replace(Object original, Object target, Object owner) throws HibernateException
	{
		return deepCopy(original);
	}
}