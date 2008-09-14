/****************************************************************************
 * Copyright (c) 2004 Composent, Inc. and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Composent, Inc. - initial API and implementation
 *****************************************************************************/
package org.eclipse.ecf.ui.util;

import java.net.MalformedURLException;
import java.net.URL;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.browser.IWebBrowser;
import org.eclipse.ui.browser.IWorkbenchBrowserSupport;

public class BrowserHelper {

	private static final String ECF_BROWSER = "ECF Browser";

	IWebBrowser browser;

	public BrowserHelper(String id) throws PartInitException {
		IWorkbenchBrowserSupport support = PlatformUI.getWorkbench()
				.getBrowserSupport();
		browser = support.createBrowser(IWorkbenchBrowserSupport.LOCATION_BAR
				| IWorkbenchBrowserSupport.NAVIGATION_BAR, id, ECF_BROWSER,
				ECF_BROWSER);
	}

	public BrowserHelper() throws PartInitException {
		this(null);
	}

	public void openURL(final String url) {
		Display.getDefault().asyncExec(new Runnable() {
			public void run() {
				try {
					IWorkbenchBrowserSupport support = PlatformUI
							.getWorkbench().getBrowserSupport();
					URL anURL = new URL(url);
					support.createBrowser(
							IWorkbenchBrowserSupport.LOCATION_BAR
									| IWorkbenchBrowserSupport.NAVIGATION_BAR,
							anURL.toExternalForm(), null, null).openURL(anURL);
				} catch (MalformedURLException e) {
					MessageDialog.openError(PlatformUI.getWorkbench()
							.getDisplay().getActiveShell(),
							"Malformed URL Error", e.getLocalizedMessage());
				} catch (Exception e) {
					MessageDialog
							.openError(PlatformUI.getWorkbench().getDisplay()
									.getActiveShell(),
									"Unexpected Browser Error", e
											.getLocalizedMessage());
				}
			}
		});
	}
}