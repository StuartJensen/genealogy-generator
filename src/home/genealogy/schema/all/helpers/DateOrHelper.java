package home.genealogy.schema.all.helpers;

import home.genealogy.schema.all.Date;
import home.genealogy.schema.all.DateOr;

public class DateOrHelper
{
	public static String getDateOr(DateOr dateOr)
	{
		Date date = dateOr.getDate();
		if (null == date)
		{
			return "";
		}
		return DateHelper.getDate(date);
	}

}
