/*******************************************************************************
 * Copyright (c) 2008 Marcelo Mayworm. All rights reserved. This
 * program and the accompanying materials are made available under the terms of
 * the Eclipse Public License v1.0 which accompanies this distribution, and is
 * available at http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: 	Marcelo Mayworm - initial API and implementation
 *
 ******************************************************************************/
package org.eclipse.ecf.internal.provider.xmpp.search;

import org.eclipse.ecf.presence.search.ICriterion;
import org.eclipse.ecf.presence.search.Selection;

public class XMPPSelection extends Selection {

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ecf.presence.search.ISelection#eq(java.lang.String, java.lang.String)
	 */
	public ICriterion eq(String field, String value) {
		return new XMPPSimpleCriterion(field, value, "");
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ecf.presence.search.ISelection#eq(java.lang.String, java.lang.String)
	 */
	public ICriterion eq(String field, String value, boolean ignoreCase) {
		return new XMPPSimpleCriterion(field, value, "");
	}
	
}
