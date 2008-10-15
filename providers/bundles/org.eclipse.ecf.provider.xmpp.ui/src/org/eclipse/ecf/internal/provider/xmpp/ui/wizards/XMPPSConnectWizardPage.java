/****************************************************************************
 * Copyright (c) 2007 Remy Suen, Composent Inc., and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Remy Suen <remy.suen@gmail.com> - initial API and implementation
 *****************************************************************************/
package org.eclipse.ecf.internal.provider.xmpp.ui.wizards;

import java.util.regex.Matcher;

import org.eclipse.ecf.internal.provider.xmpp.ui.Messages;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

final class XMPPSConnectWizardPage extends XMPPConnectWizardPage {

	XMPPSConnectWizardPage() {
		super();
		setTitle(Messages.XMPPSConnectWizardPage_WIZARD_PAGE_TITLE);
		setDescription(Messages.XMPPSConnectWizardPage_WIZARD_PAGE_DESCRIPTION);
		setPageComplete(false);
	}

	XMPPSConnectWizardPage(String usernameAtHost) {
		this();
		this.usernameAtHost = usernameAtHost;
	}

	private void verify() {
		final String text = connectText.getText();
		if (text.equals("")) { //$NON-NLS-1$
			updateStatus(Messages.XMPPSConnectWizardPage_WIZARD_PAGE_STATUS);
		} else {
			final Matcher matcher = emailPattern.matcher(text);
			if (!matcher.matches()) {
				updateStatus(Messages.XMPPConnectWizardPage_WIZARD_STATUS_INCOMPLETE);
			} else {
				restorePassword(text);
				updateStatus(null);
			}
		}
	}

	public void createControl(Composite parent) {
		
		parent = new Composite(parent, SWT.NONE);
		
		parent.setLayout(new GridLayout());
		final GridData fillData = new GridData(SWT.FILL, SWT.CENTER, true, false);
		final GridData endData = new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1);

		Label label = new Label(parent, SWT.LEFT);
		label.setText(Messages.XMPPConnectWizardPage_LABEL_USERID);

		connectText = new Combo(parent, SWT.SINGLE | SWT.BORDER | SWT.DROP_DOWN);
		connectText.setLayoutData(fillData);
		connectText.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				verify();
			}
		});
		connectText.addSelectionListener(new SelectionListener() {
			public void widgetDefaultSelected(SelectionEvent e) {
				verify();
			}

			public void widgetSelected(SelectionEvent e) {
				verify();
			}
		});

		label = new Label(parent, SWT.RIGHT);
		label.setText(Messages.XMPPSConnectWizardPage_WIZARD_PAGE_TEMPLATE);
		label.setLayoutData(endData);

		label = new Label(parent, SWT.LEFT);
		label.setText(Messages.XMPPSConnectWizardPage_WIZARD_PAGE_PASSWORD);
		passwordText = new Text(parent, SWT.SINGLE | SWT.PASSWORD | SWT.BORDER);
		passwordText.setLayoutData(fillData);

		restoreCombo();

		if (usernameAtHost != null) {
			connectText.setText(usernameAtHost);
			restorePassword(usernameAtHost);
			passwordText.setFocus();
		}

		verify();

		if (connectText.getText().equals("")) {
			updateStatus(null);
			setPageComplete(false);
		} else if (isPageComplete())
			passwordText.setFocus();

		org.eclipse.jface.dialogs.Dialog.applyDialogFont(parent);
		setControl(parent);
	}

}