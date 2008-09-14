/*******************************************************************************
 * Copyright (c) 2004 Composent, Inc. All rights reserved. This
 * program and the accompanying materials are made available under the terms of
 * the Eclipse Public License v1.0 which accompanies this distribution, and is
 * available at http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Composent, Inc. - initial API and implementation
 ******************************************************************************/
package org.eclipse.ecf.filetransfer;

import java.io.File;

import org.eclipse.ecf.core.IContainer;
import org.eclipse.ecf.core.identity.ID;
import org.eclipse.ecf.filetransfer.events.IFileTransferRequestEvent;
import org.eclipse.ecf.filetransfer.events.IIncomingFileTransferReceiveDataEvent;
import org.eclipse.ecf.filetransfer.events.IIncomingFileTransferReceiveDoneEvent;
import org.eclipse.ecf.filetransfer.events.IOutgoingFileTransferResponseEvent;
import org.eclipse.ecf.filetransfer.events.IOutgoingFileTransferSendDataEvent;
import org.eclipse.ecf.filetransfer.events.IOutgoingFileTransferSendDoneEvent;

/**
 * Entry point outgoing file transfer container adapter. This adapter interface allows providers to
 * expose file sending semantics to clients in a transport independent manner.
 * To be used, a non-null adapter reference must be returned from a call to
 * {@link IContainer#getAdapter(Class)}. Once a non-null reference is
 * retrieved, then it may be used to request to send a file to a remote user.
 * Events will then be asynchronously delivered to the provided listener to
 * complete file transfer.
 * <p>
 * To request and initiate sending a local file to a remote user:
 * 
 * <pre>
 *   // Get IOutgoingFileTransferContainerAdapter adapter
 *    IOutgoingFileTransferContainerAdapter ftc = (IOutgoingFileTransferContainerAdapter) container.getAdapter(IOutgoingFileTransferContainerAdapter.class);
 *    if (ftc != null) {
 *      // Create listener for receiving/responding to asynchronous file transfer events
 *  	     IFileTransferListener listener = new IFileTransferListener() {
 *  		     public void handleTransferEvent(IFileTransferEvent event) {
 *                // If this event is a response to the transfer request, check if file transfer rejected
 *                if (event instanceof IOutgoingFileTransferResponseEvent) {
 *                    IOutgoingFileTransferResponseEvent oftr = (IOutgoingFileTransferResponseEvent) event;
 *                    if (!oftr.requestAccepted()) {
 *                        // request rejected...tell user
 *                    }
 *                }
 *  		     }
 *  	     };
 *        // Specify the target user to receive file being sent
 *        ID userID = target user id to receive file;
 *        // Specify the local file to send
 *        File localFileToSend = new File(&quot;filename&quot;);
 *        // Actually send outgoing file request to remote user.  
 *        ftc.sendOutgoingRequest(userID, localFileToSend, listener);
 *    }
 * </pre>
 * 
 * <b>For the sender</b> the delivered events will be:
 * <ul>
 * <li>{@link IOutgoingFileTransferResponseEvent}</li>
 * <li>{@link IOutgoingFileTransferSendDataEvent}</li>
 * <li>{@link IOutgoingFileTransferSendDoneEvent}</li>
 * </ul>
 * and <b>for the {@link IIncomingFileTransferRequestListener}</b> events
 * delivered will be:
 * <ul>
 * <li>{@link IFileTransferRequestEvent}</li>
 * <li>{@link IIncomingFileTransferReceiveDataEvent}</li>
 * <li>{@link IIncomingFileTransferReceiveDoneEvent}</li>
 * </ul>
 */
public interface IOutgoingFileTransferContainerAdapter {
	/**
	 * Send request for outgoing file transfer. This method is used to initiate
	 * a file transfer to a targetReceiver (first parameter) of the
	 * localFileToSend (second parameter). File transfer events are
	 * asynchronously delivered to the file transferListener (third parameter)
	 * 
	 * @param targetReceiver
	 *            the ID of the remote to receive the file transfer request
	 * @param localFileToSend
	 *            the {@link IFileTransferInfo} for the local file to send. Must
	 *            not be null
	 * @param transferListener
	 *            a {@link IFileTransferListener} for responding to file
	 *            transfer events. Must not be null. If the target receiver
	 *            responds then an {@link IOutgoingFileTransferResponseEvent}
	 *            will be delivered to the listener
	 * @throws OutgoingFileTransferException
	 *             if the provider is not connected or is not in the correct
	 *             state for initiating file transfer
	 */
	public void sendOutgoingRequest(ID targetReceiver,
			IFileTransferInfo localFileToSend,
			IFileTransferListener transferListener)
			throws OutgoingFileTransferException;

	/**
	 * Send request for outgoing file transfer. This method is used to initiate
	 * a file transfer to a targetReceiver (first parameter) of the
	 * localFileToSend (second parameter). File transfer events are
	 * asynchronously delivered to the file transferListener (third parameter)
	 * 
	 * @param targetReceiver
	 *            the ID of the remote to receive the file transfer request
	 * @param localFileToSend
	 *            the {@link File} for the local file to send. Must not be null
	 * @param transferListener
	 *            a {@link IFileTransferListener} for responding to file
	 *            transfer events. Must not be null. If the target receiver
	 *            responds then an IOutgoingFileTransfer
	 *            will be delivered to the listener
	 * @throws OutgoingFileTransferException
	 *             if the provider is not connected or is not in the correct
	 *             state for initiating file transfer
	 */
	public void sendOutgoingRequest(ID targetReceiver, File localFileToSend,
			IFileTransferListener transferListener)
			throws OutgoingFileTransferException;

	/**
	 * Add incoming file transfer listener. If the underlying provider supports
	 * receiving file transfer requests
	 * 
	 * @param listener
	 *            to receive incoming file transfer request events. Must not be
	 *            null
	 */
	public void addListener(
			IIncomingFileTransferRequestListener listener);

	/**
	 * Remove incoming file transfer listener
	 * 
	 * @param listener
	 *            the listener to remove. Must not be null
	 * @return true if listener actually removed, false otherwise
	 */
	public boolean removeListener(
			IIncomingFileTransferRequestListener listener);

}