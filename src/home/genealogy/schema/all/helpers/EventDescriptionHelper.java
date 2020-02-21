package home.genealogy.schema.all.helpers;

import home.genealogy.schema.all.EventDescription;
import home.genealogy.schema.all.Paragraph;

import java.util.List;

public class EventDescriptionHelper
{
	public static int getEventDescriptionParagraphCount(EventDescription eventDescription)
	{
		if (null != eventDescription)
		{
			List<Paragraph> lParas = eventDescription.getParagraph();
			if (null != lParas)
			{
				return lParas.size();
			}
		}
		return 0;
	}
	
	public static Paragraph getEventDescriptionParagraph(EventDescription eventDescription, int iNth)
	{
		if (null != eventDescription)
		{
			List<Paragraph> lParas = eventDescription.getParagraph();
			if ((null != lParas) && (iNth < lParas.size()))
			{
				return lParas.get(iNth);
			}
		}
		return null;
	}
}
