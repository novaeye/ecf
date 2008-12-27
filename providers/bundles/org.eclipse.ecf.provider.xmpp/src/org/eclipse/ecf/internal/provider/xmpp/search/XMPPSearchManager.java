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

import java.net.URISyntaxException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.eclipse.core.runtime.Assert;
import org.eclipse.ecf.core.ContainerConnectException;
import org.eclipse.ecf.core.identity.ID;
import org.eclipse.ecf.core.identity.Namespace;
import org.eclipse.ecf.core.user.IUser;
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
import org.eclipse.ecf.provider.xmpp.identity.XMPPID;
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

	/** Search service name on XMPP server */
	protected static final String SERVICE_SEARCH = "search.";

	/** Wrapper for XMPP connection */
	protected ECFConnection ecfConnection;

	protected Namespace connectNamespace;

	protected ID connectedID;

	protected Form form;

	protected UserSearch manager;

	protected static final String FORM_TYPE = "FORM_TYPE";

	protected static final String NAME = "name";

	protected static final String JID = "jid";

	protected static final String SEARCH_ACTION = "search";

	
	public XMPPSearchManager() {
		manager = new UserSearch();
	}
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

		ResultList resultList = new ResultList();
		try {
			//initialize the form by chance it is null
			if (form == null)
				form = manager.getSearchForm(ecfConnection.getXMPPConnection(),
						SERVICE_SEARCH
								+ ecfConnection.getXMPPConnection()
										.getServiceName());

			/*
			 * For XMPP criterion is considered. The XMPP server will deal with
			 * the search.
			 */
			List criterions = criteria.getCriterions();
			// add the fields for the search dynamically
			// consider just the fields used on the search
			// fields checked by user
			
			String fields[] = getUserPropertiesFields();
			for (int i = 0; i < fields.length; i++) {
				Iterator criterionsIterator = criterions.iterator();
				//for each user properties field check if it
				//was added by user for the criteria
				//for each field, a search is performed, and 
				//the partial result is added to the result list
				while (criterionsIterator.hasNext()) {
					ICriterion criterion = (ICriterion) criterionsIterator.next();
					if(criterion.equals(fields[i])){
						Form answerForm = form.createAnswerForm();
						answerForm.setAnswer(fields[i], true);
						answerForm.setAnswer(SEARCH_ACTION, criterion.toExpression());
						ReportedData data = manager.sendSearchForm(ecfConnection
								.getXMPPConnection(), answerForm, SERVICE_SEARCH + ecfConnection.getXMPPConnection().getServiceName());
						// create a result list from ReportedData
						IResultList partialResultList = createResultList(data);
						resultList.addAll(partialResultList.geResults());
					}
				}
			}			

			return new XMPPSearch(resultList);
			
		} catch (final XMPPException e) {
			String message = null;
			if (e.getXMPPError() != null && e.getXMPPError().getCode() == 404) {
				message = Messages.XMPPContainer_UNRECOGONIZED_SEARCH_SERVICE;
			} else{
				message = e.getLocalizedMessage();
			}
			throw new ContainerConnectException(message, e);
		}

	}

	/**
	 * Create a result list from ReportedData. Identify dynamically columns and
	 * rows and create users adding it to a {@link IResultList}
	 * 
	 * @param data ReportedData
	 * @return {@link IResultList} a list of users
	 * @throws  
	 */
	protected IResultList createResultList(ReportedData data) {
		ResultList result = new ResultList();
		Iterator rows = data.getRows();
		while (rows.hasNext()) {
			Row row = (Row) rows.next();
			Iterator jids = row.getValues(JID);
			Iterator names = row.getValues(NAME);
			String jid = null;
			String name = null;
			// XMPP server returns the same length for both
			while (jids.hasNext() && names.hasNext()) {
				try {
					jid = (String) jids.next();
					name = (String) names.next();					
					IUser user = new User(new XMPPID(connectNamespace, jid), name);
					result.add(new XMPPResultItem(user));
				} catch(URISyntaxException e){
					throw new RuntimeException(
							"cannot create connect id for client " + jid
									+ " , name = " + name, e);
				}
			}
		}
		return result;
	}

	/**
	 * Specific implementation for XMPP
	 * 
	 * @see IUserSearchManager#search(ICriteria).
	 */
	public void search(ICriteria criteria, IUserSearchListener listener) {

	}

	/**
	 * These parameters must be not null
	 * 
	 * @param connectNamespace
	 * @param connectedID
	 * @param connection
	 */
	public void setConnection(Namespace connectNamespace, ID connectedID,
			ECFConnection connection) {
		Assert.isNotNull(connectNamespace);
		Assert.isNotNull(connectedID);
		Assert.isNotNull(connection);
		this.connectNamespace = connectNamespace;
		this.connectedID = connectedID;
		this.ecfConnection = connection;
	}

	/**
	 * Returns the user properties fields available on the XMPP server
	 * 
	 * @param form
	 * @return String[] fields for form
	 * @throws ContainerConnectException 
	 */
	public String[] getUserPropertiesFields() throws ContainerConnectException {
		try{
			if (form == null)
				form = manager.getSearchForm(ecfConnection.getXMPPConnection(),
						SERVICE_SEARCH + ecfConnection.getXMPPConnection().getServiceName());
	
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
			return (String[]) fields.toArray(new String[0]);
		} catch (final XMPPException e) {
			String message = null;
			if (e.getXMPPError() != null && e.getXMPPError().getCode() == 404) {
				message = Messages.XMPPContainer_UNRECOGONIZED_SEARCH_SERVICE;
			} else{
				message = e.getLocalizedMessage();
			}
			throw new ContainerConnectException(message, e);
		}

	}
	
	/**
	 * Notify that user search for XMPP is enabled
	 */
	public boolean isEnabled() {
		return true;
	}

}
