package org.eclipse.ecf.internal.examples.webinar.util.roster;

import org.eclipse.ecf.internal.examples.webinar.util.RosterWriter;
import org.eclipse.ecf.presence.roster.IRoster;
import org.eclipse.ecf.presence.ui.roster.AbstractRosterContributionItem;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;

public class RosterContributionItem1 extends AbstractRosterContributionItem {

	/* (non-Javadoc)
	 * @see org.eclipse.ecf.presence.ui.roster.AbstractPresenceContributionItem#makeActions()
	 */
	protected IAction[] makeActions() {
		final IRoster roster = getSelectedRoster();
		if (roster != null) {
			IAction action = new Action() {
				public void run() {
					RosterWriter accessor = new RosterWriter();
					accessor.showRoster(roster);
				}
			};
			action.setText("Show this roster on console");
			action.setImageDescriptor(PlatformUI.getWorkbench()
					.getSharedImages().getImageDescriptor(
							ISharedImages.IMG_DEF_VIEW));
			return new IAction[] { action };
		} else return null;
	}

}
