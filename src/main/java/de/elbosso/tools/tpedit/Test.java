package de.elbosso.tools.tpedit;
//$Id$
public class Test extends de.elbosso.model.tree.CloneableMutableTreeNode implements
java.awt.datatransfer.Transferable
{
	private final static org.slf4j.Logger EXCEPTION_LOGGER = org.slf4j.LoggerFactory.getLogger("de.netsysit.experimental.ExceptionLogger");
	private final static org.slf4j.Logger CLASS_LOGGER = org.slf4j.LoggerFactory.getLogger(Test.class);
	private javax.swing.tree.MutableTreeNode parent;
	private int iD;
	private int fromVersionMajor;
	private int fromVersionMinor;
	private java.lang.String description;
	private java.util.List actions=new java.util.LinkedList();
	private java.util.List expectedResults=new java.util.LinkedList();
	private java.util.List variants=new java.util.LinkedList();
	private boolean justAdded;
	private boolean justEdited;
	private java.lang.String requirementId;
	private java.lang.String tags;

	public Test()
	{
		super();
	}
	public void setID(int newiD)
	{
		int oldiD = iD;
		iD = newiD;
//		pcs.firePropertyChange("iD",oldiD,iD);
	}
	public void setVariants(java.lang.String newvariants)
	{
		if(newvariants!=null)
		{
			java.util.StringTokenizer tok=new java.util.StringTokenizer(newvariants, ",");
			while(tok.hasMoreTokens())
				addVariant(tok.nextToken());
		}
	}
	public void setFromVersionMajor(int newfromVersionMajor)
	{
		int oldfromVersionMajor = fromVersionMajor;
		fromVersionMajor = newfromVersionMajor;
//		pcs.firePropertyChange("fromVersionMajor",oldfromVersionMajor,fromVersionMajor);
	}
	public void setFromVersionMinor(int newfromVersionMinor)
	{
		int oldfromVersionMinor = fromVersionMinor;
		fromVersionMinor = newfromVersionMinor;
//		pcs.firePropertyChange("fromVersionMinor",oldfromVersionMinor,fromVersionMinor);
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
	public void setRequirementId(java.lang.String newrequirementId)
	{
		java.lang.String oldrequirementId = requirementId;
		requirementId = newrequirementId;
//		pcs.firePropertyChange("requirementId",oldrequirementId,requirementId);
	}
	public void setTags(java.lang.String newtags)
	{
		java.lang.String old = getTags();
		tags = newtags;
//		pcs.firePropertyChange("tags",old,getTags);
	}
	public int getID()
	{
		return iD;
	}
	public java.util.List getVariants()
	{
		return variants;
	}
	public int getFromVersionMajor()
	{
		return fromVersionMajor;
	}
	public int getFromVersionMinor()
	{
		return fromVersionMinor;
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
	public void addAction(java.lang.String action)
	{
		if(CLASS_LOGGER.isDebugEnabled())CLASS_LOGGER.debug(action+" added");
		actions.add(action);
		if(CLASS_LOGGER.isDebugEnabled())CLASS_LOGGER.debug("added: "+actions);
	}
	public void addExpectedResult(java.lang.String expectedResult)
	{
		expectedResults.add(expectedResult);
	}
	public void addVariant(java.lang.String variant)
	{
		variants.add(variant);
	}
	public java.util.List getActions()
	{
		return actions;
	}
	public java.util.List getExpectedResults()
	{
		return expectedResults;
	}
	public java.lang.String getRequirementId()
	{
		return requirementId;
	}
	public java.lang.String getTags()
	{
		return tags;
	}
	public void clearActions()
	{
		actions=new java.util.LinkedList();
	}
	public void clearExpectedResults()
	{
		expectedResults=new java.util.LinkedList();
	}
	public void clearVariants()
	{
		variants=new java.util.LinkedList();
	}
	public java.lang.String toString()
	{
		return description;
	}
	//Implementation of interface javax.swing.tree.TreeNode
	public javax.swing.tree.TreeNode getParent()
	{
		return parent;
	}
	//Implementation of interface javax.swing.tree.TreeNode
	public int getChildCount()
	{
		return 0;//userObject.size();
	}
	//Implementation of interface javax.swing.tree.TreeNode
	public int getIndex(javax.swing.tree.TreeNode treeNode0)
	{
		return -1;//userObject.indexOf(treeNode0);
	}
	//Implementation of interface javax.swing.tree.TreeNode
	public javax.swing.tree.TreeNode getChildAt(int int0)
	{
		return null;//userObject.get(int0);
	}
	//Implementation of interface javax.swing.tree.TreeNode
	public boolean getAllowsChildren()
	{
		return false;
	}
	//Implementation of interface javax.swing.tree.TreeNode
	public boolean isLeaf()
	{
		return true;
	}
	//Implementation of interface javax.swing.tree.TreeNode
	public java.util.Enumeration children()
	{
		return null;//new IteratorDowngrader(userObject.iterator());
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
//		if(tests.remove(mutableTreeNode0))
//			mutableTreeNode0.setParent(null);
	}
	//Implementation of interface javax.swing.tree.MutableTreeNode
	public void setUserObject(java.lang.Object object0)
	{
//		userObject=(java.util.List)object0;
	}
	//Implementation of interface javax.swing.tree.MutableTreeNode
	public void insert(javax.swing.tree.MutableTreeNode mutableTreeNode0, int int1)
	{
//		tests.insertElementAt(mutableTreeNode0,int1);
//		mutableTreeNode0.setParent(this);
	}
	//Implementation of interface javax.swing.tree.MutableTreeNode
	public void remove(int int0)
	{
//		((javax.swing.tree.MutableTreeNode)tests.remove(int0)).setParent(null);

	}
	//Implementation of interface java.awt.datatransfer.Transferable
	public java.awt.datatransfer.DataFlavor[] getTransferDataFlavors()
	{
		java.awt.datatransfer.DataFlavor[] rv=null;
		try{
		 rv=new  java.awt.datatransfer.DataFlavor[]{new java.awt.datatransfer.DataFlavor(java.awt.datatransfer.DataFlavor.javaJVMLocalObjectMimeType)};
		}
		catch(java.lang.ClassNotFoundException exp){exp.printStackTrace();}
		return rv;
	}
	//Implementation of interface java.awt.datatransfer.Transferable
	public java.lang.Object getTransferData(java.awt.datatransfer.DataFlavor dataFlavor0)
	{
		return this;
	}
	//Implementation of interface java.awt.datatransfer.Transferable
	public boolean isDataFlavorSupported(java.awt.datatransfer.DataFlavor dataFlavor0)
	{
		boolean rv=false;
		try{
		rv= dataFlavor0.equals(new java.awt.datatransfer.DataFlavor(java.awt.datatransfer.DataFlavor.javaJVMLocalObjectMimeType));
		}
		catch(java.lang.ClassNotFoundException exp){exp.printStackTrace();}
		return rv;
	}

	String getVariantsAsOneString()
	{
		java.lang.StringBuffer buf=new java.lang.StringBuffer();
		for(Object elem: variants)
		{
			if(buf.length()>0)
				buf.append(",");
			buf.append(elem.toString());
		}
		return buf.toString();
	}

	void addTags(String value)
	{
		if(tags==null)
			tags=value;
		else
		{
			if((value!=null)&&(value.trim()).length()>0)
			{
				if(tags.trim().length()>0)
					tags+=',';
				tags+=value;
			}
		}
	}

	public void updateActionMacros(String oldStringRep, String newStringRep)
	{
		java.util.List actionsCopy=new java.util.LinkedList(actions);
		for(int i=0;i<actionsCopy.size();++i)
		{
			java.lang.String current=actionsCopy.get(i).toString().trim();
			if(current.startsWith(TPEdit.MACROPREFIX))
			{
				if (current.equals(oldStringRep))
					actions.set(i, newStringRep);
				else
				{
					java.lang.String oldPrefix=oldStringRep.substring(0,oldStringRep.length()-1);
					if(current.startsWith(oldPrefix))
					{
						java.lang.String newPrefix=newStringRep.substring(0,newStringRep.length()-1);
						actions.set(i,newPrefix+(current.substring(oldPrefix.length())));
					}
				}
			}
		}
	}
	public void updateResultMacros(String oldStringRep, String newStringRep)
	{
		java.util.List expectedResultsCopy=new java.util.LinkedList(expectedResults);
		for(int i=0;i<expectedResultsCopy.size();++i)
		{
			java.lang.String current=expectedResultsCopy.get(i).toString().trim();
			if(current.startsWith(TPEdit.MACROPREFIX))
			{
				if (current.equals(oldStringRep))
					expectedResults.set(i, newStringRep);
				else
				{
					java.lang.String oldPrefix=oldStringRep.substring(0,oldStringRep.length()-1);
					if(current.startsWith(oldPrefix))
					{
						java.lang.String newPrefix=newStringRep.substring(0,newStringRep.length()-1);
						expectedResults.set(i,newPrefix+(current.substring(oldPrefix.length())));
					}
				}
			}
		}
	}

	public void deleteActionMacros(String stringRep)
	{
		java.util.List actionsCopy=new java.util.LinkedList(actions);
		for(int i=0;i<actionsCopy.size();++i)
		{
			java.lang.String current=actionsCopy.get(i).toString().trim();

			if(current.startsWith(TPEdit.MACROPREFIX))
			{
				if (current.equals(stringRep))
					actions.remove(stringRep);
				else
				{
					java.lang.String prefix=stringRep.substring(0,stringRep.length()-1);
					if(current.startsWith(prefix))
					{
						actions.remove(current);
					}
				}
			}
		}
	}
	public void deleteResultMacros(String stringRep)
	{
		java.util.List expectedResultsCopy=new java.util.LinkedList(expectedResults);
		for(int i=0;i<expectedResultsCopy.size();++i)
		{
			java.lang.String current=expectedResultsCopy.get(i).toString().trim();
			if(current.startsWith(TPEdit.MACROPREFIX))
			{
				if (current.equals(stringRep))
					expectedResults.remove(stringRep);
				else
				{
					java.lang.String prefix=stringRep.substring(0,stringRep.length()-1);
					if(current.startsWith(prefix))
					{
						expectedResults.remove(current);
					}
				}
			}
		}
	}
}
