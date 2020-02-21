package home.genealogy.schema.all.helpers;

import home.genealogy.schema.all.Title;

public class TitleHelper
{
	public static String getTitle(Title title)
	{
		if (null == title)
		{
			return "";
		}
		String strTitle = title.getName();
		if (null == strTitle)
		{
			return "";
		}
		return strTitle;
	}
}
