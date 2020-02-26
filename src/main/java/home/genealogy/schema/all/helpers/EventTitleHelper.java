package home.genealogy.schema.all.helpers;

import home.genealogy.schema.all.EventTitle;
import home.genealogy.schema.all.Paragraph;

public class EventTitleHelper
{
	public static Paragraph getEventTitleParagraph(EventTitle eventTitle)
	{
		if (null != eventTitle)
		{
			return eventTitle.getParagraph();
		}
		return null;
	}
}
