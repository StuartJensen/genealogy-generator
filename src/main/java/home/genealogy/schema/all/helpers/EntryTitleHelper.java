package home.genealogy.schema.all.helpers;

import home.genealogy.schema.all.EntryTitle;
import home.genealogy.schema.all.Paragraph;

import java.util.List;

public class EntryTitleHelper
{
	public static int getParagraphCount(EntryTitle entryTitle)
	{
		if (null != entryTitle)
		{
			List<Paragraph> lParagraphs = entryTitle.getParagraph();
			if (null != lParagraphs)
			{
				return lParagraphs.size();
			}
		}
		return 0;
	}
	
	public static Paragraph getParagraph(EntryTitle entryTitle, int iNth)
	{
		if (null != entryTitle)
		{
			List<Paragraph> lParagraphs = entryTitle.getParagraph();
			if ((null != lParagraphs) && (iNth < lParagraphs.size()))
			{
				return lParagraphs.get(iNth);
			}
		}
		return null;
	}
}
