/*
 *  Apache License
 *  Version 2.0, January 2004
 *  http://www.apache.org/licenses/
 *
 *  Copyright 2008 by chenillekit.org
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 */

package org.chenillekit.access.services.impl;

/**
 * Plaintext implementation of PasswordEncoder.
 * <p/>
 * As callers may wish to extract the password and salts separately from the encoded password, the salt must
 * not contain reserved characters (specifically '{' and '}').
 *
 * @author colin sampaleanu
 * @author Ben Alex
 * @version $Id$
 */
public class PlaintextPasswordEncoder extends BasePasswordEncoder
{
    private boolean ignorePasswordCase = false;

    public String encodePassword(String rawPass, Object salt)
    {
        return mergePasswordAndSalt(rawPass, salt, true);
    }

    public boolean isIgnorePasswordCase()
    {
        return ignorePasswordCase;
    }

    public boolean isPasswordValid(String encPass, String rawPass, Object salt)
    {
        String pass1 = encPass + "";

        // Strict delimiters is false because pass2 never persisted anywhere
        // and we want to avoid unnecessary exceptions as a result (the
        // authentication will fail as the encodePassword never allows them)
        String pass2 = mergePasswordAndSalt(rawPass, salt, false);

        if (!ignorePasswordCase)
        {
            return pass1.equals(pass2);
        }
        else
        {
            return pass1.equalsIgnoreCase(pass2);
        }
    }

    /**
     * Demerges the previously {@link #encodePassword(String, Object)}<code>String</code>.<P>The resulting
     * array is guaranteed to always contain two elements. The first is the password, and the second is the salt.</p>
     * <P>Throws an exception if <code>null</code> or an empty <code>String</code> is passed to the method.</p>
     *
     * @param password from {@link #encodePassword(String, Object)}
     *
     * @return an array containing the password and salt
     */
    public String[] obtainPasswordAndSalt(String password)
    {
        return demergePasswordAndSalt(password);
    }

    /**
     * Indicates whether the password comparison is case sensitive.<P>Defaults to <code>false</code>, meaning
     * an exact case match is required.</p>
     *
     * @param ignorePasswordCase set to <code>true</code> for less stringent comparison
     */
    public void setIgnorePasswordCase(boolean ignorePasswordCase)
    {
        this.ignorePasswordCase = ignorePasswordCase;
    }
}
