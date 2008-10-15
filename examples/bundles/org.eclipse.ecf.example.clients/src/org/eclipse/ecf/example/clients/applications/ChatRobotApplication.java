/*******************************************************************************
 * Copyright (c) 2004, 2007 Composent, Inc. and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Composent, Inc. - initial API and implementation
 ******************************************************************************/
package org.eclipse.ecf.example.clients.applications;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.ecf.core.util.ECFException;
import org.eclipse.ecf.example.clients.IMessageReceiver;
import org.eclipse.ecf.example.clients.XMPPChatClient;
import org.eclipse.ecf.presence.im.IChatMessage;
import org.eclipse.equinox.app.IApplication;
import org.eclipse.equinox.app.IApplicationContext;

public class ChatRobotApplication implements IApplication, IMessageReceiver {

	public static final int WAIT_TIME = 10000;
	public static final int WAIT_COUNT = 10;

	private boolean running = false;
	private String userName;
	private XMPPChatClient client;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.equinox.app.IApplication#start(org.eclipse.equinox.app.IApplicationContext)
	 */
	public Object start(IApplicationContext context) throws Exception {
		Object[] args = context.getArguments().values().toArray();
		while (args[0] instanceof Object[])
			args = (Object[]) args[0];
		Object[] arguments = (Object[]) args;
		int l = arguments.length;
		if (arguments[l - 1] instanceof String
				&& arguments[l - 2] instanceof String
				&& arguments[l - 3] instanceof String
				&& arguments[l - 4] instanceof String) {
			userName = (String) arguments[l - 4];
			String hostName = (String) arguments[l - 3];
			String password = (String) arguments[l - 2];
			String targetName = (String) arguments[l - 1];
			runRobot(hostName, password, targetName);
			return new Integer(0);
		}
		System.out
				.println("Usage: pass in four arguments (username, hostname, password, targetIMUser)");
		return new Integer(-1);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.equinox.app.IApplication#stop()
	 */
	public void stop() {
	}

	private void runRobot(String hostName, String password, String targetIMUser)
			throws ECFException, Exception, InterruptedException {
		// Create client
		client = new XMPPChatClient(this);
		client.setupContainer();
		client.setupPresence();

		// Then connect
		String connectTarget = userName + "@" + hostName;

		client.doConnect(connectTarget, password);

		System.out.println("ECF chat robot (" + connectTarget + ")");
		// Send initial message to target user
		client.sendChat(targetIMUser, "Hi, I'm an ECF chat robot.");

		running = true;
		int count = 0;
		// Loop ten times and send ten 'hello there' messages to targetIMUser
		// out-of-band via XMPP message properties
		while (running && count++ < WAIT_COUNT) {
			wait(WAIT_TIME);
			Map properties = new HashMap();
			properties.put("message", "howdy");
			client.sendProperties(targetIMUser, properties);
		}
	}

	public synchronized void handleMessage(IChatMessage chatMessage) {
		System.out.println("handleMessage(" + chatMessage + ")");
	}

}