/*******************************************************************************
 * Copyright (c) 2008 Jan S. Rellermeyer, and others
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Jan S. Rellermeyer - initial API and implementation
 ******************************************************************************/

package org.eclipse.ecf.tests.remoteservice.r_osgi;

import java.util.Dictionary;
import java.util.Hashtable;

import org.eclipse.ecf.remoteservice.Constants;
import org.eclipse.ecf.remoteservice.IRemoteServiceContainerAdapter;
import org.eclipse.ecf.tests.remoteservice.AbstractServiceTrackerTest;
import org.eclipse.ecf.tests.remoteservice.Activator;
import org.eclipse.ecf.tests.remoteservice.IConcatService;
import org.osgi.util.tracker.ServiceTracker;

/**
 * ServiceTrackerTest, adapted for the R-OSGi provider.
 * 
 * @author Jan S. Rellermeyer, ETH Zurich
 */
public class ServiceTrackerTest extends AbstractServiceTrackerTest {

	/**
	 * 
	 * 
	 * @see junit.framework.TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		super.setUp();
		setClientCount(1);
		createServerAndClients();
		connectClients();
		setupRemoteServiceAdapters();
		addRemoteServiceListeners();
	}

	/**
	 * 
	 * 
	 * @see junit.framework.TestCase#tearDown()
	 */
	protected void tearDown() throws Exception {
		cleanUpServerAndClients();
		super.tearDown();
	}

	/**
	 * set up the adapters. adapters[0] is the one on which services will be
	 * registered. adapters[1] is the one which will import the service and
	 * generate a proxy.
	 */
	protected void setupRemoteServiceAdapters() throws Exception {
		adapters = new IRemoteServiceContainerAdapter[clientCount + 1];
		adapters[0] = (IRemoteServiceContainerAdapter) getServer();
		final int clientCount = getClientCount();
		for (int i = 0; i < clientCount; i++) {
			adapters[i + 1] = (IRemoteServiceContainerAdapter) getClients()[i]
					.getAdapter(IRemoteServiceContainerAdapter.class);
		}
	}

	/**
	 * 
	 * @see org.eclipse.ecf.tests.ContainerAbstractTestCase#getServerIdentity()
	 */
	protected String getServerIdentity() {
		return R_OSGi.SERVER_IDENTITY;
	}

	/**
	 * 
	 * @see org.eclipse.ecf.tests.ContainerAbstractTestCase#getServerContainerName()
	 */
	protected String getServerContainerName() {
		return R_OSGi.CLIENT_CONTAINER_NAME;
	}

	/**
	 * 
	 * @see org.eclipse.ecf.tests.remoteservice.AbstractRemoteServiceTest#getClientContainerName()
	 */
	protected String getClientContainerName() {
		return R_OSGi.CLIENT_CONTAINER_NAME;
	}
	
	public void testServiceTracker() throws Exception {
		final IRemoteServiceContainerAdapter[] adapters = getRemoteServiceAdapters();
		// client [0]/adapter[0] is the service 'server'
		// client [1]/adapter[1] is the service target (client)
		final Dictionary props = new Hashtable();
		props.put(Constants.SERVICE_REGISTRATION_TARGETS, getClients()[0].getConnectedID());
		props.put(Constants.AUTOREGISTER_REMOTE_PROXY, "true");
		// Register
		adapters[0].registerRemoteService(new String[] {IConcatService.class.getName()}, createService(), props);
		// Give some time for propagation
		sleep(3000);

		final ServiceTracker st = new ServiceTracker(Activator.getDefault().getContext(), IConcatService.class.getName(), null);
		assertNotNull(st);
		st.open();
		final IConcatService concatService = (IConcatService) st.getService();
		assertNotNull(concatService);
		System.out.println("proxy call start");
		final String result = concatService.concat("OSGi ", "is cool");
		System.out.println("proxy call end. result=" + result);
		sleep(3000);
		st.close();
		sleep(3000);
	}

}