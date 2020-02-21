package home.genealogy.schema.all.helpers;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBElement;
import javax.xml.namespace.QName;

import home.genealogy.schema.all.Entry;
import home.genealogy.schema.all.Marriage;
import home.genealogy.schema.all.MarriageId;
import home.genealogy.schema.all.Paragraph;
import home.genealogy.schema.all.Person;
import home.genealogy.schema.all.PersonId;
import home.genealogy.schema.all.PersonTag;
import home.genealogy.schema.all.Photo;
import home.genealogy.schema.all.PhotoId;
import home.genealogy.schema.all.Reference;
import home.genealogy.schema.all.ReferenceEntryId;
import home.genealogy.schema.all.ReferenceId;
import home.genealogy.schema.all.Space;
import home.genealogy.schema.all.Tag;
import home.genealogy.schema.all.TagGroup;
import home.genealogy.schema.all.helpers.PhotoIdHelper.ePhotoIdType;

public class ParagraphHelper
{
	private static final String PARAGRAPH_LINE_END_NONE = "None";
	private static final String PARAGRAPH_LINE_END_HARD_RETURN = "HdRet";
	
	public enum eParagraphLineEnd
	{
		eNone,
		eHardReturn
	};
	
	public static List<Object> getContent(Paragraph paragraph)
	{
		List<Object> lContent = paragraph.getContent();
		return lContent;
	}
	
	public static boolean getIndent(Paragraph paragraph)
	{
		String strIndent = paragraph.getIndent();
		if (null != strIndent)
		{
			if ((strIndent.equalsIgnoreCase(Boolean.TRUE.toString())) ||
				(strIndent.equalsIgnoreCase("Yes")))
			{
				return true;
			}
		}
		return false;
	}
	
	public static eParagraphLineEnd getLineEnd(Paragraph paragraph)
	{
		String strLineEnd = paragraph.getLineEnd();
		if ((null != strLineEnd) && (strLineEnd.equalsIgnoreCase(PARAGRAPH_LINE_END_NONE)))
		{
			return eParagraphLineEnd.eNone;
		}
		
		return eParagraphLineEnd.eHardReturn;
	}
	
	public static List<Tag> getTagList(Paragraph paragraph, List<Tag> lTags)
	{
		if (null == lTags)
		{
			lTags = new ArrayList<Tag>();
		}
		if (null != paragraph)
		{
			List<Object> alParagraph = ParagraphHelper.getContent(paragraph);
			for(int i=0; i<alParagraph.size(); i++)
			{
				Object oParagraph = alParagraph.get(i);
				if (oParagraph instanceof Tag)
				{
					Tag tag = (Tag)oParagraph;
					lTags.add(tag);
				}
			}
		}
		return lTags;
	}
	
}
