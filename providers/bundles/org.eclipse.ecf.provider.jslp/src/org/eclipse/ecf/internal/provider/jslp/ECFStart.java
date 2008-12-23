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

import java.util.Properties;
import org.eclipse.core.runtime.*;
import org.eclipse.ecf.core.ContainerConnectException;
import org.eclipse.ecf.core.identity.IDCreateException;
import org.eclipse.ecf.core.identity.IDFactory;
import org.eclipse.ecf.core.start.IECFStart;
import org.eclipse.ecf.core.util.Trace;
import org.eclipse.ecf.discovery.service.IDiscoveryService;
import org.eclipse.ecf.provider.jslp.container.JSLPDiscoveryContainer;
import org.osgi.framework.*;

public class ECFStart implements IECFStart {

	public ECFStart() {
		// 
	}

	public IStatus run(IProgressMonitor arg0) {
		try {
			// register ourself as an OSGi service
			Properties props = new Properties();
			props.put(IDiscoveryService.CONTAINER_ID, IDFactory.getDefault().createStringID("org.eclipse.ecf.provider.jslp.container.JSLPDiscoveryContainer")); //$NON-NLS-1$
			props.put(IDiscoveryService.CONTAINER_NAME, JSLPDiscoveryContainer.NAME);
			props.put(Constants.SERVICE_RANKING, new Integer(500));

			Activator.getDefault().getContext().registerService(IDiscoveryService.class.getName(), new ServiceFactory() {
				private volatile JSLPDiscoveryContainer jdc;

				/* (non-Javadoc)
				 * @see org.osgi.framework.ServiceFactory#getService(org.osgi.framework.Bundle, org.osgi.framework.ServiceRegistration)
				 */
				public Object getService(Bundle bundle, ServiceRegistration registration) {
					if (jdc == null) {
						try {
							jdc = new JSLPDiscoveryContainer();
							jdc.connect(null, null);
						} catch (IDCreateException e) {
							Trace.catching(Activator.PLUGIN_ID, Activator.PLUGIN_ID + "/debug/methods/tracing", this.getClass(), "getService(Bundle, ServiceRegistration)", e); //$NON-NLS-1$ //$NON-NLS-2$
						} catch (ContainerConnectException e) {
							Trace.catching(Activator.PLUGIN_ID, Activator.PLUGIN_ID + "/debug/methods/tracing", this.getClass(), "getService(Bundle, ServiceRegistration)", e); //$NON-NLS-1$ //$NON-NLS-2$
							jdc = null;
						}
					}
					return jdc;
				}

				/* (non-Javadoc)
				 * @see org.osgi.framework.ServiceFactory#ungetService(org.osgi.framework.Bundle, org.osgi.framework.ServiceRegistration, java.lang.Object)
				 */
				public void ungetService(Bundle bundle, ServiceRegistration registration, Object service) {
					//TODO-mkuppe we later might want to dispose jSLP when the last!!! consumer ungets the service 
					//Though don't forget about the (ECF) Container which might still be in use
				}
			}, props);

			return Status.OK_STATUS;
		} catch (Exception e) {
			return new Status(IStatus.ERROR, Activator.PLUGIN_ID, IStatus.ERROR, "Exception starting jslp container", e); //$NON-NLS-1$
		}
	}
}
