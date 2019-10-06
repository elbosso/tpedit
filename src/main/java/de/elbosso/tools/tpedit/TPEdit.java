package de.elbosso.tools.tpedit;
//$Id$

import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.PdfReaderContentParser;
import de.netsysit.ui.dialog.GeneralPurposeInfoDialog;
import org.apache.log4j.Level;

import javax.swing.*;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.util.Collections;


public class TPEdit extends javax.swing.JFrame implements
javax.swing.event.TreeSelectionListener
,java.awt.dnd.DragGestureListener
,java.awt.dnd.DragSourceListener
,java.awt.event.MouseListener
{
	private final static org.apache.log4j.Logger EXCEPTION_LOGGER = org.apache.log4j.Logger.getLogger("de.netsysit.experimental.ExceptionLogger");
	private final static org.apache.log4j.Logger CLASS_LOGGER = org.apache.log4j.Logger.getLogger(TPEdit.class);
	private final static java.util.ResourceBundle i18n = java.util.ResourceBundle.getBundle("de.elbosso.tools.i18n", java.util.Locale.getDefault());
	private static final java.lang.String[] XMLSUFFIXES = {"xml", "xml.gz"};
	private static final java.lang.String[] TRANSFORMSUFFIXES = {"pdf", "xml", "diff.pdf", "form"};
	private final static int inset = 50;
	public final static java.lang.String NOTEDITABLESTART = ">>> include - do not edit below!";
	public final static java.lang.String NOTEDITABLEEND = "include - do not edit above! <<<";
	static final String MACROPREFIX = "§";
	private javax.swing.Action newaction;
	private de.netsysit.util.pattern.command.ChooseFileAction saveAs;
	private de.netsysit.util.pattern.command.ChooseFileAction open;
	private de.netsysit.util.pattern.command.ChooseFileAction export;
	private de.netsysit.util.pattern.command.ChooseFileAction importAction;
	private de.netsysit.util.pattern.command.ChooseFileAction saveFullPDF;
	private javax.swing.Action previewPDF;
	private de.netsysit.util.pattern.command.ChooseFileAction interactivePDF;
	private javax.swing.Action save;
	private javax.swing.Action addaction;
	private javax.swing.Action editaction;
	private javax.swing.Action editokaction;
	private javax.swing.Action editcancelaction;
	private javax.swing.Action removeaction;
	private javax.swing.Action copyaction;
	private javax.swing.Action pasteaction;
	private javax.swing.Action insertReferenceToActionsAction;
	private javax.swing.Action insertReferenceToExpectedResultsAction;
	private javax.swing.JToolBar toolbar = new javax.swing.JToolBar();
	private javax.swing.tree.DefaultTreeModel model = new javax.swing.tree.DefaultTreeModel(new javax.swing.tree.DefaultMutableTreeNode());
	private Tree tree = new Tree(model);
	private javax.swing.JScrollPane treescroller = new javax.swing.JScrollPane(tree);
	private javax.swing.JLabel lbl = new javax.swing.JLabel();
	private javax.swing.JScrollPane lblscroller = new javax.swing.JScrollPane(lbl);
	private javax.swing.JPanel bottomComponent = new javax.swing.JPanel(new java.awt.BorderLayout());
	private ProtocolNode protocolNode;
	private java.lang.String appname;
	private de.netsysit.util.pattern.command.CollapseSelectedTreeAction collapseSelectedTreeAction = new de.netsysit.util.pattern.command.CollapseSelectedTreeAction(tree, i18n.getString("I18NEditor.collapseSelectedTreeAction.text"), de.netsysit.util.ResourceLoader.getIcon("de/netsysit/ressources/gfx/common/CollapseSelected24.gif"));
	private de.netsysit.util.pattern.command.ExpandSelectedTreeAction expandSelectedTreeAction = new de.netsysit.util.pattern.command.ExpandSelectedTreeAction(tree, i18n.getString("I18NEditor.expandSelectedTreeAction.text"), de.netsysit.util.ResourceLoader.getIcon("de/netsysit/ressources/gfx/common/ExpandSelected24.gif"));
	private de.netsysit.util.pattern.command.CollapseAllTreeAction collapseTreeAction = new de.netsysit.util.pattern.command.CollapseAllTreeAction(tree, i18n.getString("I18NEditor.collapseTreeAction.text"), de.netsysit.util.ResourceLoader.getIcon("de/netsysit/ressources/gfx/common/CollapseAll24.gif"));
	private de.netsysit.util.pattern.command.ExpandAllTreeAction expandTreeAction = new de.netsysit.util.pattern.command.ExpandAllTreeAction(tree, i18n.getString("I18NEditor.expandTreeAction.text"), de.netsysit.util.ResourceLoader.getIcon("de/netsysit/ressources/gfx/common/ExpandAll24.gif"));
	private javax.swing.JPanel editendactions = new javax.swing.JPanel(new java.awt.BorderLayout());
	private javax.swing.JTextField nametf;
	private de.netsysit.ui.text.TextEditorComponent descta;
	private javax.swing.JTextField minortf;
	private javax.swing.JTextField majortf;
	private javax.swing.JTextField requirementidtf;
//	private javax.swing.JTextField tagstf;
	private javax.swing.JTextField variantstf;
	private javax.swing.JTextField desctf;
	private de.netsysit.ui.text.TextEditorComponent actionsta;
	private de.netsysit.ui.text.TextEditorComponent eresultsta;
	private javax.swing.JPanel ceditor;
	private javax.swing.JPanel teditor;
	private java.awt.dnd.DragSource dragsource = new java.awt.dnd.DragSource();
	private de.netsysit.ui.components.DragImageGlassPane glassPane = new de.netsysit.ui.components.DragImageGlassPane(this, false);
	private Category hovercat;
	private long hoverthen;
	private Test[] copied;
	//	private java.awt.dnd.DropTarget droptarget = new java.awt.dnd.DropTarget (tree, this);
	private TransformProcessor transformProcessor;
	private javax.swing.JMenuBar menuBar = new javax.swing.JMenuBar();
	private javax.swing.JMenu projmenu;
	private javax.swing.JMenu editmenu;
	private javax.swing.JMenu toolsmenu;
	private de.elbosso.ui.components.TagManager tagManager;
	private QaTeamMembersList qaTeamMemberSignatures;
	private VelocityHelper velocityHelper;
	private NodeChooserPanel nodeChooserPanel;
	private GeneralPurposeInfoDialog tagsDlg;
	private JPanel tagsDlgPanel;

	public TPEdit(java.lang.String name) throws Exception
	{
		super(name);
		velocityHelper = new VelocityHelper(this);
		bottomComponent.add(lblscroller);
		qaTeamMemberSignatures = new QaTeamMembersList();
		tree.getSelectionModel().setSelectionMode(javax.swing.tree.TreeSelectionModel.DISCONTIGUOUS_TREE_SELECTION);
		tree.setCellRenderer(new NodeRenderer());
		lbl.setBackground(java.awt.Color.WHITE);
		lbl.setVerticalTextPosition(javax.swing.SwingConstants.TOP);
		lbl.setVerticalAlignment(javax.swing.SwingConstants.TOP);
		lbl.setOpaque(true);
		treescroller.setPreferredSize(new java.awt.Dimension(350, 500));
		treescroller.setMinimumSize(new java.awt.Dimension(350, 500));
		lblscroller.setPreferredSize(new java.awt.Dimension(530, 500));
		lblscroller.setMinimumSize(new java.awt.Dimension(530, 500));
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		javax.swing.JPanel p = new javax.swing.JPanel(new java.awt.BorderLayout());
		p.add(toolbar, java.awt.BorderLayout.NORTH);
		de.netsysit.ui.components.DockingPanel dockingPanel = new de.netsysit.ui.components.DockingPanel(bottomComponent, SwingConstants.WEST);
		dockingPanel.addDockable(treescroller, i18n.getString("TPEdit.dockable.treescroller"));
		dockingPanel.addDockable(qaTeamMemberSignatures, i18n.getString("TPEdit.dockable.qaTeamMemberSignatures"));
		p.add(dockingPanel);
		setContentPane(p);
		addWindowListener(new java.awt.event.WindowListener()
		{
			//Implementation of interface java.awt.event.WindowListener
			public void windowActivated(java.awt.event.WindowEvent windowEvent0)
			{
			}

			//Implementation of interface java.awt.event.WindowListener
			public void windowOpened(java.awt.event.WindowEvent windowEvent0)
			{
			}

			//Implementation of interface java.awt.event.WindowListener
			public void windowIconified(java.awt.event.WindowEvent windowEvent0)
			{
			}

			//Implementation of interface java.awt.event.WindowListener
			public void windowClosing(java.awt.event.WindowEvent windowEvent0)
			{
				if (saveAs.isEnabled())
					de.elbosso.util.Utilities.performAction(this, saveAs);
			}

			//Implementation of interface java.awt.event.WindowListener
			public void windowDeiconified(java.awt.event.WindowEvent windowEvent0)
			{
			}

			//Implementation of interface java.awt.event.WindowListener
			public void windowDeactivated(java.awt.event.WindowEvent windowEvent0)
			{
			}

			//Implementation of interface java.awt.event.WindowListener
			public void windowClosed(java.awt.event.WindowEvent windowEvent0)
			{
			}
		});
		createActions();
		populateToolBar();
		setJMenuBar(createMenuBar());
//		tree.setRootVisible(false);
		expandTreeAction.setEnabled(false);
		collapseTreeAction.setEnabled(false);
		expandSelectedTreeAction.setEnabled(false);
		collapseSelectedTreeAction.setEnabled(false);
		expandTreeAction.putValue(javax.swing.Action.SHORT_DESCRIPTION, i18n.getString("I18NEditor.expandTreeAction.tooltip"));
		collapseTreeAction.putValue(javax.swing.Action.SHORT_DESCRIPTION, i18n.getString("I18NEditor.collapseTreeAction.tooltip"));
		expandSelectedTreeAction.putValue(javax.swing.Action.SHORT_DESCRIPTION, i18n.getString("I18NEditor.expandSelectedTreeAction.tooltip"));
		collapseSelectedTreeAction.putValue(javax.swing.Action.SHORT_DESCRIPTION, i18n.getString("I18NEditor.collapseSelectedTreeAction.tooltip"));
		java.awt.Rectangle screenSize = this.getGraphicsConfiguration().getBounds();
		setBounds(inset, inset, screenSize.width - 3 * inset, screenSize.height - 4 * inset);
		setBounds(de.elbosso.ui.Utilities.loadDimensions(TPEdit.class));
//		pack();
		setVisible(true);
		java.util.Properties props = new java.util.Properties();
		props.setProperty("resource.loader", "classpath");
		props.setProperty("classpath.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
//		p.setProperty("runtime.log.logsystem.class","org.slf4j.helpers.NOPLogger");
//		p.setProperty(org.apache.velocity.runtime.RuntimeConstants.RUNTIME_LOG_LOGSYSTEM_CLASS,"org.apache.velocity.runtime.log.Log4JLogChute");
//		p.setProperty(org.apache.velocity.runtime.log.Log4JLogChute.RUNTIME_LOG_LOG4J_LOGGER, "Velocity.Log");
		org.apache.velocity.app.Velocity.init(props);
		javax.swing.JPanel editendactioncontainer = new javax.swing.JPanel(new java.awt.GridLayout(1, 0));
		editendactioncontainer.add(new javax.swing.JButton(editcancelaction));
		editendactioncontainer.add(new javax.swing.JButton(editokaction));
		editendactions.add(editendactioncontainer, java.awt.BorderLayout.EAST);
//		editendactions.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createEmptyBorder(2,2,2,2),new de.netsysit.ui.components.ShadowBorder(4)));
		dragsource.createDefaultDragGestureRecognizer(tree, java.awt.dnd.DnDConstants.ACTION_MOVE, this);
		glassPane.setOpaque(false);
		tagManager = new de.elbosso.ui.components.TagManager(Collections.EMPTY_LIST);
	}

	public void dragGestureRecognized(java.awt.dnd.DragGestureEvent event)
	{
		javax.swing.tree.TreePath tp = tree.getSelectionPath();
		if (tp != null)
		{
			java.lang.Object obj = tp.getPath()[tp.getPathCount() - 1];
			if (obj instanceof Test)
			{
				Test todrag = (Test) obj;
				if (getRootPane().getGlassPane() != glassPane)
				{
					getRootPane().setGlassPane(glassPane);
				}
				java.awt.image.BufferedImage img = glassPane.prepareDragOperation(tree, event);
/*				de.netsysit.db.logic.ColumnDescription cd=new de.netsysit.db.logic.ColumnDescription();
				cd.setTablename(tableDescription.getTablename());
				cd.setSchemaname(tableDescription.getSchemaname());
				cd.setColumnname(columnname);
				cd.setWidgetId(getWidgetId());
				java.io.ByteArrayOutputStream baos=new java.io.ByteArrayOutputStream();
				java.beans.XMLEncoder encoder=new java.beans.XMLEncoder(baos);
				encoder.writeObject(cd);
				try{
				baos.close();
				}catch(java.io.IOException exp){}
				encoder.close();
*///				java.awt.datatransfer.StringSelection text = new  java.awt.datatransfer.StringSelection("huhu");// baos.toString());
				dragsource.startDrag(event, new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR), img, new java.awt.Point(5, 5), todrag, this);
//				dragsource.startDrag (event, java.awt.dnd.DragSource.DefaultCopyDrop,null,new java.awt.Point(5,5), text, this);
			}
		}
	}

	public void dragDropEnd(java.awt.dnd.DragSourceDropEvent dsde)
	{
	}

	public void dragEnter(java.awt.dnd.DragSourceDragEvent dsde)
	{

		glassPane.setUsedDecoration(de.netsysit.ui.components.DragImageGlassPane.OKDECORATION);
	}

	public void dragExit(java.awt.dnd.DragSourceEvent dse)
	{
		glassPane.setUsedDecoration(de.netsysit.ui.components.DragImageGlassPane.DENIEDDECORATION);
	}

	public void dragOver(java.awt.dnd.DragSourceDragEvent dsde)
	{
	}

	public void dropActionChanged(java.awt.dnd.DragSourceDragEvent dsde)
	{
	}

	private void createActions()
	{
		newaction =
				new javax.swing.AbstractAction(i18n.getString("TPEdit.newaction.text"), de.netsysit.util.ResourceLoader.getIcon("toolbarButtonGraphics/general/New24.gif"))
				{
					;

					public void actionPerformed(java.awt.event.ActionEvent evt)
					{
						newtp();
						saveAs.getFilechooser().setSelectedFile(null);
						save.setEnabled(false);
						importAction.setEnabled(true);
						qaTeamMemberSignatures.clear();
					}
				};
		newaction.putValue(javax.swing.Action.ACCELERATOR_KEY, javax.swing.KeyStroke.getKeyStroke(i18n.getString("TPEdit.newaction.accelerator")));
//		newaction.putValue(javax.swing.Action.MNEMONIC_KEY,java.lang.Integer.valueOf(javax.swing.KeyStroke.getKeyStroke(i18n.getString("TPEdit.newaction.mnemonic")).getKeyCode()));
		newaction.putValue(javax.swing.Action.SHORT_DESCRIPTION, i18n.getString("TPEdit.newaction.tooltip"));
		save =
				new javax.swing.AbstractAction(i18n.getString("TPEdit.save.text"), de.netsysit.util.ResourceLoader.getIcon("toolbarButtonGraphics/general/Save24.gif"))
				{
					;

					public void actionPerformed(java.awt.event.ActionEvent evt)
					{
						if (saveAs.getFilechooser().getSelectedFile() != null)
						{
							savetp(saveAs.getFilechooser().getSelectedFile());
							saveAs.setEnabled(false);
							save.setEnabled(false);
						}
					}
				};
		save.putValue(javax.swing.Action.ACCELERATOR_KEY, javax.swing.KeyStroke.getKeyStroke(i18n.getString("TPEdit.save.accelerator")));
//		save.putValue(javax.swing.Action.MNEMONIC_KEY,java.lang.Integer.valueOf(javax.swing.KeyStroke.getKeyStroke(i18n.getString("TPEdit.save.mnemonic")).getKeyCode()));
		save.putValue(javax.swing.Action.SHORT_DESCRIPTION, i18n.getString("TPEdit.save.tooltip"));
		save.setEnabled(false);
		de.netsysit.util.pattern.command.FileProcessor exportNodesClient =
				new de.netsysit.util.pattern.command.FileProcessor()
				{
					public boolean process(java.io.File[] files)
					{
						return exportNodes(files[0]);
					}
				};
		export = new de.netsysit.util.pattern.command.ChooseFileAction(exportNodesClient, i18n.getString("TPEdit.export.text"), de.netsysit.util.ResourceLoader.getIcon("toolbarButtonGraphics/general/Export24.gif"));
		de.netsysit.ui.filechooser.ShortcutsAccessory shortcuts = new de.netsysit.ui.filechooser.ShortcutsAccessory(export.getFilechooser(), new java.io.File(System.getProperty("user.home")), ".TPEdit");
		shortcuts.setPreferredSize(new java.awt.Dimension(200, 250));
		de.netsysit.ui.filechooser.PopUpAccessory popup = new de.netsysit.ui.filechooser.PopUpAccessory(export.getFilechooser(), shortcuts.getToolTipText(), shortcuts);
		popup.setPopUpBorder(javax.swing.BorderFactory.createEtchedBorder());
		export.getFilechooser().setAccessory(popup);
		export.getFilechooser().setMultiSelectionEnabled(false);
		export.getFilechooser().setFileSelectionMode(javax.swing.JFileChooser.FILES_ONLY);
		export.getFilechooser().setAcceptAllFileFilterUsed(false);
		export.setParent(this);
		export.setAllowedSuffixes(XMLSUFFIXES);
		export.setSaveDialog(true);
		export.putValue(javax.swing.Action.SHORT_DESCRIPTION, i18n.getString("TPEdit.export.tooltip"));
		export.setEnabled(false);
		export.setDefaultFileEnding(".xml");
		de.netsysit.util.pattern.command.FileProcessor savePropertiesClient =
				new de.netsysit.util.pattern.command.FileProcessor()
				{
					public boolean process(java.io.File[] files)
					{
						boolean rv = savetp(files[0]);
						saveAs.setEnabled(false);
						save.setEnabled(false);
						return rv;
					}
				};
		saveAs = new de.netsysit.util.pattern.command.ChooseFileAction(savePropertiesClient, i18n.getString("TPEdit.saveAs.text"), de.netsysit.util.ResourceLoader.getIcon("toolbarButtonGraphics/general/SaveAs24.gif"));
		shortcuts = new de.netsysit.ui.filechooser.ShortcutsAccessory(saveAs.getFilechooser(), new java.io.File(System.getProperty("user.home")), ".TPEdit");
		shortcuts.setPreferredSize(new java.awt.Dimension(200, 250));
		popup = new de.netsysit.ui.filechooser.PopUpAccessory(saveAs.getFilechooser(), shortcuts.getToolTipText(), shortcuts);
		popup.setPopUpBorder(javax.swing.BorderFactory.createEtchedBorder());
		saveAs.getFilechooser().setAccessory(popup);
		saveAs.getFilechooser().setMultiSelectionEnabled(false);
		saveAs.getFilechooser().setFileSelectionMode(javax.swing.JFileChooser.FILES_ONLY);
		saveAs.getFilechooser().setAcceptAllFileFilterUsed(false);
		saveAs.setParent(this);
		saveAs.setAllowedSuffixes(XMLSUFFIXES);
		saveAs.setSaveDialog(true);
		saveAs.putValue(javax.swing.Action.ACCELERATOR_KEY, javax.swing.KeyStroke.getKeyStroke(i18n.getString("TPEdit.saveAs.accelerator")));
//		saveAs.putValue(javax.swing.Action.MNEMONIC_KEY,java.lang.Integer.valueOf(javax.swing.KeyStroke.getKeyStroke(i18n.getString("TPEdit.saveAs.mnemonic")).getKeyCode()));
		saveAs.putValue(javax.swing.Action.SHORT_DESCRIPTION, i18n.getString("TPEdit.saveAs.tooltip"));
		saveAs.setEnabled(false);
		saveAs.setDefaultFileEnding(".xml");
		try
		{
			transformProcessor = (TransformProcessor) java.lang.Class.forName("de.elbosso.tools.tpedit.TransformIntoPDFProcessor").getConstructor().newInstance();
			de.netsysit.util.pattern.command.FileProcessor saveFullPDFClient =
					new de.netsysit.util.pattern.command.FileProcessor()
					{
						public boolean process(final java.io.File[] files)
						{
							final de.netsysit.ui.dialog.InfoProgressMonitor pid = de.netsysit.ui.dialog.InfoProgressMonitor.create(TPEdit.this, "Preview", 500, (java.lang.String) null);
							pid.setIndeterminate(true);
							pid.setFocusableWindowState(false);
							pid.setString("");
							pid.pack();
							pid.setLocationRelativeTo(TPEdit.this);
							pid.showDialog();
							java.lang.Runnable rble = new java.lang.Runnable()
							{
								public void run()
								{
									generatePDF(files[0]);
									javax.swing.SwingUtilities.invokeLater(new java.lang.Runnable()
									{
										public void run()
										{
											pid.close();
											if(files[0].getName().toUpperCase().endsWith(".PDF"))
												de.netsysit.ui.dialog.GeneralPurposeInfoDialog.showComponentInDialog(new javax.swing.JLabel("wrote " + transformProcessor.getNumberOfPages() + " pages in file " + files[0]));
										}
									});
								}
							};
							java.lang.Thread t = new java.lang.Thread(rble);
							t.start();
/*		try
		{
			t.join();
		} catch (InterruptedException e)
		{
			de.elbosso.util.Utilities.handleException(EXCEPTION_LOGGER,this,e,e.getMessage());
		}
*/
							return false;
						}
					};
			saveFullPDF = new de.netsysit.util.pattern.command.ChooseFileAction(saveFullPDFClient, i18n.getString("TPEdit.saveFullPDF.text"), new javax.swing.ImageIcon(de.netsysit.ui.image.DecoratedImageProducer.produceImage(de.netsysit.util.ResourceLoader.getImgResource("toolbarButtonGraphics/general/Export24.gif"), "PDF", null)));
			shortcuts = new de.netsysit.ui.filechooser.ShortcutsAccessory(saveFullPDF.getFilechooser(), new java.io.File(System.getProperty("user.home")), ".TPEdit");
			shortcuts.setPreferredSize(new java.awt.Dimension(200, 250));
			popup = new de.netsysit.ui.filechooser.PopUpAccessory(saveFullPDF.getFilechooser(), shortcuts.getToolTipText(), shortcuts);
			popup.setPopUpBorder(javax.swing.BorderFactory.createEtchedBorder());
			saveFullPDF.getFilechooser().setAccessory(popup);
			saveFullPDF.getFilechooser().setMultiSelectionEnabled(false);
			saveFullPDF.getFilechooser().setFileSelectionMode(javax.swing.JFileChooser.FILES_ONLY);
			saveFullPDF.getFilechooser().setAcceptAllFileFilterUsed(false);
			saveFullPDF.setParent(this);
			saveFullPDF.setAllowedSuffixes(TRANSFORMSUFFIXES);
			saveFullPDF.setSaveDialog(true);
			saveFullPDF.putValue(javax.swing.Action.SHORT_DESCRIPTION, i18n.getString("TPEdit.saveFullPDF.tooltip"));
			saveFullPDF.setEnabled(false);
//			saveFullPDF.setDefaultFileEnding(".pdf");
			previewPDF = new javax.swing.AbstractAction(i18n.getString("TPEdit.previewPDF.text"), de.netsysit.util.ResourceLoader.getIcon("toolbarButtonGraphics/general/PrintPreview24.gif"))
			{

				public void actionPerformed(ActionEvent e)
				{
					final de.netsysit.ui.dialog.InfoProgressMonitor pid = de.netsysit.ui.dialog.InfoProgressMonitor.create(TPEdit.this, "Preview", 500, (java.lang.String) null);
					pid.setIndeterminate(true);
					pid.setFocusableWindowState(false);
					pid.setString("");
					pid.pack();
					pid.setLocationRelativeTo(TPEdit.this);
					pid.showDialog();
					java.lang.Runnable rble = new java.lang.Runnable()
					{
						public void run()
						{
							try
							{
								java.io.File tf = java.io.File.createTempFile("tpedit", ".pdf");
								tf.deleteOnExit();

								generatePDF(tf);
								de.netsysit.util.lowlevel.BareBonesPDFViewer.openFile(tf);
							} catch (java.io.IOException exp)
							{
								de.elbosso.util.Utilities.handleException(null, getContentPane(), exp);
							} finally
							{
								javax.swing.SwingUtilities.invokeLater(new java.lang.Runnable()
								{
									public void run()
									{
										pid.close();
									}
								});
							}
						}
					};
					java.lang.Thread t = new java.lang.Thread(rble);
					t.start();
/*		try
		{
			t.join();
		} catch (InterruptedException e)
		{
			de.elbosso.util.Utilities.handleException(EXCEPTION_LOGGER,this,e,e.getMessage());
		}
*/
				}
			};
			previewPDF.putValue(javax.swing.Action.SHORT_DESCRIPTION, i18n.getString("TPEdit.previewPDF.tooltip"));
			previewPDF.setEnabled(false);
			de.netsysit.util.pattern.command.FileProcessor interactivePDFClient =
					new de.netsysit.util.pattern.command.FileProcessor()
					{
						public boolean process(final java.io.File[] files)
						{
							final de.netsysit.ui.dialog.InfoProgressMonitor pid = de.netsysit.ui.dialog.InfoProgressMonitor.create(TPEdit.this, "Preview", 500, (java.lang.String) null);
							pid.setIndeterminate(true);
							pid.setFocusableWindowState(false);
							pid.setString("");
							pid.pack();
							pid.setLocationRelativeTo(TPEdit.this);
							pid.showDialog();
							java.lang.Runnable rble = new java.lang.Runnable()
							{
								public void run()
								{
									try
									{
										java.io.File tf = java.io.File.createTempFile("tpedit", ".form");
										tf.deleteOnExit();

										generatePDF(tf);
										makeInteractive(tf, files[0]);
									} catch (java.lang.Exception exp)
									{
										de.elbosso.util.Utilities.handleException(null, getContentPane(), exp);
									} finally
									{
										javax.swing.SwingUtilities.invokeLater(new java.lang.Runnable()
										{
											public void run()
											{
												pid.close();
												de.netsysit.ui.dialog.GeneralPurposeInfoDialog.showComponentInDialog(new javax.swing.JLabel("wrote " + transformProcessor.getNumberOfPages() + " pages in file " + files[0]));
											}
										});
									}
								}
							};
							java.lang.Thread t = new java.lang.Thread(rble);
							t.start();
/*		try
		{
			t.join();
		} catch (InterruptedException e)
		{
			de.elbosso.util.Utilities.handleException(EXCEPTION_LOGGER,this,e,e.getMessage());
		}
*/
							return false;
						}
					};
			interactivePDF = new de.netsysit.util.pattern.command.ChooseFileAction(interactivePDFClient, i18n.getString("TPEdit.interactivePDF.text"), new javax.swing.ImageIcon(de.netsysit.ui.image.DecoratedImageProducer.produceImage(de.netsysit.util.ResourceLoader.getImgResource("de/netsysit/ressources/gfx/ca/Attribut_48.png"), "PDF", null)));
			shortcuts = new de.netsysit.ui.filechooser.ShortcutsAccessory(interactivePDF.getFilechooser(), new java.io.File(System.getProperty("user.home")), ".TPEdit");
			shortcuts.setPreferredSize(new java.awt.Dimension(200, 250));
			popup = new de.netsysit.ui.filechooser.PopUpAccessory(interactivePDF.getFilechooser(), shortcuts.getToolTipText(), shortcuts);
			popup.setPopUpBorder(javax.swing.BorderFactory.createEtchedBorder());
			interactivePDF.getFilechooser().setAccessory(popup);
			interactivePDF.getFilechooser().setMultiSelectionEnabled(false);
			interactivePDF.getFilechooser().setFileSelectionMode(javax.swing.JFileChooser.FILES_ONLY);
			interactivePDF.getFilechooser().setAcceptAllFileFilterUsed(false);
			interactivePDF.setParent(this);
			interactivePDF.setAllowedSuffixes(new java.lang.String[]{"pdf"});
			interactivePDF.setSaveDialog(true);
			interactivePDF.putValue(javax.swing.Action.SHORT_DESCRIPTION, i18n.getString("TPEdit.interactivePDF.tooltip"));
			interactivePDF.setEnabled(false);
			interactivePDF.setDefaultFileEnding(".pdf");
		} catch (java.lang.Throwable exp)
		{
			exp.printStackTrace();
			saveFullPDF = null;
			previewPDF = null;
			interactivePDF = null;
		}
//		saveFullPDF=null;
//		previewPDF=null;
		de.netsysit.util.pattern.command.FileProcessor openPropertiesClient =
				new de.netsysit.util.pattern.command.FileProcessor()
				{
					public boolean process(java.io.File[] files)
					{
						return opentp(files[0]);
					}
				};
		open = new de.netsysit.util.pattern.command.ChooseFileAction(openPropertiesClient, i18n.getString("TPEdit.open.text"), de.netsysit.util.ResourceLoader.getIcon("toolbarButtonGraphics/general/Open24.gif"));
		shortcuts = new de.netsysit.ui.filechooser.ShortcutsAccessory(open.getFilechooser(), new java.io.File(System.getProperty("user.home")), ".TPEdit");
		shortcuts.setPreferredSize(new java.awt.Dimension(200, 250));
		popup = new de.netsysit.ui.filechooser.PopUpAccessory(open.getFilechooser(), shortcuts.getToolTipText(), shortcuts);
		popup.setPopUpBorder(javax.swing.BorderFactory.createEtchedBorder());
		open.getFilechooser().setAccessory(popup);
		open.getFilechooser().setMultiSelectionEnabled(false);
		open.getFilechooser().setFileSelectionMode(javax.swing.JFileChooser.FILES_ONLY);
		open.getFilechooser().setAcceptAllFileFilterUsed(false);
		open.setParent(this);
		open.setAllowedSuffixes(XMLSUFFIXES);
		open.putValue(javax.swing.Action.ACCELERATOR_KEY, javax.swing.KeyStroke.getKeyStroke(i18n.getString("TPEdit.open.accelerator")));
//		open.putValue(javax.swing.Action.MNEMONIC_KEY,java.lang.Integer.valueOf(javax.swing.KeyStroke.getKeyStroke(i18n.getString("TPEdit.open.mnemonic")).getKeyCode()));
		open.putValue(javax.swing.Action.SHORT_DESCRIPTION, i18n.getString("TPEdit.open.tooltip"));
		de.netsysit.util.pattern.command.FileProcessor importNodesClient =
				new de.netsysit.util.pattern.command.FileProcessor()
				{
					public boolean process(java.io.File[] files)
					{
						return importNodes(files[0]);
					}

				};
		importAction = new de.netsysit.util.pattern.command.ChooseFileAction(importNodesClient, i18n.getString("TPEdit.import.text"), de.netsysit.util.ResourceLoader.getIcon("toolbarButtonGraphics/general/Import24.gif"));
		shortcuts = new de.netsysit.ui.filechooser.ShortcutsAccessory(importAction.getFilechooser(), new java.io.File(System.getProperty("user.home")), ".TPEdit");
		shortcuts.setPreferredSize(new java.awt.Dimension(200, 250));
		popup = new de.netsysit.ui.filechooser.PopUpAccessory(importAction.getFilechooser(), shortcuts.getToolTipText(), shortcuts);
		popup.setPopUpBorder(javax.swing.BorderFactory.createEtchedBorder());
		importAction.getFilechooser().setAccessory(popup);
		importAction.getFilechooser().setMultiSelectionEnabled(false);
		importAction.getFilechooser().setFileSelectionMode(javax.swing.JFileChooser.FILES_ONLY);
		importAction.getFilechooser().setAcceptAllFileFilterUsed(false);
		importAction.setParent(this);
		importAction.setAllowedSuffixes(XMLSUFFIXES);
		importAction.putValue(javax.swing.Action.SHORT_DESCRIPTION, i18n.getString("TPEdit.import.tooltip"));
		importAction.setEnabled(false);
		addaction =
				new javax.swing.AbstractAction(i18n.getString("TPEdit.addaction.text"), de.netsysit.util.ResourceLoader.getIcon("toolbarButtonGraphics/general/Add24.gif"))
				{
					;

					public void actionPerformed(java.awt.event.ActionEvent evt)
					{
						addToTree();
					}
				};
		addaction.putValue(javax.swing.Action.SHORT_DESCRIPTION, i18n.getString("TPEdit.addaction.tooltip"));
		addaction.setEnabled(false);
		editaction =
				new javax.swing.AbstractAction(i18n.getString("TPEdit.editaction.text"), de.netsysit.util.ResourceLoader.getIcon("toolbarButtonGraphics/general/Edit24.gif"))
				{
					public void actionPerformed(java.awt.event.ActionEvent evt)
					{
						edit();
					}
				};
		editaction.putValue(javax.swing.Action.SHORT_DESCRIPTION, i18n.getString("TPEdit.editaction.tooltip"));
		editaction.setEnabled(false);
		editokaction =
				new javax.swing.AbstractAction(i18n.getString("TPEdit.editokaction.text"), de.netsysit.util.ResourceLoader.getIcon("de/netsysit/ressources/gfx/common/Proceed16.gif"))
				{
					public void actionPerformed(java.awt.event.ActionEvent evt)
					{
						proceed();
					}
				};
		editokaction.putValue(javax.swing.Action.SHORT_DESCRIPTION, i18n.getString("TPEdit.editokaction.tooltip"));
		editcancelaction =
				new javax.swing.AbstractAction(i18n.getString("TPEdit.editcancelaction.text"), de.netsysit.util.ResourceLoader.getIcon("de/netsysit/ressources/gfx/common/Cancel16.gif"))
				{
					public void actionPerformed(java.awt.event.ActionEvent evt)
					{
						cancel();
					}
				};
		editcancelaction.putValue(javax.swing.Action.SHORT_DESCRIPTION, i18n.getString("TPEdit.editcancelaction.tooltip"));
		removeaction =
				new javax.swing.AbstractAction(i18n.getString("TPEdit.removeaction.text"), de.netsysit.util.ResourceLoader.getIcon("toolbarButtonGraphics/general/Remove24.gif"))
				{
					;

					public void actionPerformed(java.awt.event.ActionEvent evt)
					{
						removeFromTree();
					}
				};
		removeaction.putValue(javax.swing.Action.SHORT_DESCRIPTION, i18n.getString("TPEdit.removeaction.tooltip"));
		removeaction.setEnabled(false);
		copyaction =
				new javax.swing.AbstractAction(i18n.getString("TPEdit.copyaction.text"), de.netsysit.util.ResourceLoader.getIcon("toolbarButtonGraphics/general/Copy24.gif"))
				{
					;

					public void actionPerformed(java.awt.event.ActionEvent evt)
					{
						javax.swing.tree.TreePath[] tp = tree.getSelectionPaths();
						if (tp != null)
						{
							copied = new Test[tp.length];
							for (int i = 0; i < tp.length; ++i)
							{
								java.lang.Object obj = tp[i].getPath()[tp[i].getPathCount() - 1];
								if (obj instanceof Test)
								{
									copied[i] = (Test) obj;
								}
								else
								{
									copied = null;
									break;
								}
							}
						}
						actionEnable(tp);
					}
				};
		copyaction.putValue(javax.swing.Action.SHORT_DESCRIPTION, i18n.getString("TPEdit.copyaction.tooltip"));
		copyaction.setEnabled(false);
		pasteaction =
				new javax.swing.AbstractAction(i18n.getString("TPEdit.pasteaction.text"), de.netsysit.util.ResourceLoader.getIcon("toolbarButtonGraphics/general/Paste24.gif"))
				{
					;

					public void actionPerformed(java.awt.event.ActionEvent evt)
					{
						pasteIntoTree();
					}
				};
		pasteaction.putValue(javax.swing.Action.SHORT_DESCRIPTION, i18n.getString("TPEdit.pasteaction.tooltip"));
		pasteaction.setEnabled(false);
		insertReferenceToActionsAction =new javax.swing.AbstractAction(i18n.getString("TPEdit.insertReferenceToActionsAction.text"),de.netsysit.util.ResourceLoader.getIcon("de/netsysit/ressources/gfx/ca/Makro expandieren_48.png"))
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				insertReferenceToActions();
			}
		};
		insertReferenceToActionsAction.putValue(javax.swing.Action.SHORT_DESCRIPTION, i18n.getString("TPEdit.insertReferenceToActionsAction.tooltip"));
		insertReferenceToExpectedResultsAction =new javax.swing.AbstractAction(i18n.getString("TPEdit.insertReferenceToExpectedResultsAction.text"),de.netsysit.util.ResourceLoader.getIcon("de/netsysit/ressources/gfx/ca/Makro expandieren_48.png"))
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				insertReferenceToExpectedResults();
			}
		};
		insertReferenceToExpectedResultsAction.putValue(javax.swing.Action.SHORT_DESCRIPTION, i18n.getString("TPEdit.insertReferenceToExpectedResultsAction.tooltip"));
	}
	private void insertReferenceToActions()
	{
		if(nodeChooserPanel==null)
			nodeChooserPanel=new NodeChooserPanel(velocityHelper);
		nodeChooserPanel.setModel(model);
		nodeChooserPanel.setSelectedTreePath(tree.getSelectionPath());
		javax.swing.JOptionPane.showMessageDialog(tree,nodeChooserPanel);
		javax.swing.tree.TreePath tp=nodeChooserPanel.getSelectedTreePath();
		if(tp!=null)
		{
			Test test=(Test)tp.getLastPathComponent();
			java.util.Set<TreeNode> leaves = getAllLeafNodes((TreeNode) model.getRoot());
			java.lang.StringBuffer strbuf=new java.lang.StringBuffer();
			strbuf.append(NOTEDITABLESTART);
			strbuf.append("\n");
			strbuf.append(MACROPREFIX+tp.toString());
			strbuf.append("\n");
			strbuf.append(getActionsAsEditableAndExpandedText(test, leaves));
			strbuf.append("\n");
			strbuf.append(NOTEDITABLEEND);
			strbuf.append("\n");
//			System.out.println(strbuf.toString());
			actionsta.insertTextAtCursor(strbuf.toString());
		}
	}
	private void insertReferenceToExpectedResults()
	{
		if(nodeChooserPanel==null)
			nodeChooserPanel=new NodeChooserPanel(velocityHelper);
		nodeChooserPanel.setModel(model);
		nodeChooserPanel.setSelectedTreePath(tree.getSelectionPath());
		javax.swing.JOptionPane.showMessageDialog(tree,nodeChooserPanel);
		javax.swing.tree.TreePath tp=nodeChooserPanel.getSelectedTreePath();
		if(tp!=null)
		{
			Test test=(Test)tp.getLastPathComponent();
			java.util.Set<TreeNode> leaves = getAllLeafNodes((TreeNode) model.getRoot());
			java.lang.StringBuffer strbuf=new java.lang.StringBuffer();
			strbuf.append(NOTEDITABLESTART);
			strbuf.append("\n");
			strbuf.append(MACROPREFIX+tp.toString());
			strbuf.append("\n");
			strbuf.append(getExpectedResultsAsEditableAndExpandedText(test, leaves));
			strbuf.append("\n");
			strbuf.append(NOTEDITABLEEND);
			strbuf.append("\n");
//			System.out.println(strbuf.toString());
			eresultsta.insertTextAtCursor(strbuf.toString());
		}
	}

	private void makeInteractive(java.io.File formFile, java.io.File resultFile) throws Exception
	{
		try
		{
			java.io.InputStream resource = new java.io.FileInputStream(formFile);
			SearchWordStrategy strategy = new SearchWordStrategy();
			PdfReader reader = new PdfReader(resource);

			PdfReaderContentParser parser = new PdfReaderContentParser(reader);
			for (int page = 1; page <= reader.getNumberOfPages(); page++)
			{
				strategy.setPageNumber(page);
				parser.processContent(page, strategy);
			}
			strategy.close();
			resource.close();
			java.util.List<SearchWordStrategy.TextChunk> chunks = strategy.getLocationalResult();
			AddContentToPDF actp = new AddContentToPDF(qaTeamMemberSignatures.getAsList());
			actp.perform(chunks, formFile, resultFile);
		} catch (java.lang.Throwable t)
		{
			de.elbosso.util.Utilities.handleException(EXCEPTION_LOGGER, this, t, t.getMessage());
		}
	}

	private void proceed()
	{
		javax.swing.tree.TreePath tp = tree.getSelectionPath();
		if (tp != null)
		{
			String oldStringRep=MACROPREFIX+tp.toString();
			java.lang.Object obj = tp.getPath()[tp.getPathCount() - 1];
			if (obj instanceof Category)
			{
				Category toedit = (Category) obj;
				toedit.setName(nametf.getText());
				toedit.setDescription(descta.getText());
				toedit.setJustEdited(true);
			}
			else if (obj instanceof Test)
			{
				Test toedit = (Test) obj;
				toedit.clearActions();
				toedit.clearExpectedResults();
				toedit.clearVariants();
				toedit.setDescription(desctf.getText());
				toedit.setVariants(variantstf.getText());
				if ((requirementidtf.getText() != null) && (requirementidtf.getText().trim().length() > 0))
					toedit.setRequirementId(requirementidtf.getText());
				else
					toedit.setRequirementId(null);
/*				if ((tagstf.getText() != null) && (tagstf.getText().trim().length() > 0))
				{
					toedit.setTags(tagstf.getText());
					tagManager.addTag(tagstf.getText());
				}
				else
					toedit.setTags(null);
*/				toedit.setTags(tagManager.getSelectedTagsAsString());
				toedit.setFromVersionMajor(java.lang.Integer.parseInt(majortf.getText()));
				toedit.setFromVersionMinor(java.lang.Integer.parseInt(minortf.getText()));
				toedit.setJustEdited(true);
				boolean hasseentext = false;
				java.io.StringReader areader = new java.io.StringReader(actionsta.getText());
				java.io.BufferedReader br = new java.io.BufferedReader(areader);
				boolean insideMacroBlock = false;
				try
				{
					java.lang.String line = br.readLine();
					java.lang.StringBuffer abuf = new java.lang.StringBuffer();
					while (line != null)
					{

						if (line.trim().length() == 0)
						{
							if (insideMacroBlock == false)
							{
								if (hasseentext == true)
								{
									abuf.append("\n");
									toedit.addAction(abuf.toString());
									abuf = new java.lang.StringBuffer();
									hasseentext = false;
								}
							}
						}
						else if (line.startsWith(NOTEDITABLESTART))
						{
							if (hasseentext == true)
							{
								abuf.append("\n");
								toedit.addAction(abuf.toString());
								abuf = new java.lang.StringBuffer();
								hasseentext = false;
							}
							insideMacroBlock = true;
						}
						else if (line.startsWith(NOTEDITABLEEND))
						{
							insideMacroBlock = false;
							abuf.append("\n");
							toedit.addAction(abuf.toString());
							abuf = new java.lang.StringBuffer();
							hasseentext = false;
						}
						else if (line.startsWith(MACROPREFIX))
						{
							if (insideMacroBlock == true)
							{
								hasseentext = true;
								abuf.append(line);
								abuf.append("\n");
							}
						}
						else
						{
							if (insideMacroBlock == false)
							{
								hasseentext = true;
								abuf.append(line);
								abuf.append("\n");
							}
						}
						line = br.readLine();
					}
					if (hasseentext == true)
					{
						toedit.addAction(abuf.toString());
					}
				} catch (java.io.IOException exp)
				{
					exp.printStackTrace();
				}
				try
				{
					br.close();
					areader.close();
				} catch (java.io.IOException exp)
				{
					exp.printStackTrace();
				}
				hasseentext = false;
				insideMacroBlock = false;
				areader = new java.io.StringReader(eresultsta.getText());
				br = new java.io.BufferedReader(areader);
				try
				{
					java.lang.String line = br.readLine();
					java.lang.StringBuffer abuf = new java.lang.StringBuffer();
					while (line != null)
					{

						if (line.trim().length() == 0)
						{
							if (insideMacroBlock == false)
							{
								if (hasseentext == true)
								{
									abuf.append("\n");
									toedit.addExpectedResult(abuf.toString());
									abuf = new java.lang.StringBuffer();
									hasseentext = false;
								}
							}
						}
						else if (line.startsWith(NOTEDITABLESTART))
						{
							if (hasseentext == true)
							{
								abuf.append("\n");
								toedit.addExpectedResult(abuf.toString());
								abuf = new java.lang.StringBuffer();
								hasseentext = false;
							}
							insideMacroBlock = true;
						}
						else if (line.startsWith(NOTEDITABLEEND))
						{
							insideMacroBlock = false;
							abuf.append("\n");
							toedit.addExpectedResult(abuf.toString());
							abuf = new java.lang.StringBuffer();
							hasseentext = false;
						}
						else if (line.startsWith(MACROPREFIX))
						{
							if (insideMacroBlock == true)
							{
								hasseentext = true;
								abuf.append(line);
								abuf.append("\n");
							}
						}
						else
						{
							if (insideMacroBlock == false)
							{
								hasseentext = true;
								abuf.append(line);
								abuf.append("\n");
							}
						}
						line = br.readLine();
					}
					if (hasseentext == true)
					{
						toedit.addExpectedResult(abuf.toString());
					}
				} catch (java.io.IOException exp)
				{
					exp.printStackTrace();
				}
				try
				{
					br.close();
					areader.close();
				} catch (java.io.IOException exp)
				{
					exp.printStackTrace();
				}
			}
			String newStringRep=MACROPREFIX+tree.getSelectionPath().toString();
			if(newStringRep.equals(oldStringRep)==false)
			{
				java.util.Set<TreeNode> leaves = getAllLeafNodes((TreeNode) model.getRoot());
				for (TreeNode treeNode : leaves)
				{
					Test test = (Test) treeNode;
					test.updateActionMacros(oldStringRep, newStringRep);
					test.updateResultMacros(oldStringRep, newStringRep);
				}
			}
		}
		cancel();
	}

	private void cancel()
	{
		javax.swing.tree.TreePath tp = tree.getSelectionPath();
		if (tp != null)
		{
			java.lang.Object obj = tp.getPath()[tp.getPathCount() - 1];
			if (obj instanceof Category)
			{
				Category toedit = (Category) obj;
				((javax.swing.tree.DefaultTreeModel) tree.getModel()).nodeChanged(toedit);
				tree.getSelectionModel().clearSelection();
				tree.getSelectionModel().setSelectionPath(tp);
			}
			else if (obj instanceof Test)
			{
				Test toedit = (Test) obj;
				((javax.swing.tree.DefaultTreeModel) tree.getModel()).nodeChanged(toedit);
				tree.getSelectionModel().clearSelection();
				tree.getSelectionModel().setSelectionPath(tp);
			}
		}
		actionEnable(tree.getSelectionPaths());
		open.setEnabled(true);
		saveAs.setEnabled(true);
		if (saveFullPDF != null)
			saveFullPDF.setEnabled(true);
		if (previewPDF != null)
			previewPDF.setEnabled(true);
		if (interactivePDF != null)
			interactivePDF.setEnabled(true);
		save.setEnabled(saveAs.getFilechooser().getSelectedFile() != null);
		bottomComponent.removeAll();
		bottomComponent.add(lbl);
		tree.setEnabled(true);
		getContentPane().invalidate();
		getContentPane().doLayout();
		getContentPane().repaint();
	}

	private void setupCategoryEditing(Category toedit)
	{
		if (ceditor == null)
		{
			if (nametf == null)
			{
				nametf = new javax.swing.JTextField();
				nametf.setBorder(javax.swing.BorderFactory.createEtchedBorder());
			}
			if (descta == null)
			{
				descta = new de.netsysit.ui.text.EditorTab(null, null);
				//			descta.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(),i18n.getString("TPEdit.description.label")));
			}
			ceditor = new javax.swing.JPanel(new java.awt.BorderLayout());
			javax.swing.JPanel e = new javax.swing.JPanel(new java.awt.BorderLayout());
			javax.swing.JPanel f = new javax.swing.JPanel(new java.awt.BorderLayout());
			f.add(nametf);
			f.add(new javax.swing.JLabel(i18n.getString("TPEdit.name.label")), java.awt.BorderLayout.WEST);
			e.add(f, java.awt.BorderLayout.NORTH);
			//javax.swing.JScrollPane tascroller=new javax.swing.JScrollPane(descta);
			//e.add(tascroller);
			e.add(descta.getJComponent());
			//tascroller.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(),i18n.getString("TPEdit.description.label")));
			descta.getJComponent().setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), i18n.getString("TPEdit.description.label")));
			e.add(new javax.swing.JSeparator(), java.awt.BorderLayout.SOUTH);
			ceditor.add(e);
			ceditor.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createEmptyBorder(2, 2, 2, 2), new de.netsysit.ui.components.ShadowBorder(4)));
		}
		ceditor.add(editendactions, java.awt.BorderLayout.SOUTH);
		nametf.setText(toedit.getName());
		descta.setText(toedit.getDescription());
		bottomComponent.removeAll();
		bottomComponent.add(ceditor);
		tree.setEnabled(false);
		clearActionEnable();
		open.setEnabled(false);
		saveAs.setEnabled(false);
		save.setEnabled(false);
		if (saveFullPDF != null)
			saveFullPDF.setEnabled(false);
		if (previewPDF != null)
			previewPDF.setEnabled(false);
		if (interactivePDF != null)
			interactivePDF.setEnabled(false);
		bottomComponent.invalidate();
		bottomComponent.validate();
		bottomComponent.doLayout();
		bottomComponent.repaint();
	}

	private void setupTestEditing(Test toedit)
	{
		if (teditor == null)
		{
			if (variantstf == null)
			{
				variantstf = new javax.swing.JTextField();
				variantstf.setBorder(javax.swing.BorderFactory.createEtchedBorder());
			}
			if (requirementidtf == null)
			{
				requirementidtf = new javax.swing.JTextField();
				requirementidtf.setBorder(javax.swing.BorderFactory.createEtchedBorder());
			}
/*			if (tagstf == null)
			{
				tagstf = new javax.swing.JTextField();
				tagstf.setBorder(javax.swing.BorderFactory.createEtchedBorder());
			}
*/			if (minortf == null)
			{
				minortf = new javax.swing.JTextField();
				minortf.setBorder(javax.swing.BorderFactory.createEtchedBorder());
			}
			if (majortf == null)
			{
				majortf = new javax.swing.JTextField();
				majortf.setBorder(javax.swing.BorderFactory.createEtchedBorder());
			}
			if (desctf == null)
			{
				desctf = new javax.swing.JTextField();
				desctf.setBorder(javax.swing.BorderFactory.createEtchedBorder());
			}
			if (actionsta == null)
			{
				java.util.Map<java.lang.Integer,java.lang.Object[]> customActions=new java.util.HashMap();
				customActions.put(java.lang.Integer.valueOf(13),new java.lang.Object[]{insertReferenceToActionsAction});
				actionsta = new de.netsysit.ui.text.EditorTab(null, null, NOTEDITABLESTART, NOTEDITABLEEND, false,customActions);
				//			actionsta.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(),i18n.getString("TPEdit.description.label")));
			}
			if (eresultsta == null)
			{
				java.util.Map<java.lang.Integer,java.lang.Object[]> customActions=new java.util.HashMap();
				customActions.put(java.lang.Integer.valueOf(13),new java.lang.Object[]{insertReferenceToExpectedResultsAction});
				eresultsta = new de.netsysit.ui.text.EditorTab(null, null, NOTEDITABLESTART, NOTEDITABLEEND, false,customActions);
				//			eresultsta.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(),i18n.getString("TPEdit.description.label")));
			}
			teditor = new javax.swing.JPanel(new java.awt.BorderLayout());
			javax.swing.JPanel g = new javax.swing.JPanel(new java.awt.BorderLayout());
			javax.swing.JPanel h = new javax.swing.JPanel(new java.awt.BorderLayout());
			javax.swing.JPanel left = new javax.swing.JPanel(new java.awt.GridLayout(0, 1));
			javax.swing.JPanel right = new javax.swing.JPanel(new java.awt.GridLayout(0, 1));
			left.add(new javax.swing.JLabel(i18n.getString("TPEdit.description.label")));
			left.add(new javax.swing.JLabel(i18n.getString("TPEdit.requirementsid.label")));
			left.add(new javax.swing.JLabel(i18n.getString("TPEdit.variants.label")));
			left.add(new javax.swing.JLabel(i18n.getString("TPEdit.major.label")));
			left.add(new javax.swing.JLabel(i18n.getString("TPEdit.minor.label")));
			left.add(new javax.swing.JLabel(i18n.getString("TPEdit.tags.label")));
			right.add(desctf);
			right.add(requirementidtf);
			right.add(variantstf);
			right.add(majortf);
			right.add(minortf);
			javax.swing.AbstractAction tagEditAction=new javax.swing.AbstractAction("tags")
			{
				@Override
				public void actionPerformed(ActionEvent e)
				{
					if(tagsDlg==null)
					{
						tagsDlg = de.netsysit.ui.dialog.GeneralPurposeInfoDialog.create(tree, "tags");
						tagsDlgPanel=new javax.swing.JPanel(new java.awt.BorderLayout());
						tagsDlgPanel.add(tagManager.getAllTagsPanel());
						tagsDlgPanel.add(tagManager.getTagTexField(), BorderLayout.SOUTH);
						tagsDlgPanel.setPreferredSize(new java.awt.Dimension(600,600));
					}
					tagsDlg.showDialog(tagsDlgPanel);
				}
			};
			javax.swing.JPanel tagsPanel=new javax.swing.JPanel(new java.awt.BorderLayout());
			tagsPanel.add(new javax.swing.JButton(tagEditAction), BorderLayout.EAST);
			javax.swing.JScrollPane scroller=new javax.swing.JScrollPane(tagManager.getSelectedTagsPanel());
			tagsPanel.add(scroller);
			scroller.setPreferredSize(new java.awt.Dimension(tagManager.getSelectedTagsPanel().getPreferredSize().width,64));
//			right.add(tagsPanel);
			g.add(left, java.awt.BorderLayout.WEST);
			g.add(right);
			h.add(g, java.awt.BorderLayout.NORTH);
			javax.swing.JPanel tp=new javax.swing.JPanel(new java.awt.BorderLayout());
			javax.swing.JPanel tapanel = new javax.swing.JPanel(new java.awt.GridLayout(0, 1));
			tp.add(tapanel);
			tp.add(tagsPanel, BorderLayout.NORTH);
//			javax.swing.JScrollPane atascroller=new javax.swing.JScrollPane(actionsta);
//			tapanel.add(atascroller);
			tagsPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), i18n.getString("TPEdit.tags.label")));
			tapanel.add(actionsta.getJComponent());
//			atascroller.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(),i18n.getString("TPEdit.actions.label")));
			actionsta.getJComponent().setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), i18n.getString("TPEdit.actions.label")));
//			javax.swing.JScrollPane rtascroller=new javax.swing.JScrollPane(eresultsta);
//			tapanel.add(rtascroller);
			tapanel.add(eresultsta.getJComponent());
//			rtascroller.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(),i18n.getString("TPEdit.eresults.label")));
			eresultsta.getJComponent().setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "TPEdit.eresults.label"));
			if (CLASS_LOGGER.isTraceEnabled())
				CLASS_LOGGER.trace(actionsta.getJComponent() + "\n" + eresultsta.getJComponent());
			h.add(tp);
			h.add(new javax.swing.JSeparator(), java.awt.BorderLayout.SOUTH);
			teditor.add(h);
			teditor.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createEmptyBorder(2, 2, 2, 2), new de.netsysit.ui.components.ShadowBorder(4)));
		}
		teditor.add(editendactions, java.awt.BorderLayout.SOUTH);
		desctf.setText(toedit.getDescription());
		variantstf.setText(toedit.getVariantsAsOneString());
		if (toedit.getRequirementId() != null)
			requirementidtf.setText(toedit.getRequirementId());
		else
			requirementidtf.setText("");
		if (toedit.getRequirementId() != null)
			tagManager.setSelectedTagsAsString(toedit.getTags());
		else
			tagManager.setSelectedTagsAsString(null);
		majortf.setText(java.lang.Integer.toString(toedit.getFromVersionMajor()));
		minortf.setText(java.lang.Integer.toString(toedit.getFromVersionMinor()));
		java.util.Set<TreeNode> leaves = getAllLeafNodes((TreeNode) model.getRoot());
		actionsta.setText(getActionsAsEditableAndExpandedText(toedit, leaves));
		eresultsta.setText(getExpectedResultsAsEditableAndExpandedText(toedit, leaves));
		bottomComponent.removeAll();
		bottomComponent.add(teditor);
		tree.setEnabled(false);
		clearActionEnable();
		open.setEnabled(false);
		saveAs.setEnabled(false);
		save.setEnabled(false);
		if (saveFullPDF != null)
			saveFullPDF.setEnabled(false);
		if (previewPDF != null)
			previewPDF.setEnabled(false);
		if (interactivePDF != null)
			interactivePDF.setEnabled(false);
		bottomComponent.invalidate();
		bottomComponent.validate();
		bottomComponent.doLayout();
		bottomComponent.repaint();
	}

	private java.lang.String getActionsAsEditableAndExpandedText(de.elbosso.tools.tpedit.Test test, java.util.Set<TreeNode> leaves)
	{
		return getActionsAsEditableAndExpandedText(test,leaves,true);
	}
	private java.lang.String getActionsAsEditableAndExpandedText(de.elbosso.tools.tpedit.Test test, java.util.Set<TreeNode> leaves, boolean outermost)
	{
		java.lang.StringBuffer abuf = new java.lang.StringBuffer();
		for (java.util.Iterator iter = test.getActions().iterator(); iter.hasNext(); )
		{
			java.lang.String line = iter.next().toString();
			if(outermost==true)
			{
				if (line.startsWith(MACROPREFIX))
				{
					abuf.append(NOTEDITABLESTART);
					abuf.append('\n');
				}
				abuf.append(line);
				if (line.startsWith(MACROPREFIX))
				{
					abuf.append("\n\n");
				}
			}
			else
			{
				if(line.startsWith(MACROPREFIX)==false)
					abuf.append(line);
			}
			if (line.startsWith(MACROPREFIX))
			{
				boolean found=false;
				java.lang.String stringrep = line.trim().substring(MACROPREFIX.length());
				for (TreeNode node : leaves)
				{
					javax.swing.tree.TreePath path = new javax.swing.tree.TreePath(model.getPathToRoot(node));
//				System.out.println(stringrep+" "+(path.toString()));
					if (path.toString().equals(stringrep))
					{
//						System.out.println("+" + stringrep);
						de.elbosso.tools.tpedit.Test testToInclude = (de.elbosso.tools.tpedit.Test) node;
						abuf.append(getActionsAsEditableAndExpandedText(testToInclude, leaves,false));
						abuf.append("\n");
						found=true;
						break;
					}
				}
				if(found==false)
				{
					abuf.append("Macro not found!\n");
				}
			}
			if ((line.startsWith(MACROPREFIX))&&(outermost==true))
			{
				abuf.append(NOTEDITABLEEND);
				abuf.append('\n');
			}
//			abuf.append("\n\n");
		}
		return abuf.toString();
	}

	private java.lang.String getExpectedResultsAsEditableAndExpandedText(de.elbosso.tools.tpedit.Test test, java.util.Set<TreeNode> leaves)
	{
		return getExpectedResultsAsEditableAndExpandedText(test,leaves,true);
	}
	private java.lang.String getExpectedResultsAsEditableAndExpandedText(de.elbosso.tools.tpedit.Test test, java.util.Set<TreeNode> leaves, boolean outermost)
	{
		java.lang.StringBuffer abuf = new java.lang.StringBuffer();
		for (java.util.Iterator iter = test.getExpectedResults().iterator(); iter.hasNext(); )
		{
			java.lang.String line = iter.next().toString();
			if(outermost==true)
			{
				if (line.startsWith(MACROPREFIX))
				{
					abuf.append(NOTEDITABLESTART);
					abuf.append('\n');
				}
				abuf.append(line);
				if (line.startsWith(MACROPREFIX))
				{
					abuf.append("\n\n");
				}
			}
			else
			{
				if(line.startsWith(MACROPREFIX)==false)
					abuf.append(line);
			}
			if (line.startsWith(MACROPREFIX))
			{
				boolean found=false;
				java.lang.String stringrep = line.trim().substring(MACROPREFIX.length());
				for (TreeNode node : leaves)
				{
					javax.swing.tree.TreePath path = new javax.swing.tree.TreePath(model.getPathToRoot(node));
//				System.out.println(stringrep+" "+(path.toString()));
					if (path.toString().equals(stringrep))
					{
						System.out.println("+" + stringrep);
						de.elbosso.tools.tpedit.Test testToInclude = (de.elbosso.tools.tpedit.Test) node;
						abuf.append(getExpectedResultsAsEditableAndExpandedText(testToInclude, leaves,false));
						abuf.append("\n");
						found=true;
						break;
					}
				}
				if(found==false)
				{
					abuf.append("Macro not found!\n");
				}
			}
			if ((line.startsWith(MACROPREFIX))&&(outermost==true))
			{
				abuf.append(NOTEDITABLEEND);
				abuf.append('\n');
			}
//			abuf.append("\n\n");
		}
		return abuf.toString();
	}

	public java.util.Set<TreeNode> getAllLeafNodes(TreeNode node)
	{
//		System.out.println("analyzing " + node + " " + node.isLeaf() + " " + node.getChildCount());
		java.util.Set<TreeNode> leafNodes = new java.util.HashSet<TreeNode>();

		if (node.isLeaf())
		{
			leafNodes.add(node);
		}
		else
		{
			for (int i = 0; i < node.getChildCount(); ++i)
			{
				leafNodes.addAll(getAllLeafNodes(node.getChildAt(i)));
			}
		}
		return leafNodes;
	}

	private void edit()
	{
		javax.swing.tree.TreePath tp = tree.getSelectionPath();
		if (tp != null)
		{
			java.lang.Object obj = tp.getPath()[tp.getPathCount() - 1];
			if (obj instanceof Category)
			{
				Category toedit = (Category) obj;
				setupCategoryEditing(toedit);
			}
			else if (obj instanceof Test)
			{
				Test toedit = (Test) obj;
				setupTestEditing(toedit);
			}
		}
	}

	private void addToTree()
	{
		javax.swing.tree.TreePath tp = tree.getSelectionPath();
		if (tp != null)
		{
			java.lang.Object obj = tree.getSelectionPath().getPath()[tree.getSelectionPath().getPathCount() - 1];
			if (obj instanceof ProtocolNode)
			{
				java.lang.String entered = javax.swing.JOptionPane.showInputDialog(tree, i18n.getString("TPEdit.newcategorydialog.text"), i18n.getString("TPEdit.newcategorydialog.heading"), javax.swing.JOptionPane.QUESTION_MESSAGE);
				if (entered != null)
				{
					Category toinsert = new Category();
					toinsert.setName(entered);
					toinsert.setDescription("");
					toinsert.setJustAdded(true);
					((ProtocolNode) obj).insert(toinsert, ((ProtocolNode) obj).getChildCount());
					((javax.swing.tree.DefaultTreeModel) tree.getModel()).nodesWereInserted((ProtocolNode) obj, new int[]{((ProtocolNode) obj).getChildCount() - 1});
					tree.getSelectionModel().clearSelection();
					tree.getSelectionModel().setSelectionPath(tp);
					saveAs.setEnabled(true);
					save.setEnabled(saveAs.getFilechooser().getSelectedFile() != null);
				}
			}
			else if (obj instanceof Category)
			{
				java.lang.String entered = javax.swing.JOptionPane.showInputDialog(tree, i18n.getString("TPEdit.newtestcasedialog.text"), i18n.getString("TPEdit.newtestcasedialog.heading"), javax.swing.JOptionPane.QUESTION_MESSAGE);
				if (entered != null)
				{
					Test toinsert = new Test();
					toinsert.setID(((Category) obj).getChildCount() + 1);
					toinsert.setDescription(entered);
					toinsert.setVariants("");
					toinsert.setJustAdded(true);
					((Category) obj).insert(toinsert, ((Category) obj).getChildCount());
					((javax.swing.tree.DefaultTreeModel) tree.getModel()).nodesWereInserted((Category) obj, new int[]{((Category) obj).getChildCount() - 1});
					tree.getSelectionModel().clearSelection();
					tree.setSelectionPath(new javax.swing.tree.TreePath(((javax.swing.tree.DefaultTreeModel) tree.getModel()).getPathToRoot(toinsert)));
					saveAs.setEnabled(true);
					save.setEnabled(saveAs.getFilechooser().getSelectedFile() != null);
					edit();
				}
			}
			else if (obj instanceof Test)
			{
//				java.lang.String entered=javax.swing.JOptionPane.showInputDialog(tree,i18n.getString("TPEdit.newtestcasedialog.text"),i18n.getString("TPEdit.newtestcasedialog.heading"),javax.swing.JOptionPane.QUESTION_MESSAGE);
//				if(entered!=null)
				{
					Test toinsert = new Test();
					toinsert.setID(((Test) obj).getParent().getChildCount() + 1);
					toinsert.setDescription(((Test) obj).getDescription());
					toinsert.setVariants(((Test) obj).getVariantsAsOneString());
					for (java.lang.Object ref : ((Test) obj).getActions())
					{
						toinsert.addAction(ref.toString());
					}
					for (java.lang.Object ref : ((Test) obj).getExpectedResults())
					{
						toinsert.addExpectedResult(ref.toString());
					}
					toinsert.setJustAdded(true);
					((Category) ((Test) obj).getParent()).insert(toinsert, ((Test) obj).getParent().getChildCount());
					((javax.swing.tree.DefaultTreeModel) tree.getModel()).nodesWereInserted(((Test) obj).getParent(), new int[]{(((Test) obj).getParent()).getChildCount() - 1});
					tree.getSelectionModel().clearSelection();
					tree.setSelectionPath(new javax.swing.tree.TreePath(((javax.swing.tree.DefaultTreeModel) tree.getModel()).getPathToRoot(toinsert)));
					saveAs.setEnabled(true);
					save.setEnabled(saveAs.getFilechooser().getSelectedFile() != null);
					edit();
				}
			}
		}
	}

	private void removeFromTree()
	{
		javax.swing.tree.TreePath[] tp = tree.getSelectionPaths();
		if (tp != null)
		{
			java.util.Set<TreeNode> leaves = getAllLeafNodes((TreeNode) model.getRoot());
			for (int i = 0; i < tp.length; ++i)
			{
				java.lang.Object obj = tp[i].getPath()[tp[i].getPathCount() - 1];
				if ((obj instanceof Category) || (obj instanceof Test))
				{
					if (javax.swing.JOptionPane.showConfirmDialog(tree, i18n.getString("TPEdit.removedialog.text"), i18n.getString("TPEdit.removedialog.heading"), javax.swing.JOptionPane.WARNING_MESSAGE) == javax.swing.JOptionPane.OK_OPTION)
					{
						java.lang.String stringRep=MACROPREFIX+tp[i].toString();
						javax.swing.tree.MutableTreeNode mtn = (javax.swing.tree.MutableTreeNode) obj;
						javax.swing.tree.MutableTreeNode oldpar = (javax.swing.tree.MutableTreeNode) mtn.getParent();
						int oldindex = oldpar.getIndex(mtn);
						oldpar.remove(mtn);
						((javax.swing.tree.DefaultTreeModel) tree.getModel()).nodesWereRemoved(oldpar, new int[]{oldindex}, new java.lang.Object[]{mtn});
						saveAs.setEnabled(true);
						if (saveFullPDF != null)
							saveFullPDF.setEnabled(true);
						if (previewPDF != null)
							previewPDF.setEnabled(true);
						if (interactivePDF != null)
							interactivePDF.setEnabled(true);
						save.setEnabled(saveAs.getFilechooser().getSelectedFile() != null);

						for (TreeNode treeNode : leaves)
						{
							Test test = (Test) treeNode;
							test.deleteActionMacros(stringRep);
							test.deleteResultMacros(stringRep);
						}

					}
				}
			}
		}
	}

	private void pasteIntoTree()
	{
		javax.swing.tree.TreePath[] tp = tree.getSelectionPaths();
		if (((tp != null) && (tp.length == 1)) && (copied != null))
		{
			try
			{
				for (int i = 0; i < copied.length; ++i)
				{
					Test copy = (Test) copied[i].clone();
					copy.setJustAdded(true);
					java.lang.Object obj = tree.getSelectionPath().getPath()[tree.getSelectionPath().getPathCount() - 1];
					if (obj instanceof Category)
					{
						javax.swing.tree.MutableTreeNode oldpar = (javax.swing.tree.MutableTreeNode) obj;
						int newindex = 0;
						oldpar.insert(copy, newindex);
						((javax.swing.tree.DefaultTreeModel) tree.getModel()).nodesWereInserted(oldpar, new int[]{newindex});
					}
					else if (obj instanceof Test)
					{
						javax.swing.tree.MutableTreeNode mtn = (javax.swing.tree.MutableTreeNode) obj;
						javax.swing.tree.MutableTreeNode oldpar = (javax.swing.tree.MutableTreeNode) mtn.getParent();
						int oldindex = oldpar.getIndex(mtn);
						int newindex = oldindex + 1;
						oldpar.insert(copy, newindex);
						((javax.swing.tree.DefaultTreeModel) tree.getModel()).nodesWereInserted(oldpar, new int[]{newindex});
					}
					saveAs.setEnabled(true);
					if (saveFullPDF != null)
						saveFullPDF.setEnabled(true);
					if (previewPDF != null)
						previewPDF.setEnabled(true);
					if (interactivePDF != null)
						interactivePDF.setEnabled(true);
					save.setEnabled(saveAs.getFilechooser().getSelectedFile() != null);
					importAction.setEnabled(true);
				}
			} catch (java.lang.CloneNotSupportedException exp)
			{
			}
		}
	}

	private boolean opentp(java.io.File file)
	{
		boolean rv = false;
		ImportHandler handler = new ImportHandler(file, tagManager);

		// Use the default (non-validating) parser
		javax.xml.parsers.SAXParserFactory factory = javax.xml.parsers.SAXParserFactory.newInstance();
		try
		{
			// Parse the input
			javax.xml.parsers.SAXParser saxParser = factory.newSAXParser();
			try
			{
				saxParser.parse(new java.util.zip.GZIPInputStream(new java.io.FileInputStream(file)), handler);
			} catch (java.io.IOException exp)
			{
				saxParser.parse(file, handler);
			}
			saveAs.setEnabled(false);
			save.setEnabled(false);
			if (saveFullPDF != null)
				saveFullPDF.setEnabled(true);
			if (previewPDF != null)
				previewPDF.setEnabled(true);
			if (interactivePDF != null)
				interactivePDF.setEnabled(true);
			collapseTreeAction.setEnabled(true);
			expandTreeAction.setEnabled(true);
			appname = handler.getAppname();
			protocolNode = new ProtocolNode(handler.getCategories(), appname);
			qaTeamMemberSignatures.clear();
			qaTeamMemberSignatures.addAll(handler.getQaTeamMemberSignatures());
			tree.removeTreeSelectionListener(this);
			model = new javax.swing.tree.DefaultTreeModel(protocolNode);
/*			de.elbosso.model.tree.CloneableMutableTreeNode alternativeRoot=de.elbosso.model.tree.Utilities.deepClone(protocolNode);
			javax.swing.tree.DefaultTreeModel alternativeModel=new javax.swing.tree.DefaultTreeModel(alternativeRoot);
			javax.swing.JTree alternativeTree=new javax.swing.JTree(alternativeModel);
			javax.swing.JOptionPane.showMessageDialog(tree,new javax.swing.JScrollPane(alternativeTree));
*/			tree.setModel(model);
			tree.getSelectionModel().setSelectionMode(javax.swing.tree.TreeSelectionModel.DISCONTIGUOUS_TREE_SELECTION);
			tree.addTreeSelectionListener(this);
			tree.addMouseListener(this);
			lbl.setText("");
			if (saveAs.getFilechooser().getSelectedFile() == null)
				saveAs.getFilechooser().setSelectedFile(file);
			importAction.setEnabled(true);
		} catch (Throwable t)
		{
			de.elbosso.util.Utilities.handleException(EXCEPTION_LOGGER,tree,t);
		}
		return rv;
	}

	private boolean importNodes(File file)
	{
		boolean rv = false;
		ImportHandler handler = new ImportHandler(file, tagManager);

		// Use the default (non-validating) parser
		javax.xml.parsers.SAXParserFactory factory = javax.xml.parsers.SAXParserFactory.newInstance();
		try
		{
			// Parse the input
			javax.xml.parsers.SAXParser saxParser = factory.newSAXParser();
			try
			{
				saxParser.parse(new java.util.zip.GZIPInputStream(new java.io.FileInputStream(file)), handler);
			} catch (java.io.IOException exp)
			{
				saxParser.parse(file, handler);
			}
			saveAs.setEnabled(true);
			if (saveFullPDF != null)
				saveFullPDF.setEnabled(true);
			if (previewPDF != null)
				previewPDF.setEnabled(true);
			if (interactivePDF != null)
				interactivePDF.setEnabled(true);
			save.setEnabled(saveAs.getFilechooser().getSelectedFile() != null);
			collapseTreeAction.setEnabled(true);
			expandTreeAction.setEnabled(true);
			tree.getSelectionModel().clearSelection();
			java.util.List cl = protocolNode.getCategories();
			java.util.Map catlatch = new java.util.HashMap();
			for (Object object : cl)
			{
				Category cc = (Category) object;
				catlatch.put(cc.getName(), cc);
			}
			java.util.List<java.lang.String> t = handler.getQaTeamMemberSignatures();
			qaTeamMemberSignatures.addAll(t);
			java.util.List hc = handler.getCategories();
			for (Object object : hc)
			{
				Category thc = (Category) object;
				Category toadd = (Category) catlatch.get(thc.getName());
				if (toadd == null)
				{
					java.util.List tcl = thc.getTests();
					for (Object object1 : tcl)
					{
						((Test) object1).setID(0);
						((Test) object1).setJustAdded(true);
					}
					thc.setJustAdded(true);
					protocolNode.insert(thc, protocolNode.getChildCount());
					((javax.swing.tree.DefaultTreeModel) tree.getModel()).nodesWereInserted(protocolNode, new int[]{protocolNode.getChildCount() - 1});
				}
				else
				{
					java.util.List tcl = thc.getTests();
					for (Object object1 : tcl)
					{
						Test copy = (Test) ((Test) object1).clone();
						copy.setID(0);
						copy.setJustAdded(true);
						toadd.addTest(copy);
					}
				}
			}
			tree.repaint();
		} catch (Throwable t)
		{
			t.printStackTrace();
		}
		return rv;
	}

	private void newtp()
	{
		java.lang.String newname = "";
		while ((newname != null) && (newname.trim().length() < 1))
		{
			newname = javax.swing.JOptionPane.showInputDialog(TPEdit.this, i18n.getString("TPEdit.newdialog.title"));
			if (newname != null)
			{
				if (newname.trim().length() > 0)
				{
					appname = newname;
					protocolNode = new ProtocolNode(new java.util.LinkedList(), appname);
					collapseTreeAction.setEnabled(true);
					expandTreeAction.setEnabled(true);
					tree.removeTreeSelectionListener(this);
					model = new javax.swing.tree.DefaultTreeModel(protocolNode);
					tree.setModel(model);
					tree.getSelectionModel().setSelectionMode(javax.swing.tree.TreeSelectionModel.DISCONTIGUOUS_TREE_SELECTION);
					tree.addTreeSelectionListener(this);
					tree.addMouseListener(this);
					lbl.setText("");
					saveAs.setEnabled(true);
					save.setEnabled(false);
					if (saveFullPDF != null)
						saveFullPDF.setEnabled(true);
					if (previewPDF != null)
						previewPDF.setEnabled(true);
					if (interactivePDF != null)
						interactivePDF.setEnabled(true);
					if (saveAs.getFilechooser().getSelectedFile() == null)
						saveAs.getFilechooser().setSelectedFile(new java.io.File(newname));
				}
				else
				{
					javax.swing.JOptionPane.showMessageDialog(TPEdit.this, i18n.getString("TPEdit.newtp.errordlg.msg"), i18n.getString("TPEdit.newtp.errordlg.title"), javax.swing.JOptionPane.ERROR_MESSAGE);
				}
			}
		}
	}

	private boolean savetp(java.io.File file)
	{
		boolean rv = false;
		savetpImpl(file,false);
		return rv;
	}

	private void savetpImpl(java.io.File file,boolean expandMacros)
	{
		try
		{
			SaxExporter saxer = new SaxExporter(tagManager, protocolNode.getCategories(), appname, qaTeamMemberSignatures.getAsList(),expandMacros?velocityHelper:null);
			java.io.InputStreamReader fr = new java.io.InputStreamReader(this.getClass().getClassLoader().getResourceAsStream("de/elbosso/tools/tpedit/test_protocol.xsd"));
			java.io.BufferedReader br = new java.io.BufferedReader(fr);
			org.xml.sax.InputSource inputSource = new org.xml.sax.InputSource(br);
			javax.xml.transform.sax.SAXSource source = new javax.xml.transform.sax.SAXSource(saxer, inputSource);
			javax.xml.transform.TransformerFactory factory = javax.xml.transform.TransformerFactory.newInstance();
			javax.xml.transform.Transformer transformer = factory.newTransformer();
			//transformer.setOutputProperty(javax.xml.transform.OutputKeys.OMIT_XML_DECLARATION, "yes");
			transformer.setOutputProperty(javax.xml.transform.OutputKeys.INDENT, "yes");
			java.io.OutputStream pos = new java.io.FileOutputStream(file);
			java.io.OutputStream os = pos;
//			if(compress==true)
//				os=new java.util.zip.GZIPOutputStream(pos);
			javax.xml.transform.stream.StreamResult result = new javax.xml.transform.stream.StreamResult(os);
			transformer.transform(source, result);
			os.close();
			if (pos != os)
				pos.close();
			br.close();
			fr.close();
			java.io.InputStream wsris = this.getClass().getClassLoader().getResourceAsStream("de/elbosso/tools/tpedit/test_protocol.xsd");
			byte[] buf = new byte[65536];
			java.io.OutputStream wsros = new java.io.FileOutputStream(file.getParentFile().getCanonicalPath() + java.io.File.separator + "test_protocol.xsd");
			int bytesread = wsris.read(buf);
			while (bytesread > -1)
			{
				wsros.write(buf, 0, bytesread);
				bytesread = wsris.read(buf);
			}
			wsros.close();
			wsris.close();
		} catch (Exception exp)
		{
			de.elbosso.util.Utilities.handleException(EXCEPTION_LOGGER, TPEdit.this, exp, exp.getMessage());
		}

	}

	private boolean generatePDF(final java.io.File file)
	{
		boolean rv = false;
		try
		{
			java.io.File source = java.io.File.createTempFile(this.getClass().getName(), ".xml");
			source.deleteOnExit();
//			System.out.println(source);
			savetpImpl(source,true);
			java.util.Map<java.lang.String, java.lang.Object> parameters = new java.util.HashMap();
			if (tagManager.getListModel().getSize() > 0)
			{
//				de.netsysit.ui.dialog.OneFromManyListDialog dlg = de.netsysit.ui.dialog.OneFromManyListDialog.create(TPEdit.this, "Tag");
//				int index = dlg.showDialog(tagManager);
//				parameters.put("tag", index == -1 ? "!!" : tagManager.getElementAt(index).toString());
				parameters.put("tag",  "!!" );
			}
			transformProcessor.perform(source, file, parameters);
		} catch (Exception exp)
		{
			de.elbosso.util.Utilities.handleException(EXCEPTION_LOGGER, TPEdit.this, exp, exp.getMessage());
		}
		return rv;
	}

	private boolean exportNodes(File file)
	{
		boolean rv = false;
		try
		{
			java.util.Map catlatch = new java.util.HashMap();
			java.util.List selcat = new java.util.LinkedList();
			javax.swing.tree.TreePath[] tp = tree.getSelectionPaths();
			if (tp != null)
			{
				for (int i = 0; i < tp.length; ++i)
				{
					java.lang.Object obj = tp[i].getPath()[tp[i].getPathCount() - 1];
					if (obj instanceof Category)
					{
						Category dataholder = (Category) catlatch.get(obj);
						if (dataholder == null)
						{
							dataholder = new Category();
							dataholder.setDescription(((Category) obj).getDescription());
							dataholder.setName(((Category) obj).getName());
							catlatch.put(obj, dataholder);
							selcat.add(dataholder);
						}
						java.util.List tcl = ((Category) obj).getTests();
						for (Object object : tcl)
						{
							Test copy = (Test) ((Test) object).clone();
							dataholder.addTest(copy);
						}
					}
					else if (obj instanceof Test)
					{
						Category dataholder = (Category) catlatch.get(((Test) obj).getParent());
						if (dataholder == null)
						{
							dataholder = new Category();
							dataholder.setDescription(((Category) ((Test) obj).getParent()).getDescription());
							dataholder.setName(((Category) ((Test) obj).getParent()).getName());
							catlatch.put(((Test) obj).getParent(), dataholder);
							selcat.add(dataholder);

						}
						Test copy = (Test) ((Test) obj).clone();
						dataholder.addTest(copy);
					}
				}
			}

			SaxExporter saxer = new SaxExporter(tagManager, selcat, appname, qaTeamMemberSignatures.getAsList());
			java.io.InputStreamReader fr = new java.io.InputStreamReader(this.getClass().getClassLoader().getResourceAsStream("de/elbosso/tools/tpedit/test_protocol.xsd"));
			java.io.BufferedReader br = new java.io.BufferedReader(fr);
			org.xml.sax.InputSource inputSource = new org.xml.sax.InputSource(br);
			javax.xml.transform.sax.SAXSource source = new javax.xml.transform.sax.SAXSource(saxer, inputSource);
			javax.xml.transform.TransformerFactory factory = javax.xml.transform.TransformerFactory.newInstance();
			javax.xml.transform.Transformer transformer = factory.newTransformer();
			//transformer.setOutputProperty(javax.xml.transform.OutputKeys.OMIT_XML_DECLARATION, "yes");
			transformer.setOutputProperty(javax.xml.transform.OutputKeys.INDENT, "yes");
			java.io.OutputStream pos = new java.io.FileOutputStream(file);
			java.io.OutputStream os = pos;
//			if(compress==true)
//				os=new java.util.zip.GZIPOutputStream(pos);
			javax.xml.transform.stream.StreamResult result = new javax.xml.transform.stream.StreamResult(os);
			transformer.transform(source, result);
			os.close();
			if (pos != os)
				pos.close();
			br.close();
			fr.close();
			java.io.InputStream wsris = this.getClass().getClassLoader().getResourceAsStream("de/elbosso/tools/tpedit/test_protocol.xsd");
			byte[] buf = new byte[65536];
			java.io.OutputStream wsros = new java.io.FileOutputStream(file.getParentFile().getCanonicalPath() + java.io.File.separator + "test_protocol.xsd");
			int bytesread = wsris.read(buf);
			while (bytesread > -1)
			{
				wsros.write(buf, 0, bytesread);
				bytesread = wsris.read(buf);
			}
			wsros.close();
			wsris.close();
		} catch (Exception exp)
		{
		}
		return rv;
	}

	private void populateToolBar()
	{
		toolbar.setFloatable(false);
		toolbar.add(newaction);
		toolbar.add(open);
		toolbar.add(saveAs);
		toolbar.add(save);
		toolbar.addSeparator();
		toolbar.add(importAction);
		toolbar.add(export);
		if (((saveFullPDF != null) && (previewPDF != null)) && (interactivePDF != null))
		{
			toolbar.addSeparator();
			toolbar.add(previewPDF);
			toolbar.add(saveFullPDF);
			toolbar.add(interactivePDF);
		}
		toolbar.addSeparator();
		toolbar.add(addaction);
		toolbar.add(editaction);
		toolbar.add(removeaction);
		toolbar.addSeparator();
		toolbar.add(copyaction);
		toolbar.add(pasteaction);
		toolbar.addSeparator();
		toolbar.add(expandTreeAction);
		toolbar.add(collapseTreeAction);
		toolbar.add(expandSelectedTreeAction);
		toolbar.add(collapseSelectedTreeAction);
	}

	private javax.swing.JMenuBar createMenuBar()
	{
		projmenu = new javax.swing.JMenu(i18n.getString("TPEdit.menu.project.title"));
		projmenu.setIcon(de.netsysit.util.ResourceLoader.getIcon("toolbarButtonGraphics/development/Application16.gif"));
//		projmenu.setMnemonic(java.awt.event.KeyEvent.VK_P);
		projmenu.add(newaction);
		projmenu.add(open);
		projmenu.add(saveAs);
		projmenu.add(save);
		projmenu.addSeparator();
		projmenu.add(importAction);
		projmenu.add(export);
		if (((saveFullPDF != null) && (previewPDF != null)) && (interactivePDF != null))
		{
			projmenu.addSeparator();
			projmenu.add(previewPDF);
			projmenu.add(saveFullPDF);
			projmenu.add(interactivePDF);
		}
		menuBar.add(projmenu);
		editmenu = new javax.swing.JMenu(i18n.getString("TPEdit.menu.editmenu.title"));
		editmenu.setIcon(de.netsysit.util.ResourceLoader.getIcon("toolbarButtonGraphics/general/Edit16.gif"));
//		editmenu.setMnemonic(java.awt.event.KeyEvent.VK_D);
		editmenu.add(addaction);
		editmenu.add(editaction);
		editmenu.add(removeaction);
		editmenu.addSeparator();
		editmenu.add(copyaction);
		editmenu.add(pasteaction);
		menuBar.add(editmenu);
		toolsmenu = new de.netsysit.ui.menu.ActionTypeAwareMenu(i18n.getString("TPEdit.menu.tools.title"));
		toolsmenu.setIcon(de.netsysit.util.ResourceLoader.getIcon("toolbarButtonGraphics/development/WebComponent16.gif"));
//		toolsmenu.setMnemonic(java.awt.event.KeyEvent.VK_T);
		toolsmenu.add(expandTreeAction);
		toolsmenu.add(collapseTreeAction);
		toolsmenu.add(expandSelectedTreeAction);
		toolsmenu.add(collapseSelectedTreeAction);
		menuBar.add(toolsmenu);
		return menuBar;
	}

	private void clearActionEnable()
	{
		addaction.setEnabled(false);
		editaction.setEnabled(false);
		removeaction.setEnabled(false);
		copyaction.setEnabled(false);
		export.setEnabled(false);
		pasteaction.setEnabled(false);
	}

	private void actionEnable(javax.swing.tree.TreePath[] tp)
	{
		addaction.setEnabled((tp != null) && (tp.length == 1));
		editaction.setEnabled((tp != null) && (tp.length == 1));
		removeaction.setEnabled((tp != null) && (tp.length > 0));
		copyaction.setEnabled((tp != null) && (tp.length > 0));
		export.setEnabled((tp != null) && (tp.length > 0));
		pasteaction.setEnabled(((tp != null) && (tp.length == 1) && (copied != null)));
		if ((tp != null) && (tp.length == 1))
		{
			java.lang.Object obj = tp[0].getPath()[tp[0].getPathCount() - 1];
			if (obj instanceof Category)
			{
			}
			else if (obj instanceof Test)
			{
//				addaction.setEnabled(false);
			}
			else
			{
				editaction.setEnabled(false);
				pasteaction.setEnabled(false);
			}
		}
		else if ((tp != null) && (tp.length == 1))
		{
			for (int i = 0; i < tp.length; ++i)
			{
				java.lang.Object obj = tp[1].getPath()[tp[1].getPathCount() - 1];
				if (obj instanceof Test == false)
				{
					copyaction.setEnabled(false);
					break;
				}
			}
			for (int i = 0; i < tp.length; ++i)
			{
				java.lang.Object obj = tp[1].getPath()[tp[1].getPathCount() - 1];
				if (obj instanceof Test)
				{
				}
				else if (obj instanceof Category)
				{
				}
				else
				{
					export.setEnabled(false);
					removeaction.setEnabled(false);
					break;
				}
			}
		}
	}

	//Implementation of interface java.awt.event.MouseListener
	public void mouseEntered(java.awt.event.MouseEvent mouseEvent0)
	{
	}

	//Implementation of interface java.awt.event.MouseListener
	public void mousePressed(java.awt.event.MouseEvent mouseEvent0)
	{
	}

	//Implementation of interface java.awt.event.MouseListener
	public void mouseClicked(java.awt.event.MouseEvent mouseEvent0)
	{
		if (mouseEvent0.getClickCount() > 1)
		{
			if (editaction.isEnabled())
				de.elbosso.util.Utilities.performAction(this, editaction);
		}
	}

	//Implementation of interface java.awt.event.MouseListener
	public void mouseExited(java.awt.event.MouseEvent mouseEvent0)
	{
	}

	//Implementation of interface java.awt.event.MouseListener
	public void mouseReleased(java.awt.event.MouseEvent mouseEvent0)
	{
	}

	//Implementation of interface javax.swing.event.TreeSelectionListener
	public void valueChanged(javax.swing.event.TreeSelectionEvent treeSelectionEvent0)
	{
		javax.swing.tree.TreePath[] tp = tree.getSelectionPaths();
		clearActionEnable();
		try
		{
			de.elbosso.tools.tpedit.Utilities.renderPreview(velocityHelper,tree,lbl);
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
			actionEnable(tp);
		}
	}

	public DefaultTreeModel getModel()
	{
		return model;
	}

	public static void main(java.lang.String[] args) throws Exception
	{
//		de.elbosso.util.Utilities.configureBasicStdoutLogging(Level.ALL);
		try
		{
			java.util.Properties iconFallbacks = new java.util.Properties();
			java.io.InputStream is=de.netsysit.util.ResourceLoader.getResource("de/elbosso/ressources/data/icon_trans_material.properties").openStream();
			iconFallbacks.load(is);
			is.close();
			de.netsysit.util.ResourceLoader.configure(iconFallbacks);
		}
		catch(java.io.IOException ioexp)
		{
			ioexp.printStackTrace();
		}
//		de.netsysit.util.ResourceLoader.setSize(false ? de.netsysit.util.ResourceLoader.IconSize.medium : de.netsysit.util.ResourceLoader.IconSize.small);
/*		org.apache.commons.logging.Log fopLogger = org.apache.commons.logging.LogFactory.getLog("org.apache.fop");
		System.out.println(fopLogger.getClass());
		System.out.println(fopLogger.isTraceEnabled());
		System.out.println(fopLogger.isDebugEnabled());
		System.out.println(fopLogger.isInfoEnabled());
		System.out.println(fopLogger.isWarnEnabled());
		System.out.println(fopLogger.isErrorEnabled());
		System.out.println(fopLogger.isFatalEnabled());
*/		de.netsysit.util.ResourceLoader.setSize(false ? de.netsysit.util.ResourceLoader.IconSize.medium : de.netsysit.util.ResourceLoader.IconSize.small);
		new TPEdit(i18n.getString("TPEdit.app.title"));
	}

	class Tree extends javax.swing.JTree implements
			java.awt.dnd.DropTargetListener
	{
		private java.awt.dnd.DropTarget droptarget = new java.awt.dnd.DropTarget(this, this);

		Tree(javax.swing.tree.TreeModel model)
		{
			super(model);
		}

		public void dragEnter(java.awt.dnd.DropTargetDragEvent dtde)
		{

			// DONE: Behaviour �ndern in Flag an DragImageGlassPane setzen
			java.awt.Component comp = dtde.getDropTargetContext().getComponent();
			//		java.awt.Component parent=getParent();
			//		if(parent==null)
			//			parent=this;
			//		latchcolor=parent.getBackground();
			if (comp instanceof de.netsysit.ui.components.DragImageGlassPane)
			{
				if (((de.netsysit.ui.components.DragImageGlassPane) comp).getSource() instanceof TPEdit)
					((de.netsysit.ui.components.DragImageGlassPane) comp).setUsedDecoration(de.netsysit.ui.components.DragImageGlassPane.OKDECORATION);
				else
					((de.netsysit.ui.components.DragImageGlassPane) comp).setUsedDecoration(de.netsysit.ui.components.DragImageGlassPane.DENIEDDECORATION);
			}
		}

		public void dragExit(java.awt.dnd.DropTargetEvent dte)
		{
			//		java.awt.Component parent=getParent();
			//		if(parent==null)
			//			parent=this;
			//		parent.setBackground(latchcolor);
			java.awt.Component comp = dte.getDropTargetContext().getComponent();
			if (comp instanceof de.netsysit.ui.components.DragImageGlassPane)
			{
				((de.netsysit.ui.components.DragImageGlassPane) comp).setUsedDecoration(de.netsysit.ui.components.DragImageGlassPane.DENIEDDECORATION);
			}
		}

		public void dragOver(java.awt.dnd.DropTargetDragEvent dtde)
		{

			java.awt.Point p = dtde.getLocation();
			javax.swing.tree.TreePath tp = tree.getClosestPathForLocation(p.x, p.y);
			java.lang.Object obj = tp.getLastPathComponent();
			if ((obj instanceof Test) || (obj instanceof Category))
			{
				if (obj instanceof Category)
				{
					long now = System.currentTimeMillis();
					if (obj == hovercat)
					{
						if (hovercat != null)
						{
							if (now - hoverthen > 800l)
							{
//								tree.expandPath(tp);
							}
						}
					}
					else
					{
						hoverthen = now;
						hovercat = (Category) obj;
					}
				}
				glassPane.setUsedDecoration(de.netsysit.ui.components.DragImageGlassPane.OKDECORATION);
			}
			else
				glassPane.setUsedDecoration(de.netsysit.ui.components.DragImageGlassPane.DENIEDDECORATION);
		}

		public void drop(java.awt.dnd.DropTargetDropEvent dtde)
		{
			try
			{

				java.awt.datatransfer.Transferable transferable = dtde.getTransferable();
				java.awt.datatransfer.DataFlavor df = new java.awt.datatransfer.DataFlavor(java.awt.datatransfer.DataFlavor.javaJVMLocalObjectMimeType);
				if (transferable.isDataFlavorSupported(df))
				{
					Test todrop = (Test) transferable.getTransferData(df);

					dtde.acceptDrop(java.awt.dnd.DnDConstants.ACTION_COPY);
					java.awt.Point p = dtde.getLocation();
					javax.swing.tree.TreePath tp = tree.getClosestPathForLocation(p.x, p.y);
					java.lang.Object obj = tp.getLastPathComponent();
					Category par = null;
					int newindex = 0;
					if (obj instanceof Test)
					{
						par = (Category) ((Test) obj).getParent();
						newindex = par.getIndex((Test) obj) + 1;//einfügen nach Knoten
//						newindex=par.getIndex((Test)obj);//einfügen vor Knoten
					}
					else
					{
						par = (Category) obj;
//						newindex=par.getChildCount();//einfügen vor Knoten
					}
					Category oldpar = (Category) todrop.getParent();
					oldpar.setJustEdited(true);
					int oldindex = oldpar.getIndex(todrop);
					if ((oldindex < newindex) && (par == oldpar))
						--newindex;

//					if(oldpar!=par)
					{
						par.insert(todrop, newindex);
						par.setJustEdited(true);
						((javax.swing.tree.DefaultTreeModel) getModel()).nodesWereRemoved(oldpar, new int[]{oldindex}, new java.lang.Object[]{todrop});
						((javax.swing.tree.DefaultTreeModel) getModel()).nodesWereInserted(par, new int[]{newindex});
					}
/*					else
					{
						if(oldindex<newindex)
						{
							par.insert(todrop,newindex);
							((javax.swing.tree.DefaultTreeModel)getModel()).nodeStructureChanged(par);
						}
						else if(oldindex>newindex)
						{

						}
					}
*/
					dtde.getDropTargetContext().dropComplete(true);
				}
				else
				{
					dtde.rejectDrop();
					java.awt.Toolkit.getDefaultToolkit().beep();
					//				LOGGER.debug("Drop was rejected");
				}
			} catch (java.awt.datatransfer.UnsupportedFlavorException ufException)
			{
//				org.apache.log4j.Logger.getLogger("ExceptionCatcher").error("Unsupported data flavour encountered during Drag and Drop",ufException);
				java.awt.Toolkit.getDefaultToolkit().beep();
				dtde.rejectDrop();
			} catch (java.lang.ClassNotFoundException ufException)
			{
//				org.apache.log4j.Logger.getLogger("ExceptionCatcher").error("Unsupported data flavour encountered during Drag and Drop",ufException);
				java.awt.Toolkit.getDefaultToolkit().beep();
				dtde.rejectDrop();
			} catch (java.io.IOException ufException)
			{
//				org.apache.log4j.Logger.getLogger("ExceptionCatcher").error("Unsupported data flavour encountered during Drag and Drop",ufException);
				java.awt.Toolkit.getDefaultToolkit().beep();
				dtde.rejectDrop();
			}
		}

		public void dropActionChanged(java.awt.dnd.DropTargetDragEvent dtde)
		{
		}
	}
}

