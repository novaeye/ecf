/******************************************************************************* 
 * Copyright (c) 2009 EclipseSource and others. All rights reserved. This
 * program and the accompanying materials are made available under the terms of
 * the Eclipse Public License v1.0 which accompanies this distribution, and is
 * available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   EclipseSource - initial API and implementation
 *   Composent, Inc - Simplifications
*******************************************************************************/
package org.eclipse.ecf.remoteservice.rest.util;

import org.eclipse.ecf.remoteservice.rest.IRestCall;
import org.eclipse.ecf.remoteservice.rest.RestCall;

import java.util.Map;

/**
 * Factory to support the creation of {@link IRestCall} objects.
 */
public class RestCallFactory {

	public static IRestCall createRestCall(String fqMethod, Object[] params, Map requestHeaders, long timeout) {
		return new RestCall(fqMethod, params, requestHeaders, timeout);
	}

	public static IRestCall createRestCall(String fqMethod, Object[] params, Map requestHeaders) {
		return new RestCall(fqMethod, params, requestHeaders);
	}

	public static IRestCall createRestCall(String fqMethod, Object[] params) {
		return new RestCall(fqMethod, params);
	}

	public static IRestCall createRestCall(String resourcePath) {
		return createRestCall(resourcePath, null);
	}

}