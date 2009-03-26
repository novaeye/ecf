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

package org.eclipse.ecf.provider.filetransfer.httpclient;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.net.ssl.SSLSocketFactory;

import org.apache.commons.httpclient.ConnectTimeoutException;
import org.apache.commons.httpclient.Credentials;
import org.apache.commons.httpclient.ProxyClient;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.auth.AuthScope;
import org.apache.commons.httpclient.params.HttpConnectionParams;
import org.apache.commons.httpclient.protocol.ProtocolSocketFactory;
import org.eclipse.ecf.core.util.Proxy;
import org.eclipse.ecf.core.util.ProxyAddress;
import org.eclipse.ecf.internal.provider.filetransfer.httpclient.Activator;

public class SslProtocolSocketFactory implements ProtocolSocketFactory {

	//	private SSLContext sslContext;

	private Proxy proxy;

	public SslProtocolSocketFactory(Proxy proxy) {
		this.proxy = proxy;
	}

	public Socket createSocket(String remoteHost, int remotePort) throws IOException, UnknownHostException {
		return Activator.getDefault().getSSLSocketFactory().createSocket(remoteHost, remotePort);
	}

	public Socket createSocket(String remoteHost, int remotePort, InetAddress clientHost, int clientPort) throws IOException, UnknownHostException {
		return Activator.getDefault().getSSLSocketFactory().createSocket(remoteHost, remotePort, clientHost, clientPort);
	}

	public Socket createSocket(String remoteHost, int remotePort, InetAddress clientHost, int clientPort, HttpConnectionParams params) throws IOException, UnknownHostException, ConnectTimeoutException {
		final SSLSocketFactory sslSocketFactory = Activator.getDefault().getSSLSocketFactory();
		if (params == null || params.getConnectionTimeout() == 0)
			return sslSocketFactory.createSocket(remoteHost, remotePort, clientHost, clientPort);

		if (proxy != null && !Proxy.NO_PROXY.equals(proxy)) {
			ProxyClient proxyClient = new ProxyClient();

			ProxyAddress address = proxy.getAddress();
			proxyClient.getHostConfiguration().setProxy(address.getHostName(), address.getPort());
			proxyClient.getHostConfiguration().setHost(remoteHost, remotePort);
			String proxyUsername = proxy.getUsername();
			String proxyPassword = proxy.getPassword();
			if (proxyUsername != null && !proxyUsername.equals("")) { //$NON-NLS-1$
				Credentials credentials = new UsernamePasswordCredentials(proxyUsername, proxyPassword);
				AuthScope proxyAuthScope = new AuthScope(address.getHostName(), address.getPort(), AuthScope.ANY_REALM);
				proxyClient.getState().setProxyCredentials(proxyAuthScope, credentials);
			}

			ProxyClient.ConnectResponse response = proxyClient.connect();
			if (response.getSocket() != null) {
				// tunnel SSL via the resultant socket
				Socket sslsocket = sslSocketFactory.createSocket(response.getSocket(), remoteHost, remotePort, true);
				return sslsocket;
			}
		}
		// Direct connection
		Socket socket = sslSocketFactory.createSocket();
		socket.bind(new InetSocketAddress(clientHost, clientPort));
		socket.connect(new InetSocketAddress(remoteHost, remotePort), params.getConnectionTimeout());
		return socket;
	}
}
