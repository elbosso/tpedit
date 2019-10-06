/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.elbosso.tools.tpedit;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.*;
import com.itextpdf.text.pdf.PdfPageLabels;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class AddContentToPDF
{
	private static long dateFieldCounter;
	private java.util.List<java.lang.String> qaTeamMemberSignatures;

	public AddContentToPDF(List<String> qaTeamMemberSignatures)
	{
		this.qaTeamMemberSignatures = qaTeamMemberSignatures;
	}

	void perform(java.util.List<SearchWordStrategy.TextChunk> chunks, java.io.File formFile, java.io.File resultFile) throws IOException, DocumentException
	{
		/* example inspired from "iText in action" (2006), chapter 2 */

		PdfReader reader = new PdfReader(formFile.getCanonicalPath()); // input PDF
		com.itextpdf.text.Document document = new com.itextpdf.text.Document(com.itextpdf.text.PageSize.A4);
		java.io.FileOutputStream fos=new FileOutputStream(resultFile);
		PdfStamper stamper1 = new PdfStamper(reader, fos);
		PdfWriter stamper = stamper1.getWriter();//PdfWriter.getInstance(document,fos); // output PDF
		BaseFont bf = BaseFont.createFont(
				BaseFont.HELVETICA, BaseFont.CP1252, BaseFont.NOT_EMBEDDED); // set font
		document.open();
		//loop on pages (1-based)
		PdfPageLabels pageLabels=new PdfPageLabels();
		for (int i = 1; i <= reader.getNumberOfPages(); i++)
		{
			PdfContentByte over = stamper1.getOverContent(i);
			pageLabels.addPageLabel(i,PdfPageLabels.DECIMAL_ARABIC_NUMERALS);
			com.itextpdf.text.pdf.PdfImportedPage page = stamper.getImportedPage(reader, i);

// Copy first page of existing PDF into output PDF
//if(i==2)
			document.newPage();
			over.addTemplate(page, 0, 0);
//Text (@508.31403,596.55,1.0 -> 544.998,596.55,1.0): Group:
//orientationMagnitude: 0
//distPerpendicular: -596
//distParallel: 508.31403
//topLocation: 544.998,605.166,1.0
//bottomLocation: 544.998,594.066,1.0
			int loop = 0;

SearchWordStrategy.TextChunk lefty=null;
			for (SearchWordStrategy.TextChunk chunk : chunks)
			{

				if(i==chunk.getPageNo())
				{
				com.itextpdf.text.Rectangle rect = chunk.getBoundingBox();



//				rect.setBorder(com.itextpdf.text.Rectangle.BOX);
//				rect.setBorderWidth(2);
//				over.setRGBColorFill(0xFF, 0xFF, 0);
  //  over.setLineWidth(3);
//				over.rectangle(rect);
//	over.fill();
//			over.fill();
				if(chunk.getText().equals("Group:"))
				{
					addRadio(rect, over,stamper,stamper1,i);
				}
				else if(chunk.getText().equalsIgnoreCase("%X%"))
				{
					addCheckBox(rect, over,stamper,stamper1,i);
				}
				else if(chunk.getText().equalsIgnoreCase("%DF"))
				{
					lefty=chunk;
				}
				else if(chunk.getText().equalsIgnoreCase("DF%"))
				{
					if(lefty!=null)
					{
						com.itextpdf.text.Rectangle rectl = lefty.getBoundingBox();
						float left=(java.lang.Math.min(rectl.getLeft(),chunk.getBoundingBox().getLeft()));
						float bottom=(java.lang.Math.min(rectl.getBottom(),chunk.getBoundingBox().getBottom()));
						float right=(java.lang.Math.max(rectl.getRight(),chunk.getBoundingBox().getRight()));
						float top=(java.lang.Math.max(rectl.getTop(),chunk.getBoundingBox().getTop()));
						com.itextpdf.text.Rectangle rect1=new com.itextpdf.text.Rectangle(left,bottom,right,top);
						addDateField(rect1, over,stamper,stamper1,i);
						lefty=null;
					}
				}
				else if(chunk.getText().equalsIgnoreCase("%TF"))
				{
					lefty=chunk;
				}
				else if(chunk.getText().equalsIgnoreCase("TF%"))
				{
					if(lefty!=null)
					{
						com.itextpdf.text.Rectangle rectl = lefty.getBoundingBox();
						float left=(java.lang.Math.min(rectl.getLeft(),chunk.getBoundingBox().getLeft()));
						float bottom=(java.lang.Math.min(rectl.getBottom(),chunk.getBoundingBox().getBottom()));
						float right=(java.lang.Math.max(rectl.getRight(),chunk.getBoundingBox().getRight()));
						float top=(java.lang.Math.max(rectl.getTop(),chunk.getBoundingBox().getTop()));
						com.itextpdf.text.Rectangle rect1=new com.itextpdf.text.Rectangle(left,bottom,right,top);
						addTextField(rect1, over,stamper,stamper1,i,rectl.getHeight());
						lefty=null;
					}
				}
				else if(chunk.getText().equalsIgnoreCase("%QF"))
				{
					lefty=chunk;
				}
				else if(chunk.getText().equalsIgnoreCase("QF%"))
				{
					if(lefty!=null)
					{
						com.itextpdf.text.Rectangle rectl = lefty.getBoundingBox();
						float left=(java.lang.Math.min(rectl.getLeft(),chunk.getBoundingBox().getLeft()));
						float bottom=(java.lang.Math.min(rectl.getBottom(),chunk.getBoundingBox().getBottom()));
						float right=(java.lang.Math.max(rectl.getRight(),chunk.getBoundingBox().getRight()));
						float top=(java.lang.Math.max(rectl.getTop(),chunk.getBoundingBox().getTop()));
						com.itextpdf.text.Rectangle rect1=new com.itextpdf.text.Rectangle(left,bottom,right,top);
						addComboField(rect1, over,stamper,stamper1,i,rectl.getHeight());
						lefty=null;
					}
				}
//				"."
//				else
//					over.stroke();

//				++loop;
//				if (loop > 30)
//				{
//					break;
//				}
				}
			}
		}
		document.close();
		stamper.setPageLabels(pageLabels);
		stamper1.close();
		fos.close();
		reader.close();
	}
	private void tippex(com.itextpdf.text.Rectangle rect,PdfContentByte over,PdfStamper stamper)
	{
		over.saveState();
			over.setRGBColorStroke(0xFF, 0xFF, 0xFF);
		over.setColorFill(com.itextpdf.text.BaseColor.WHITE);
			over.setLineWidth(1f);
			over.rectangle(rect.getLeft(),rect.getBottom(),rect.getRight()-rect.getLeft(),rect.getTop()-rect.getBottom());
		over.fillStroke();
		over.restoreState();
	}
	private void addCheckBox(com.itextpdf.text.Rectangle rect,PdfContentByte over,PdfWriter writer,PdfStamper stamper,int page) throws IOException, DocumentException
	{
		tippex(rect,over,stamper);
		float w=rect.getWidth();
		float h=rect.getHeight();
		float dim=java.lang.Math.min(w, h);
		float xo=(w-dim)/2.0f;
		float yo=(h-dim)/2.0f;
		com.itextpdf.text.Rectangle rect1=new com.itextpdf.text.Rectangle(rect.getLeft()+xo,rect.getBottom()+yo,rect.getLeft()+xo+dim,rect.getBottom()+yo+dim);
		com.itextpdf.text.pdf.RadioCheckField bt = new com.itextpdf.text.pdf.RadioCheckField(writer, rect1, "check1", "Yes");
 bt.setCheckType(com.itextpdf.text.pdf.RadioCheckField.TYPE_CHECK);
 bt.setBorderWidth(com.itextpdf.text.pdf.BaseField.BORDER_WIDTH_THIN);
 bt.setBorderColor(com.itextpdf.text.BaseColor.BLACK);
 bt.setBackgroundColor(com.itextpdf.text.BaseColor.WHITE);
 PdfFormField ck = bt.getCheckField();
 		stamper.addAnnotation(ck,page);
	}
	private void addTextField(com.itextpdf.text.Rectangle rect,PdfContentByte over,PdfWriter writer,PdfStamper stamper,int page,float lineheight) throws IOException, DocumentException
	{
		boolean multiline=rect.getHeight()!=lineheight;
		tippex(rect,over,stamper);
		com.itextpdf.text.pdf.TextField name = new com.itextpdf.text.pdf.TextField(writer, rect, "name");
		name.setBorderColor(com.itextpdf.text.BaseColor.DARK_GRAY);
		name.setBorderStyle(com.itextpdf.text.pdf.PdfBorderDictionary.STYLE_SOLID);
		name.setBorderWidth(1);

		name.setFontSize(lineheight-2*name.getBorderWidth());

		if(multiline)
		{
			name.setOptions(com.itextpdf.text.pdf.TextField.MULTILINE
					| com.itextpdf.text.pdf.TextField.DO_NOT_SCROLL);
		}
		PdfFormField personal_name = name.getTextField();
		stamper.addAnnotation(personal_name,page);
	}
	private void addComboField(com.itextpdf.text.Rectangle rect,PdfContentByte over,PdfWriter writer,PdfStamper stamper,int page,float lineheight) throws IOException, DocumentException
	{
		boolean multiline=rect.getHeight()!=lineheight;
		tippex(rect,over,stamper);
		com.itextpdf.text.pdf.TextField name = new com.itextpdf.text.pdf.TextField(writer, rect, "name");
		name.setBorderColor(com.itextpdf.text.BaseColor.DARK_GRAY);
		name.setBorderStyle(com.itextpdf.text.pdf.PdfBorderDictionary.STYLE_SOLID);
		name.setBorderWidth(1);

		name.setFontSize(lineheight-2*name.getBorderWidth());

		if(multiline)
		{
			name.setOptions(com.itextpdf.text.pdf.TextField.MULTILINE
					| com.itextpdf.text.pdf.TextField.DO_NOT_SCROLL);
		}
		PdfFormField personal_name;
		if((qaTeamMemberSignatures!=null)&&(qaTeamMemberSignatures.isEmpty()==false))
		{
			name.setChoices(qaTeamMemberSignatures.toArray(new java.lang.String[0]));
			name.setChoiceExports(qaTeamMemberSignatures.toArray(new java.lang.String[0]));
			name.setOptions(com.itextpdf.text.pdf.TextField.EDIT);
			personal_name = name.getComboField();
		}
		else
			personal_name = name.getTextField();
		stamper.addAnnotation(personal_name,page);
	}
	private void addDateField(com.itextpdf.text.Rectangle rect,PdfContentByte over,PdfWriter writer,PdfStamper stamper,int page) throws IOException, DocumentException
	{
		boolean multiline=false;
		tippex(rect,over,stamper);
		com.itextpdf.text.Rectangle trect=new com.itextpdf.text.Rectangle(rect.getLeft(),rect.getBottom(),rect.getLeft()+rect.getWidth()-rect.getHeight(),rect.getBottom()+ rect.getHeight());
		com.itextpdf.text.pdf.TextField date = new com.itextpdf.text.pdf.TextField(writer, trect, "date"+dateFieldCounter);
		date.setBorderColor(com.itextpdf.text.BaseColor.DARK_GRAY);
		date.setBorderStyle(com.itextpdf.text.pdf.PdfBorderDictionary.STYLE_SOLID);
		date.setBorderWidth(1);

		date.setFontSize(rect.getHeight()-2*date.getBorderWidth());
		PdfFormField personal_date = date.getTextField();
		//personal_date.setAdditionalActions(PdfName.V, PdfAction.javaScript("AFDate_FormatEx( 'dd-mm-yyyy' );", writer));
		stamper.addAnnotation(personal_date,page);
		trect=new com.itextpdf.text.Rectangle(trect.getLeft()+trect.getWidth()+1,trect.getBottom(),trect.getLeft()+trect.getWidth()+trect.getHeight(),trect.getBottom()+ trect.getHeight());

		com.itextpdf.text.pdf.PushbuttonField now = new com.itextpdf.text.pdf.PushbuttonField(writer, trect, "o");
		now.setBorderColor(com.itextpdf.text.BaseColor.DARK_GRAY);
		now.setBorderStyle(com.itextpdf.text.pdf.PdfBorderDictionary.STYLE_SOLID);
		now.setBorderWidth(1);
		now.setBackgroundColor(BaseColor.LIGHT_GRAY);
		personal_date = now.getField();
		personal_date.setAction(PdfAction.javaScript("getField(\"date"+dateFieldCounter+"\").value = util.printd(\"yyyy-mm-dd HH:MM\", new Date());", writer));
		stamper.addAnnotation(personal_date,page);
		++dateFieldCounter;
	}

	private void addRadio(com.itextpdf.text.Rectangle rect,PdfContentByte over,PdfWriter writer,PdfStamper stamper,int page)
	{
		tippex(rect,over,stamper);
 			float height = rect.getTop()-rect.getBottom();
			float yy = rect.getBottom();
			over.moveTo(0, 0);
			com.itextpdf.text.pdf.PdfFormField radio = com.itextpdf.text.pdf.PdfFormField.createRadioButton(writer, true);
			com.itextpdf.text.pdf.PdfAppearance tpOff = over.createAppearance(20, 20);
			com.itextpdf.text.pdf.PdfAppearance tpOn = over.createAppearance(20, 20);

			tpOff.circle(10, 10, 9);
			tpOff.stroke();

			tpOn.circle(10, 10, 9);
			tpOn.stroke();
			tpOn.circle(10, 10, 3);
			tpOn.fillStroke();

			radio.setFieldName("key");
			radio.setValueAsName("false");

			String truetext = "";
			String falsetext = "";
			String natext = "";
			float xKoord = rect.getLeft();
			float tlength = 0;

			com.itextpdf.text.Font font = com.itextpdf.text.FontFactory.getFont(com.itextpdf.text.FontFactory.HELVETICA, com.itextpdf.text.Font.DEFAULTSIZE, com.itextpdf.text.Font.NORMAL);
			com.itextpdf.text.pdf.BaseFont bfont = font.getBaseFont();

			truetext = "true.text";
			falsetext = "false.text";
			natext = "na.text";
//		addLabelToRadiobutton(over, bfont, xKoord - 3, (int) yy, height, "links", truetext);

			// Zeichnen true (männlich)-Button
			com.itextpdf.text.pdf.PdfFormField radio1 = com.itextpdf.text.pdf.PdfFormField.createEmpty(writer);
			radio1.setWidget(new com.itextpdf.text.Rectangle(xKoord, yy, xKoord + height, yy + height), com.itextpdf.text.pdf.PdfAnnotation.HIGHLIGHT_INVERT);
			radio1.setAppearanceState("false");
			radio1.setAppearance(com.itextpdf.text.pdf.PdfAnnotation.APPEARANCE_NORMAL, "Off", tpOff);
			radio1.setAppearance(com.itextpdf.text.pdf.PdfAnnotation.APPEARANCE_NORMAL, "false", tpOn);
			radio.addKid(radio1);

			// Zeichnen false (weiblich)-Button
			xKoord += (rect.getRight()-rect.getLeft())/2f;
			com.itextpdf.text.pdf.PdfFormField radio2 = com.itextpdf.text.pdf.PdfFormField.createEmpty(writer);
			radio2.setWidget(new com.itextpdf.text.Rectangle(xKoord, yy, xKoord + height, yy + height), com.itextpdf.text.pdf.PdfAnnotation.HIGHLIGHT_INVERT);
			radio2.setAppearanceState("Off");
			radio2.setAppearance(com.itextpdf.text.pdf.PdfAnnotation.APPEARANCE_NORMAL, "Off", tpOff);
			radio2.setAppearance(com.itextpdf.text.pdf.PdfAnnotation.APPEARANCE_NORMAL, "true", tpOn);
			radio.addKid(radio2);

			// Beschriftung false (weiblich)-Button
			tlength = bfont.getWidthPoint(falsetext, 12);
			xKoord = xKoord + height;
			// + (int)tlength;
//		addLabelToRadiobutton(over, bfont,  xKoord, (int) yy, height, "rechts", falsetext);

			stamper.addAnnotation(radio,page);

	}
}
