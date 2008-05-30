/*******************************************************************************
 * Copyright (c) 2008 Versant Corp.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Markus Kuppe (mkuppe <at> versant <dot> com) - initial API and implementation
 ******************************************************************************/
package org.eclipse.ecf.internal.provider.jslp;

import ch.ethz.iks.slp.*;
import java.util.*;
import org.eclipse.ecf.core.util.Trace;

public class NullPatternLocator implements LocatorDecorator {
	private final ServiceLocationEnumeration emptyServiceLocationEnumeration = new ServiceLocationEnumeration() {
		public Object next() throws ServiceLocationException {
			throw new ServiceLocationException(ServiceLocationException.INTERNAL_SYSTEM_ERROR, "no elements"); //$NON-NLS-1$
		}

		public boolean hasMoreElements() {
			return false;
		}

		public Object nextElement() {
			throw new NoSuchElementException();
		}
	};

	/* (non-Javadoc)
	 * @see ch.ethz.iks.slp.Locator#findAttributes(ch.ethz.iks.slp.ServiceURL, java.util.List, java.util.List)
	 */
	public ServiceLocationEnumeration findAttributes(ServiceURL url, List scopes, List attributeIds) {
		return emptyServiceLocationEnumeration;
	}

	/* (non-Javadoc)
	 * @see ch.ethz.iks.slp.Locator#findAttributes(ch.ethz.iks.slp.ServiceType, java.util.List, java.util.List)
	 */
	public ServiceLocationEnumeration findAttributes(ServiceType type, List scopes, List attributeIds) {
		Trace.trace(Activator.PLUGIN_ID, JSLPDebugOptions.METHODS_TRACING, getClass(), "findAttributes(ch.ethz.iks.slp.ServiceType, java.util.List, java.util.List)", Locator.class + " not present"); //$NON-NLS-1$//$NON-NLS-2$
		return emptyServiceLocationEnumeration;
	}

	/* (non-Javadoc)
	 * @see ch.ethz.iks.slp.Locator#findServiceTypes(java.lang.String, java.util.List)
	 */
	public ServiceLocationEnumeration findServiceTypes(String namingAuthority, List scopes) {
		Trace.trace(Activator.PLUGIN_ID, JSLPDebugOptions.METHODS_TRACING, getClass(), "findServiceTypes(String, List)", Locator.class + " not present"); //$NON-NLS-1$//$NON-NLS-2$
		return emptyServiceLocationEnumeration;
	}

	/* (non-Javadoc)
	 * @see ch.ethz.iks.slp.Locator#findServices(ch.ethz.iks.slp.ServiceType, java.util.List, java.lang.String)
	 */
	public ServiceLocationEnumeration findServices(ServiceType type, List scopes, String searchFilter) throws IllegalArgumentException {
		Trace.trace(Activator.PLUGIN_ID, JSLPDebugOptions.METHODS_TRACING, getClass(), "findServies(ServiceType, List, String)", Locator.class + " not present"); //$NON-NLS-1$//$NON-NLS-2$
		return emptyServiceLocationEnumeration;
	}

	/* (non-Javadoc)
	 * @see ch.ethz.iks.slp.Locator#getLocale()
	 */
	public Locale getLocale() {
		Trace.trace(Activator.PLUGIN_ID, JSLPDebugOptions.METHODS_TRACING, getClass(), "getLocale()", Locator.class + " not present"); //$NON-NLS-1$//$NON-NLS-2$
		return Locale.getDefault();
	}

	/* (non-Javadoc)
	 * @see ch.ethz.iks.slp.Locator#setLocale(java.util.Locale)
	 */
	public void setLocale(Locale locale) {
		Trace.trace(Activator.PLUGIN_ID, JSLPDebugOptions.METHODS_TRACING, getClass(), "setLocale(Locale)", Locator.class + " not present"); //$NON-NLS-1$//$NON-NLS-2$
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ecf.internal.provider.jslp.LocatorDecorator#getServiceURLs(ch.ethz.iks.slp.ServiceType, java.util.List)
	 */
	public Map getServiceURLs(ServiceType serviceType, List scopes) {
		Trace.trace(Activator.PLUGIN_ID, JSLPDebugOptions.METHODS_TRACING, getClass(), "getServiceURLs(ServiceType, List scopes", Locator.class + " not present"); //$NON-NLS-1$//$NON-NLS-2$
		return new HashMap();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ecf.internal.provider.jslp.LocatorDecorator#getServiceURLs()
	 */
	public Map getServiceURLs() {
		Trace.trace(Activator.PLUGIN_ID, JSLPDebugOptions.METHODS_TRACING, getClass(), "getServiceURLs()", Locator.class + " not present"); //$NON-NLS-1$//$NON-NLS-2$
		return new HashMap();
	}
}
