package org.eclipse.ecf.internal.examples.remoteservices.hello.host;

import java.util.Properties;

import org.eclipse.ecf.core.IContainerManager;
import org.eclipse.ecf.examples.remoteservices.hello.IHello;
import org.eclipse.ecf.examples.remoteservices.hello.impl.Hello;
import org.eclipse.ecf.osgi.services.distribution.IDistributionConstants;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;
import org.osgi.util.tracker.ServiceTracker;


public class Activator implements BundleActivator, IDistributionConstants {

	private BundleContext context;
	private ServiceTracker containerManagerServiceTracker;
	private ServiceRegistration helloRegistration;
	
	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext context) throws Exception {
		this.context = context;
		// Create R-OSGi Container
		IContainerManager containerManager = getContainerManagerService();
		containerManager.getContainerFactory().createContainer("ecf.r_osgi.peer");
		
		Properties props = new Properties();
		// add OSGi service property indicating this
		props.put(REMOTE_INTERFACES, REMOTE_INTERFACES_WILDCARD);
		// register remote service
		helloRegistration = context.registerService(IHello.class.getName(), new Hello(), props);
		System.out.println("Host: Hello Service Registered");
	}

	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext context) throws Exception {
		if (helloRegistration != null) {
			helloRegistration.unregister();
			helloRegistration = null;
		}
		if (containerManagerServiceTracker != null) {
			containerManagerServiceTracker.close();
			containerManagerServiceTracker = null;
		}
		this.context = null;
	}

	private IContainerManager getContainerManagerService() {
		if (containerManagerServiceTracker == null) {
			containerManagerServiceTracker = new ServiceTracker(context, IContainerManager.class.getName(),null);
			containerManagerServiceTracker.open();
		}
		return (IContainerManager) containerManagerServiceTracker.getService();
	}

}
