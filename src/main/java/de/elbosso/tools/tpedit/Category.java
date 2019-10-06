package de.elbosso.tools.tpedit;
//$Id$
public class Category extends de.elbosso.model.tree.CloneableMutableTreeNode
{
	private javax.swing.tree.MutableTreeNode parent;
	private java.lang.String name;
	private java.lang.String description;
	private java.util.Vector tests=new java.util.Vector();
	private boolean justAdded;
	private boolean justEdited;

	public Category()
	{
		super();
	}
	public void setName(java.lang.String newname)
	{
		java.lang.String oldname = name;
		name = newname;
//		pcs.firePropertyChange("name",oldname,name);
	}
	public void setDescription(java.lang.String newdescription)
	{
		java.lang.String olddescription = description;
		description = newdescription.replace('\n',' ');

//		pcs.firePropertyChange("description",olddescription,description);
	}
	public void setJustAdded(boolean newjustAdded)
	{
		boolean oldjustAdded = justAdded;
		justAdded = newjustAdded;
//		pcs.firePropertyChange("justAdded",oldjustAdded,justAdded);
	}
	public void setJustEdited(boolean newjustEdited)
	{
		if(isJustAdded()==false)
		{
			boolean oldjustEdited = justEdited;
			justEdited = newjustEdited;
//			pcs.firePropertyChange("justEdited",oldjustEdited,justEdited);
		}
	}
	public java.lang.String getName()
	{
		return name;
	}
	public java.lang.String getDescription()
	{
		return description;
	}
	public boolean isJustAdded()
	{
		return justAdded;
	}
	public boolean isJustEdited()
	{
		return justEdited;
	}
	public void addTest(Test test)
	{
		boolean add=true;
		for (Object object : tests) {
			if((((Test)object).getID()==test.getID())&&(test.getID()>0))
			{
				add=false;
				break;
			}
		}
		if(add)
		{
			tests.add(test);
			test.setParent(this);
		}
	}
	public java.util.List getTests()
	{
		return tests;
	}
	public java.lang.String toString()
	{
		return name;
	}
	//Implementation of interface javax.swing.tree.TreeNode
	public javax.swing.tree.TreeNode getParent()
	{
		return parent;
	}
	//Implementation of interface javax.swing.tree.TreeNode
	public int getChildCount()
	{
		return tests.size();
	}
	//Implementation of interface javax.swing.tree.TreeNode
	public int getIndex(javax.swing.tree.TreeNode treeNode0)
	{
		return tests.indexOf(treeNode0);
	}
	//Implementation of interface javax.swing.tree.TreeNode
	public javax.swing.tree.TreeNode getChildAt(int int0)
	{
		return (javax.swing.tree.TreeNode)tests.get(int0);
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
		return new de.netsysit.util.IteratorDowngrader(tests.iterator());
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
		if(tests.remove(mutableTreeNode0))
			mutableTreeNode0.setParent(null);
	}
	//Implementation of interface javax.swing.tree.MutableTreeNode
	public void setUserObject(java.lang.Object object0)
	{
//		userObject=(java.util.List)object0;
	}
	//Implementation of interface javax.swing.tree.MutableTreeNode
	public void insert(javax.swing.tree.MutableTreeNode mutableTreeNode0, int int1)
	{
		if((javax.swing.tree.MutableTreeNode)mutableTreeNode0.getParent()!=null)
			((javax.swing.tree.MutableTreeNode)mutableTreeNode0.getParent()).remove(mutableTreeNode0);
		tests.insertElementAt(mutableTreeNode0,int1);

		mutableTreeNode0.setParent(this);

	}
	//Implementation of interface javax.swing.tree.MutableTreeNode
	public void remove(int int0)
	{
		((javax.swing.tree.MutableTreeNode)tests.remove(int0)).setParent(null);
		
	}
}
