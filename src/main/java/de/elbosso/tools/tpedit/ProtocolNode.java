package de.elbosso.tools.tpedit;
//$Id$
public class ProtocolNode extends de.elbosso.model.tree.CloneableMutableTreeNode
{
	private javax.swing.tree.MutableTreeNode parent;
	private java.util.Vector userObject;
	private java.lang.String appname;

	public ProtocolNode(java.util.List categories,java.lang.String appname)
	{
		super();
		userObject=new java.util.Vector(categories);
		for(java.util.Iterator iter=categories.iterator();iter.hasNext();)
			((Category)iter.next()).setParent(this);
		this.appname=appname;
	}
	public java.lang.String getAppname()
	{
		return appname;
	}
	public java.lang.String toString()
	{
		return appname;
	}
	public java.util.List getCategories()
	{
		return userObject;
	}
	//Implementation of interface javax.swing.tree.TreeNode
	public javax.swing.tree.TreeNode getParent()
	{
		return parent;
	}
	//Implementation of interface javax.swing.tree.TreeNode
	public int getChildCount()
	{
		return userObject.size();
	}
	//Implementation of interface javax.swing.tree.TreeNode
	public int getIndex(javax.swing.tree.TreeNode treeNode0)
	{
		return userObject.indexOf(treeNode0);
	}
	//Implementation of interface javax.swing.tree.TreeNode
	public javax.swing.tree.TreeNode getChildAt(int int0)
	{
		return (javax.swing.tree.TreeNode)userObject.get(int0);
	}
	//Implementation of interface javax.swing.tree.TreeNode
	public boolean getAllowsChildren()
	{
		return true;
	}
	//Implementation of interface javax.swing.tree.TreeNode
	public boolean isLeaf()
	{
		return false;
	}
	//Implementation of interface javax.swing.tree.TreeNode
	public java.util.Enumeration children()
	{
		return new de.netsysit.util.IteratorDowngrader(userObject.iterator());
	}
	//Implementation of interface javax.swing.tree.MutableTreeNode
	public void setParent(javax.swing.tree.MutableTreeNode mutableTreeNode0)
	{
		parent=mutableTreeNode0;
	}
	//Implementation of interface javax.swing.tree.MutableTreeNode
	public void removeFromParent()
	{
		parent.remove(this);
	}
	//Implementation of interface javax.swing.tree.MutableTreeNode
	public void remove(javax.swing.tree.MutableTreeNode mutableTreeNode0)
	{
		if(userObject.remove(mutableTreeNode0))
			mutableTreeNode0.setParent(null);
	}
	//Implementation of interface javax.swing.tree.MutableTreeNode
	public void setUserObject(java.lang.Object object0)
	{
		userObject=new java.util.Vector((java.util.List)object0);
	}
	//Implementation of interface javax.swing.tree.MutableTreeNode
	public void insert(javax.swing.tree.MutableTreeNode mutableTreeNode0, int int1)
	{
		if((javax.swing.tree.MutableTreeNode)mutableTreeNode0.getParent()!=null)
			((javax.swing.tree.MutableTreeNode)mutableTreeNode0.getParent()).remove(mutableTreeNode0);
		userObject.insertElementAt(mutableTreeNode0,int1);
		mutableTreeNode0.setParent(this);
	}
	//Implementation of interface javax.swing.tree.MutableTreeNode
	public void remove(int int0)
	{
		((javax.swing.tree.MutableTreeNode)userObject.remove(int0)).setParent(null);
		
	}
}
