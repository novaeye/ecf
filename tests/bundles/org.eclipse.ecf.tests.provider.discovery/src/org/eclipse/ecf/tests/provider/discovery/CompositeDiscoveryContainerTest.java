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
package org.eclipse.ecf.tests.provider.discovery;

import java.util.List;

import org.eclipse.ecf.core.util.ECFException;
import org.eclipse.ecf.provider.discovery.CompositeDiscoveryContainer;
import org.eclipse.ecf.tests.discovery.DiscoveryTest;

public class CompositeDiscoveryContainerTest extends DiscoveryTest {

	private TestDiscoveryContainer testDiscoveryContainer;

	public CompositeDiscoveryContainerTest() {
		super("ecf.discovery.*");
		setComparator(new CompositeServiceInfoComporator());
	}
	
	/**
	 * Check if 
	 */
	public void testAddContainerWithRegisteredServices() {
		 try {
			discoveryContainer.registerService(serviceInfo);
		} catch (ECFException e) {
			fail("Registering a service failed on a new IDCA");
		}
		CompositeDiscoveryContainer cdc = (CompositeDiscoveryContainer) discoveryContainer;
		testDiscoveryContainer = new TestDiscoveryContainer();
		assertTrue(cdc.addContainer(testDiscoveryContainer));
		List registeredServices = testDiscoveryContainer.getRegisteredServices();
		assertEquals("registerService(aService) wasn't called on TestDiscoveryContainer", serviceInfo, registeredServices.get(0));
	}

	public void testAddContainerWithoutRegisteredServices() {
		 try {
			discoveryContainer.registerService(serviceInfo);
			discoveryContainer.unregisterService(serviceInfo);
		} catch (ECFException e) {
			fail("Re-/Unregistering a service failed on a new IDCA");
		}
		CompositeDiscoveryContainer cdc = (CompositeDiscoveryContainer) discoveryContainer;
		testDiscoveryContainer = new TestDiscoveryContainer();
		assertTrue(cdc.addContainer(testDiscoveryContainer));
		List registeredServices = testDiscoveryContainer.getRegisteredServices();
		assertTrue(registeredServices.isEmpty());
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.ecf.tests.discovery.DiscoveryTest#tearDown()
	 */
	protected void tearDown() throws Exception {
		if(testDiscoveryContainer != null){
			CompositeDiscoveryContainer cdc = (CompositeDiscoveryContainer) discoveryContainer;
			assertTrue(cdc.removeContainer(testDiscoveryContainer));
		}
		super.tearDown();
	}
}