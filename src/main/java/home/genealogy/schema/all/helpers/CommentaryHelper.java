package home.genealogy.schema.all.helpers;

import home.genealogy.schema.all.Caption;
import home.genealogy.schema.all.Commentary;
import home.genealogy.schema.all.Paragraph;

import java.util.List;

public class CommentaryHelper
{
	public static boolean isEmpty(Commentary commentary)
	{
		if (null != commentary)
		{
			return (0 == commentary.getParagraph().size());
		}
		return true;
	}
	
	public static int getParagraphCount(Commentary commentary)
	{
		if (null != commentary)
		{
			 List<Paragraph> lParagraphs = commentary.getParagraph();
			 return lParagraphs.size();
		}
		return 0;
	}
	
	public static Paragraph getParagraph(Commentary commentary, int iNth)
	{
		if (null != commentary)
		{
			List<Paragraph> lParagraphs = commentary.getParagraph();
			 if (lParagraphs.size() > iNth)
			 {
				 return lParagraphs.get(iNth);
			 }
		}
		return null;
	}
	
	public static List<Paragraph> getParagraphs(Commentary commentary)
	{
		if (null != commentary)
		{
			 return commentary.getParagraph();
		}
		return null;
	}
}
