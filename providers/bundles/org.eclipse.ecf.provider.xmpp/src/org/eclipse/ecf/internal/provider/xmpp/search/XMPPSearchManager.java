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
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.eclipse.core.runtime.Assert;
import org.eclipse.ecf.core.ContainerConnectException;
import org.eclipse.ecf.core.identity.ID;
import org.eclipse.ecf.core.identity.IDCreateException;
import org.eclipse.ecf.core.identity.IDFactory;
import org.eclipse.ecf.core.identity.Namespace;
import org.eclipse.ecf.core.user.User;
import org.eclipse.ecf.internal.provider.xmpp.Messages;
import org.eclipse.ecf.internal.provider.xmpp.smack.ECFConnection;
import org.eclipse.ecf.presence.search.ICriteria;
import org.eclipse.ecf.presence.search.ICriterion;
import org.eclipse.ecf.presence.search.IResultList;
import org.eclipse.ecf.presence.search.ISearch;
import org.eclipse.ecf.presence.search.ISelection;
import org.eclipse.ecf.presence.search.IUserSearchListener;
import org.eclipse.ecf.presence.search.IUserSearchManager;
import org.eclipse.ecf.presence.search.ResultList;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smackx.Form;
import org.jivesoftware.smackx.FormField;
import org.jivesoftware.smackx.ReportedData;
import org.jivesoftware.smackx.ReportedData.Row;
import org.jivesoftware.smackx.search.UserSearch;

/**
 * A specific implementation for XMPP provider. XEP-0055:
 * http://www.xmpp.org/extensions/xep-0055.html
 * 
 * @see IUserSearchManager
 */
public class XMPPSearchManager implements IUserSearchManager {

	protected static final String FORM_TYPE = "FORM_TYPE";

	protected static final String NAME = "name";

	protected static final String JID = "jid";

	protected static final String SEARCH_ACTION = "search";

	/** Search service name on XMPP server */
	protected static final String SERVICE_SEARCH = "search.";

	/** Wrapper for XMPP connection */
	ECFConnection ecfConnection;

	Namespace connectNamespace;

	ID connectedID;

	/**
	 * Create a specific {@link ICriteria} for XMPP
	 */
	public ICriteria createCriteria() {
		return new XMPPCriteria();
	}

	/**
	 * Create a specific {@link ISelection} for XMPP
	 */
	public ISelection createSelection() {
		return new XMPPSelection();
	}

	/**
	 * Specific implementation for XMPP
	 * 
	 * @see IUserSearchManager#search(ICriteria).
	 */
	public ISearch search(ICriteria criteria) throws ContainerConnectException {

		try {

			UserSearch manager = new UserSearch();
			String host = ecfConnection.getXMPPConnection().getServiceName();
			Form form = manager.getSearchForm(
					ecfConnection.getXMPPConnection(), SERVICE_SEARCH + host);
			
			/*
			 * For XMPP criterion is considered. The XMPP server will
			 * deal with the search.
			 */
			List criterions =  criteria.getCriterions();
			Form answerForm = form.createAnswerForm();
			// add the fields for the search dynamically
			//consider just the fields used on the search
			String fields[] = getUserPropertiesFields(form);

			// type of action
			//TODO make it better
			Iterator it = criterions.iterator();
			while (it.hasNext()) {
				ICriterion criterion = (ICriterion) it.next();
				if(criterion.equals(SEARCH_ACTION)){
					answerForm.setAnswer(SEARCH_ACTION, criterion.toExpression());
				}
				
				for (int i = 0; i < fields.length; i++) {
					String fieldName = fields[i];
					if(criterion.equals(fieldName)){
						answerForm.setAnswer(fieldName,Boolean.valueOf(criterion.toExpression()).booleanValue());
						break;
					}
				}
			}
			
			ReportedData data = manager.sendSearchForm(ecfConnection
					.getXMPPConnection(), answerForm, SERVICE_SEARCH + host);

			//create a result list from ReportedData
			IResultList resultList = createResultList(data);
			
			ISearch search = new XMPPSearch(resultList);
			
			return search;
		} catch (final XMPPException e) {
			String message = null;
            if (e.getXMPPError() != null && e.getXMPPError().getCode() == 404) {
            	message = Messages.XMPPContainer_UNRECOGONIZED_SEARCH_SERVICE;
            } else
            	message = e.getLocalizedMessage();
                
			throw new ContainerConnectException(message, e);
		}

	}

	/**
	 * Create a result list from ReportedData. 
	 * Identify dynamically columns and rows and create users
	 * adding it to a {@link IResultList}
	 * @param data
	 * @return {@link IResultList} a list of users
	 */
	protected IResultList createResultList(ReportedData data) {
		
		Collection users = new ArrayList(5);
		
		Iterator rows = data.getRows();
		while (rows.hasNext()) {
			Row row = (Row) rows.next();
			
			Iterator jids = row.getValues(JID);
			Iterator names = row.getValues(NAME);
			String jid = null;
			String name = null;
			//XMPP server returns the same length for both
			while (jids.hasNext() && names.hasNext()) {
				try {
					jid = (String)jids.next();
					name = (String)names.next();
					users.add(new XMPPResultItem(new User(IDFactory.getDefault().createID(connectNamespace, jid), name)));
				} catch (final IDCreateException e) {
					throw new RuntimeException("cannot create connect id for client " +jid +" , name = "+name, e);
				}
			}
		}

		ResultList result = new ResultList(users);
		
		return result;
	}

	/**
	 * Returns the user properties fields available on the XMPP server
	 * 
	 * @param form
	 * @return String[] fields for form
	 */
	public String[] getUserPropertiesFields(Form form) {

		Set fields = new HashSet();
		Iterator userProperties = form.getFields();

		while (userProperties.hasNext()) {
			FormField field = (FormField) userProperties.next();
			String variable = field.getVariable();
			// ignore these fields
			if (!variable.equalsIgnoreCase(FORM_TYPE)
					&& !variable.equalsIgnoreCase(SEARCH_ACTION))
				fields.add(variable);
		}

		String[] array = (String[]) fields.toArray(new String[0]);
		return array;
	}

	/**
	 * Specific implementation for XMPP
	 * 
	 * @see IUserSearchManager#search(ICriteria).
	 */
	public void search(ICriteria criteria, IUserSearchListener listener){


	}

	/**
	 * These parameters must be not null
	 * @param connectNamespace
	 * @param connectedID
	 * @param connection
	 */
	public void setConnection(Namespace connectNamespace, ID connectedID, ECFConnection connection) {
		Assert.isNotNull(connectNamespace);
		Assert.isNotNull(connectedID);
		Assert.isNotNull(connection);
		
		this.connectNamespace = connectNamespace;
		this.connectedID = connectedID;
		this.ecfConnection = connection;
		
	}

	public String[] getUserPropertiesFields() {
		// TODO Auto-generated method stub
		return null;
	}

}
