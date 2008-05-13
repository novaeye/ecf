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

package org.eclipse.ecf.storage;

import org.eclipse.equinox.security.storage.ISecurePreferences;

/**
 *
 */
public interface INamespaceEntry {

	public ISecurePreferences getPreferences();

	public IIDEntry[] getIDEntries();

	public void delete();

}
