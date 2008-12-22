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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.ecf.presence.search.ICriteria;
import org.eclipse.ecf.presence.search.ICriterion;

public class XMPPCriteria implements ICriteria {

	protected List criteria;
	
	public XMPPCriteria(){
		criteria = Collections.synchronizedList(new ArrayList());
	}
	
	public void add(ICriterion criterion) {
		criteria.add(criterion);
	}

	public List getCriterions() {
		return criteria;
	}

	public boolean isEmpty() {
		return criteria.isEmpty();
	}

}
