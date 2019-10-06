package de.elbosso.tools.tpedit;

import javax.swing.tree.TreeNode;

//public weil sonst Velocity nicht auf die enthaltenen Methoden zugreifen kann!
public class VelocityHelper extends java.lang.Object
{
	private org.apache.commons.text.StringEscapeUtils stringEscapeUtils=new org.apache.commons.text.StringEscapeUtils();
	private TPEdit tpedit;

	VelocityHelper(TPEdit tpedit)
	{
		super();
		this.tpedit=tpedit;
	}
	public java.lang.String escapeHtml4(java.lang.String input)
	{
		return stringEscapeUtils.escapeHtml4(input);
	}
	public String[] actionReferences(String name)
	{
		String[] rv=null;
		java.util.Set<TreeNode> leaves = tpedit.getAllLeafNodes((TreeNode) tpedit.getModel().getRoot());
		if (name.startsWith(TPEdit.MACROPREFIX))
		{
			java.lang.String stringrep = name.trim().substring(TPEdit.MACROPREFIX.length());
			for (TreeNode node : leaves)
			{
				javax.swing.tree.TreePath path = new javax.swing.tree.TreePath(tpedit.getModel().getPathToRoot(node));
//				System.out.println(stringrep+" "+(path.toString()));
				if (path.toString().equals(stringrep))
				{
//						System.out.println("+" + name);
					de.elbosso.tools.tpedit.Test testToInclude = (de.elbosso.tools.tpedit.Test) node;
					java.util.List actions=testToInclude.getActions();
					rv=new java.lang.String[actions.size()];
					for (int i=0;i<rv.length;++i)
					{
						rv[i]=actions.get(i).toString();
					}
				}
			}
			if(rv==null)
				rv=new java.lang.String[]{"Macro "+name+" not found!"};
		}
		return rv;
	}
	public String[] resultReferences(String name)
	{
		String[] rv=null;
		java.util.Set<TreeNode> leaves = tpedit.getAllLeafNodes((TreeNode) tpedit.getModel().getRoot());
		if (name.startsWith(TPEdit.MACROPREFIX))
		{
			java.lang.String stringrep = name.trim().substring(TPEdit.MACROPREFIX.length());
			for (TreeNode node : leaves)
			{
				javax.swing.tree.TreePath path = new javax.swing.tree.TreePath(tpedit.getModel().getPathToRoot(node));
//				System.out.println(stringrep+" "+(path.toString()));
				if (path.toString().equals(stringrep))
				{
//						System.out.println("+" + name);
					de.elbosso.tools.tpedit.Test testToInclude = (de.elbosso.tools.tpedit.Test) node;
					java.util.List results=testToInclude.getExpectedResults();
					rv=new java.lang.String[results.size()];
					for (int i=0;i<rv.length;++i)
					{
						rv[i]=results.get(i).toString();
					}
				}
			}
			if(rv==null)
				rv=new java.lang.String[]{"Macro "+name+" not found!"};
		}
		return rv;
	}
}
