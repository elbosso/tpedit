/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package de.elbosso.tools.tpedit;

import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;

/**
 *
 * @author elbosso
 */
public class TransformIntoPDFProcessor extends java.lang.Object implements
		TransformProcessor
{
	private int numberOfPages;

	class DevNull extends OutputStream
	{
		@Override
		public void write(byte[] b) throws IOException
		{

		}

		@Override
		public void write(byte[] b, int off, int len) throws IOException
		{

		}

		@Override
		public void write(int b) throws IOException
		{

		}
	}
	public void perform(File inFile, File outFile) throws java.io.IOException, TransformerConfigurationException, TransformerException
	{
		perform(inFile, outFile, java.util.Collections.EMPTY_MAP);
	}
	public void perform(File inFile, File outFile, java.util.Map<java.lang.String,java.lang.Object> parameters) throws java.io.IOException, TransformerConfigurationException, TransformerException
	{
		java.io.InputStream inStream=new java.io.FileInputStream(inFile);
		perform(inStream, outFile,parameters);
		inStream.close();
	}
	public void perform(java.io.InputStream inStream, File outFile) throws java.io.IOException, TransformerConfigurationException, TransformerException
	{
		perform(inStream, outFile, java.util.Collections.EMPTY_MAP);
	}
	public void perform(java.io.InputStream inStream, File outFile, java.util.Map<java.lang.String,java.lang.Object> parameters) throws java.io.IOException, TransformerConfigurationException, TransformerException
	{
		java.io.PrintStream latchErr= System.err;
		java.io.PrintStream latchOut= System.out;
		System.setErr(new PrintStream(new DevNull()));
		System.setOut(new PrintStream(new DevNull()));
		numberOfPages=0;
		try
		{

			java.io.InputStream xmlStream = inStream;
			//        SSHFile xsltFile = outFile;
			javax.xml.transform.Source xmlSource =
					new javax.xml.transform.stream.StreamSource(xmlStream);
			javax.xml.transform.Source xsltSource = null;
			if (outFile.getCanonicalPath().toUpperCase().endsWith(".DIFF.PDF"))
				xsltSource = new javax.xml.transform.stream.StreamSource(de.netsysit.util.ResourceLoader.getResource("de/elbosso/tools/tpedit/test_protocol_fo_diff.xsl").openStream());
			else if (outFile.getCanonicalPath().toUpperCase().endsWith(".PDF"))
				xsltSource = new javax.xml.transform.stream.StreamSource(de.netsysit.util.ResourceLoader.getResource("de/elbosso/tools/tpedit/test_protocol_fo.xsl").openStream());
			else if (outFile.getCanonicalPath().toUpperCase().endsWith(".XML"))
				xsltSource = new javax.xml.transform.stream.StreamSource(de.netsysit.util.ResourceLoader.getResource("de/elbosso/tools/tpedit/test_protocol_docbook.xsl").openStream());
			else if (outFile.getCanonicalPath().toUpperCase().endsWith(".FORM"))
				xsltSource = new javax.xml.transform.stream.StreamSource(de.netsysit.util.ResourceLoader.getResource("de/elbosso/tools/tpedit/test_protocol_form.xsl").openStream());


			if ((outFile.getCanonicalPath().toUpperCase().endsWith(".PDF")) || (outFile.getCanonicalPath().toUpperCase().endsWith(".FORM")))
			{
				// configure fopFactory as desired
				final org.apache.fop.apps.FopFactory fopFactory = org.apache.fop.apps.FopFactory.newInstance(new File(".").toURI());

				org.apache.fop.apps.FOUserAgent foUserAgent = fopFactory.newFOUserAgent();
				// configure foUserAgent as desired

				// Setup output
				java.io.OutputStream out = new java.io.FileOutputStream(outFile);
				out = new java.io.BufferedOutputStream(out);

				try
				{
					// Construct fop with desired output format
					org.apache.fop.apps.Fop fop = fopFactory.newFop(org.apache.fop.apps.MimeConstants.MIME_PDF, foUserAgent, out);

					// Setup XSLT
					javax.xml.transform.TransformerFactory factory = javax.xml.transform.TransformerFactory.newInstance();
					javax.xml.transform.Transformer transformer = factory.newTransformer(xsltSource);

					// Set the value of a <param> in the stylesheet
					transformer.setParameter("versionParam", "2.0");

					// Setup input for XSLT transformation
					javax.xml.transform.Source src = xmlSource;

					// Resulting SAX events (the generated FO) must be piped through to FOP
					javax.xml.transform.Result res = new javax.xml.transform.sax.SAXResult(fop.getDefaultHandler());

					// Start XSLT transformation and FOP processing
					transformer.transform(src, res);
					numberOfPages=fop.getResults().getPageCount();
				} catch (java.lang.Throwable t)
				{
					throw new java.io.IOException(t);
				} finally
				{
					out.close();
				}

			}
			else
			{
				java.io.OutputStream dbos = new java.io.FileOutputStream(outFile);
				javax.xml.transform.Result result =
						new javax.xml.transform.stream.StreamResult(dbos);

				// create an instance of TransformerFactory
				javax.xml.transform.TransformerFactory transFact =
						javax.xml.transform.TransformerFactory.newInstance();

				javax.xml.transform.Transformer trans =
						transFact.newTransformer(xsltSource);
				for (java.lang.String paramname : parameters.keySet())
				{
					trans.setParameter(paramname, parameters.get(paramname));
				}

				trans.transform(xmlSource, result);
				dbos.close();
			}
		}
		finally
		{
			System.setErr(latchErr);
			System.setOut(latchOut);
		}
	}
	public int getNumberOfPages()
	{
		return numberOfPages;
	}
}
