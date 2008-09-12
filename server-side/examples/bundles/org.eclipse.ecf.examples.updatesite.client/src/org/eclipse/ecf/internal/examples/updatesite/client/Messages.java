/****************************************************************************
 * Copyright (c) 2008 Composent, Inc. and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Composent, Inc. - initial API and implementation
 *****************************************************************************/

package org.eclipse.ecf.internal.examples.updatesite.client;

import org.eclipse.osgi.util.NLS;

/**
 *
 */
public class Messages extends NLS {
	private static final String BUNDLE_NAME = "org.eclipse.ecf.internal.examples.updatesite.client.messages"; //$NON-NLS-1$
	public static String UpdateSiteServiceAccessHandler_OPEN_BROWSER_MENU_TEXT;
	public static String UpdateSiteServiceAccessHandler_OPEN_INSTALLER_MENU_TEXT;
	public static String UpdateSiteServiceAccessHandler_UPDATESITE_INDEX_HTML;
	public static String UpdateSiteServiceAccessHandler_UPDATESITE_SERVICE;
	static {
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}

	private Messages() {
		// nothing
	}
}