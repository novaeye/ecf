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

/*
 * Created on Dec 20, 2004
 *  
 */
package org.eclipse.ecf.provider.generic;

import java.util.Enumeration;
import java.util.Hashtable;
import org.eclipse.ecf.core.ISharedObjectConnector;
import org.eclipse.ecf.core.events.ISharedObjectEvent;
import org.eclipse.ecf.core.events.SharedObjectCallEvent;
import org.eclipse.ecf.core.identity.ID;
import org.eclipse.ecf.core.util.AsynchResult;
import org.eclipse.ecf.core.util.IQueueEnqueue;
import org.eclipse.ecf.core.util.QueueException;

public class SOConnector implements ISharedObjectConnector {
    ID sender;
    Hashtable receiverQueues = null;

    public SOConnector(ID sender, ID[] recv, IQueueEnqueue[] queues) {
        super();
        this.receiverQueues = new Hashtable();
        for (int i = 0; i < recv.length; i++) {
            receiverQueues.put(recv[i], queues[i]);
        }
    }

    protected void fireEvent(ISharedObjectEvent event) throws QueueException {
        for (Enumeration e = receiverQueues.elements(); e.hasMoreElements();) {
            IQueueEnqueue queue = (IQueueEnqueue) e.nextElement();
            queue.enqueue(event);
        }
    }

    protected void fireEvents(ISharedObjectEvent[] event) throws QueueException {
        for (Enumeration e = receiverQueues.elements(); e.hasMoreElements();) {
            IQueueEnqueue queue = (IQueueEnqueue) e.nextElement();
            if (queue != null) {
                queue.enqueue(event);
            }
        }
    }

    protected AsynchResult[] fireCallEvent(ISharedObjectEvent event)
            throws QueueException {
        AsynchResult[] results = new AsynchResult[receiverQueues.size()];
        int i = 0;
        for (Enumeration e = receiverQueues.elements(); e.hasMoreElements();) {
            IQueueEnqueue queue = (IQueueEnqueue) e.nextElement();
            results[i] = new AsynchResult();
            queue.enqueue(new SharedObjectCallEvent(event
                    .getSenderSharedObjectID(), event, results[i]));
        }
        return results;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ecf.core.ISharedObjectConnector#getSender()
     */
    public ID getSender() {
        return sender;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ecf.core.ISharedObjectConnector#getReceivers()
     */
    public ID[] getReceivers() {
        return (ID[]) receiverQueues.keySet().toArray(
                new ID[receiverQueues.size()]);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ecf.core.ISharedObjectConnector#enqueue(org.eclipse.ecf.core.events.ISharedObjectEvent)
     */
    public void enqueue(ISharedObjectEvent event) throws QueueException {
        fireEvent(event);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ecf.core.ISharedObjectConnector#enqueue(org.eclipse.ecf.core.events.ISharedObjectEvent[])
     */
    public void enqueue(ISharedObjectEvent[] events) throws QueueException {
        fireEvents(events);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ecf.core.ISharedObjectConnector#callAsynch(org.eclipse.ecf.core.events.ISharedObjectEvent)
     */
    public AsynchResult[] callAsynch(ISharedObjectEvent arg) throws Exception {
        return fireCallEvent(arg);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ecf.core.ISharedObjectConnector#dispose()
     */
    public void dispose() {
        if (receiverQueues != null) {
            receiverQueues.clear();
            receiverQueues = null;
        }
        sender = null;
    }
}