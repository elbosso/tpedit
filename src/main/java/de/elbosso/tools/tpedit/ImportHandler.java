package de.elbosso.tools.tpedit;
//$Id$

import de.elbosso.util.lang.TagDescription;

import java.io.File;
import java.util.List;

class ImportHandler extends org.xml.sax.helpers.DefaultHandler
{
	private final static org.apache.log4j.Logger CLASS_LOGGER = org.apache.log4j.Logger.getLogger(ImportHandler.class);
	private java.util.List categories;
	private java.io.File file;
	private Category category;
	private Test test;
	private java.lang.String currentElement;
	private java.lang.String currentElementContent;
	private java.lang.StringBuffer content;
	private java.lang.String appname;
	private boolean ignoreTag;
	private de.elbosso.ui.components.TagManager tagManager;
	private de.elbosso.util.lang.TagDescription tagDescription;
	private java.util.List<java.lang.String> qaTeamMemberSignatures;

	ImportHandler(java.io.File file)
	{
		this(file,null);
	}

	ImportHandler(File file, de.elbosso.ui.components.TagManager tagManager)
	{
		super();
		this.file=file;
		this.tagManager=tagManager;
	}
	public java.util.List getCategories()
	{
		return categories;
	}
	public java.lang.String getAppname()
	{
		return appname;
	}

	public List<String> getQaTeamMemberSignatures()
	{
		return qaTeamMemberSignatures;
	}

	public void startDocument()throws org.xml.sax.SAXException
	{
		qaTeamMemberSignatures=new java.util.LinkedList();
		categories=new java.util.LinkedList();
	}
	public void endDocument()throws org.xml.sax.SAXException
	{

	}
	public void startElement(java.lang.String namespaceURI,java.lang.String sName, java.lang.String qName,org.xml.sax.Attributes attrs)throws org.xml.sax.SAXException
	{
		content=new java.lang.StringBuffer();
		java.lang.String eName = sName; // element name
		if ("".equals(eName))
			eName = qName; // namespaceAware = false

		currentElement=eName;
		if(eName.equals("FunctionalTest"))
		{
			if(attrs!=null)
			{
				appname=attrs.getValue(attrs.getIndex("application"));
			}
		}
		else if(eName.equals("Category"))
		{
			category=new Category();
			if(attrs!=null)
			{
				category.setName(attrs.getValue(attrs.getIndex("name")));
			}
		}
		else if(eName.equals("TagDescription"))
		{
			tagDescription=new TagDescription();
		}
		else if(eName.equals("Test"))
		{
			test=new Test();
			if(attrs!=null)
			{
				test.setFromVersionMajor(attrs.getValue(attrs.getIndex("FromVersionMajor"))!=null?java.lang.Integer.parseInt(attrs.getValue(attrs.getIndex("FromVersionMajor"))):0);
				test.setFromVersionMinor(attrs.getValue(attrs.getIndex("FromVersionMinor"))!=null?java.lang.Integer.parseInt(attrs.getValue(attrs.getIndex("FromVersionMinor"))):0);
				test.setRequirementId(attrs.getValue(attrs.getIndex("RequirementID")));
				java.lang.String tag=attrs.getValue(attrs.getIndex("Tags"));
				if(((tag!=null)&&(tag.length()>0))&&(tag.indexOf('.')<0))
					tag="uncategorized."+tag;
				test.addTags(tag);
				if(tagManager!=null)
					tagManager.addTag(tag);
				test.setID(category.getChildCount()+1);
				test.setVariants(attrs.getValue(attrs.getIndex("Variants")));
			}
		}
		else if(eName.equals("Tag"))
		{
			ignoreTag=false;
			java.lang.String hidden=attrs.getValue("hidden");
			if((hidden!=null)&&(hidden.trim().equalsIgnoreCase("true")))
				ignoreTag=true;
		}
	}
	public void endElement(java.lang.String namespaceURI,java.lang.String sName, java.lang.String qName)throws org.xml.sax.SAXException
	{
		java.lang.String eName = sName; // element name
		if ("".equals(eName))
			eName = qName; // namespaceAware = false

		if(eName.equals("Category"))
		{
			categories.add(category);
			category=new Category();
		}
		else if(eName.equals("Test"))
		{
			category.addTest(test);
			test=null;
		}
		else if(currentElement!=null)
		{
			if(currentElement.equals("Description"))
			{
				if(test!=null)
					test.setDescription(content.toString());
				else
				{
					category.setDescription(content.toString());
				}
			}
			else if(currentElement.equals("Action"))
			{

				if(test==null)
				{
					if(CLASS_LOGGER.isTraceEnabled())CLASS_LOGGER.trace("test wronkly closed when parsing an action "+content.toString());
				}
				else
					test.addAction(content.toString());
			}
			else if(currentElement.equals("ExpectedResult"))
			{
				if(test==null)
				{
					if(CLASS_LOGGER.isTraceEnabled())CLASS_LOGGER.trace("test wronkly closed when parsing an expectedResult "+content.toString());
				}
				else
					test.addExpectedResult(content.toString());
			}
			else if(currentElement.equals("Variant"))
			{
				if(test==null)
				{
					if(CLASS_LOGGER.isTraceEnabled())CLASS_LOGGER.trace("test wronkly closed when parsing a variant "+content.toString());
				}
				else
					test.addVariant(content.toString());
			}
			else if(currentElement.equals("Tag"))
			{
				if(test==null)
				{
					if(CLASS_LOGGER.isTraceEnabled())CLASS_LOGGER.trace("test wronkly closed when parsing a tag "+content.toString());
				}
				else
//				if(content.toString().equals("!!")==false)
				{
					if(ignoreTag==false)
					{
						java.lang.String tag=content.toString();
						if(((tag!=null)&&(tag.length()>0))&&(tag.indexOf('.')<0))
							tag="uncategorized."+tag;
						test.addTags(tag);
						if(tagManager!=null)
							tagManager.addTag(tag);
					}
					ignoreTag=false;
				}
			}
			else if(currentElement.equals("TagName"))
			{
				if (tagDescription == null)
				{
					if (CLASS_LOGGER.isTraceEnabled())
						CLASS_LOGGER.trace("test wronkly closed when parsing a tag description " + content.toString());
				}
				else
//				if(content.toString().equals("!!")==false)
				{
					tagDescription.setName(content.toString());
				}
			}
			else if(currentElement.equals("Color"))
			{
				if (tagDescription == null)
				{
					if (CLASS_LOGGER.isTraceEnabled())
						CLASS_LOGGER.trace("test wronkly closed when parsing a tag description " + content.toString());
				}
				else
//				if(content.toString().equals("!!")==false)
				{
//					tagDescription.content.toString());
				}
			}
			else if(currentElement.equals("TagDescription"))
			{
				if (tagDescription == null)
				{
					if (CLASS_LOGGER.isTraceEnabled())
						CLASS_LOGGER.trace("test wronkly closed when parsing a tag description " + content.toString());
				}
				else
//				if(content.toString().equals("!!")==false)
				{
					java.lang.String tag=tagDescription.getName();
					if(((tag!=null)&&(tag.length()>0))&&(tag.indexOf('.')<0))
						tagDescription.setName("uncategorized."+tag);
					tagManager.addTag(tagDescription);
				}
			}
			else if(currentElement.equals("Signature"))
			{
				qaTeamMemberSignatures.add(content.toString());
			}
		}
		currentElement=null;
	}
	public void characters(char[] ch,int start, int length)
	{
		if(currentElement!=null)
		{

			if(currentElement.equals("ExpectedResult"))
				content.append(new java.lang.String(ch,start,length));
			else if(currentElement.equals("Action"))
				content.append(new java.lang.String(ch,start,length));
			else if(currentElement.equals("Description"))
				content.append(new java.lang.String(ch,start,length));
			else if(currentElement.equals("Variant"))
				content.append(new java.lang.String(ch,start,length));
			else if(currentElement.equals("Tag"))
				content.append(new java.lang.String(ch,start,length));
			else if(currentElement.equals("TagName"))
				content.append(new java.lang.String(ch,start,length));
			else if(currentElement.equals("Signature"))
				content.append(new java.lang.String(ch,start,length));
		}
	}
}

