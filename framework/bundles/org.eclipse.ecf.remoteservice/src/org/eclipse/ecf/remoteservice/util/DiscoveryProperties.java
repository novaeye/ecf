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

package org.eclipse.ecf.remoteservice.util;

import java.util.Properties;
import org.eclipse.core.runtime.Assert;
import org.eclipse.ecf.core.IContainer;
import org.eclipse.ecf.core.identity.ID;
import org.eclipse.ecf.remoteservice.Constants;

/**
 *
 */
public class DiscoveryProperties extends Properties {

	private static final long serialVersionUID = -6293580853756989675L;

	public DiscoveryProperties(String clazz, String containerFactory, IContainer container) {
		Assert.isNotNull(container);
		Assert.isNotNull(clazz);
		ID connectedID = container.getConnectedID();
		String connectNamespace = (connectedID == null) ? container.getConnectNamespace().getName() : connectedID.getNamespace().getName();
		String connectID = (connectedID == null) ? null : connectedID.getName();
		ID containerID = container.getID();
		String targetNamespace = (containerID == null) ? null : containerID.getNamespace().getName();
		String targetID = (containerID == null) ? null : containerID.getName();
		put(Constants.DISCOVERY_OBJECTCLASS_PROPERTY, clazz);
		Assert.isNotNull(containerFactory);
		put(Constants.DISCOVERY_CONTAINER_FACTORY_PROPERTY, containerFactory);
		if (connectNamespace != null)
			put(Constants.DISCOVERY_CONNECT_ID_NAMESPACE_PROPERTY, connectNamespace);
		if (connectID != null)
			put(Constants.DISCOVERY_CONNECT_ID_PROPERTY, connectID);
		if (targetNamespace != null)
			put(Constants.DISCOVERY_SERVICE_ID_NAMESPACE_PROPERTY, targetNamespace);
		if (targetID != null)
			put(Constants.DISCOVERY_SERVICE_ID_PROPERTY, targetID);
	}

	public DiscoveryProperties(String clazz, String containerFactory, String connectNamespace, String connectID, String targetNamespace, String targetID) {
		Assert.isNotNull(clazz);
		put(Constants.DISCOVERY_OBJECTCLASS_PROPERTY, clazz);
		Assert.isNotNull(containerFactory);
		put(Constants.DISCOVERY_CONTAINER_FACTORY_PROPERTY, containerFactory);
		if (connectNamespace != null)
			put(Constants.DISCOVERY_CONNECT_ID_NAMESPACE_PROPERTY, connectNamespace);
		if (connectID != null)
			put(Constants.DISCOVERY_CONNECT_ID_PROPERTY, connectID);
		if (targetNamespace != null)
			put(Constants.DISCOVERY_SERVICE_ID_NAMESPACE_PROPERTY, targetNamespace);
		if (targetID != null)
			put(Constants.DISCOVERY_SERVICE_ID_PROPERTY, targetID);
	}
}