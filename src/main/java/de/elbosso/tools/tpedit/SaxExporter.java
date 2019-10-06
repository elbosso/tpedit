package de.elbosso.tools.tpedit;

import de.elbosso.util.Utilities;

//$Id$
class SaxExporter extends java.lang.Object implements org.xml.sax.XMLReader
{
	private final VelocityHelper velocityHelper;
	private java.util.List categories;
	private org.xml.sax.ContentHandler handler;
	private java.lang.String nsu = "";  // NamespaceURI
	private org.xml.sax.helpers.AttributesImpl rootatts = new org.xml.sax.helpers.AttributesImpl();
	// We're not doing namespaces, and we have no
	// attributes on our elements.
	private org.xml.sax.Attributes emptyatts = new org.xml.sax.helpers.AttributesImpl();
	private java.lang.String rootElement = "FunctionalTest";
	private de.elbosso.ui.components.TagManager tagManager;
	private java.util.Collection<java.lang.String> qaMembers;

	SaxExporter(de.elbosso.ui.components.TagManager tagManager,java.util.List categories,java.lang.String appname, java.util.Collection<java.lang.String> qaMembers)
	{
		this(tagManager,categories,appname,qaMembers,null);
	}
	SaxExporter(de.elbosso.ui.components.TagManager tagManager,java.util.List categories,java.lang.String appname, java.util.Collection<java.lang.String> qaMembers,VelocityHelper velocityHelper)
	{
		super();
		this.tagManager=tagManager;
		this.categories=categories;
		this.qaMembers=qaMembers;
		this.velocityHelper=velocityHelper;
		rootatts.addAttribute("xmlns","xsi","xmlns:xsi","CDATA","http://www.w3.org/2001/XMLSchema-instance");
		rootatts.addAttribute("xsi","noNamespaceSchemaLocation","xsi:noNamespaceSchemaLocation","CDATA","test_protocol.xsd");
		rootatts.addAttribute(null,null,"application","CDATA",appname);
		rootatts.addAttribute(null,null,"date","CDATA",new java.sql.Date(System.currentTimeMillis()).toString());
	}
	/** Parse an XML document from a system identifier (URI). */
	public void parse(String systemId)
	throws java.io.IOException, org.xml.sax.SAXException
	{ }

	/** Return the current DTD handler. */
	public org.xml.sax.DTDHandler getDTDHandler()
	{ return null; }

	/** Return the current entity resolver. */
	public org.xml.sax.EntityResolver getEntityResolver()
	{ return null; }

	/** Allow an application to register an entity resolver. */
	public void setEntityResolver(org.xml.sax.EntityResolver resolver)
	{ }

	/** Allow an application to register a DTD event handler. */
	public void setDTDHandler(org.xml.sax.DTDHandler handler)
	{ }

	/** Look up the value of a property. */
	public Object getProperty(java.lang.String name)
	{ return null; }

	/** Set the value of a property. */
	public void setProperty(java.lang.String name, java.lang.Object value)
	{ }

	/** Set the state of a feature. */
	public void setFeature(java.lang.String name, boolean value)
	{ }

	/** Look up the value of a feature. */
	public boolean getFeature(java.lang.String name)
	{ return false; }

	/** Allow an application to register an error event handler. */
	public void setErrorHandler(org.xml.sax.ErrorHandler handler)
	{ }

	/** Return the current error handler. */
	public org.xml.sax.ErrorHandler getErrorHandler()
	{ return null; }



	/** Allow an application to register a content event handler. */
	public void setContentHandler(org.xml.sax.ContentHandler handler) {
	  this.handler = handler;
	}

	/** Return the current content handler. */
	public org.xml.sax.ContentHandler getContentHandler() {
	  return this.handler;
	}
	private void exportMacroActions(java.util.List collection,java.lang.Object parent)throws java.io.IOException, org.xml.sax.SAXException
	{
		org.xml.sax.helpers.AttributesImpl tatts = new org.xml.sax.helpers.AttributesImpl();
		tatts.addAttribute(null,null,"fromMacro","CDATA",parent==null?"false":"true");
		for(java.util.Iterator aiter=collection.iterator();aiter.hasNext();)
		{
			java.lang.String content=aiter.next().toString();
			if(velocityHelper!=null)
			{
				java.lang.String[] arr=velocityHelper.actionReferences(content);
				java.util.List children=arr!=null?java.util.Arrays.asList(arr):null;
				if(children!=null)
					exportMacroActions(children,content);
				else
				{
					handler.startElement(nsu, "Action", "Action", tatts);
					handler.characters(content.toCharArray(), 0, content.length());
					handler.endElement(nsu, "Action", "Action");
				}
			}
			else
			{
				handler.startElement(nsu,"Action","Action", tatts);
				handler.characters(content.toCharArray(),0,content.length());
				handler.endElement(nsu,"Action","Action");
			}
		}
	}
	private void exportMacroResults(java.util.List collection,java.lang.Object parent)throws java.io.IOException, org.xml.sax.SAXException
	{
		org.xml.sax.helpers.AttributesImpl tatts = new org.xml.sax.helpers.AttributesImpl();
		tatts.addAttribute(null,null,"fromMacro","CDATA",parent==null?"false":"true");
		for(java.util.Iterator aiter=collection.iterator();aiter.hasNext();)
		{
			java.lang.String content=aiter.next().toString();
			if(velocityHelper!=null)
			{
				java.lang.String[] arr=velocityHelper.resultReferences(content);
				java.util.List children=arr!=null?java.util.Arrays.asList(arr):null;
				if(children!=null)
					exportMacroResults(children,content);
				else
				{
					handler.startElement(nsu,"ExpectedResult","ExpectedResult", tatts);
					handler.characters(content.toCharArray(),0,content.length());
					handler.endElement(nsu,"ExpectedResult","ExpectedResult");
				}
			}
			else
			{
				handler.startElement(nsu,"ExpectedResult","ExpectedResult", tatts);
				handler.characters(content.toCharArray(),0,content.length());
				handler.endElement(nsu,"ExpectedResult","ExpectedResult");
			}
		}
	}
	private void fill()throws java.io.IOException, org.xml.sax.SAXException
	{
		int loop=0;
		handler.startElement(nsu,"TagsDescription","TagsDescription", emptyatts);
		for(int i=0;i<tagManager.getListModel().getSize();++i)
		{
			de.elbosso.util.lang.TagDescription td=(de.elbosso.util.lang.TagDescription)tagManager.getListModel().getElementAt(i);
			handler.startElement(nsu,"TagDescription","TagDescription", emptyatts);
			handler.startElement(nsu,"TagName","TagName", emptyatts);
			handler.characters(td.getName().toCharArray(),0,td.getName().length());
			handler.endElement(nsu,"TagName","TagName");
			handler.startElement(nsu,"Color","Color", emptyatts);
			java.lang.String t= Utilities.toStringRepresentation(td.getColor());
			handler.characters(t.toCharArray(),0,t.length());
			handler.endElement(nsu,"Color","Color");
			handler.endElement(nsu,"TagDescription","TagDescription");
		}
		handler.endElement(nsu,"TagsDescription","TagsDescription");
		handler.startElement(nsu,"QaTeamMembers","QaTeamMembers", emptyatts);
		for(java.lang.String qaMember:qaMembers)
		{
			handler.startElement(nsu,"QaTeamMember","QaTeamMember", emptyatts);
			handler.startElement(nsu,"Signature","Signature", emptyatts);
			handler.characters(qaMember.toCharArray(),0,qaMember.length());
			handler.endElement(nsu,"Signature","Signature");
			handler.endElement(nsu,"QaTeamMember","QaTeamMember");
		}
		handler.endElement(nsu,"QaTeamMembers","QaTeamMembers");
		for(java.util.Iterator citer=categories.iterator();citer.hasNext();++loop)
		{
			Category category=(Category)citer.next();
			org.xml.sax.helpers.AttributesImpl catts = new org.xml.sax.helpers.AttributesImpl();
			catts.addAttribute(null,null,"ID","CDATA",java.lang.Integer.toString(loop));
			catts.addAttribute(null,null,"name","CDATA",category.getName());
			if(category.isJustAdded())
				catts.addAttribute(null,null,"new","CDATA","true");
			if(category.isJustEdited())
				catts.addAttribute(null,null,"edited","CDATA","true");
			handler.startElement(nsu, "Category","Category", catts);
			handler.startElement(nsu,"Description","Description", emptyatts);
			handler.characters(category.getDescription().toCharArray(),0,category.getDescription().length());
			handler.endElement(nsu,"Description","Description");
			int testcount=1;
			for(java.util.Iterator titer=category.getTests().iterator();titer.hasNext();++testcount)
			{
				Test test=(Test)titer.next();
				org.xml.sax.helpers.AttributesImpl tatts = new org.xml.sax.helpers.AttributesImpl();
				test.setID(testcount);
				tatts.addAttribute(null,null,"ID","CDATA",java.lang.Integer.toString(testcount));
//				tatts.addAttribute(null,null,"Variants","CDATA",test.getVariants());
				tatts.addAttribute(null,null,"FromVersionMajor","CDATA",java.lang.Integer.toString(test.getFromVersionMajor()));
				tatts.addAttribute(null,null,"FromVersionMinor","CDATA",java.lang.Integer.toString(test.getFromVersionMinor()));
				if(test.isJustAdded())
					tatts.addAttribute(null,null,"new","CDATA","true");
				if(test.isJustEdited())
					tatts.addAttribute(null,null,"edited","CDATA","true");
				if(test.getRequirementId()!=null)
					tatts.addAttribute(null,null,"RequirementID","CDATA",test.getRequirementId());
//				if(test.getTags()!=null)
//					tatts.addAttribute(null,null,"Tags","CDATA",test.getTags());
				handler.startElement(nsu,"Test","Test", tatts);
				handler.startElement(nsu,"Description","Description", emptyatts);
				handler.characters(test.getDescription().toCharArray(),0,test.getDescription().length());
				handler.endElement(nsu,"Description","Description");
				handler.startElement(nsu,"Actions","Actions", emptyatts);
				java.util.List actions=test.getActions();
				exportMacroActions(actions,null);
				handler.endElement(nsu,"Actions","Actions");
				handler.startElement(nsu,"ExpectedResults","ExpectedResults", emptyatts);
				java.util.List results=test.getExpectedResults();
				exportMacroResults(results,null);
				handler.endElement(nsu,"ExpectedResults","ExpectedResults");
				if(test.getVariants().isEmpty()==false)
				{
					handler.startElement(nsu,"Variants","Variants", emptyatts);
					for(java.util.Iterator viter=test.getVariants().iterator();viter.hasNext();)
					{
						java.lang.String content=viter.next().toString();
						handler.startElement(nsu,"Variant","Variant", emptyatts);
						handler.characters(content.toCharArray(),0,content.length());
						handler.endElement(nsu,"Variant","Variant");
					}
					handler.endElement(nsu,"Variants","Variants");
				}
				handler.startElement(nsu,"Tags","Tags", emptyatts);
				tatts = new org.xml.sax.helpers.AttributesImpl();
				tatts.addAttribute(null,null,"hidden","CDATA","true");
				handler.startElement(nsu,"Tag","Tag", tatts);
				handler.characters("!!".toCharArray(),0,"!!".length());
				handler.endElement(nsu,"Tag","Tag");
				if(test.getTags()!=null)
				{
					java.util.StringTokenizer tok=new java.util.StringTokenizer(test.getTags(), ",");
					while(tok.hasMoreTokens())
					{
						handler.startElement(nsu,"Tag","Tag", emptyatts);
						java.lang.String t=tok.nextToken().trim();
						handler.characters(t.toCharArray(),0,t.length());
						handler.endElement(nsu,"Tag","Tag");
					}
				}
				handler.endElement(nsu,"Tags","Tags");
				handler.endElement(nsu,"Test","Test");
			}
			handler.endElement(nsu,"Category","Category");      
		}
	}
	public void parse(org.xml.sax.InputSource input) throws java.io.IOException, org.xml.sax.SAXException
	{
		handler.startDocument();      
		handler.startElement(nsu, rootElement, rootElement, rootatts); 
		fill();
		handler.endElement(nsu, rootElement, rootElement);
		handler.endDocument();      
	}
}

