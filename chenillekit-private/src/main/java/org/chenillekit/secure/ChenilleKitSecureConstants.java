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

/**
 *
 */

package org.chenillekit.secure;

/**
 * some constants for chenillekit secure module.
 *
 * @author <a href="mailto:mlusetti@gmail.com">M.Lusetti</a>
 * @version $Id$
 */
public class ChenilleKitSecureConstants
{
    /**
     * The logical name of the login page
     */
    public static final String LOGIN_PAGE = "chenillekit.secure-login-page";

    /**
     * Meta data key applied to pages that may only be accessed after a succesfull login.
     */
    public static final String PRIVATE_PAGE = "chenillekit.secure-private-page";
    public static final String PRIVATE_PAGE_ROLE = "chenillekit.secure-private-page-role";
    public static final String PRIVATE_PAGE_GROUP = "chenillekit.secure-private-page-group";

}
