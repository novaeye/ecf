/*******************************************************************************
 * Copyright (c) 2004 Composent, Inc. and others. All rights reserved. This
 * program and the accompanying materials are made available under the terms of
 * the Eclipse Public License v1.0 which accompanies this distribution, and is
 * available at http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Composent, Inc. - initial API and implementation
 ******************************************************************************/
package org.eclipse.ecf.core.sharedobject;

import org.eclipse.ecf.core.util.ECFException;

public class SharedObjectNotFoundException extends ECFException {
	private static final long serialVersionUID = 3256725086957285689L;

	public SharedObjectNotFoundException() {
		super();
	}

	public SharedObjectNotFoundException(String arg0) {
		super(arg0);
	}

	public SharedObjectNotFoundException(String msg, Throwable cause) {
		super(msg, cause);
	}

	public SharedObjectNotFoundException(Throwable cause) {
		super(cause);
	}
}