package home.genealogy.schema.all.helpers;

import home.genealogy.schema.all.Column;
import home.genealogy.schema.all.Paragraph;

import java.util.List;

public class ColumnHelper
{
	public static int getParagraphCount(Column column)
	{
		if (null != column)
		{
			List<Paragraph> lParagraphs = column.getParagraph();
			if (null != lParagraphs)
			{
				return lParagraphs.size();
			}
		}
		return 0;
	}
	
    public static Paragraph getParagraph(Column column, int iNth)
    {
		if (null != column)
		{
			List<Paragraph> lParagraphs = column.getParagraph();
			if (null != lParagraphs)
			{			
				return lParagraphs.get(iNth);
			}
		}
		return null;
    }
}
