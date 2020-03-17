package home.genealogy.schema.all.helpers;

import java.util.List;

import home.genealogy.schema.all.Date;
import home.genealogy.schema.all.DateOr;
import home.genealogy.util.StringUtil;

public class DateOrHelper
{
	public static String getDateOr(DateOr dateOr)
	{
		StringBuilder sb = new StringBuilder();
		if ((null != dateOr) &&
			(null != dateOr.getDate()) &&
			(!dateOr.getDate().isEmpty()))
		{
			sb.append(" [or ");
			List<Date> lDateOrs = dateOr.getDate();
			for (Date or : lDateOrs)
			{
				String strDate = DateHelper.getDate(or);
				if (StringUtil.exists(strDate))
				{
					sb.append(strDate).append("; ");
				}
			}
			sb.append("]");
		}
		return sb.toString();
	}
	
	public static boolean isEmpty(DateOr dateOr)
	{
		if ((null != dateOr) &&
			(null != dateOr.getDate()) &&
			(!dateOr.getDate().isEmpty()))
		{
			List<Date> lDateOrs = dateOr.getDate();
			for (Date or : lDateOrs)
			{
				if (!DateHelper.isEmpty(or))
				{
					return false;
				}
			}
		}
		return true;
	}
	
	public static int getOldestYear(DateOr dateOr, int iRelativeToYear)
	{
		int iYear = iRelativeToYear;
		if ((null != dateOr) &&
			(null != dateOr.getDate()) &&
			(!dateOr.getDate().isEmpty()))
		{
			List<Date> lDateOrs = dateOr.getDate();
			for (Date or : lDateOrs)
			{
				String strYear = or.getYear();
				if (StringUtil.exists(strYear))
				{
					try
					{
						int iCandidate = Integer.parseInt(strYear);
						if (iCandidate < iYear)
						{
							iYear = iCandidate;
						}
					}
					catch (NumberFormatException nfe)
					{
						// Ignore
					}
				}
			}
		}
		return iYear;
	}
}
