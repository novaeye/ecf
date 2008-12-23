/*******************************************************************************
 * Copyright (c) 2007 Versant Corp.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Markus Kuppe (mkuppe <at> versant <dot> com) - initial API and implementation
 ******************************************************************************/
package org.eclipse.ecf.internal.provider.jslp;

import ch.ethz.iks.slp.Advertiser;
import ch.ethz.iks.slp.Locator;
import org.osgi.framework.*;
import org.osgi.util.tracker.ServiceTracker;

public class Activator implements BundleActivator {
	// The shared instance
	private static Activator plugin;
	public static final String PLUGIN_ID = "org.eclipse.ecf.provider.jslp"; //$NON-NLS-1$

	/**
	 * Returns the shared instance
	 * 
	 * @return the shared instance
	 */
	public static Activator getDefault() {
		return plugin;
	}

	// we need to keep a ref on our context
	// @see https://bugs.eclipse.org/bugs/show_bug.cgi?id=108214
	private volatile BundleContext bundleContext;

	private volatile ServiceTracker locatorSt;
	private volatile ServiceTracker advertiserSt;

	/**
	 * The constructor
	 */
	public Activator() {
		plugin = this;
	}

	public Bundle getBundle() {
		return bundleContext.getBundle();
	}

	public LocatorDecorator getLocator() {
		locatorSt.open();
		Locator aLocator = (Locator) locatorSt.getService();
		if (aLocator == null) {
			return new NullPatternLocator();
		}
		return new LocatorDecoratorImpl(aLocator);
	}

	public Advertiser getAdvertiser() {
		advertiserSt.open();
		Advertiser advertiser = (Advertiser) advertiserSt.getService();
		if (advertiser == null) {
			return new NullPatternAdvertiser();
		}
		return advertiser;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.core.runtime.Plugins#start(org.osgi.framework.BundleContext)
	 */
	public void start(final BundleContext context) throws Exception {
		bundleContext = context;

		// initially get the locator and add a life cycle listener
		locatorSt = new ServiceTracker(context, Locator.class.getName(), null);

		// initially get the advertiser and add a life cycle listener
		advertiserSt = new ServiceTracker(context, Advertiser.class.getName(), null);

	}

	public BundleContext getContext() {
		return bundleContext;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.core.runtime.Plugin#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext context) throws Exception {
		//TODO-mkuppe here we should do something like a deregisterAll(), but see ungetService(...);
		plugin = null;
		bundleContext = null;
		if (advertiserSt != null) {
			advertiserSt.close();
			advertiserSt = null;
		}
		if (locatorSt != null) {
			locatorSt.close();
			locatorSt = null;
		}
	}
}
