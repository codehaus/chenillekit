/*
 * Apache License
 * Version 2.0, January 2004
 * http://www.apache.org/licenses/
 *
 * Copyright 2009 by chenillekit.org
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 */

package org.chenillekit.image.services;

import java.awt.image.BufferedImage;

/**
 * captcha producer.
 *
 * @version $Id$
 */
public interface CaptchaProducer
{
	/**
	 * create the captch from given string.
	 */
	BufferedImage createImage(String captchaString);

	/**
	 * create a random String.
	 */
	String createText();
}
