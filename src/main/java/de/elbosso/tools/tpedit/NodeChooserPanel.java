package de.elbosso.tools.tpedit;

import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;
import java.awt.*;

public class NodeChooserPanel extends javax.swing.JPanel implements
		javax.swing.event.TreeSelectionListener
{
	private final static org.apache.log4j.Logger EXCEPTION_LOGGER = org.apache.log4j.Logger.getLogger("de.netsysit.experimental.ExceptionLogger");
	private final static org.apache.log4j.Logger CLASS_LOGGER = org.apache.log4j.Logger.getLogger(NodeChooserPanel.class);
	private final static java.util.ResourceBundle i18n = java.util.ResourceBundle.getBundle("de.elbosso.tools.i18n", java.util.Locale.getDefault());
	private javax.swing.JTree tree = new javax.swing.JTree();
	private javax.swing.JScrollPane treescroller = new javax.swing.JScrollPane(tree);
	private javax.swing.JLabel lbl = new javax.swing.JLabel();
	private javax.swing.JScrollPane lblscroller = new javax.swing.JScrollPane(lbl);
	private VelocityHelper velocityHelper;
	private javax.swing.tree.TreePath selectedPath;

	NodeChooserPanel(VelocityHelper velocityHelper)
	{
		super(new java.awt.BorderLayout());
		this.velocityHelper=velocityHelper;
		treescroller.setPreferredSize(new java.awt.Dimension(350, 500));
		treescroller.setMinimumSize(new java.awt.Dimension(350, 500));
		lblscroller.setPreferredSize(new java.awt.Dimension(530, 500));
		lblscroller.setMinimumSize(new java.awt.Dimension(530, 500));
		javax.swing.JSplitPane splitter=new javax.swing.JSplitPane(JSplitPane.HORIZONTAL_SPLIT,treescroller,lblscroller);
		lbl.setBackground(java.awt.Color.WHITE);
		lbl.setVerticalTextPosition(javax.swing.SwingConstants.TOP);
		lbl.setVerticalAlignment(javax.swing.SwingConstants.TOP);
		lbl.setOpaque(true);
		tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
		tree.addTreeSelectionListener(this);
		tree.setCellRenderer(new CustomDefaultTreeCellRenderer());
		add(splitter);
	}
	void setModel(javax.swing.tree.TreeModel model)
	{
		tree.setModel(model);
	}

	@Override
	public void valueChanged(TreeSelectionEvent e)
	{
		javax.swing.tree.TreePath[] tp = tree.getSelectionPaths();
		try
		{
			lbl.setText("");
			if (tp != null)
			{
				if (tp.length == 1)
				{
					java.lang.Object obj = tp[0].getPath()[tp[0].getPathCount() - 1];
					if ((obj instanceof Test)&&(selectedPath.getLastPathComponent()!=obj))
					{
						de.elbosso.tools.tpedit.Utilities.renderPreview(velocityHelper, tree, lbl);
					}
				}
			}
		} catch (org.apache.velocity.exception.MethodInvocationException exp)
		{
			de.elbosso.util.Utilities.handleException(EXCEPTION_LOGGER,tree,exp);
		} catch (org.apache.velocity.exception.ParseErrorException exp)
		{
			de.elbosso.util.Utilities.handleException(EXCEPTION_LOGGER,tree,exp);
		} catch (org.apache.velocity.exception.ResourceNotFoundException exp)
		{
			de.elbosso.util.Utilities.handleException(EXCEPTION_LOGGER,tree,exp);
		} catch (java.io.IOException exp)
		{
			de.elbosso.util.Utilities.handleException(EXCEPTION_LOGGER,tree,exp);
		} catch (java.lang.Exception exp)
		{
			de.elbosso.util.Utilities.handleException(EXCEPTION_LOGGER,tree,exp);
		}
		finally
		{
		}
	}

	public void setSelectedTreePath(TreePath selectionPath)
	{
		selectedPath=selectionPath;
	}

	public TreePath getSelectedTreePath()
	{
		javax.swing.tree.TreePath tp = tree.getSelectionPath();
		if (tp != null)
		{

			if (((tp.getLastPathComponent() instanceof Test)==false) || (selectedPath.getLastPathComponent() == tp.getLastPathComponent()))
			{
				tp = null;
			}
		}
		return tp;
	}
	class CustomDefaultTreeCellRenderer extends DefaultTreeCellRenderer
	{
		@Override
		public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus) {
			boolean enabled=Test.class.isAssignableFrom(value.getClass());
			if(enabled==true)
			{
				enabled=(selectedPath.getLastPathComponent()==value)==false;
//				System.out.println(selectedPath.getLastPathComponent()==value);
			}

			if(enabled==false)
			{
				sel=enabled;
				hasFocus=enabled;
			}

			Component treeCellRendererComponent = super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
			treeCellRendererComponent.setEnabled(enabled);

			return treeCellRendererComponent;
		}
	}
}
