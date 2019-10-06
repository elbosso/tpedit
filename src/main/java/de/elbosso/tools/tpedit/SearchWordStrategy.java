/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.elbosso.tools.tpedit;

import com.itextpdf.text.pdf.parser.*;
import de.elbosso.util.Utilities;

import java.util.List;

/**
 *
 * @author elbosso
 */
public class SearchWordStrategy implements com.itextpdf.text.pdf.parser.RenderListener
{
	private int pageNumber;
	/**
	 * set to true for debugging
	 */
	public static boolean DUMP_STATE = false;

	/**
	 * a summary of all found text
	 */
	private java.util.List<TextChunk> locationalResult = new java.util.LinkedList<TextChunk>();

	/**
	 * Creates a new text extraction renderer.
	 */
	public SearchWordStrategy()
	{
	}

	public void setPageNumber(int pageNumber)
	{
		this.pageNumber = pageNumber;
	}

	public List<TextChunk> getLocationalResult()
	{
		return locationalResult;
	}

	/**
	 * @see com.itextpdf.text.pdf.parser.RenderListener#beginTextBlock()
	 */
	public void beginTextBlock()
	{
	}

	/**
	 * @see com.itextpdf.text.pdf.parser.RenderListener#endTextBlock()
	 */
	public void endTextBlock()
	{
	}

	public void close()
	{
		if (DUMP_STATE)
		{
			dumpState();
		}

	}

	/**
	 * @param str
	 * @return true if the string starts with a space character, false if the
	 * string is empty or starts with a non-space character
	 */
	private boolean startsWithSpace(String str)
	{
		if (str.length() == 0)
		{
			return false;
		}
		return str.startsWith(" ");
	}

	/**
	 * @param str
	 * @return true if the string ends with a space character, false if the
	 * string is empty or ends with a non-space character
	 */
	private boolean endsWithSpace(String str)
	{
		if (str.length() == 0)
		{
			return false;
		}
		return str.endsWith(" ");
	}

	/**
	 * Returns the result so far.
	 *
	 * @return a String with the resulting text.
	 */
	public String getResultantText()
	{
		Utilities.sopln("getResultantText");
		String word = "";

		float lastLeft = 0;
		float lastWidth = 0;
		float lastTop = 0;
		float lastHeight = 0;
		float lastRight = 0;

		if (DUMP_STATE)
		{
			dumpState();
		}

		java.util.Collections.sort(locationalResult);

		StringBuilder sb = new StringBuilder();
		TextChunk lastChunk = null;
		for (TextChunk chunk : locationalResult)
		{

			if (lastChunk == null)
			{
				word = chunk.getText();  //Stores the word
				//sb.Append(chunk.text);
				//create a rectangle to get the cordinate
				com.itextpdf.text.Rectangle rect = new com.itextpdf.text.Rectangle(chunk.startLocation.get(Vector.I1),
						chunk.startLocation.get(Vector.I2), chunk.endLocation.get(Vector.I1),
						chunk.topLocation.get(Vector.I2));
				lastLeft = rect.getLeft();
				lastRight = rect.getRight();
				lastTop
						= rect.getTop();
				lastHeight = rect.getHeight();
				lastWidth = rect.getWidth();
				//sb.Append(rect.Left.ToString() + "," + rect.Top.ToString() + "," + rect.Width.toString() + "," + rect.Height.toString());
			}
			else //if get the space, thats means a new word
			if (chunk.isSameLine(lastChunk))
			{
				float dist = chunk.getDistanceFromEndOf(lastChunk);

				if (dist < -chunk.getCharSpaceWidth())
				{
					//sb.Append(' ');
					//sb.Append(word);// + " ");
					sb.append("word\n"); //+ "&nbsp;");
					word = "";
					lastLeft = 0;
					lastWidth = 0;
					lastTop = 0;
					lastHeight = 0;
					lastRight = 0;
				}
				// we only insert a blank space if the trailing character of the previous string wasn't a space, and the leading character of the current string isn't a space
				else if (dist > chunk.getCharSpaceWidth() / 2.0f
						&& !startsWithSpace(chunk.getText()) && !endsWithSpace(lastChunk.getText()))
				{
					//sb.Append(' ');
					sb.append("word\n"); //+ "&nbsp;");
					word = "";
					lastLeft = 0;
					lastWidth = 0;
					lastTop = 0;
					lastHeight = 0;
					lastRight = 0;
				}
				word += chunk.getText(); //if no space then it is same word
				//sb.Append(chunk.text);
				com.itextpdf.text.Rectangle rect = new com.itextpdf.text.Rectangle(chunk.startLocation.get(Vector.I1),
						chunk.startLocation.get(Vector.I2), chunk.endLocation.get(Vector.I1),
						chunk.topLocation.get(Vector.I2));
				lastWidth += rect.getWidth();//increase the width

			}
			else
			{
				//new line , starts from begining
				sb.append("word\n"); //+ "&nbsp;");
				word = "";
				word = chunk.getText();
				sb.append('\n');
				//sb.Append(chunk.text);
				com.itextpdf.text.Rectangle rect = new com.itextpdf.text.Rectangle(chunk.startLocation.get(Vector.I1),
						chunk.startLocation.get(Vector.I2), chunk.topLocation.get(Vector.I1),
						chunk.topLocation.get(Vector.I2));
				lastLeft = rect.getLeft();
				lastRight = rect.getRight();
				lastTop
						= rect.getTop();
				lastHeight = rect.getHeight();
				lastWidth = rect.getWidth();

			}
			lastChunk = chunk;
		}

		return sb.toString();

	}

	/**
	 * Used for debugging only
	 */
	private void dumpState()
	{
		for (TextChunk location : locationalResult)
		{

			location.printDiagnostics();

			Utilities.sopln();
		}

	}

	/**
	 *
	 * @see
	 * com.itextpdf.text.pdf.parser.RenderListener#renderText(com.itextpdf.text.pdf.parser.com.itextpdf.text.pdf.parser.TextRenderInfo)
	 */
	public void renderText(com.itextpdf.text.pdf.parser.TextRenderInfo renderInfo)
	{
		if (renderInfo.getText().indexOf(" ") < 0)
		{
			com.itextpdf.text.pdf.parser.LineSegment segment = renderInfo.getBaseline();
			com.itextpdf.text.pdf.parser.LineSegment dl = renderInfo.getDescentLine();
			com.itextpdf.text.pdf.parser.LineSegment al = renderInfo.getAscentLine();
			if (renderInfo.getRise() != 0)
			{ // remove the rise from the baseline - we do this because the text from a super/subscript render operations should probably be considered as part of the baseline of the text the super/sub is relative to
				Matrix riseOffsetTransform = new Matrix(0,
						-renderInfo.getRise());
				segment = segment.transformBy(riseOffsetTransform);
				al = al.transformBy(riseOffsetTransform);
				dl = dl.transformBy(riseOffsetTransform);
			}
			Vector topRight = al.getEndPoint();
			Vector bottomRight=dl.getStartPoint();



			TextChunk location = new TextChunk(renderInfo.getText(),
					segment.getStartPoint(), segment.getEndPoint(), topRight,bottomRight,
					renderInfo.getSingleSpaceWidth());
			locationalResult.add(location);
		}
		else
		{
			java.lang.String rawString = renderInfo.getText();

			java.util.List<com.itextpdf.text.pdf.parser.TextRenderInfo> criList = (java.util.List<com.itextpdf.text.pdf.parser.TextRenderInfo>) renderInfo.getCharacterRenderInfos();
			java.lang.StringBuffer sb = new java.lang.StringBuffer();
			Vector startLocation=null;
			Vector endlocation=null;
			Vector topRight=null;
			Vector bottomRight=null;
			com.itextpdf.text.pdf.parser.LineSegment segment=null;
			com.itextpdf.text.pdf.parser.LineSegment dl = null;
			com.itextpdf.text.pdf.parser.LineSegment al = null;
			for (com.itextpdf.text.pdf.parser.TextRenderInfo cri : criList)
			{
				segment = cri.getBaseline();
				dl = renderInfo.getDescentLine();
				al = renderInfo.getAscentLine();
				if (cri.getRise() != 0)
				{ // remove the rise from the baseline - we do this because the text from a super/subscript render operations should probably be considered as part of the baseline of the text the super/sub is relative to
					Matrix riseOffsetTransform = new Matrix(0,
							-cri.getRise());
					segment = segment.transformBy(riseOffsetTransform);
					al = al.transformBy(riseOffsetTransform);
					dl = dl.transformBy(riseOffsetTransform);
				}
				topRight = al.getEndPoint();
				bottomRight=dl.getStartPoint();



				if (cri.getText().equals(" "))
				{
					if (sb.length() > 0)
					{

endlocation=segment.getStartPoint();
TextChunk location = new TextChunk(sb.toString(),
							startLocation,endlocation, topRight,bottomRight,
							cri.getSingleSpaceWidth());
//location.printDiagnostics();
					locationalResult.add(location);
sb=new java.lang.StringBuffer();
					}
					TextChunk location = new TextChunk(cri.getText(),
							segment.getStartPoint(), segment.getEndPoint(), topRight,bottomRight,
							cri.getSingleSpaceWidth());
					locationalResult.add(location);
				}
				else
				{
					if(sb.length()<1)
						startLocation=segment.getStartPoint();
					sb.append(cri.getText());
				}
			}
			if(sb.length()>0)
			{
					TextChunk location = new TextChunk(sb.toString(),
							startLocation,segment.getEndPoint(), topRight,bottomRight,
							renderInfo.getSingleSpaceWidth());
					locationalResult.add(location);
				location.printDiagnostics();
			}
		}

	}

	/**
	 * Represents a chunk of text, it's orientation, and location relative to
	 * the orientation vector
	 */
	class TextChunk extends java.lang.Object implements Comparable<TextChunk>
	{
private int pageNo;
		/**
		 * the text of the chunk
		 */
		String text;
		/**
		 * the starting location of the chunk
		 */
		Vector startLocation;
		/**
		 * the ending location of the chunk
		 */
		Vector endLocation;
		/**
		 * the top location of the chunk
		 */
		Vector topLocation;
		Vector bottomLocation;
		/**
		 * unit vector in the orientation of the chunk
		 */
		Vector orientationVector;
		/**
		 * the orientation as a scalar for quick sorting
		 */
		int orientationMagnitude;
		/**
		 * perpendicular distance to the orientation unit vector (i.e. the Y
		 * position in an unrotated coordinate system) we round to the nearest
		 * integer to handle the fuzziness of comparing floats
		 */
		int distPerpendicular;
		/**
		 * distance of the start of the chunk parallel to the orientation unit
		 * vector (i.e. the X position in an unrotated coordinate system)
		 */
		float distParallelStart;
		/**
		 * distance of the end of the chunk parallel to the orientation unit
		 * vector (i.e. the X position in an unrotated coordinate system)
		 */
		float distParallelEnd;
		/**
		 * the width of a single space character in the font of the chunk
		 */
		float charSpaceWidth;

		public TextChunk(String str, Vector startLocation, Vector endLocation, Vector topLocation, Vector bottomLocation, float charSpaceWidth)
		{
			this.text = str;
			this.startLocation = startLocation;
			this.endLocation = endLocation;
			this.charSpaceWidth = charSpaceWidth;

			this.topLocation = topLocation;

			this.bottomLocation=bottomLocation;
			this.pageNo=pageNumber;

			Vector oVector = endLocation.subtract(startLocation);
			if (oVector.length() == 0)
			{
				oVector = new Vector(1, 0, 0);
			}
			orientationVector = oVector.normalize();
			orientationMagnitude
					= (int) (java.lang.Math.atan2(orientationVector.get(Vector.I2), orientationVector.get(Vector.I1))
					* 1000);

			// see http://mathworld.wolfram.com/Point-LineDistance2-Dimensional.html
			// the two vectors we are crossing are in the same plane, so the result will be purely
			// in the z-axis (out of plane) direction, so we just take the I3 component of the result
			Vector origin = new Vector(0, 0, 1);
			distPerpendicular
					= (int) (startLocation.subtract(origin)).cross(orientationVector).get(Vector.I3);

			distParallelStart = orientationVector.dot(startLocation);
			distParallelEnd = orientationVector.dot(endLocation);
		}
public com.itextpdf.text.Rectangle getBoundingBox()
{
	return new com.itextpdf.text.Rectangle(startLocation.get(Vector.I1),
						bottomLocation.get(Vector.I2), endLocation.get(Vector.I1),
						topLocation.get(Vector.I2));
}
		public float getCharSpaceWidth()
		{
			return charSpaceWidth;
		}

		public String getText()
		{
			return text;
		}

		public void printDiagnostics()
		{
			Utilities.sopln("Text (@" + startLocation + " -> "
					+ endLocation + "): " + text);
			Utilities.sopln("orientationMagnitude: "
					+ orientationMagnitude);
			Utilities.sopln("distPerpendicular: " + distPerpendicular);
			Utilities.sopln("distParallel: " + distParallelStart);
			Utilities.sopln("topLocation: "+topLocation);
			Utilities.sopln("bottomLocation: "+bottomLocation);
			Utilities.sopln("PageNumber: "+pageNo);
		}

		public int getPageNo()
		{
			return pageNo;
		}

		/**
		 * @param as the location to compare to
		 * @return true is this location is on the the same line as the other
		 */
		public boolean isSameLine(TextChunk a)
		{
			if (orientationMagnitude != a.orientationMagnitude)
			{
				return false;
			}
			if (distPerpendicular != a.distPerpendicular)
			{
				return false;
			}
			return true;
		}

		/**
		 * Computes the distance between the end of 'other' and the beginning of
		 * this chunk in the direction of this chunk's orientation vector. Note
		 * that it's a bad idea to call this for chunks that aren't on the same
		 * line and orientation, but we don't explicitly check for that
		 * condition for performance reasons.
		 *
		 * @param other
		 * @return the number of spaces between the end of 'other' and the
		 * beginning of this chunk
		 */
		public float getDistanceFromEndOf(TextChunk other)
		{
			float distance = distParallelStart - other.distParallelEnd;
			return distance;
		}

		/**
		 * Compares based on orientation, perpendicular distance, then parallel
		 * distance
		 *
		 * @see java.lang.Comparable#compareTo(java.lang.Object)
		 */
		public int compareTo(TextChunk rhs)
		{
			if (this == rhs)
			{
				return 0; // not really needed, but just in case
			}
			int rslt;
			rslt = java.lang.Integer.compare(orientationMagnitude,
					rhs.orientationMagnitude);
			if (rslt != 0)
			{
				return rslt;
			}

			rslt = java.lang.Integer.compare(distPerpendicular, rhs.distPerpendicular);
			if (rslt != 0)
			{
				return rslt;
			}

			// note: it's never safe to check floating point numbers for equality, and if two chunks
			// are truly right on top of each other, which one comes first or second just doesn't matter
			// so we arbitrarily choose this way.
			rslt = distParallelStart < rhs.distParallelStart ? -1 : 1;

			return rslt;
		}

	}

	/**
	 * no-op method - this renderer isn't interested in image events
	 *
	 * @see
	 * com.itextpdf.text.pdf.parser.RenderListener#renderImage(com.itextpdf.text.pdf.parser.ImageRenderInfo)
	 * @since 5.0.1
	 */
	public void renderImage(com.itextpdf.text.pdf.parser.ImageRenderInfo renderInfo)
	{
		// do nothing
	}
}
