/****************************************************************************
 * Copyright (c) 2007 Composent, Inc. and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Composent, Inc. - initial API and implementation
 *****************************************************************************/

package org.eclipse.ecf.discovery.identity;

import org.eclipse.ecf.core.identity.IDCreateException;
import org.eclipse.ecf.core.identity.IDFactory;
import org.eclipse.ecf.core.identity.Namespace;

/**
 * ServiceIDFactory implementation.
 */
public class ServiceIDFactory implements IServiceIDFactory {

	private static final IServiceIDFactory instance = new ServiceIDFactory();

	public static IServiceIDFactory getDefault() {
		return instance;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ecf.discovery.identity.IServiceIDFactory#createServiceID(org.eclipse.ecf.core.identity.Namespace, org.eclipse.ecf.discovery.identity.IServiceTypeID, java.lang.String)
	 */
	public IServiceID createServiceID(Namespace namespace, IServiceTypeID serviceType, String serviceName) throws IDCreateException {
		return (IServiceID) IDFactory.getDefault().createID(namespace, new Object[] {serviceType, serviceName});
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ecf.discovery.identity.IServiceIDFactory#createServiceID(org.eclipse.ecf.core.identity.Namespace, java.lang.String, java.lang.String)
	 */
	public IServiceID createServiceID(Namespace namespace, String serviceType, String serviceName) throws IDCreateException {
		return (IServiceID) IDFactory.getDefault().createID(namespace, new Object[] {serviceType, serviceName});
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ecf.discovery.identity.IServiceIDFactory#createServiceID(org.eclipse.ecf.core.identity.Namespace, java.lang.String)
	 */
	public IServiceID createServiceID(Namespace namespace, String serviceType) throws IDCreateException {
		return this.createServiceID(namespace, serviceType, null);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ecf.discovery.identity.IServiceIDFactory#createServiceID(org.eclipse.ecf.core.identity.Namespace, org.eclipse.ecf.discovery.identity.IServiceTypeID)
	 */
	public IServiceID createServiceID(Namespace namespace, IServiceTypeID serviceType) throws IDCreateException {
		return this.createServiceID(namespace, serviceType, null);
	}
}