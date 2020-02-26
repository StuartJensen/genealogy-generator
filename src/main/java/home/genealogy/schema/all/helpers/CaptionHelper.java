package home.genealogy.schema.all.helpers;

import home.genealogy.schema.all.Caption;
import home.genealogy.schema.all.Paragraph;

import java.util.List;

public class CaptionHelper
{
	public static boolean isEmpty(Caption caption)
	{
		if (null != caption)
		{
			return (0 == caption.getParagraph().size());
		}
		return true;
	}
	
	public static int getParagraphCount(Caption caption)
	{
		if (null != caption)
		{
			 List<Paragraph> lParagraphs = caption.getParagraph();
			 return lParagraphs.size();
		}
		return 0;
	}
	
	public static Paragraph getParagraph(Caption caption, int iNth)
	{
		if (null != caption)
		{
			List<Paragraph> lParagraphs = caption.getParagraph();
			 if (lParagraphs.size() > iNth)
			 {
				 return lParagraphs.get(iNth);
			 }
		}
		return null;
	}
	
	public static List<Paragraph> getParagraphs(Caption caption)
	{
		if (null != caption)
		{
			 return caption.getParagraph();
		}
		return null;
	}
}
