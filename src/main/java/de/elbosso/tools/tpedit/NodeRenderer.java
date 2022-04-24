/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package de.elbosso.tools.tpedit;

import javax.swing.tree.TreeNode;
import java.io.IOException;

/**
 *
 * @author juergen
 */
class NodeRenderer extends javax.swing.tree.DefaultTreeCellRenderer
{
	private final static org.slf4j.Logger EXCEPTION_LOGGER = org.slf4j.LoggerFactory.getLogger("de.netsysit.experimental.ExceptionLogger");
	private final static org.slf4j.Logger CLASS_LOGGER = org.slf4j.LoggerFactory.getLogger(NodeRenderer.class);
	private java.net.URL newurl;
	private java.net.URL updatedurl;
	private java.awt.image.BufferedImage testimg;
	private java.awt.image.BufferedImage categoryimg;
	private java.awt.image.BufferedImage catimg;
	private java.awt.image.BufferedImage emptycategoryimg;

	NodeRenderer()
	{
		super();
		newurl=de.netsysit.util.ResourceLoader.getImgResource("de/netsysit/ressources/gfx/symbols/New16.gif");
		updatedurl=de.netsysit.util.ResourceLoader.getImgResource("de/netsysit/ressources/gfx/symbols/Pencil16.gif");
	}
	@Override
	public java.awt.Component getTreeCellRendererComponent(
					javax.swing.JTree tree,
					java.lang.Object value,
					boolean sel,
					boolean expanded,
					boolean leaf,
					int row,
					boolean hasFocus)
	{
		super.getTreeCellRendererComponent(tree, value, sel,expanded, leaf, row,hasFocus);
		javax.swing.ImageIcon icon=null;//TABLE_ICON;
			if((categoryimg==null)||(emptycategoryimg==null))
			{
				javax.swing.Icon defaulticon=getIcon();
				java.awt.image.BufferedImage bimg=null;
				try
				{
					bimg = javax.imageio.ImageIO.read(de.netsysit.util.ResourceLoader.getResource("de/netsysit/ressources/gfx/ca/Kategorie_32.png"));
				}
				catch (IOException ex)
				{
				}
//				defaulticon.paintIcon(tree, bimg.getGraphics(), 0, 0);
				categoryimg=bimg;
				try
				{
					bimg = javax.imageio.ImageIO.read(de.netsysit.util.ResourceLoader.getResource("de/elbosso/ressources/gfx/eb/Kategorie_leer_32.png"));
				}
				catch (IOException ex)
				{
				}
//				defaulticon.paintIcon(tree, bimg.getGraphics(), 0, 0);
				emptycategoryimg=bimg;
			}
		if (value instanceof Test)
		{
			if(testimg==null)
			{
				javax.swing.Icon defaulticon=getIcon();
				java.awt.image.BufferedImage bimg=null;
				try
				{
					bimg = javax.imageio.ImageIO.read(de.netsysit.util.ResourceLoader.getResource("de/netsysit/ressources/gfx/common/Document16.gif"));
				}
				catch (IOException ex)
				{
				}
//				defaulticon.paintIcon(tree, bimg.getGraphics(), 0, 0);
				testimg=bimg;
			}
			icon=new javax.swing.ImageIcon(testimg);
			try
			{
				if(((Test)value).isJustAdded())
					icon=new javax.swing.ImageIcon(de.netsysit.ui.image.DecoratedImageProducer.produceImage(testimg,newurl));
				else if(((Test)value).isJustEdited())
					icon=new javax.swing.ImageIcon(de.netsysit.ui.image.DecoratedImageProducer.produceImage(testimg,updatedurl));
			}
			catch(java.io.IOException exp)
			{
			}
		}
		else if (value instanceof Category)
		{
			if(((Category)value).getChildCount()>0)
				catimg=categoryimg;
			else
				catimg=emptycategoryimg;
			try
			{
				if(((Category)value).isJustAdded())
					icon=new javax.swing.ImageIcon(de.netsysit.ui.image.DecoratedImageProducer.produceImage(catimg,newurl));
				else if(((Category)value).isJustEdited())
					icon=new javax.swing.ImageIcon(de.netsysit.ui.image.DecoratedImageProducer.produceImage(catimg,updatedurl));
				else
				{
					java.util.List tcl=((Category)value).getTests();
					icon=new javax.swing.ImageIcon(catimg);
					for (Object object : tcl)
					{
						Test tc=(Test)object;
						if((tc.isJustAdded())||(tc.isJustEdited()))
						{
							icon=new javax.swing.ImageIcon(de.netsysit.ui.image.DecoratedImageProducer.produceImage(catimg,updatedurl));
							break;
						}
					}
				}
			}
			catch(java.io.IOException exp)
			{
				icon=null;
			}
		}
		else
		{
			if(((TreeNode)value).getChildCount()>0)
				icon=new javax.swing.ImageIcon(categoryimg);
			else
				icon=new javax.swing.ImageIcon(emptycategoryimg);
		}
		if(icon!=null)
		{
			setIcon(icon);
			setDisabledIcon(icon);
		}
		return this;
	}
}
