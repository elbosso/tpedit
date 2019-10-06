/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package de.elbosso.tools.tpedit;

import javax.swing.event.ListSelectionEvent;
import java.util.List;

/**
 *
 * @author Susanne Bach
 */
public class QaTeamMembersList extends javax.swing.JPanel implements
		javax.swing.event.ListSelectionListener
{
	private final static org.apache.log4j.Logger CLASS_LOGGER = org.apache.log4j.Logger.getLogger(QaTeamMembersList.class);
	private final static org.apache.log4j.Logger EXCEPTION_LOGGER = org.apache.log4j.Logger.getLogger("ExceptionCatcher");
	private final static java.util.ResourceBundle i18n = java.util.ResourceBundle.getBundle("de.elbosso.tools.i18n", java.util.Locale.getDefault());
	private javax.swing.JList list;
	private javax.swing.JToolBar toolbar;
	private de.netsysit.model.list.SophisticatedListModel slm;

	public QaTeamMembersList()
	{
		super(new java.awt.BorderLayout());
		toolbar = new javax.swing.JToolBar();
		toolbar.setFloatable(false);
		createActions();

		list = new javax.swing.JList();
		list.addListSelectionListener(this);
		list.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
		slm = new de.netsysit.model.list.SophisticatedListModel(list);
		slm.setObjectCreator(new de.netsysit.util.ObjectCreator()
		{
			public Object create()
			{
				java.lang.String newName=javax.swing.JOptionPane.showInputDialog(QaTeamMembersList.this,i18n.getString("QaTeamMembersList.newdlg.title"));
				return newName;
			}
		});

//		slm.getAddElementAction().putValue(javax.swing.Action.SMALL_ICON, de.netsysit.util.ResourceLoader.getIcon("toolbarButtonGraphics/general/Paste24.gif"));
		toolbar.add(slm.getAddElementAction());
		toolbar.add(slm.getRemoveElementAction());

		add(toolbar, java.awt.BorderLayout.NORTH);

		add(new javax.swing.JScrollPane(list));
	}

	private void createActions()
	{
	}

	public void valueChanged(ListSelectionEvent e)
	{
		if(e.getValueIsAdjusting()==false)
		{
		}
	}

	public List<String> getAsList()
	{
		return slm.getAsList();
	}

	public void clear()
	{
		slm.clear();
	}

	public void addAll(List<String> qaTeamMemberSignatures)
	{
		java.util.List content=new java.util.LinkedList(getAsList());
		for(java.lang.String qam:qaTeamMemberSignatures)
		{
			if(content.contains(qam)==false)
				slm.add(qam);
		}
		invalidate();
		validate();
		doLayout();
		repaint();
	}
}


