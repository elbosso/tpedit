package de.elbosso.tools.tpedit;

import java.io.IOException;

public class Utilities extends  java.lang.Object
{
	private final static org.slf4j.Logger EXCEPTION_LOGGER = org.slf4j.LoggerFactory.getLogger("de.netsysit.experimental.ExceptionLogger");
	private final static org.slf4j.Logger CLASS_LOGGER = org.slf4j.LoggerFactory.getLogger(Utilities.class);
	private final static java.util.ResourceBundle i18n = java.util.ResourceBundle.getBundle("de.elbosso.tools.i18n", java.util.Locale.getDefault());

	private Utilities()
	{
		super();
	}
	static void renderPreview(VelocityHelper velocityHelper,javax.swing.JTree tree, javax.swing.JLabel lbl) throws IOException
	{
		javax.swing.tree.TreePath[] tp = tree.getSelectionPaths();
		if (tp != null)
		{
			if (tp.length == 1)
			{
				java.lang.Object obj = tp[0].getPath()[tp[0].getPathCount() - 1];
				if (obj instanceof Category)
				{

					Category category = (Category) obj;
					org.apache.velocity.VelocityContext context = new org.apache.velocity.VelocityContext();
					context.put("category", category);
					context.put("helper", velocityHelper);
					java.io.StringWriter sw = new java.io.StringWriter();
					java.io.PrintWriter w = new java.io.PrintWriter(sw);//null;
					org.apache.velocity.app.Velocity.mergeTemplate("de/elbosso/tools/tpedit/" + i18n.getString("TPEdit.category.template"), java.nio.charset.StandardCharsets.UTF_8.name(), context, w);
					w.flush();
					w.close();
					sw.close();
					lbl.setText(sw.getBuffer().toString());
				}
				else if (obj instanceof Test)
				{
					Test test = (Test) obj;
					if (CLASS_LOGGER.isDebugEnabled()) CLASS_LOGGER.debug(java.util.Objects.toString(test.getActions()));
					org.apache.velocity.VelocityContext context = new org.apache.velocity.VelocityContext();
					context.put("test", test);
					context.put("helper", velocityHelper);
					java.io.StringWriter sw = new java.io.StringWriter();
					java.io.PrintWriter w = new java.io.PrintWriter(sw);//null;
					org.apache.velocity.app.Velocity.mergeTemplate("de/elbosso/tools/tpedit/" + i18n.getString("TPEdit.test.template"), java.nio.charset.StandardCharsets.UTF_8.name(), context, w);
					w.flush();
					w.close();
					sw.close();
					lbl.setText(sw.getBuffer().toString());
				}
				else
				{
					lbl.setText("");
				}
			}
			else
			{
				lbl.setText("");
			}
		}
	}
}
