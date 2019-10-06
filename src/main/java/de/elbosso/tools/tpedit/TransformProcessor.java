/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package de.elbosso.tools.tpedit;

import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;

/**
 *
 * @author elbosso
 */
public interface TransformProcessor 
{
	public void perform(java.io.File inFile, java.io.File outFile) throws java.io.IOException, TransformerConfigurationException, TransformerException;
	public void perform(java.io.File inFile, java.io.File outFile, java.util.Map<java.lang.String, java.lang.Object> parameters) throws java.io.IOException, TransformerConfigurationException, TransformerException;
	public void perform(java.io.InputStream inStream, java.io.File outFile) throws java.io.IOException, TransformerConfigurationException, TransformerException;
	public void perform(java.io.InputStream inStream, java.io.File outFile, java.util.Map<java.lang.String, java.lang.Object> parameters) throws java.io.IOException, TransformerConfigurationException, TransformerException;
	public int getNumberOfPages();
}
